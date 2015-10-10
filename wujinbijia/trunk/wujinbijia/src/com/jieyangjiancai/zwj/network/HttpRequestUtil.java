package com.jieyangjiancai.zwj.network;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class HttpRequestUtil {
	
	private RequestQueue mVolleyQueue;
	private RequestQueue mVolleyQueueUpload;
	private ImageLoader mImageLoader;
	private Context mContext;
	
	public HttpRequestUtil(Context context) {
		// TODO Auto-generated constructor stub
		initVolley(context);
	}
	
	public void initVolley(Context context) {
		// volley 网络库
		mContext = context;
		int max_cache_size = getDefaultLruCacheSize();
		mVolleyQueue = Volley.newRequestQueue(context);
		mVolleyQueueUpload = Volley.newRequestQueue(context, new MultiPartStack());
		mImageLoader = new ImageLoader(mVolleyQueue, new com.jieyangjiancai.zwj.network.DiskBitmapCache(context.getCacheDir(), max_cache_size));
	}
	

	
	/**
	 * 关闭app 使用
	 */
	public void terminate() {
		// 关闭网络请求
		if(mVolleyQueue != null){
			mVolleyQueue.stop();
			mVolleyQueueUpload.stop();
		}
	}
	
	public RequestQueue getHttpQueue() {
		if (mVolleyQueue == null)
			mVolleyQueue = Volley.newRequestQueue(mContext);
		return mVolleyQueue;
	}

	public RequestQueue getUploadHttpQueue() {
		if (mVolleyQueueUpload == null)
			mVolleyQueueUpload = Volley.newRequestQueue(mContext, new MultiPartStack());
		return mVolleyQueueUpload;
	}
	
	public static int getDefaultLruCacheSize() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		//final int cacheSize = maxMemory / 8;
		final int cacheSize = maxMemory;
		return cacheSize;
	}
	
	/**
	 * post request
	 * 
	 * @param builder
	 * @param listener
	 * @param errorListener
	 */
//	public void HttpRequest(Uri.Builder builder, Response.Listener<JSONObject> listener, ErrorListener errorListener) {
//
//		JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, builder.toString(), null, listener, errorListener);
//		jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//		getHttpQueue().add(jsonObjRequest);
//		Log.d("wujin", "HttpRequest: URI="+builder.toString());
//	}
	public void HttpRequest(Uri.Builder builder, Response.Listener<JSONObject> listener, ErrorListener errorListener) {

		JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, builder.toString(), null, listener, errorListener);
		jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
		getHttpQueue().add(jsonObjRequest);
		Log.d("wujin", "HttpRequest: URI="+builder.toString());
	}

	public void HttpRequest2(Uri.Builder builder, Response.Listener<JSONArray> listener, ErrorListener errorListener) {

		JsonArrayRequest jsonObjRequest = new JsonArrayRequest(builder.toString(), listener, errorListener);
		jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
		getHttpQueue().add(jsonObjRequest);

	}
	/**
	 * 下载文件
	 * 
	 * @param url
	 * @param download_path
	 * @param listener
	 * @param errorListener
	 */
	public void downLoadFile(String url, String download_path, Response.Listener<String> listener, ErrorListener errorListener) {
		DownloadRequest downloadRequest = new DownloadRequest(url, download_path, listener, errorListener);
		// downloadRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
		// DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
		// DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		getHttpQueue().add(downloadRequest);
	}

	/**
	 * 上传文件
	 * 
	 * @param files
	 * @param params
	 * @param url
	 * @param listener
	 * @param errorListener
	 */
	public void HttpMultiPartRequst(final Map<String, File> files, final Map<String, String> params, String url,
			Response.Listener<JSONObject> listener, ErrorListener errorListener) {

		MultiPartRequest multiPartStringRequest = new MultiPartRequest(Request.Method.POST, url, listener, errorListener); // {

		for (Map.Entry<String, File> entry : files.entrySet()) {
			multiPartStringRequest.addFileUpload(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<String, String> entry : params.entrySet()) {
			multiPartStringRequest.addStringUpload(entry.getKey(), entry.getValue());
		}
		
		// add retry ,hlai
		multiPartStringRequest.setRetryPolicy(new DefaultRetryPolicy(8000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		//Log.d("wujin", "HttpMultiPartRequst: multiPartStringRequest.getUrl()="+multiPartStringRequest.getUrl());
		getUploadHttpQueue().add(multiPartStringRequest);

	}

	public void ImageLoad(String url, ImageListener listener) {
		mImageLoader.get(url, listener/*
									 * new FadeInImageListener(holder.image,
									 * JSONObjectRequestActvity.this)
									 */);
	}
	

	
	/**
	 * 上传文件
	 * 
	 * @param order_id
	 * @param token
	 */
	public void uploadFile(File file, String token, Response.Listener<JSONObject> listener, ErrorListener errorListener) {

//		 String md5 = FileUtils.calculateMD5(file);
//		 Map<String, String> params = new HashMap<String, String>();
//		 params.put("token", token);
//		 params.put("upload_id", "12312312312");
//		 Map<String, File> files = new HashMap<String, File>();
//		 files.put("photo", file);
//		 String url = mAppDomain + URLs.UPLOAD_FILE;
//		
//		 HttpMultiPartRequst(files, params, url, listener, errorListener);
	}
	

	public void uploadImage(File file, String userId, String token, String type, Response.Listener<JSONObject> listener, ErrorListener errorListener)
	{
//		 String md5 = FileUtils.calculateMD5(file);
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("user_id", userId);
		 params.put("token", token);
		 params.put("index", "1");
		 params.put("type", type); //（1-名片，2-照片下单，3-付款凭证，4-上传报价明细）
		 Map<String, File> files = new HashMap<String, File>();
		 files.put("photo", file);
		 String url = URLs.UPLOAD_IMAGE;
		 
		 Log.d("wujin", "uploadImage: URI="+url);
		
		 HttpMultiPartRequst(files, params, url, listener, errorListener);
	}
	
}
