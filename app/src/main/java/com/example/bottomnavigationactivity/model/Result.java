package com.example.bottomnavigationactivity.model;

import org.json.JSONObject;

public class Result {
	
	/** 请求连接id唯一标识 **/
	private int type;

	/** 请求参数 **/
	private Object[] params;

	/**
	 * 服务器响应的信息
	 */
	private String msg;
	/**
	 * 请求是否成功
	 */
	private boolean isSucc;
	/**
	 * 返回码
	 */
	private int resultCode;
	/**
	 * json对象
	 */
	private JSONObject jsonObject;
	/**
	 * 返回的数据
	 */
	private Object data;
	/**
	 * 总页数
	 */
	private int pageSize;
	
	public Result(){}
	
	public Result(boolean isSucc,String msg){
		this.isSucc = isSucc;
		this.msg = msg;
	}
	public Result(boolean isSucc,String msg,int resultCode){
		this.isSucc = isSucc;
		this.msg = msg;
		this.resultCode = resultCode;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public boolean isSucc() {
		return isSucc;
	}
	public void setSucc(boolean isSucc) {
		this.isSucc = isSucc;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
