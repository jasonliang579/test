package com.jieyangjiancai.zwj.data;

import java.util.ArrayList;

public class IndustryMessageItem {
	public String industry_id;
	public String content;
	public String create_time; 	
	public ArrayList<PictureItem> picture_arr = new ArrayList<PictureItem>(); 
	public UserItem userInfo;
}
