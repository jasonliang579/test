package com.jieyangjiancai.zwj.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jieyangjiancai.zwj.R;

public class SettingFragment extends Fragment implements OnClickListener {

	private View mMainView = null;
	private boolean mInit = false;
	private Context mContext;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();
		if (mInit == false)
			init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mMainView = inflater.inflate(R.layout.fragment_setting_main, container, false);
		mInit = false;

		return mMainView;
	}

	private void init() {
		mMainView.findViewById(R.id.title_bar_back).setOnClickListener(this);
		TextView title = (TextView) mMainView.findViewById(R.id.title_bar_text);
		title.setText("设置");

		mMainView.findViewById(R.id.lay_myInfo).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		FragmentManager fragmentManager;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_bar_back:
			fragmentManager = getActivity().getSupportFragmentManager();
			SettingFragment setting = new SettingFragment();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.fragment_contain, setting);
			fragmentTransaction.commit();
			// FragmentsManager.ChangeFragment(new SettingFragment());
			break;
		case R.id.lay_myInfo:
			fragmentManager = getActivity().getSupportFragmentManager();
			UserInfoFragment userInfoFragment = new UserInfoFragment();
			fragmentManager.beginTransaction().replace(R.id.fragment_contain, userInfoFragment).commit();
			break;
		default:
			break;
		}
	}

}
