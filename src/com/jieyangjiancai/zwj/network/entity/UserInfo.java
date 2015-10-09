package com.jieyangjiancai.zwj.network.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;


public class UserInfo extends BaseEntity {
	private String address;
	private String area_code;
	private String area_name;
	private String block_count;
	private String businesscard_id;
	private String city_code;
	private String city_name;
	private String company_name;
	private String deal_count;
	private String phone;
	private String province_code;
	private String province_name;
	private String total_rebate;
	private String user_name;
	private String user_type;

	public void setAddress(String str) {
		this.address = str;
	}
	public String getAddress() {
		return this.address;
	}
	
	public void setAreaCode(String str) {
		this.area_code = str;
	}
	public String getAreaCode() {
		return this.area_code;
	}
	
	public void setAreaName(String str) {
		this.area_name = str;
	}
	public String getAreaName() {
		return this.area_name;
	}
	
	public void setBlockCount(String str) {
		this.block_count = str;
	}
	public String getBlockCount() {
		return this.block_count;
	}
	
	public void setBusinesscardId(String str) {
		this.businesscard_id = str;
	}
	public String getBusinesscardId() {
		return this.businesscard_id;
	}
	
	public void setCityCode(String str) {
		this.city_code = str;
	}
	public String getCityCode() {
		return this.city_code;
	}
	
	public void setCityName(String str) {
		this.city_name = str;
	}
	public String getCityName() {
		return this.city_name;
	}

	public void setCompanyName(String str) {
		this.company_name = str;
	}
	public String getCompanyName() {
		return this.company_name;
	}
	
	public void setDealCount(String str) {
		this.deal_count = str;
	}
	public String getDealCount() {
		return this.deal_count;
	}
	
	public void setPhone(String str) {
		this.phone = str;
	}
	public String getPhone() {
		return this.phone;
	}
	
	public void setProvinceCode(String str) {
		this.province_code = str;
	}
	public String getProvinceCode() {
		return this.province_code;
	}
	
	public void setProvinceName(String str) {
		this.province_name = str;
	}
	public String getProvinceName() {
		return this.province_name;
	}
	
	public void setTotalRebate(String str) {
		this.total_rebate = str;
	}
	public String getTotalRebate() {
		return this.total_rebate;
	}
	
	public void setUserName(String str) {
		this.user_name = str;
	}
	public String getUserName() {
		return this.user_name;
	}
	
	public void setUserType(String str) {
		this.user_type = str;
	}
	public String getUserType() {
		return this.user_type;
	}


	public static UserInfo parse(JSONObject response) throws JSONException {
		UserInfo entity = new UserInfo();
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
		String str = jo.optString("address", "");
		entity.setAddress(str);
		str = jo.optString("area_code", "");
		entity.setAreaCode(str);
		str = jo.optString("area_name", "");
		entity.setAreaName(str);
		str = jo.optString("block_count", "");
		entity.setBlockCount(str);
		str = jo.optString("businesscard_id", "");
		entity.setBusinesscardId(str);
		str = jo.optString("city_code", "");
		entity.setCityCode(str);
		str = jo.optString("city_name", "");
		entity.setCityName(str);
		str = jo.optString("company_name", "");
		entity.setCompanyName(str);
		str = jo.optString("deal_count", "");
		entity.setDealCount(str);
		str = jo.optString("phone", "");
		entity.setPhone(str);
		str = jo.optString("province_code", "");
		entity.setProvinceCode(str);
		str = jo.optString("province_name", "");
		entity.setProvinceName(str);
		str = jo.optString("total_rebate", "");
		entity.setTotalRebate(str);
		str = jo.optString("user_name", "");
		entity.setUserName(str);
		str = jo.optString("user_type", "");
		entity.setUserType(str);

		return entity;

	}

}
