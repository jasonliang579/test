package com.jieyangjiancai.zwj.common;

import android.content.SharedPreferences;

public class SharedPreferencesUtil {

	private SharedPreferences config;

	public SharedPreferencesUtil(SharedPreferences config) {
		this.config = config;// demo演示,允许其他应用读取
	}

	// 各种基本类型
	public void setBoolean(String key, Boolean flag) {
		config.edit().putBoolean(key, flag).commit();
	}

	public boolean getBoolean(String key) {
		return config.getBoolean(key, true);
	}

	public void setFloat(String key, float flag) {
		config.edit().putFloat(key, flag).commit();
	}

	public float getFloat(String key) {
		return config.getFloat(key, 0f);
	}

	public void setInt(String key, int flag) {
		config.edit().putInt(key, flag).commit();
	}

	public int getInt(String key) {
		return config.getInt(key, 0);
	}

	public void setLong(String key, long flag) {
		config.edit().putLong(key, flag).commit();
	}

	public long getLong(String key) {
		return config.getLong(key, 0);
	}

	public void setString(String key, String flag) {
		config.edit().putString(key, flag).commit();
	}

	public String getString(String key) {
		return config.getString(key, "");
	}


}
