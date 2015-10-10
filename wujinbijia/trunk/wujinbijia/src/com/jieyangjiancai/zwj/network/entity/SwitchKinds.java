package com.jieyangjiancai.zwj.network.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;
import com.jieyangjiancai.zwj.data.SwitchItem;


public class SwitchKinds extends BaseEntity {
	private ArrayList<SwitchItem> mListSwitchItem = new ArrayList<SwitchItem>();

	public ArrayList<SwitchItem> GetListSwitch()
	{
		return mListSwitchItem;
	}
	
	public static SwitchKinds parse(JSONArray response) throws JSONException {
		SwitchKinds entity = new SwitchKinds();

		for (int i=0; i<response.length(); i++)
		{
			SwitchItem item = new SwitchItem();
			JSONObject jobject = (JSONObject) response.get(i);
			item.id 	= jobject.optInt("id");
			item.name	= jobject.optString("name");
			
			JSONArray jarrayList = jobject.getJSONArray("classificationList");
			for (int j=0; j<jarrayList.length(); j++)
			{
				SwitchItem item2 = new SwitchItem();
				JSONObject jobject2 = (JSONObject)jarrayList.get(j);
				item2.id	= jobject2.optInt("id");
				item2.name	= jobject2.optString("name");
				
				JSONArray jarrayList2 = jobject2.getJSONArray("modelList");
				for (int k=0; k<jarrayList2.length(); k++)
				{
					SwitchItem item3 = new SwitchItem();
					JSONObject jobject3 = (JSONObject)jarrayList2.get(k);
					item3.id	= jobject3.optInt("id");
					item3.name	= jobject3.optString("name");
					
					JSONArray jarrayList3 = jobject3.getJSONArray("modeltypeList");
					for (int m=0; m<jarrayList3.length(); m++)
					{
						SwitchItem item4 = new SwitchItem();
						JSONObject jobject4 = (JSONObject)jarrayList3.get(m);
						item4.id	= jobject4.optInt("id");
						item4.name	= jobject4.optString("name");
						item4.productId = jobject4.optString("productId");
						item3.items.add(item4);
					}
					item2.items.add(item3);
				}
				item.items.add(item2);
			}
			entity.mListSwitchItem.add(item);
		}

		return entity;

	}

}
