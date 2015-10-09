package com.jieyangjiancai.zwj.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.network.URLs;

public class WarrantyActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout mLayoutProgress;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.web_warranty);
        
		Init();
	}

	
	private void Init()
	{
		findViewById(R.id.title_bar_back).setOnClickListener(this);

		TextView title = (TextView) findViewById(R.id.title_bar_text);
		title.setText("担保声明");
		
		mLayoutProgress = (RelativeLayout)findViewById(R.id.layout_progress_bar);
		mLayoutProgress.setVisibility(View.INVISIBLE);
		
		WebView webView = (WebView)findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		//webView.loadUrl("http://www.baidu.com");
		String url = URLs.WEB_WARRANTY;
		webView.loadUrl(url);
		
		
		webView.setWebViewClient(new WebViewClient(){
		//网页加载开始时调用，显示加载提示旋转进度条
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            mLayoutProgress.setVisibility(android.view.View.VISIBLE);
        }

        //网页加载完成时调用，隐藏加载提示旋转进度条
        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            mLayoutProgress.setVisibility(android.view.View.GONE);
        }
        //网页加载失败时调用，隐藏加载提示旋转进度条
        @Override
        public void onReceivedError(WebView view, int errorCode,
                String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
            mLayoutProgress.setVisibility(android.view.View.GONE);
        }
        
    });
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
	
}
