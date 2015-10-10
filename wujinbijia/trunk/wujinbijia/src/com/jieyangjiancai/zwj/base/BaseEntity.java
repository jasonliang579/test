package com.jieyangjiancai.zwj.base;

import java.io.Serializable;



public class BaseEntity implements Serializable{

	private int error = 0;//默认0 成功, 1 token 错误, 2 token 过期.
	private String errortext;
	
	public void setError(int error){
		this.error = error;
	}
	
	public int getError(){
		return this.error;
	}
	
	public void setErrorText(String t){
		errortext = t;
	}
	
	public String getErrorText(){
		return errortext;
	}
	
	
}
