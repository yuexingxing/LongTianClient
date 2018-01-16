package com.exam.longtian.activity.query;

import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.presenter.PresenterQuery;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/** 
 * 运单详情
 * 
 * @author yxx
 *
 * @date 2017-12-4 上午11:39:25
 * 
 */
public class OrderDetailActivity extends BaseActivity {

	@ViewInject(R.id.order_detail_billcode) EditText edtBillcode;
	
	@ViewInject(R.id.order_detail_tv_billcode) TextView tvBillcode;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentViewId(R.layout.activity_order_detail);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("运单详情");
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	public void submit(View v){

		String billcode = edtBillcode.getText().toString();
		if(TextUtils.isEmpty(billcode)){
			CommandTools.showToast("请输入运单号");
			return;
		}

		PresenterQuery.waybill_detail(this, billcode, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				BillInfo billInfo = (BillInfo) data;
				tvBillcode.setText(billInfo.getBillCode());
			}
		});
	}

	/**
	 * 扫描记录
	 * @param v
	 */
	public void scanDetail(View v){

		startActivity((new Intent(this, ScanRecordActivity.class)));
	}

}
