package com.jieyangjiancai.zwj.network;

import java.io.Serializable;

/**
 * 服务器URL 接口
 * 
 * @author hlai
 * 
 */
public class URLs implements Serializable {

	/***************************************
	 * 后台api
	 ***************************************/

	public final static String HOST_DEV = "120.24.94.45:8110/jieyangEB";//开发版 
	public final static String HOST_WEB_DEV = "120.24.94.45:8110/interact";//开发版 
	
	public final static String HOST_RELEASE = "release.wujinbijia.com";//正式版
	public final static String HOST_WEB_RELEASE = "release.interact.wujinbijia.com";//正式版
		
	public final static String HTTP = "http://";
	public final static String HTTPS = "https://";
	public final static String URL_SPLITTER = "/";
	 
	public final static String URL_API_HOST = HTTP + HOST_RELEASE + URL_SPLITTER;
	public final static String URL_API_HOST_WEB = HTTP + HOST_WEB_RELEASE + URL_SPLITTER;

	// tasker action
	public final static String APP_UPDATE = URL_API_HOST + "/intf/appVersion!checkNeedUpdate.action";
	public final static String VERIFY_CODE = URL_API_HOST + "intf/verifyCode!makeVerifyCode.action";
	public final static String USER_LOGIN = URL_API_HOST + "intf/userlogin!login.action";
	public final static String USER_UPDATE_INFO = URL_API_HOST + "intf/updateUserInfo!update.action";
	public final static String FETCH_REBATE_AMOUNT = URL_API_HOST + "intf/rebate!fetchRebateAmount.action";
	public final static String KINDS_VERSION = URL_API_HOST + "intf/kindsVersion!fetchVersion.action";
	public final static String KINDS_ALL = URL_API_HOST + "site/mobileAppIntf!fetchAllData.action";
	public final static String CALCULATE_PRICE = URL_API_HOST + "site/queryPrice!calculatePrice.action";
	public final static String KINDS_ALL_DATA = URL_API_HOST + "intf/kindsProduct!getKindsAllData.action";
	public final static String KIND_PRICE = URL_API_HOST + "intf/kindsProduct!getKindsPrice.action";
	public final static String PRODUCT_PROPERTY = URL_API_HOST + "intf/productProperty!fetchProductProperty.action";
	public final static String PUT_CART = URL_API_HOST + "intf/cart!putCart.action";
	public final static String QUERY_CART = URL_API_HOST + "intf/cart!queryCart.action";
	public final static String DELETE_ITEM = URL_API_HOST + "intf/cart!deleteItem.action";
	public final static String MODIFY_ITEM = URL_API_HOST + "intf/cart!/intf/cart!modifyItem.action.action";
	public final static String PUT_ORDER = URL_API_HOST + "intf/order!putOrder.action";
	public final static String PAY_ORDER = URL_API_HOST + "intf/payment!payOrder.action";
	public final static String QUERY_ORDER = URL_API_HOST + "intf/order!queryOrder.action";
	public final static String SHOW_ORDER = URL_API_HOST + "intf/order!showOrder.action";
	public final static String DELETE_ORDER = URL_API_HOST + "intf/order!deleteOrder.action";
	public final static String QUERY_ADDRESS_LIST = URL_API_HOST + "intf/distribution!queryAddressList.action";
	public final static String ADD_ADDRESS = URL_API_HOST + "intf/distribution!addAddress.action";
	public final static String DELETE_ADDRESS = URL_API_HOST + "intf/distribution!deleteAddress.action";

	public final static String FETCH_SWITCH = URL_API_HOST + "intf/kindsProduct!fetchKindsAllData.action";
	public final static String PRICE_SWITCH = URL_API_HOST + "intf/kindsProduct!fetchKindsPrice.action";

	public final static String USER_INFO = URL_API_HOST + "intf/getUserInfo!fetchUserInfo.action";

	public final static String PHOTO_ORDER = URL_API_HOST + "intf/order!uploadPhotoOrder.action";

	public final static String GET_ADDRESS_CODE = URL_API_HOST + "site/district!fetchAllDistrictIntf.action";
	public final static String UPLOAD_IMAGE = URL_API_HOST + "intf/fileupload!upload.action";
	
	public final static String Get_ORDER = URL_API_HOST + "intf/orderMessage!fetchOrderMessageList.action";
	public final static String MAKE_MY_ORDER = URL_API_HOST + "intf/orderMessage!savePrice.action";
	public final static String GET_My_PRICE_ORDER = URL_API_HOST + "intf/orderMessage!fetchMyPriceList.action";
	public final static String FETCH_ORDER_LIST = URL_API_HOST + "intf/orderMessage!fetchOrderMessageListByUserId.action";
	public final static String FETCH_ORDER_SUPPLIER_LIST = URL_API_HOST + "intf/orderMessage!fetchPriceListByOrderMessageId.action";
	public final static String SELECT_SUPPLIER = URL_API_HOST + "intf/orderMessage!selectSupplier.action";
	public final static String UPLOAD_PAY_PHOTE = URL_API_HOST + "intf/payment!uploadPaymentVoucher.action";
	public final static String GET_SUPPLIER_INFO = URL_API_HOST + "intf/orderMessage!fetchSupplierInfo.action";
	public final static String LOGOUT = URL_API_HOST + "intf/userlogin!logout.action";
	public final static String FETCH_REBATE_LIST = URL_API_HOST + "intf/rebate!fetchRebateList.action";
	public final static String INDUSTRY_MESSAGE_SAVE = URL_API_HOST + "intf/industryMessage!save.action";
	public final static String INDUSTRY_MESSAGE_FETCH = URL_API_HOST + "intf/industryMessage!fetchIndustryMessage.action";
	public final static String FETCH_KINDS = URL_API_HOST + "intf/kindsItem!fetchKinds.action";
	public final static String CONFIRM_RECEIPT = URL_API_HOST + "intf/orderMessage!confirmReceipt.action";
	public final static String FETCH_SINGLE_ORDER_MESSAGE = URL_API_HOST + "intf/orderMessage!fetchSingleOrderMessage.action";
	public final static String ADD_MESSAGE = URL_API_HOST + "intf/message!saveMessage.action";
	public final static String GET_MESSAGE_LIST = URL_API_HOST + "intf/message!fetchMessageListByOrderMessageId.action";

	
	//web
	public final static String WEB_SHARE = URL_API_HOST_WEB + "app/share.jsp";
	public final static String WEB_ABOUT = URL_API_HOST_WEB + "help/about.jsp";
	public final static String WEB_BAND_ACCOUNT = URL_API_HOST_WEB + "bankAccount/listBankAccount.action";
	public final static String WEB_AGREEMENT = URL_API_HOST_WEB + "help/agreement.jsp";
	public final static String WEB_WARRANTY = URL_API_HOST_WEB + "help/warranty.jsp";
	public final static String WEB_SHARE_LOCAL = URL_API_HOST_WEB + "app/giftpack_app.jsp";
	public final static String WEB_HELP = URL_API_HOST_WEB + "help/orderMessage.jsp";
	public final static String WEB_GET_ORDER_HELP = URL_API_HOST_WEB + "help/price.jsp";

	// 每次请求个数
	public final static String REQUEST_PAGE_COUNT = "5";



	
	
	
	

}
