package com.jieyangjiancai.zwj.network.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;
import com.jieyangjiancai.zwj.data.AddressItem;


public class AddressCode extends BaseEntity {
	private String vcode;
	private String expire;
	private boolean isNewUser;
	public ArrayList<AddressItem> addressList = new ArrayList<AddressItem>();

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

	public static AddressCode parse(JSONObject response) throws JSONException {
		AddressCode entity = new AddressCode();
		int error = response.getInt("error");
		entity.setError(error);
		if(error != 0){
			entity.setErrorText(response.optString("errormsg", ""));
			return entity;
		}
		
		if (!response.has("data")) {
			return null;
		}
		
		JSONArray jarray = response.getJSONArray("data");
		for (int i=0; i<jarray.length(); i++)
		{
			JSONObject jobject = (JSONObject)jarray.get(i);
			AddressItem itemProvince = new AddressItem();
			itemProvince.id 	= jobject.getString("provinceId");
			itemProvince.name   = jobject.getString("provinceName");
			
			JSONArray jarrayCityList = jobject.getJSONArray("cityList");
			for (int j=0; j<jarrayCityList.length(); j++)
			{
				JSONObject jobjectCity = (JSONObject)jarrayCityList.get(j);
				AddressItem itemCity = new AddressItem();
				itemCity.id 	= jobjectCity.getString("cityId");
				itemCity.name   = jobjectCity.getString("cityName");
				
				JSONArray jarrayAreaList = jobjectCity.getJSONArray("areaList");
				for (int k=0; k<jarrayAreaList.length(); k++)
				{
					JSONObject jobjectArea = (JSONObject)jarrayAreaList.get(k);
					AddressItem itemArea = new AddressItem();
					itemArea.id 	= jobjectArea.getString("areaId");
					itemArea.name   = jobjectArea.getString("areaName");
					itemCity.addressList.add(itemArea);
				}
				itemProvince.addressList.add(itemCity);
			}
			entity.addressList.add(itemProvince);
		}
		

		return entity;

	}

}
