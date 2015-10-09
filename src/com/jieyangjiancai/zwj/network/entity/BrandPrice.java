package com.jieyangjiancai.zwj.network.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;
import com.jieyangjiancai.zwj.data.BrandPriceItem;


public class BrandPrice extends BaseEntity {
	public ArrayList<BrandPriceItem> mListBrandPrice = new ArrayList<BrandPriceItem>();

	public static BrandPrice parse(JSONArray response) throws JSONException {
		BrandPrice entity = new BrandPrice();

		//JSONArray jarrayBrandList = new JSONArray(response.toString());
		for (int i=0; i<response.length(); i++)
		{
			BrandPriceItem item = new BrandPriceItem();
			JSONObject jobject = (JSONObject) response.get(i);
			item.brandCode 	= jobject.optString("brandCode");
			item.price	   	= jobject.optDouble("price");
			entity.mListBrandPrice.add(item);
		}
		
		return entity;
	}

}
