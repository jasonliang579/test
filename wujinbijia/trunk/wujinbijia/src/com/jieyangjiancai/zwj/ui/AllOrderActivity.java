package com.jieyangjiancai.zwj.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.ui.fragments.OrderListFragment;

public class AllOrderActivity extends FragmentActivity implements OnClickListener {
	
	private ViewPager mPager;  
	private ArrayList<Fragment> mFragmentList; 
	
	TextView mTextAllOrder;
	TextView mTextUnpayOrder;
	TextView mTextUnsendOrder;
	TextView mTextUnreceiveOrder;
	
	TextView mTextAllOrderSelect;
	TextView mTextUnpayOrderSelect;
	TextView mTextUnsendOrderSelect;
	TextView mTextUnreceiveOrderSelect;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_allorder);
        
        Init();
	}
	
	private void Init()
	{
		//返回
		findViewById(R.id.title_bar_back).setOnClickListener(this);
		
		TextView title = (TextView)findViewById(R.id.title_bar_text);
		title.setText("全部订单");
//		ImageView more = (ImageView)findViewById(R.id.title_bar_more);
//		more.setImageResource(R.drawable.titlemessage);
		
		//订单的Tab
		mTextAllOrder = (TextView)findViewById(R.id.order_text_all);
		mTextUnpayOrder = (TextView)findViewById(R.id.order_text_unpay);
		mTextUnsendOrder = (TextView)findViewById(R.id.order_text_unsend);
		mTextUnreceiveOrder = (TextView)findViewById(R.id.order_text_unreceive);
		
		mTextAllOrderSelect = (TextView)findViewById(R.id.order_text_all_select);
		mTextUnpayOrderSelect = (TextView)findViewById(R.id.order_text_unpay_select);
		mTextUnsendOrderSelect = (TextView)findViewById(R.id.order_text_unsend_select);
		mTextUnreceiveOrderSelect = (TextView)findViewById(R.id.order_text_unreceive_select);
		
		mTextAllOrder.setOnClickListener(this);
		mTextUnpayOrder.setOnClickListener(this);
		mTextUnsendOrder.setOnClickListener(this);
		mTextUnreceiveOrder.setOnClickListener(this);
		
		InitViewPaper();
	}
	
	private void InitViewPaper()
	{
		mPager = (ViewPager)findViewById(R.id.order_viewpager);  
		mFragmentList = new ArrayList<Fragment>();  

		OrderListFragment allOrder = new OrderListFragment();
		OrderListFragment unPayOrder = new OrderListFragment();
		OrderListFragment unSendOrder = new OrderListFragment();
		OrderListFragment unReceiveOrder = new OrderListFragment();
       
        mFragmentList.add(allOrder);  
        mFragmentList.add(unPayOrder);  
        mFragmentList.add(unSendOrder);  
        mFragmentList.add(unReceiveOrder); 
          
        //在fragment里调用viewpager时，使用getChildFragmentManager();
        //在fragmentActivity里调用viewpager时，使用getSupportFragmentManager();
        //mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), mFragmentList));  
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList));  
        mPager.setOnPageChangeListener(new MyOnPageChangeListener()); 
        mPager.setCurrentItem(0);//设置当前显示页
        mPager.setOffscreenPageLimit(3);
	}
	
	private class MyOnPageChangeListener implements OnPageChangeListener{  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
        }  
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
        }  
        @Override  
        public void onPageSelected(int arg0) {  
        	int selectColor = 0xffff373d;
        	int unselectColor = 0xff272727;
        	int unselectBGColor = 0x00000000;
        	mTextAllOrder.setTextColor(unselectColor);
        	mTextUnpayOrder.setTextColor(unselectColor);
        	mTextUnsendOrder.setTextColor(unselectColor);
        	mTextUnreceiveOrder.setTextColor(unselectColor);
        	
        	mTextAllOrderSelect.setBackgroundColor(unselectBGColor);
        	mTextUnpayOrderSelect.setBackgroundColor(unselectBGColor);
        	mTextUnsendOrderSelect.setBackgroundColor(unselectBGColor);
        	mTextUnreceiveOrderSelect.setBackgroundColor(unselectBGColor);
        	
        	switch(arg0)
        	{
        	case 0:
        		mTextAllOrder.setTextColor(selectColor);
        		mTextAllOrderSelect.setBackgroundColor(selectColor);
        		break;
        	case 1:
        		mTextUnpayOrder.setTextColor(selectColor);
        		mTextUnpayOrderSelect.setBackgroundColor(selectColor);
        		break;
        	case 2:
        		mTextUnsendOrder.setTextColor(selectColor);
        		mTextUnsendOrderSelect.setBackgroundColor(selectColor);
        		break;
        	case 3:
        		mTextUnreceiveOrder.setTextColor(selectColor);
        		mTextUnreceiveOrderSelect.setBackgroundColor(selectColor);
        		break;
        	}
        }  
    }  
      
	private class MyFragmentPagerAdapter extends FragmentPagerAdapter{  
	    ArrayList<Fragment> list;  
	    public MyFragmentPagerAdapter(FragmentManager fm,ArrayList<Fragment> list) {  
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
		switch(v.getId())
		{
		case R.id.title_bar_back: //返回
			finish();
			break;
			
		case R.id.order_text_all: //全部订单
			mPager.setCurrentItem(0);
			break;
			
		case R.id.order_text_unpay: //待付款
			mPager.setCurrentItem(1);
			break;
		
		case R.id.order_text_unsend: //未送货
			mPager.setCurrentItem(2);
			break;
			
		case R.id.order_text_unreceive: //未收货
			mPager.setCurrentItem(3);
			break;
		}
	}
	
	
}
