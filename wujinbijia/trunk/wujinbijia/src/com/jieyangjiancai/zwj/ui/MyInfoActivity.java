package com.jieyangjiancai.zwj.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.common.ToastMessage;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.Rebate;
import com.jieyangjiancai.zwj.network.entity.UpdateUserInfo;

public class MyInfoActivity extends BaseActivity implements OnClickListener {
	private TextView mTextRebate;
	
	private TextView mTextName;
	private TextView mTextPhone;
	private TextView mTextCompany;
	private TextView mTextAddress;
	private Context mContext;
	private RelativeLayout mLayoutProgress;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_myinfo);
        mContext = this;
		Init();
		InitData();
	}

	private void Init() {
		mLayoutProgress = (RelativeLayout)findViewById(R.id.layout_progress_bar);
		mLayoutProgress.setVisibility(View.VISIBLE);
		
		findViewById(R.id.title_bar_back).setOnClickListener(this);
		findViewById(R.id.btn_modify).setOnClickListener(this);
		findViewById(R.id.layout_rebate).setOnClickListener(this);
		
		TextView title = (TextView) findViewById(R.id.title_bar_text);
		title.setText("我的");
		//ImageView more = (ImageView) findViewById(R.id.title_bar_more);
		//more.setImageResource(R.drawable.message);

		findViewById(R.id.layout_about).setOnClickListener(this);
		findViewById(R.id.layout_bankaccount).setOnClickListener(this);
		findViewById(R.id.layout_howtouse).setOnClickListener(this);
		findViewById(R.id.btn_logout).setOnClickListener(this);
		
		mTextRebate = (TextView)findViewById(R.id.text_rebate);
		mTextName = (TextView)findViewById(R.id.text_name);
		mTextPhone = (TextView)findViewById(R.id.text_phone);
		mTextCompany = (TextView)findViewById(R.id.text_company);
		mTextAddress = (TextView)findViewById(R.id.text_address);
	}
	
	private void InitData()
	{
		//获取优惠金额
		GetRebateAmount();
		
		//获取用户信息
		GetPensonInfo();
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
					mTextRebate.setText("￥"+rebate.getAmount());
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
				String t = error.getMessage();
				ToastMessage.show(MyInfoActivity.this, t);
			}
		};
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
					String error = response.getString("error");
					if("0".equals(error)){
						//ToastMessage.show(MyInfoActivity.this, response.toString());
						ConfigUtil.mUserInfo = UpdateUserInfo.parse(response);
						ConfigUtil.mUserName = ConfigUtil.mUserInfo.getUserName();
						mTextName.setText(ConfigUtil.mUserInfo.getUserName());
						mTextPhone.setText(ConfigUtil.mUserInfo.getPhone());
						mTextCompany.setText(ConfigUtil.mUserInfo.getCompanyName());
						mTextAddress.setText(ConfigUtil.mUserInfo.getAddress());
					}else if("2".equals(error)){
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
				String t = error.getMessage();
				ToastMessage.show(MyInfoActivity.this, t);
			}
		};
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_bar_back: // 返回
			finish();
			break;

		case R.id.btn_modify:
			finish();
			intent = new Intent(this, PensonInfoActivity.class);
			startActivity(intent);
			break;

		case R.id.layout_about:
			intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
			
		case R.id.layout_howtouse:
			intent = new Intent(this, GuideActivity.class);
			startActivity(intent);
			break;
			
		case R.id.layout_bankaccount:
			intent = new Intent(this, BankAccountActivity.class);
			startActivity(intent);
			break;
			
		case R.id.btn_logout:
			Logout();
			break;
			
		case R.id.layout_rebate:
			intent = new Intent(this, RebateListActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	private void Logout()
	{
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.Logout(ConfigUtil.mUserId, ConfigUtil.mToken, reqSuccessListener(), reqErrorListener());
	}
	private Response.Listener<JSONObject> reqSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if("0".equals(response.getString("error"))){
						ConfigUtil.setLogout();
						Thread.sleep(300);
						finish();
					}else{
						if(mContext != null){
							ToastMessage.show(mContext, response.getString("errormsg"));
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	private Response.ErrorListener reqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				String t = error.getMessage();
				ToastMessage.show(MyInfoActivity.this, t);
			}
		};
	}

}
