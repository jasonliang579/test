package com.jieyangjiancai.zwj.network.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;
import com.jieyangjiancai.zwj.data.CartItem;


public class QueryCart extends BaseEntity {
	public ArrayList<CartItem> mListCartItem = new ArrayList<CartItem>();

	
	/*
	 * {"data":
	 * 		[{"amount":1,
	 * 		  "brand_id":1,
	 * 		  "color_name":"黑",
	 * 		  "id":9,
	 * 		  "price":3.2863380,
	 * 		  "product_id":1,
	 * 		  "product_name":"1 K系控制电缆 KVV 4*1",
	 * 		  "property_name_arr":"阻燃（ZR/ZC）,双色地线"}],
	 * "error":"0",
	 * "errormsg":""}
	 */
	public static QueryCart parse(JSONObject response) throws JSONException {
		QueryCart entity = new QueryCart();
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
				CartItem item = new CartItem();
				JSONObject jobject 	= (JSONObject) jArray.get(i);
				item.amount 	= jobject.optInt("amount",0);
				item.brandId 	= jobject.optString("brand_id","");
				item.colorName 	= jobject.optString("color_name","");
				item.id 	= jobject.optString("id","");
				item.price 	= jobject.optDouble("price", 0);
				item.productId 	= jobject.optString("product_id","");
				item.productName 	= jobject.optString("product_name","");
				item.propertyNameArr 	= jobject.optString("property_name_arr","");
				entity.mListCartItem.add(item);

			}
		}
		
		return entity;
	}

}
