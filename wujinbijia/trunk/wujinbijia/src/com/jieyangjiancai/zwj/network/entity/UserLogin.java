package com.jieyangjiancai.zwj.network.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;

/**
 * 用户登录/注册
 * 
 * @author hlai
 * 
 */
public class UserLogin extends BaseEntity {
	private String userId;
	private String token;
	private long expire;
	private String userName;
	
	public void setUserId(String str) {
		this.userId = str;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setToken(String str) {
		this.token = str;
	}
	
	public String getToken() {
		return this.token;
	}

	public long getExpire() {
		return this.expire;
	}
	
	public void setExpire(long str) {
		
		this.expire = str;
	}
	
	public void setUserName(String str) {
		this.userName = str;
	}

	public String getUserName() {
		return this.userName;
	}

	
	public static UserLogin parse(JSONObject response) throws JSONException {
		UserLogin entity = new UserLogin();
		int error = response.getInt("error");
		entity.setError(error);
		if (error != 0) {
			entity.setErrorText(response.optString("errormsg", ""));
			return entity;
		}

		if (!response.has("data")) {
			return entity;
		}
		
		JSONObject jo = response.getJSONObject("data");
		String str = jo.optString("user_id", "");
		entity.setUserId(str);

		str = jo.optString("token", "");
		entity.setToken(str);
		long expire = jo.optLong("expire", 0);
		entity.setExpire(expire);

		if (jo.has("user_name")) {
			str = jo.optString("user_name", "");
			entity.setUserName(str);
		}
		
		return entity;

	}

}
