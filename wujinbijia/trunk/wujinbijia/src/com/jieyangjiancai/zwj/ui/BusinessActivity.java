package com.jieyangjiancai.zwj.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.network.entity.UpdateUserInfo.CertificateArr;
import com.jieyangjiancai.zwj.utils.Util;

public class BusinessActivity extends BaseActivity{
    
    private GridView bus_gridview;
    private List list = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        
        initView();
    }

    private void initView() {
        bus_gridview = (GridView)findViewById(R.id.bus_gridview);
        ((TextView) findViewById(R.id.title_bar_text)).setText("营业执照");        
        
        List<CertificateArr> l = ConfigUtil.mUserInfo.getCompany_certificate_arr();
        
        for (CertificateArr certificateArr : l) {
            list.add(certificateArr.getThumb());
        }
        list.add(R.drawable.add_photo_order);
        bus_gridview.setAdapter(new Adpter(this));
    }
    
    
    
    class Adpter extends BaseAdapter{
        private Context context;
        public Adpter(Context context) {
            this.context = context;
        }
        
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
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
        public View getView(int pos, View view, ViewGroup arg2) {
            ImageView imageView = null;
            if(view == null){
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, Util.dip2px(context, 126)));//设置ImageView对象布局 
                imageView.setAdjustViewBounds(false);//设置边界对齐 
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型 
                imageView.setPadding(6, 6, 6, 6);//设置间距
            }else{
                imageView = (ImageView)view;
            }
            if(pos != list.size()-1 )
                imageLoader.displayImage(list.get(pos).toString(), imageView, options);
            else
                imageLoader.displayImage("drawable://" + Integer.valueOf(list.get(pos).toString()), imageView, options);
            
            return imageView;
        }
        
    }
}
