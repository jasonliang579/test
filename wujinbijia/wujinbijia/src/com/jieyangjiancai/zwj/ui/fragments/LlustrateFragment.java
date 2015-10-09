package com.jieyangjiancai.zwj.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jieyangjiancai.zwj.R;

public class LlustrateFragment extends Fragment {
	
	private View mMainView = null;
	private boolean mInit = false;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (mInit == false)
			init();
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
        	mMainView = inflater.inflate(R.layout.fragment_llustrate, container, false);
        	mInit = false;
        }
		
		return mMainView;
	}
	
	private void init()
	{
	}
	

}
