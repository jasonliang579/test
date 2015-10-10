package com.jieyangjiancai.zwj.network.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;

/**
 *获取优惠金额
 * 
 * @author hlai
 * 
 */
public class Rebate extends BaseEntity {
	private String amount;

	public void setAmount(String str) {
		this.amount = str;
	}

	public String getAmount() {
		return this.amount;
	}

	
	public static Rebate parse(JSONObject response) throws JSONException {
		Rebate entity = new Rebate();
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
		String str = jo.optString("amount", "");
		entity.setAmount(str);
		return entity;

	}

}
