package com.jieyangjiancai.zwj.data;

import java.util.ArrayList;

public class OrderItem {
		public String order_message_id; //订单消息Id
		public String title; 	//标题
		public String content;  //手机端询单备注
		public ArrayList<PictureItem> picture_arr = new ArrayList<PictureItem>(); //图片列表
		public ArrayList<PictureItem> payment_voucher_picture_arr = new ArrayList<PictureItem>(); 
		public String show_time;//显示时间
		public String order_status; //订单状态
		public String order_status_content; //订单状态显示内容
		public String deal_price; //成交价格
		public String supplier_id; //被选中报价的供应商ID
		
		//下面文档没有，但数据有返回
		public String order_id;
		public String cur_user_price;
		public String price_count;
		public int message_count;
}
