package com.jieyangjiancai.zwj.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.common.FileUtils;
import com.jieyangjiancai.zwj.common.ImageUtils;
import com.jieyangjiancai.zwj.common.StringUtils;
import com.jieyangjiancai.zwj.common.ToastMessage;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.CardId;
import com.jieyangjiancai.zwj.network.entity.UpdateUserInfo;
import com.jieyangjiancai.zwj.ui.views.ShowZoomView;
import com.jieyangjiancai.zwj.utils.AppUtil;

public class GetOrderMakePriceActivity extends BaseActivity implements OnClickListener {
	private ArrayList<ImageView> mImageList = new ArrayList<ImageView>();
//	private ImageView mImageViewFull;
	private String mOrderMessageId;
	private RelativeLayout mLayoutProgress;

	// private String mPhotoId;

	// private File mFile;
	private ArrayList<File> mFiles = new ArrayList<File>();
	private ArrayList<String> mPhotoArray = new ArrayList<String>();
	private int mPhotoCount;
	
	private EditText mEditDeliverPlace;
	private ShowZoomView mShowZoomView;
	
	private Button btn_my_offer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_getorder_offer);

		Init();
	}

	private void Init() {
		InitUI();
		InitData();
		mShowZoomView = new ShowZoomView(this);
	}

	private void InitUI() {
		findViewById(R.id.title_bar_back).setOnClickListener(this);

		TextView title = (TextView) findViewById(R.id.title_bar_text);
		title.setText("我要接单");

//		mImageViewFull = (ImageView) findViewById(R.id.image_full);
//		mImageViewFull.setVisibility(View.INVISIBLE);
//		mImageViewFull.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mImageViewFull.setVisibility(View.INVISIBLE);
//			}
//		});

		btn_my_offer = (Button)findViewById(R.id.btn_my_offer);
		btn_my_offer.setOnClickListener(this);
		findViewById(R.id.btn_phone).setOnClickListener(this);
		findViewById(R.id.image_price_content1).setOnClickListener(this);
		findViewById(R.id.image_price_content2).setOnClickListener(this);
		findViewById(R.id.image_price_content3).setOnClickListener(this);
		findViewById(R.id.text_help).setOnClickListener(this);
		
		mLayoutProgress = (RelativeLayout) findViewById(R.id.layout_progress_bar);
		mLayoutProgress.setVisibility(View.INVISIBLE);

		findViewById(R.id.image_select_picture1).setOnClickListener(this);
		
		mEditDeliverPlace = (EditText) findViewById(R.id.edit_deliver_place);
	}

	private void InitData() {
		Intent intent = getIntent();
		String order_status = intent.getStringExtra("order_status");
		String show_time = intent.getStringExtra("show_time");
		String order_id = intent.getStringExtra("order_id");
		String content = intent.getStringExtra("content");
		String order_status_content = intent.getStringExtra("order_status_content");
		String picturepath1 = intent.getStringExtra("picture1");
		String picturepath2 = intent.getStringExtra("picture2");
		String picturepath3 = intent.getStringExtra("picture3");
		String thumbpic1 = intent.getStringExtra("thumbpic1");
		String thumbpic2 = intent.getStringExtra("thumbpic2");
		String thumbpic3 = intent.getStringExtra("thumbpic3");
		// intent.putExtra("picture1", item.picture_arr.get(0).path);
		mOrderMessageId = intent.getStringExtra("order_message_id");

		// View view = inflate.inflate(R.layout.getorder_item, null);
		TextView textView = (TextView) findViewById(R.id.text_status);
		textView.setText(ConfigUtil.getStatus(order_status));

		textView = (TextView) findViewById(R.id.text_time);
		textView.setText(ConfigUtil.getFormatedDateTime(Long.valueOf(show_time)));

		textView = (TextView) findViewById(R.id.text_order_id);
		textView.setText(order_id);

		ImageView imageView = (ImageView) findViewById(R.id.image_content1);
		LoadImage(imageView, thumbpic1, picturepath1);
		imageView = (ImageView) findViewById(R.id.image_content2);
		LoadImage(imageView, thumbpic2, picturepath2);
		imageView = (ImageView) findViewById(R.id.image_content3);
		LoadImage(imageView, thumbpic3, picturepath3);

		textView = (TextView) findViewById(R.id.text_remark);
		textView.setText(content);

		textView = (TextView) findViewById(R.id.text_status_content);
		textView.setText(order_status_content);
		if (order_status_content == null || order_status_content.equals(""))
			findViewById(R.id.layout_status_content).setVisibility(View.GONE);
		else
			findViewById(R.id.layout_status_content).setVisibility(View.VISIBLE);
		
		
		//获取用户信息
		GetPensonInfo();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_bar_back:
			finish();
			break;

		case R.id.btn_my_offer:
		    btn_my_offer.setEnabled(false);
			mLayoutProgress.setVisibility(View.VISIBLE);
			// 上传报价明细
			UploadPriceDetail();
			break;

		case R.id.btn_phone:
			MakePhoneCall();
			break;

		case R.id.image_select_picture1:
			ConfigUtil.doPickPhotoAction(this , 3);
			// SelectImage();
			break;
			
		case R.id.image_price_content1:
		{
			ShowSelectDialog(this, v);
			break;
		}
		case R.id.image_price_content2:
		{
			ShowSelectDialog(this, v);
			break;
		}
		case R.id.image_price_content3:
		{
			ShowSelectDialog(this, v);
			break;
		}
		case R.id.text_help:
			Intent intent = new Intent(this, GetOrderHelpActivity.class);
			this.startActivity(intent);
			break;
		}
	}

	private void LoadImage(ImageView imageView, String thumbUrl, String url) 
	{
		String loadUrl = thumbUrl;
		if(thumbUrl == null || thumbUrl.equals(""))
		{
			if(url == null || url.equals(""))
				return;
			else
				loadUrl = url;
		}
		
		imageView.setTag(loadUrl);
		imageView.setTag(R.id.tag_fullpath, url);
		mImageList.add(imageView);
		
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.ImageLoad(loadUrl, reqImageListener());
	}

	private ImageLoader.ImageListener reqImageListener() {
		return new ImageLoader.ImageListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onResponse(ImageContainer arg0, boolean arg1) {
				// TODO Auto-generated method stub
				for (int i = 0; i < mImageList.size(); i++) {
					ImageView imageView = mImageList.get(i);
					String url = (String) imageView.getTag();
					if (url.equals(arg0.getRequestUrl())) {
						imageView.setImageBitmap(arg0.getBitmap());
						imageView.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// ToastMessage.show(GetOrderActivity.this,
								// "显示图片");
								ImageView imageView = (ImageView) v;
//								mImageViewFull.setImageDrawable(imageView.getDrawable());
//								mImageViewFull.setVisibility(View.VISIBLE);
								String url = (String)v.getTag(R.id.tag_fullpath);
								mShowZoomView.ShowZoomImageView(imageView.getDrawable(), url);
							}
						});
						break;
					}
				}
			}
		};
	}

	private void MakePhoneCall() {
		String phone = ConfigUtil.mPhone;
		Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
		startActivity(phoneIntent);
	}

	// 上传报价明细
	private void UploadPriceDetail() {
		
		String price = ((EditText) findViewById(R.id.edit_my_price)).getText().toString();
		if(price.equals("") || price.equals("0")){//先判断价格
			ToastMessage.show(this, "请输入有效的价格");
			mLayoutProgress.setVisibility(View.INVISIBLE);
			return;
		}
		
		try {
			Double.parseDouble(price);//强转 ，失败的话，代表用户输入错误 
		} catch (Exception e) {
			ToastMessage.show(this, "请输入有效的价格");
			mLayoutProgress.setVisibility(View.INVISIBLE);
			return;
		}
		
		if (mFiles.size() <= 0) {
			ToastMessage.show(this, "请上传报价明细照片。");
			mLayoutProgress.setVisibility(View.INVISIBLE);
			return;
		}

		mLayoutProgress.setVisibility(View.VISIBLE);

		String userId = ConfigUtil.mUserId;
		String token = ConfigUtil.mToken;
		String type = "4"; // 4-上传报价明细

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
				// ToastMessage.show(GetOrderMakePriceActivity.this, "上传照片成功");
				try {
					CardId cardId = CardId.parse(response);
					String photoId = cardId.getPhotoId();
					if (mPhotoArray.contains(photoId))
						return;
					else
						mPhotoArray.add(photoId);

					mPhotoCount--;
					if (mPhotoCount == 0) {
						EditText edit = (EditText) findViewById(R.id.edit_my_price);
						EditText editRemark = (EditText) findViewById(R.id.edit_remark);
						//mEditDeliverPlace = (EditText) findViewById(R.id.edit_deliver_place);
						
						String price = getSubDian(edit.getText().toString());
						String remark = editRemark.getText().toString();
						String user_name = ConfigUtil.mUserName;
						String order_message_id = mOrderMessageId;
						String photo_arr = "";
						for (int i = 0; i < mPhotoArray.size(); i++)
							photo_arr += mPhotoArray.get(i) + ",";

						String deliver_place = mEditDeliverPlace.getText().toString();

						MakeMyPrice(price, remark, user_name, order_message_id, photo_arr, deliver_place);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	/**
	 * 返回小数点后两位
	 * @param s
	 * @return
	 */
	public String getSubDian(String s){
		int index = s.indexOf(".");
		int len = s.length() - index;
		return s.substring(0, index == -1 ? s.length() : (len) > 2 ? index + 3 : index + (len));
	}
	private Response.ErrorListener reqUploadErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mLayoutProgress.setVisibility(View.INVISIBLE);
				ToastMessage.show(GetOrderMakePriceActivity.this, "上传照片失败");
				btn_my_offer.setEnabled(true);
			}
		};
	}

	private void MakeMyPrice(String price, String remark, String user_name, String order_message_id, String picture_arr,
			String deliver_place) {
		String userId = ConfigUtil.mUserId;
		String token = ConfigUtil.mToken;

		BackendDataApi bdApi = ((WJApplication) this.getApplicationContext()).getHttpRequest();
		bdApi.MakeMyOrder(userId, token, price, remark, user_name, order_message_id, picture_arr, deliver_place, reqOrderSuccessListener(),
				reqOrderErrorListener());
	}

	private Response.Listener<JSONObject> reqOrderSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					ToastMessage.show(GetOrderMakePriceActivity.this, "发布报价成功");
					Finish();
					
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
				mLayoutProgress.setVisibility(View.INVISIBLE);
				ToastMessage.show(GetOrderMakePriceActivity.this, "发布报价失败");
				btn_my_offer.setEnabled(true);
			}
		};
	}

	private void Finish()
	{
		this.setResult(Activity.RESULT_OK);
		finish();
	}
	
	//获取用户信息
	private void GetPensonInfo()
	{
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.userInfo(ConfigUtil.mUserId, ConfigUtil.mToken, reqPensonInfoSuccessListener(), reqPensonInfoErrorListener());
	}
	private Response.Listener<JSONObject> reqPensonInfoSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					//ToastMessage.show(MyInfoActivity.this, response.toString());
					ConfigUtil.mUserInfo = UpdateUserInfo.parse(response);
					ConfigUtil.mUserName = ConfigUtil.mUserInfo.getUserName();
					String address = ConfigUtil.mUserInfo.getProvinceName() + 
							ConfigUtil.mUserInfo.getCityName() +
							ConfigUtil.mUserInfo.getAreaName();
					mEditDeliverPlace.setText(address);
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
				//String t = error.getMessage();
				//ToastMessage.show(MyInfoActivity.this, t);
			}
		};
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {

			switch (requestCode) {
			case ConfigUtil.PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的
				/*if (data == null || data.getData() == null) {
					ToastMessage.show(getApplicationContext(), "请尝试使用其他相册浏览!");
					return;
				}
				ContentResolver resolver = getContentResolver();
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
					ImageView imageView = (ImageView) findViewById(R.id.image_price_content1);
					imageView.setImageBitmap(bitmap);
					imageView.setVisibility(View.VISIBLE);
				} else if (mFiles.size() == 1) {
					ImageView imageView = (ImageView) findViewById(R.id.image_price_content2);
					imageView.setImageBitmap(bitmap);
					imageView.setVisibility(View.VISIBLE);
				} else if (mFiles.size() == 2) {
					ImageView imageView = (ImageView) findViewById(R.id.image_price_content3);
					imageView.setImageBitmap(bitmap);
					imageView.setVisibility(View.VISIBLE);
				} else
					return;

				File file = new File(fullPath);
				mFiles.add(file);
				Log.d("wujin", "path=" + fullPath);*/
				mFiles = ConfigUtil.getOnActivityResultPaths(this, data, mFiles);
				break;
			}
			case ConfigUtil.CAMERA_WITH_DATA: {// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
				Bitmap bitmap = ConfigUtil.getThumbnailBitmap(this, ConfigUtil.getPhotoPath());
				String fullPath;
				if(bitmap != null){
					fullPath = ConfigUtil.getThumbFilePath();
				}else{
					return;
				}
				if (mFiles.size() == 0) {
					ImageView imageView = (ImageView) findViewById(R.id.image_price_content1);
					imageView.setImageBitmap(bitmap);
					imageView.setVisibility(View.VISIBLE);
				} else if (mFiles.size() == 1) {
					ImageView imageView = (ImageView) findViewById(R.id.image_price_content2);
					imageView.setImageBitmap(bitmap);
					imageView.setVisibility(View.VISIBLE);
				} else if (mFiles.size() == 2) {
					ImageView imageView = (ImageView) findViewById(R.id.image_price_content3);
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
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mShowZoomView.onKeyDown(keyCode, event) == true)
			return true;
		return super.onKeyDown(keyCode, event);
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
		case R.id.image_price_content1:
		{
			ImageView imageView = (ImageView) v;
			imageView.setImageBitmap(null);
			imageView.setVisibility(View.GONE);
			
			ImageView imageView2 = (ImageView) findViewById(R.id.image_content2);
			Drawable drawable2 = imageView2.getDrawable();
			if(drawable2 != null)
			{
				imageView.setImageDrawable(drawable2);
				imageView.setVisibility(View.VISIBLE);
				imageView2.setImageBitmap(null);
				imageView2.setVisibility(View.GONE);
			}
			
			ImageView imageView3 = (ImageView) findViewById(R.id.image_content3);
			Drawable drawable3 = imageView3.getDrawable();
			if(drawable3 != null)
			{
				imageView2.setImageDrawable(drawable3);
				imageView2.setVisibility(View.VISIBLE);
				imageView3.setImageBitmap(null);
				imageView3.setVisibility(View.GONE);
			}
			if(mFiles.size() > 0)
				mFiles.remove(0);
			break;
		}
		case R.id.image_price_content2:
		{
			ImageView imageView = (ImageView) v;
			
			ImageView imageView3 = (ImageView) findViewById(R.id.image_content3);
			Drawable drawable3 = imageView3.getDrawable();
			if(drawable3 != null)
			{
				imageView.setImageDrawable(drawable3);
				imageView.setVisibility(View.VISIBLE);
				imageView3.setImageBitmap(null);
				imageView3.setVisibility(View.GONE);
			}
			else
			{
				imageView.setImageBitmap(null);
				imageView.setVisibility(View.GONE);
			}
			if(mFiles.size() > 1)
				mFiles.remove(1);
			break;
		}
		case R.id.image_price_content3:
		{
			ImageView imageView = (ImageView) v;
			imageView.setImageBitmap(null);
			imageView.setVisibility(View.GONE);
			if(mFiles.size() > 2)
				mFiles.remove(2);
			break;
		}
		}
	}
	
}
