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
		public String getOrder_message_id() {
			return order_message_id;
		}
		public void setOrder_message_id(String order_message_id) {
			this.order_message_id = order_message_id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public ArrayList<PictureItem> getPicture_arr() {
			return picture_arr;
		}
		public void setPicture_arr(ArrayList<PictureItem> picture_arr) {
			this.picture_arr = picture_arr;
		}
		public ArrayList<PictureItem> getPayment_voucher_picture_arr() {
			return payment_voucher_picture_arr;
		}
		public void setPayment_voucher_picture_arr(
				ArrayList<PictureItem> payment_voucher_picture_arr) {
			this.payment_voucher_picture_arr = payment_voucher_picture_arr;
		}
		public String getShow_time() {
			return show_time;
		}
		public void setShow_time(String show_time) {
			this.show_time = show_time;
		}
		public String getOrder_status() {
			return order_status;
		}
		public void setOrder_status(String order_status) {
			this.order_status = order_status;
		}
		public String getOrder_status_content() {
			return order_status_content;
		}
		public void setOrder_status_content(String order_status_content) {
			this.order_status_content = order_status_content;
		}
		public String getDeal_price() {
			return deal_price;
		}
		public void setDeal_price(String deal_price) {
			this.deal_price = deal_price;
		}
		public String getSupplier_id() {
			return supplier_id;
		}
		public void setSupplier_id(String supplier_id) {
			this.supplier_id = supplier_id;
		}
		public String getOrder_id() {
			return order_id;
		}
		public void setOrder_id(String order_id) {
			this.order_id = order_id;
		}
		public String getCur_user_price() {
			return cur_user_price;
		}
		public void setCur_user_price(String cur_user_price) {
			this.cur_user_price = cur_user_price;
		}
		public String getPrice_count() {
			return price_count;
		}
		public void setPrice_count(String price_count) {
			this.price_count = price_count;
		}
		public int getMessage_count() {
			return message_count;
		}
		public void setMessage_count(int message_count) {
			this.message_count = message_count;
		}
		
		
}
