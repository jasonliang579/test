package com.jieyangjiancai.zwj.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.data.CustomerItem;
import com.jieyangjiancai.zwj.data.MyPriceItem;
import com.jieyangjiancai.zwj.data.OrderItem;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.MyPriceList;
import com.jieyangjiancai.zwj.ui.views.ShowZoomView;
import com.jieyangjiancai.zwj.views.PullToRefreshView;
import com.jieyangjiancai.zwj.views.PullToRefreshView.OnFooterRefreshListener;
import com.jieyangjiancai.zwj.views.PullToRefreshView.OnHeaderRefreshListener;

public class GetOrderMyPriceActivity extends BaseActivity implements OnClickListener {
	private ArrayList<ImageView> mImageList = new ArrayList<ImageView>();
//	private ImageView mImageViewFull;
	private RelativeLayout mLayoutProgress;
	
	private ListView mListView;
	private MyAdapter mMyAdapter;
	private ArrayList<MyPriceItem> mListMyPrice = new ArrayList<MyPriceItem>();
	
	private String PGAE_COUNT = "2"/*ConfigUtil.mPageSize*/;
	private HashMap<String, Bitmap> mMapBitmap = new HashMap<String, Bitmap>();
	
	private PullToRefreshView mListViewPull; 
	private String mFinalId = "";
	private ShowZoomView mShowZoomView;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_getordermypriceorder);
        
        Init();
    }
	
	private void Init()
	{
		InitUI();
		InitData();
		mShowZoomView = new ShowZoomView(this);
	}
	
	private void InitUI()
	{
		findViewById(R.id.title_bar_back).setOnClickListener(this);
		
		TextView title = (TextView)findViewById(R.id.title_bar_text);
		title.setText("接单记录");
		
//		mImageViewFull = (ImageView)findViewById(R.id.image_full);
//		mImageViewFull.setVisibility(View.INVISIBLE);
//		mImageViewFull.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mImageViewFull.setVisibility(View.INVISIBLE);
//			}
//		});
		
		mLayoutProgress = (RelativeLayout)findViewById(R.id.layout_progress_bar);
		mLayoutProgress.setVisibility(View.INVISIBLE);
		
		InitPullListView();
	}
	private void InitPullListView()
	{
		mListView = (ListView)findViewById(R.id.list_myprice);
		mMyAdapter = new MyAdapter(this);
		mListView.setAdapter(mMyAdapter);
		
		mListViewPull = (PullToRefreshView)findViewById(R.id.pull_refresh_view);
		mListViewPull.setOnHeaderRefreshListener(new OnHeaderRefreshListener (){
			public void onHeaderRefresh(PullToRefreshView view) {
				//3、获取我的询单报价（接单）
				mFinalId = "";
				GetMyPrice();
				
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
				GetMyPrice();
				
				mListViewPull.postDelayed(new Runnable() {
					public void run() {
						mListViewPull.onHeaderRefreshComplete();	
						mMyAdapter.notifyDataSetChanged();
					}
				},6000);
			}
        });
	}
	private void InitData()
	{
		mLayoutProgress.setVisibility(View.VISIBLE);
		//3、获取我的询单报价（接单）
		GetMyPrice();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.title_bar_back:
			finish();
			break;
			
		}
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
						imageView.invalidate();
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
	
	//3、获取我的询单报价（接单）
	private void GetMyPrice()
	{
		String userId = ConfigUtil.mUserId;
		String token = ConfigUtil.mToken;
		String final_id = mFinalId;
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.GetMyPrice(userId, token, PGAE_COUNT, final_id,
				reqSuccessListener(), reqErrorListener());
	}
	
	private Response.Listener<JSONObject> reqSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if (mFinalId.equals(""))
						mListMyPrice.clear();
					
					MyPriceList myPriceList = MyPriceList.parse(response);
					//mListMyPrice = myPriceList.GetMyPrices();
					
					ArrayList<MyPriceItem> listPrice = myPriceList.GetMyPrices();
					if (listPrice == null || listPrice.size() <= 0){
						mListViewPull.StopFooterRefresh(true);
					}
					else
					{
						int finalId = -1;
						int tempFinalId = 0;
						for (int i=0; i<listPrice.size(); i++)
						{
							mListMyPrice.add(listPrice.get(i));
							mFinalId = listPrice.get(i).getOrder_message().getOrder_message_id();
							String s = mFinalId;
							System.out.println(mFinalId);
							/*tempFinalId = Integer.valueOf(mFinalId);
							if (finalId == -1)
							{
								finalId = tempFinalId;
							}
							else
							{
								if (tempFinalId < finalId)
									finalId = tempFinalId;
							}*/
							;
						}
						
//						mFinalId = String.valueOf(finalId);
						
						mMyAdapter.notifyDataSetChanged();
					}
					//ToastMessage.show(MyPriceActivity.this, "获取接单记录成功");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mLayoutProgress.setVisibility(View.INVISIBLE);
				mListViewPull.onHeaderRefreshComplete();
				mListViewPull.onFooterRefreshComplete();
			}
		};
	}
	private Response.ErrorListener reqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mLayoutProgress.setVisibility(View.INVISIBLE);
				mListViewPull.onHeaderRefreshComplete();
				mListViewPull.onFooterRefreshComplete();
				//ToastMessage.show(MyPriceActivity.this, "获取我的询单报价失败");
			}
		};
	}
	
	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		
		public MyAdapter(Context context)
		{ 
			mInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		}  
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (mListMyPrice == null || mListMyPrice.size() <= 0)
				return 0;
			return mListMyPrice.size();
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
			Log.d("wujin", "getView: position="+ position);
			ViewHolder vh = null;
			if(convertView == null) {
				Log.d("wujin", "convertView == null: position="+ position);
				vh = new ViewHolder();
				convertView = mInflater.inflate(R.layout.mypriceorder_item, parent, false);
				vh.textMyPrice = (TextView)convertView.findViewById(R.id.text_my_price);
				vh.textStatus 	= (TextView)convertView.findViewById(R.id.text_status);
				vh.text_lable_price = (TextView)convertView.findViewById(R.id.text_lable_price);
				vh.text_deal_price = (TextView)convertView.findViewById(R.id.text_deal_price);
				vh.textTime 	= (TextView)convertView.findViewById(R.id.text_time);
				vh.textOrderId 	= (TextView)convertView.findViewById(R.id.text_order_id);
				vh.imageView1 	= (ImageView)convertView.findViewById(R.id.image_content1);
				vh.imageView2 	= (ImageView)convertView.findViewById(R.id.image_content2);
				vh.imageView3 	= (ImageView)convertView.findViewById(R.id.image_content3);
				vh.textRemark	= (TextView)convertView.findViewById(R.id.text_remark);
				//vh.textStatusContent 	= (TextView)convertView.findViewById(R.id.text_status_content);
				
				vh.layout_status_content = (LinearLayout)convertView.findViewById(R.id.layout_status_content);
				vh.layout_customer_connect = (LinearLayout)convertView.findViewById(R.id.layout_customer_connect);
				vh.text_customer_name	= (TextView)convertView.findViewById(R.id.text_customer_name);
				vh.text_customer_company= (TextView)convertView.findViewById(R.id.text_customer_company);
				vh.text_customer_phone	= (TextView)convertView.findViewById(R.id.text_customer_phone);
				vh.text_customer_address= (TextView)convertView.findViewById(R.id.text_customer_address);
				vh.text_price_time		= (TextView)convertView.findViewById(R.id.text_price_time);
				vh.image_price_content1	= (ImageView)convertView.findViewById(R.id.image_price_content1);
				vh.image_price_content2	= (ImageView)convertView.findViewById(R.id.image_price_content2);
				vh.image_price_content3	= (ImageView)convertView.findViewById(R.id.image_price_content3);
				vh.text_price_remark	= (TextView)convertView.findViewById(R.id.text_price_remark);
				vh.text_content_status	= (TextView)convertView.findViewById(R.id.text_content_status);
				
				mImageList.add(vh.imageView1);
				mImageList.add(vh.imageView2);
				mImageList.add(vh.imageView3);
				mImageList.add(vh.image_price_content1);
				mImageList.add(vh.image_price_content2);
				mImageList.add(vh.image_price_content3);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
			
			vh.layout_status_content.setVisibility(View.GONE);
			
			MyPriceItem item = mListMyPrice.get(position);
			OrderItem orderItem = item.order_message;
			
			vh.textStatus.setText(ConfigUtil.getStatus(orderItem.order_status));
			if (orderItem.deal_price != null && orderItem.deal_price.trim().length() > 0)
			{
				vh.text_deal_price.setText(orderItem.deal_price + "元 ");
				vh.text_lable_price.setVisibility(View.VISIBLE);
				vh.text_deal_price.setVisibility(View.VISIBLE);
			}
			else
			{
				vh.text_deal_price.setText("");
				vh.text_deal_price.setVisibility(View.GONE);
				vh.text_lable_price.setVisibility(View.GONE);
			}
			vh.textTime.setText(ConfigUtil.getFormatedDateTime(Long.valueOf(orderItem.show_time)));
			vh.textOrderId.setText(orderItem.order_id);
			
	        
	        String path;
	        String fullPath;
			int pictureCount = orderItem.picture_arr.size();
	        if (pictureCount >= 1)
	        {
	        	path = orderItem.picture_arr.get(0).thumb;
	        	fullPath= orderItem.picture_arr.get(0).path;
	        	Bitmap bitmap = mMapBitmap.get(path);
				if (bitmap == null || bitmap.isRecycled())
					LoadImage(vh.imageView1, path, fullPath);
				else
					vh.imageView1.setImageBitmap(bitmap);
				vh.imageView1.setTag(path);
				vh.imageView1.setTag(R.id.tag_fullpath, fullPath);
				SetBitmapOnClickListener(vh.imageView1, bitmap);
	        }
	        else
	        {
	        	vh.imageView1.setImageBitmap(null);
	        	vh.imageView1.setOnClickListener(null);
	        }
	        
	        if (pictureCount >= 2)
	        {
	        	path = orderItem.picture_arr.get(1).thumb;
	        	fullPath= orderItem.picture_arr.get(1).path;
	        	Bitmap bitmap = mMapBitmap.get(path);
				if (bitmap == null || bitmap.isRecycled())
					LoadImage(vh.imageView2, path, fullPath);
				else
					vh.imageView2.setImageBitmap(bitmap);
				vh.imageView2.setTag(path);
				vh.imageView2.setTag(R.id.tag_fullpath, fullPath);
				SetBitmapOnClickListener(vh.imageView2, bitmap);
	        }
	        else
	        {
	        	vh.imageView2.setImageBitmap(null);
	        	vh.imageView2.setOnClickListener(null);
	        }
	        
	        if (pictureCount >= 3)
	        {
	        	path = orderItem.picture_arr.get(2).thumb;
	        	fullPath= orderItem.picture_arr.get(2).path;
	        	Bitmap bitmap = mMapBitmap.get(path);
				if (bitmap == null || bitmap.isRecycled())
					LoadImage(vh.imageView3, path, fullPath);
				else
					vh.imageView3.setImageBitmap(bitmap);
				vh.imageView3.setTag(path);
				vh.imageView3.setTag(R.id.tag_fullpath, fullPath);
				SetBitmapOnClickListener(vh.imageView3, bitmap);
	        }
	        else
	        {
	        	vh.imageView3.setImageBitmap(null);
	        	vh.imageView3.setOnClickListener(null);
	        }
			vh.textRemark.setText(orderItem.content);
			
	        
	        if(orderItem.order_status.equals("3") || orderItem.order_status.equals("4") ||
	        	orderItem.order_status.equals("5") || orderItem.order_status.equals("5"))
	        {
	        	CustomerItem customer = item.customer;
	        	if (customer != null)
	        	{
	        		vh.layout_customer_connect.setVisibility(View.VISIBLE);
					vh.text_customer_name.setText(customer.user_name);
					vh.text_customer_company.setText(customer.company_name);
					vh.text_customer_phone.setText(customer.phone);
					String address = customer.province_name+customer.city_name+customer.area_name+customer.address;
					vh.text_customer_address.setText(address);
	        	}
	        }
	        else
	        	vh.layout_customer_connect.setVisibility(View.GONE);

	        vh.textMyPrice.setText(item.price);
	        vh.text_price_time.setText(ConfigUtil.getFormatedDateTime(Long.valueOf(item.price_time)));
	        if (item.remark == null || item.remark.equals(""))
	        	vh.text_price_remark.setVisibility(View.GONE);
	        else
	        {
	        	vh.text_price_remark.setVisibility(View.VISIBLE);
	        	vh.text_price_remark.setText(item.remark);
	        }
	        if (orderItem.order_status_content == null || orderItem.order_status_content.equals(""))
	        	vh.text_content_status.setVisibility(View.GONE);
	        else
	        {
	        	vh.text_content_status.setVisibility(View.VISIBLE);
	        	vh.text_content_status.setText(orderItem.order_status_content);
	        }
	        
			int picturePriceCount = item.picture_arr.size();
	        if (picturePriceCount >= 1)
	        {
	        	path = item.picture_arr.get(0).thumb;
	        	fullPath= item.picture_arr.get(0).path;
	        	Bitmap bitmap = mMapBitmap.get(path);
				if (bitmap == null || bitmap.isRecycled())
					LoadImage(vh.image_price_content1, path, fullPath);
				else
					vh.image_price_content1.setImageBitmap(bitmap);
				vh.image_price_content1.setTag(path);
				vh.image_price_content1.setTag(R.id.tag_fullpath, fullPath);
				SetBitmapOnClickListener(vh.image_price_content1, bitmap);
	        }
	        else
	        {
	        	vh.image_price_content1.setImageBitmap(null);
	        	vh.image_price_content1.setOnClickListener(null);
	        }
	        
	        if (picturePriceCount >= 2)
	        {
	        	path = item.picture_arr.get(1).thumb;
	        	fullPath= item.picture_arr.get(1).path;
	        	Bitmap bitmap = mMapBitmap.get(path);
				if (bitmap == null || bitmap.isRecycled())
					LoadImage(vh.image_price_content2, path, fullPath);
				else
					vh.image_price_content2.setImageBitmap(bitmap);
				vh.image_price_content2.setTag(path);
				vh.image_price_content2.setTag(R.id.tag_fullpath, fullPath);
				SetBitmapOnClickListener(vh.image_price_content2, bitmap);
	        }
	        else
	        {
	        	vh.image_price_content2.setImageBitmap(null);
	        	vh.image_price_content2.setOnClickListener(null);
	        }
	        
	        if (picturePriceCount >= 3)
	        {
	        	path = item.picture_arr.get(2).thumb;
	        	fullPath= item.picture_arr.get(2).path;
	        	Bitmap bitmap = mMapBitmap.get(path);
				if (bitmap == null || bitmap.isRecycled())
					LoadImage(vh.image_price_content3, path, fullPath);
				else
					vh.image_price_content3.setImageBitmap(bitmap);
				vh.image_price_content3.setTag(path);
				vh.image_price_content3.setTag(R.id.tag_fullpath, fullPath);
				SetBitmapOnClickListener(vh.image_price_content3, bitmap);
	        }
	        else
	        {
	        	vh.image_price_content3.setImageBitmap(null);
	        	vh.image_price_content3.setOnClickListener(null);
	        }
			
            return convertView;
		}
	}
	
	class ViewHolder{
    	TextView textMyPrice;
    	TextView textStatus;
    	//TextView textStatusContent;
    	TextView text_lable_price;
    	TextView text_deal_price;
    	TextView textTime;
    	TextView textOrderId;
    	ImageView imageView1;
    	ImageView imageView2;
    	ImageView imageView3;
    	TextView textRemark;
    	
    	LinearLayout layout_status_content;
    	LinearLayout layout_customer_connect;
    	TextView text_customer_name;
    	TextView text_customer_company;
    	TextView text_customer_phone;
    	TextView text_customer_address;
    	TextView text_price_time;
    	ImageView image_price_content1;
    	ImageView image_price_content2;
    	ImageView image_price_content3;
    	TextView text_price_remark;
    	TextView text_content_status;
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
	
}
