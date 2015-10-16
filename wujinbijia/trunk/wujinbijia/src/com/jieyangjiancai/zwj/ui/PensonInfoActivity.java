package com.jieyangjiancai.zwj.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.jieyangjiancai.zwj.data.AddressItem;
import com.jieyangjiancai.zwj.network.BackendDataApi;
import com.jieyangjiancai.zwj.network.entity.AddressCode;
import com.jieyangjiancai.zwj.network.entity.CardId;
import com.jieyangjiancai.zwj.network.entity.UpdateUserInfo;

public class PensonInfoActivity extends BaseActivity implements OnClickListener {
	
	private String mProvinces[];

	private ImageView mImageBuySelect;
	private ImageView mImageSaleSelect;
	
	private AddressCode mAddressCode;
	
	private EditText mEditName;
	private String mUserType; //用户类型：1-买方，2-卖方
	private EditText mEditCompany;
	private EditText mEditAddress;
	private EditText mEditIdCard;
	
	private EditText mEditProvince;
	private MyListAdapter mAdapterProvince;
	private AlertDialog mDialogProvince;
	private int mIndexProvince;
	
	private EditText mEditCity;
	private MyListAdapter mAdapterCity;
	private AlertDialog mDialogCity;
	private int mIndexCity;
	
	private EditText mEditArea;
	private MyListAdapter mAdapterArea;
	private AlertDialog mDialogArea;
	private int mIndexArea;
	
	private ImageView mImageUpload;
	
	private RelativeLayout mLayoutProgress;
	
	private RelativeLayout pens_layout_company;
	private RelativeLayout pens_layout_business;
	
	private String companyString = "";//记录返回的公司介绍
	private String businessString = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_personinfo);
		
		Init();
		GetAddressCode();
	}
	
	private void Init()
	{
		mLayoutProgress = (RelativeLayout) findViewById(R.id.layout_progress_bar);
		mLayoutProgress.setVisibility(View.INVISIBLE);
		
		mEditName = (EditText)findViewById(R.id.edit_name);
		mUserType = "1";
		mEditCompany = (EditText)findViewById(R.id.edit_company);
		mEditAddress = (EditText)findViewById(R.id.edit_address);
		mEditIdCard = (EditText)findViewById(R.id.edit_idcard);
		mEditIdCard.setEnabled(false);
		
		findViewById(R.id.btn_personinfo_conform).setOnClickListener(this);
		mEditProvince = (EditText)findViewById(R.id.edit_province);
		mEditCity	= (EditText)findViewById(R.id.edit_city);
		mEditArea	= (EditText)findViewById(R.id.edit_area);
		mEditProvince.setOnClickListener(this);
		mEditCity.setOnClickListener(this);
		mEditArea.setOnClickListener(this);
		
		TextView title = (TextView)findViewById(R.id.title_bar_text);
		title.setText("详情资料");
		
		findViewById(R.id.title_bar_back).setOnClickListener(this);
		
		mImageBuySelect = (ImageView)findViewById(R.id.image_buy_select);
		mImageSaleSelect = (ImageView)findViewById(R.id.image_sale_select);
		mImageBuySelect.setOnClickListener(this);
		mImageSaleSelect.setOnClickListener(this);
		
		
		mImageUpload = (ImageView)findViewById(R.id.image_upload);
		mImageUpload.setOnClickListener(this);
		
		pens_layout_company = (RelativeLayout)findViewById(R.id.pens_layout_company);//公司 介绍
		pens_layout_company.setOnClickListener(this);
		pens_layout_business = (RelativeLayout)findViewById(R.id.pens_layout_business);
		pens_layout_business.setOnClickListener(this);
		
		if (ConfigUtil.mUserInfo != null)
		{
			UpdateUserInfo userInfo = ConfigUtil.mUserInfo;
			mEditName.setText(userInfo.getUserName());
			mEditCompany.setText(userInfo.getCompanyName());
			mEditAddress.setText(userInfo.getAddress());
			
			companyString = userInfo.getCompany_description();//公司介绍
			
			mUserType = userInfo.getUserType();
			if (mUserType.equals("1"))
			{
				mImageBuySelect.setImageResource(R.drawable.radio_selected);
				mImageSaleSelect.setImageResource(R.drawable.radio_unselect);	
			}
			else
			{
				mImageBuySelect.setImageResource(R.drawable.radio_unselect);
				mImageSaleSelect.setImageResource(R.drawable.radio_selected);
			}
		}
	}
	
	private void GetAddressCode()
	{
		mLayoutProgress.setVisibility(View.VISIBLE);
		BackendDataApi bdApi = ((WJApplication)getApplicationContext()).getHttpRequest();
		bdApi.GetAddressCode(reqSuccessListener(), reqErrorListener());

	}
	public Response.Listener<JSONObject> reqSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				//ToastMessage.show(getActivity(), response.toString());
				try {
					
					mAddressCode = AddressCode.parse(response);
					if (mAddressCode.addressList.size() > 0)
					{
						mProvinces = new String[mAddressCode.addressList.size()];
						for (int i=0; i<mAddressCode.addressList.size(); i++)
						{
							AddressItem item = mAddressCode.addressList.get(i);
							mProvinces[i] = item.name;
						}
						
						UpdateAddressData();
					}
					//adapterProvince.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mLayoutProgress.setVisibility(View.INVISIBLE);
			}
		};
	}

	public Response.ErrorListener reqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
//				String t = error.getMessage();
//				ToastMessage.show(PensonInfoActivity.this, t);
				mLayoutProgress.setVisibility(View.INVISIBLE);
			}
		};
	}

	private void UpdateAddressData()
	{
		for (int i=0; i<mAddressCode.addressList.size(); i++)
		{
			AddressItem item = mAddressCode.addressList.get(i);
			mProvinces[i] = item.name;
		}
		
		
		UpdateUserInfo userInfo = ConfigUtil.mUserInfo;
		if (userInfo != null)
		{
			String provinceCode = userInfo.getProvinceCode();
			String cityCode 	= userInfo.getCityCode();
			String areaCode 	= userInfo.getAreaCode();
			
			
			for (int i=0; i<mAddressCode.addressList.size(); i++)
			{
				AddressItem item = mAddressCode.addressList.get(i);
				if (provinceCode.equals(item.id))
				{
					mIndexProvince = i;
					mEditProvince.setText(item.name);
					break;
				}
			}
			
			AddressItem item = mAddressCode.addressList.get(mIndexProvince);
			for (int i=0; i<item.addressList.size(); i++)
			{
				AddressItem cityItem = item.addressList.get(i);
				if (cityCode.equals(cityItem.id))
				{
					mIndexCity = i;
					mEditCity.setText(cityItem.name);
					break;
				}
			}
			
			item = mAddressCode.addressList.get(mIndexProvince);
			item = item.addressList.get(mIndexCity);
			for (int i=0; i<item.addressList.size(); i++)
			{
				AddressItem areaItem = item.addressList.get(i);
				if (areaCode.equals(areaItem.id))
				{
					mIndexArea = i;
					mEditArea.setText(areaItem.name);
					break;
				}
			}
		}
		
	}
	
	private void CreateProvinceDialog()
	{
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
         LayoutInflater inflater = this.getLayoutInflater();
         View view = inflater.inflate(R.layout.dialog_selectlist, null);
         
         ListView listview = (ListView)view.findViewById(R.id.listview_select);
         String data[] = mProvinces;
         mAdapterProvince = new MyListAdapter(this, data);
 		 listview.setAdapter(mAdapterProvince);
 		 listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mIndexProvince = position;
				mEditProvince.setText((String)mAdapterProvince.getItem(position));
				mEditCity.setText("");
				mEditArea.setText("");
				mDialogProvince.cancel();
			}
 		});
         
         builder.setView(view);
         
         mDialogProvince = builder.create();
         //mDialogProvince.show();
	}
	private void ShowProvinceDialog()
	{
		if (mDialogProvince == null)
			CreateProvinceDialog();
		
        mDialogProvince.show();
	}
	
	private void CreateCityDialog()
	{
		AddressItem item = mAddressCode.addressList.get(mIndexProvince);
		if(item.addressList.size() < 0)
			return;

		String data[] = new String[item.addressList.size()];
		for (int i=0; i<item.addressList.size(); i++)
		{
			data[i] = item.addressList.get(i).name;
		}
		
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
         LayoutInflater inflater = this.getLayoutInflater();
         View view = inflater.inflate(R.layout.dialog_selectlist, null);
         
         ListView listview = (ListView)view.findViewById(R.id.listview_select);
         mAdapterCity = new MyListAdapter(this, data);
 		 listview.setAdapter(mAdapterCity);
 		 listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mIndexCity = position;
				mEditCity.setText((String)mAdapterCity.getItem(position));
				mEditArea.setText("");
				mDialogCity.cancel();
			}
 		});
         
         builder.setView(view);
         
         mDialogCity = builder.create();
         //mDialogCity.show();
	}
	private void ShowCityDialog()
	{
		if (mDialogCity == null)
			CreateCityDialog();
		
		mDialogCity.show();
	}
	
	private void CreateAreaDialog()
	{
		AddressItem item = mAddressCode.addressList.get(mIndexProvince);
		item = item.addressList.get(mIndexCity);
		if(item.addressList.size() < 0)
			return;

		String data[] = new String[item.addressList.size()];
		for (int i=0; i<item.addressList.size(); i++)
		{
			data[i] = item.addressList.get(i).name;
		}
		
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
         LayoutInflater inflater = this.getLayoutInflater();
         View view = inflater.inflate(R.layout.dialog_selectlist, null);
         
         ListView listview = (ListView)view.findViewById(R.id.listview_select);
         mAdapterArea = new MyListAdapter(this, data);
 		 listview.setAdapter(mAdapterArea);
 		 listview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mIndexArea = position;
				mEditArea.setText((String)mAdapterArea.getItem(position));
				mDialogArea.cancel();
			}
 		});
         
         builder.setView(view);
         
         mDialogArea = builder.create();
         //mDialogArea.show();
	}
	private void ShowAreaDialog()
	{
		if (mDialogArea == null)
			CreateAreaDialog();
		
		mDialogArea.show();
	}
	
	private class MyListAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		private String[] mData;
		public MyListAdapter(Context context, String[] data)
		{
			mInflater = LayoutInflater.from(context);
			mData = data;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (mData == null)
				return 0;
			return mData.length;
		}

		@Override
		public String getItem(int arg0) {
			// TODO Auto-generated method stub
			return mData[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			ListItemView  listItemView = null;
			if (convertView == null) {   
	            listItemView = new ListItemView();    
	            convertView = mInflater.inflate(R.layout.listitem_province, null);   
	             
	            listItemView.text = (TextView)convertView.findViewById(R.id.listitem_text);   
	           
	            convertView.setTag(listItemView);   
	        }else {   
	            listItemView = (ListItemView)convertView.getTag();   
	        }  
			listItemView.text.setText(mData[position]);
			return convertView;
		}
		
	}
	
	 private final class ListItemView{                //自定义控件集合     
         public TextView text;     
	 }  
	 
	 private void ModifyPersonInfo()
	 {
		 if(mEditProvince.getText().toString() == null || mEditProvince.getText().toString().trim().equals("")
					|| mEditCity.getText().toString() == null || mEditCity.getText().toString().trim().equals("")	
					|| mEditArea.getText().toString() == null || mEditArea.getText().toString().trim().equals("")	
					|| mEditName.getText().toString() == null || mEditName.getText().toString().trim().equals("")	
					|| mEditCompany.getText().toString() == null || mEditCompany.getText().toString().trim().equals("")	
					|| mEditAddress.getText().toString() == null || mEditAddress.getText().toString().trim().equals("")	)
		 {
			 ToastMessage.show(PensonInfoActivity.this, "请填写完整资料");
			 return;
		 }
 
		 String user_id	= ConfigUtil.mUserId;
		 String token	= ConfigUtil.mToken;
		 String user_name = mEditName.getText().toString();
		 String company_name  = mEditCompany.getText().toString();
		 
		 AddressItem itemProvince = mAddressCode.addressList.get(mIndexProvince);
		 AddressItem itemCity = itemProvince.addressList.get(mIndexCity);
		 AddressItem itemArea = itemCity.addressList.get(mIndexArea);
		 String province_code = itemProvince.id;
		 String city_code	  = itemCity.id;
		 String area_code	  = itemArea.id;
		 String address		  = mEditAddress.getText().toString();
		 String business_card = mEditIdCard.getText().toString();
		 String user_type 	  = mUserType;
		
		 
		 BackendDataApi bdApi = ((WJApplication)getApplicationContext()).getHttpRequest();
		 bdApi.updateInfo(user_id, token, user_name, company_name, province_code,
				 city_code, area_code, address, business_card, user_type, companyString , businessString ,
				 reqPersonSuccessListener(), reqPersonErrorListener());
	 }
	 

	public Response.Listener<JSONObject> reqPersonSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				ToastMessage.show(PensonInfoActivity.this, "修改个人资料成功");
				ConfigUtil.mUserName = mEditName.getText().toString();
				ConfigUtil.setLoginInfo(ConfigUtil.mToken, ConfigUtil.mUserId, 
						ConfigUtil.mUserName, ConfigUtil.mTokenExpire);

				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finish();
				
			}
		};
	}

	public Response.ErrorListener reqPersonErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				String t = error.getMessage();
				ToastMessage.show(PensonInfoActivity.this, t);
			}
		};
	}
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {

			switch (requestCode) {
			case ConfigUtil.PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的

				if (data == null || data.getData() == null) {
					ToastMessage.show(getApplicationContext(), "请尝试使用其他相册浏览!");
					return;
				}
				String selectedImagePath = null;
				String fullPath;
				Uri selectedImageUri = data.getData();
				if (selectedImageUri != null) {
					selectedImagePath = ImageUtils.getImagePath(selectedImageUri, this);
				}

				if (selectedImagePath != null) {
					fullPath = selectedImagePath;
				} else {
					fullPath = ConfigUtil.getPhotoPath();
				}
				Bitmap bitmap = ConfigUtil.getThumbnailBitmap(this, fullPath);
				if(bitmap != null){
					fullPath = ConfigUtil.getThumbFilePath();
				}else{
					return;
				}

				File file = new File(fullPath);
				UploadImage(file);

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
				
				File file = new File(fullPath);
				UploadImage(file);

				break;
			}
			
			case 10001 ://公司介绍返回
			    companyString = data.getStringExtra(ComPanyEditActivity.EXTRA_EDS);
			    break;
			case 10002 :
				businessString = data.getStringExtra(BusinessActivity.EXTRA_ID);
				break;
			
			}
			
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void UploadImage(File file)
	{
		mLayoutProgress.setVisibility(View.VISIBLE);
		
		 String type = "1";
		 BackendDataApi bdApi = ((WJApplication)getApplicationContext()).getHttpRequest();
		 bdApi.uploadImage(file, ConfigUtil.mUserId, ConfigUtil.mToken, type, reqUploadSuccessListener(), reqUploadErrorListener());
	}
	public Response.Listener<JSONObject> reqUploadSuccessListener() {
		return new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				mLayoutProgress.setVisibility(View.INVISIBLE);
				ToastMessage.show(PensonInfoActivity.this, "上传名片成功。");
				try {
					CardId cardId = CardId.parse(response);
					if(cardId.getError() != 0)
					{
						ToastMessage.show(PensonInfoActivity.this, "上传名片失败");
						return;
					}
					//mEditIdCard.setText(cardId.getPhotoId());
					mEditIdCard.setText("名片已上传");
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
				ToastMessage.show(PensonInfoActivity.this, "上传名片失败。");
			}
		};
	}
	
	
	public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
	}
	public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btn_personinfo_conform:
			ModifyPersonInfo();
			break;
			
		case R.id.edit_province:
			CreateProvinceDialog();
			ShowProvinceDialog();
			break;
		case R.id.edit_city:
			CreateCityDialog();
			ShowCityDialog();
			break;
		case R.id.edit_area:
			CreateAreaDialog();
			ShowAreaDialog();
			break;
			
		case R.id.title_bar_back:
			finish();
			break;
			
		case R.id.image_buy_select:
			mImageBuySelect.setImageResource(R.drawable.radio_selected);
			mImageSaleSelect.setImageResource(R.drawable.radio_unselect);
			mUserType = "1";
			break;
			
		case R.id.image_sale_select:
			mImageBuySelect.setImageResource(R.drawable.radio_unselect);
			mImageSaleSelect.setImageResource(R.drawable.radio_selected);
			mUserType = "2";
			break;	
			
		case R.id.image_upload:
			//SelectImage();
			ConfigUtil.doPickPhotoAction(this);
			break;
		case R.id.pens_layout_company :
		    Intent intent = new Intent(this , ComPanyEditActivity.class);
		    intent.putExtra(ComPanyEditActivity.EXTRA_EDS, companyString);
		    startActivityForResult(intent, 10001);
		    break;
		case R.id.pens_layout_business :
		    Intent bus = new Intent(this , BusinessActivity.class);
		    startActivityForResult(bus , 10002);
		    break;
		}
	}
	
}
