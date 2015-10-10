package com.jieyangjiancai.zwj.common;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;
/**
 * 信鸽注册/启动
 * @author hlai
 *
 */
public class XGPushRegister {

	private CountDownTimer mCountDown;
	private int mTryTimes;
	private boolean mTryFlag;
	private Context mContext;
	private RegisterPushCallback mCallback;
	
	public XGPushRegister(Context context, RegisterPushCallback callback) {
		mContext = context;
		mTryFlag = true;
		mTryTimes = 2;
		mCallback = callback;
		checkPushToken();
		mCountDown = new CountDownTimer(8000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

				KMLog.d("pushtoken 倒计时........." + millisUntilFinished / 1000);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				if (mTryTimes > 1) {
					mTryTimes--;
					mCountDown.start();
					checkPushToken();
				} else {
					mCountDown.cancel();
					mTryFlag = false;
					mCallback.RegisterPushError();
					ToastMessage.show(mContext, "注册推送失败!");
				}

			}
		}.start();
	}

	public static String getPushToken(Context context){
		
		return  XGPushConfig.getToken(context.getApplicationContext());
	}
	
	private void checkPushToken() {
//		String pushToken = ((KMApplication)mContext.getApplicationContext()).getPushToken();
		KMLog.d("注册pushtoken ........." + mTryTimes);

		int XGstatus = XGPushManager.getServiceStatus(mContext.getApplicationContext());
		String ptoken = XGPushConfig.getToken(mContext.getApplicationContext());
		// 信鸽调试.发布不要打开.
		XGPushConfig.enableDebug(mContext.getApplicationContext(), false);
		
		// 信鸽注册 start
		XGPushManager.registerPush(mContext.getApplicationContext(), new XGIOperateCallback() {
			@Override
			public void onSuccess(Object data, int flag) {
				// TODO Auto-generated method stub
//				((KMApplication) mContext.getApplicationContext()).setPushToken((String) data);

				KMLog.d("pushtoken注册成功:" + (String) data);
				if (mCountDown != null) {
					mCountDown.cancel();
				}
				mTryFlag = false;
				mCallback.RegisterPushSuccess(data, flag);
			}

			@Override
			public void onFail(Object data, int errCode, String msg) {
				// TODO Auto-generated method stub
				if (mCountDown != null) {
					mCountDown.cancel();
				}
				mTryFlag = false;
				KMLog.e("pushtoken 注册失败:" + data);
				ToastMessage.show(mContext.getApplicationContext(), msg);
				
//				dismissLoadingDialog();
				mCallback.RegisterPushError();
			}

		});
		XGPushManager.getServiceStatus(mContext.getApplicationContext());
		// 2.36（不包括）之前的版本需要调用以下2行代码
		Intent service = new Intent(mContext.getApplicationContext(), XGPushService.class);
		mContext.getApplicationContext().startService(service);

	}
	
	public interface RegisterPushCallback{
		
		public void RegisterPushSuccess(Object data, int flag);
		public void RegisterPushError();

	}
	
	
	
}
