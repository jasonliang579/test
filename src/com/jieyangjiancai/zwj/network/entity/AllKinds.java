package com.jieyangjiancai.zwj.network.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;
import com.jieyangjiancai.zwj.data.AreaItem;
import com.jieyangjiancai.zwj.data.BrandItem;
import com.jieyangjiancai.zwj.data.CmcRelItem;
import com.jieyangjiancai.zwj.data.StandardItem;


public class AllKinds extends BaseEntity {

	public static AllKinds parse(JSONObject response, 
								ArrayList<BrandItem> listBrands, 
								ArrayList<CmcRelItem> listCmcRels) throws JSONException {
		AllKinds entity = new AllKinds();
//		int error = response.getInt("error");
//		entity.setError(error);
//		if(error != 0){
//			entity.setErrorText(response.optString("errormsg", ""));
//			return entity;
//		}
		
		if (!response.has("allBrand")) {
			return null;
		}

		JSONArray jarrayAllBrand = response.getJSONArray("allBrand");
		JSONArray jarrayAllCmcRel = response.getJSONArray("allCmcRel");
		for (int i=0; i<jarrayAllBrand.length(); i++)
		{
			JSONObject jobject = (JSONObject)jarrayAllBrand.get(i);
			BrandItem brand = new BrandItem();
			brand.code = jobject.getString("code");
			brand.id   = jobject.getInt("id");
			brand.name = jobject.getString("name");
			brand.recommendFlag = jobject.getString("recommendFlag");
			brand.showFlag = jobject.getString("showFlag");
			brand.status = jobject.getString("status");
			listBrands.add(brand);
		}
		for (int i=0; i<jarrayAllCmcRel.length(); i++)
		{
			JSONObject jobject = (JSONObject)jarrayAllCmcRel.get(i);
			CmcRelItem cmcrel = new CmcRelItem();
			cmcrel.id = jobject.getInt("id");
			cmcrel.name = jobject.getString("name");
			JSONArray jarrayStandard = jobject.getJSONArray("modelList");
			
			for (int j=0; j<jarrayStandard.length(); j++)
			{
				JSONObject jostandard = (JSONObject)jarrayStandard.get(j);
				StandardItem standard = new StandardItem();
				standard.cmRelId = jostandard.getInt("cmRelId");
				standard.id 	= jostandard.getInt("id");
				standard.name	 = jostandard.getString("name");
				cmcrel.mListStandard.add(standard);
				
				JSONArray jarrayArea = jostandard.getJSONArray("crosssectionList");
				for (int k=0; k<jarrayArea.length(); k++)
				{
					JSONObject joarea = (JSONObject)jarrayArea.get(k);
					AreaItem area = new AreaItem();
					area.cmcRelId = joarea.getInt("cmcRelId");
					area.id 	= joarea.getInt("id");
					area.name	= joarea.getString("name");
					standard.mListArea.add(area);
				}
			}
			
			listCmcRels.add(cmcrel);
		}
		
		//JSONArray jarray = new JSONArray();
		//jo.toJSONArray(jarray);
		
//		String str = jo.optString("vcode", "");
//		entity.setVcode(str);
//
//		str = jo.optString("is_newuser", "0");
//		entity.setIsNewUser(str);

		return entity;

	}

}
