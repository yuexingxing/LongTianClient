package com.exam.longtian.activity.query;

import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/** 
 * 查询
 * 
 * @author yxx
 *
 * @date 2017-12-1 下午4:23:08
 * 
 */
public class QueryMenuActivity extends BaseActivity {


	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentViewId(R.layout.activity_query_menu);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("查询");
		hidenTopBar();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	/**
	 * 收件查询
	 * @param v
	 */
	public void receive(View v){

		startActivity((new Intent(this, QueryReceiveActivity.class)));
	}

	/**
	 * 派件查询
	 * @param v
	 */
	public void delivery(View v){

	}

	/**
	 * 订单详情
	 * @param v
	 */
	public void detail(View v){
		startActivity((new Intent(this, OrderDetailActivity.class)));
	}

	/**
	 * 返回
	 * @param v
	 */
	public void back(View v){

		finish();
	}
}
