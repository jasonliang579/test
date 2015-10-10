package com.jieyangjiancai.zwj.ui;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.common.ToastMessage;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.data.MessageItem;
import com.jieyangjiancai.zwj.data.OrderItem;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.GetOrder;
import com.jieyangjiancai.zwj.network.entity.MessageList;
import com.jieyangjiancai.zwj.ui.GetOrderActivity.ViewHolder;
import com.jieyangjiancai.zwj.views.PullToRefreshView;
import com.jieyangjiancai.zwj.views.PullToRefreshView.OnFooterRefreshListener;
import com.jieyangjiancai.zwj.views.PullToRefreshView.OnHeaderRefreshListener;

public class MessageListActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout mLayoutProgress;
	private String ORDER_COUNT_PER_PAGE = ConfigUtil.mPageSize;
	private ListView mListView;
	private MyAdapter mMyAdapter;
	private PullToRefreshView mListViewPull; 
	private String mFinalId = "";
	private ArrayList<MessageItem> mListMessage = new ArrayList<MessageItem>();
	
	private String mOrderMessageId;
	private String mType="";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_message_list);
        
		Init();
		InitData();
	}

	
	private void Init()
	{
		findViewById(R.id.title_bar_back).setOnClickListener(this);
		findViewById(R.id.layout_write_message).setOnClickListener(this);
		
		TextView title = (TextView) findViewById(R.id.title_bar_text);
		title.setText("留言");
		
		mLayoutProgress = (RelativeLayout)findViewById(R.id.layout_progress_bar);
		mLayoutProgress.setVisibility(View.INVISIBLE);
		
		InitPullListView();
	}
	private void InitData()
	{
		Intent intent = getIntent();
		mOrderMessageId = intent.getStringExtra("order_message_id");
		mType = intent.getStringExtra("type");
		if (mType != null && mType.equals("reply"))
			findViewById(R.id.layout_write_message).setVisibility(View.GONE);
		else
			findViewById(R.id.layout_write_message).setVisibility(View.VISIBLE);
		GetMessageList();
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
				GetMessageList();
				
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
				
				GetMessageList();
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
	
	private void GetMessageList()
	{
		mLayoutProgress.setVisibility(View.VISIBLE);
		
		String userId = ConfigUtil.mUserId;
		String token = ConfigUtil.mToken;
		String pageSize = ORDER_COUNT_PER_PAGE;
		String finalId = mFinalId;
		String orderMessageId = mOrderMessageId;
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.GetMessageList(userId, token, pageSize, orderMessageId, finalId, 
				reqSuccessListener(), reqErrorListener());
	}
	
	private Response.Listener<JSONObject> reqSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					if (mFinalId.equals(""))
						mListMessage.clear();
					
					MessageList messageList = MessageList.parse(response);
					ArrayList<MessageItem> lists = messageList.GetMessageList();
					if (lists == null || lists.size() <= 0)
						mListViewPull.StopFooterRefresh(true);
					else
					{
						for (int i=0; i<lists.size(); i++)
						{
							mListMessage.add(lists.get(i));
							mFinalId = lists.get(i).message_id;
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
	private Response.ErrorListener reqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mLayoutProgress.setVisibility(View.INVISIBLE);
				mListViewPull.onHeaderRefreshComplete();
				mListViewPull.onFooterRefreshComplete();
				ToastMessage.show(MessageListActivity.this, "获取留言失败");
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
				convertView = mInflater.inflate(R.layout.listitem_message, parent, false);
				vh.text_company 	= (TextView)convertView.findViewById(R.id.text_company);
				vh.text_name 	= (TextView)convertView.findViewById(R.id.text_name);
				vh.text_time 	= (TextView)convertView.findViewById(R.id.text_time);
				vh.text_content = (TextView)convertView.findViewById(R.id.text_content);
				
				vh.layout_reply_message = (LinearLayout)convertView.findViewById(R.id.layout_reply_message);
				vh.text_reply_company 	= (TextView)convertView.findViewById(R.id.text_reply_company);
				vh.text_reply_name 	= (TextView)convertView.findViewById(R.id.text_reply_name);
				vh.text_reply_time 	= (TextView)convertView.findViewById(R.id.text_reply_time);
				vh.text_reply_content = (TextView)convertView.findViewById(R.id.text_reply_content);
				vh.layout_reply = (LinearLayout)convertView.findViewById(R.id.layout_reply);
				
				convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
			
			MessageItem item = mListMessage.get(position);
			vh.text_company.setText(item.user_company);
			vh.text_name.setText(item.user_name);
			vh.text_time.setText(ConfigUtil.getFormatedDateTime(Long.valueOf(item.create_time)));
			vh.text_content.setText(item.message_content);

			MessageItem replyItem = item.reply_message;
			if (replyItem != null)
			{
				vh.layout_reply_message.setVisibility(View.VISIBLE);
				vh.text_reply_company.setText(replyItem.user_company);
				vh.text_reply_name.setText(replyItem.user_name);
				if (replyItem.create_time != null && !replyItem.create_time.equals(""))
					vh.text_reply_time.setText(ConfigUtil.getFormatedDateTime(Long.valueOf(replyItem.create_time)));
				vh.text_reply_content.setText(replyItem.message_content);
				
				vh.layout_reply.setVisibility(View.GONE);
				
			}
			else
			{
				vh.layout_reply_message.setVisibility(View.GONE);
				vh.layout_reply.setVisibility(View.VISIBLE);
				vh.layout_reply.setTag(position);
				vh.layout_reply.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int position = (Integer)v.getTag();
						String reply_message_id = mListMessage.get(position).message_id;
						Intent intent = new Intent(MessageListActivity.this, AddMessageActivity.class);
						intent.putExtra("order_message_id", mOrderMessageId);
						intent.putExtra("reply_message_id", reply_message_id);
						intent.putExtra("type", mType);
						startActivityForResult(intent,1);
					}
				});
			}

			return convertView;
		}
	}
	class ViewHolder{
    	TextView text_company;
    	TextView text_name;
    	TextView text_time;
    	TextView text_content;
    	
    	LinearLayout layout_reply_message;
    	TextView text_reply_company;
    	TextView text_reply_name;
    	TextView text_reply_time;
    	TextView text_reply_content;    	
    	LinearLayout layout_reply;
    } 
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.title_bar_back:
			finish();
			break;
			
		case R.id.layout_write_message:
			Intent intent = new Intent(this, AddMessageActivity.class);
			intent.putExtra("order_message_id", mOrderMessageId);
			//intent.putExtra("type", mType);
			this.startActivityForResult(intent,1);
			break;
		}
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == Activity.RESULT_OK) {

			switch (requestCode) {
			case 1:
				//重新获取数据
				mFinalId = "";
				GetMessageList();
				break;
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
