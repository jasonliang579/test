package com.jieyangjiancai.zwj.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jieyangjiancai.zwj.R;
import com.umeng.analytics.MobclickAgent;

public class GuideActivity extends Activity implements OnClickListener, OnPageChangeListener {
	private RelativeLayout mLayoutProgress;
	private ViewPager mViewPager;
	private List<View> mViews = new ArrayList<View>();  
	private List<ImageView> mDotViews = new ArrayList<ImageView>();  
	private LayoutInflater mInflater;  
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        
		Init();
	}

	
	private void Init()
	{
		mInflater = getLayoutInflater(); 
		
		// 得到views集合  
		mViews = new ArrayList<View>();  
        //此处可以根据需要自由设定，这里只是简单的测试  
        for (int i = 1; i <= 12; i++) {  
            View view = mInflater.inflate(R.layout.guide_item, null);  
            mViews.add(view);  
        } 
        
        mDotViews.add((ImageView)findViewById(R.id.imageview1));
        mDotViews.add((ImageView)findViewById(R.id.imageview2));
        mDotViews.add((ImageView)findViewById(R.id.imageview3));
        mDotViews.add((ImageView)findViewById(R.id.imageview4));
        mDotViews.add((ImageView)findViewById(R.id.imageview5));
        mDotViews.add((ImageView)findViewById(R.id.imageview6));
        mDotViews.add((ImageView)findViewById(R.id.imageview7));
        mDotViews.add((ImageView)findViewById(R.id.imageview8));
        mDotViews.add((ImageView)findViewById(R.id.imageview9));
        mDotViews.add((ImageView)findViewById(R.id.imageview10));
        mDotViews.add((ImageView)findViewById(R.id.imageview11));
        mDotViews.add((ImageView)findViewById(R.id.imageview12));
        mDotViews.get(0).setImageResource(R.drawable.dot_select);
        
		mViewPager = (ViewPager)findViewById(R.id.viewpager);

		// 1.设置幕后item的缓存数目  
		mViewPager.setOffscreenPageLimit(2);   
		// 2.设置页与页之间的间距  
		//mViewPager.setPageMargin(10);  
		// 3.将父类的touch事件分发至viewPgaer，否则只能滑动中间的一个view对象  
//		container.setOnTouchListener(new View.OnTouchListener() {  
//		    @Override  
//		    public boolean onTouch(View v, MotionEvent event) {  
//		        return viewPager.dispatchTouchEvent(event);  
//		    }  
//		});  
		
		mViewPager.setAdapter(new MyAdapter()); // 为viewpager设置adapter  
        mViewPager.setOnPageChangeListener(this);// 设置监听器  
	}


	class MyAdapter extends PagerAdapter {  
		  
        /** 
         * PagerAdapter管理数据大小 
         */  
        @Override  
        public int getCount() {  
            return mViews.size();  
        }  
  
        /** 
         * 关联key 与 obj是否相等，即是否为同一个对象 
         */  
        @Override  
        public boolean isViewFromObject(View view, Object obj) {  
            return view == obj; // key  
        }  
  
        /** 
         * 销毁当前page的相隔2个及2个以上的item时调用 
         */  
        @Override  
        public void destroyItem(ViewGroup container, int position, Object object) {  
            container.removeView((View) object); // 将view 类型 的object熊容器中移除,根据key  
        }  
  
        /** 
         * 当前的page的前一页和后一页也会被调用，如果还没有调用或者已经调用了destroyItem 
         */  
        @Override  
        public Object instantiateItem(ViewGroup container, int position) {  
            View view = mViews.get(position);  
            // 如果访问网络下载图片，此处可以进行异步加载  
            ImageView img = (ImageView) view.findViewById(R.id.image_content1);
            //int resid = position + 1;
           
            img.setImageResource( R.drawable.guide_index1 + position);
            container.addView(view);  
            return mViews.get(position); // 返回该view对象，作为key  
        }  
    }  
  
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {  
        if (mViewPager != null) {  
        	mViewPager.invalidate();  
        }  
    }  
  
    // 一个新页被调用时执行,仍为原来的page时，该方法不被调用  
    public void onPageSelected(int position) {  
    	for (int i=0; i<mDotViews.size(); i++)
    		mDotViews.get(i).setImageResource(R.drawable.dot);
    	ImageView imageview = mDotViews.get(position);
    	imageview.setImageResource(R.drawable.dot_select);
    }  
  
    /* 
     * SCROLL_STATE_IDLE: pager处于空闲状态 SCROLL_STATE_DRAGGING： pager处于正在拖拽中 
     * SCROLL_STATE_SETTLING： pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程 
     */  
    public void onPageScrollStateChanged(int state) {  
    }  
  
  
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.title_bar_back:
			finish();
			break;
		}
	}
	@Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
    }
    
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
