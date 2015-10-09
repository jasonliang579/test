package com.jieyangjiancai.zwj.network;

import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;

public class KMJsonObjectRequest extends JsonObjectRequest{

	public KMJsonObjectRequest(String url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener) {
		super(url, jsonRequest, listener, errorListener);
		// TODO Auto-generated constructor stub
	}

	public KMJsonObjectRequest(int method, String url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
		// TODO Auto-generated constructor stub
	}

	@Override
	public com.android.volley.Request.Priority getPriority() {
		// TODO Auto-generated method stub
		return super.getPriority();
	}

}
