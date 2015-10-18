package com.jieyangjiancai.zwj.ui;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
import com.jieyangjiancai.zwj.network.entity.Rebate;
import com.jieyangjiancai.zwj.network.entity.UserInfo;
import com.jieyangjiancai.zwj.utils.AppUtil;

public class PayOrderActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout mLayoutProgress;
//	private File mFile;
//	private String mPhotoId;
	private String mOrderMessageId;
	private String mSupplierId;
	private TextView mTextSupplierName;
	private TextView mTextSupplierPhone;
	private TextView mTextSupplierAddress;
	private TextView mTextRebate;
	
	private ArrayList<File> mFiles = new ArrayList<File>();
	private ArrayList<String> mPhotoArray = new ArrayList<String>();
	private int mPhotoCount;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pay_order);
        
        Init();
    }
	
	private void Init()
	{
		InitUI();
		InitData();
	}
	
	private void InitUI()
	{
		//返回
		findViewById(R.id.title_bar_back).setOnClickListener(this);
		findViewById(R.id.text_upload_payphoto).setOnClickListener(this);
		findViewById(R.id.btn_pay).setOnClickListener(this);
		findViewById(R.id.text_pay_account).setOnClickListener(this);
		findViewById(R.id.text_warranty).setOnClickListener(this);
		
		mTextSupplierName = (TextView)findViewById(R.id.text_supplier_name);
		mTextSupplierPhone = (TextView)findViewById(R.id.text_supplier_phone);
		mTextSupplierAddress = (TextView)findViewById(R.id.text_supplier_address);
		mTextRebate = (TextView)findViewById(R.id.text_rebate);
		
		TextView title = (TextView)findViewById(R.id.title_bar_text);
		title.setText("支付费用");
		
		mLayoutProgress = (RelativeLayout)findViewById(R.id.layout_progress_bar);
		mLayoutProgress.setVisibility(View.INVISIBLE);
	}
	
	private void InitData()
	{
		Intent intent = getIntent();
		mOrderMessageId = intent.getStringExtra("order_message_id");
		mSupplierId		= intent.getStringExtra("supplier_id");
		
		GetRebateAmount();
		
		GetSupplierInfo();
	}
	
	//获取优惠金额
	private void GetRebateAmount()
	{
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.fetchRebateAccount(ConfigUtil.mUserId, ConfigUtil.mToken, reqGetRebateSuccessListener(), reqGetRebateErrorListener());
	}
	private Response.Listener<JSONObject> reqGetRebateSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					Rebate rebate = Rebate.parse(response);
					mTextRebate.setText("您还有"+rebate.getAmount()+"元优惠券可以使用");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	private Response.ErrorListener reqGetRebateErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
//				String t = error.getMessage();
//				ToastMessage.show(MyInfoActivity.this, t);
			}
		};
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch(v.getId())
		{
		case R.id.title_bar_back:
			finish();
			break;
		case R.id.text_upload_payphoto:
			ConfigUtil.doPickPhotoAction(this , 3);
			break;
			
		case R.id.btn_pay:
			UploadPayImage();
			break;
			
		case R.id.text_pay_account:
			intent = new Intent(this, BankAccountActivity.class);
			startActivity(intent);
			break;
			
		case R.id.text_warranty:
			intent = new Intent(this, WarrantyActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	private void UploadPayImage()
	{
		if (mFiles.size() <= 0)
		{
			ToastMessage.show(this, "请上传报价明细照片。");
			mLayoutProgress.setVisibility(View.INVISIBLE);
			return;
		}
		mLayoutProgress.setVisibility(View.VISIBLE);
		
		String userId = ConfigUtil.mUserId;
		String token = ConfigUtil.mToken;
		String type = "3"; //3-付款凭证

		mPhotoArray.clear();
		mPhotoCount = mFiles.size();
		for (int i = 0; i < mFiles.size(); i++) {
			BackendDataApi bdApi = ((WJApplication) this.getApplicationContext()).getHttpRequest();
			bdApi.uploadImage(mFiles.get(i), userId, token, type, reqUploadSuccessListener(), reqUploadErrorListener());
		}
//		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
//		bdApi.uploadImage(mFile, userId, token, type,  reqUploadSuccessListener(), reqUploadErrorListener());
	}
	private Response.Listener<JSONObject> reqUploadSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				//ToastMessage.show(PayOrderActivity.this, "上传照片成功");
				try {
					CardId cardId = CardId.parse(response);
					String photoId = cardId.getPhotoId();
					if (mPhotoArray.contains(photoId))
						return;
					else
						mPhotoArray.add(photoId);

					mPhotoCount--;
					if (mPhotoCount == 0) 
					{
						String order_message_id = mOrderMessageId;
						String photo_arr = "";
						for (int i = 0; i < mPhotoArray.size(); i++)
							photo_arr += mPhotoArray.get(i) + ",";
						
						UploadPayPhoto(photo_arr,order_message_id);
					}

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
				ToastMessage.show(PayOrderActivity.this, "上传照片失败");
			}
		};
	}
	private void UploadPayPhoto(String picture_arr, String order_message_id)
	{
		mLayoutProgress.setVisibility(View.VISIBLE);
		
		String userId = ConfigUtil.mUserId;
		String token = ConfigUtil.mToken;
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.UploadPayPhote(userId, token, picture_arr, order_message_id, reqPaySuccessListener(), reqPayErrorListener());
		AppUtil.sendUmengOnEvent(this, "10005");
	}
	
	private Response.Listener<JSONObject> reqPaySuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				ToastMessage.show(PayOrderActivity.this, "上传支付凭证成功");
				Finish();
				
				try {
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mLayoutProgress.setVisibility(View.INVISIBLE);
			}
		};
	}
	private Response.ErrorListener reqPayErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mLayoutProgress.setVisibility(View.INVISIBLE);
				ToastMessage.show(PayOrderActivity.this, "上传支付凭证失败");
			}
		};
	}

	private void Finish()
	{
//		setResult(Activity.RESULT_OK);
		finish();
		Intent intent = new Intent(this, MainActivityNew.class);
		startActivity(intent);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {

			switch (requestCode) {
			case ConfigUtil.PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的
				
				/*ArrayList<String> paths = ConfigUtil.getOnActivityResultPaths(this, data);
				if(paths == null) return;
				for (int i = 0; i < paths.size(); i++) {
					String fullPath = paths.get(i);
					Bitmap bitmap = ConfigUtil.getThumbnailBitmap(this, fullPath);
					if(bitmap != null){
						fullPath = ConfigUtil.getThumbFilePath();
					}else{
						return;
					}
					if(mFiles.size() == 0) {
						ImageView imageView = (ImageView) findViewById(R.id.image_content1);
						imageView.setImageBitmap(bitmap);
						imageView.setVisibility(View.VISIBLE);
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
				}*/
				mFiles = ConfigUtil.getOnActivityResultPaths(this, data, mFiles);
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
	

	
	private void GetSupplierInfo()
	{
		mLayoutProgress.setVisibility(View.VISIBLE);
		
		String userId = ConfigUtil.mUserId;
		String token = ConfigUtil.mToken;
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.GetSupplierInfo(userId, token, mOrderMessageId, mSupplierId, reqSuccessListener(), reqErrorListener());
	}
	
	private Response.Listener<JSONObject> reqSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				//ToastMessage.show(PayOrderActivity.this, "获取供应商信息成功");
				try {
					UserInfo userinfo = UserInfo.parse(response);
					mTextSupplierName.setText(userinfo.getUserName());
					mTextSupplierPhone.setText(userinfo.getPhone());
					String address= userinfo.getProvinceName()+userinfo.getCityName()+userinfo.getAddress();
					mTextSupplierAddress.setText(address);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mLayoutProgress.setVisibility(View.INVISIBLE);
			}
		};
	}
	private Response.ErrorListener reqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mLayoutProgress.setVisibility(View.INVISIBLE);
				ToastMessage.show(PayOrderActivity.this, "获取供应商信息失败");
			}
		};
	}
}
