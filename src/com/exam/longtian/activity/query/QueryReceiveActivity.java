package com.exam.longtian.activity.query;

import java.util.Calendar;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.presenter.PresenterQuery;
import com.exam.longtian.util.API;
import com.exam.longtian.util.OkHttpUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

/** 
 * 收件查询
 * 
 * @author yxx
 *
 * @date 2017-12-1 下午4:37:02
 * 
 */
public class QueryReceiveActivity extends BaseActivity {


	@ViewInject(R.id.query_receive_starttime) EditText edtStartTime;
	@ViewInject(R.id.query_receive_endtime) EditText edtEndTime;

	@ViewInject(R.id.query_receive_bill_count) EditText edtBillCount;
	@ViewInject(R.id.query_receive_count) EditText edtCount;
	@ViewInject(R.id.query_receive_total_weight) EditText edtTotalWeight;
	@ViewInject(R.id.query_receive_total_v3) EditText edtTotalV3;
	@ViewInject(R.id.query_receive_fee1) EditText edtFee1;
	@ViewInject(R.id.query_receive_fee2) EditText edtFee2;
	@ViewInject(R.id.query_receive_fee3) EditText edtFee3;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentViewId(R.layout.activity_query_receive);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("收件查询");
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		Calendar c = Calendar.getInstance();
		String strDate = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DAY_OF_MONTH);

		edtStartTime.setText(strDate + " 00:00:00");
		edtEndTime.setText(strDate + " 23:59:59");
	}

	/**
	 * 起始日期
	 * @param v
	 */
	public void startTime(View v){

		Calendar c = Calendar.getInstance();

		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {  

			@Override  
			public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {  

				edtStartTime.setText(String.format("%d-%d-%d 00:00:00",i,i1+1,i2));  
			}  

		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show(); 
	}

	/**
	 * 结束日期
	 * @param v
	 */
	public void endTime(View v){

		Calendar c = Calendar.getInstance();

		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {  

			@Override  
			public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {  

				edtEndTime.setText(String.format("%d-%d-%d 23:59:59",i,i1+1,i2));  
			}  

		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show(); 
	}

	/**
	 * 查询
	 * @param v
	 */
	public void query(View v){

		PresenterQuery.waybill_getReWaybillList(this, new ObjectCallback() {
			
			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * 明细
	 * @param v
	 */
	public void detail(View v){

		startActivity((new Intent(this, ReceiveDetailActivity.class)));
	}
}
