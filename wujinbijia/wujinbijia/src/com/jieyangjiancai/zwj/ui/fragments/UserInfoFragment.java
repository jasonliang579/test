package com.jieyangjiancai.zwj.ui.fragments;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.SwitchKinds;

public class UserInfoFragment extends Fragment implements OnClickListener {

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
		mMainView = inflater.inflate(R.layout.fragment_user_info, container, false);
		mInit = false;
		BackendDataApi bdApi = ((WJApplication)getActivity().getApplicationContext()).getHttpRequest();
		//bdApi.userInfo(ConfigUtil.mUserId, ConfigUtil.mToken, reqSuccessListener(), reqErrorListener());
		
		return mMainView;
	}

	private void init() {
		mMainView.findViewById(R.id.title_bar_back).setOnClickListener(this);
		TextView title = (TextView) mMainView.findViewById(R.id.title_bar_text);
		title.setText("个人资料");

	}

	@Override
	public void onClick(View v) {
		FragmentManager fragmentManager;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_bar_back:
			fragmentManager = getActivity().getSupportFragmentManager();
			SettingFragment setting = new SettingFragment();
			fragmentManager.beginTransaction().replace(R.id.fragment_contain, setting).commit();
			break;
		case R.id.lay_myInfo:
		
			break;
		default:
			break;
		}
	}
	
	
	private Response.Listener<JSONArray> reqSuccessListener() {
		return new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				try {
					SwitchKinds switchKinds = SwitchKinds.parse(response);
					
				} catch (JSONException e) {
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
				String t = error.getMessage();
				//ToastMessage.show(getActivity(), t);
			}
		};
	}

}
