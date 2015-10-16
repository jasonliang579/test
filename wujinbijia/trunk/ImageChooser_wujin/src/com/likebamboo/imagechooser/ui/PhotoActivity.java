/**
 * PhotoActivity.java
 * ImageChooser
 * 
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.likebamboo.imagechooser.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.likebamboo.imagechooser.R;
import com.likebamboo.imagechooser.listener.OnTaskResultListener;
import com.likebamboo.imagechooser.model.ImageGroup;
import com.likebamboo.imagechooser.task.ImageLoadTask;
import com.likebamboo.imagechooser.ui.adapter.ImageGroupAdapter;
import com.likebamboo.imagechooser.utils.SDcardUtil;
import com.likebamboo.imagechooser.utils.TaskUtil;
import com.likebamboo.imagechooser.utils.Util;
import com.likebamboo.imagechooser.widget.LoadingLayout;

import java.util.ArrayList;

/**
 * 图片选择主界面，列出所有图片文件夹
 * 
 * @author likebamboo
 */
public class PhotoActivity extends BaseActivity implements OnItemClickListener {
    /**
     * loading布局
     */
    private LoadingLayout mLoadingLayout = null;

    /**
     * 图片组GridView
     */
    private GridView mGroupImagesGv = null;

    /**
     * 适配器
     */
    private ImageGroupAdapter mGroupAdapter = null;

    /**
     * 图片扫描一般任务
     */
    private ImageLoadTask mLoadTask = null;
    
    public static Context context;
    
    public static int REQUEST_CODE = 0x123; 
    public static String EXTRA_PATH = "extra_path";
    
    private Button btn_option;
    
    public static int maxSelected = 3;
    public static String EXTRA_MAX = "max";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFormat(PixelFormat.TRANSLUCENT);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo);
        
        TextView title = (TextView)findViewById(R.id.title);
        title.setText(getResources().getString(R.string.activity_title));
        btn_option = (Button)findViewById(R.id.option);
        btn_option.setVisibility(View.VISIBLE);
        
        maxSelected = getIntent().getIntExtra(EXTRA_MAX, 3);
        
        initView();
        loadImages();
        this.context = this;
        
        btn_option.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent data = new Intent();
                ArrayList<String> paths = Util.getSeletedImages(PhotoActivity.this);
                data.putStringArrayListExtra(PhotoActivity.EXTRA_PATH, paths);
                setResult(RESULT_OK, data);
                btnClickReturnBeforeActivity(null);
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> selectedList = Util.getSeletedImages(context);
        if(selectedList == null) return;
        int size = selectedList.size();
        if(size > 0){
            btn_option.setBackgroundResource(R.drawable.btn_check);
            if(size == 1)
                btn_option.setText(checkStr + "(" + selectedList.size() + ")");
            else
                btn_option.setText(searchStr + "(" + selectedList.size() + ")"); 
            btn_option.setEnabled(true);
        }else{
            btn_option.setText(checkStr);
            btn_option.setBackgroundResource(R.drawable.btn_select_normal);
            btn_option.setEnabled(false);
        }
    }
    
    public static Context getContext(){
        return context;
    }
    
    /**
     * 初始化界面元素
     */
    private void initView() {
        mLoadingLayout = (LoadingLayout)findViewById(R.id.loading_layout);
        mGroupImagesGv = (GridView)findViewById(R.id.images_gv);
    }

    /**
     * 加载图片
     */
    private void loadImages() {
        mLoadingLayout.showLoading(true);
        //如果没有存储卡，则显示nosd卡
        if (!SDcardUtil.hasExternalStorage()) {
            mLoadingLayout.showEmpty(getString(R.string.donot_has_sdcard));
            return;
        }

        // 线程正在执行
        if (mLoadTask != null && mLoadTask.getStatus() == Status.RUNNING) {
            return;
        }

        mLoadTask = new ImageLoadTask(this, new OnTaskResultListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResult(boolean success, String error, Object result) {
                mLoadingLayout.showLoading(false);
                // 如果加载成功
                if (success && result != null && result instanceof ArrayList) {
                    setImageAdapter((ArrayList<ImageGroup>)result);
                } else {
                    // 加载失败，显示错误提示
                    mLoadingLayout.showFailed(getString(R.string.loaded_fail));
                }
            }
        });
        TaskUtil.execute(mLoadTask);
    }

    /**
     * 构建GridView的适配器
     * 
     * @param data
     */
    private void setImageAdapter(ArrayList<ImageGroup> data) {
        if (data == null || data.size() == 0) {
            mLoadingLayout.showEmpty(getString(R.string.no_images));
        }
        mGroupAdapter = new ImageGroupAdapter(this, data, mGroupImagesGv);
        mGroupImagesGv.setAdapter(mGroupAdapter);
        mGroupImagesGv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
        ImageGroup imageGroup = mGroupAdapter.getItem(position);
        if (imageGroup == null) {
            return;
        }
        ArrayList<String> childList = imageGroup.getImages();
        Intent mIntent = new Intent(PhotoActivity.this, ImageListActivity.class);
        mIntent.putExtra(ImageListActivity.EXTRA_TITLE, imageGroup.getDirName());
        mIntent.putStringArrayListExtra(ImageListActivity.EXTRA_IMAGES_DATAS, childList);
        startActivityForResult(mIntent, REQUEST_CODE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            setResult(RESULT_OK, data);
            btnClickReturnBeforeActivity(null);
        }
    }
    @Override
    public void onBackPressed() {
        btnClickReturnBeforeActivity(null);
    }
}
