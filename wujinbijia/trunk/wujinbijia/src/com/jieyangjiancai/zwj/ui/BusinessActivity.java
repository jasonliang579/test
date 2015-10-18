package com.jieyangjiancai.zwj.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.common.ImageUtils;
import com.jieyangjiancai.zwj.common.ToastMessage;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.CardId;
import com.jieyangjiancai.zwj.network.entity.UpdateUserInfo.CertificateArr;
import com.jieyangjiancai.zwj.utils.Util;
import com.likebamboo.imagechooser.ui.PhotoActivity;

public class BusinessActivity extends BaseActivity{
    
    private GridView bus_gridview;
    private List<Comparable> list = new ArrayList<Comparable>();
    
    private RelativeLayout mLayoutProgress;
    
    private int uploadPathSize = 1;//记录上传图片 的张数
    private String upImageId = "";//记录当前图片 的ID
    public static final String EXTRA_ID = "extra_id"; 
    public static final String EXTRA_CLASS = "extra_class"; 
    private String eClass = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        
        initView();
    }

    private void initView() {
        
        eClass = getIntent().getStringExtra(EXTRA_CLASS);
        mLayoutProgress = (RelativeLayout) findViewById(R.id.layout_progress_bar);
        mLayoutProgress.setVisibility(View.INVISIBLE);
        bus_gridview = (GridView)findViewById(R.id.bus_gridview);
        
        if(eClass == null){//营业执照
        
            ((TextView) findViewById(R.id.title_bar_text)).setText("营业执照");        
            
            List<CertificateArr> l = ConfigUtil.mUserInfo.getCompany_certificate_arr();
            
            for (CertificateArr certificateArr : l) {
                list.add(certificateArr.getThumb());
                upImageId = upImageId + certificateArr.getPicture_id() + ",";
            }
            list.add(R.drawable.add_photo_order);
        }else{//经营商品
            ((TextView) findViewById(R.id.title_bar_text)).setText("经营商品");
            
            List<CertificateArr> l = ConfigUtil.mUserInfo.getProduct_picture_arr();
            
            for (CertificateArr certificateArr : l) {
                list.add(certificateArr.getThumb());
                upImageId = upImageId + certificateArr.getPicture_id() + ",";
            }
            list.add(R.drawable.add_photo_order);
            
        }
        
        
        bus_gridview.setAdapter(new Adpter(this));
        
        
        
        
        bus_gridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
                if(pos == list.size() -1 ){
                    ConfigUtil.doPickPhotoAction(BusinessActivity.this , 9);
                }
            }
        });
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {
            case ConfigUtil.PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的

                if (data == null) {
                    ToastMessage.show(getApplicationContext(), "请尝试使用其他相册浏览!");
                    return;
                }
                ArrayList<String> paths = data.getStringArrayListExtra(PhotoActivity.EXTRA_PATH);
                if(paths == null && paths.size() < 1){
                    ToastMessage.show(getApplicationContext(), "请尝试使用其他相册浏览!");
                    return;
                }
                uploadPathSize = paths.size();
               for (int i = 0; i < paths.size(); i++) {
                   String fullPath = paths.get(i);
                   
                   Bitmap bitmap = ConfigUtil.getThumbnailBitmap(this, fullPath);
                   if(bitmap != null){
                       fullPath = ConfigUtil.getThumbFilePath();
                   }else{
                       return;
                   }

                   File file = new File(fullPath);
                   UploadImage(file);
                   
                   if(bitmap.isRecycled())
                       bitmap.recycle();
               }
                

                break;
            }
            case ConfigUtil.CAMERA_WITH_DATA: {// 照相机程序返回

                String fullPath = ConfigUtil.getPhotoPath();
                Bitmap bitmap = ConfigUtil.getThumbnailBitmap(this, fullPath);
                if(bitmap != null){
                    fullPath = ConfigUtil.getThumbFilePath();
                }else{
                    return;
                }
                uploadPathSize = 1;
                File file = new File(fullPath);
                UploadImage(file);

                break;
            }
            
            }
            
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    
    public void UploadImage(File file)
    {
        mLayoutProgress.setVisibility(View.VISIBLE);
        
         String type = eClass == null ? "7" : "8";//这里区分上传类型
         BackendDataApi bdApi = ((WJApplication)getApplicationContext()).getHttpRequest();
         bdApi.uploadImage(file, ConfigUtil.mUserId, ConfigUtil.mToken, type, reqUploadSuccessListener(), reqUploadErrorListener());
    }
    public Response.Listener<JSONObject> reqUploadSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                
                try {
                    CardId cardId = CardId.parse(response);
                    if(cardId.getError() != 0)
                    {
                        ToastMessage.show(BusinessActivity.this, "上传失败");
                        return;
                    }else{
                    	uploadPathSize--;
                    	
                    	upImageId = upImageId + cardId.getPhotoId();
                		if(uploadPathSize != 0)
                			upImageId = upImageId + ",";
                    	
                    	if(uploadPathSize == 0){
                    		mLayoutProgress.setVisibility(View.INVISIBLE);
                    		ToastMessage.show(BusinessActivity.this, "上传成功,请保存用户资料。");
                    		Intent data = new Intent();
                    		data.putExtra(EXTRA_ID, upImageId);
                    		setResult(RESULT_OK, data);
                            finish();
                    	}
                        
                    }
                    //mEditIdCard.setText(cardId.getPhotoId());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
    }
    public Response.ErrorListener reqUploadErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //String t = error.getMessage();
                //ToastMessage.show(PensonInfoActivity.this, t);
                mLayoutProgress.setVisibility(View.INVISIBLE);
                ToastMessage.show(BusinessActivity.this, "上传失败。");
            }
        };
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
