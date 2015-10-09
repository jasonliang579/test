package com.jieyangjiancai.zwj;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.jieyangjiancai.zwj.common.KMLog;
import com.jieyangjiancai.zwj.common.ToastMessage;
import com.jieyangjiancai.zwj.common.sysUtils;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.VerifyCode;
import com.jieyangjiancai.zwj.ui.LoginActivity;

/**
 * 全局配置和网络访问
 * 
 * @author hlai
 * 
 */
public class WJApplication extends Application {


	private BackendDataApi mRequestDataApi;
	
	private String mTaskerKey;// 登录账号(手机号码/?)
	private boolean mIsLogin = false;// 是否已经登录
	private boolean mIsOnline = false;// 是否在线, true 是, false 否.
	private boolean mIsSound = true;// 声音提醒
	private boolean mIsVibrate = true;// 震动提醒
	private boolean mPushRegister = false; // 推送注册
	private String mPushToken; // 推送token
	private String mToken;// 登录token
	private String mOrderId = null;
	private String mAppDomain;
	private String mWebDomain;

	// 本地临时配置
	private SharedPreferences mSettings;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//账户信息
		ConfigUtil.initConfig(getApplicationContext());
		// 网络库
		mRequestDataApi = new BackendDataApi(getApplicationContext());
		
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
//		mSettings.unregisterOnSharedPreferenceChangeListener(mPreferenceListener);
		//终止网络
		mRequestDataApi.terminate();
	}

	
	public BackendDataApi getHttpRequest(){
		return mRequestDataApi;
	}


	BufferedInputStream file2byte(File file) {
		// File file = new File(path);
		int size = (int) file.length();
		byte[] bytes = new byte[size];
		try {
			BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
			buf.read(bytes, 0, bytes.length);
			// buf.close();
			return buf;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断当前版本是否兼容目标版本的方法
	 * 
	 * @param VersionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}


}
