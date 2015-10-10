package com.jieyangjiancai.zwj.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.ui.fragments.SettingFragment;

public class SettingActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		SettingFragment setting = new SettingFragment();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_contain, setting);
		fragmentTransaction.commit();
	}


}
