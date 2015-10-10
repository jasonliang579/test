package com.jieyangjiancai.zwj.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.common.ToastMessage;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.data.IndustryMessageItem;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.IndustryMessage;
import com.jieyangjiancai.zwj.ui.views.ShowZoomView;
import com.jieyangjiancai.zwj.views.PullToRefreshView;
import com.jieyangjiancai.zwj.views.PullToRefreshView.OnFooterRefreshListener;
import com.jieyangjiancai.zwj.views.PullToRefreshView.OnHeaderRefreshListener;
import com.umeng.analytics.MobclickAgent;

public class IndustryMessageActivity extends Activity implements OnClickListener {
	private String ORDER_COUNT_PER_PAGE = ConfigUtil.mPageSize;
	private RelativeLayout mLayoutProgress;
	
	private ArrayList<ImageView> mImageList = new ArrayList<ImageView>();
//	private ImageView mImageViewFull;
	
	private ListView mListView;
	private MyAdapter mMyAdapter;
	private ArrayList<IndustryMessageItem> mListMessage = new ArrayList<IndustryMessageItem>();
	private HashMap<String, Bitmap> mMapBitmap = new HashMap<String, Bitmap>();
	
	private PullToRefreshView mListViewPull; 
	private String mFinalId = "";
	
	private ShowZoomView mShowZoomView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_industrymessage);
        
		Init();
		InitData();
		mShowZoomView = new ShowZoomView(this);
	}
	
	private void Init()
	{
		findViewById(R.id.title_bar_back).setOnClickListener(this);

		TextView title = (TextView) findViewById(R.id.title_bar_text);
		title.setText("行业圈");
		TextView titleMore = (TextView) findViewById(R.id.title_bar_more_text);
		titleMore.setText("发布信息");
		titleMore.setOnClickListener(this);
		
		mLayoutProgress = (RelativeLayout)findViewById(R.id.layout_progress_bar);
		mLayoutProgress.setVisibility(View.INVISIBLE);
		
//		mImageViewFull = (ImageView)findViewById(R.id.image_full);
//		mImageViewFull.setVisibility(View.INVISIBLE);
//		mImageViewFull.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mImageViewFull.setVisibility(View.INVISIBLE);
//			}
//		});
		
		InitPullListView();
	}
	
	private void InitPullListView()
	{
		mListView = (ListView)findViewById(R.id.list_message);
		mMyAdapter = new MyAdapter(this);
		mListView.setAdapter(mMyAdapter);
		
		mListViewPull = (PullToRefreshView)findViewById(R.id.pull_refresh_view);
		mListViewPull.setOnHeaderRefreshListener(new OnHeaderRefreshListener (){
			public void onHeaderRefresh(PullToRefreshView view) {
				//刷新，重新获取数据
				mFinalId = "";
				FetchIndustryMessage();
				
				// TODO Auto-generated method stub
				mListViewPull.postDelayed(new Runnable() {
					public void run() {
						mListViewPull.onHeaderRefreshComplete();	
						mMyAdapter.notifyDataSetChanged();
					}
				},6000);
			}
        	
        });
		mListViewPull.setOnFooterRefreshListener(new OnFooterRefreshListener(){
			public void onFooterRefresh(PullToRefreshView view) {
				// TODO Auto-generated method stub
				
				FetchIndustryMessage();
				//6秒后如果加载不成功，就关闭加载栏
				mListViewPull.postDelayed(new Runnable() {
					public void run() {
						mListViewPull.onFooterRefreshComplete();
						mMyAdapter.notifyDataSetChanged();
					}
				}, 6000);
			}
        });
		mListViewPull.StopHeaderRefresh(false);
	}
	private void InitData()
	{
		FetchIndustryMessage();
	}
	
	private void FetchIndustryMessage()
	{
		mLayoutProgress.setVisibility(View.VISIBLE);
		
		String pageSize = ORDER_COUNT_PER_PAGE;
		String finalId = mFinalId;
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.FetchIndustryMessage(pageSize,finalId, reqOrderSuccessListener(), reqOrderErrorListener());
	}
	
	private Response.Listener<JSONObject> reqOrderSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if (mFinalId.equals(""))
						mListMessage.clear();
					
					IndustryMessage message = IndustryMessage.parse(response);
					ArrayList<IndustryMessageItem>  listMessage = message.GetListMessage();
					if (listMessage == null || listMessage.size() <= 0)
						mListViewPull.StopFooterRefresh(true);
					else
					{
						for (int i=0; i<listMessage.size(); i++)
						{
							mListMessage.add(listMessage.get(i));
							mFinalId = listMessage.get(i).industry_id;
						}
						
						mMyAdapter.notifyDataSetChanged();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mListViewPull.onHeaderRefreshComplete();
				mListViewPull.onFooterRefreshComplete();
				mLayoutProgress.setVisibility(View.INVISIBLE);
			}
		};
	}
	private Response.ErrorListener reqOrderErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mLayoutProgress.setVisibility(View.INVISIBLE);
				mListViewPull.onHeaderRefreshComplete();
				mListViewPull.onFooterRefreshComplete();
				//ToastMessage.show(GetOrderActivity.this, "获取询单消息失败");
			}
		};
	}
	
	private class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		
		public MyAdapter(Context context)
		{ 
			mInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		}  
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (mListMessage == null || mListMessage.size() <= 0)
				return 0;
			return mListMessage.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			ViewHolder vh = null;
			if(convertView == null) {
				vh = new ViewHolder();
				convertView = mInflater.inflate(R.layout.message_item, parent, false);
				vh.text_address 	= (TextView)convertView.findViewById(R.id.text_address);
				vh.text_name 	= (TextView)convertView.findViewById(R.id.text_name);
				vh.text_remark 	= (TextView)convertView.findViewById(R.id.text_remark);
				vh.image_content1 	= (ImageView)convertView.findViewById(R.id.image_content1);
				vh.text_time	= (TextView)convertView.findViewById(R.id.text_time);
				
				mImageList.add(vh.image_content1);
				convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
			
			IndustryMessageItem item = mListMessage.get(position);
			
			String address = item.userInfo.province_name+item.userInfo.city_name+item.userInfo.area_name;
			vh.text_address.setText(address);
			vh.text_name.setText(item.userInfo.user_name);
			vh.text_remark.setText(item.content);
			vh.text_time.setText(ConfigUtil.getFormatedDateTime(Long.valueOf(item.create_time)));
			
			String path;
			String fullPath;
			int pictureCount = item.picture_arr.size();
	        if (pictureCount >= 1)
	        {
	        	path = item.picture_arr.get(0).thumb;
	        	fullPath= item.picture_arr.get(0).path;
	        	Bitmap bitmap = mMapBitmap.get(path);
				if (bitmap == null || bitmap.isRecycled())
					LoadImage(vh.image_content1, path, fullPath);
				else
					vh.image_content1.setImageBitmap(bitmap);
				vh.image_content1.setTag(path);
				vh.image_content1.setTag(R.id.tag_fullpath, fullPath);
				SetBitmapOnClickListener(vh.image_content1, bitmap);
	        }
	        else
	        	vh.image_content1.setImageBitmap(null);
	        
            return convertView;
		}
	}
	
	class ViewHolder{
    	TextView text_address;
    	TextView text_name;
    	TextView text_remark;
    	ImageView image_content1;
    	TextView text_time;
    } 
	
	private void SetBitmapOnClickListener(ImageView imageView, Bitmap bitmap)
	{
		imageView.setImageBitmap(bitmap);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView imageView = (ImageView)v;
//				mImageViewFull.setImageDrawable(imageView.getDrawable());
//				mImageViewFull.setVisibility(View.VISIBLE);
				String url = (String)v.getTag(R.id.tag_fullpath);
				mShowZoomView.ShowZoomImageView(imageView.getDrawable(), url);
			}
		});
	}
	



	private void LoadImage(ImageView imageView, String thumbUrl, String url)
	{
		String loadUrl = thumbUrl;
		if(thumbUrl == null || thumbUrl.equals(""))
		{
			if(url == null || url.equals(""))
				return;
			else
				loadUrl = url;
		}
		
//		imageView.setTag(thumbUrl);
//		imageView.setTag(R.id.tag_fullpath, url);
//		mImageList.add(imageView);
		
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.ImageLoad(loadUrl, reqImageListener());
	}
	
	private ImageLoader.ImageListener reqImageListener() {
		return new ImageLoader.ImageListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onResponse(ImageContainer arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Message message = mHandler.obtainMessage(0, arg0.getBitmap());  
				Bundle bundle = new Bundle();    
                bundle.putString("requestUrl", arg0.getRequestUrl()); 
                message.setData(bundle);
				mHandler.sendMessage(message); 
				/*
				for(int i=0; i<mImageList.size(); i++)
				{
					ImageView imageView = mImageList.get(i);
					String url = (String)imageView.getTag();
					if (url.equals(arg0.getRequestUrl()))
					{
						Bitmap bitmap = mMapBitmap.get(arg0.getRequestUrl());
						if (bitmap == null || bitmap.isRecycled())
						{
							mMapBitmap.put(arg0.getRequestUrl(), arg0.getBitmap());
						}
						
						imageView.setImageBitmap(arg0.getBitmap());
						imageView.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								ImageView imageView = (ImageView)v;
//								mImageViewFull.setImageDrawable(imageView.getDrawable());
//								mImageViewFull.setVisibility(View.VISIBLE);
								String url = (String)v.getTag(R.id.tag_fullpath);
								mShowZoomView.ShowZoomImageView(imageView.getDrawable(), url);
							}
						});
						break;
					}
				}
				*/
			}
		};
	}
		
	Handler mHandler = new Handler(){
		public void handleMessage(Message message) {  
			
			String requestUrl = message.getData().getString("requestUrl");
			Bitmap msgBitmap = (Bitmap) message.obj;
			
			Log.d("wujin", "mImageList.size()="+mImageList.size());
			for(int i=0; i<mImageList.size(); i++)
			{
				ImageView imageView = mImageList.get(i);
				String url = (String)imageView.getTag();
				if (url != null && url.equals(requestUrl))
				{
					Bitmap bitmap = mMapBitmap.get(requestUrl);
					if (bitmap == null || bitmap.isRecycled())
					{
						mMapBitmap.put(requestUrl, msgBitmap);
					}
					
					Log.d("wujin", "onResponse():setImageBitmap");
					imageView.setImageBitmap(msgBitmap);
					imageView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							ImageView imageView = (ImageView)v;
//							mImageViewFull.setImageDrawable(imageView.getDrawable());
//							mImageViewFull.setVisibility(View.VISIBLE);
							String url = (String)v.getTag(R.id.tag_fullpath);
							mShowZoomView.ShowZoomImageView(imageView.getDrawable(), url);
						}
					});
					imageView.invalidate();
					
					break;
				}
			}
            
        }  
	};
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.title_bar_back:
			finish();
			break;
			
		case R.id.title_bar_more_text:
			if (ConfigUtil.mUserId == null || ConfigUtil.mUserId.equals("")) {
				Intent intent = new Intent(this, LoginActivity.class);
				this.startActivity(intent);
			}
			else if (ConfigUtil.mUserName == null || ConfigUtil.mUserName.equals("")
					|| ConfigUtil.mUserName.trim().equals(""))
			{
				ToastMessage.show(this, "请完善个人资料");
				Intent intent = new Intent(this, PensonInfoActivity.class);
				this.startActivity(intent);
			}
			else
			{
				Intent intent = new Intent(this, SendMessageActivity.class);
				startActivityForResult(intent, 2);
			}
			break;
		}
	}
	
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		switch (resultCode) 
		{ 
		   	case RESULT_OK:
			    if (requestCode == 2)
			    {
			    	mFinalId = "";
			    	FetchIndustryMessage();
			    }
			    break;
		   	default:
		   		break;
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mShowZoomView.onKeyDown(keyCode, event) == true)
			return true;
		return super.onKeyDown(keyCode, event);
	}
	

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Iterator iter = mMapBitmap.entrySet().iterator();
		while(iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			Bitmap bitmap = (Bitmap)entry.getValue();
			if (bitmap != null && !bitmap.isRecycled())
			{
				bitmap.recycle();
				bitmap = null;
			}
		}
	}
	
	@Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
    }
    
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
