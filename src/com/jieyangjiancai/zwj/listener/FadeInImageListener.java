package com.jieyangjiancai.zwj.listener;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.jieyangjiancai.zwj.R;

/**
 * 带动画显示图片的图片监听
 * @author hlai
 *
 */
public class FadeInImageListener implements ImageLoader.ImageListener {

	WeakReference<ImageView> mImageView;
	Context mContext;
	boolean isBg = false;
	
	public FadeInImageListener(ImageView image,Context context) {
		mImageView = new WeakReference<ImageView>(image);
		mContext = context;
	}
	
	public FadeInImageListener(ImageView image,Context context,boolean isBg) {
		mImageView = new WeakReference<ImageView>(image);
		mContext = context;
		this.isBg = isBg;
	}
	
	@Override
	public void onErrorResponse(VolleyError arg0) {
		if(mImageView.get() != null) {
			if(isBg){
				mImageView.get().setBackgroundResource(R.drawable.ic_launcher);
			}else{
				mImageView.get().setImageResource(R.drawable.ic_launcher);
			}
		}
	}

	@SuppressLint("NewApi") @Override
	public void onResponse(ImageContainer response, boolean arg1) {
		if(mImageView.get() != null) {
			ImageView image = mImageView.get();
			if(response.getBitmap() != null) {
                image.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
                if(isBg){
                	image.setBackgroundDrawable(new BitmapDrawable(response.getBitmap()));
                }else{
                	image.setImageBitmap(response.getBitmap());
                }
			} else {
				if(isBg){
                	image.setBackgroundResource(R.drawable.ic_launcher);
                }else{
                	image.setImageResource(R.drawable.ic_launcher);
                }
			}
		}
	}
	
}