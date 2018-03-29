package com.exam.longtian.activity.query;

import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.activity.MainMenuActivity;
import com.exam.longtian.activity.inputbill.InputBill3Activity;
import com.exam.longtian.camera.CaptureActivity;
import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.presenter.PresenterQuery;
import com.exam.longtian.printer.bluetooth.PrinterConnectDialog;
import com.exam.longtian.printer.bluetooth.PrinterSettingMenuActivity;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.Constant;
import com.exam.longtian.util.CommandTools.CommandToolsCallback;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.exam.longtian.util.RegularUtil;
import com.gprinter.io.GpDevice;
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
	private String waybillCode;

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
		setRightTitle("打印");

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == Constant.CAPTURE_BILLCODE && resultCode == RESULT_OK) {

			Bundle bundle = data.getExtras();
			String strBillcode = bundle.getString("result");
			edtBillcode.setText(strBillcode);
			submit(null);
		}
	}

	public void scan(View v){

		Intent openCameraIntent = new Intent(this, CaptureActivity.class);
		startActivityForResult(openCameraIntent, Constant.CAPTURE_BILLCODE);
	}

	/* 
	 * 打印
	 * (non-Javadoc)
	 * @see com.exam.longtian.activity.BaseActivity#clickRight(android.view.View)
	 */
	public void clickRight(View v){

		if (MainMenuActivity.mGpService == null) {
			CommandTools.showToast("打印机服务启动失败，请检查打印机");
			return;
		}

		if(MainMenuActivity.printer_status != GpDevice.STATE_VALID_PRINTER){

			CommandTools.showChooseDialog(this, "请先连接打印机", new CommandToolsCallback() {

				@Override
				public void callback(int position) {
					// TODO Auto-generated method stub
					if(position == 0){

						Intent intent = new Intent(OrderDetailActivity.this, PrinterConnectDialog.class);
						boolean[] state = MainMenuActivity.getConnectState();
						intent.putExtra(MainMenuActivity.CONNECT_STATUS, state);
						startActivity(intent);
						//						finish();
					}
				}
			});

			return;
		}


	}

	public void submit(View v){

		waybillCode = edtBillcode.getText().toString();
		if(TextUtils.isEmpty(waybillCode)){
			CommandTools.showToast("请输入运单号");
			return;
		}

		if(!RegularUtil.checkBill(waybillCode)){
			CommandTools.showToast("运单号不符合规则");
			return;
		}

		clearData();
		PresenterQuery.waybill_detail(this, waybillCode, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				if(data == null){
					return;
				}

				BillInfo billInfo = (BillInfo) data;
				((TextView)findViewById(R.id.order_detail_tv_billcode)).setText(billInfo.getBillCode());
				((TextView)findViewById(R.id.order_detail_send_site)).setText(billInfo.getSendSiteName());
				((TextView)findViewById(R.id.order_detail_deli_site)).setText(billInfo.getDispScanSiteName());
				((TextView)findViewById(R.id.order_detail_send_man)).setText(billInfo.getSenderName());
				((TextView)findViewById(R.id.order_detail_send_time)).setText(billInfo.getSendDate());
				((TextView)findViewById(R.id.order_detail_send_customer)).setText(billInfo.getSenderCustName());
				((TextView)findViewById(R.id.order_detail_send_company)).setText(billInfo.getSenderCompanyName());
				((TextView)findViewById(R.id.order_detail_send_addr)).setText(billInfo.getSenderAddress());
				((TextView)findViewById(R.id.order_detail_send_phone)).setText(billInfo.getSenderPhone());

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

		waybillCode = edtBillcode.getText().toString();
		if(TextUtils.isEmpty(waybillCode)){
			CommandTools.showToast("请输入运单号");
			return;
		}

		Intent intent = new Intent(this, ScanRecordActivity.class);
		intent.putExtra("waybillCode", waybillCode);
		startActivity(intent);
	}

	public void clearData(){

		((TextView)findViewById(R.id.order_detail_tv_billcode)).setText("");
		((TextView)findViewById(R.id.order_detail_send_site)).setText("");
		((TextView)findViewById(R.id.order_detail_deli_site)).setText("");
		((TextView)findViewById(R.id.order_detail_send_man)).setText("");
		((TextView)findViewById(R.id.order_detail_send_time)).setText("");
		((TextView)findViewById(R.id.order_detail_send_customer)).setText("");
		((TextView)findViewById(R.id.order_detail_send_company)).setText("");
		((TextView)findViewById(R.id.order_detail_send_addr)).setText("");
		((TextView)findViewById(R.id.order_detail_send_phone)).setText("");

		((TextView)findViewById(R.id.order_detail_service_type)).setText("");
		((TextView)findViewById(R.id.order_detail_goods_type)).setText("");
		((TextView)findViewById(R.id.order_detail_piece_num)).setText("");
		((TextView)findViewById(R.id.order_detail_fee1)).setText("");

		((TextView)findViewById(R.id.order_detail_total_weight)).setText("");
		((TextView)findViewById(R.id.order_detail_total_v3)).setText("");
		((TextView)findViewById(R.id.order_detail_pay_type)).setText("");
		((TextView)findViewById(R.id.order_detail_fee2)).setText("");

		((TextView)findViewById(R.id.order_detail_rec_man)).setText("");
		((TextView)findViewById(R.id.order_detail_rec_phone)).setText("");
		((TextView)findViewById(R.id.order_detail_rec_customer)).setText("");
		((TextView)findViewById(R.id.order_detail_rec_company)).setText("");
		((TextView)findViewById(R.id.order_detail_rec_addr)).setText("");

		((TextView)findViewById(R.id.order_detail_child_billcode)).setText("");
	}

	/* (non-Javadoc)
	 * @see com.exam.longtian.activity.BaseActivity#onScanSuccess(java.lang.String)
	 */
	public void onScanSuccess(String barcode) {
		// TODO Auto-generated method stub
		edtBillcode.setText(barcode);
		submit(null);
	}
}
