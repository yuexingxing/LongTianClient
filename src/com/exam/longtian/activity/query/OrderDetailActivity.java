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
				((TextView)findViewById(R.id.order_detail_tv_billcode)).setText(billInfo.getBillCode());
				((TextView)findViewById(R.id.order_detail_send_site)).setText(billInfo.getSendSiteName());
				((TextView)findViewById(R.id.order_detail_deli_site)).setText(billInfo.getDispScanSiteName());
				((TextView)findViewById(R.id.order_detail_send_man)).setText(billInfo.getSenderName());
				((TextView)findViewById(R.id.order_detail_send_time)).setText(billInfo.getSendDate());
				((TextView)findViewById(R.id.order_detail_send_customer)).setText(billInfo.getSenderCustName());
				((TextView)findViewById(R.id.order_detail_send_company)).setText(billInfo.getSenderCompanyName());
				((TextView)findViewById(R.id.order_detail_send_addr)).setText(billInfo.getSenderAddress());

				((TextView)findViewById(R.id.order_detail_service_type)).setText(billInfo.getServicePatternName());
				((TextView)findViewById(R.id.order_detail_goods_type)).setText(billInfo.getPackageKindName());
				((TextView)findViewById(R.id.order_detail_piece_num)).setText(billInfo.getPieceNum());
				((TextView)findViewById(R.id.order_detail_fee1)).setText(billInfo.getAgencyFund());

				((TextView)findViewById(R.id.order_detail_total_weight)).setText(billInfo.getTotalWeight());
				((TextView)findViewById(R.id.order_detail_total_v3)).setText(billInfo.getTotalVolume());
				((TextView)findViewById(R.id.order_detail_pay_type)).setText(billInfo.getPayModeName());
				((TextView)findViewById(R.id.order_detail_fee2)).setText(billInfo.getFreight());

				((TextView)findViewById(R.id.order_detail_rec_man)).setText(billInfo.getRecipientsCustName());
				((TextView)findViewById(R.id.order_detail_rec_phone)).setText(billInfo.getRecipientsPhone());
				((TextView)findViewById(R.id.order_detail_rec_customer)).setText(billInfo.getRecipientsCustName());
				((TextView)findViewById(R.id.order_detail_rec_company)).setText(billInfo.getRecipientsCompanyName());
				((TextView)findViewById(R.id.order_detail_rec_addr)).setText(billInfo.getRecipientsAddress());

				((TextView)findViewById(R.id.order_detail_child_billcode)).setText(billInfo.getSubBillcode());
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
