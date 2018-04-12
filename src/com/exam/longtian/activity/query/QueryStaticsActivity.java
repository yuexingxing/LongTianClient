package com.exam.longtian.activity.query;

import java.util.Calendar;

import org.json.JSONObject;

import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.presenter.PresenterQuery;
import com.exam.longtian.presenter.PresenterUtil;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.DateUtil;
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
 * 收件/派件--统计
 * 
 * @author yxx
 *
 * @date 2017-12-1 下午4:37:02
 * 
 */
public class QueryStaticsActivity extends BaseActivity {

	@ViewInject(R.id.query_receive_starttime) EditText edtStartTime;
	@ViewInject(R.id.query_receive_endtime) EditText edtEndTime;

	@ViewInject(R.id.query_receive_bill_count) EditText edtBillCount;
	@ViewInject(R.id.query_receive_count) EditText edtCount;
	@ViewInject(R.id.query_receive_total_weight) EditText edtTotalWeight;
	@ViewInject(R.id.query_receive_total_v3) EditText edtTotalV3;
	@ViewInject(R.id.query_receive_fee1) EditText edtFee1;
	@ViewInject(R.id.query_receive_fee2) EditText edtFee2;
	@ViewInject(R.id.query_receive_fee3) EditText edtFee3;

	private String orderType;

	private int startYear;
	private int startMonth;
	private int startDay;

	private int endYear;
	private int endMonth;
	private int endDay;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentViewId(R.layout.activity_query_receive);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

		orderType = getIntent().getStringExtra("order_type");
		if(PresenterUtil.ORDER_TYPE_RECEIVE.equals(orderType)){
			setTitle("收件统计");
		}else{
			setTitle("派件统计");
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		Calendar c = Calendar.getInstance();
		
		startYear = c.get(Calendar.YEAR);
		startMonth = c.get(Calendar.MONTH) + 1;
		startDay = c.get(Calendar.DAY_OF_MONTH);

		endYear = startYear;
		endMonth = startMonth;
		endDay = startDay;

		String strDate = startYear + "-" + startMonth + "-" + startDay;

		edtStartTime.setText(strDate);
		edtEndTime.setText(strDate);
	}

	/**
	 * 起始日期
	 * @param v
	 */
	public void startTime(View v){

		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {  

			@Override  
			public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {  

				startYear = i;
				startMonth = (i1 + 1);
				startDay = i2;

				edtStartTime.setText(String.format("%d-%d-%d",startYear,startMonth,startDay));  
			}  

		}, startYear, startMonth - 1, startDay).show(); 
	}

	/**
	 * 结束日期
	 * @param v
	 */
	public void endTime(View v){

		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {  

			@Override  
			public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {  

				endYear = i;
				endMonth = (i1 + 1);
				endDay = i2;

				edtEndTime.setText(String.format("%d-%d-%d",endYear,endMonth,endDay));  
			}  

		}, endYear, endMonth - 1, endDay).show(); 
	}

	/**
	 * 查询
	 * @param v
	 */
	public void query(View v){
		
		if(!DateUtil.compareDate(startYear, startMonth, startDay, endYear, endMonth, endDay)){
			CommandTools.showToast("起始时间不能大于结束时间");
			return;
		}
		
		String startTime = edtStartTime.getText().toString() + " 00:00:00";
		String endTime = edtEndTime.getText().toString() + " 23:59:59";

		clearData();
		PresenterQuery.waybill_getWaybillCountBySelf(this, orderType, startTime, endTime, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				JSONObject jsonObject = (JSONObject) data;
				if(jsonObject == null){
					return;
				}

				edtCount.setText(jsonObject.optInt("count") + "");
				edtBillCount.setText(jsonObject.optInt("pieceNum") + "");
				edtTotalWeight.setText(jsonObject.optDouble("totalWeight") + "");
				edtTotalV3.setText(jsonObject.optDouble("totalVolume") + "");
				edtFee1.setText(jsonObject.optDouble("agencyFund") + "");
				edtFee2.setText(jsonObject.optDouble("freight") + "");
				edtFee3.setText(jsonObject.optDouble("daofreight") + "");
			}
		});
	}

	public void clearData(){

		edtCount.setText("");
		edtBillCount.setText("");
		edtTotalWeight.setText("");
		edtTotalV3.setText("");
		edtFee1.setText("");
		edtFee2.setText("");
		edtFee3.setText("");
	}

	/**
	 * 明细
	 * @param v
	 */
	public void detail(View v){
		
		String startTime = edtStartTime.getText().toString() + " 00:00:00";
		String endTime = edtEndTime.getText().toString() + " 23:59:59";

		Intent intent = new Intent(this, ReceiveDetailActivity.class);
		intent.putExtra("order_type", orderType);
		intent.putExtra("startTime", startTime);
		intent.putExtra("endTime", endTime);
		startActivity(intent);
	}
}
