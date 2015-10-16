/**
 * ImageGroupAdapter.java
 * ImageChooser
 * 
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.likebamboo.imagechooser.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.likebamboo.imagechooser.R;
import com.likebamboo.imagechooser.loader.LocalImageLoader;
import com.likebamboo.imagechooser.loader.LocalImageLoader.ImageCallBack;
import com.likebamboo.imagechooser.ui.ImageListActivity;
import com.likebamboo.imagechooser.utils.Util;
import com.likebamboo.imagechooser.widget.MyImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 某个图片组中图片列表适配器
 * 
 * @author likebamboo
 */
public class ImageListAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 图片列表
     */
    private ArrayList<String> mDataList = new ArrayList<String>();

    /**
     * 选中的图片列表
     */
    private ArrayList<String> mSelectedList = new ArrayList<String>();

    /**
     * 容器
     */
    private View mContainer = null;
    private ImageListActivity imageActivity;

    public ImageListAdapter(Context context, ArrayList<String> list, View container , ImageListActivity imageActivity) {
        mDataList = list;
        mContext = context;
        mSelectedList = Util.getSeletedImages(context);
        mContainer = container;
        this.imageActivity = imageActivity;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public String getItem(int position) {
        if (position < 0 || position > mDataList.size()) {
            return null;
        }
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.image_list_item, null);
            holder.mImageIv = (MyImageView)view.findViewById(R.id.list_item_iv);
            holder.mClickArea = view.findViewById(R.id.list_item_cb_click_area);
            holder.mSelectedCb = (CheckBox)view.findViewById(R.id.list_item_cb);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }

        final String path = getItem(position);
        // 加载图片
        holder.mImageIv.setTag(path);
        // 加载图片
        // 利用NativeImageLoader类加载本地图片
        Bitmap bitmap = LocalImageLoader.getInstance().loadImage(path, holder.mImageIv.getPoint(),
                new ImageCallBack() {
                    @Override
                    public void onImageLoader(Bitmap bitmap, String path) {
                        ImageView mImageView = (ImageView)mContainer.findViewWithTag(path);
                        if (bitmap != null && mImageView != null) {
                            mImageView.setImageBitmap(bitmap);
                        }
                    }
                });
        if (bitmap != null) {
            holder.mImageIv.setImageBitmap(bitmap);
        } else {
            holder.mImageIv.setImageResource(R.drawable.pic_thumb);
        }

        holder.mSelectedCb.setChecked(false);
        // 该图片是否选中
        for (String selected : mSelectedList) {
            if (selected.equals(path)) {
                holder.mSelectedCb.setChecked(true);
            }
        }

        // 可点区域单击事件
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = holder.mSelectedCb.isChecked();
                
                if (!checked) {
                    holder.mSelectedCb.setChecked(addImage(path));
                } else {
                    holder.mSelectedCb.setChecked(!checked);
                    deleteImage(path);
                }
            }
        });

        return view;
    }

    /**
     * 将图片地址添加到已选择列表中
     * 
     * @param path
     */
    private boolean addImage(String path) {
        if (mSelectedList.contains(path)) {
            return true;
        }
        if(mSelectedList.size() >= 9){
            Toast.makeText(mContext, "最多支持9张图标", Toast.LENGTH_SHORT).show();
            return false;
        }
            
        mSelectedList.add(path);
        imageActivity.setBtnOption(mSelectedList);
        return true;
    }

    /**
     * 将图片地址从已选择列表中删除
     * 
     * @param path
     */
    private void deleteImage(String path) {
        mSelectedList.remove(path);
        imageActivity.setBtnOption(mSelectedList);
    }

    /**
     * 获取已选中的图片列表
     * 
     * @return
     */
    public ArrayList<String> getSelectedImgs() {
        return mSelectedList;
    }

    static class ViewHolder {
        public MyImageView mImageIv;

        public View mClickArea;

        public CheckBox mSelectedCb;

    }

}
