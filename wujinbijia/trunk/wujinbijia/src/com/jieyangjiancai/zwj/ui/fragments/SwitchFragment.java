package com.jieyangjiancai.zwj.ui.fragments;

import java.util.ArrayList;

import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.data.KindItem;
import com.jieyangjiancai.zwj.data.KindItemList;
import com.jieyangjiancai.zwj.data.SwitchItem;
import com.jieyangjiancai.zwj.data.SwitchPriceItem;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.KindsItem;
import com.jieyangjiancai.zwj.network.entity.SwitchKinds;
import com.jieyangjiancai.zwj.network.entity.SwitchPrice;
import com.jieyangjiancai.zwj.ui.fragments.CableFragment.EditChangedListener;

public class SwitchFragment extends Fragment implements OnClickListener {
	private View mMainView = null;
	private boolean mInit = false;

	private WheelView mWheelBrand;
	private WheelView mWheelCategory;
	private WheelView mWheelStandard;
	private WheelView mWheelModel;
	private TextView mTextPrice;
	private TextView mTextName;

	private ArrayList<SwitchItem> mListSwitchItem;

	private boolean bWheelBrandScrolling = false;
	private boolean bWheelCategoryScrolling = false;
	private boolean bWheelStandardScrolling = false;
	private boolean bWheelModelScrolling = false;

	private RelativeLayout mLayoutProgress;

	private KindsItem mKindsItem;
	
	private TextView mTextBrand;
	private TextView mTextCategory;
	private TextView mTextStandard;
	private TextView mTextModel;
	
	private EditText mEditDiscount1;
	private EditText mEditDiscount2;
	private EditText mEditDiscount3;
	private EditText mEditDiscount4;
	
	private double mCurrentPrice;
	
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
			mMainView = inflater.inflate(R.layout.fragment_switch, container, false);
			mInit = false;
		}

		return mMainView;
	}

	private void init() {
		mLayoutProgress = (RelativeLayout) mMainView.findViewById(R.id.layout_progress_bar);

		mWheelBrand = (WheelView) mMainView.findViewById(R.id.whell_brand);
		mWheelCategory = (WheelView) mMainView.findViewById(R.id.whell_category);
		mWheelStandard = (WheelView) mMainView.findViewById(R.id.whell_standard);
		mWheelModel = (WheelView) mMainView.findViewById(R.id.whell_model);
		mTextPrice = (TextView) mMainView.findViewById(R.id.text_brand_price);
		mTextName = (TextView) mMainView.findViewById(R.id.text_switch_name);


		mTextBrand = (TextView)mMainView.findViewById(R.id.text_brand);
		mTextCategory = (TextView)mMainView.findViewById(R.id.text_category);
		mTextStandard = (TextView)mMainView.findViewById(R.id.text_standard);
		mTextModel = (TextView)mMainView.findViewById(R.id.text_model);
		
		mEditDiscount1 = (EditText)mMainView.findViewById(R.id.edit_discount1);
		mEditDiscount2 = (EditText)mMainView.findViewById(R.id.edit_discount2);
		mEditDiscount3 = (EditText)mMainView.findViewById(R.id.edit_discount3);
		mEditDiscount4 = (EditText)mMainView.findViewById(R.id.edit_discount4);
		mEditDiscount1.addTextChangedListener(new EditChangedListener());
		mEditDiscount2.addTextChangedListener(new EditChangedListener());
		mEditDiscount3.addTextChangedListener(new EditChangedListener());
		mEditDiscount4.addTextChangedListener(new EditChangedListener());
		
		GetKindsItem();
		
		GetSwitchData();
	}

	private void GetKindsItem()
	{
		BackendDataApi bdApi = ((WJApplication)getActivity().getApplicationContext()).getHttpRequest();
		bdApi.FetchKinds(reqKindsItemSuccessListener(), reqKindsItemErrorListener());
	}
	private Response.Listener<JSONObject> reqKindsItemSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					mKindsItem = KindsItem.parse(response);
					UpdateKindsItem();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
	}
	private Response.ErrorListener reqKindsItemErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//String t = error.getMessage();
				//ToastMessage.show(getActivity(), t);
			}
		};
	}
	
	private void UpdateKindsItem()
	{
		ArrayList<KindItemList> list = mKindsItem.GetKindItemList();
		for (int i=0; i<list.size(); i++)
		{
			KindItemList item = list.get(i);
			if (item.code.equals("electrical")) // 开关电器
			{
				ArrayList<KindItem> kindItems = item.kindItemList;
				for(int j=0; j<kindItems.size(); j++)
				{
					KindItem kindItem = kindItems.get(j);
					if (kindItem.id.equals("5"))
						mTextBrand.setText(kindItem.itemName);
					if (kindItem.id.equals("6"))
						mTextCategory.setText(kindItem.itemName);
					if (kindItem.id.equals("7"))
						mTextStandard.setText(kindItem.itemName);
					if (kindItem.id.equals("8"))
						mTextModel.setText(kindItem.itemName);
				}
			}
		}
	}
	
	private void GetSwitchData() {
		BackendDataApi bdApi = ((WJApplication) getActivity().getApplicationContext()).getHttpRequest();
		bdApi.fetchSwitch(reqSuccessListener(), reqErrorListener());
	}

	private Response.Listener<JSONArray> reqSuccessListener() {
		return new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				try {
					SwitchKinds switchKinds = SwitchKinds.parse(response);
					mListSwitchItem = switchKinds.GetListSwitch();

					mTextName.setText(mListSwitchItem.get(0).name);
					
					int width = mWheelBrand.getWidth();
					mWheelBrand.SetWidth(width);
					mWheelCategory.SetWidth(width);
					mWheelStandard.SetWidth(width);
					mWheelModel.SetWidth(width);
					
					updateWheelBrand();
					updateWheelCategory();
					updateWheelStandard();
					updateWheelModel();
					updatePrice();
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
				// ToastMessage.show(getActivity(), t);
			}
		};
	}

	private void updateWheelBrand() {
		try {
			int length = mListSwitchItem.size();
			String[] names = new String[length];
			for (int i = 0; i < length; i++) {
				SwitchItem item = mListSwitchItem.get(i);
				names[i] = item.name;
				Log.d("wujin", "updateWheelBrand:names[" + i + "]=" + names[i]);
			}

			ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this.getActivity(), names);
			adapter.setTextSize(15);
			mWheelBrand.setViewAdapter(adapter);
			mWheelBrand.addScrollingListener(new OnWheelScrollListener() {
				public void onScrollingStarted(WheelView wheel) {
					bWheelBrandScrolling = true;
				}

				public void onScrollingFinished(WheelView wheel) {
					if (bWheelBrandScrolling == true) {
						mTextName.setText(mListSwitchItem.get(mWheelBrand.getCurrentItem()).name);

						updateWheelCategory();
						updateWheelStandard();
						updateWheelModel();
						updatePrice();
						bWheelBrandScrolling = false;
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateWheelCategory() {
		try {
			int brandId = mWheelBrand.getCurrentItem();
			SwitchItem brandItem = mListSwitchItem.get(brandId); // 品牌
			int length = brandItem.items.size();
			String[] names = new String[length];
			for (int i = 0; i < length; i++) {
				SwitchItem item = brandItem.items.get(i);
				names[i] = item.name;
				Log.d("wujin", "updateWheelCategory: names[" + i + "]=" + names[i]);
			}

			ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this.getActivity(), names);
			adapter.setTextSize(15);
			mWheelCategory.setViewAdapter(adapter);
			mWheelCategory.setCurrentItem(0);
			mWheelCategory.addScrollingListener(new OnWheelScrollListener() {
				public void onScrollingStarted(WheelView wheel) {
					bWheelCategoryScrolling = true;
				}

				public void onScrollingFinished(WheelView wheel) {
					if (bWheelCategoryScrolling == true) {
						updateWheelStandard();
						updateWheelModel();
						updatePrice();
						bWheelCategoryScrolling = false;
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateWheelStandard() {
		try {
			SwitchItem branditem = mListSwitchItem.get(mWheelBrand.getCurrentItem()); // 品牌滚轮
			SwitchItem categoryItem = branditem.items.get(mWheelCategory.getCurrentItem()); // 分类滚轮
			int length = categoryItem.items.size();
			String[] names = new String[length];
			for (int i = 0; i < length; i++) {
				SwitchItem item = categoryItem.items.get(i);
				names[i] = item.name;
				Log.d("wujin", "updateWheelStandard: names[" + i + "]=" + names[i]);
			}

			ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this.getActivity(), names);
			adapter.setTextSize(15);
			mWheelStandard.setViewAdapter(adapter);
			mWheelStandard.setCurrentItem(0);
			mWheelStandard.addScrollingListener(new OnWheelScrollListener() {
				public void onScrollingStarted(WheelView wheel) {
					bWheelCategoryScrolling = true;
				}

				public void onScrollingFinished(WheelView wheel) {
					if (bWheelCategoryScrolling == true) {
						updateWheelModel();
						updatePrice();
						bWheelCategoryScrolling = false;
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateWheelModel() {
		try {
			SwitchItem branditem = mListSwitchItem.get(mWheelBrand.getCurrentItem()); // 品牌滚轮
			SwitchItem categoryItem = branditem.items.get(mWheelCategory.getCurrentItem()); // 分类滚轮
			SwitchItem standardItem = categoryItem.items.get(mWheelStandard.getCurrentItem()); // 规格滚轮
			int length = standardItem.items.size();
			String[] names = new String[length];
			for (int i = 0; i < length; i++) {
				SwitchItem item = standardItem.items.get(i);
				names[i] = item.name;
				Log.d("wujin", "updateWheelModel: names[" + i + "]=" + names[i]);
			}

			ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this.getActivity(), names);
			adapter.setTextSize(15);
			mWheelModel.setViewAdapter(adapter);
			mWheelModel.setCurrentItem(0);
			mWheelModel.addScrollingListener(new OnWheelScrollListener() {
				public void onScrollingStarted(WheelView wheel) {
					bWheelModelScrolling = true;
				}

				public void onScrollingFinished(WheelView wheel) {
					if (bWheelModelScrolling == true) {
						Log.d("wujin", "updateWheelArea:onScrollingFinished()");
						updatePrice();
						bWheelModelScrolling = false;
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updatePrice() {
		try {
			mTextPrice.setText("未有价格");
			mLayoutProgress.setVisibility(View.VISIBLE);

			SwitchItem branditem = mListSwitchItem.get(mWheelBrand.getCurrentItem()); // 品牌滚轮
			SwitchItem categoryItem = branditem.items.get(mWheelCategory.getCurrentItem()); // 分类滚轮
			SwitchItem standardItem = categoryItem.items.get(mWheelStandard.getCurrentItem()); // 规格滚轮
			SwitchItem modelItem = standardItem.items.get(mWheelModel.getCurrentItem()); // 型号滚轮

			String productId = String.valueOf(modelItem.productId);
			String modeltypeId = String.valueOf(modelItem.id);
			BackendDataApi bdApi = ((WJApplication) getActivity().getApplicationContext()).getHttpRequest();
			bdApi.switchPrice(productId, modeltypeId, reqPriceSuccessListener(), reqPriceErrorListener());
		} catch (Exception e) {
			mLayoutProgress.setVisibility(View.INVISIBLE);
		}
	}

	private Response.Listener<JSONArray> reqPriceSuccessListener() {
		return new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				try {
					mTextPrice.setText("未有价格");
					SwitchPrice sPrice = SwitchPrice.parse(response);
					ArrayList<SwitchPriceItem> listSwitchPrice = sPrice.GetPrices();
					for (int i = 0; i < listSwitchPrice.size(); i++) {
						SwitchPriceItem item = listSwitchPrice.get(i);
						if (item.type.equals("selected")) {
							mTextPrice.setText(String.valueOf(item.realPrice));
							mCurrentPrice = item.realPrice;
							CalcDiscount();
							break;
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mLayoutProgress.setVisibility(View.INVISIBLE);
			}
		};
	}

	private Response.ErrorListener reqPriceErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mLayoutProgress.setVisibility(View.INVISIBLE);
				mTextPrice.setText("未有价格");
				// String t = error.getMessage();
				// ToastMessage.show(getActivity(), t);
			}
		};
	}
	
	private void CalcDiscount()
	{
		//String price = mTextBrandPrice.getText().toString();
		try{
			float p = (float) mCurrentPrice; //Float.valueOf(price);
			float discount1 = 1;
			float discount2 = 1;
			float discount3 = 1;
			float discount4 = 1;
			String strDiscount = mEditDiscount1.getText().toString();
			if (strDiscount != null && !strDiscount.equals(""))
				discount1 = Float.valueOf(strDiscount);
			strDiscount = mEditDiscount2.getText().toString();
			if (strDiscount != null && !strDiscount.equals(""))
				discount2 = Float.valueOf(strDiscount);
			strDiscount = mEditDiscount3.getText().toString();
			if (strDiscount != null && !strDiscount.equals(""))
				discount3 = Float.valueOf(strDiscount);
			strDiscount = mEditDiscount4.getText().toString();
			if (strDiscount != null && !strDiscount.equals(""))
				discount4 = Float.valueOf(strDiscount);
			
			p = p*discount1*discount2*discount3*discount4;
			mTextPrice.setText(String.valueOf(p));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	class EditChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        	CalcDiscount();
        }
    };

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

}
