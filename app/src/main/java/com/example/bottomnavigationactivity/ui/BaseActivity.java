package com.example.bottomnavigationactivity.ui;


import com.example.bottomnavigationactivity.model.Result;
import com.example.bottomnavigationactivity.task.CommonAsyncTask;
import com.example.bottomnavigationactivity.task.OnDataListener;
import com.example.bottomnavigationactivity.util.AndroidUtils;
import com.example.bottomnavigationactivity.util.Commom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseActivity extends AppCompatActivity implements OnClickListener, OnDataListener {
	//头部的左边ImageView
	protected ImageView mIvBack;
	//头部的左边TextView
	protected TextView mTvTitle;
	
	private CommonAsyncTask task;

	@Override
	public abstract Result doFetchData(Object obj) throws Exception ;
	//返回事件 结束activity
	protected OnClickListener goBackListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			BaseActivity.this.finish();
		}
	};

	//成功后关闭loading
	@Override
	public void doProcessData(Object obj) throws Exception{
		hideLoading();
	}
	
	/**
	 * 返回错误后提升
	 */
	@Override
	public void doErrorData(Object obj) throws Exception {
		hideLoading();
		Result result = (Result)obj;
		if (null != result) {

		} else {

		}
	}

	@Override
	public abstract void onClick(View v);
	
	protected abstract void initUI();
	
	protected ProgressDialog mDialog;
	
	/*
	 * 显示正在加载
	 */
	protected void showLoading(){
		mDialog = AndroidUtils.showDialog(this, "on Loading");
		mDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				task.disConnection();
			}
		});
	}
	/*
	 * 隐藏正在加载
	 */
	protected void hideLoading(){
		AndroidUtils.hideDialog(this, mDialog);
	}
	
	/*
	 * 新建线程后台处理
	 */
	protected void doConnection(int type, Object... params) {
		task = new CommonAsyncTask(this);
		task.doConnection(type, params);
	}
	protected void doConnection(int type) {
		task = new CommonAsyncTask(this);
		task.doConnection(type);
	}

	
}
