package com.exam.longtian.activity.query;

import java.util.Calendar;

import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.presenter.PresenterQuery;
import com.exam.longtian.presenter.PresenterUtil;
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
 * 派件查询
 * 
 * @author yxx
 *
 * @date 2018-1-25 上午10:13:13
 * 
 */
public class QueryDispActivity extends BaseActivity {

	@ViewInject(R.id.query_disp_starttime) EditText edtStartTime;
	@ViewInject(R.id.query_disp_endtime) EditText edtEndTime;

	@ViewInject(R.id.query_disp_bill_count) EditText edtBillCount;
	@ViewInject(R.id.query_disp_count) EditText edtCount;
	@ViewInject(R.id.query_disp_total_weight) EditText edtTotalWeight;
	@ViewInject(R.id.query_disp_total_v3) EditText edtTotalV3;
	@ViewInject(R.id.query_disp_fee1) EditText edtFee1;
	@ViewInject(R.id.query_disp_fee2) EditText edtFee2;
	@ViewInject(R.id.query_disp_fee3) EditText edtFee3;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentViewId(R.layout.activity_query_disp);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("派件查询");
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

		PresenterQuery.waybill_getReWaybillList(this, PresenterUtil.ORDER_TYPE_DISP, new ObjectCallback() {

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

		Intent intent = new Intent(this, ReceiveDetailActivity.class);
		intent.putExtra("order_type", PresenterUtil.ORDER_TYPE_DISP);
		startActivity(intent);
	}
}
