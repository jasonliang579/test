package com.jieyangjiancai.zwj.ui.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jieyangjiancai.zwj.R;

public class HomeFragment extends Fragment implements OnClickListener {
	private View mMainView = null;
	private boolean mInit = false;

	private ViewPager mPager;
	private ArrayList<Fragment> mFragmentList;

	private TextView mTextCableSelect;
	private TextView mTextSwitchSelect;


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (mInit == false)
			init(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (null != mMainView) {
			mInit = true;
			ViewGroup parent = (ViewGroup) mMainView.getParent();
			if (null != parent) {
				parent.removeView(mMainView);
			}
		} else {
			mMainView = inflater.inflate(R.layout.fragment_home, container, false);
			mInit = false;
		}

		return mMainView;
	}

	private void init(Bundle savedInstanceState) {
		InitViewPaper();

		mMainView.findViewById(R.id.title_bar_back).setOnClickListener(this);
		//mMainView.findViewById(R.id.image_cart).setOnClickListener(this);
		mTextCableSelect = (TextView) mMainView.findViewById(R.id.text_cable_select);
		mTextSwitchSelect = (TextView) mMainView.findViewById(R.id.text_switch_select);

		mMainView.findViewById(R.id.text_cable).setOnClickListener(this);
		mMainView.findViewById(R.id.text_switch).setOnClickListener(this);

	}

	private void InitViewPaper() {
		// 创建ViewPager里面的页面
		mPager = (ViewPager) mMainView.findViewById(R.id.viewpager);
		mFragmentList = new ArrayList<Fragment>();

		CableFragment cableFragment = new CableFragment();
		SwitchFragment switchFragment = new SwitchFragment();

		mFragmentList.add(cableFragment);
		mFragmentList.add(switchFragment);

		// 给ViewPager设置适配器
		mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), mFragmentList));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mPager.setCurrentItem(0);// 设置当前显示页
	}

	private class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			switch (arg0) {
			case 0:
				mTextCableSelect.setVisibility(View.VISIBLE);
				mTextSwitchSelect.setVisibility(View.INVISIBLE);
				break;

			case 1:
				mTextSwitchSelect.setVisibility(View.VISIBLE);
				mTextCableSelect.setVisibility(View.INVISIBLE);
				break;
			}
		}
	}

	private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		ArrayList<Fragment> list;

		public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
			super(fm);
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_bar_back:
			this.getActivity().finish();

			break;


		case R.id.text_cable:
			mPager.setCurrentItem(0);
			break;

		case R.id.text_switch:
			mPager.setCurrentItem(1);
			break;
		}
	}


}
