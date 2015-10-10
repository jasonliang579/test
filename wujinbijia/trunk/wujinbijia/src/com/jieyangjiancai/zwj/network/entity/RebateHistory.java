package com.jieyangjiancai.zwj.network.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;

/**
 *获取优惠金额获取记录
 * 
 * @author hlai
 * 
 */
public class RebateHistory extends BaseEntity {
	private static List<RebateItem> rebateItemList;
	
	public void setRebateItems(List<RebateItem> list) {
		this.rebateItemList = list;
	}
	
	public List<RebateItem> getRebateItems() {
		return this.rebateItemList;
	}

	
	public static RebateHistory parse(JSONObject response) throws JSONException {
		RebateHistory entity = new RebateHistory();
		int error = response.getInt("error");
		entity.setError(error);
		if (error != 0) {
			entity.setErrorText(response.optString("errormsg", ""));
			return entity;
		}
		if (!response.has("data")) {
			return entity;
		}
		JSONArray ja = response.getJSONArray("data");
		rebateItemList = new ArrayList<RebateItem>();
		RebateItem r = null;
		JSONObject jitem;
		for (int i = 0; i < ja.length(); i++){
			jitem = ja.optJSONObject(i);
			r.setOrderId(jitem.optString("order_id", ""));
			r.setAmount(jitem.optString("amount", ""));
			rebateItemList.add(r);
			
		}
		
		entity.setRebateItems(rebateItemList);
		return entity;

	}

}
