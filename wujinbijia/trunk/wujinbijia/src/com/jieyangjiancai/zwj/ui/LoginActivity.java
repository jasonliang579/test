package com.jieyangjiancai.zwj.ui;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.common.KMLog;
import com.jieyangjiancai.zwj.common.ToastMessage;
import com.jieyangjiancai.zwj.common.XGPushRegister;
import com.jieyangjiancai.zwj.common.sysUtils;
import com.jieyangjiancai.zwj.config.BuildConfig;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.UserLogin;
import com.jieyangjiancai.zwj.network.entity.VerifyCode;
import com.umeng.analytics.MobclickAgent;

public class LoginActivity extends Activity implements OnClickListener {

	private TextView mTextViewPhone;
	private TextView mTextViewCode;
	private TextView mTextViewSendCode;
	private boolean mCanSendCode = true;
	private int mTimerCount;
	private static VerifyCode mVCode;

	private Timer timer = new Timer();
	private TimerTask task;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		Init();
	}

	private void Init() {
		findViewById(R.id.btn_login_conform).setOnClickListener(this);
		findViewById(R.id.title_bar_back).setOnClickListener(this);
		findViewById(R.id.text_agreement).setOnClickListener(this);

		TextView title = (TextView) findViewById(R.id.title_bar_text);
		title.setText("登陆");

		mTextViewPhone = (TextView) findViewById(R.id.text_login_phone);
		mTextViewCode = (TextView) findViewById(R.id.text_login_code);
		mTextViewSendCode = (TextView) findViewById(R.id.text_sendcode);
		mTextViewSendCode.setOnClickListener(this);
		mTextViewCode.setOnClickListener(this);
		mTextViewPhone.setOnClickListener(this);
		mTextViewSendCode.setBackgroundColor(0xffdf5054);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.text_login_phone:
			mTextViewPhone.requestFocus();
			mTextViewPhone.setCursorVisible(true);
			break;
		case R.id.text_login_code:
			mTextViewCode.setCursorVisible(true);
			mTextViewCode.requestFocus();
			break;
		case R.id.text_sendcode:
			if (mCanSendCode == true) {
				String phone = mTextViewPhone.getText().toString().trim();
				if (phone == null || phone.equals(""))
				{
					ToastMessage.show(this, "请输入手机号");
					break;
				}
				if (phone.length() != 11)
				{
					ToastMessage.show(this, "手机号有误");
					break;
				}
				SendVerfyCode();
				mTextViewSendCode.setBackgroundColor(0xffa8a8a8);
				mTextViewSendCode.setText("重新发送(60s)");
				mTimerCount = 60;
				mCanSendCode = false;
				StartTimer();

				mTextViewCode.requestFocus();
				mTextViewCode.setCursorVisible(true);
			}
			break;

		case R.id.btn_login_conform:
			String code = mTextViewCode.getText().toString();
			if (code == null || code.equals("")) {
				ToastMessage.show(this, "请输入验证码！");
			} else {
				Login();
			}
			break;

		case R.id.title_bar_back:
			finish();
			break;

		case R.id.text_agreement:
			Intent intent = new Intent(this, AgreementActivity.class);
			this.startActivity(intent);
			break;
		}
	}

	public void SendVerfyCode() {
		mVCode = null;
		((WJApplication) this.getApplicationContext()).getHttpRequest().loginVcode(mTextViewPhone.getText().toString(),
				reqSuccessListener(), reqErrorListener());
	}

	public Response.Listener<JSONObject> reqSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					VerifyCode verifycode = VerifyCode.parse(response);
					
					if (verifycode.getError() == 0)
						mVCode = verifycode;
					else
						mVCode = null;
					
					if (mVCode != null && ConfigUtil.IsTest){
						mTextViewCode.setText(mVCode.getVcode());
					}
					if(BuildConfig.deBug){
					    mTextViewCode.setText(mVCode.getVcode());
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

	public Response.ErrorListener reqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// String t = error.getMessage();
				 ToastMessage.show(LoginActivity.this, "网络错误!");
			}
		};
	}

	private void Login() {
		
		if (mVCode == null)
		{
			ToastMessage.show(LoginActivity.this, "验证码错误！");
			return;
		}
		else
		{
			String code = mTextViewCode.getText().toString();
			String vcode = mVCode.getVcode();
			if (vcode == null)
			{
				ToastMessage.show(LoginActivity.this, "验证码错误！");
				return;
			}

			if (vcode != null && !code.equals(mVCode.getVcode()))
			{
				ToastMessage.show(LoginActivity.this, "验证码错误！");
				return;
			}
		}
			
		BackendDataApi bdApi = ((WJApplication) this.getApplicationContext()).getHttpRequest();
		String ip = sysUtils.getIPAddress(true);
		String pushToken = XGPushRegister.getPushToken(this);
		bdApi.login(mTextViewPhone.getText().toString(), mTextViewCode.getText().toString(), ip, pushToken, reqLoginSuccessListener(), reqLoginErrorListener());
	}

	public Response.Listener<JSONObject> reqLoginSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if(!response.optString("error", "-1").equals("0")){
						ToastMessage.show(LoginActivity.this, response.optString("errormsg", ""));
						return;
					}
					UserLogin user = UserLogin.parse(response);
					ConfigUtil.setLoginInfo(user.getToken(), user.getUserId(), user.getUserName(), user.getExpire());
					ToastMessage.show(LoginActivity.this, "登陆成功！");
					StopTimer();

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (ConfigUtil.mUserName != null && !ConfigUtil.mUserName.equals("")
							&& !ConfigUtil.mUserName.trim().equals("")) {
						finish();
					} else {
						finish();
						Intent intent = new Intent(LoginActivity.this, PensonInfoActivity.class);
						LoginActivity.this.startActivity(intent);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

	public Response.ErrorListener reqLoginErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// String t = error.getMessage();
				 ToastMessage.show(LoginActivity.this, "网络请求失败");
			}
		};
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				mTimerCount--;
				if (mTimerCount <= 0) {
					mTextViewSendCode.setBackgroundColor(0xffdf5054);
					mTextViewSendCode.setText("发送验证码");
					mCanSendCode = true;
					StopTimer();
				} else {
					mTextViewSendCode.setText("重新发送(" + mTimerCount + "s)");
				}
			}
			super.handleMessage(msg);
		};
	};

	private void StartTimer() {
		timer = new Timer();

		task = new TimerTask() {
			@Override
			public void run() {
				// 需要做的事:发送消息
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}
		};

		timer.schedule(task, 1000, 1000); // 1s后执行task,经过1s再次执行
	}

	private void StopTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (task != null) {
			task.cancel();
			task = null;
		}
	}
	
	@Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
    }
    
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
