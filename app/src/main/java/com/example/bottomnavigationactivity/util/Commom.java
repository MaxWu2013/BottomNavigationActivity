package com.example.bottomnavigationactivity.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.graphics.Bitmap.CompressFormat;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class Commom {
	private static final String TAG = "commom";

	private static final String URL_PREFIX = "https://data.gov.sg";
	
	public static final String NO_NETWORK = "network error";
	public static final String TIMEOUT = "network timeout";

	// 请求的超时时间
	private static final int TIME_OUT = 30000; 

	/**
	 * Get请求
	 * 
	 * @param url
	 *            请求的路径
	 * @return
	 */
	public static String httpGet(String url,Map<String, String> param ,Context con) {
		if(!isNetworkAvailable(con)){
			return NO_NETWORK;
		}
		if(param == null){
			param = new HashMap<String,String>();
		}

		String paramStr = "";
		Iterator<Entry<String,String>> iter = param.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String,String> en = iter.next();
			paramStr +=(en.getKey()+"="+en.getValue()+"&");
		}
		if(paramStr.equals("")){
			url = URL_PREFIX+url;
		}else{
			paramStr = paramStr.substring(0,paramStr.length()-1);
			url = URL_PREFIX+url+"?"+paramStr;
		}
//		Log.w("httpGet","url:"+url);
		OkHttpClient client = new OkHttpClient();
		Request.Builder builder = new Request.Builder();
		builder.url(url);
		Request request = builder.build();
		try {
			Response response = client.newCall(request).execute();
			return response.body().string();
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}



	/**
	 * POST请求
	 * 
	 * @param url
	 *            请求的路径
	 * @param param
	 *            请求的参数
	 * @return
	 */
	public static String httpPost(String url, Map<String, String> param,Context con) {
		if(!isNetworkAvailable(con)){
			return NO_NETWORK;
		}
		OkHttpClient client = new OkHttpClient();
		if (param == null) {
			param = new HashMap<String, String>();
		}
		FormBody.Builder formBodyBuilder = new FormBody.Builder();
		Iterator<Entry<String,String>> iter = param.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String,String> en = iter.next();
			formBodyBuilder.add(en.getKey(),en.getValue());
		}

		FormBody formBody = formBodyBuilder.build();

		//Create a http request object.
		Request.Builder builder = new Request.Builder();
		builder = builder.url(url);
		builder = builder.post(formBody);
		Request request = builder.build();

		try {
			Response response = client.newCall(request).execute();
			return response.body().string();
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;

	}


	/**
	 * 解析json字符串
	 * 
	 * @throws JSONException
	 */
	public static JSONObject parseJsonString(String json) throws JSONException {
		JSONTokener jsonTokener = new JSONTokener(json);
		JSONObject jObject = (JSONObject) jsonTokener.nextValue();
		return jObject;
	}
	/**
	 * 检查网络
	 */
	public static boolean isNetworkAvailable(Context context) {
		final ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
			return false;
		}
		return true;
	}

}
