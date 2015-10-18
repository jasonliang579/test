package com.jieyangjiancai.zwj.data;

import java.util.ArrayList;

public class MyPriceItem {
	public OrderItem order_message;
	public String deliver_place;
	public String order_message_price_id; 	
	public ArrayList<PictureItem> picture_arr = new ArrayList<PictureItem>(); //图片列表
	public String price;  
	public String price_time;  
	public String remark;  
	public SupplierItem supplier;
	public CustomerItem customer;
	public OrderItem getOrder_message() {
		return order_message;
	}
	public void setOrder_message(OrderItem order_message) {
		this.order_message = order_message;
	}
	public String getDeliver_place() {
		return deliver_place;
	}
	public void setDeliver_place(String deliver_place) {
		this.deliver_place = deliver_place;
	}
	public String getOrder_message_price_id() {
		return order_message_price_id;
	}
	public void setOrder_message_price_id(String order_message_price_id) {
		this.order_message_price_id = order_message_price_id;
	}
	public ArrayList<PictureItem> getPicture_arr() {
		return picture_arr;
	}
	public void setPicture_arr(ArrayList<PictureItem> picture_arr) {
		this.picture_arr = picture_arr;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPrice_time() {
		return price_time;
	}
	public void setPrice_time(String price_time) {
		this.price_time = price_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public SupplierItem getSupplier() {
		return supplier;
	}
	public void setSupplier(SupplierItem supplier) {
		this.supplier = supplier;
	}
	public CustomerItem getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerItem customer) {
		this.customer = customer;
	}
	
	
}
