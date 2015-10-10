package com.jieyangjiancai.zwj.network.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;
import com.jieyangjiancai.zwj.data.OrderItem;
import com.jieyangjiancai.zwj.data.PictureItem;


public class RebateList extends BaseEntity {
	private ArrayList<RebateItem> mListRebateItem = new ArrayList<RebateItem>();

	public ArrayList<RebateItem> GetListRebate()
	{
		return mListRebateItem;
	}
	
	public static RebateList parse(JSONObject response) throws JSONException {
		RebateList entity = new RebateList();
		int error = response.getInt("error");
		entity.setError(error);
		if(error != 0){
			entity.setErrorText(response.optString("errormsg", ""));
			return entity;
		}
		
		if (!response.has("data")) {
			return null;
		}
		
		JSONArray jArray = response.getJSONArray("data");
		if (jArray != null)
		{
			for (int i=0; i<jArray.length(); i++)
			{
				RebateItem item = new RebateItem();
				JSONObject jobject 	= (JSONObject) jArray.get(i);
				item.setAmount(jobject.getString("amount"));
				item.setCreateTime(jobject.getString("create_time"));
				item.setOrderId(jobject.getString("order_id"));
				
				entity.mListRebateItem.add(item);
			}
		}

		return entity;

	}

}
