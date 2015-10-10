package com.jieyangjiancai.zwj.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.jieyangjiancai.zwj.R;

public class ArriveToPayActivity extends FragmentActivity implements OnClickListener  {
	
	 
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_arrivetopay);
        
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
		findViewById(R.id.title_bar_back).setVisibility(View.GONE);
		
		TextView title = (TextView)findViewById(R.id.title_bar_text);
		title.setText("货到付款");
		
//		findViewById(R.id.image_home).setOnClickListener(this);
//		findViewById(R.id.image_phoneorder).setOnClickListener(this);
//		findViewById(R.id.image_photoorder).setOnClickListener(this);
//		findViewById(R.id.image_cart).setOnClickListener(this);
	}
	private void InitData()
	{
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
//		case R.id.image_home:
//			FragmentsManager.ChangeFragment(new HomeFragment());
//			break;
//			
//		case R.id.image_phoneorder:
//			FragmentsManager.ChangeFragment(new PhoneOrderFragment());
//			break;
//			
//		case R.id.image_photoorder:
//			break;
//			
//		case R.id.image_cart:
//			FragmentsManager.ChangeFragment(new ShoppingCartFragment());
//			break;
		}
	}
	
}
