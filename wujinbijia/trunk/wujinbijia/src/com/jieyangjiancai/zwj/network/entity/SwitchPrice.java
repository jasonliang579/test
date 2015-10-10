package com.jieyangjiancai.zwj.network.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;
import com.jieyangjiancai.zwj.data.SwitchPriceItem;


public class SwitchPrice extends BaseEntity {
	private ArrayList<SwitchPriceItem> mListSwitchPrice = new ArrayList<SwitchPriceItem>();

	public ArrayList<SwitchPriceItem> GetPrices()
	{
		return mListSwitchPrice;
	}
	public static SwitchPrice parse(JSONArray response) throws JSONException {
		SwitchPrice entity = new SwitchPrice();

		for (int i=0; i<response.length(); i++)
		{
			SwitchPriceItem item = new SwitchPriceItem();
			JSONObject jobject 	= (JSONObject) response.get(i);
			item.brandCode 		= jobject.optString("brandCode");
			item.classificationCode = jobject.optString("classificationCode");
			item.id 			= jobject.optInt("id");
			item.kindId 		= jobject.optInt("kindId");
			item.modelCode 		= jobject.optString("modelCode");
			item.modeltypeCode 	= jobject.optString("modeltypeCode");
			item.realPrice	   	= jobject.optDouble("realPrice");
			item.type 			= jobject.optString("type");
			
			entity.mListSwitchPrice.add(item);
		}
		
		return entity;
	}

}
