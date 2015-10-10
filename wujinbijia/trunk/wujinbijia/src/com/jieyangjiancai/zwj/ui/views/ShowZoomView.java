package com.jieyangjiancai.zwj.ui.views;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ZoomControls;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.network.BackendDataApi;

public class ShowZoomView {

	private Activity mActivity;
	private RelativeLayout mLayoutFull;
	private ZoomControls zoomCtrl;// 系统自带的缩放控制组件
	private ImageZoomView zoomView;// 自定义的图片显示组件
	private ImageZoomState zoomState;// 图片缩放和移动状态类
	private SimpleImageZoomListener zoomListener;// 缩放事件监听器
	private Bitmap mFullBitmap = null;
	
	public ShowZoomView(Activity activity)
	{
		mActivity = activity;
	}
	private void ShowZoomImageView(Drawable drawable)
	{
		if (zoomState == null)
		{
			zoomState = new ImageZoomState();
			zoomListener = new SimpleImageZoomListener();
			zoomListener.setZoomState(zoomState);
			zoomCtrl = (ZoomControls)mActivity.findViewById(R.id.zoomCtrl);
			this.setImageController();
			zoomView = (ImageZoomView)mActivity.findViewById(R.id.zoomView);
			mLayoutFull = (RelativeLayout)mActivity.findViewById(R.id.layout_full_image_view);
		}
		mLayoutFull.setVisibility(View.VISIBLE);
		BitmapDrawable bd = (BitmapDrawable) drawable;
	    zoomView.setImage(bd.getBitmap());
	    zoomView.setImageZoomState(zoomState);
	    zoomView.setOnTouchListener(zoomListener);
	    zoomView.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
              setFullScreen();
           }
	    });
	    zoomState.setmZoom(1);
	}
	
	public void ShowZoomImageView(Drawable drawable, String url)
	{
		if (zoomState == null)
		{
			zoomState = new ImageZoomState();
			zoomListener = new SimpleImageZoomListener();
			zoomListener.setZoomState(zoomState);
			zoomCtrl = (ZoomControls)mActivity.findViewById(R.id.zoomCtrl);
			this.setImageController();
			zoomView = (ImageZoomView)mActivity.findViewById(R.id.zoomView);
			mLayoutFull = (RelativeLayout)mActivity.findViewById(R.id.layout_full_image_view);
		}
		mLayoutFull.setVisibility(View.VISIBLE);
		//BitmapDrawable bd = (BitmapDrawable) drawable;
	    //zoomView.setImage(bd.getBitmap());
	    zoomView.setImageZoomState(zoomState);
	    zoomView.setOnTouchListener(zoomListener);
	    zoomView.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
              setFullScreen();
           }
	    });
	    zoomState.setmZoom(1);
	    
	    if (url != null)
	    	LoadImage(url);
	}
	
	private void LoadImage(String url)
	{
		if(url == null || url.equals(""))
			return;
		
		Log.d("wujin","LoadImage: full, url="+url);
		
		//url="http://dev.dianlanbijia.com/images/201509261408259723311208448_thumb_20150926140816.jpg";
		//url="http://dev.dianlanbijia.com/images/201509261441571064867773611_thumb_20150926144137.jpg";

		BackendDataApi bdApi = ((WJApplication)mActivity.getApplicationContext()).getHttpRequest();
		bdApi.ImageLoad(url, reqImageListener());
	}
	private ImageLoader.ImageListener reqImageListener() {
		return new ImageLoader.ImageListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onResponse(ImageContainer arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (mFullBitmap != null && !mFullBitmap.isRecycled())
				{
					mFullBitmap.recycle();
					mFullBitmap = null;
				}
				mFullBitmap = arg0.getBitmap();
				
				zoomView.setImage(mFullBitmap);
				Log.d("wujin","LoadImage finish");
			}
		};
	}

	private void setImageController() {
		zoomCtrl.setOnZoomInClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
              float z = zoomState.getmZoom() + 0.25f;// 图像大小增加原来的0.25倍
              zoomState.setmZoom(z);
              zoomState.notifyObservers();
           }
       });

       zoomCtrl.setOnZoomOutClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
              float z = zoomState.getmZoom() - 0.25f;// 图像大小减少原来的0.25倍
              zoomState.setmZoom(z);
              zoomState.notifyObservers();
           }
       });
	}

    /**
     * 隐藏处ImageZoomView外地其他组件，全屏显示
     */
    private void setFullScreen() {
       if (zoomCtrl != null) {
           if (zoomCtrl.getVisibility() == View.VISIBLE) {
              // zoomCtrl.setVisibility(View.GONE);
              zoomCtrl.hide(); // 有过度效果
           } else if (zoomCtrl.getVisibility() == View.GONE) {
              // zoomCtrl.setVisibility(View.VISIBLE);
              zoomCtrl.show();// 有过渡效果
           }
       }
    }
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mLayoutFull == null)
			return false;
		if (mLayoutFull.getVisibility() == View.GONE)
			return false;
		
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(mLayoutFull != null)
				mLayoutFull.setVisibility(View.GONE);
			return true;
		}
		return false;
	}
}
