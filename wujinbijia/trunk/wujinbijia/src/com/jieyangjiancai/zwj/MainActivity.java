package com.jieyangjiancai.zwj;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jieyangjiancai.zwj.common.KMLog;
import com.jieyangjiancai.zwj.common.ToastMessage;
import com.jieyangjiancai.zwj.common.XGPushRegister;
import com.jieyangjiancai.zwj.common.XGPushRegister.RegisterPushCallback;
import com.jieyangjiancai.zwj.network.entity.VerifyCode;

public class MainActivity extends Activity {

	private static RequestQueue mSingleQueue;
	private static String TAG = "test";
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main_brand);
		Button btn = (Button)findViewById(R.id.test_button);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				testHttp();
				
			}
		});
		
	}
	
	
	/**
	 * test http request
	 * 
	 * @param listener
	 * @param errorListener
	 */
	public void testHttp() {

		((WJApplication)getApplicationContext()).getHttpRequest().loginVcode("13929514753", reqSuccessListener(), reqErrorListener());
	}

	/**
	 * test listner
	 * 
	 * @return
	 */
	public Response.Listener<JSONObject> reqSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				ToastMessage.show(mContext, response.toString());
				try {
					VerifyCode v = VerifyCode.parse(response);
					if(v.getError() != 0){
						ToastMessage.show(mContext,v.getErrorText());
					}else{
						KMLog.e("vcode:" + v.getVcode() + "  is_new_user:"+ v.getIsNewUser());
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

	/**
	 * test listner
	 * 
	 * @return
	 */
	public Response.ErrorListener reqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				String t = error.getMessage();
				ToastMessage.show(mContext, "登陆失败！");
			}
		};
	}

}
