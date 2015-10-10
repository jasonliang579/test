package com.jieyangjiancai.zwj.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jieyangjiancai.zwj.MainActivity;
import com.jieyangjiancai.zwj.R;
import com.jieyangjiancai.zwj.WJApplication;
import com.jieyangjiancai.zwj.base.BaseFragmentActivity;
import com.jieyangjiancai.zwj.common.DES;
import com.jieyangjiancai.zwj.common.KMLog;
import com.jieyangjiancai.zwj.common.ToastMessage;
import com.jieyangjiancai.zwj.common.XGPushRegister;
import com.jieyangjiancai.zwj.common.XGPushRegister.RegisterPushCallback;
import com.jieyangjiancai.zwj.common.sysUtils;
import com.jieyangjiancai.zwj.config.ConfigUtil;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.URLs;
import com.jieyangjiancai.zwj.network.entity.UpdateUserInfo;
import com.umeng.analytics.MobclickAgent;

public class MainActivityNew extends BaseFragmentActivity implements OnClickListener  {
	private Context mContext;
	private ViewPager mViewPager;  
	//private Handler mHandler;
	private ImageHandler mHandler = new ImageHandler(new WeakReference<MainActivityNew>(this));  
	protected static final long MSG_DELAY = 3000;
	private ArrayList<View> mViews = new ArrayList<View>();  
	private RelativeLayout mLayoutProgress;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_new);
        mContext = this;
        Init();
        
        ConfigUtil.initConfig(this);
//        if (ConfigUtil.mUserName == null || ConfigUtil.mUserName.trim().equals(""))
//        {
//        	GetPensonInfo();
//        }
        
        
		String ver = sysUtils.getVersionName(this);
		//检查更新
        ((WJApplication)getApplicationContext()).getHttpRequest().checkUpdate(ver, reqSuccessListener(), reqErrorListener());
		new XGPushRegister(mContext, new RegisterPushCallback() {

			@Override
			public void RegisterPushSuccess(Object data, int flag) {
				// TODO Auto-generated method stub
				KMLog.d("注册信鸽推送成功,登录app.");
			}

			@Override
			public void RegisterPushError() {
				// TODO Auto-generated method stub
			}
		});
		
		MobclickAgent.updateOnlineConfig(this);
    }
	
	private void Init()
	{
		InitUI();
		InitData();
		
		SaveBitmap();
	}
	
	private void InitUI()
	{
		mLayoutProgress = (RelativeLayout)findViewById(R.id.layout_progress_bar);
		mLayoutProgress.setVisibility(View.INVISIBLE);
		
		//findViewById(R.id.image_top_banner).setOnClickListener(this);
		findViewById(R.id.btn_makeorder).setOnClickListener(this);
		findViewById(R.id.btn_getorder).setOnClickListener(this);
		findViewById(R.id.btn_myinfo).setOnClickListener(this);
		findViewById(R.id.btn_brand).setOnClickListener(this);
		findViewById(R.id.btn_new).setOnClickListener(this);
		findViewById(R.id.btn_wechat).setOnClickListener(this);
		findViewById(R.id.btn_phone).setOnClickListener(this);
		
		InitViewPager();
		
	}
	private void InitViewPager()
	{
		mViewPager = (ViewPager)findViewById(R.id.viewpager);
		LayoutInflater inflater = LayoutInflater.from(this);  
        View view1 = inflater.inflate(R.layout.viewpager_industry, null);  
        View view2 = inflater.inflate(R.layout.viewpager_warranty, null);  
        
        mViews.add(view1);  
        mViews.add(view2);  
        mViewPager.setAdapter(new MyPagerAdapter(mViews));  
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {  
            //配合Adapter的currentItem字段进行设置。  
            @Override  
            public void onPageSelected(int arg0) {  
            	mHandler.sendMessage(Message.obtain(mHandler, ImageHandler.MSG_PAGE_CHANGED, arg0, 0));  
            }  
            @Override  
            public void onPageScrolled(int arg0, float arg1, int arg2) {  
            }  
            //覆写该方法实现轮播效果的暂停和恢复  
            @Override  
            public void onPageScrollStateChanged(int arg0) { 
                switch (arg0) {  
                case ViewPager.SCROLL_STATE_SETTLING:
                	break;
                case ViewPager.SCROLL_STATE_DRAGGING:  
                	mHandler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);  
                    break;  
                case ViewPager.SCROLL_STATE_IDLE:  
                	mHandler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);  
                    break;  
                default:  
                    break;  
                }  
            }  
        });  
        //mViewPager.setCurrentItem(Integer.MAX_VALUE/2);//默认在中间，使用户看不到边界  
        //开始轮播效果  
        mHandler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);  
	}
	
	private class MyPagerAdapter extends PagerAdapter{  
        
        private ArrayList<View> viewlist;  
   
        public MyPagerAdapter(ArrayList<View> viewlist) {  
            this.viewlist = viewlist;  
        }  
   
        @Override  
        public int getCount() {  
            return viewlist.size();  
        }  
        
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            return arg0==arg1;  
        } 
        
        @Override    
        public void destroyItem(ViewGroup container, int position,    
                 Object object) {    
             //Warning：不要在这里调用removeView  
        }   
        
        @Override  
        public Object instantiateItem(ViewGroup container, int position) {  
            container.addView(viewlist.get(position));  
            if (position == 0)
            {
            	ImageView imageView = (ImageView)findViewById(R.id.image_top_banner);
            	imageView.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MainActivityNew.this, GuideActivity.class);
						startActivity(intent);
					}
            	});
            }
            else if (position == 1)
            {
            	ImageView imageView = (ImageView)findViewById(R.id.image_warranty);
            	imageView.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MainActivityNew.this, WarrantyActivity.class);
						startActivity(intent);
					}
            	});
            }
            	
//            weibo_button=(Button) findViewById(R.id.button1);  
//            weibo_button.setOnClickListener(new OnClickListener() {  
//                  
//                public void onClick(View v) {  
//                    intent=new Intent(ViewPagerDemo.this,WeiBoActivity.class);  
//                    startActivity(intent);  
//                }  
//            });  
            return viewlist.get(position);  
        }  
    } 
	
	private void InitData()
	{
	}
	
	@Override
	protected void onDestroy()
	{
		ConfigUtil.mUserId = "";
		ConfigUtil.mToken = "";
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
			case R.id.image_top_banner:
			{
				Intent intent = new Intent(MainActivityNew.this, GuideActivity.class);
				this.startActivity(intent);
			}
			break;
			case R.id.btn_makeorder:
			{
				if (ConfigUtil.mUserId == null || ConfigUtil.mUserId.equals("")) {
					Intent intent = new Intent(MainActivityNew.this, LoginActivity.class);
					this.startActivity(intent);
				}
				else if (ConfigUtil.mUserName == null || ConfigUtil.mUserName.equals("") 
						|| ConfigUtil.mUserName.trim().equals(""))
				{
					ToastMessage.show(MainActivityNew.this, "请完善个人资料");
					Intent intent = new Intent(MainActivityNew.this, PensonInfoActivity.class);
					this.startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(MainActivityNew.this, QueryOrderActivity.class);
					this.startActivity(intent);
				}
			}
			break;
	
			case R.id.btn_getorder:
			{
				if (ConfigUtil.mUserId == null || ConfigUtil.mUserId.equals("")) {
					Intent intent = new Intent(MainActivityNew.this, LoginActivity.class);
					this.startActivity(intent);
				}
				else if (ConfigUtil.mUserName == null || ConfigUtil.mUserName.equals("")
						|| ConfigUtil.mUserName.trim().equals(""))
				{
					ToastMessage.show(MainActivityNew.this, "请完善个人资料");
					Intent intent = new Intent(MainActivityNew.this, PensonInfoActivity.class);
					this.startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(MainActivityNew.this, GetOrderActivity.class);
					this.startActivity(intent);
				}
			}
			break;	
			
			case R.id.btn_myinfo:
			{
				// 未登录
				if (ConfigUtil.mUserId == null || ConfigUtil.mUserId.equals("")) {
					Intent intent = new Intent(MainActivityNew.this, LoginActivity.class);
					this.startActivity(intent);
				} 
//				else if (ConfigUtil.mUserName == null || ConfigUtil.mUserName.equals("")
//						|| ConfigUtil.mUserName.trim().equals(""))
//				{
//					Intent intent = new Intent(MainActivityNew.this, PensonInfoActivity.class);
//					this.startActivity(intent);
//				}
				// 已登录
				else {
					Intent intent = new Intent(MainActivityNew.this, MyInfoActivity.class);
					this.startActivity(intent);
				}
			}
			break;
			
			case R.id.btn_brand:
			{
				Intent phoneIntent = new Intent(this, MainActivityBrand.class); 
				startActivity(phoneIntent);
			}
			break;
			
			case R.id.btn_new:
			{
				Intent phoneIntent = new Intent(this, IndustryMessageActivity.class); 
				startActivity(phoneIntent);
			}
			break;
			
			case R.id.btn_wechat:
			{
//				Intent phoneIntent = new Intent(this, WebShareActivity.class); 
//				startActivity(phoneIntent);
				
				ShowShare();
				
			}
			break;
			
			case R.id.btn_phone:
			{
				MakePhoneCall();
			}
			break;
		}
	}
	
	private void MakePhoneCall()
	{
		String phone = ConfigUtil.mPhone;
		Intent phoneIntent = 
				new Intent("android.intent.action.CALL", Uri.parse("tel:"+ phone)); 
		startActivity(phoneIntent);
	}
	
	private void ShowShare()
	{
		
//		http://120.24.94.45:8110/interact/app/giftpack_app.jsp?useId=23
		ConfigUtil.mShareContent = "我们是一个装在手机上的五金城，由广州粤景五金城，车陂瀛富五金城，佛山广佛五金城，华南电器城的5000多家五金经销商共同打造！";
		 if(ConfigUtil.mUserId == null || ConfigUtil.mUserId.length()== 0){
			 ToastMessage.show(this, "请先登录!");
			 return; 
		 }else{
			 String scretKey;
			try {
				scretKey = DES.encryptDES("userId=" + ConfigUtil.mUserId, "Zhao@vVj");
				ConfigUtil.mShareContentURL = URLs.WEB_SHARE_LOCAL + "?" + scretKey;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ToastMessage.show(this, "分享错误.");
				return;
			}
		 }
			 
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 //oks.disableSSOWhenAuthorize(); 
		 
		 // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//		 oks.setTitle(getString(R.string.share));
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 //oks.setTitleUrl("http://sharesdk.cn");
		 // text是分享文本，所有平台都需要这个字段
//		 oks.setText(ConfigUtil.mShareContent);
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
//		 oks.setUrl(ConfigUtil.mShareContentURL);
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 //oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 //oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 //oks.setSiteUrl("http://sharesdk.cn");
		 
		 String testImage = "/sdcard/wujin/logo.jpg";
		// oks.setAddress("12345678901");
		 oks.setTitle(getString(R.string.share));
		 oks.setTitleUrl(ConfigUtil.mShareContentURL);
		 oks.setText(ConfigUtil.mShareContent);
		 oks.setImagePath(testImage);
		//oks.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");
		oks.setUrl(ConfigUtil.mShareContentURL);
		oks.setFilePath(testImage);
		oks.setComment(ConfigUtil.mShareContent);
		oks.setSite(getString(R.string.app_name));
		oks.setSiteUrl(ConfigUtil.mShareContentURL);
//		oks.setVenueName("ShareSDK");
//		oks.setVenueDescription("This is a beautiful place!");
//		oks.disableSSOWhenAuthorize();
//		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
//			public void onShare(Platform platform, ShareParams paramsToShare) {
////					// 改写twitter分享内容中的text字段，否则会超长，
////					// 因为twitter会将图片地址当作文本的一部分去计算长度
////					if ("Twitter".equals(platform.getName())) {
////						paramsToShare.setText(platform.getContext().getString(R.string.share_content_short));
////					}
//			}
//		});
			
		 // 启动分享GUI
		 oks.show(this);
	}
	
	public void share(){
    	Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, "hello");
		shareIntent.putExtra(Intent.EXTRA_TITLE, "找五金分享");
		shareIntent.setType("text/plain");
		// 设置分享列表的标题，并且每次都显示分享列表
		startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

	
	public static String mUpdateUrl;
	public static int mIsForce;
	public static int mIsUpdate;
	public Response.Listener<JSONObject> reqSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// ToastMessage.show(this, response.toString());
				KMLog.d(response.toString());
				if(response.optInt("error") == 0){
					try {
						JSONObject jdata = response.getJSONObject("data");
						mIsUpdate = jdata.optInt("is_need_update",0);
						mIsForce = jdata.optInt("is_force_update",0);
						mUpdateUrl = jdata.getString("update_url");
						if(mIsUpdate == 1){
							if( mIsForce == 1){
								new AlertDialog.Builder(mContext).setTitle("系统提示")//设置对话框标题  
							     .setMessage("有新版本更新")//设置显示的内容  
							     .setCancelable(false)
							     .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮  
							         @Override  
							         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
							             // TODO Auto-generated method stub  
							        	 dialog.cancel();
									 
							        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUpdateUrl));//需要http: 开头
										mContext.startActivity(browserIntent);
										android.os.Process.killProcess(android.os.Process.myPid());
							         }  
							     }).show();//在按键响应事件中显示此对话框 
							}else{
								new AlertDialog.Builder(mContext).setTitle("系统提示")//设置对话框标题  
							     .setMessage("有新版本更新")//设置显示的内容  
							     .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮  
							         @Override  
							         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
							             // TODO Auto-generated method stub  
							        	 dialog.cancel();
									 
							        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUpdateUrl));//需要http: 开头
										mContext.startActivity(browserIntent);
										android.os.Process.killProcess(android.os.Process.myPid());
							         }  
							     }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮  
							         @Override  
							         public void onClick(DialogInterface dialog, int which) {//响应事件  
							             // TODO Auto-generated method stub  
							        	 dialog.cancel();
							         }  
							     }).show();//在按键响应事件中显示此对话框 
							}
							
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}else{
					String msg = response.optString("errormsg");
					ToastMessage.show(mContext, msg);
				}
			}
		};
	}

	public Response.ErrorListener reqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// String t = error.getMessage();
//				 ToastMessage.show(getApplicationContext(), "网络错误!");
			}
		};
	}
	
	public void SaveBitmap() { 
		String picName = "logo.jpg";
		File f1 = new File("/sdcard/wujin/"); 
		if (f1.exists() == false)
			f1.mkdir();
		File f2 = new File("/sdcard/wujin/", picName); 
		if (f2.exists()) { 
			f2.delete(); 
		} 
		try { 
			this.getResources().getDrawable(R.drawable.ic_launcher);
			//PackageInfo info = getPackageManager().getPackageArchiveInfo(fullPath, 0);
			//ApplicationInfo appinfo = info.applicationInfo;
			//label = getPackageManager().getApplicationLabel(appinfo).toString();
			//Drawable drawable = getPackageManager().getApplicationIcon(info.packageName);
			
			Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
			FileOutputStream out = new FileOutputStream(f2); 
			Bitmap bm = drawable2Bitmap(drawable);
			bm.compress(Bitmap.CompressFormat.PNG, 90, out); 
			out.flush(); 
			out.close(); 
			Log.i("wujin", "已经保存Logo"); 
		} catch (FileNotFoundException e) { 
		// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} catch (IOException e) { 
		// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} 
	} 
	
	private Bitmap drawable2Bitmap(Drawable drawable) {  
        if (drawable instanceof BitmapDrawable) {  
            return ((BitmapDrawable) drawable).getBitmap();  
        } else if (drawable instanceof NinePatchDrawable) {  
            Bitmap bitmap = Bitmap  
                    .createBitmap(  
                            drawable.getIntrinsicWidth(),  
                            drawable.getIntrinsicHeight(),  
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                                    : Bitmap.Config.RGB_565);  
            Canvas canvas = new Canvas(bitmap);  
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),  
                    drawable.getIntrinsicHeight());  
            drawable.draw(canvas);  
            return bitmap;  
        } else {  
            return null;  
        }  
    } 
	
	private static class ImageHandler extends Handler{  
        
        /** 
         * 请求更新显示的View。 
         */  
        protected static final int MSG_UPDATE_IMAGE  = 1;  
        /** 
         * 请求暂停轮播。 
         */  
        protected static final int MSG_KEEP_SILENT   = 2;  
        /** 
         * 请求恢复轮播。 
         */  
        protected static final int MSG_BREAK_SILENT  = 3;  
        /** 
         * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。 
         * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页， 
         * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。 
         */  
        protected static final int MSG_PAGE_CHANGED  = 4;  
           
        //轮播间隔时间  
        protected static final long MSG_DELAY = 3000;  
           
        //使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等  
        private WeakReference<MainActivityNew> weakReference;  
        private int currentItem = 0;  
           
        protected ImageHandler(WeakReference<MainActivityNew> wk){  
            weakReference = wk;  
        }  
           
        @Override  
        public void handleMessage(Message msg) {  
            super.handleMessage(msg);  
            //Log.d(LOG_TAG, "receive message " + msg.what);  
            MainActivityNew activity = weakReference.get();  
            if (activity==null){  
                //Activity已经回收，无需再处理UI了  
                return ;  
            }  
            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。  
            if (activity.mHandler.hasMessages(MSG_UPDATE_IMAGE)){  
                activity.mHandler.removeMessages(MSG_UPDATE_IMAGE);  
            }  
            switch (msg.what) {  
            case MSG_UPDATE_IMAGE:  
                currentItem++;  
                if (currentItem >= activity.mViews.size())
                	currentItem = 0;
                activity.mViewPager.setCurrentItem(currentItem);  
                //准备下次播放  
                activity.mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);  
                break;  
            case MSG_KEEP_SILENT:  
                //只要不发送消息就暂停了  
                break;  
            case MSG_BREAK_SILENT:  
                activity.mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);  
                break;  
            case MSG_PAGE_CHANGED:  
                //记录当前的页号，避免播放的时候页面显示不正确。  
                currentItem = msg.arg1;  
                break;  
            default:  
                break;  
            }   
        }  
    }  
	
	
	//获取用户信息
	private void GetPensonInfo()
	{
		mLayoutProgress.setVisibility(View.VISIBLE);
		BackendDataApi bdApi = ((WJApplication)this.getApplicationContext()).getHttpRequest();
		bdApi.userInfo(ConfigUtil.mUserId, ConfigUtil.mToken, reqPensonInfoSuccessListener(), reqPensonInfoErrorListener());
	}
	private Response.Listener<JSONObject> reqPensonInfoSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					String error = response.getString("error");
					if("0".equals(error)){
						//ToastMessage.show(MyInfoActivity.this, response.toString());
						ConfigUtil.mUserInfo = UpdateUserInfo.parse(response);
						ConfigUtil.mUserName = ConfigUtil.mUserInfo.getUserName();
						ConfigUtil.setLoginInfo(ConfigUtil.mToken, ConfigUtil.mUserId, ConfigUtil.mUserName, ConfigUtil.mTokenExpire);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				mLayoutProgress.setVisibility(View.INVISIBLE);
			}
		};
	}
	private Response.ErrorListener reqPensonInfoErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				mLayoutProgress.setVisibility(View.INVISIBLE);
				String t = error.getMessage();
				//ToastMessage.show(MyInfoActivity.this, t);
			}
		};
	}
	
}
