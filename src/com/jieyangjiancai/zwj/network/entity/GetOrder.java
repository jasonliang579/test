package com.jieyangjiancai.zwj.network.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;
import com.jieyangjiancai.zwj.data.OrderItem;
import com.jieyangjiancai.zwj.data.PictureItem;


public class GetOrder extends BaseEntity {
	private ArrayList<OrderItem> mListOrderItem = new ArrayList<OrderItem>();

	public ArrayList<OrderItem> GetListOrder()
	{
		return mListOrderItem;
	}
	
	public static GetOrder parse(JSONObject response) throws JSONException {
		GetOrder entity = new GetOrder();
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
				OrderItem item = new OrderItem();
				JSONObject jobject 	= (JSONObject) jArray.get(i);
				item.order_message_id 	= jobject.getString("order_message_id");
				item.title 	= jobject.getString("title");
				item.content 	= jobject.getString("content");
				item.show_time 	= jobject.getString("show_time");
				item.order_status 	= jobject.getString("order_status");
				item.order_status_content 	= jobject.getString("order_status_content");
				item.deal_price 	= jobject.getString("deal_price");
				item.supplier_id 	= jobject.getString("supplier_id");
				item.message_count  = jobject.getInt("message_count");
				JSONArray jarrayList = jobject.getJSONArray("picture_arr");
				for (int j=0; j<jarrayList.length(); j++)
				{
					PictureItem item2 = new PictureItem();
					JSONObject jobject2 = (JSONObject)jarrayList.get(j);
					if (jobject2.has("thumb")) 
						item2.thumb	= jobject2.getString("thumb");
					item2.path		= jobject2.getString("path");
					item2.picture_id	= jobject2.optInt("picture_id");
					item.picture_arr.add(item2);
				}
				JSONArray jarrayList2 = jobject.getJSONArray("payment_voucher_picture_arr");
				for (int j=0; j<jarrayList2.length(); j++)
				{
					PictureItem item2 = new PictureItem();
					JSONObject jobject2 = (JSONObject)jarrayList2.get(j);
					if (jobject2.has("thumb")) 
						item2.thumb	= jobject2.getString("thumb");
					item2.path		= jobject2.getString("path");
					item2.picture_id	= jobject2.optInt("picture_id");
					item.payment_voucher_picture_arr.add(item2);
				}
				
				if (jobject.has("order_id")) {
					item.order_id 	= jobject.getString("order_id");
				}
				if (jobject.has("cur_user_price")) {
					item.cur_user_price 	= jobject.getString("cur_user_price");
				}
				if (jobject.has("price_count")) {
					item.price_count 	= jobject.getString("price_count");
				}
				
				entity.mListOrderItem.add(item);
			}
		}

		return entity;

	}

}
