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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.common.DES;
import com.jieyangjiancai.zwj.common.ToastMessage;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.data.OrderItem;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.URLs;
import com.jieyangjiancai.zwj.network.entity.GetOrder;
import com.jieyangjiancai.zwj.ui.views.ShowZoomView;
import com.jieyangjiancai.zwj.views.PullToRefreshView;
import com.jieyangjiancai.zwj.views.PullToRefreshView.OnFooterRefreshListener;
import com.jieyangjiancai.zwj.views.PullToRefreshView.OnHeaderRefreshListener;

public class GetOrderActivity extends BaseActivity implements OnClickListener {
	
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
	
	
	private GetOrder mGetOrder;
	
	private ShowZoomView mShowZoomView;
	private Context mContext;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_getorder);
        mContext = this;
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

		TextView title = (TextView) findViewById(R.id.title_bar_text);
		title.setText("我要接单");
		ImageView more = (ImageView) findViewById(R.id.title_bar_more);
		more.setVisibility(View.GONE);
		TextView titleMore = (TextView) findViewById(R.id.title_bar_more_text);
		titleMore.setText("接单记录");
		titleMore.setOnClickListener(this);
		
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
		mListView = (ListView)findViewById(R.id.list_getorder);
		mMyAdapter = new MyAdapter(this);
		mListView.setAdapter(mMyAdapter);
		
		mListViewPull = (PullToRefreshView)findViewById(R.id.pull_refresh_view);
		mListViewPull.setOnHeaderRefreshListener(new OnHeaderRefreshListener (){
			public void onHeaderRefresh(PullToRefreshView view) {
				//刷新，重新获取数据
				mFinalId = "";
				GetOrder();
				
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
				
				GetOrder();
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
		mFinalId = "";
        GetOrder();
	}
	
	private void MyOffer(int index)
	{
		//ArrayList<OrderItem> orderList = mGetOrder.GetListOrder();
		OrderItem item = mListOrder.get(index);
			
		Intent intent = new Intent(this, GetOrderMakePriceActivity.class);
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
	    intent.putExtra("order_message_id", item.order_message_id);
	    
		this.startActivityForResult(intent, 1);
	}
	
	//获取询单消息（接单，只能看别人发的询单）
	private void GetOrder()
	{
		mLayoutProgress.setVisibility(View.VISIBLE);
		
		String userId = ConfigUtil.mUserId;
		String token = ConfigUtil.mToken;
		String pageSize = ORDER_COUNT_PER_PAGE;
		String finalId = mFinalId;
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.GetOrder(userId, token, pageSize,finalId, reqOrderSuccessListener(), reqOrderErrorListener());
	}
	
	private Response.Listener<JSONObject> reqOrderSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
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
							for (int i=0; i<listOrder.size(); i++)
							{
								mListOrder.add(listOrder.get(i));
								mFinalId = listOrder.get(i).order_message_id;
							}
							
							mMyAdapter.notifyDataSetChanged();
						}
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
			
			ViewHolder vh = null;
			if(convertView == null) {
				vh = new ViewHolder();
				convertView = mInflater.inflate(R.layout.getorder_item, parent, false);
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
				//vh.layoutDetail = (RelativeLayout)convertView.findViewById(R.id.layout_detail);
				//vh.textSupplier	= (TextView)convertView.findViewById(R.id.text_supplier);
				vh.textMakePriceCount = (TextView)convertView.findViewById(R.id.text_make_price_count);
				vh.layoutMessage= (LinearLayout)convertView.findViewById(R.id.layout_message);
				vh.layoutOffer  = (LinearLayout)convertView.findViewById(R.id.layout_offer);
				vh.btnMakePrice = (Button)convertView.findViewById(R.id.btn_makeprice);
                vh.btnMakeShare = (Button)convertView.findViewById(R.id.btn_makeshare);
				
				convertView.setTag(vh);
				
				mImageList.add(vh.imageView1);
				mImageList.add(vh.imageView2);
				mImageList.add(vh.imageView3);
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
//	        	LoadImage(vh.imageView1, path, fullPath);
	        	vh.imageView1.setTag(path);
	        	vh.imageView1.setTag(R.id.tag_fullpath, fullPath);
				
				//vh.imageView1.setOnClickListener(null);
				SetBitmapOnClickListener(vh.imageView1, bitmap);
				vh.imageView1.setVisibility(View.VISIBLE);
	        }
	        else
	        {
	        	vh.imageView1.setImageBitmap(null);
	        	vh.imageView1.setOnClickListener(null);
	        	vh.imageView1.setVisibility(View.INVISIBLE);
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
//	        	LoadImage(vh.imageView2, path, fullPath);
				
				SetBitmapOnClickListener(vh.imageView2, bitmap);
				vh.imageView2.setVisibility(View.VISIBLE);
	        }
	        else
	        {
	        	vh.imageView2.setVisibility(View.INVISIBLE);
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
//	        	LoadImage(vh.imageView3, path, fullPath);
				
				SetBitmapOnClickListener(vh.imageView3, bitmap);
				vh.imageView3.setVisibility(View.VISIBLE);
	        }
	        else
	        {
	        	vh.imageView3.setImageBitmap(null);
	        	vh.imageView3.setOnClickListener(null);
	        	vh.imageView3.setVisibility(View.INVISIBLE);
	        }
	        
			vh.textRemark.setText(orderItem.content);
			
			vh.layoutMessage.setTag(position);
			vh.layoutMessage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub 
					ShowMessageList((Integer)v.getTag());
				}
			});
			
			if (!orderItem.order_status.equals("0"))
			{
				vh.textMakePriceCount.setVisibility(View.GONE);
				vh.layoutOffer.setVisibility(View.GONE);
			}
			else
			{
				vh.textMakePriceCount.setVisibility(View.VISIBLE);
				vh.layoutOffer.setVisibility(View.VISIBLE);
				
				int priceCount = Integer.valueOf(orderItem.price_count);
				if (priceCount > 0)
				{
					String str = "已有"+orderItem.price_count+"人报价";
			        SpannableStringBuilder style=new SpannableStringBuilder(str); 
			        style.setSpan(new ForegroundColorSpan(Color.RED), 2, 2 + orderItem.price_count.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE ); 
			        vh.textMakePriceCount.setText(style);
			        vh.textMakePriceCount.setVisibility(View.VISIBLE);
				}
				else
					vh.textMakePriceCount.setVisibility(View.GONE);
		        
		        vh.btnMakePrice.findViewById(R.id.btn_makeprice);
		        vh.btnMakePrice.setTag(position);
		        vh.btnMakePrice.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub 
						MyOffer((Integer)v.getTag());
					}
				});
		        vh.btnMakeShare.findViewById(R.id.btn_makeshare);
		        vh.btnMakeShare.setTag(position);
		        vh.btnMakeShare.setOnClickListener(new OnClickListener() {
		        	@Override
		        	public void onClick(View v) {
		        		// TODO Auto-generated method stub 
		        		ShowShare((Integer)v.getTag());
		        	}
		        });	
			}
	        
            return convertView;
		}
	}
	
	private void ShowShare(int index)
	{
		
//		http://120.24.94.45:8110/interact/app/giftpack_app.jsp?useId=23
		ConfigUtil.mShareContent = "我们是一个装在手机上的五金城，由广州粤景五金城，车陂瀛富五金城，佛山广佛五金城，华南电器城的5000多家五金经销商共同打造！";
		 if(ConfigUtil.mUserId == null || ConfigUtil.mUserId.length()== 0){
			 ToastMessage.show(this, "请先登录!");
			 return; 
		 }else{
			 String scretKey;
			try {
				OrderItem item = mListOrder.get(index);
				
				scretKey = DES.encryptDES("userId=" + ConfigUtil.mUserId + "&orderMessageId="+ item.order_message_id, "Zhao@vVj");
				ConfigUtil.mShareContentURL = URLs.WEB_SHARE_LOCAL + "?" + scretKey;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ToastMessage.show(this, "分享错误.");
				return;
			}
		 }
			 
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 //oks.disableSSOWhenAuthorize(); 
		 
		 // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//		 oks.setTitle(getString(R.string.share));
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 //oks.setTitleUrl("http://sharesdk.cn");
		 // text是分享文本，所有平台都需要这个字段
//		 oks.setText(ConfigUtil.mShareContent);
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
//		 oks.setUrl(ConfigUtil.mShareContentURL);
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 //oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 //oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 //oks.setSiteUrl("http://sharesdk.cn");
		 
		 String testImage = "/sdcard/wujin/logo.jpg";
		 // oks.setAddress("12345678901");
		 oks.setTitle(getString(R.string.share));
		 oks.setTitleUrl(ConfigUtil.mShareContentURL);
		 oks.setText(ConfigUtil.mShareContent);
		 oks.setImagePath(testImage);
		//oks.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");
		oks.setUrl(ConfigUtil.mShareContentURL);
		oks.setFilePath(testImage);
		oks.setComment(ConfigUtil.mShareContent);
		oks.setSite(getString(R.string.app_name));
		oks.setSiteUrl(ConfigUtil.mShareContentURL);
//			oks.setVenueName("ShareSDK");
//			oks.setVenueDescription("This is a beautiful place!");
//			oks.disableSSOWhenAuthorize();
//			oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
//				public void onShare(Platform platform, ShareParams paramsToShare) {
////						// 改写twitter分享内容中的text字段，否则会超长，
////						// 因为twitter会将图片地址当作文本的一部分去计算长度
////						if ("Twitter".equals(platform.getName())) {
////							paramsToShare.setText(platform.getContext().getString(R.string.share_content_short));
////						}
//				}
//			});
			
		 // 启动分享GUI
		 oks.show(this);
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
    	//RelativeLayout layoutDetail;
    	//TextView textSupplier;
    	TextView textMakePriceCount;
    	LinearLayout layoutMessage;
    	LinearLayout layoutOffer;
    	Button btnMakePrice;
    	Button btnMakeShare;
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
//				mImageViewFull.setBackgroundColor(0xB0000000);
				
				String url = (String)v.getTag(R.id.tag_fullpath);
				mShowZoomView.ShowZoomImageView(imageView.getDrawable(), url);
			}
		});
	}
	
	private void LoadImage(ImageView imageView, String thumbUrl, String url)
	{
		//Log.d("wujin", "LoadImage()");
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
				//Log.d("wujin", "onResponse()");
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
					if (url != null && url.equals(arg0.getRequestUrl()))
					{
//						Bitmap bitmap = mMapBitmap.get(arg0.getRequestUrl());
//						if (bitmap == null || bitmap.isRecycled())
//						{
//							mMapBitmap.put(arg0.getRequestUrl(), arg0.getBitmap());
//						}
						
						Log.d("wujin", "onResponse():setImageBitmap");
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
					
					//Log.d("wujin", "onResponse():setImageBitmap");
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
			Intent intent = new Intent(this, GetOrderMyPriceActivity.class);
			this.startActivity(intent);
			break;
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mShowZoomView.onKeyDown(keyCode, event) == true)
			return true;
		return super.onKeyDown(keyCode, event);
	}
	
	private void ShowMessageList(int index)
	{
		OrderItem orderItem = mListOrder.get(index);
		Intent intent = new Intent(this, MessageListActivity.class);
	    intent.putExtra("order_message_id", orderItem.order_message_id);
	    
		this.startActivity(intent);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == Activity.RESULT_OK) {

			switch (requestCode) {
			case 1:
				InitData();
				break;
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
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
