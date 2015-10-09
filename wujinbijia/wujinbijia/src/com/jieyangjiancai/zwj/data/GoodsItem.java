package com.jieyangjiancai.zwj.data;

public class GoodsItem {
	public String mBrand; //品牌
	public String mType; //分类
	public String mStandard; //规格
	public String mArea; //截面积
	
	public String mMoney;
	public String mCount;
	
	public GoodsItem(String brand, String type, String standard, String area, String money, String count)
	{
		mBrand = brand;
		mType = type;
		mStandard = standard;
		mArea = area;
		
		mMoney = money;
		mCount = count;
	}
}
