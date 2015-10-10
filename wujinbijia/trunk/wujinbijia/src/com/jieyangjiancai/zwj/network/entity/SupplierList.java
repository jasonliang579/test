package com.jieyangjiancai.zwj.network.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;
import com.jieyangjiancai.zwj.data.OrderItem;
import com.jieyangjiancai.zwj.data.OrderSupplierItem;
import com.jieyangjiancai.zwj.data.PictureItem;
import com.jieyangjiancai.zwj.data.SupplierItem;

public class SupplierList extends BaseEntity {
	private ArrayList<OrderSupplierItem> mListSupplierItem = new ArrayList<OrderSupplierItem>();

	public ArrayList<OrderSupplierItem> GetSupplierList()
	{
		return mListSupplierItem;
	}
	
	public static SupplierList parse(JSONObject response) throws JSONException {
		SupplierList entity = new SupplierList();
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
				JSONObject jobject 	= (JSONObject) jArray.get(i);
				OrderSupplierItem item 		= new OrderSupplierItem();
				item.deliver_place 			= jobject.optString("deliver_place");
				item.order_message_price_id = jobject.optString("order_message_price_id");
				item.price 					= jobject.optString("price");
				item.price_time 			= jobject.optString("price_time");
				item.remark 				= jobject.optString("remark");
				
				
				JSONArray jarrayList 		= jobject.getJSONArray("picture_arr");
				for (int j=0; j<jarrayList.length(); j++)
				{
					PictureItem item2 = new PictureItem();
					JSONObject jobject2 = (JSONObject)jarrayList.get(j);
					if (jobject2.has("thumb")) 
						item2.thumb		= jobject2.getString("thumb");
					item2.path			= jobject2.getString("path");
					item2.picture_id	= jobject2.optInt("picture_id");
					item.picture_arr.add(item2);
				}
				
				JSONObject jOrderItem 	= jobject.getJSONObject("order_message");
				OrderItem orderItem = new OrderItem();
				orderItem.content		= jOrderItem.getString("content");
				orderItem.deal_price	= jOrderItem.getString("deal_price");
				orderItem.order_id		= jOrderItem.getString("order_id");
				orderItem.order_message_id	= jOrderItem.getString("order_message_id");
				orderItem.order_status	= jOrderItem.getString("order_status");
				orderItem.order_status_content = jOrderItem.getString("order_status_content");
				orderItem.show_time		= jOrderItem.getString("show_time");
				orderItem.title			= jOrderItem.getString("title");
				JSONArray jarrayList2 	= jOrderItem.getJSONArray("picture_arr");
				for (int k=0; k<jarrayList2.length(); k++)
				{
					PictureItem item3 	= new PictureItem();
					JSONObject jobject3 = (JSONObject)jarrayList2.get(k);
					item3.path			= jobject3.getString("path");
					item3.picture_id	= jobject3.optInt("picture_id");
					orderItem.picture_arr.add(item3);
				}
				item.order_message = orderItem;
				
				JSONObject jSupplierItem  = jobject.getJSONObject("supplier");
				SupplierItem supplierItem = new SupplierItem();
				supplierItem.block_count = jSupplierItem.getString("block_count");
				supplierItem.deal_count  = jSupplierItem.getString("deal_count");
				supplierItem.supplier_id = jSupplierItem.getString("supplier_id");
				if (jSupplierItem.has("supplier_name"))
					supplierItem.supplier_name = jSupplierItem.getString("supplier_name");
				if (jSupplierItem.has("phone"))
					supplierItem.supplier_phone = jSupplierItem.getString("phone");
				if (jSupplierItem.has("company_name"))
					supplierItem.company_name = jSupplierItem.getString("company_name");
				if (jSupplierItem.has("address"))
					supplierItem.address = jSupplierItem.getString("address");
				if (jSupplierItem.has("area_name"))
					supplierItem.area_name = jSupplierItem.getString("area_name");
				if (jSupplierItem.has("city_name"))
					supplierItem.city_name = jSupplierItem.getString("city_name");
				if (jSupplierItem.has("province_name"))
					supplierItem.province_name = jSupplierItem.getString("province_name");
				
				item.supplier = supplierItem;
				
				entity.mListSupplierItem.add(item);
			}
		}
		
		return entity;
	}

}
