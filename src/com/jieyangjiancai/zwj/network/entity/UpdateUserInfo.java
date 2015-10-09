package com.jieyangjiancai.zwj.network.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;

/**
 * 增加/修改用户其它信息
 * 
 * @author hlai
 * 
 */
public class UpdateUserInfo extends BaseEntity {

	private String address;
	private String areaCode;
	private String areaName;
	private String businesscardId;
	private String cityCode;
	private String cityName;
	private String companyName;
	private String phone;
	private String provinceCode;
	private String provinceName;
	private String userName;
	private String userType;
	
	public void setAddress(String str) {
		this.address = str;
	}
	public String getAddress() {
		return this.address;
	}

	public void setAreaCode(String str) {
		this.areaCode = str;
	}
	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaName(String str) {
		this.areaName = str;
	}
	public String getAreaName() {
		return this.areaName;
	}

	public void setBusinesscardId(String str) {
		this.businesscardId = str;
	}
	public String getBusinesscardId() {
		return this.businesscardId;
	}

	public void setCityCode(String str) {
		this.cityCode = str;
	}
	public String getCityCode() {
		return this.cityCode;
	}

	public void setCityName(String str) {
		this.cityName = str;
	}
	public String getCityName() {
		return this.cityName;
	}

	public void setCompanyName(String str) {
		this.companyName = str;
	}
	public String getCompanyName() {
		return this.companyName;
	}

	public void setPhone(String str) {
		this.phone = str;
	}
	public String getPhone() {
		return this.phone;
	}

	public void setProvinceCode(String str) {
		this.provinceCode = str;
	}
	public String getProvinceCode() {
		return this.provinceCode;
	}

	public void setProvinceName(String str) {
		this.provinceName = str;
	}
	public String getProvinceName() {
		return this.provinceName;
	}

	public void setUserName(String str) {
		this.userName = str;
	}
	public String getUserName() {
		return this.userName;
	}

	public void setUserType(String str) {
		this.userType = str;
	}
	public String getUserType() {
		return this.userType;
	}	
	
	public static UpdateUserInfo parse(JSONObject response) throws JSONException {
		//{"data":
		//		{"address":"","area_code":"75504","area_name":"福田","businesscard_id":0,
		//			"city_code":"755","city_name":"深圳市",
		//			"company_name":"","phone":"13929500000","province_code":"1","province_name":"广东省",
		//			"user_id":24,"user_name":"","user_type":1},
		//"error":"0",
		//"errormsg":""}
		
		UpdateUserInfo entity = new UpdateUserInfo();
		int error = response.getInt("error");
		entity.setError(error);
		if (error != 0) {
			entity.setErrorText(response.optString("errormsg", ""));
			return entity;
		}

		if (!response.has("data")) {
			return null;
		}
		JSONObject jo = response.getJSONObject("data");
		String str = jo.optString("address", "");
		entity.setAddress(str);

		str = jo.optString("area_code", "");
		entity.setAreaCode(str);
		str = jo.optString("area_name", "");
		entity.setAreaName(str);
		str = jo.optString("businesscard_id", "");
		entity.setBusinesscardId(str);
		str = jo.optString("city_code", "");
		entity.setCityCode(str);
		str = jo.optString("city_name", "");
		entity.setCityName(str);
		str = jo.optString("company_name", "");
		entity.setCompanyName(str);
		str = jo.optString("phone", "");
		entity.setPhone(str);
		str = jo.optString("province_code", "");
		entity.setProvinceCode(str);
		str = jo.optString("province_name", "");
		entity.setProvinceName(str);
		str = jo.optString("user_name", "");
		entity.setUserName(str);
		str = jo.optString("user_type", "");
		entity.setUserType(str);
		return entity;

	}

}
