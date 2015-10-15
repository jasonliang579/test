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
import android.widget.EditText;
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
import com.jieyangjiancai.zwj.utils.AppUtil;
import com.jieyangjiancai.zwj.views.PullToRefreshView;
import com.jieyangjiancai.zwj.views.PullToRefreshView.OnFooterRefreshListener;
import com.jieyangjiancai.zwj.views.PullToRefreshView.OnHeaderRefreshListener;

public class AddMessageActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout mLayoutProgress;
	private EditText mEditMessage;
	
	private String mOrderMessageId;
	private String mReplyMessageId = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_message);
        
		Init();
		InitData();
	}

	
	private void Init()
	{
		findViewById(R.id.title_bar_back).setOnClickListener(this);
		findViewById(R.id.btn_add_message).setOnClickListener(this);
		
		TextView title = (TextView) findViewById(R.id.title_bar_text);
		title.setText("新留言");
		
		mEditMessage = (EditText)findViewById(R.id.edit_mark);
		
		mLayoutProgress = (RelativeLayout)findViewById(R.id.layout_progress_bar);
		mLayoutProgress.setVisibility(View.INVISIBLE);
	}
	
	private void InitData()
	{
		Intent intent = getIntent();
		mOrderMessageId = intent.getStringExtra("order_message_id");

		String type = intent.getStringExtra("type");
		if (type != null && type.equals("reply"))
		{
			mReplyMessageId = intent.getStringExtra("reply_message_id");
			TextView title = (TextView) findViewById(R.id.title_bar_text);
			title.setText("回复留言");
		}
	}

	private void AddMessage()
	{
		mLayoutProgress.setVisibility(View.VISIBLE);
		
		String userId = ConfigUtil.mUserId;
		String token = ConfigUtil.mToken;
		String orderMessageId = mOrderMessageId;
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.AddMessage(userId, token, orderMessageId, 
				mEditMessage.getText().toString(), mReplyMessageId,
				reqSuccessListener(), reqErrorListener());
	}
	
	private Response.Listener<JSONObject> reqSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					setResult(Activity.RESULT_OK);
					finish();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mLayoutProgress.setVisibility(View.INVISIBLE);
			}
		};
	}
	private Response.ErrorListener reqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mLayoutProgress.setVisibility(View.INVISIBLE);
				ToastMessage.show(AddMessageActivity.this, "发布留言失败");
			}
		};
	}
	 
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.title_bar_back:
			finish();
			break;
			
		case R.id.btn_add_message:
			AddMessage();
			AppUtil.sendUmengOnEvent(this, "10004");
			break;
		}
	}
	
}
