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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.common.ToastMessage;
import com.jieyangjiancai.zwj.data.AreaItem;
import com.jieyangjiancai.zwj.data.BrandItem;
import com.jieyangjiancai.zwj.data.BrandPriceItem;
import com.jieyangjiancai.zwj.data.CmcRelItem;
import com.jieyangjiancai.zwj.data.KindItem;
import com.jieyangjiancai.zwj.data.KindItemList;
import com.jieyangjiancai.zwj.data.StandardItem;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.AllKinds;
import com.jieyangjiancai.zwj.network.entity.BrandPrice;
import com.jieyangjiancai.zwj.network.entity.KindsItem;

public class CableFragment extends Fragment implements OnClickListener {
	private View mMainView = null;
	private boolean mInit = false;
	
	private WheelView mWheelBrand;
	private WheelView mWheelCategory;
	private WheelView mWheelStandard;
	private WheelView mWheelArea;
	private TextView mTextBrandPrice;
	private TextView mTextBrandName;
	
	private ArrayList<BrandItem> mListBrands = new ArrayList<BrandItem>();
	private ArrayList<CmcRelItem> mListCmcRels = new ArrayList<CmcRelItem>();
	
	private ArrayList<AreaItem> mCurListArea;
	
	private boolean bWheelBrandScrolling = false;
	private boolean bWheelCategoryScrolling = false;
	private boolean bWheelStandardScrolling = false;
	private boolean bWheelAreaScrolling = false;
	
	private RelativeLayout mLayoutProgress;
	
	private KindsItem mKindsItem;
	
	private TextView mTextBrand;
	private TextView mTextCategory;
	private TextView mTextStandard;
	private TextView mTextArea;
	
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
        	mMainView = inflater.inflate(R.layout.fragment_cable, container, false);
        	mInit = false;
        }
		
		return mMainView;
	}
	
	private void init()
	{
		mLayoutProgress = (RelativeLayout)mMainView.findViewById(R.id.layout_progress_bar);
		
		mWheelBrand = (WheelView) mMainView.findViewById(R.id.whell_brand);
		mWheelCategory= (WheelView) mMainView.findViewById(R.id.whell_category);
		mWheelStandard= (WheelView) mMainView.findViewById(R.id.whell_standard);
		mWheelArea= (WheelView) mMainView.findViewById(R.id.whell_area);
		mTextBrandPrice = (TextView)mMainView.findViewById(R.id.text_brand_price);
		mTextBrandName = (TextView)mMainView.findViewById(R.id.text_brand_name);
	 
		mTextBrand = (TextView)mMainView.findViewById(R.id.text_brand);
		mTextCategory = (TextView)mMainView.findViewById(R.id.text_category);
		mTextStandard = (TextView)mMainView.findViewById(R.id.text_standard);
		mTextArea = (TextView)mMainView.findViewById(R.id.text_area);
		
		mEditDiscount1 = (EditText)mMainView.findViewById(R.id.edit_discount1);
		mEditDiscount2 = (EditText)mMainView.findViewById(R.id.edit_discount2);
		mEditDiscount3 = (EditText)mMainView.findViewById(R.id.edit_discount3);
		mEditDiscount4 = (EditText)mMainView.findViewById(R.id.edit_discount4);
		mEditDiscount1.addTextChangedListener(new EditChangedListener());
		mEditDiscount2.addTextChangedListener(new EditChangedListener());
		mEditDiscount3.addTextChangedListener(new EditChangedListener());
		mEditDiscount4.addTextChangedListener(new EditChangedListener());
		
		GetKindsItem();
		
		GetAllKinds();
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
			if (item.code.equals("cable")) // 电线电缆
			{
				ArrayList<KindItem> kindItems = item.kindItemList;
				for(int j=0; j<kindItems.size(); j++)
				{
					KindItem kindItem = kindItems.get(j);
					if (kindItem.id.equals("1"))
						mTextBrand.setText(kindItem.itemName);
					if (kindItem.id.equals("2"))
						mTextCategory.setText(kindItem.itemName);
					if (kindItem.id.equals("3"))
						mTextStandard.setText(kindItem.itemName);
					if (kindItem.id.equals("4"))
						mTextArea.setText(kindItem.itemName);
				}
			}
		}
	}
	
   
	public void GetAllKinds() {
		BackendDataApi bdApi = ((WJApplication)getActivity().getApplicationContext()).getHttpRequest();
		bdApi.kindsAll(reqSuccessListener(), reqErrorListener());
	}
	private Response.Listener<JSONObject> reqSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					AllKinds.parse(response, mListBrands, mListCmcRels);
					String[] brands = new String[mListBrands.size()];
					for(int i=0; i<mListBrands.size(); i++)
					{
						BrandItem brand = mListBrands.get(i);
						brands[i] = brand.name;
					}
					mTextBrandName.setText(mListBrands.get(0).name);
					
					int width = mWheelBrand.getWidth();
					mWheelBrand.SetWidth(width);
					mWheelCategory.SetWidth(width);
					mWheelStandard.SetWidth(width);
					mWheelArea.SetWidth(width);
					
				    updateWheelBrand(mWheelBrand, brands);
				    updateWheelCategory();
				    updateWheelStandard();
		        	updateWheelArea();
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
				ToastMessage.show(getActivity(), t);
			}
		};
	}

	private void updateWheelBrand(WheelView wheel, String brands[])
	{
		try
		{
			ArrayWheelAdapter<String> adapter =
            new ArrayWheelAdapter<String>(this.getActivity(), brands);
	        adapter.setTextSize(15);
	        wheel.setViewAdapter(adapter);
	        //wheel.setCurrentItem(1);    
	        
	        wheel.addScrollingListener( new OnWheelScrollListener() {
	            public void onScrollingStarted(WheelView wheel) {
	                 bWheelBrandScrolling = true;
	            }
	            public void onScrollingFinished(WheelView wheel) {
	            	BrandItem brand = mListBrands.get(mWheelBrand.getCurrentItem());
	            	mTextBrandName.setText(brand.name);
 					if (bWheelBrandScrolling == true)
	                {
				       updateWheelCategory();
	            	   updateWheelStandard();
	            	   updateWheelArea();
	            	   updatePrice();
	            	   bWheelBrandScrolling = false;
	               }
	            }
	 		});
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
    
	private void updateWheelCategory() 
	{
		try
		{
			int length = mListCmcRels.size();
			String[] categorys = new String[length];
			for(int i=0; i<length; i++)
			{
				CmcRelItem cmcrel = mListCmcRels.get(i);
				categorys[i] = cmcrel.name;
				//Log.d("wujin", "categorys[" + i + "]=" + categorys[i]);
			}
			
	        ArrayWheelAdapter<String> adapter =
	            new ArrayWheelAdapter<String>(this.getActivity(), categorys);
	        adapter.setTextSize(15);
	        mWheelCategory.setViewAdapter(adapter);
	        mWheelCategory.setCurrentItem(0);
	       
	        mWheelCategory.addScrollingListener(new OnWheelScrollListener() {
	           public void onScrollingStarted(WheelView wheel) {
	        	   bWheelCategoryScrolling = true;
	           }
	           public void onScrollingFinished(WheelView wheel) {
	               if (bWheelCategoryScrolling == true)
	               {
	            	   updateWheelStandard();
	            	   updateWheelArea();
	            	   updatePrice();
	            	   bWheelCategoryScrolling = false;
	               }
	        	   
	           }
	       });
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }

	private void updateWheelStandard() 
	{
		try
		{
			int categoryId = mListCmcRels.get(mWheelCategory.getCurrentItem()).id;
			int length = mListCmcRels.size();
			for(int i=0; i<length; i++)
			{
				CmcRelItem cmcrel = mListCmcRels.get(i);
				if (categoryId == cmcrel.id)
				{
					ArrayList<StandardItem> listStandard = cmcrel.mListStandard;
					String[] standards = new String[listStandard.size()];
					for (int j=0; j<listStandard.size(); j++)
					{
						StandardItem standardItem = listStandard.get(j);
						standards[j] = standardItem.name;
						//Log.d("wujin", "standards[" + j + "]=" + standards[j]);
					}
					
					ArrayWheelAdapter<String> adapter =
				            new ArrayWheelAdapter<String>(this.getActivity(), standards);
			        adapter.setTextSize(15);
			        mWheelStandard.setViewAdapter(adapter);
			        mWheelStandard.setCurrentItem(0);
			        mWheelStandard.addScrollingListener(new OnWheelScrollListener() {
			           public void onScrollingStarted(WheelView wheel) {
			        	   bWheelStandardScrolling = true;
			           }
			           public void onScrollingFinished(WheelView wheel) {
			               if (bWheelStandardScrolling == true)
			               {
				        	   updateWheelArea();
				        	   updatePrice();
				        	   bWheelStandardScrolling = false;
			               }
			           }
			        });
					break;
				}
			}	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
	
	private void updateWheelArea() 
	{
		try
		{
			ArrayList<StandardItem> listStandard = mListCmcRels.get(mWheelCategory.getCurrentItem()).mListStandard;
			mCurListArea = listStandard.get(mWheelStandard.getCurrentItem()).mListArea;
			
			String[] areas = new String[mCurListArea.size()];
			for (int i=0; i<mCurListArea.size(); i++)
			{
				areas[i] = mCurListArea.get(i).name;
			}
			
			ArrayWheelAdapter<String> adapter =
		            new ArrayWheelAdapter<String>(this.getActivity(), areas);
	        adapter.setTextSize(15);
	        mWheelArea.setViewAdapter(adapter);
	        mWheelArea.setCurrentItem(0);
	        mWheelArea.addScrollingListener(new OnWheelScrollListener() {
	           public void onScrollingStarted(WheelView wheel) {
	        	   bWheelAreaScrolling = true;
	        	   Log.d("wujin", "updateWheelArea:onScrollingStarted()");
	           }
	           public void onScrollingFinished(WheelView wheel) {
	               if (bWheelAreaScrolling == true)
	               {
	            	   Log.d("wujin", "updateWheelArea:onScrollingFinished()");
		        	   updatePrice(); 
		        	   bWheelAreaScrolling = false;
	               }
	           }
		    });
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
	
	private int GetCurrentCmcRelId()
	{
		ArrayList<StandardItem> listStandard = mListCmcRels.get(mWheelCategory.getCurrentItem()).mListStandard;
		mCurListArea = listStandard.get(mWheelStandard.getCurrentItem()).mListArea;
		int cmcRelId = mCurListArea.get(mWheelArea.getCurrentItem()).cmcRelId;
		
		return cmcRelId;
	}
	private void updatePrice()
	{
		try
		{
			mLayoutProgress.setVisibility(View.VISIBLE);
			mTextBrandPrice.setText("未有价格");
			//int cmcRelId = mListBrands.get(mWheelBrand.getCurrentItem()).id;
			int cmcRelId = GetCurrentCmcRelId();
			BackendDataApi bdApi = ((WJApplication)getActivity().getApplicationContext()).getHttpRequest();
			bdApi.brandPrice(""+cmcRelId, reqPriceSuccessListener(), reqPriceErrorListener());
		}
		catch(Exception e)
		{
			mLayoutProgress.setVisibility(View.INVISIBLE);
		}
	}
	private Response.Listener<JSONArray> reqPriceSuccessListener() {
		return new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				try {
					mTextBrandPrice.setText("未有价格");
					BrandPrice bPrice = BrandPrice.parse(response);
					BrandItem item = mListBrands.get(mWheelBrand.getCurrentItem());
					
					for (int i=0; i<bPrice.mListBrandPrice.size(); i++)
					{
						BrandPriceItem brandPriceItem = bPrice.mListBrandPrice.get(i);
						if (brandPriceItem.brandCode.equals(item.name))
						{
							mCurrentPrice = brandPriceItem.price;
							mTextBrandPrice.setText(brandPriceItem.price+"");
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
				mTextBrandPrice.setText("未有价格");
				String t = error.getMessage();
				ToastMessage.show(getActivity(), t);
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
			mTextBrandPrice.setText(String.valueOf(p));
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
		switch(v.getId())
		{
//		case R.id.edit_discount1:
//			break;
		}
	}
}
