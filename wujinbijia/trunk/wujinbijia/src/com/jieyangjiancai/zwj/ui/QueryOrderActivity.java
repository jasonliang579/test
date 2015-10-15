package com.jieyangjiancai.zwj.ui;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.common.ImageUtils;
import com.jieyangjiancai.zwj.common.ToastMessage;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.CardId;
import com.jieyangjiancai.zwj.network.entity.UpdateUserInfo;
import com.jieyangjiancai.zwj.utils.AppUtil;

public class QueryOrderActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout mLayoutProgress;
	private EditText mEditMark;

	private ArrayList<File> mFiles = new ArrayList<File>();
	// private File mFile;
	// private String mPhotoId;
	// private String mPhotoArray;
	private ArrayList<String> mPhotoArray = new ArrayList<String>();
	private int mPhotoCount;
	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_queryorder);
		mContext = this;
		
		Init();
	}

	private void Init() {

		mLayoutProgress = (RelativeLayout) findViewById(R.id.layout_progress_bar);
		mLayoutProgress.setVisibility(View.INVISIBLE);

		// 返回
		findViewById(R.id.title_bar_back).setOnClickListener(this);

		TextView title = (TextView) findViewById(R.id.title_bar_text);
		title.setText("发布询单");
		ImageView more = (ImageView) findViewById(R.id.title_bar_more);
		more.setVisibility(View.GONE);
		TextView titleMore = (TextView) findViewById(R.id.title_bar_more_text);
		titleMore.setText("询单记录");
		titleMore.setOnClickListener(this);

		findViewById(R.id.btn_makephone).setOnClickListener(this);
		findViewById(R.id.img_add_photoorder).setOnClickListener(this);
		findViewById(R.id.btn_makeorder).setOnClickListener(this);
		findViewById(R.id.text_help).setOnClickListener(this);
		findViewById(R.id.image_content1).setOnClickListener(this);
		findViewById(R.id.image_content2).setOnClickListener(this);
		findViewById(R.id.image_content3).setOnClickListener(this);
		
		mEditMark = (EditText) findViewById(R.id.edit_mark);
//		mEditMark.clearFocus();
//		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.hideSoftInputFromWindow(mEditMark.getWindowToken(), 0);
		
		
		GetPensonInfo();
	}
	

	private void MakePhoneCall() {
		String phone = ConfigUtil.mPhone;
		Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
		startActivity(phoneIntent);
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
				} else if (mFiles.size() == 1) {
					ImageView imageView = (ImageView) findViewById(R.id.image_content2);
					imageView.setImageBitmap(bitmap);
				} else if (mFiles.size() == 2) {
					ImageView imageView = (ImageView) findViewById(R.id.image_content3);
					imageView.setImageBitmap(bitmap);
				} else
					return;

				File file = new File(fullPath);
				mFiles.add(file);

				break;
			}
			case ConfigUtil.CAMERA_WITH_DATA: {// 照相机程序返回

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
				} else if (mFiles.size() == 1) {
					ImageView imageView = (ImageView) findViewById(R.id.image_content2);
					imageView.setImageBitmap(bitmap);
				} else if (mFiles.size() == 2) {
					ImageView imageView = (ImageView) findViewById(R.id.image_content3);
					imageView.setImageBitmap(bitmap);
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



	// 提交订单，先上传照片
	private void MakeOrder() {
		if (mFiles.size() <= 0) {
			ToastMessage.show(this, "请上传产品清单照片。");
			return;
		}
		mLayoutProgress.setVisibility(View.VISIBLE);

		String userId = ConfigUtil.mUserId;
		String token = ConfigUtil.mToken;
		String type = "2";

		mPhotoArray.clear();
//		mPhotoCount = mFiles.size();
//		for (int i = 0; i < mFiles.size(); i++) {
//			BackendDataApi bdApi = ((WJApplication) this.getApplicationContext()).getHttpRequest();
//			bdApi.uploadImage(mFiles.get(i), userId, token, type, reqUploadSuccessListener(), reqUploadErrorListener());
//		}
		mPhotoCount = 0;
		BackendDataApi bdApi = ((WJApplication) this.getApplicationContext()).getHttpRequest();
		bdApi.uploadImage(mFiles.get(mPhotoCount), userId, token, type, reqUploadSuccessListener(), reqUploadErrorListener());
		AppUtil.sendUmengOnEvent(this, "10001");
	}

	private Response.Listener<JSONObject> reqUploadSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// ToastMessage.show(MakeOrderActivity.this, "上传照片成功");
				try {
					CardId cardId = CardId.parse(response);

					if(cardId.getError() != 0)
					{
						mLayoutProgress.setVisibility(View.INVISIBLE);
						ToastMessage.show(QueryOrderActivity.this, "上传照片失败");
						return;
					}
					
					String photoId = cardId.getPhotoId();
					if (mPhotoArray.contains(photoId))
						return;
					else
						mPhotoArray.add(photoId);

					Log.d("wujin", "uploadImage success, mPhotoCount=" + mPhotoCount);
					mPhotoCount++;
					if (mPhotoCount > 0 && mPhotoCount < mFiles.size())
					{
						Thread.sleep(300);
						String userId = ConfigUtil.mUserId;
						String token = ConfigUtil.mToken;
						String type = "2";
						BackendDataApi bdApi = ((WJApplication) getApplicationContext()).getHttpRequest();
						bdApi.uploadImage(mFiles.get(mPhotoCount), userId, token, type, reqUploadSuccessListener(), reqUploadErrorListener());
					}
					else if (mPhotoCount >= mFiles.size())
					{
						MakeQueryOrder();
					}
//					mPhotoCount--;
//					if (mPhotoCount == 0)
//					{
//						MakeQueryOrder();
//					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
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
				ToastMessage.show(QueryOrderActivity.this, "上传照片失败");
				Log.d("wujin", "uploadImage error=" + error.getMessage());
				 //ToastMessage.show(MakeOrderActivity.this, error.getMessage());
			}
		};
	}

	// 提交订单
	private void MakeQueryOrder() {
		mLayoutProgress.setVisibility(View.VISIBLE);

		String userId = ConfigUtil.mUserId;
		String token = ConfigUtil.mToken;
		String photo_arr = "";
		for (int i = 0; i < mPhotoArray.size(); i++)
			photo_arr += mPhotoArray.get(i) + ",";
		String remark = mEditMark.getText().toString();
		BackendDataApi bdApi = ((WJApplication) this.getApplicationContext()).getHttpRequest();
		bdApi.photoOrder(userId, token, photo_arr, remark, reqOrderSuccessListener(), reqOrderErrorListener());
	}

	private Response.Listener<JSONObject> reqOrderSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				ToastMessage.show(QueryOrderActivity.this, "提交订单成功");
				InitData();
				//finish();
				try {
				} catch (Exception e) {
					// TODO Auto-generated catch block
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
				String t = error.getMessage();
				if (t != null)
					Log.d("wujin", "errormessage="+t);
				mLayoutProgress.setVisibility(View.INVISIBLE);
				ToastMessage.show(QueryOrderActivity.this, "提交订单失败");
			}
		};
	}
	
	private void InitData()
	{
		mFiles.clear();
		mPhotoArray.clear();
		//mEditMark.setText("报价当天有效，超过时间请再次确认");
		UpdateUserInfo userInfo = ConfigUtil.mUserInfo;
		String address = userInfo.getProvinceName()+userInfo.getCityName()+userInfo.getAreaName();
		mEditMark.setText("接受订做，发货到"+address);
		ImageView imageView = (ImageView) findViewById(R.id.image_content1);
		imageView.setImageBitmap(null);
		imageView = (ImageView) findViewById(R.id.image_content2);
		imageView.setImageBitmap(null);
		imageView = (ImageView) findViewById(R.id.image_content3);
		imageView.setImageBitmap(null);
	}
	
	//获取用户信息
	private void GetPensonInfo()
	{
//		if (ConfigUtil.mUserInfo == null)
		{
			BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
			bdApi.userInfo(ConfigUtil.mUserId, ConfigUtil.mToken, reqPensonInfoSuccessListener(), reqPensonInfoErrorListener());
		}
//		else
//		{
//			UpdateUserInfo userInfo = ConfigUtil.mUserInfo;
//			String address = userInfo.getProvinceName()+userInfo.getCityName()+userInfo.getAreaName();
//			mEditMark.setText("接受订做，发货到"+address);
//		}
	}
	private Response.Listener<JSONObject> reqPensonInfoSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String error = response.getString("error");
					if("0".equals(error)){
						ConfigUtil.mUserInfo = UpdateUserInfo.parse(response);
						ConfigUtil.mUserName = ConfigUtil.mUserInfo.getUserName();
						String address = ConfigUtil.mUserInfo.getProvinceName() + 
								ConfigUtil.mUserInfo.getCityName() +
								ConfigUtil.mUserInfo.getAreaName();
						mEditMark.setText("接受订做，发货到"+address);
					}
					else if("2".equals(error)){
						//token 过期
						ToastMessage.show(mContext, response.getString("errormsg"));
						if(mContext != null){
							ConfigUtil.setLoginInfo("", "", "", 0);
							finish();
						}
					}
					else{
						if(mContext != null){
							ToastMessage.show(mContext, response.getString("errormsg"));
							finish();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				mLayoutProgress.setVisibility(View.INVISIBLE);
			}
		};
	}
	private Response.ErrorListener reqPensonInfoErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mLayoutProgress.setVisibility(View.INVISIBLE);
			}
		};
	}
	
	
	private View mSelectedView;
	private void ShowSelectDialog(Context context, View v) {  
		mSelectedView = v;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);  
        builder.setTitle("提示");  
        builder.setMessage("删除该图片吗？");  
        builder.setPositiveButton("确定",  
            new DialogInterface.OnClickListener() {  
                public void onClick(DialogInterface dialog, int whichButton) {  
                	DeletePhoto(mSelectedView);
                }  
            });  
        builder.setNegativeButton("取消",  
            new DialogInterface.OnClickListener() {  
                public void onClick(DialogInterface dialog, int whichButton) {  
                }  
            });  
        builder.show();  
    }  
	
	private void DeletePhoto(View v)
	{
		switch (v.getId()) {
		case R.id.image_content1:
		{
			ImageView imageView = (ImageView) v;
			imageView.setImageBitmap(null);
			
			ImageView imageView2 = (ImageView) findViewById(R.id.image_content2);
			Drawable drawable2 = imageView2.getDrawable();
			if(drawable2 != null)
			{
				imageView.setImageDrawable(drawable2);
				imageView2.setImageBitmap(null);
			}
			
			ImageView imageView3 = (ImageView) findViewById(R.id.image_content3);
			Drawable drawable3 = imageView3.getDrawable();
			if(drawable3 != null)
			{
				imageView2.setImageDrawable(drawable3);
				imageView3.setImageBitmap(null);
			}
			if(mFiles.size() > 0)
				mFiles.remove(0);
			break;
		}
		case R.id.image_content2:
		{
			ImageView imageView = (ImageView) v;
			
			ImageView imageView3 = (ImageView) findViewById(R.id.image_content3);
			Drawable drawable3 = imageView3.getDrawable();
			if(drawable3 != null)
			{
				imageView.setImageDrawable(drawable3);
				imageView3.setImageBitmap(null);
			}
			else
				imageView.setImageBitmap(null);
			if(mFiles.size() > 1)
				mFiles.remove(1);
			break;
		}
		case R.id.image_content3:
		{
			ImageView imageView = (ImageView) v;
			imageView.setImageBitmap(null);
			if(mFiles.size() > 2)
				mFiles.remove(2);
			break;
		}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_bar_back:
			finish();
			break;

		case R.id.title_bar_more_text: // 询单记录
		{
			AppUtil.sendUmengOnEvent(this, "10002");
			Intent intent = new Intent(this, QueryOrderHistoryActivity.class);
			this.startActivity(intent);
			break;
		}

		case R.id.btn_makephone: // 电话下单
			MakePhoneCall();
			break;

		case R.id.img_add_photoorder:
			// SelectImage();
			ConfigUtil.doPickPhotoAction(this);
			break;

		case R.id.btn_makeorder:
			MakeOrder();
			break;
			
		case R.id.text_help:
		{
			Intent intent = new Intent(this, HelpActivity.class);
			this.startActivity(intent);
			break;
		}
		
		case R.id.image_content1:
		{
			ShowSelectDialog(this, v);
			break;
		}
		case R.id.image_content2:
		{
			ShowSelectDialog(this, v);
			break;
		}
		case R.id.image_content3:
		{
			ShowSelectDialog(this, v);
			break;
		}
		
		}
		
	}

}
