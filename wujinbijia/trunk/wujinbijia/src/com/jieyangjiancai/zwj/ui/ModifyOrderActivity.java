package com.jieyangjiancai.zwj.ui;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.data.GoodsItem;

public class ModifyOrderActivity extends BaseActivity implements OnClickListener {
	
	private ArrayList<GoodsItem> mGoodsList = new ArrayList<GoodsItem>();
	
	private TextView mTotalMoney;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_modifyorder);
        
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
		title.setText("修改订单");
		mTotalMoney  = (TextView)findViewById(R.id.textview_total_money);
		
		InitListView();
	}
	
	
	private void InitListView()
	{
		ListView listview = (ListView)findViewById(R.id.listview_modifyorder);
		
		MyListAdapter adapter = new MyListAdapter(this);
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
	            listItemView.mItemEditCount = (EditText)convertView.findViewById(R.id.edit_orderitem_count);  
	            listItemView.mItemEditCount.setVisibility(View.VISIBLE);
	            listItemView.mItemCount.setVisibility(View.INVISIBLE);
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
			listItemView.mItemEditCount.setText(good.mCount);
			
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
        private EditText mItemEditCount;
	} 
	
	private void InitData()
	{
		GetData();
		
		mTotalMoney.setText("￥8888");
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
			break;

		}
	}
	
	
}
