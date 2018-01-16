package com.exam.longtian.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.activity.arrive.ArriveScanActivity;
import com.exam.longtian.activity.inputbill.InputBillActivity;
import com.exam.longtian.activity.query.QueryMenuActivity;
import com.exam.longtian.activity.send.SendScanActivity;
import com.exam.longtian.activity.sign.SignScanActivity;
import com.lidroid.xutils.ViewUtils;

/** 
 * 主菜单
 * 
 * @author yxx
 *
 * @date 2017-11-27 下午6:15:40
 * 
 */
public class MainMenuActivity extends BaseActivity {

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_main);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		hidenTopBar();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	/**
	 * 录单
	 * @param v
	 */
	public void ludan(View v){

		startActivity((new Intent(this, InputBillActivity.class)));
	}

	/**
	 * 发件扫描
	 * @param v
	 */
	public void send(View v){

		startActivity((new Intent(this, SendScanActivity.class)));
	}

	/**
	 * 到件
	 * @param v
	 */
	public void arrive(View v){

		startActivity((new Intent(this, ArriveScanActivity.class)));
	}

	/**
	 * 签收
	 * @param v
	 */
	public void sign(View v){

		startActivity((new Intent(this, SignScanActivity.class)));
	}

	/**
	 * 订单查询
	 * @param v
	 */
	public void query(View v){

		startActivity((new Intent(this, QueryMenuActivity.class)));
	}

	/**
	 * 退出
	 * @param v
	 */
	public void back(View v){

		exit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) { // 获取 back键

			exit();
		}
		return false;
	}

	public void exit(){

		new AlertDialog.Builder(this)
		.setTitle("提示")
		.setMessage("确认退出程序？")
		.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MyApplication.finishAllActivities();
				finish();
			}
		}).setNegativeButton("取消", null).show();
	}
}
