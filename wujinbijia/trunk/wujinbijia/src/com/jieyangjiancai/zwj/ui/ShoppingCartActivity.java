package com.jieyangjiancai.zwj.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.common.ToastMessage;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.data.GoodsItem;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.QueryCart;

public class ShoppingCartActivity extends BaseActivity implements OnClickListener {
//	private View mMainView = null;
//	private boolean mInit = false;
	
	private TextView mTextBeforeDiscount;
	private TextView mTextDiscount;
	
	private ArrayList<GoodsItem> mGoodsList = new ArrayList<GoodsItem>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shoppingcart);
        
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
		title.setText("购物车");
		TextView more = (TextView)findViewById(R.id.title_bar_more_text);
		more.setText("编辑");
		more.setOnClickListener(this);
		
		
		mTextBeforeDiscount = (TextView)findViewById(R.id.textview_cart_beforediscount);
		mTextDiscount = (TextView)findViewById(R.id.textview_cart_discount);
		
		InitListView();
	}
	
	private void InitListView()
	{
		ListView listview = (ListView)findViewById(R.id.listview_shoppingcart);
		
		MyListAdapter adapter = new MyListAdapter(this);
		listview.setAdapter(adapter);
	}
	
	private class MyListAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		
		public MyListAdapter(Context context)
		{
			mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mGoodsList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ListItemView  listItemView = null;
			if (convertView == null) {   
	            listItemView = new ListItemView();    
	            convertView = mInflater.inflate(R.layout.listitem_orderitem, null);   
	            
	            listItemView.mItemLayout = (LinearLayout)convertView.findViewById(R.id.layout_order_item);   
	            listItemView.mItemLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						//ShowOrderFragment(0);
					}
				});
	            
	            listItemView.mItemBrand = (TextView)convertView.findViewById(R.id.textview_brand);  
	            listItemView.mItemStandard = (TextView)convertView.findViewById(R.id.textview_standard);  
	            listItemView.mItemType = (TextView)convertView.findViewById(R.id.textview_type);  
	            listItemView.mItemArea = (TextView)convertView.findViewById(R.id.textview_area);  
	            
	            listItemView.mItemMoney = (TextView)convertView.findViewById(R.id.textview_orderitem_money);   
	            listItemView.mItemCount = (TextView)convertView.findViewById(R.id.textview_orderitem_count);   
	            listItemView.mItemSelect = (ImageView)convertView.findViewById(R.id.image_select);
	            listItemView.mItemDelete = (TextView)convertView.findViewById(R.id.textview_item_delete);
	            listItemView.mItemEdit = (TextView)convertView.findViewById(R.id.textview_edit);
	            
	            listItemView.mItemSelect.setVisibility(View.VISIBLE);
	            //listItemView.mItemDelete.setVisibility(View.VISIBLE);
	            listItemView.mItemEdit.setTag(listItemView.mItemDelete);
	            listItemView.mItemEdit.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						TextView text = (TextView) v;
						String str = text.getText().toString();
						View deleteView = (View) v.getTag();
						if (str.equals("编辑"))
						{
							deleteView.setVisibility(View.VISIBLE);
							text.setText("完成");
						}
						else
						{
							text.setText("编辑");
							deleteView.setVisibility(View.GONE);
						}
					}
	            });
	            
	            convertView.setTag(listItemView);   
	        }else {   
	            listItemView = (ListItemView)convertView.getTag();   
	        }  
			
			GoodsItem good = mGoodsList.get(position);
			listItemView.mItemBrand.setText(good.mBrand);
			listItemView.mItemStandard.setText(good.mStandard);
			listItemView.mItemType.setText(good.mType);
			listItemView.mItemArea.setText(good.mArea);
			
			listItemView.mItemMoney.setText("￥"+good.mMoney);
			listItemView.mItemCount.setText("x"+good.mCount);
			
			return convertView;
		}
	}
	
	private final class ListItemView{
        private LinearLayout mItemLayout;
        
        private TextView mItemBrand;
        private TextView mItemStandard;
        private TextView mItemType;
        private TextView mItemArea;
        private ImageView mItemSelect;
        private TextView mItemDelete;
        private TextView mItemEdit;
        
        private TextView mItemMoney;
        private TextView mItemCount;
        
	} 
	
	private void InitData()
	{
		QueryCart();
		//GetData();
	}
	private void GetData()
	{
		GoodsItem good = new GoodsItem("广州电缆厂", "绝缘电线", "BVR", "2.5", "999", "4");
		mGoodsList.add(good);
		good = new GoodsItem("上海电缆厂", "电线", "BVR", "4.5", "500", "6");
		mGoodsList.add(good);
		good = new GoodsItem("开关厂", "开关", "BVR", "2", "120", "100");
		mGoodsList.add(good);
		good = new GoodsItem("Mode厂", "开关", "BVR", "6", "88", "1234");
		mGoodsList.add(good);
		
		String discount = "￥1888";
		String str = discount+"（5折）";
		int index = discount.length();
		mTextDiscount.setText(str);
		SpannableStringBuilder builder = new SpannableStringBuilder(mTextDiscount.getText().toString());  
		  
		//ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色  
		ForegroundColorSpan redSpan = new ForegroundColorSpan(0xffff373d);  
		builder.setSpan(redSpan, 0, index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		
		mTextBeforeDiscount.setText("￥3888");
		mTextDiscount.setText(builder);
	}
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.title_bar_back:
			//FragmentsManager.BackFragment();
			finish();
			break;
			
		case R.id.title_bar_more_text:
			break;
		}
	}
	
	//查询购物车
	private void QueryCart()
	{
		String userId = "1";
		String token = ConfigUtil.mToken;
		
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.queryCart(userId, token,
				reqPutCartSuccessListener(), reqPutCartErrorListener());

	}
	private Response.Listener<JSONObject> reqPutCartSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				ToastMessage.show(ShoppingCartActivity.this, "添加到购物车成功");
				
					try {
						QueryCart queryCart = QueryCart.parse(response);
						
//						BrandItem item = mListBrands.get(mWheelBrand.getCurrentItem());
//						
//						for (int i=0; i<bPrice.mListBrandPrice.size(); i++)
//						{
//							BrandPriceItem brandPriceItem = bPrice.mListBrandPrice.get(i);
//							if (brandPriceItem.brandCode.equals(item.name))
//							{
//								mTextBrandPrice.setText(brandPriceItem.price+"");
//								break;
//							}
//						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		};
	}
	private Response.ErrorListener reqPutCartErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
//					mTextBrandPrice.setText("");
				String t = error.getMessage();
				ToastMessage.show(ShoppingCartActivity.this, t);
			}
		};
	}
	
	
}
