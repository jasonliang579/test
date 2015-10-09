package com.jieyangjiancai.zwj.network.entity;

public class RebateItem {
	private String amount;
	private String order_id;
	private String create_time;

	public void setAmount(String str) {
		this.amount = str;
	}

	public String getAmount() {
		return this.amount;
	}

	public void setOrderId(String str) {
		this.order_id = str;
	}

	public String getOrderId() {
		return this.order_id;
	}

	public void setCreateTime(String str) {
		this.create_time = str;
	}

	public String getCreateTime() {
		return this.create_time;
	}
}
