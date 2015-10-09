package com.jieyangjiancai.zwj.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.base.BaseActivity;
import com.jieyangjiancai.zwj.network.URLs;

public class WebShareActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout mLayoutProgress;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.web_share);
        
		Init();
	}

	
	private void Init()
	{
		findViewById(R.id.title_bar_back).setOnClickListener(this);

		TextView title = (TextView) findViewById(R.id.title_bar_text);
		title.setText("微信分享");
		
		mLayoutProgress = (RelativeLayout)findViewById(R.id.layout_progress_bar);
		mLayoutProgress.setVisibility(View.INVISIBLE);
		
		WebView webView = (WebView)findViewById(R.id.webView);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		//webView.loadUrl("http://www.baidu.com");
		String url = URLs.WEB_SHARE;
		webView.loadUrl(url);
		
		webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
		webView.loadUrl("javascript:setAsyncToken()");
		
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

	
	 public class JavaScriptInterface {
	        Context mContext;
	 
	        /** Instantiate the interface and set the context */
	        JavaScriptInterface(Context c) {
	            mContext = c;
	        }
	 
	        /** Show a toast from the web page */
	        @JavascriptInterface
	        public void showToast(String toast) {
	            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
	        }
	        
	        @JavascriptInterface
	        public void share(String url){
	        	Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.putExtra(Intent.EXTRA_TEXT, url);
				shareIntent.putExtra(Intent.EXTRA_TITLE, "找五金分享");
				shareIntent.setType("text/plain");
				// 设置分享列表的标题，并且每次都显示分享列表
				startActivity(Intent.createChooser(shareIntent, "分享到"));
	        }
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
