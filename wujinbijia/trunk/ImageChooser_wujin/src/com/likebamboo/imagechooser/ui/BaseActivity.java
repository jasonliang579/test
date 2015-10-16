/**
 * BaseActivity.java
 * ImageChooser
 * 
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.likebamboo.imagechooser.ui;

import com.likebamboo.imagechooser.loader.LocalImageLoader;
import com.likebamboo.imagechooser.utils.ConfigUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author likebamboo
 */
public class BaseActivity extends Activity {
    
    protected String checkStr = "选择";
    protected String searchStr = "搜索";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    
    public void btnClickReturnBeforeActivity(View view){
//        LocalImageLoader.getInstance().evictAll();
        ConfigUtil.getInstance(getApplicationContext()).clear();
        finish();
    }
}
