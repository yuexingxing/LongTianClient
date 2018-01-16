package com.exam.longtian.activity.inputbill;

import org.json.JSONException;
import org.json.JSONObject;

import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.R.id;
import com.exam.longtian.R.layout;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.util.API;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.OkHttpUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

/** 
 * 录单3
 * 
 * @author yxx
 *
 * @date 2017-11-28 下午4:15:16
 * 
 */
public class InputBill3Activity extends BaseActivity {

	@ViewInject(R.id.input_bill3_phone) EditText edtPhone;
	@ViewInject(R.id.input_bill3_name) EditText edtName;
	@ViewInject(R.id.input_bill3_passenger) EditText edtPassenger;
	@ViewInject(R.id.input_bill3_rule) EditText edtRules;
	@ViewInject(R.id.input_bill3_company) EditText edtCompany;
	@ViewInject(R.id.input_bill3_address) EditText edtAddress;
	@ViewInject(R.id.input_bill3_remark) EditText edtRemark;

	private Gson gson;
	private GsonBuilder builder;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_input_bill3);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("录单三");

		//这两句代码必须的，为的是初始化出来gson这个对象，才能拿来用
		builder = new GsonBuilder();
		gson = builder.create();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == 0x0001 && resultCode == RESULT_OK) {
			edtPassenger.setText(data.getStringExtra("customer"));

			InputBillActivity.mBillInfo.setSenderCustName(data.getStringExtra("customer"));
			InputBillActivity.mBillInfo.setSenderCustGcode(data.getStringExtra("code"));

		}else if(requestCode == 0x0002 && resultCode == RESULT_OK){

		}
	}


	/**
	 * 寄件客户
	 * @param v
	 */
	public void postPassenger(View v){

		Intent intent = new Intent(this, CustomerListActivity.class);
		startActivityForResult(intent, 0x0001);
	}

	/**
	 * 上一页
	 * @param v
	 */
	public void prePage(View v){
		finish();
	}

	/**
	 * 下一页
	 * @param v
	 */
	public void nextPage(View v){

	}

	/**
	 * 提交
	 * @param v
	 */
	public void submit(View v){

		BillInfo mBillInfo = InputBillActivity.mBillInfo;

		mBillInfo.setSenderName(edtName.getText().toString());
		mBillInfo.setSenderPhone(edtPhone.getText().toString());
		mBillInfo.setSenderAddress(edtAddress.getText().toString());

		mBillInfo.setRemark(edtRemark.getText().toString());
		
		if(TextUtils.isEmpty(mBillInfo.getSenderCustGcode())){
			CommandTools.showToast("请选择寄件人");
			return;
		}

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(gson.toJson(InputBillActivity.mBillInfo, BillInfo.class));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		OkHttpUtil.post(this, API.waybill_add, jsonObject, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				Message msg = new Message();
				msg.what = 0x0011;
				msg.obj = message;
				MyApplication.getEventBus().post(msg);
				if(success){
					finish();
				}
			}
		});
	}
}
