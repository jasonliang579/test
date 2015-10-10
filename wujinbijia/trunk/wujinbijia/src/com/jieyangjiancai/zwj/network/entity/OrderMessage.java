package com.jieyangjiancai.zwj.network.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;
import com.jieyangjiancai.zwj.data.OrderItem;
import com.jieyangjiancai.zwj.data.PictureItem;


public class OrderMessage extends BaseEntity {
	private OrderItem mOrderMessage = new OrderItem();

	public void setOrderMessage(OrderItem order) {
		this.mOrderMessage = order;
	}

	public OrderItem getOrderMessage() {
		return this.mOrderMessage;
	}

	public static OrderMessage parse(JSONObject response) throws JSONException {
		OrderMessage entity = new OrderMessage();
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
		OrderItem order = new OrderItem();
		order.order_message_id = jo.optString("order_message_id", "");
		order.order_id = jo.optString("order_id", "");
		order.title = jo.optString("title", "");
		order.content = jo.optString("content", "");
		order.show_time = jo.optString("show_time", "");
		order.order_status = jo.optString("order_status", "");
		order.order_status_content = jo.optString("order_status_content", "");
		order.deal_price = jo.optString("deal_price", "");
		order.supplier_id = jo.optString("supplier_id", "");
		JSONArray jarrayList = jo.getJSONArray("picture_arr");
		for (int j=0; j<jarrayList.length(); j++)
		{
			PictureItem item = new PictureItem();
			JSONObject jobject = (JSONObject)jarrayList.get(j);
			if (jobject.has("thumb")) 
				item.thumb	= jobject.getString("thumb");
			item.path		= jobject.getString("path");
			item.picture_id	= jobject.optInt("picture_id");
			order.picture_arr.add(item);
		}
		entity.setOrderMessage(order);

		return entity;

	}

}
