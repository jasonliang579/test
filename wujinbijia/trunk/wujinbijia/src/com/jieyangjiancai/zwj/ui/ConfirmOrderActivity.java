package com.jieyangjiancai.zwj.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.data.GoodsItem;

public class ConfirmOrderActivity extends BaseActivity implements OnClickListener {
	
	private TextView mReceiverName;
	private TextView mReceiverPhone;
	private TextView mReceiverAddress;
	
	private TextView mCouponCount;
	private TextView mCouponUse;
	
	private TextView mMoney;
	
	private ArrayList<GoodsItem> mGoodsList = new ArrayList<GoodsItem>();
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_confirmorder);
        
        Init();
	}

	private void Init()
	{
		findViewById(R.id.title_bar_back).setOnClickListener(this);
		
		TextView title = (TextView)findViewById(R.id.title_bar_text);
		title.setText("确认订单");
		
		mMoney = (TextView)findViewById(R.id.textview_actual_money);
		
		InitListView();
		
		InitData();
	}
	
	private void InitData()
	{
		Receiver receiver = new Receiver("张三", "135663455334", "广东广州对口高考文科高品位评估机构刚给我个化合物");
		mReceiverName.setText(receiver.mName);
		mReceiverPhone.setText(receiver.mPhone);
		mReceiverAddress.setText("收货地址："+receiver.mAddress);
		
		int count = 0;
		mCouponCount.setText(count + "张可用");
		mCouponUse.setText("未使用");
		
		double money = 999.9;
		mMoney.setText("￥"+money);
		
		GetData();
	}

	private void GetData()
	{
		GoodsItem good = new GoodsItem("广州电缆厂", "绝缘电线", "BVR", "2.5", "999", "4");
		mGoodsList.add(good);
		good = new GoodsItem("上海电缆厂", "电线", "BVR", "4.5", "500", "6");
		mGoodsList.add(good);
		good = new GoodsItem("开关厂", "开关", "BVR", "2", "120", "100");
		mGoodsList.add(good);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.title_bar_back:
			finish();
			
		case R.id.layout_receiver:
			Intent intent = new Intent(this, AddressManageActivity.class);
			startActivityForResult(intent, 1);
			break;
			
		
		}
	}
	
	private void InitListView()
	{
		ListView listview = (ListView)findViewById(R.id.listview_order_confirm);
		LinearLayout  hearder = (LinearLayout)LayoutInflater.from(this)
				.inflate(R.layout.listview_header_receiver, null);
		LinearLayout  foot = (LinearLayout)LayoutInflater.from(this)
				.inflate(R.layout.listview_foot_pay, null);
		
		listview.addHeaderView(hearder);
		listview.addFooterView(foot);
		
		MyListAdapter adapter = new MyListAdapter(this);
		listview.setAdapter(adapter);
		
		LinearLayout receiver = (LinearLayout)hearder.findViewById(R.id.layout_receiver);
		receiver.setOnClickListener(this);
		mReceiverName = (TextView)hearder.findViewById(R.id.textview_receiver_name);
		mReceiverPhone = (TextView)hearder.findViewById(R.id.textview_receiver_phone);
		mReceiverAddress = (TextView)hearder.findViewById(R.id.textview_receiver_address);
		
		mCouponCount = (TextView)foot.findViewById(R.id.textview_coupon);
		mCouponUse = (TextView)foot.findViewById(R.id.textview_use_coupon);
	}
	
	private class MyListAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		
		public MyListAdapter(Context context)
		{
			mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mGoodsList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ListItemView  listItemView = null;
			if (convertView == null) {   
	            listItemView = new ListItemView();    
	            convertView = mInflater.inflate(R.layout.listitem_orderitem, null);   
	            
	            listItemView.mItemLayout = (LinearLayout)convertView.findViewById(R.id.layout_order_item);   
	            listItemView.mItemLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						//ShowOrderFragment(0);
					}
				});
	            
	            listItemView.mItemBrand = (TextView)convertView.findViewById(R.id.textview_brand);  
	            listItemView.mItemStandard = (TextView)convertView.findViewById(R.id.textview_standard);  
	            listItemView.mItemType = (TextView)convertView.findViewById(R.id.textview_type);  
	            listItemView.mItemArea = (TextView)convertView.findViewById(R.id.textview_area);  
	            
	            listItemView.mItemMoney = (TextView)convertView.findViewById(R.id.textview_orderitem_money);   
	            listItemView.mItemCount = (TextView)convertView.findViewById(R.id.textview_orderitem_count);   
	            convertView.setTag(listItemView);   
	        }else {   
	            listItemView = (ListItemView)convertView.getTag();   
	        }  
			
			GoodsItem good = mGoodsList.get(position);
			listItemView.mItemBrand.setText(good.mBrand);
			listItemView.mItemStandard.setText(good.mStandard);
			listItemView.mItemType.setText(good.mType);
			listItemView.mItemArea.setText(good.mArea);
			
			listItemView.mItemMoney.setText("￥"+good.mMoney);
			listItemView.mItemCount.setText("x"+good.mCount);
			
			return convertView;
		}
	}
	
	private final class ListItemView{
        private LinearLayout mItemLayout;
        
        private TextView mItemBrand;
        private TextView mItemStandard;
        private TextView mItemType;
        private TextView mItemArea;
        
        private TextView mItemMoney;
        private TextView mItemCount;
	} 
	 
	//收货人类Receiver
	private class Receiver
	{
		private String mName;
		private String mPhone;
		private String mAddress;
		
		public Receiver(String name,String phone, String address)
		{
			mName = name;
			mPhone = phone;
			mAddress = address;
		}
	}
	
	/*
	//商品
	public class Goods
	{
		private String mBrand; //品牌
		private String mType; //分类
		private String mStandard; //规格
		private String mArea; //截面积
		
		private String mMoney;
		private String mCount;
		
		public Goods(String brand, String type, String standard, String area, String money, String count)
		{
			mBrand = brand;
			mType = type;
			mStandard = standard;
			mArea = area;
			
			mMoney = money;
			mCount = count;
		}
	}
	*/
	
	
}
