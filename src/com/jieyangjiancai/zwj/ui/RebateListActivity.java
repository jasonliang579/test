package com.jieyangjiancai.zwj.ui;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.network.entity.RebateItem;
import com.jieyangjiancai.zwj.network.entity.RebateList;

public class RebateListActivity extends BaseActivity implements OnClickListener {
	
	private ListView mListView;
	private MyAdapter mMyAdapter;
	private ArrayList<RebateItem> mListRebate;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rebatelist);
        
		Init();
	}

	
	private void Init()
	{
		findViewById(R.id.title_bar_back).setOnClickListener(this);
		
		TextView title = (TextView)findViewById(R.id.title_bar_text);
		title.setText("优惠券");
		
		mListView = (ListView)findViewById(R.id.listview);
		mMyAdapter = new MyAdapter(this);
		mListView.setAdapter(mMyAdapter);
		
		GetRebateList();
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
	

	private void GetRebateList() {
		((WJApplication)this.getApplicationContext()).getHttpRequest().fetchRebateList(
				ConfigUtil.mUserId, ConfigUtil.mToken, reqSuccessListener(), reqErrorListener());
	}

	private Response.Listener<JSONObject> reqSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				//ToastMessage.show(this, response.toString());
				try {
					RebateList rebateList = RebateList.parse(response);
					mListRebate = rebateList.GetListRebate();
					mMyAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

	private Response.ErrorListener reqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//String t = error.getMessage();
				//ToastMessage.show(LoginActivity.this, t);
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
			if (mListRebate == null || mListRebate.size() <= 0)
				return 0;
			return mListRebate.size();
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
				convertView = mInflater.inflate(R.layout.rebate_item, parent, false);
				//vh.textMyPrice = (TextView)convertView.findViewById(R.id.text_my_price);
				vh.textRebate 	= (TextView)convertView.findViewById(R.id.text_rebate);
				vh.textTime 	= (TextView)convertView.findViewById(R.id.text_time);
				vh.textOrderId 	= (TextView)convertView.findViewById(R.id.text_orderid);
				
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
			
			RebateItem item = mListRebate.get(position);
			
			vh.textRebate.setText(item.getAmount() + "元");
			vh.textTime.setText(ConfigUtil.getFormatedDateTime(Long.valueOf(item.getCreateTime())));
			vh.textOrderId.setText(item.getOrderId());
			
            return convertView;
		}
	}
	
	class ViewHolder{
    	TextView textRebate;
    	TextView textTime;
    	TextView textOrderId;
    } 
	
}
