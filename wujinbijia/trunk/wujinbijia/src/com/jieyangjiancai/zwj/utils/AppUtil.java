package com.jieyangjiancai.zwj.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

public class AppUtil {
	/**
	 * 友盟计数事件统计
	 * @param context
	 * @param id
	 */
	public static void sendUmengOnEvent(Context context , String id){
		MobclickAgent.onEvent(context, id);
	}
}
