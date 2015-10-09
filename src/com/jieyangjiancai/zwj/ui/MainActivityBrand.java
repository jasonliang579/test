package com.jieyangjiancai.zwj.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.ui.fragments.CableFragment;
import com.jieyangjiancai.zwj.ui.fragments.SwitchFragment;

public class MainActivityBrand extends FragmentActivity implements OnClickListener  {
	private TextView mTextCableSelect;
	private TextView mTextSwitchSelect;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_brand);
        
        
        FragmentsManager.Init(this);

//      FragmentsManager.InitFragment(new HomeFragment());  
        FragmentsManager.InitFragment(new CableFragment());  
        
        Init();
    }
	
	private void Init()
	{
		InitUI();
		InitData();
	}
	
	private void InitUI()
	{
		findViewById(R.id.title_bar_back).setOnClickListener(this);
		findViewById(R.id.text_cable).setOnClickListener(this);
		findViewById(R.id.text_switch).setOnClickListener(this);
		
		mTextCableSelect = (TextView) findViewById(R.id.text_cable_select);
		mTextSwitchSelect = (TextView) findViewById(R.id.text_switch_select);
		
	}
	private void InitData()
	{
		
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_bar_back:
			finish();
			break;

		case R.id.text_cable:
			mTextCableSelect.setVisibility(View.VISIBLE);
			mTextSwitchSelect.setVisibility(View.INVISIBLE);
			FragmentsManager.ChangeFragment(new CableFragment());
			break;

		case R.id.text_switch:
			mTextSwitchSelect.setVisibility(View.VISIBLE);
			mTextCableSelect.setVisibility(View.INVISIBLE);
			FragmentsManager.ChangeFragment(new SwitchFragment());
			break;
		}
	}
	
}
