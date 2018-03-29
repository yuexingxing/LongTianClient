package com.exam.longtian.activity.inputbill;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.activity.MainMenuActivity;
import com.exam.longtian.activity.query.ReceiveDetailActivity;
import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.entity.SubBillInfo;
import com.exam.longtian.printer.bluetooth.PrintUtil;
import com.exam.longtian.printer.bluetooth.PrinterConnectDialog;
import com.exam.longtian.printer.bluetooth.PrintUtil.CallBack;
import com.exam.longtian.printer.bluetooth.PrinterSettingMenuActivity;
import com.exam.longtian.util.API;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.CommandTools.CommandToolsCallback;
import com.exam.longtian.util.OkHttpUtil;
import com.exam.longtian.util.RegularUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gprinter.io.GpDevice;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/** 
 * 录单3-
 * 
 * 支持标签打印
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
	@ViewInject(R.id.input_bill3_company) EditText edtCompany;
	@ViewInject(R.id.input_bill3_address) EditText edtAddress;
	@ViewInject(R.id.input_bill3_remark) EditText edtRemark;

	private Gson gson;
	private GsonBuilder builder;

	private BillInfo mBillInfo;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_input_bill3);
		ViewUtils.inject(this);

		mBillInfo = MainMenuActivity.mBillInfo;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("录单三");
		setRightTitle("打印");

		//这两句代码必须的，为的是初始化出来gson这个对象，才能拿来用
		builder = new GsonBuilder();
		gson = builder.create();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	public void onResume(){
		super.onResume();

		edtPhone.setText(mBillInfo.getSenderPhone());
		edtName.setText(mBillInfo.getSenderName());
		edtPassenger.setText(mBillInfo.getSenderCustName());
		edtCompany.setText(mBillInfo.getSenderCompanyName());
		edtAddress.setText(mBillInfo.getSenderAddress());
		edtRemark.setText(mBillInfo.getRemark());
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == 0x0001 && resultCode == RESULT_OK) {

			edtPassenger.setText(data.getStringExtra("customer"));
		}else if(requestCode == 0x0002 && resultCode == RESULT_OK){

		}
	}

	/**
	 * 寄件客户
	 * @param v
	 */
	public void postPassenger(View v){

		saveCurrentData();

		Intent intent = new Intent(this, CustomerListActivity.class);
		intent.putExtra("type", 3);
		startActivityForResult(intent, 0x0001);
	}

	/**
	 * 上一页
	 * @param v
	 */
	public void prePage(View v){

		saveCurrentData();
		finish();
	}

	/**
	 * 下一页
	 * @param v
	 */
	public void nextPage(View v){

	}

	public boolean checkData(){

		if(TextUtils.isEmpty(mBillInfo.getSenderPhone())){
			CommandTools.showToast("请输入寄件人电话");
			return false;
		}

		if(!RegularUtil.checkPhone(mBillInfo.getSenderPhone())){
			CommandTools.showToast("寄件电话格式不正确");
			return false;
		}

		if(TextUtils.isEmpty(mBillInfo.getSenderName())){
			CommandTools.showToast("请输入寄件人");
			return false;
		}

		return true;
	}

	/**
	 * 提交
	 * @param v
	 */
	public void submit(View v){

		saveCurrentData();

		if(!checkData()){
			return;
		}

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(gson.toJson(MainMenuActivity.mBillInfo, BillInfo.class));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		OkHttpUtil.post(this, API.waybill_add, jsonObject, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub
				CommandTools.showToast(message);
				
				Message msg = new Message();
				msg.what = 0x0011;
				msg.obj = message;
				MyApplication.getEventBus().post(msg);
			
				if(success){

					CommandTools.showChooseDialog(InputBill3Activity.this, "是否打印标签吗？", new CommandToolsCallback() {

						@Override
						public void callback(int position) {

							if(position == 0){

								startPrint();

								Intent intent = new Intent();
								setResult(RESULT_OK, intent);
								finish();
							}
						}
					});
				}
			}
		});
	}

	/**
	 * 保存当前界面数据
	 */
	public void saveCurrentData(){

		mBillInfo.setSenderName(edtName.getText().toString());
		mBillInfo.setSenderPhone(edtPhone.getText().toString());
		mBillInfo.setSenderAddress(edtAddress.getText().toString());
		mBillInfo.setSenderCompanyName(edtCompany.getText().toString());

		mBillInfo.setRemark(edtRemark.getText().toString());
	}

	/*
	 * 打印当前录入数据
	 * (non-Javadoc)
	 * @see com.exam.longtian.activity.BaseActivity#clickRight(android.view.View)
	 */
	public void clickRight(View v){

		saveCurrentData();

		if(!checkData()){
			return;
		}

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

						Intent intent = new Intent(InputBill3Activity.this, PrinterConnectDialog.class);
						boolean[] state = MainMenuActivity.getConnectState();
						intent.putExtra(MainMenuActivity.CONNECT_STATUS, state);
						startActivity(intent);
					}
				}
			});

			return;
		}

		CommandTools.showChooseDialog(InputBill3Activity.this, "确定打印该数据吗？", new CommandToolsCallback() {

			@Override
			public void callback(int position) {
				// TODO Auto-generated method stub
				if(position == 0){

					startPrint();
				}
			}
		});
	}

	public void startPrint(){

		String[] arrBill = new String[0];
		if(!TextUtils.isEmpty(MainMenuActivity.mBillInfo.getSubBillcode())){
			arrBill = MainMenuActivity.mBillInfo.getSubBillcode().split(",");
		}

		List<BillInfo> list = new ArrayList<BillInfo>();
		for(int i=0; i<arrBill.length; i++){

			BillInfo billInfo = (BillInfo) MainMenuActivity.mBillInfo.clone();//克隆一个新的对象
			billInfo.setBillCode(arrBill[i]);

			list.add(billInfo);
		}

		PrintUtil.printLabelList(list, null);
	}
}
