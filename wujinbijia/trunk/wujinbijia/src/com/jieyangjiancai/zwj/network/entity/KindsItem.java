package com.jieyangjiancai.zwj.network.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;
import com.jieyangjiancai.zwj.data.KindItem;
import com.jieyangjiancai.zwj.data.KindItemList;

public class KindsItem extends BaseEntity {
	private ArrayList<KindItemList> mKindItemList = new ArrayList<KindItemList>();

	public ArrayList<KindItemList> GetKindItemList()
	{
		return mKindItemList;
	}
	
	public static KindsItem parse(JSONObject response) throws JSONException {
		KindsItem entity = new KindsItem();
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
				KindItemList item = new KindItemList();
				JSONObject jobject 	= (JSONObject) jArray.get(i);
				item.code 	= jobject.optString("code","");
				item.id 	= jobject.optString("id","");
				item.name 	= jobject.optString("name","");
				item.version 	= jobject.optString("version","");
				
				if (jobject.has("kindItemList")) 
				{
					JSONArray jArrayKindItemList = jobject.getJSONArray("kindItemList");
					for(int j=0; j<jArrayKindItemList.length(); j++)
					{
						JSONObject jobjectItem = (JSONObject)jArrayKindItemList.get(j);
						KindItem kindItem = new KindItem();
						kindItem.id		  = jobjectItem.optString("id","");
						kindItem.itemName = jobjectItem.optString("itemName","");
						
						item.kindItemList.add(kindItem);
					}
				}
				
				entity.mKindItemList.add(item);
			}
		}

		return entity;

	}

}
