package com.jieyangjiancai.zwj.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.common.ToastMessage;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.data.OrderSupplierItem;
import com.jieyangjiancai.zwj.data.SupplierItem;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.SupplierList;
import com.jieyangjiancai.zwj.ui.views.ShowZoomView;

public class QueryOrderDetailPriceActivity extends BaseActivity implements OnClickListener {
	private ArrayList<ImageView> mImageList = new ArrayList<ImageView>();
//	private ImageView mImageViewFull;
	private RelativeLayout mLayoutProgress;
	
	private String mOrderMessageId;
	
	private ListView mListView;
	private MyAdapter mMyAdapter;
	private ArrayList<OrderSupplierItem> mListSupplier = new ArrayList<OrderSupplierItem>();
	private HashMap<String, Bitmap> mMapBitmap = new HashMap<String, Bitmap>();
	
	private String mSelectSupplierId;
	private String mOrderStatus;
	private String mSupplierId;
	private Context mContext;
	
	private ShowZoomView mShowZoomView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qureyorder_detail_price);
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
		
		TextView title = (TextView)findViewById(R.id.title_bar_text);
		title.setText("询单报价");
		
//		mImageViewFull = (ImageView)findViewById(R.id.image_full);
//		mImageViewFull.setVisibility(View.INVISIBLE);
//		mImageViewFull.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mImageViewFull.setVisibility(View.INVISIBLE);
//			}
//		});
		
		mListView = (ListView)findViewById(R.id.list_order_detail_price);
		
		LayoutInflater inflater =(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.queryorderselectsupplier_item, mListView, false);
		//view.findViewById(R.id.layout_detail).setVisibility(View.GONE);
		mListView.addHeaderView(view);
		mMyAdapter = new MyAdapter(this);
		mListView.setAdapter(mMyAdapter);
		
		mLayoutProgress = (RelativeLayout)findViewById(R.id.layout_progress_bar);
		mLayoutProgress.setVisibility(View.INVISIBLE);
	}
	
	private void InitData()
	{
		Intent intent = getIntent();
		String order_status = intent.getStringExtra("order_status");
		String show_time 	= intent.getStringExtra("show_time");
		String order_id 	= intent.getStringExtra("order_id");
		String content 		= intent.getStringExtra("content");
		String order_status_content = intent.getStringExtra("order_status_content");
		String picturepath1 = intent.getStringExtra("picture1");
		String picturepath2 = intent.getStringExtra("picture2");
		String picturepath3 = intent.getStringExtra("picture3");
		String thumbpic1 = intent.getStringExtra("thumbpic1");
		String thumbpic2 = intent.getStringExtra("thumbpic2");
		String thumbpic3 = intent.getStringExtra("thumbpic3");
		String payment_picture1 = intent.getStringExtra("payment_picture1");
		String payment_picture2 = intent.getStringExtra("payment_picture2");
		String payment_picture3 = intent.getStringExtra("payment_picture3");
		String thumbpaypic1 = intent.getStringExtra("thumbpaypic1");
		String thumbpaypic2 = intent.getStringExtra("thumbpaypic2");
		String thumbpaypic3 = intent.getStringExtra("thumbpaypic3");
		if (payment_picture1 == null && payment_picture2 == null && payment_picture3 == null)				
		{
			findViewById(R.id.pay_content_title).setVisibility(View.GONE);
		}
		//intent.putExtra("picture1", item.picture_arr.get(0).path);
		mOrderMessageId = intent.getStringExtra("order_message_id");
		mOrderStatus = order_status;
		mSupplierId = intent.getStringExtra("supplier_id");
		
		//View view = inflate.inflate(R.layout.getorder_item, null);
        TextView textView = (TextView)findViewById(R.id.text_status);
        textView.setText(ConfigUtil.getStatus(order_status));
        
        textView = (TextView)findViewById(R.id.text_time);
        textView.setText(ConfigUtil.getFormatedDateTime(Long.valueOf(show_time)));
        
        textView = (TextView)findViewById(R.id.text_order_id);
        textView.setText(order_id);
        
        ImageView imageView = (ImageView)findViewById(R.id.image_content1);
        LoadImage(imageView, thumbpic1, picturepath1);
        imageView = (ImageView)findViewById(R.id.image_content2);
        LoadImage(imageView, thumbpic2, picturepath2);
        imageView = (ImageView)findViewById(R.id.image_content3);
        LoadImage(imageView, thumbpic3, picturepath3);
        
        textView = (TextView)findViewById(R.id.text_remark);
        textView.setText(content);
        
        imageView = (ImageView)findViewById(R.id.image_pay_content1);
        LoadImage(imageView, thumbpaypic1, payment_picture1);
        imageView = (ImageView)findViewById(R.id.image_pay_content2);
        LoadImage(imageView, thumbpaypic2, payment_picture2);
        imageView = (ImageView)findViewById(R.id.image_pay_content3);
        LoadImage(imageView, thumbpaypic3, payment_picture3);
        
        textView = (TextView)findViewById(R.id.text_status_content);
        textView.setText(order_status_content);
        if (order_status_content == null || order_status_content.equals(""))
        	findViewById(R.id.layout_status_content).setVisibility(View.GONE);
        else
        	findViewById(R.id.layout_status_content).setVisibility(View.VISIBLE);
        
        if (mOrderStatus.equals("0") )
        {
        	findViewById(R.id.layout_pay).setVisibility(View.GONE);
        }
        	
        FetchOrderSupplierList();
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
	
	private void FetchOrderSupplierList()
	{
		mLayoutProgress.setVisibility(View.VISIBLE);

		String userId = ConfigUtil.mUserId;
		String token = ConfigUtil.mToken;
		String order_message_id = mOrderMessageId;
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.FetchOrderSupplierList(userId, token, order_message_id, reqOrderSuccessListener(), reqOrderErrorListener());
	}
	private Response.Listener<JSONObject> reqOrderSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				//ToastMessage.show(QueryOrderDetailPriceActivity.this, "获取询单报价列表成功");
				try {
					SupplierList list = SupplierList.parse(response);
					ArrayList<OrderSupplierItem> supplierList = list.GetSupplierList();
					if (mOrderStatus.equals("0") )
						mListSupplier = supplierList;
					else
					{
						for(int i=0; i<supplierList.size(); i++)
						{
							OrderSupplierItem item = supplierList.get(i);
							if (item.supplier.supplier_id.equals(mSupplierId))
							{
								mListSupplier.add(item);
								break;
							}
						}
					}
					 
					mMyAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mLayoutProgress.setVisibility(View.INVISIBLE);
			}
		};
	}
	private Response.ErrorListener reqOrderErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mLayoutProgress.setVisibility(View.INVISIBLE);
				ToastMessage.show(QueryOrderDetailPriceActivity.this, "获取询单报价列表失败");
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
			if (mListSupplier == null || mListSupplier.size() <= 0)
				return 0;
			return mListSupplier.size();
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
				convertView = mInflater.inflate(R.layout.supplier_item, parent, false);
				vh.text_suppliername 	= (TextView)convertView.findViewById(R.id.text_suppliername);
				vh.text_supplier_record = (TextView)convertView.findViewById(R.id.text_supplier_record);
				vh.text_pricetime= (TextView)convertView.findViewById(R.id.text_time);
				vh.text_price 	= (TextView)convertView.findViewById(R.id.text_price);
				vh.layout_images = (LinearLayout)convertView.findViewById(R.id.layout_images);
				vh.image_content1 	= (ImageView)convertView.findViewById(R.id.image_content1);
				vh.image_content2 	= (ImageView)convertView.findViewById(R.id.image_content2);
				vh.image_content3 	= (ImageView)convertView.findViewById(R.id.image_content3);
				vh.text_remark	= (TextView)convertView.findViewById(R.id.text_remark);
				vh.text_deliver_place = (TextView)convertView.findViewById(R.id.text_deliver_place);
				vh.layout_deliver_place = (LinearLayout)convertView.findViewById(R.id.layout_deliver_place);
				vh.layout_supplier_connect = (LinearLayout)convertView.findViewById(R.id.layout_supplier_connect);
				vh.text_supplier_name 	= (TextView)convertView.findViewById(R.id.text_supplier_name);
				vh.text_supplier_company= (TextView)convertView.findViewById(R.id.text_supplier_company);
				vh.text_supplier_phone 	= (TextView)convertView.findViewById(R.id.text_supplier_phone);
				vh.text_supplier_address= (TextView)convertView.findViewById(R.id.text_supplier_address);
				vh.btn_select_supplier = (Button)convertView.findViewById(R.id.btn_select_supplier);
				
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
			
			OrderSupplierItem orderItem = mListSupplier.get(position);
			
			SupplierItem supplierItem = orderItem.supplier;
			//if (supplierItem.supplier_name != null)
			//	vh.text_suppliername.setText(supplierItem.supplier_name);
			//String str = "交易成功"+supplierItem.deal_count+"次  流单"+supplierItem.block_count+"次";
			//vh.text_supplier_record.setText(str);
			
			vh.text_suppliername.setText("供应商");
			vh.text_pricetime.setText(ConfigUtil.getFormatedDateTime(Long.valueOf(orderItem.price_time)));
			vh.text_price.setText(orderItem.price+"元");
	        
			String path;
			String fullPath;
			int pictureCount = orderItem.picture_arr.size();
			if (pictureCount >= 1)
				vh.layout_images.setVisibility(View.VISIBLE);
			else
				vh.layout_images.setVisibility(View.GONE);
	        if (pictureCount >= 1)
	        {
	        	path = orderItem.picture_arr.get(0).thumb;
	        	fullPath= orderItem.picture_arr.get(0).path;
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
	        
	        if (pictureCount >= 2)
	        {
	        	path = orderItem.picture_arr.get(1).thumb;
	        	fullPath= orderItem.picture_arr.get(1).path;
	        	Bitmap bitmap = mMapBitmap.get(path);
				if (bitmap == null || bitmap.isRecycled())
					LoadImage(vh.image_content2, path, fullPath);
				else
					vh.image_content2.setImageBitmap(bitmap);
				vh.image_content2.setTag(path);
				vh.image_content2.setTag(R.id.tag_fullpath, fullPath);
				SetBitmapOnClickListener(vh.image_content2, bitmap);
	        }
	        else
	        	vh.image_content2.setImageBitmap(null);
	        
	        if (pictureCount >= 3)
	        {
	        	path = orderItem.picture_arr.get(2).thumb;
	        	fullPath= orderItem.picture_arr.get(2).path;
	        	Bitmap bitmap = mMapBitmap.get(path);
				if (bitmap == null || bitmap.isRecycled())
					LoadImage(vh.image_content3, path, fullPath);
				else
					vh.image_content3.setImageBitmap(bitmap);
				vh.image_content3.setTag(path);
				vh.image_content3.setTag(R.id.tag_fullpath, fullPath);
				SetBitmapOnClickListener(vh.image_content3, bitmap);
	        }
	        else
	        	vh.image_content3.setImageBitmap(null);
	        
			vh.text_remark.setText(orderItem.remark);
			vh.text_deliver_place.setText(orderItem.deliver_place);
			vh.text_supplier_company.setText(supplierItem.company_name);
			vh.text_supplier_name.setText(supplierItem.supplier_name);
			vh.text_supplier_phone.setText(supplierItem.supplier_phone);
			String address = supplierItem.province_name +
							supplierItem.city_name +
							supplierItem.area_name;
			vh.text_supplier_address.setText(address);
			vh.btn_select_supplier.setTag(supplierItem.supplier_id);
			if (mOrderStatus.equals("3") || //待支付
				mOrderStatus.equals("4")) //待客服确认收款
			{
				vh.layout_supplier_connect.setVisibility(View.VISIBLE);
				vh.layout_deliver_place.setVisibility(View.GONE);
				vh.btn_select_supplier.setText("上传支付凭证");
				vh.btn_select_supplier.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mSelectSupplierId = (String)v.getTag();
						PayOrder();
					}
				});
			}else if(mOrderStatus.equals("5")){//确认收货
				vh.layout_supplier_connect.setVisibility(View.VISIBLE);
				vh.layout_deliver_place.setVisibility(View.GONE);
				vh.btn_select_supplier.setText("确认收货");
				vh.btn_select_supplier.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						confirmReceipt();
					}
				});
			}
			else
			{
				vh.layout_supplier_connect.setVisibility(View.GONE);
				vh.layout_deliver_place.setVisibility(View.VISIBLE);
				vh.btn_select_supplier.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ShowSelectDialog(QueryOrderDetailPriceActivity.this);
						mSelectSupplierId = (String)v.getTag();
					}
				});
			}
			
			if (mOrderStatus.equals("0") || 
					mOrderStatus.equals("3") ||
					mOrderStatus.equals("4") ||
					mOrderStatus.equals("5"))
			{
				vh.btn_select_supplier.setVisibility(View.VISIBLE);
			}
			else
			{
				vh.btn_select_supplier.setVisibility(View.GONE);
			}
            return convertView;
		}
	}
	
	class ViewHolder{
    	TextView text_suppliername;
    	TextView text_supplier_record;
    	TextView text_pricetime;
    	TextView text_price;
    	LinearLayout layout_images;
    	ImageView image_content1;
    	ImageView image_content2;
    	ImageView image_content3;
    	TextView text_remark;
    	TextView text_deliver_place;
    	
    	ImageView image_pay_content1;
    	ImageView image_pay_content2;
    	ImageView image_pay_content3;
    	
    	LinearLayout layout_deliver_place;
    	LinearLayout layout_supplier_connect;
    	TextView text_supplier_name;
    	TextView text_supplier_company;
    	TextView text_supplier_phone;
    	TextView text_supplier_address;
    	
    	Button btn_select_supplier;
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
		//Log.d("wujin", "LoadImage: thumbUrl= "+thumbUrl+",url="+url);
		String loadUrl = thumbUrl;
		if(thumbUrl == null || thumbUrl.equals(""))
		{
			if(url == null || url.equals(""))
				return;
			else
				loadUrl = url;
		}
		
		imageView.setTag(loadUrl);
		imageView.setTag(R.id.tag_fullpath, url);
		mImageList.add(imageView);
		
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
				for(int i=0; i<mImageList.size(); i++)
				{
					ImageView imageView = mImageList.get(i);
					String url = (String)imageView.getTag();
					//Log.d("wujin", "url="+url);
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
			}
		};
	}
	
	private void SelectSupplier(String supplierId)
	{
		mLayoutProgress.setVisibility(View.VISIBLE);

		String userId = ConfigUtil.mUserId;
		String token = ConfigUtil.mToken;
		String order_message_id = mOrderMessageId;
		String supplier_id = supplierId;
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.SelectSupplier(userId, token, order_message_id, supplier_id, reqSelectSuccessListener(), reqSelectErrorListener());
	}
	private Response.Listener<JSONObject> reqSelectSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				//ToastMessage.show(QueryOrderDetailPriceActivity.this, "选择供应商成功");
				//finish();
				PayOrder();
				
				mLayoutProgress.setVisibility(View.INVISIBLE);
			}
		};
	}
	private Response.ErrorListener reqSelectErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mLayoutProgress.setVisibility(View.INVISIBLE);
				ToastMessage.show(QueryOrderDetailPriceActivity.this, "选择供应商失败");
			}
		};
	}
	
	//确认收货
	private void confirmReceipt()
	{
		mLayoutProgress.setVisibility(View.VISIBLE);

		String userId = ConfigUtil.mUserId;
		String token = ConfigUtil.mToken;
		String order_message_id = mOrderMessageId;
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.confirmReceipt(userId, token, order_message_id, reqConfirmSuccessListener(), reqConfirmErrorListener());
	}
	
	private Response.Listener<JSONObject> reqConfirmSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				mLayoutProgress.setVisibility(View.INVISIBLE);
				if("0".equals(response.optString("error", ""))){
					finish();
				}else{
					if(mContext != null){
						ToastMessage.show(mContext, response.optString("error_msg", "确认收货失败"));
					}
				}
			}
		};
	}
	
	private Response.ErrorListener reqConfirmErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mLayoutProgress.setVisibility(View.INVISIBLE);
				ToastMessage.show(QueryOrderDetailPriceActivity.this, "网络异常");
			}
		};
	}
	
	
	private void ShowSelectDialog(Context context) {  
        AlertDialog.Builder builder = new AlertDialog.Builder(context);  
        builder.setTitle("提示");  
        builder.setMessage("确定选择该供应商？ 确定后，其他供应商的报价将自动失效！");  
        builder.setPositiveButton("确定",  
            new DialogInterface.OnClickListener() {  
                public void onClick(DialogInterface dialog, int whichButton) {  
					SelectSupplier(mSelectSupplierId);
                }  
            });  
        builder.setNegativeButton("取消",  
            new DialogInterface.OnClickListener() {  
                public void onClick(DialogInterface dialog, int whichButton) {  
                }  
            });  
        builder.show();  
    }  
	
	private void PayOrder()
	{
		Intent intent = new Intent(this, PayOrderActivity.class);
		intent.putExtra("order_message_id", mOrderMessageId);
		intent.putExtra("supplier_id", mSelectSupplierId); 
		startActivity(intent);
		finish();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mShowZoomView.onKeyDown(keyCode, event) == true)
			return true;
		return super.onKeyDown(keyCode, event);
	}
}
