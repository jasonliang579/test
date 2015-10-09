package com.jieyangjiancai.zwj.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.common.FileUtils;
import com.jieyangjiancai.zwj.common.ImageUtils;
import com.jieyangjiancai.zwj.common.StringUtils;
import com.jieyangjiancai.zwj.common.ToastMessage;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.URLs;
import com.jieyangjiancai.zwj.network.entity.CardId;

public class SendMessageActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout mLayoutProgress;
	
	private ArrayList<File> mFiles = new ArrayList<File>();
	private ArrayList<String> mPhotoArray = new ArrayList<String>();
	private int mPhotoCount;
	private EditText mEditMark;
	private ImageView mSelectPhoto;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sendmessage);
        
		Init();
	}

	
	private void Init()
	{
		findViewById(R.id.title_bar_back).setOnClickListener(this);
		findViewById(R.id.image_select_picture1).setOnClickListener(this);
		findViewById(R.id.btn_send_message).setOnClickListener(this);
		
		TextView title = (TextView) findViewById(R.id.title_bar_text);
		title.setText("发布新消息");
		
		mEditMark = (EditText) findViewById(R.id.edt_remark);
		mSelectPhoto = (ImageView)findViewById(R.id.image_select_picture1);
		
		mLayoutProgress = (RelativeLayout) findViewById(R.id.layout_progress_bar);
		mLayoutProgress.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.title_bar_back:
			finish();
			break;
			
		case R.id.image_select_picture1:
			ConfigUtil.doPickPhotoAction(this);
			break;
			
		case R.id.btn_send_message:
			SendMessage();
//			setResult(RESULT_OK);
//			finish();
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {

			switch (requestCode) {
			case ConfigUtil.PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的

				if (data == null || data.getData() == null) {
					ToastMessage.show(getApplicationContext(), "请尝试使用其他相册浏览!");
					return;
				}
				//ContentResolver resolver = getContentResolver();
				String selectedImagePath = null;
				String fullPath;
				Uri selectedImageUri = data.getData();
				if (selectedImageUri != null) {
					selectedImagePath = ImageUtils.getImagePath(selectedImageUri, this);
				}

				if (selectedImagePath != null) {
					fullPath = selectedImagePath;
				} else {
					fullPath = ConfigUtil.getPhotoPath();
				}
				Bitmap bitmap = ConfigUtil.getThumbnailBitmap(this, fullPath);
				if(bitmap != null){
					fullPath = ConfigUtil.getThumbFilePath();
				}else{
					return;
				}
				if (mFiles.size() == 0) {
					ImageView imageView = (ImageView) findViewById(R.id.image_content1);
					imageView.setImageBitmap(bitmap);
					imageView.setVisibility(View.VISIBLE);
					mSelectPhoto.setVisibility(View.GONE);
				} else if (mFiles.size() == 1) {
					ImageView imageView = (ImageView) findViewById(R.id.image_content2);
					imageView.setImageBitmap(bitmap);
					imageView.setVisibility(View.VISIBLE);
				} else if (mFiles.size() == 2) {
					ImageView imageView = (ImageView) findViewById(R.id.image_content3);
					imageView.setImageBitmap(bitmap);
					imageView.setVisibility(View.VISIBLE);
				} else
					return;

				File file = new File(fullPath);
				mFiles.add(file);
				Log.d("wujin", "path=" + fullPath);

				break;
			}
			case ConfigUtil.CAMERA_WITH_DATA: {// 照相机程序返回的,再次调用图片剪辑程序去修剪图片

				String fullPath = ConfigUtil.getPhotoPath();
				Bitmap bitmap = ConfigUtil.getThumbnailBitmap(this, fullPath);
				if(bitmap != null){
					fullPath = ConfigUtil.getThumbFilePath();
				}else{
					return;
				}
				if (mFiles.size() == 0) {
					ImageView imageView = (ImageView) findViewById(R.id.image_content1);
					imageView.setImageBitmap(bitmap);
					imageView.setVisibility(View.VISIBLE);
					mSelectPhoto.setVisibility(View.GONE);
				} else if (mFiles.size() == 1) {
					ImageView imageView = (ImageView) findViewById(R.id.image_content2);
					imageView.setImageBitmap(bitmap);
					imageView.setVisibility(View.VISIBLE);
				} else if (mFiles.size() == 2) {
					ImageView imageView = (ImageView) findViewById(R.id.image_content3);
					imageView.setImageBitmap(bitmap);
					imageView.setVisibility(View.VISIBLE);
				} else
					return;

				File file = new File(fullPath);
				mFiles.add(file);

				break;
			}
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}



	private void SendMessage() {
		if (mFiles.size() <= 0) {
			ToastMessage.show(this, "请上传图片。");
			return;
		}
		mLayoutProgress.setVisibility(View.VISIBLE);

		String userId = ConfigUtil.mUserId;
		String token = ConfigUtil.mToken;
		String type = "5"; //5-行业圈

		mPhotoArray.clear();
		mPhotoCount = mFiles.size();
		for (int i = 0; i < mFiles.size(); i++) {
			BackendDataApi bdApi = ((WJApplication) this.getApplicationContext()).getHttpRequest();
			bdApi.uploadImage(mFiles.get(i), userId, token, type, reqUploadSuccessListener(), reqUploadErrorListener());
		}
	}

	private Response.Listener<JSONObject> reqUploadSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				//ToastMessage.show(SendMessageActivity.this, "上传照片成功");
				try {
					CardId cardId = CardId.parse(response);

					String photoId = cardId.getPhotoId();
					if (mPhotoArray.contains(photoId))
						return;
					else
						mPhotoArray.add(photoId);

					mPhotoCount--;
					if (mPhotoCount == 0)
						UpdateMessage();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					mLayoutProgress.setVisibility(View.INVISIBLE);
					e.printStackTrace();
				}
			}
		};
	}

	private Response.ErrorListener reqUploadErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mLayoutProgress.setVisibility(View.INVISIBLE);
				ToastMessage.show(SendMessageActivity.this, "上传照片失败");
				Log.d("wujin", "uploadImage error=" + error.getMessage());
				//ToastMessage.show(MakeOrderActivity.this, error.getMessage());
			}
		};
	}


	private void UpdateMessage() {
		mLayoutProgress.setVisibility(View.VISIBLE);

		String userId = ConfigUtil.mUserId;
		String token = ConfigUtil.mToken;
		String photo_arr = "";
		for (int i = 0; i < mPhotoArray.size(); i++)
			photo_arr += mPhotoArray.get(i) + ",";
		String remark = mEditMark.getText().toString();
		BackendDataApi bdApi = ((WJApplication) this.getApplicationContext()).getHttpRequest();
		bdApi.SaveIndustryMessage(userId, token, remark, photo_arr, reqOrderSuccessListener(), reqOrderErrorListener());
	}

	private Response.Listener<JSONObject> reqOrderSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				//ToastMessage.show(SendMessageActivity.this, "发布新消息成功");
				setResult(RESULT_OK);
				finish();
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
				mLayoutProgress.setVisibility(View.INVISIBLE);
			}
		};
	}

	private Response.ErrorListener reqOrderErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mLayoutProgress.setVisibility(View.INVISIBLE);
				ToastMessage.show(SendMessageActivity.this, "发布新消息失败");
			}
		};
	}
	
}
