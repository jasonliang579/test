package com.jieyangjiancai.zwj.network.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;

/**
 * 验证码
 * 
 * @author hlai
 * 
 */
public class VerifyCode extends BaseEntity {
	private String vcode;
	private String expire;
	private boolean isNewUser;

	public void setVcode(String str) {
		this.vcode = str;
	}

	public String getVcode() {
		return this.vcode;
	}

	public void setIsNewUser(String str) {
		if (str.equals("1")) {
			this.isNewUser = true;
		} else {
			this.isNewUser = false;
		}
	}

	public boolean getIsNewUser() {
		return this.isNewUser;
	}

	public static VerifyCode parse(JSONObject response) throws JSONException {
		VerifyCode entity = new VerifyCode();
		int error = response.getInt("error");
		entity.setError(error);
		if(error != 0){
			entity.setErrorText(response.optString("errormsg", ""));
			return entity;
		}
		
		if (!response.has("data")) {
			return null;
		}
		JSONObject jo = response.getJSONObject("data");
		String str;
		if(jo.has("vcode")){
			str = jo.optString("vcode", "");
			entity.setVcode(str);
		}

		str = jo.optString("is_newuser", "0");
		entity.setIsNewUser(str);

		return entity;

	}

}
