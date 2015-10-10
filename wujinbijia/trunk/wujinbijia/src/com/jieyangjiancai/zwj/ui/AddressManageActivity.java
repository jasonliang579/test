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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.base.BaseActivity;

public class AddressManageActivity extends BaseActivity implements OnClickListener  {
	
	private ArrayList<Address> mAddressList = new ArrayList<Address>();
	
	private MyListAdapter mAdapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_addressmanage);
        
        Init();
    }
	
	private void Init()
	{
		InitUI();
		InitData();
	}
	
	private void InitUI()
	{
		//返回
		findViewById(R.id.title_bar_back).setOnClickListener(this);
		
		TextView title = (TextView)findViewById(R.id.title_bar_text);
		title.setText("收货地址管理");

		//findViewById(R.id.btn_add_address).setOnClickListener(this);
		
		InitListView();
		
	}
	private void InitData()
	{
		Address address = new Address(1,"小张","1230000000","现代汉语公司","广州番禺嗟来之食嗽路",false);
		mAddressList.add(address);
		address = new Address(2,"老王","18022222222","隔壁老王公司","广州天河体育中心正佳广场",true);
		mAddressList.add(address);
		address = new Address(3,"阿顺","13312336678","枯木公司","广州中山大道3路",false);
		mAddressList.add(address);
		address = new Address(4,"小小","1854443333","感觉顺利喝下困公司","广州到目前点传统具有全国路",false);
		mAddressList.add(address);
	}

	private void InitListView()
	{
		
		ListView listview = (ListView)findViewById(R.id.listview_address);
		mAdapter = new MyListAdapter(this);
		listview.setAdapter(mAdapter);
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
			return mAddressList.size();
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
	            convertView = mInflater.inflate(R.layout.listitem_address, null);   
	            
	            listItemView.itemName = (TextView)convertView.findViewById(R.id.textview_name);
	            listItemView.itemPhone = (TextView)convertView.findViewById(R.id.textview_phone);
	            listItemView.itemCompany = (TextView)convertView.findViewById(R.id.textview_company);
	            listItemView.itemAddress = (TextView)convertView.findViewById(R.id.textview_address);
	            listItemView.itemDefault = (ImageView)convertView.findViewById(R.id.image_default);
	            listItemView.itemLayoutDefault = (LinearLayout)convertView.findViewById(R.id.layout_default);
	            listItemView.itemLayoutDefault.setTag(position);
	            listItemView.itemLayoutDefault.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						int index = (Integer) v.getTag();
						SetDefault(index);
					}
	            });
	            listItemView.itemLayoutModify = (LinearLayout)convertView.findViewById(R.id.layout_address_modify);
	            listItemView.itemLayoutModify.setTag(position);
	            listItemView.itemLayoutModify.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						int index = (Integer) v.getTag();
						ModifyAddress(index);
					}
	            });
	            convertView.setTag(listItemView);   
	        }else {   
	            listItemView = (ListItemView)convertView.getTag();   
	        }  
			
			Address address = mAddressList.get(position);
			listItemView.itemName.setText(address.mName);
			listItemView.itemPhone.setText(address.mPhone);
			listItemView.itemCompany.setText(address.mCompany);
			listItemView.itemAddress.setText(address.mAddress);
			if (address.mIsDefault)
				listItemView.itemDefault.setImageResource(R.drawable.selected);
			else
				listItemView.itemDefault.setImageResource(R.drawable.unselect);
			listItemView.itemLayoutDefault.setTag(position);
			
			return convertView;
		}
	}
	
	private class ListItemView {
        private TextView itemName;
        private TextView itemPhone;
        private TextView itemCompany;
        private TextView itemAddress;
        private ImageView itemDefault;
        private LinearLayout itemLayoutDefault;
        private LinearLayout itemLayoutModify;
	} 
	
	private class Address {
		private int mId;
		private String mName;
		private String mPhone;
		private String mCompany;
		private String mAddress;
		private boolean mIsDefault;
		
		Address(int id, String name, String phone, String company, String address, boolean isDefault)
		{
			mId = id;
			mName = name;
			mPhone = phone;
			mCompany = company;
			mAddress = address;
			mIsDefault = isDefault;
		}
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.title_bar_back:
			finish();
			break;
			
		//case R.id.btn_add_address: //添加地址
		//	break;

		}
	}
	
	private void SetDefault(int index)
	{
		for (int i=0; i<mAddressList.size(); i++)
		{
			Address address = mAddressList.get(i);
			if (i == index)
				address.mIsDefault = true;
			else
				address.mIsDefault = false;
			mAddressList.set(i, address);
		}
		mAdapter.notifyDataSetChanged();
	}
	
	private void ModifyAddress(int index)
	{
		Intent intent = new Intent(this, AddressModifyActivity.class);
		this.startActivityForResult(intent, 1);
	}
}
