package com.jieyangjiancai.zwj.network;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.jieyangjiancai.zwj.common.ImageUtils;
import com.jieyangjiancai.zwj.common.KMLog;

/**
 * 本地sdcard缓存类.
 * 
 * @author hlai
 * 
 */
public class DiskBitmapCache extends DiskBasedCache implements ImageCache {

	public DiskBitmapCache(File rootDirectory, int maxCacheSizeInBytes) {
		super(rootDirectory, maxCacheSizeInBytes);
	}

	public DiskBitmapCache(File cacheDir) {
		super(cacheDir);
	}

	public Bitmap getBitmap(String url) {
		final Entry requestedItem = get(url);

		getSdcardBitmap(url);
		if (requestedItem == null) {
			// 机内ram没有,取sdcard
			Bitmap b = getSdcardBitmap(url);
			if (b == null) {
				return null;
			} else {
				return b;
			}
		}

		return BitmapFactory.decodeByteArray(requestedItem.data, 0, requestedItem.data.length);
	}

	public void putBitmap(String url, Bitmap bitmap) {

		final Entry entry = new Entry();
		// 下载指定大小的文件以防解码图片时内存溢出
		// 如果 w & h 在请求ImageLoader时使用,则不会调用.
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		 Bitmap downSized = downSizeBitmap(bitmap, 800);
		
		 downSized.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		 byte[] data = baos.toByteArray();
		
		 System.out.println("####### Size of bitmap is ######### " + url +
		 " : " + data.length);
		 entry.data = data;
		// 这个是直接使用
//		entry.data = ImageUtils.convertBitmapToBytes(bitmap);
//		 saveSdcardBitmap(bitmap, url);

		put(url, entry);
		 saveSdcardBitmap(downSized, url);
	}

	public Bitmap downSizeBitmap(Bitmap bitmap, int reqSize) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		

		float scale = 1;
		if (width > height)
		{
			if (width < reqSize)
				return bitmap;
			
			scale = ((float) reqSize) / width;
		}
		else
		{
			if (height < reqSize)
				return bitmap;
			
			scale = ((float) reqSize) / height;
		}
//		float scaleWidth = ((float) reqSize) / width;
//		float scaleHeight = ((float) reqSize) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
		return resizedBitmap;

		/*
		 * if(bitmap.getWidth() < reqSize) { return bitmap; } else { return
		 * Bitmap.createScaledBitmap(bitmap, reqSize, reqSize, false); }
		 */
	}

	/**
	 * 读取sdcard图片
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getSdcardBitmap(String url) {
		String pathRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wujin/Cache";
		int pos = url.lastIndexOf('/');
		String pic = pathRoot + url.substring(pos);

		File imgFile = new File(pic);
		if (imgFile.exists()) {
			KMLog.d(" get sacard" + url);
			Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			return bitmap;
		} else {
			return null;
		}
	}

	/**
	 * 储存图片
	 * 
	 * @param bitmap
	 * @param url
	 */
	public void saveSdcardBitmap(Bitmap bitmap, String url) {
		String pathRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wujin/Cache";
		File savedir = new File(pathRoot);
		if (!savedir.exists()) {
			savedir.mkdirs();
		}
		int pos = url.lastIndexOf('/');
		String pic = pathRoot + url.substring(pos);
		File fPic = new File(pic);
		if (fPic.exists()) {
			KMLog.d(url + "  is exist");
			return;
		}
		try {
			ImageUtils.saveImageToSD(pic, bitmap, 100);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KMLog.d(url + "  saved! ");
	}

}
