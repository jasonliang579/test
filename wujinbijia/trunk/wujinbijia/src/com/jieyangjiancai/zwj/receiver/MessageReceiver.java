package com.jieyangjiancai.zwj.receiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.common.KMLog;
import com.jieyangjiancai.zwj.common.NotificationService;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.data.OrderItem;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.OrderMessage;
import com.jieyangjiancai.zwj.network.entity.UpdateUserInfo;
import com.jieyangjiancai.zwj.ui.GetOrderActivity;
import com.jieyangjiancai.zwj.ui.GetOrderMakePriceActivity;
import com.jieyangjiancai.zwj.ui.GetOrderMyPriceActivity;
import com.jieyangjiancai.zwj.ui.QueryOrderActivity;
import com.jieyangjiancai.zwj.ui.QueryOrderDetailPriceActivity;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

public class MessageReceiver extends XGPushBaseReceiver {
	private Intent intent = new Intent("com.jieyangjiancai.zwj.notify");
	public static final String LogTag = "TPushReceiver";

	private Context mContext;
	private OrderMessage mOrderMessage;
	private static String mType = "";
	
	private void show(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	// 通知展示
	@Override
	public void onNotifactionShowedResult(Context context, XGPushShowedResult notifiShowedRlt) {
		if (context == null || notifiShowedRlt == null) {
			return;
		}
		if (isWorkingLayout(context)) {
			String customContent = notifiShowedRlt.getCustomContent();
			intent.putExtra("customcontent", customContent);
			intent.putExtra("title", notifiShowedRlt.getTitle());
			intent.putExtra("content", notifiShowedRlt.getContent());
			context.sendBroadcast(intent);
		} else {

			XGNotification notific = new XGNotification();
			notific.setMsg_id(notifiShowedRlt.getMsgId());
			notific.setTitle(notifiShowedRlt.getTitle());
			notific.setContent(notifiShowedRlt.getContent());
			// notificationActionType==1为Activity，2为url，3为intent
			notific.setNotificationActionType(notifiShowedRlt.getNotificationActionType());
			// Activity,url,intent都可以通过getActivity()获得
			notific.setActivity(notifiShowedRlt.getActivity());
			notific.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
			NotificationService.getInstance(context).save(notific);
		}
		
		// show(context, "您有1条新消息, " + "通知被展示 ， " + notifiShowedRlt.toString());
	}

	@Override
	public void onUnregisterResult(Context context, int errorCode) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "反注册成功";
		} else {
			text = "反注册失败" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);

	}

	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"设置成功";
		} else {
			text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);

	}

	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"删除成功";
		} else {
			text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);

	}

	// 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
	@Override
	public void onNotifactionClickedResult(Context context, XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
		mContext = context;
		String text = "";
		if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
			// 通知在通知栏被点击啦。。。。。
			// APP自己处理点击的相关动作
			// 这个动作可以在activity的onResume也能监听，请看第3点相关内容
			String customContent = message.getCustomContent();
			if (customContent != null && customContent.length() != 0) {
				try {
					JSONObject obj = new JSONObject(customContent);
					String type = obj.optString("type", "");
					String userType = obj.optString("userType", "");
					String orderMessageId = obj.optString("orderMessageId", "");
					String title = message.getTitle();
					String content = message.getContent();
					// ...

					
					Log.d("wujin","onNotifactionClickedResult:type="+type);
					if (type.equals("price")) {// 表示有供应商报价，跳到选择供应商页面
						GotoQueryOrderDetailPrice(mContext, orderMessageId, userType);
//						Intent intent = new Intent(mContext, QueryOrderActivity.class);
//						mContext.startActivity(intent);
					} else if (type.equals("order")) {// 表示有新询单，跳到询单报价页面
						GotoGetOrderPrice(mContext, orderMessageId, userType);
//						Intent intent = new Intent(context, GetOrderActivity.class);
//						context.startActivity(intent);
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
//							Intent intent = new Intent(context, QueryOrderActivity.class);
//							context.startActivity(intent);
						}
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			return;
		} else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			// 通知被清除啦。。。。
			// APP自己处理通知被清除后的相关动作
			text = "通知被清除 :" + message;
		}
		// Toast.makeText(context, "广播接收到通知被点击:" + message.toString(),
		// Toast.LENGTH_SHORT).show();
		// 获取自定义key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					Log.d(LogTag, "get custom value:" + value);
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP自主处理的过程。。。
		Log.d(LogTag, text);
		// show(context, text);
	}

	@Override
	public void onRegisterResult(Context context, int errorCode, XGPushRegisterResult message) {
		// TODO Auto-generated method stub
		if (context == null || message == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = message + "注册成功";
			// 在这里拿token
			String token = message.getToken();
		} else {
			text = message + "注册失败，错误码：" + errorCode;
		}
		Log.d(LogTag, text);
		// show(context, text);
	}

	// 消息透传
	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		// TODO Auto-generated method stub
		String text = "收到消息:" + message.toString();
		KMLog.d(LogTag, text);
		// 获取自定义key-value\
		String strContext = message.getContent();
		String strCustomContent = message.getCustomContent();
		if (strCustomContent != null && strCustomContent.length() != 0) {
			try {
				System.out.println("接到推送：" + strCustomContent);
				JSONObject obj = new JSONObject(strCustomContent);
				// PushContent push = PushContent.parse(obj);
				// String orderId = push.getOrderId();
				//
				// Intent serviceIntent = new Intent(context,KMService.class);
				// serviceIntent.putExtra("order_id", orderId);
				// context.startService(serviceIntent);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	boolean isWorkingLayout(Context context) {
		boolean b = false;

		if (getTopActivityName(context).contains("com.jieyangjiancai.zwj")) {
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
	
	private void GotoGetOrderPrice(Context context, String orderMessageId, String userType)
	{
		mType = "GotoGetOrderPrice";
		FetchSingleOrderMessage(context, orderMessageId, userType);
	}
	private void GotoQueryOrderDetailPrice(Context context, String orderMessageId, String userType)
	{
		mType = "GotoQueryOrderDetailPrice";
		FetchSingleOrderMessage(context, orderMessageId, userType);
	}
	private void GotoGetMyPrice(Context context, String orderMessageId, String userType)
	{
		mType = "GotoGetMyPrice";
		FetchSingleOrderMessage(context, orderMessageId, userType);
	}
	private void StartGetOrderMakePriceActivity()
	{
		OrderItem item = mOrderMessage.getOrderMessage();
		//Intent intent = new Intent(mContext, GetOrderMakePriceActivity.class);
		Intent intent = new Intent();
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
	    		intent.putExtra("thumbpic"+(i+1), item.picture_arr.get(i).thumb);
	    	}
	    }
	    intent.putExtra("order_message_id", item.order_message_id);
	    intent.setClassName("com.jieyangjiancai.zwj", "com.jieyangjiancai.zwj.ui.GetOrderMakePriceActivity");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    mContext.startActivity(intent);
	    
	    //Intent i = new Intent();
		//i.setClassName("com.jieyangjiancai.zwj", "com.jieyangjiancai.zwj.ui.QueryOrderActivity");
		//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//context.startActivity(i);
	}
	private void StartQueryOrderDetailPriceActivity()
	{
		OrderItem item = mOrderMessage.getOrderMessage();
		//Intent intent = new Intent(mContext, QueryOrderDetailPriceActivity.class);
		Intent intent = new Intent();
		
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
	    		intent.putExtra("thumbpic"+(i+1), item.picture_arr.get(i).thumb);
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
	    intent.setClassName("com.jieyangjiancai.zwj", "com.jieyangjiancai.zwj.ui.QueryOrderDetailPriceActivity");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    mContext.startActivity(intent);
	}
	private void StartGetMyPriceActivity()
	{
		Intent intent = new Intent();
		intent.setClassName("com.jieyangjiancai.zwj", "com.jieyangjiancai.zwj.ui.GetOrderMyPriceActivity");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}
	
	private void FetchSingleOrderMessage(Context context, String orderMessageId, String userType)
	{
			BackendDataApi bdApi = ((WJApplication)context.getApplicationContext()).getHttpRequest();
			bdApi.FetchSingleOrderMessage(ConfigUtil.mUserId, ConfigUtil.mToken, 
					orderMessageId, userType,
					reqSuccessListener(), reqErrorListener());
	}
	private Response.Listener<JSONObject> reqSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String error = response.getString("error");
					if(!"0".equals(error)){
						if(mContext != null){
							Toast.makeText(mContext, "订单过期或者错误", Toast.LENGTH_LONG);
						}
						return ;
					}
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
	private Response.ErrorListener reqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		};
	}
	

}
