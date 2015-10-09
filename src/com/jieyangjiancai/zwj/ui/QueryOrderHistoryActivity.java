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
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.common.ToastMessage;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.data.OrderItem;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.GetOrder;
import com.jieyangjiancai.zwj.ui.views.ShowZoomView;
import com.jieyangjiancai.zwj.views.PullToRefreshView;
import com.jieyangjiancai.zwj.views.PullToRefreshView.OnFooterRefreshListener;
import com.jieyangjiancai.zwj.views.PullToRefreshView.OnHeaderRefreshListener;

public class QueryOrderHistoryActivity extends BaseActivity implements OnClickListener  {
	
	private String ORDER_COUNT_PER_PAGE = ConfigUtil.mPageSize;
	private RelativeLayout mLayoutProgress;
	private ArrayList<ImageView> mImageList = new ArrayList<ImageView>();
	//private ImageView mImageViewFull;
	
	private ListView mListView;
	private MyAdapter mMyAdapter;
	private ArrayList<OrderItem> mListOrder = new ArrayList<OrderItem>();
	private HashMap<String, Bitmap> mMapBitmap = new HashMap<String, Bitmap>();
	
	private PullToRefreshView mListViewPull; 
	private String mFinalId = "";
	
	private ShowZoomView mShowZoomView;
	private Context mContext;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qureyorder_history);
        mContext = this;
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mFinalId = "";
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
		title.setText("询单记录");
		
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
		mListView = (ListView)findViewById(R.id.list_queryorder);
		mMyAdapter = new MyAdapter(this);
		mListView.setAdapter(mMyAdapter);
		
		mListViewPull = (PullToRefreshView)findViewById(R.id.pull_refresh_view);
		mListViewPull.setOnHeaderRefreshListener(new OnHeaderRefreshListener (){
			public void onHeaderRefresh(PullToRefreshView view) {
				//刷新，重新获取数据
				mFinalId = "";
				FetchOrderList();
				
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
				
				FetchOrderList();
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
        FetchOrderList();
	}
	
	private void ShowMessageList(int index)
	{
		OrderItem orderItem = mListOrder.get(index);
		Intent intent = new Intent(this, MessageListActivity.class);
	    intent.putExtra("order_message_id", orderItem.order_message_id);
	    intent.putExtra("type", "reply");
		this.startActivity(intent);
	}

	private void ShowDetailOrder(OrderItem item)
	{
		Intent intent = new Intent(this, QueryOrderDetailPriceActivity.class);
		intent.putExtra("order_status", item.order_status);
	    intent.putExtra("show_time", item.show_time);
	    intent.putExtra("order_id", item.order_id);
	    intent.putExtra("content", item.content);
	    intent.putExtra("order_status_content", item.order_status_content);
	    if (item.picture_arr.size()>0)
	    {
	    	for(int i=0; i<item.picture_arr.size(); i++)
	    	{
	    		intent.putExtra("picture"+(i+1), item.picture_arr.get(i).path);
	    		intent.putExtra("thumbpic"+(i+1), item.picture_arr.get(i).thumb);
	    	}
	    }
	    if (item.payment_voucher_picture_arr.size()>0)
	    {
	    	for(int i=0; i<item.payment_voucher_picture_arr.size(); i++)
	    	{
	    		intent.putExtra("payment_picture"+(i+1), item.payment_voucher_picture_arr.get(i).path);
	    		intent.putExtra("thumbpaypic"+(i+1), item.payment_voucher_picture_arr.get(i).thumb);
	    	}
	    }
	    intent.putExtra("order_message_id", item.order_message_id);
	    intent.putExtra("supplier_id", item.supplier_id);
		startActivityForResult(intent, 1);
	}
	
	
	private void FetchOrderList()
	{
		mLayoutProgress.setVisibility(View.VISIBLE);

		String userId = ConfigUtil.mUserId;
		String token = ConfigUtil.mToken;
		String pageSize = ORDER_COUNT_PER_PAGE;
		String finalId = mFinalId;
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.FetchOrderList(userId, token, pageSize, finalId, reqOrderSuccessListener(), reqOrderErrorListener());
	}
	private Response.Listener<JSONObject> reqOrderSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				//ToastMessage.show(QueryOrderHistoryActivity.this, "获取询单记录成功");
				try {
					String error = response.getString("error");
					if("0".equals(error)){
						if (mFinalId.equals(""))
							mListOrder.clear();
						GetOrder getOrder = GetOrder.parse(response);
						ArrayList<OrderItem> listOrder = getOrder.GetListOrder();
						if (listOrder == null || listOrder.size() <= 0)
							mListViewPull.StopFooterRefresh(true);
						else
						{
							int finalId = -1;
							int tempFinalId = 0;
							for (int i=0; i<listOrder.size(); i++)
							{
								mListOrder.add(listOrder.get(i));
								mFinalId = listOrder.get(i).order_message_id;
								tempFinalId = Integer.valueOf(mFinalId);
								if (finalId == -1)
								{
									finalId = tempFinalId;
								}
								else
								{
									if (tempFinalId < finalId)
										finalId = tempFinalId;
								}
							}
						
							mFinalId = String.valueOf(finalId);
						}
						mMyAdapter.notifyDataSetChanged();
					}
					else if("2".equals(error)){
						//token 过期
						ToastMessage.show(mContext, response.getString("errormsg"));
						if(mContext != null){
							ConfigUtil.setLoginInfo("", "", "", 0);
							finish();
						}
					}
					else{
						if(mContext != null){
							ToastMessage.show(mContext, response.getString("errormsg"));
							finish();
						}
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
				//ToastMessage.show(QueryOrderHistoryActivity.this, "获取询单记录失败");
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
			if (mListOrder == null || mListOrder.size() <= 0)
				return 0;
			return mListOrder.size();
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
				convertView = mInflater.inflate(R.layout.queryorder_item, parent, false);
				//vh.textMyPrice = (TextView)convertView.findViewById(R.id.text_my_price);
				vh.textStatus 	= (TextView)convertView.findViewById(R.id.text_status);
				vh.text_lable_price = (TextView)convertView.findViewById(R.id.text_lable_price);
				vh.text_deal_price = (TextView)convertView.findViewById(R.id.text_deal_price);
				vh.textTime 	= (TextView)convertView.findViewById(R.id.text_time);
				vh.textOrderId 	= (TextView)convertView.findViewById(R.id.text_order_id);
				vh.imageView1 	= (ImageView)convertView.findViewById(R.id.image_content1);
				vh.imageView2 	= (ImageView)convertView.findViewById(R.id.image_content2);
				vh.imageView3 	= (ImageView)convertView.findViewById(R.id.image_content3);
				vh.textRemark	= (TextView)convertView.findViewById(R.id.text_remark);
				vh.textStatusContent = (TextView)convertView.findViewById(R.id.text_status_content);
				vh.layoutMessage = (RelativeLayout)convertView.findViewById(R.id.layout_message);
				vh.layoutDetail = (RelativeLayout)convertView.findViewById(R.id.layout_detail);
				vh.textSupplier	= (TextView)convertView.findViewById(R.id.text_supplier);
				
				mImageList.add(vh.imageView1);
				mImageList.add(vh.imageView2);
				mImageList.add(vh.imageView3);
				
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
			
			OrderItem orderItem = mListOrder.get(position);
			
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
			vh.textStatusContent.setText(orderItem.order_status_content);
	        if (orderItem.order_status_content == null || orderItem.order_status_content.equals(""))
	        	vh.textStatusContent.setVisibility(View.GONE);
	        else
	        	vh.textStatusContent.setVisibility(View.VISIBLE);
	        
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
			
			
			if (orderItem.order_status.equals("0")) //"抢单中"
			{
				vh.textSupplier.setText("已经有"+orderItem.price_count+"人报价，查看详细报价");
			}
			else {
				vh.textSupplier.setText(ConfigUtil.getStatus(orderItem.order_status));
				
			}
				
			if (orderItem.message_count > 0)
			{
				vh.layoutMessage.setVisibility(View.VISIBLE);
				vh.layoutMessage.setTag(position);
				vh.layoutMessage.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ShowMessageList((Integer)v.getTag());
					}
				});
			}
			else
				vh.layoutMessage.setVisibility(View.GONE);
			
			
			int priceCount = Integer.valueOf(orderItem.price_count);
			if (priceCount <= 0)
				vh.layoutDetail.setVisibility(View.GONE);
			else
			{
				vh.layoutDetail.setVisibility(View.VISIBLE);
				vh.layoutDetail.setTag(position);
				vh.layoutDetail.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (mListOrder != null)
						{
							int position = (Integer)v.getTag();
							if (mListOrder.size() > position)
							{
								OrderItem orderItem = mListOrder.get(position);
//								if (orderItem.order_status.equals("0") || //"抢单中"
//								//	orderItem.order_status.equals("4") || 
//									orderItem.order_status.equals("3"))   //"待支付"
//								{
//									ShowDetailOrder(orderItem);
//								}
//								else if (orderItem.order_status.equals("4")) //"待客服确认收款"
//								{
//									//PayOrder(orderItem);
//									ShowDetailOrder(orderItem);
//								}
								ShowDetailOrder(orderItem);
							}
						}
					}
				});
			}
			
            return convertView;
		}
	}
	
	class ViewHolder{
    	TextView textStatus;
    	TextView text_lable_price;
    	TextView text_deal_price;
    	TextView textTime;
    	TextView textOrderId;
    	ImageView imageView1;
    	ImageView imageView2;
    	ImageView imageView3;
    	TextView textRemark;
    	TextView textStatusContent;
    	RelativeLayout layoutMessage;
    	RelativeLayout layoutDetail;
    	TextView textSupplier;
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
				
				//mShowZoomView.ShowZoomImageView(imageView.getDrawable());
				
				String url = (String)v.getTag(R.id.tag_fullpath);
				mShowZoomView.ShowZoomImageView(imageView.getDrawable(), url);
			}
		});
	}
	
	
	private void LoadImage(ImageView imageView, String thumbUrl, String url)
	{
		Log.d("wujin", "LoadImage:thumbUrl="+thumbUrl);
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
			
			Log.d("wujin", "handleMessage:requestUrl="+requestUrl);
			
			//Log.d("wujin", "mImageList.size()="+mImageList.size());
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
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == Activity.RESULT_OK) {

			switch (requestCode) {
			case 1:
				finish();
				break;
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
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
