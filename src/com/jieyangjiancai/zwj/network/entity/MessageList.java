package com.jieyangjiancai.zwj.network.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jieyangjiancai.zwj.base.BaseEntity;
import com.jieyangjiancai.zwj.data.MessageItem;

public class MessageList extends BaseEntity {
	private ArrayList<MessageItem> mListMessageItem = new ArrayList<MessageItem>();

	public ArrayList<MessageItem> GetMessageList()
	{
		return mListMessageItem;
	}
	
	public static MessageList parse(JSONObject response) throws JSONException {
		MessageList entity = new MessageList();
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
				MessageItem item 	= new MessageItem();
				item.message_content = jobject.optString("message_content");
				item.message_id 	= jobject.optString("message_id");
				item.create_time 	= jobject.optString("create_time");
				item.user_company 	= jobject.optString("user_company");
				item.user_id 		= jobject.optString("user_id");
				item.user_name 		= jobject.optString("user_name");
				
				if (jobject.has("reply_message"))
				{
					JSONObject jobjectReplay = jobject.getJSONObject("reply_message");
					if (jobjectReplay != null && jobjectReplay.length() > 0)
					{
						MessageItem replyitem 	= new MessageItem();
						replyitem.message_content = jobjectReplay.optString("message_content");
						replyitem.message_id 	= jobjectReplay.optString("message_id");
						replyitem.create_time 	= jobjectReplay.optString("create_time");
						replyitem.user_company 	= jobjectReplay.optString("user_company");
						replyitem.user_id 		= jobjectReplay.optString("user_id");
						replyitem.user_name 	= jobjectReplay.optString("user_name");
						item.reply_message = replyitem;
					}
					else	
						item.reply_message = null;
				}
				else
					item.reply_message = null;
				
				entity.mListMessageItem.add(item);
			}
		}
		
		return entity;
	}

}
