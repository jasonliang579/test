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
}
