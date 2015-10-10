package com.jieyangjiancai.zwj.config;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.jieyangjiancai.zwj.common.FileUtils;
import com.jieyangjiancai.zwj.common.ImageUtils;
import com.jieyangjiancai.zwj.common.StringUtils;
import com.jieyangjiancai.zwj.common.ToastMessage;
import com.jieyangjiancai.zwj.network.entity.UpdateUserInfo;

public class ConfigUtil {
	public static boolean IsTest = false;
	
	public static String mToken;
	public static String mUserId;
	public static long mTokenExpire;

	public static String mPhone = "4000987095";
	public static String mUserName;

	public static String mPageSize = "10";
	public static UpdateUserInfo mUserInfo;
	
	public static String mShareContent = "找五金，免费帮你找最便宜的五金水电产品！http://120.24.94.45:8110/interact/app/giftpack_app.jsp?NiNjiNzp49XDgdCdt0aCaQ==";
	public static String mShareContentURL = "http://120.24.94.45:8110/interact/app/giftpack_app.jsp?NiNjiNzp49XDgdCdt0aCaQ==";

	/* 用来标识请求照相功能的activity */
	public static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	public static final int PHOTO_PICKED_WITH_DATA = 3021;
	public static final String PHOTO_SAVE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/wujinphoto/";
	/* 拍照的照片存储位置 */
	private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory().toString() + "/wujinphoto");
	// private static final String PHOTO_NAME = "wujintemp.jpg";
	private static File mCurrentPhotoFile;// 照相机拍照得到的图片

	private static Activity mActivity;
	private static String mFullFileName;// 照片完整路径
	private static String theThumbnail;//缩略图
	// 本地临时配置
	private static SharedPreferences mSettings;
	// 登录账号
	public static final String USER_ID = "user_id";
	public static final String USER_TOKEN = "user_token";
	public static final String USER_NAME = "user_name";
	public static final String USER_LOGIN_EXPIRE = "login_expire";//token过期时间
	
	public static void initConfig(Context context) {
		mSettings = PreferenceManager.getDefaultSharedPreferences(context);
		mToken = mSettings.getString(USER_TOKEN, "");
		mUserId = mSettings.getString(USER_ID, "");
		mUserName = mSettings.getString(USER_NAME, "");
		mTokenExpire = mSettings.getLong(USER_LOGIN_EXPIRE, 0);
		long curTime = System.currentTimeMillis();
		if(curTime >= mTokenExpire){
			setLoginInfo("", "", "", 0);
		}
	}
	
	public static void setLoginInfo(String token, String userId, String userName, long expire){
		mToken = token;
		mUserId = userId;
		mUserName = userName;
		mTokenExpire = expire;
		
		SharedPreferences.Editor edit = mSettings.edit();
		edit.putString(USER_TOKEN, token);
		edit.putString(USER_ID, userId);
		edit.putString(USER_NAME, userName);
		edit.putLong(USER_LOGIN_EXPIRE, expire);
		edit.commit();
	}
	
	public static void setLogout(){
		setLoginInfo("", "", "", 0);
	}
	
	public static String getStatus(String status) {
		String str = "";
		if (status.equals("0"))
			str = "抢单中";
		else if (status.equals("1"))
			str = "完成抢单";
		else if (status.equals("2"))
			str = "交易取消";
		else if (status.equals("3"))
			str = "待支付";
		else if (status.equals("4"))
			str = "待客服确认收款";
		else if (status.equals("5"))
			str = "待收货";
		else if (status.equals("6"))
			str = "交易成功";

		return str;
	}
	
	public static String getFormatedDateTime(long dateTime) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date date = new Date(dateTime*1000);
        return sDateFormat.format(date);
    }

	public static void doPickPhotoAction(Context context) {
		mActivity = (Activity) context;

		// Wrap our context to inflate list items using correct theme
		final Context dialogContext = new ContextThemeWrapper(context, android.R.style.Theme_Light);
		String cancel = "返回";
		String[] choices;
		choices = new String[2];
		choices[0] = "拍照  "; // 拍照
		choices[1] = "从图库中选择"; // 从相册中选择
		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext, android.R.layout.simple_list_item_1, choices);

		final AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
		// builder.setTitle(R.string.attachToContact);
		builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				switch (which) {
				case 0: {
					String status = Environment.getExternalStorageState();
					if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
						doTakePhoto();// 用户点击了从照相机获取
					} else {
						// showToast("没有SD卡");
					}
					break;

				}
				case 1:
					doPickPhotoFromGallery();// 从相册中去获取
					break;
				}
			}
		});
		builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
		builder.create().show();
	}

	public static File GetPhotoFile() {
		// return mCurrentPhotoFile;
		File file = null;
		if (mFullFileName != null) {
			file = new File(mFullFileName);
		}
		return file;

	}

	public static String getPhotoPath() {
		return mFullFileName;
	}

	// 拍照获取图片
	private static void doTakePhoto() {
		try {
			String savePath = PHOTO_SAVE_PATH;//Environment.getExternalStorageDirectory().getAbsolutePath() + "/wujinphoto";
			// PHOTO_DIR.mkdirs();// 创建照片的存储目录
			File savedir = new File(savePath);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
			String fileName = getPhotoFileName();
			mFullFileName = savePath + fileName;
			File file = new File(savePath, fileName);
			final Intent intent = getTakePickIntent(file);
			mActivity.startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			// Toast.makeText(this, R.string.photoPickerNotFoundText,
			// Toast.LENGTH_LONG).show();
		}
	}

	private static Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}

	// 用当前时间给取得的图片命名
	private static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		// "'IMG'_yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date) + ".jpg";
	}

	// 请求Gallery程序
	private static void doPickPhotoFromGallery() {
		try {
			// Launch picker to choose photo for selected contact
			final Intent intent = getPhotoPickIntent();
			mActivity.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			// Toast.makeText(this, R.string.photoPickerNotFoundText1,
			// Toast.LENGTH_LONG).show();
		}
	}

	// 封装请求Gallery的intent
	private static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		// intent.putExtra("crop", "true");
		// intent.putExtra("aspectX", 1);
		// intent.putExtra("aspectY", 1);
		// intent.putExtra("outputX", 80);
		// intent.putExtra("outputY", 80);
		// intent.putExtra("return-data", true);
		return intent;
	}
	
	public static Bitmap getThumbnailBitmap(Context context,String fullPath) {
		if (!StringUtils.isEmpty(fullPath)) {
			// 存放照片的文件夹
			String savePath = ConfigUtil.PHOTO_SAVE_PATH;
			File imgFile = null;
			
			File savedir = new File(savePath);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}

			String largeFileName = FileUtils.getFileName(fullPath);
			String largeFilePath = savePath + largeFileName;
			// 判断是否已存在缩略图
			if (largeFileName.startsWith("thumb_") && new File(largeFilePath).exists()) {
				theThumbnail = largeFilePath;
				imgFile = new File(theThumbnail);
			} else {
				// 生成上传的800宽度图片
				String thumbFileName = "thumb_" + largeFileName;
				theThumbnail = savePath + thumbFileName;
				if (new File(theThumbnail).exists()) {
					imgFile = new File(theThumbnail);
				} else {
					try {
						// 压缩上传的图片
						ImageUtils.createImageThumbnail(context, fullPath, theThumbnail, 800, 80);
						imgFile = new File(theThumbnail);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			Bitmap b = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			return b;
		} else {
			ToastMessage.show(context, "图片path 为空!");
			return null;
		}
	}
	
	public static String getThumbFilePath(){
		return theThumbnail;
	}
		
}
