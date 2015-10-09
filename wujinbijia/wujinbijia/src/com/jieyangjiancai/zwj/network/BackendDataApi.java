package com.jieyangjiancai.zwj.network;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jieyangjiancai.zwj.common.XGPushRegister;

public class BackendDataApi {
	private static HttpRequestUtil mHttpRequestUtil;
	private Context mContext;

	public BackendDataApi(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		if (mHttpRequestUtil == null) {
			mHttpRequestUtil = new HttpRequestUtil(context);
		}
	}

	public void terminate() {
		if (mHttpRequestUtil != null) {
			mHttpRequestUtil.terminate();
		}

	}

	/**
	 * 检查更新
	 * 
	 * @param phone
	 * @param listener
	 * @param errorListener
	 */
	public static void checkUpdate(String cur_version, Response.Listener<JSONObject> listener, ErrorListener errorListener) {

		String url = URLs.APP_UPDATE;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("device_type", "1");
		builder.appendQueryParameter("cur_version", cur_version);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}
	
	/**
	 * 登录验证码 暂时使用返回数据,后面是使用手机短信.
	 * 
	 * @param phone
	 * @param listener
	 * @param errorListener
	 */
	public static void loginVcode(String phone, Response.Listener<JSONObject> listener, ErrorListener errorListener) {

		String url = URLs.VERIFY_CODE;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("phone", phone);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 登录/注册
	 * 
	 * @param phone
	 * @param vcode
	 * @param ip
	 * @param listener
	 * @param errorListener
	 */
	public static void login(String phone, String vcode, String ip,String pushtoken, Response.Listener<JSONObject> listener, ErrorListener errorListener) {

		String url = URLs.USER_LOGIN;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("phone", phone);
		builder.appendQueryParameter("vcode", vcode);
		builder.appendQueryParameter("ip", ip);
		builder.appendQueryParameter("device_token", pushtoken);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 增加/修改用户信息
	 * 
	 * @param user_id
	 *            用户ID
	 * @param token
	 *            token
	 * @param user_name
	 *            姓名
	 * @param company_name
	 *            公司
	 * @param province_code
	 *            省代码
	 * @param city_code
	 *            市代码
	 * @param area_code
	 *            区代码
	 * @param address
	 *            公地址
	 * @param business_card
	 *            名片
	 * @param listener
	 * @param errorListener
	 */
	public static void updateInfo(String user_id, String token, String user_name, String company_name, String province_code,
			String city_code, String area_code, String address, String business_card, String user_type,
			Response.Listener<JSONObject> listener, ErrorListener errorListener) {

		String url = URLs.USER_UPDATE_INFO;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("user_name", user_name);
		builder.appendQueryParameter("company_name", company_name);
		builder.appendQueryParameter("province_code", province_code);
		builder.appendQueryParameter("city_code", city_code);
		builder.appendQueryParameter("area_code", area_code);
		builder.appendQueryParameter("address", address);
		builder.appendQueryParameter("business_card", business_card);
		builder.appendQueryParameter("user_type", user_type);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 获取回赠金额
	 * 
	 * @param user_id
	 * @param token
	 * @param listener
	 * @param errorListener
	 */
	public static void fetchRebateAccount(String user_id, String token, Response.Listener<JSONObject> listener, ErrorListener errorListener) {

		String url = URLs.FETCH_REBATE_AMOUNT;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}
	//获取优惠金额获取记录
	public static void fetchRebateList(String user_id, String token, Response.Listener<JSONObject> listener, ErrorListener errorListener) {

		String url = URLs.FETCH_REBATE_LIST;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 根据分类关联ID获取品牌价格列表
	 * 
	 * @param class_id
	 * @param listener
	 * @param errorListener
	 */
	public static void kindsVersion(String class_id, Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.KINDS_VERSION;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("class_id", class_id);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 获取品牌和分类所有数据
	 * 
	 * @param listener
	 * @param errorListener
	 */
	public static void kindsAll(Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.KINDS_ALL;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		// builder.appendQueryParameter("class_id", class_id);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 根据分类关联ID获取品牌价格列表
	 * 
	 * @param cmcRelId
	 * @param listener
	 * @param errorListener
	 */
	public static void brandPrice(String cmcRelId, Response.Listener<JSONArray> listener, ErrorListener errorListener) {
		String url = URLs.CALCULATE_PRICE;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("cmcRelId", cmcRelId);
		mHttpRequestUtil.HttpRequest2(builder, listener, errorListener);
		Log.d("wujin", "url = " + builder.toString());
	}

	public static void switchPrice(String productId, String modeltypeId, Response.Listener<JSONArray> listener, ErrorListener errorListener) {
		String url = URLs.PRICE_SWITCH;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("kindId", "1");
		builder.appendQueryParameter("productId", productId);
		builder.appendQueryParameter("modeltypeId", modeltypeId);
		mHttpRequestUtil.HttpRequest2(builder, listener, errorListener);
		Log.d("wujin", "url = " + builder.toString());
	}

	/**
	 * 获取所有品牌类别接口
	 * 
	 * @param kindId
	 * @param listener
	 * @param errorListener
	 */
	public static void kaiguanKindsAllData(String kindId, Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.KINDS_ALL_DATA;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("kindId", kindId);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 获取价格
	 * 
	 * @param kindId
	 * @param productId
	 * @param modeltypeId
	 * @param listener
	 * @param errorListener
	 */
	public static void kindPrice(String kindId, String productId, String modeltypeId, Response.Listener<JSONObject> listener,
			ErrorListener errorListener) {
		String url = URLs.KIND_PRICE;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("kindId", kindId);
		builder.appendQueryParameter("productId", productId);
		builder.appendQueryParameter("modeltypeId", modeltypeId);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 查看具体某个商品属性
	 * 
	 * @param user_id
	 * @param token
	 * @param brand_id
	 * @param product_id
	 * @param listener
	 * @param errorListener
	 */
	public static void productProperty(String user_id, String token, String brand_id, String product_id,
			Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.PRODUCT_PROPERTY;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("brand_id", brand_id);
		builder.appendQueryParameter("product_id", product_id);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 添加商品到购物车
	 * 
	 * @param user_id
	 * @param token
	 * @param brand_id
	 *            品牌ID
	 * @param product_id
	 *            具体商品ID
	 * @param color_id
	 *            颜色ID
	 * @param property_id_arr
	 *            属性ID数组
	 * @param amount
	 *            数量
	 * @param listener
	 * @param errorListener
	 */
	public static void putCart(String user_id, String token, String brand_id, String product_id, String color_id, String property_id_arr,
			String amount, Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.PUT_CART;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("brand_id", brand_id);
		builder.appendQueryParameter("product_id", product_id);
		builder.appendQueryParameter("color_id", color_id);
		builder.appendQueryParameter("property_id_arr", property_id_arr);
		builder.appendQueryParameter("amount", amount);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 获取购物车列表信息
	 * 
	 * @param user_id
	 * @param token
	 * @param listener
	 * @param errorListener
	 */
	public static void queryCart(String user_id, String token, Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.QUERY_CART;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 删除购物车中的某件商品
	 * 
	 * @param id
	 *            商品在购物车中的ID
	 * @param user_id
	 * @param token
	 * @param listener
	 * @param errorListener
	 */
	public static void deleteCartItem(String id, String user_id, String token, Response.Listener<JSONObject> listener,
			ErrorListener errorListener) {
		String url = URLs.DELETE_ITEM;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("id", id);
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 修改购物车中具体某商品的数量
	 * 
	 * @param id
	 *            商品在购物车的id
	 * @param user_id
	 *            用户ID
	 * @param token
	 * @param amount
	 *            商品数量
	 * @param listener
	 * @param errorListener
	 */
	public static void deleteCartItem(String id, String user_id, String token, String amount, Response.Listener<JSONObject> listener,
			ErrorListener errorListener) {
		String url = URLs.MODIFY_ITEM;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("id", id);
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("amount", amount);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 
	 * 把多个商品生成订单
	 * 
	 * @param id
	 * @param user_id
	 * @param token
	 * @param amount
	 * @param listener
	 * @param errorListener
	 */
	public static void putOrder(String user_id, String token, String order_discount, String data, Response.Listener<JSONObject> listener,
			ErrorListener errorListener) {
		String url = URLs.PUT_ORDER;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("order_discount", order_discount);
		builder.appendQueryParameter("data", data);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 根据订单信息生成支付ID（主要用于第三方支付，支付完成后会回调后台）
	 * 
	 * @param user_id
	 * @param token
	 * @param order_id
	 *            订单id
	 * @param total_money
	 *            总金额
	 * @param cashback_money
	 *            返现金额
	 * @param listener
	 * @param errorListener
	 */
	public static void payOrder(String user_id, String token, String order_id, String total_money, String cashback_money,
			Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.PAY_ORDER;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("order_id", order_id);
		builder.appendQueryParameter("total_money", total_money);
		builder.appendQueryParameter("cashback_money", cashback_money);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 获取订单列表信息（带条件查询，未支付，已支付，所有订单）
	 * 
	 * @param user_id
	 * @param token
	 * @param pay_status
	 * @param listener
	 * @param errorListener
	 */
	public static void queryOrder(String user_id, String token, String pay_status, Response.Listener<JSONObject> listener,
			ErrorListener errorListener) {
		String url = URLs.QUERY_ORDER;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("pay_status", pay_status);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 获取具体订单信息
	 * 
	 * @param user_id
	 * @param token
	 * @param order_id
	 * @param listener
	 * @param errorListener
	 */
	public static void showOrder(String user_id, String token, String order_id, Response.Listener<JSONObject> listener,
			ErrorListener errorListener) {
		String url = URLs.SHOW_ORDER;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("order_id", order_id);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 订单取消（未支付的允许取消）
	 * 
	 * @param user_id
	 * @param token
	 * @param order_id
	 * @param listener
	 * @param errorListener
	 */
	public static void deleteOrder(String user_id, String token, String order_id, Response.Listener<JSONObject> listener,
			ErrorListener errorListener) {
		String url = URLs.DELETE_ORDER;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("order_id", order_id);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 获取收货地址列表
	 * 
	 * @param user_id
	 * @param token
	 * @param order_id
	 * @param listener
	 * @param errorListener
	 */
	public static void queryAddressList(String user_id, String token, Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.QUERY_ADDRESS_LIST;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 增加收货地址，包括收货地址，收货人姓名，联系电话
	 * 
	 * @param user_id
	 * @param token
	 * @param receive_address
	 * @param consignee_name
	 * @param consignee_phone
	 * @param listener
	 * @param errorListener
	 */
	public static void addAddress(String user_id, String token, String receive_address, String consignee_name, String consignee_phone,
			Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.ADD_ADDRESS;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("receive_address", receive_address);
		builder.appendQueryParameter("consignee_name", consignee_name);
		builder.appendQueryParameter("consignee_phone", consignee_phone);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	/**
	 * 删除收货地址
	 * 
	 * @param user_id
	 * @param token
	 * @param address_id
	 * @param listener
	 * @param errorListener
	 */
	public static void deleteAddress(String user_id, String token, String address_id, Response.Listener<JSONObject> listener,
			ErrorListener errorListener) {
		String url = URLs.DELETE_ADDRESS;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("address_id", address_id);

		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	// 获取开关电器数据
	public static void fetchSwitch(Response.Listener<JSONArray> listener, ErrorListener errorListener) {
		String url = URLs.FETCH_SWITCH;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("kindId", "1");
		mHttpRequestUtil.HttpRequest2(builder, listener, errorListener);
	}

	/**
	 * 用户信息
	 * 
	 * @param userId
	 * @param token
	 * @param listener
	 * @param errorListener
	 */
	public static void userInfo(String userId, String token, Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.USER_INFO;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", userId);
		builder.appendQueryParameter("token", token);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}

	/**
	 * 上传文件
	 * 
	 * @param order_id
	 * @param token
	 */
	public void uploadFile(File file, String token, Response.Listener<JSONObject> listener, ErrorListener errorListener) {

		mHttpRequestUtil.uploadFile(file, token, listener, errorListener);
	}

	public void uploadImage(File file, String userId, String token, String type, Response.Listener<JSONObject> listener,
			ErrorListener errorListener) {

		mHttpRequestUtil.uploadImage(file, userId, token, type, listener, errorListener);
	}

	/**
	 * 下载图片
	 * 
	 * @param url
	 * @param listener
	 */
	public void ImageLoad(String url, ImageListener listener) {

		mHttpRequestUtil.ImageLoad(url, listener);
	}

	// 拍照下单
	public static void photoOrder(String user_id, String token, String photo_arr, 
			String remark, Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.PHOTO_ORDER;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("picture_arr", photo_arr);
		builder.appendQueryParameter("remark", remark);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}


	// 获取省份/城市/地区信息
	public static void GetAddressCode(Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.GET_ADDRESS_CODE;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);

	}

	//1. 获取询单消息（接单，只能看别人发的询单）
	public static void GetOrder(String user_id, String token, String page_size,String final_id, Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.Get_ORDER;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("page_size", page_size);
		builder.appendQueryParameter("final_id", final_id);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}
		
	//2.询单报价（我要接单）
	public static void MakeMyOrder(String user_id, String token, String price, String remark, 
			String user_name, String order_message_id, String picture_arr, String deliver_place,
			Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.MAKE_MY_ORDER;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("price", price);
		builder.appendQueryParameter("remark", remark);
		builder.appendQueryParameter("user_name", user_name);
		builder.appendQueryParameter("order_message_id", order_message_id);
		builder.appendQueryParameter("picture_arr", picture_arr);
		builder.appendQueryParameter("deliver_place", deliver_place);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}
	
	//3、获取我的询单报价（接单）
	public static void GetMyPrice(String user_id, String token, String page_size,String final_id, 
			Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.GET_My_PRICE_ORDER;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("page_size", page_size);
		builder.appendQueryParameter("final_id", final_id);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}
	
	//5、获取当前用户询单记录（包括接单数量，订单状态，备注信息），如果确认付款好，显示上传凭证。
	public static void FetchOrderList(String user_id, String token, String page_size,String final_id, 
			Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.FETCH_ORDER_LIST;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("page_size", page_size);
		builder.appendQueryParameter("final_id", final_id);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}
	
	//4、获取单个询单消息的报价列表
	public static void FetchOrderSupplierList(String user_id, String token, String order_message_id, 
			Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.FETCH_ORDER_SUPPLIER_LIST;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("order_message_id", order_message_id);
		//builder.appendQueryParameter("final_id", final_id);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}
	
	//6、用户选择供应商
	public static void SelectSupplier(String user_id, String token, String order_message_id,  String supplier_id,
			Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.SELECT_SUPPLIER;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("order_message_id", order_message_id);
		builder.appendQueryParameter("supplier_id", supplier_id);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}
	
	//确认收货
	public static void confirmReceipt(String user_id, String token, String order_message_id,
			Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.CONFIRM_RECEIPT;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("order_message_id", order_message_id);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}
	
	//上传支付凭证
	public static void UploadPayPhote(String user_id, String token, String picture_arr, String order_message_id, 
			Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.UPLOAD_PAY_PHOTE;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("picture_arr", picture_arr);
		builder.appendQueryParameter("order_message_id", order_message_id);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}
	
	//7、用户查看供应商信息
	public static void GetSupplierInfo(String user_id, String token, String order_message_id,  String supplier_id,
			Response.Listener<JSONObject> listener, ErrorListener errorListener)
	{
		String url = URLs.GET_SUPPLIER_INFO;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("order_message_id", order_message_id);
		builder.appendQueryParameter("supplier_id", supplier_id);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}
	
	public static void Logout(String user_id, String token, 
			Response.Listener<JSONObject> listener, ErrorListener errorListener)
	{
		String url = URLs.LOGOUT;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}
	
	public static void SaveIndustryMessage(String user_id, String token, 
			String content, String picture_arr, 
			Response.Listener<JSONObject> listener, ErrorListener errorListener)
	{
		String url = URLs.INDUSTRY_MESSAGE_SAVE;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("content", content);
		builder.appendQueryParameter("picture_arr", picture_arr);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}
	
	public static void FetchIndustryMessage(String page_size, String final_id, 
			Response.Listener<JSONObject> listener, ErrorListener errorListener)
	{
		String url = URLs.INDUSTRY_MESSAGE_FETCH;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("page_size", page_size);
		builder.appendQueryParameter("final_id", final_id);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}
	
	public static void FetchKinds(
			Response.Listener<JSONObject> listener, ErrorListener errorListener)
	{
		String url = URLs.FETCH_KINDS;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}
	
	public static void FetchSingleOrderMessage(String user_id, String token, 
			String order_message_id, String intf_type, 
			Response.Listener<JSONObject> listener, ErrorListener errorListener)
	{
		String url = URLs.FETCH_SINGLE_ORDER_MESSAGE;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("order_message_id", order_message_id);
		builder.appendQueryParameter("intf_type", intf_type);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}
	public static void AddMessage(String user_id, String token, 
			String order_message_id, String message_content, String reply_message_id,
			Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.ADD_MESSAGE;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("order_message_id", order_message_id);
		builder.appendQueryParameter("message_content", message_content);
		builder.appendQueryParameter("reply_message_id", reply_message_id);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}
	public static void GetMessageList(String user_id, String token, 
			String page_size, String order_message_id, String final_id, 
			Response.Listener<JSONObject> listener, ErrorListener errorListener) {
		String url = URLs.GET_MESSAGE_LIST;
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("user_id", user_id);
		builder.appendQueryParameter("token", token);
		builder.appendQueryParameter("page_size", page_size);
		builder.appendQueryParameter("order_message_id", order_message_id);
		builder.appendQueryParameter("final_id", final_id);
		mHttpRequestUtil.HttpRequest(builder, listener, errorListener);
	}
	
	
}
