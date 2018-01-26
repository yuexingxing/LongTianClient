package com.exam.longtian.activity.query;

import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.presenter.PresenterUtil;
import com.lidroid.xutils.ViewUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/** 
 * ��ѯ
 * 
 * @author yxx
 *
 * @date 2017-12-1 ����4:23:08
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
		setTitle("��ѯ");
		hidenTopBar();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	/**
	 * �ռ���ѯ
	 * @param v
	 */
	public void receive(View v){

		Intent intent = new Intent(this, QueryReceiveActivity.class);
		intent.putExtra("order_type", PresenterUtil.ORDER_TYPE_RECEIVE);
		startActivity(intent);
	}

	/**
	 * �ɼ���ѯ
	 * @param v
	 */
	public void delivery(View v){

		Intent intent = new Intent(this, QueryDispActivity.class);
		intent.putExtra("order_type", PresenterUtil.ORDER_TYPE_DISP);
		startActivity(intent);
	}

	/**
	 * ��������
	 * @param v
	 */
	public void detail(View v){
		startActivity((new Intent(this, OrderDetailActivity.class)));
	}

	/**
	 * ����
	 * @param v
	 */
	public void back(View v){

		finish();
	}
}
