/**
 * ImageListActivity.java
 * ImageChooser
 * 
 * Created by likebamboo on 2014-4-23
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.likebamboo.imagechooser.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.likebamboo.imagechooser.R;
import com.likebamboo.imagechooser.loader.LocalImageLoader;
import com.likebamboo.imagechooser.ui.adapter.ImageListAdapter;
import com.likebamboo.imagechooser.utils.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 某个文件夹下的所有图片列表
 * 
 * @author likebamboo
 */
public class ImageListActivity extends BaseActivity implements OnItemClickListener {

    /**
     * title
     */
    public static final String EXTRA_TITLE = "extra_title";

    /**
     * 图片列表extra
     */
    public static final String EXTRA_IMAGES_DATAS = "extra_images";

    /**
     * 图片列表GridView
     */
    private GridView mImagesGv = null;

    /**
     * 图片地址数据源
     */
    private ArrayList<String> mImages = new ArrayList<String>();

    /**
     * 适配器
     */
    private ImageListAdapter mImageAdapter = null;

    private int max = 0;// 检查前10个文件是否有缓存的

    private Button btn_option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        TextView title_tv = (TextView)findViewById(R.id.title);
        btn_option = (Button)findViewById(R.id.option);
        btn_option.setVisibility(View.VISIBLE);
        setBtnOption(Util.getSeletedImages(this));

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (!TextUtils.isEmpty(title)) {
            // setTitle(title);
            title_tv.setText(title);
        } else
            title_tv.setText(getResources().getString(R.string.activity_title));

        initView();
        if (getIntent().hasExtra(EXTRA_IMAGES_DATAS)) {
            mImages = getIntent().getStringArrayListExtra(EXTRA_IMAGES_DATAS);
            Collections.reverse(mImages);// 反转顺序

            for (int i = 0; i < 10; i++) {
                if (mImages.size() > i) {
                    File file = new File(mImages.get(i));
                    if (!file.exists()) {
                        mImages.remove(i);
                        i--;
                    }
                }
            }

            setAdapter(mImages);
        }

        btn_option.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent data = new Intent();
                ArrayList<String> paths = mImageAdapter.getSelectedImgs();
                data.putStringArrayListExtra(PhotoActivity.EXTRA_PATH, paths);
                setResult(RESULT_OK, data);
                btnClickReturnBeforeActivity(null);
            }
        });
    }

    /**
     * 设置按钮文字
     */
    public void setBtnOption(ArrayList<String> selectedList) {
        if (selectedList == null)
            return;
        int size = selectedList.size();
        if (size > 0) {
            btn_option.setBackgroundResource(R.drawable.btn_check);
            if (size == 1)
                btn_option.setText(checkStr + "(" + selectedList.size() + ")");
            else
                btn_option.setText(searchStr + "(" + selectedList.size() + ")");
            btn_option.setEnabled(true);
        } else {
            btn_option.setText(checkStr);
            btn_option.setBackgroundResource(R.drawable.btn_select_normal);
            btn_option.setEnabled(false);
        }
    }

    /**
     * 初始化界面元素
     */
    private void initView() {
        mImagesGv = (GridView)findViewById(R.id.images_gv);
    }

    /**
     * 构建并初始化适配器
     * 
     * @param datas
     */
    private void setAdapter(ArrayList<String> datas) {
        mImageAdapter = new ImageListAdapter(this, datas, mImagesGv, this);
        mImagesGv.setAdapter(mImageAdapter);
        mImagesGv.setOnItemClickListener(this);
    }

    /*
     * (non-Javadoc)
     * @see
     * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
     * .AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        LocalImageLoader.getInstance().evictAll();

        String picPath = mImages.get(arg2);
        Intent data = new Intent();
        data.putExtra(PhotoActivity.EXTRA_PATH, picPath);
        setResult(RESULT_OK, data);
        finish();
        // Intent i = new Intent(this, ImageBrowseActivity.class);
        // i.putExtra(ImageBrowseActivity.EXTRA_IMAGES, mImages);
        // i.putExtra(ImageBrowseActivity.EXTRA_INDEX, arg2);
        // startActivity(i);
    }

    @Override
    public void btnClickReturnBeforeActivity(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (mImageAdapter != null) {
            Util.saveSelectedImags(this, mImageAdapter.getSelectedImgs());
        }
        super.onBackPressed();
    }

}
