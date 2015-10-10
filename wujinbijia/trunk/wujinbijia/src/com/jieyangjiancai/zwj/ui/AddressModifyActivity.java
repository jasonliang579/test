package com.jieyangjiancai.zwj.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.jieyangjiancai.zwj.R;

public class AddressModifyActivity extends Activity implements OnClickListener  {
	
	private TextView mAddressName;
	private TextView mAddressPhone;
	private TextView mAddressCompany;
	private TextView mAddress;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_addressmodify);
        
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
		title.setText("编辑收货地址");
		TextView textMore = (TextView)findViewById(R.id.title_bar_more_text);
		textMore.setText("删除");
		textMore.setOnClickListener(this);

		mAddressName = (TextView)findViewById(R.id.textview_address_name);
		mAddressPhone = (TextView)findViewById(R.id.textview_address_phone);
		mAddressCompany = (TextView)findViewById(R.id.textview_address_company);
		mAddress = (TextView)findViewById(R.id.textview_address);
		
	}
	private void InitData()
	{
		mAddressName.setText("张三");
		mAddressPhone.setText("144666444467");
		mAddressCompany.setText("的卡片我国公司");
		mAddress.setText("广州了坑爹道德观和理科高考历史课路");
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.title_bar_back:
			finish();
			break;
			
		case R.id.title_bar_more_text: 
			break;

		}
	}
	

}
