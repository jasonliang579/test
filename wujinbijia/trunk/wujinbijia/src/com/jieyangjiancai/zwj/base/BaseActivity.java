package com.jieyangjiancai.zwj.base;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.data.OrderItem;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.OrderMessage;
import com.jieyangjiancai.zwj.ui.GetOrderActivity;
import com.jieyangjiancai.zwj.ui.GetOrderMakePriceActivity;
import com.jieyangjiancai.zwj.ui.GetOrderMyPriceActivity;
import com.jieyangjiancai.zwj.ui.MainActivityNew;
import com.jieyangjiancai.zwj.ui.QueryOrderActivity;
import com.jieyangjiancai.zwj.ui.QueryOrderDetailPriceActivity;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity {
	private wujinReceiver updateListViewReceiver;
	private static Context mContext;
	private static OrderMessage mOrderMessage;
	private static String mType = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (updateListViewReceiver != null) {
			unregisterReceiver(updateListViewReceiver);
			updateListViewReceiver = null;
		}
		MobclickAgent.onPause(this);
	}

	private void registerReceiver() {
		// 0.注册数据更新监听器
		if (updateListViewReceiver == null) {

			updateListViewReceiver = new wujinReceiver();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction("com.jieyangjiancai.zwj.notify");
			registerReceiver(updateListViewReceiver, intentFilter);
		}
	}

	public class wujinReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// ToastMessage.show(context, "收到通知!");
			// String orderId = intent.getStringExtra("orderid");
			// String action = intent.getStringExtra("action");
			String customContent = intent.getStringExtra("customcontent");
			if (customContent != null && customContent.length() != 0) {
				try {
					JSONObject obj = new JSONObject(customContent);
					String type = obj.optString("type", "");
					String userType = obj.optString("userType", "");
					String orderMessageId = obj.optString("orderMessageId", "");
					String title = intent.getStringExtra("title");
					String content = intent.getStringExtra("content");
					normalDialog(mContext, title, content, "取消", "确定", type, userType, orderMessageId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void normalDialog(Context context, String title, String text, String strCancel, String strOK, final String type,
			final String userType, final String orderMessageId) {

		AlertDialog.Builder normalDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(text)
				.setPositiveButton(strOK, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
						Log.d("wujin","BaseActivity:normalDialog:type="+type);
						if (type.equals("price")) {// 表示有供应商报价，跳到选择供应商页面
							GotoQueryOrderDetailPrice(mContext, orderMessageId, userType);
//							Intent intent = new Intent(mContext, QueryOrderActivity.class);
//							mContext.startActivity(intent);
						} else if (type.equals("order")) {// 表示有新询单，跳到询单报价页面
							GotoGetOrderPrice(mContext, orderMessageId, userType);
//							Intent intent = new Intent(mContext, GetOrderActivity.class);
//							mContext.startActivity(intent);
						} else if (type.equals("selectSupplier")) {// 表示客户选择了供应商，跳到询单报价页面
							GotoGetMyPrice(mContext, orderMessageId, userType);
						} else if (type.equals("payment")) {// 表示客户已经支付，跳到询单报价页面
							GotoGetMyPrice(mContext, orderMessageId, userType);
						} else if (type.equals("receive")) {
							// 如果userType=supplier，跳到询单报价页面，如果userType=buyer，跳到选择供应商页面
							if (userType.equals("supplier")) {
								GotoGetMyPrice(mContext, orderMessageId, userType);
							} else {
								GotoQueryOrderDetailPrice(mContext, orderMessageId, userType);
//								Intent intent = new Intent(mContext, QueryOrderActivity.class);
//								mContext.startActivity(intent);
							}
						}
					}
				});
		if (strCancel != null) {
			normalDialog.setNegativeButton(strCancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
		}
		normalDialog.setCancelable(false);
		normalDialog.show();
	}

	boolean isWorkingLayout(Context context, String activityPackage) {
		boolean b = false;

		if (getTopActivityName(context).contains(activityPackage)) {
			b = true;
		}

		return b;
	}

	public String getTopActivityName(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
		RunningTaskInfo rti = runningTasks.get(0);
		ComponentName component = rti.topActivity;
		return component.getClassName();
	}
	

	private static void GotoGetOrderPrice(Context context, String orderMessageId, String userType)
	{
		mType = "GotoGetOrderPrice";
		FetchSingleOrderMessage(context, orderMessageId, userType);
	}
	private static void GotoQueryOrderDetailPrice(Context context, String orderMessageId, String userType)
	{
		mType = "GotoQueryOrderDetailPrice";
		FetchSingleOrderMessage(context, orderMessageId, userType);
	}
	private static void GotoGetMyPrice(Context context, String orderMessageId, String userType)
	{
		mType = "GotoGetMyPrice";
		FetchSingleOrderMessage(context, orderMessageId, userType);
	}
	private static void StartGetOrderMakePriceActivity()
	{
		OrderItem item = mOrderMessage.getOrderMessage();
		Intent intent = new Intent(mContext, GetOrderMakePriceActivity.class);
	    intent.putExtra("order_status", item.order_status);
	    intent.putExtra("show_time", item.show_time);
	    intent.putExtra("order_id", item.order_id);
	    intent.putExtra("content", item.content);
	    intent.putExtra("order_status_content", item.order_status_content);
	    if (item.picture_arr.size()>0)
	    {
	    	for(int i=0; i<item.picture_arr.size(); i++)
	    	{
	    		intent.putExtra("picture"+(i+1), item.picture_arr.get(i).path);
	    	}
	    }
	    intent.putExtra("order_message_id", item.order_message_id);
	    mContext.startActivity(intent);
	}
	private static void StartQueryOrderDetailPriceActivity()
	{
		OrderItem item = mOrderMessage.getOrderMessage();
		Intent intent = new Intent(mContext, QueryOrderDetailPriceActivity.class);
		intent.putExtra("order_status", item.order_status);
	    intent.putExtra("show_time", item.show_time);
	    intent.putExtra("order_id", item.order_id);
	    intent.putExtra("content", item.content);
	    intent.putExtra("order_status_content", item.order_status_content);
	    if (item.picture_arr.size()>0)
	    {
	    	for(int i=0; i<item.picture_arr.size(); i++)
	    	{
	    		intent.putExtra("picture"+(i+1), item.picture_arr.get(i).path);
	    	}
	    }
	    if (item.payment_voucher_picture_arr.size()>0)
	    {
	    	for(int i=0; i<item.payment_voucher_picture_arr.size(); i++)
	    	{
	    		intent.putExtra("payment_picture"+(i+1), item.payment_voucher_picture_arr.get(i).path);
	    	}
	    }
	    intent.putExtra("order_message_id", item.order_message_id);
	    intent.putExtra("supplier_id", item.supplier_id);
	    mContext.startActivity(intent);
	}
	private static void StartGetMyPriceActivity()
	{
		Intent intent = new Intent(mContext, GetOrderMyPriceActivity.class);
		mContext.startActivity(intent);
	}
	private static void FetchSingleOrderMessage(Context context, String orderMessageId, String userType)
	{
			BackendDataApi bdApi = ((WJApplication)context.getApplicationContext()).getHttpRequest();
			bdApi.FetchSingleOrderMessage(ConfigUtil.mUserId, ConfigUtil.mToken, 
					orderMessageId, userType,
					reqSuccessListener(), reqErrorListener());
	}
	private static Response.Listener<JSONObject> reqSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					mOrderMessage = OrderMessage.parse(response);
					
					if (mType.equals("GotoGetOrderPrice"))
						StartGetOrderMakePriceActivity();
					else if (mType.equals("GotoQueryOrderDetailPrice"))
						StartQueryOrderDetailPriceActivity();
					else if (mType.equals("GotoGetMyPrice"))
						StartGetMyPriceActivity();
				    
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	private static Response.ErrorListener reqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		};
	}

}
