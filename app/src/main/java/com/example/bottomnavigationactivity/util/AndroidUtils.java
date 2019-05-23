package com.example.bottomnavigationactivity.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;



public class AndroidUtils {
	
	private static DisplayMetrics mDisplayMetrics;

	/**
	 * 弹出dialog提示框
	 * 
	 * @param context
	 * @param message 弹出的消息
	 */
	public static ProgressDialog showDialog(Context context,String message){
		ProgressDialog dialog = ProgressDialog.show(context, "", message, true);
		dialog.setCancelable(true);
		return dialog;
	}
	
	public static void hideDialog(Context context, ProgressDialog mDialog) {

		if (null != mDialog) {
			mDialog.dismiss();
		}
	}
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		if (mDisplayMetrics == null) {
			mDisplayMetrics = context.getResources().getDisplayMetrics();
		}
		return (int) (dpValue * mDisplayMetrics.density + 0.5f);
	}
	
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}  
	
	/**
	 * 判断网络是否有效
	 * 
	 * @param context
	 *            上下文对象
	 * @return boolean -- TRUE 有效 -- FALSE 无效
	 */
	public static boolean isNetworkAvailable(Context context) {
		if (context != null) {
			ConnectivityManager manager = (ConnectivityManager) context
					.getApplicationContext().getSystemService(
							Context.CONNECTIVITY_SERVICE);
			if (manager == null) {
				return false;
			}
			NetworkInfo networkinfo = manager.getActiveNetworkInfo();
			if (networkinfo == null || !networkinfo.isAvailable()) {
				return false;
			}

			if (networkinfo.getState() == NetworkInfo.State.CONNECTED) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 判断字符串是否为空
	 * @param text
	 * @return
	 */
	public static boolean isTextEmpty(String text) {
		if(TextUtils.isEmpty(text) || "".equals(text.trim())) {
			return true;
		}
		return false;
	}

	//获取Assert 文件的内容
	public static String getContentFromAssert(Context context , String fileName){
		AssetManager assetManager = context.getAssets();
		try{
			InputStream is = assetManager.open(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			StringBuilder sb = new StringBuilder();
			
			String line = null ;
			while((line=br.readLine())!=null){
				sb.append(line+"\r\n");
			}
			
			return sb.toString();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void writeSharedPreferencesString(Context context, String key, String value) {
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				context.getPackageName() + "_share", Context.MODE_PRIVATE);
		SharedPreferences.Editor mEditor = mSharedPreferences.edit();
		mEditor.putString(key, value);
		mEditor.commit();
	}
	
	public static void writeSharedPreferencesInt(Context context, String key, int value) {
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				context.getPackageName() + "_share", Context.MODE_PRIVATE);
		SharedPreferences.Editor mEditor = mSharedPreferences.edit();
		mEditor.putInt(key, value);
		mEditor.commit();
	}
	
	public static String getSharedPreferencesString(Context context, String key) {
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				context.getPackageName() + "_share", Context.MODE_PRIVATE);
		return mSharedPreferences.getString(key, "");
	}
	
	public static int getSharedPreferencesInt(Context context, String key , int defValue) {
		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				context.getPackageName() + "_share", Context.MODE_PRIVATE);
		return mSharedPreferences.getInt(key, defValue);
	}

}
