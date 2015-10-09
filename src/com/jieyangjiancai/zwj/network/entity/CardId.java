package com.jieyangjiancai.zwj.network.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;


public class CardId extends BaseEntity {
	private String photoId;
	private String index;

	public void setPhotoId(String str) {
		this.photoId = str;
	}

	public String getPhotoId() {
		return this.photoId;
	}
	
	public void setPhotoIndex(String str) {
		this.index = str;
	}

	public String getPhotoIndex() {
		return this.index;
	}
	

	public static CardId parse(JSONObject response) throws JSONException {
		CardId entity = new CardId();
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
		String str = jo.optString("photo_id", "");
		entity.setPhotoId(str);

		str = jo.optString("index", "0");
		entity.setPhotoIndex(str);

		return entity;

	}

}
