package com.jieyangjiancai.zwj.common;

import android.util.Log;

/**
 * log 控制类
 * 
 * @author hlai
 * 
 */
public class KMLog {

	private static final boolean DEBUG = true;
	public static final String TAG = "KM";

	public static void d(String msg) {
		if (DEBUG) {
			Log.d(TAG, msg);
		}
	}

	public static void i(String msg) {
		if (DEBUG) {
			Log.i(TAG, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (DEBUG) {
			Log.e(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (DEBUG) {
			Log.i(tag, msg);
		}
	}

	public static void e(String msg) {
		if (DEBUG) {
			Log.e(TAG, msg);
		}
	}

}
