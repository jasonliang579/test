package com.jieyangjiancai.zwj.ui.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.ui.ModifyOrderActivity;
import com.jieyangjiancai.zwj.ui.OrderDetailActivity;

public class OrderListFragment extends Fragment {
	
	private View mMainView = null;
	private boolean mInit = false;
	
	private ArrayList<OrderItem> mOrderList = new ArrayList<OrderItem>();
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (mInit == false)
			Init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (null != mMainView) {
			mInit = true;
            ViewGroup parent = (ViewGroup) mMainView.getParent();
            if (null != parent) {
                parent.removeView(mMainView);
            }
        } else {
        	mMainView = inflater.inflate(R.layout.fragment_orderlist, container, false);
        	mInit = false;
        }
		return mMainView;
	}
	
	private void Init()
	{
		InitListView();
		
		InitData();
	}
	
	private void InitData()
	{
		OrderItem item = new OrderItem("1123456", 3000, "100", "2015-7-21");
		item.SetPayMoney(1800);
		mOrderList.add(item);
		item = new OrderItem("233335", 4321, "34", "2015-7-15");
		mOrderList.add(item);
		item = new OrderItem("555554444", 22420, "323", "2015-341");
		mOrderList.add(item);
	}
	
	private void InitListView()
	{
		ListView listview = (ListView)mMainView.findViewById(R.id.listview_order);
		MyListAdapter adapter = new MyListAdapter(this.getActivity());
		listview.setAdapter(adapter);
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
			return mOrderList.size();
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
	            convertView = mInflater.inflate(R.layout.listitem_orderlist, null);   
	            
	            listItemView.itemLayout = (LinearLayout)convertView.findViewById(R.id.layout_orderlist_item);   
	            listItemView.itemLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						StartConfimOrderActivity(0);
					}
				});
	            
	            listItemView.mTextOrderID = (TextView)convertView.findViewById(R.id.text_order_id);
	            listItemView.mTextOrderMoney = (TextView)convertView.findViewById(R.id.text_order_money);
	            listItemView.mTextOrderDate = (TextView)convertView.findViewById(R.id.text_order_date);
	            listItemView.mTextOrderCount = (TextView)convertView.findViewById(R.id.text_order_count);
	            listItemView.mTextOrderItemCount = (TextView)convertView.findViewById(R.id.text_order_item_count);
	            listItemView.mTextOrderTotalMoney = (TextView)convertView.findViewById(R.id.text_order_totalmoney);
	            listItemView.mTextOrderTayDetail = (TextView)convertView.findViewById(R.id.text_order_paydetail);
	            listItemView.mBtnOrderDelete = (Button)convertView.findViewById(R.id.btn_order_delete);
	            listItemView.mBtnOrderModify = (Button)convertView.findViewById(R.id.btn_order_modify);
	            listItemView.mBtnOrderConfirm = (Button)convertView.findViewById(R.id.btn_order_confirm);
	            
	            listItemView.mBtnOrderModify.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						StartModifyOrderActivity(0);
					}
				});
	            
	            convertView.setTag(listItemView);   
	        }else {   
	            listItemView = (ListItemView)convertView.getTag();   
	        }  
			
			OrderItem item = mOrderList.get(position);
			listItemView.mTextOrderID.setText(item.mOrderID);
			listItemView.mTextOrderMoney.setText("￥"+item.mOrderMoney);
			listItemView.mTextOrderDate.setText(item.mOrderDate);
			listItemView.mTextOrderCount.setText("x"+item.mOrderItemCount);
			listItemView.mTextOrderItemCount.setText("共"+item.mOrderItemCount+"件商品");
			listItemView.mTextOrderTotalMoney.setText("￥"+item.mOrderMoney);
			if (item.mOrderPayMoney > 0)
			{
				listItemView.mTextOrderTayDetail.setVisibility(View.VISIBLE);
				String str = "已付定金"+item.mOrderPayMoney+",还需要支付"+item.mOrderUnPayMoney;
				listItemView.mTextOrderTayDetail.setText(str);
			}
			else
			{
				listItemView.mTextOrderTayDetail.setVisibility(View.GONE);
			}
				
			
			return convertView;
		}
	}
	
	private class ListItemView{
        private LinearLayout itemLayout;
        private TextView mTextOrderID;
        private TextView mTextOrderMoney;
        private TextView mTextOrderDate;
        private TextView mTextOrderCount;
        private TextView mTextOrderItemCount;
        private TextView mTextOrderTotalMoney;
        private TextView mTextOrderTayDetail;
        private Button mBtnOrderDelete;
        private Button mBtnOrderModify;
        private Button mBtnOrderConfirm;
	} 
	
	private class OrderItem{
		String mOrderID;
		double mOrderMoney;
		String mOrderItemCount;
		String mOrderDate;
		double mOrderPayMoney;
		double mOrderUnPayMoney;
				
		OrderItem(String orderId, double money, String count, String date)
		{
			mOrderID = orderId;
			mOrderMoney = money;
			mOrderItemCount = count;
			mOrderDate = date;
			mOrderPayMoney = 0;
		}
		void SetPayMoney(double pay)
		{
			mOrderPayMoney = pay;
			mOrderUnPayMoney = mOrderMoney - pay;
		}
	}

	
	private void StartConfimOrderActivity(int orderindex)
	{
		Intent intent = new Intent(this.getActivity(), OrderDetailActivity.class);
		startActivity(intent);
	}
	private void StartModifyOrderActivity(int orderindex)
	{
		Intent intent = new Intent(this.getActivity(), ModifyOrderActivity.class);
		startActivity(intent);
	}
}
