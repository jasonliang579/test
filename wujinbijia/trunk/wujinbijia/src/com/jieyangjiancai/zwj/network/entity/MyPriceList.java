package com.jieyangjiancai.zwj.network.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;
import com.jieyangjiancai.zwj.data.CustomerItem;
import com.jieyangjiancai.zwj.data.MyPriceItem;
import com.jieyangjiancai.zwj.data.OrderItem;
import com.jieyangjiancai.zwj.data.PictureItem;
import com.jieyangjiancai.zwj.data.SupplierItem;


public class MyPriceList extends BaseEntity {
	private ArrayList<MyPriceItem> mListMyPrice = new ArrayList<MyPriceItem>();

	public ArrayList<MyPriceItem> GetMyPrices()
	{
		return mListMyPrice;
	}
	public static MyPriceList parse(JSONObject response) throws JSONException {
		MyPriceList entity = new MyPriceList();
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
				MyPriceItem item = new MyPriceItem();
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
					if (jobject3.has("thumb")) 
						item3.thumb		= jobject3.getString("thumb");
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
				item.supplier = supplierItem;
				
				if (jOrderItem.has("customer")) {
					JSONObject jCustomerItem  = jOrderItem.getJSONObject("customer");
					CustomerItem customerItem = new CustomerItem();
					customerItem.block_count = jCustomerItem.getString("block_count");
					customerItem.deal_count  = jCustomerItem.getString("deal_count");
					customerItem.businesscard_id = jCustomerItem.getString("businesscard_id");
					customerItem.company_name = jCustomerItem.getString("company_name");
					customerItem.address = jCustomerItem.getString("address");
					customerItem.area_name = jCustomerItem.getString("area_name");
					customerItem.area_code = jCustomerItem.getString("area_code");
					customerItem.city_name = jCustomerItem.getString("city_name");
					customerItem.city_code = jCustomerItem.getString("city_code");
					customerItem.province_name = jCustomerItem.getString("province_name");
					customerItem.province_code = jCustomerItem.getString("province_code");
					customerItem.total_rebate = jCustomerItem.getString("total_rebate");
					customerItem.user_id 	= jCustomerItem.getString("user_id");
					customerItem.user_name = jCustomerItem.getString("user_name");
					customerItem.user_type = jCustomerItem.getString("user_type");
					customerItem.phone = jCustomerItem.getString("phone");
					item.customer = customerItem;
				}

				entity.mListMyPrice.add(item);
			}
		}
		
		return entity;
	}

}
