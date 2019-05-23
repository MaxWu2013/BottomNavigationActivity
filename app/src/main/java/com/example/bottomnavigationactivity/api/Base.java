package com.example.bottomnavigationactivity.api;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;


import com.example.bottomnavigationactivity.model.Result;
import com.example.bottomnavigationactivity.util.Commom;
import com.example.bottomnavigationactivity.util.Constant;


import android.content.Context;


public abstract class Base {
	
	public Context context;
	private static final String prefName = "apiCache";
	
	//session 
	public static String PHPSessionId = null;
	
	
	protected Base(Context context){
		this.context = context;
	}
	/**
	 * 根据json获取结果
	 * @param json json字符串
	 * @param isNeedJsonObject 是否需要JSONObject
	 *
	 * @return
	 */
	protected Result getResult(String json,boolean isNeedJsonObject){
		if(json == null){
			return new Result(false, "server error", Constant.ERROR_SERVER_ERROR);
		}
		if(json.equals(Commom.NO_NETWORK)){
			return new Result(false, "server error",Constant.ERROR_NO_NETWORK);
		}
		if(json.equals(Commom.TIMEOUT)){
			return new Result(false, "server error",Constant.ERROR_TIMEOUT);
		}
		Result result = new Result();
		try {
			JSONObject jo = Commom.parseJsonString(json);
			if(isNeedJsonObject)result.setJsonObject(jo);
			if(jo.has(Constant.RESPONSE_PARAM_IS_SUCCESS)){
				result.setResultCode(Constant.RESULT_CODE_SUCCESS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if(result.getResultCode() == Constant.RESULT_CODE_SUCCESS){
			result.setSucc(true);
		}else{
			result.setSucc(false);
		}
		if(result.getResultCode() == 0){
			result.setMsg("sessection time out");
			PHPSessionId = null;
		}
		return result;
	}
	
	/**
	 * 根据json获取结果 不返回JSONObject
	 * @param json json字符串
	 * @return
	 */
	protected Result getResult(String json){
		return getResult(json,false);
	}
	
	protected String getString(int id){
		return context.getString(id);
	}
	
	/**
	 * 需要session的Get请求
	 */
	public  String httpGetNeedSession(String url,
			Map<String, String> param,Context con) {
		if (PHPSessionId != null) {
			if (param == null) {
				param = new HashMap<String, String>();
			}
			param.put("session", PHPSessionId);
		}
		return httpGet(url, param);
	}
	
	/**
	 * 需要session的POST请求
	 */
	public  String httpPostNeedSession(String url,
			Map<String, String> param,Context con) {
		if (PHPSessionId != null) {
			if (param == null) {
				param = new HashMap<String, String>();
			}
			param.put("session", PHPSessionId);
		}
		return httpPost(url, param);
	}
	
	public String httpGet(String url, Map<String, String> param ) {
		return Commom.httpGet(url, param, context);
	}
	public String httpPost(String url, Map<String, String> param) {
		return Commom.httpPost(url, param,context);
	}
	
}
