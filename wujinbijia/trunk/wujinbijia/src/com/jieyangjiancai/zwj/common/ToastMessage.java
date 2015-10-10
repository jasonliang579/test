package com.jieyangjiancai.zwj.common;

import android.content.Context;
import android.widget.Toast;

public class ToastMessage {
	
	public static void show(Context context, String msg) {
		if(context == null)
			return;
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public static void show(Context context, String msg, int time) {
		if(context == null)
			return;
		Toast.makeText(context, msg, time).show();
	}
	

	
	
}
