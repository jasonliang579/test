package com.jieyangjiancai.zwj.network.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;
import com.jieyangjiancai.zwj.data.IndustryMessageItem;
import com.jieyangjiancai.zwj.data.OrderItem;
import com.jieyangjiancai.zwj.data.PictureItem;
import com.jieyangjiancai.zwj.data.UserItem;


public class IndustryMessage extends BaseEntity {
	private ArrayList<IndustryMessageItem> mListMessageItem = new ArrayList<IndustryMessageItem>();

	public ArrayList<IndustryMessageItem> GetListMessage()
	{
		return mListMessageItem;
	}
	
	public static IndustryMessage parse(JSONObject response) throws JSONException {
		IndustryMessage entity = new IndustryMessage();
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
				
				IndustryMessageItem item = new IndustryMessageItem();
				JSONObject jobject 	= (JSONObject) jArray.get(i);
				item.industry_id 	= jobject.getString("industry_id");
				item.content 	= jobject.getString("content");
				item.create_time 	= jobject.getString("create_time");
				
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
				
				JSONObject jObjectUser	= jobject.getJSONObject("user");
				UserItem userItem = new UserItem();
				if (jObjectUser.has("user_name")) 
					userItem.user_name 		= jObjectUser.getString("user_name");
				userItem.province_name 	= jObjectUser.getString("province_name");
				userItem.city_name 		= jObjectUser.getString("city_name");
				userItem.area_name 		= jObjectUser.getString("area_name");
				item.userInfo = userItem;
				
				entity.mListMessageItem.add(item);
			}
		}

		return entity;

	}

}
