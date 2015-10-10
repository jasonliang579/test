package com.jieyangjiancai.zwj.network.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;
import com.jieyangjiancai.zwj.data.PropertyItem;


public class ProductProperty extends BaseEntity {
	private String propertyId;
	private String propertyName;
	private ArrayList<PropertyItem> mListProperty = new ArrayList<PropertyItem>();

	public void setPropertyId(String str) {
		this.propertyId = str;
	}

	public String getPropertyId() {
		return this.propertyId;
	}

	public void setPropertyName(String str) {
		this.propertyName = str;
	}

	public String getPropertyName() {
		return this.propertyName;
	}

	/*{"error":"0",
	 * "data":
	 * 		[{"property_id":"0",
	 * 		  "property_name":"颜色",
	 * 		  "detail":
	 * 				[{"value":"黑",
	 * 				  "id":1,
	 * 				  "float":"0"}]}],
	 * "errormsg":""}*/
	public static ProductProperty parse(JSONObject response) throws JSONException {
		ProductProperty entity = new ProductProperty();
		int error = response.getInt("error");
		entity.setError(error);
		if(error != 0){
			entity.setErrorText(response.optString("errormsg", ""));
			return entity;
		}
		
		if (!response.has("data")) {
			return null;
		}
		//解析有错
		String str = response.optString("product_id", "");
		entity.setPropertyId(str);
		str = response.optString("product_name", "");
		entity.setPropertyName(str);
		
		
		JSONArray jArray = response.getJSONArray("detail");
		if (jArray != null)
		{
			for (int i=0; i<jArray.length(); i++)
			{
				PropertyItem item = new PropertyItem();
				JSONObject jobject 	= (JSONObject) jArray.get(i);
				item.id 	= jobject.getString("id");
				item.value 	= jobject.getString("value");
				entity.mListProperty.add(item);
			}
		}

		return entity;

	}

}
