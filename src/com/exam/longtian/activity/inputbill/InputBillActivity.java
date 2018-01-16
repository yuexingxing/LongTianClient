package com.exam.longtian.activity.inputbill;

import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.util.CommandTools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

/** 
 * 录单
 * 
 * @author yxx
 *
 * @date 2017-11-28 下午2:05:06
 * 
 */
public class InputBillActivity extends BaseActivity {

	@ViewInject(R.id.input_bill_sitename) EditText edtSiteName;
	@ViewInject(R.id.input_bill_billcode) EditText edtBillcode;
	@ViewInject(R.id.input_bill_delivery_sitename) EditText edtDeliSiteName;
	@ViewInject(R.id.input_bill_rec_address) EditText edtRecAddress;
	@ViewInject(R.id.input_bill_rec_phone) EditText edtRecPhone;
	@ViewInject(R.id.input_bill_rec_name) EditText edtRecName;
	@ViewInject(R.id.input_bill_rec_pass) EditText edtRecCustomer;
	@ViewInject(R.id.input_bill_rec_company) EditText edtRecCompany;

	public static BillInfo mBillInfo = new BillInfo();
	private String recipientsCustGcode;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_input_bill);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("录单一");
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		edtSiteName.setText(MyApplication.mUser.getOpEmpName());
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == 0x0001 && resultCode == RESULT_OK) {
			edtDeliSiteName.setText(data.getStringExtra("name"));

			mBillInfo.setDestSiteGcode(data.getStringExtra("code"));
		}else if(requestCode == 0x0002 && resultCode == RESULT_OK){
			edtRecCustomer.setText(data.getStringExtra("name"));
			edtRecName.setText(data.getStringExtra("name"));
			edtRecCompany.setText(data.getStringExtra("companyName"));
			edtRecPhone.setText(data.getStringExtra("phone"));

			recipientsCustGcode = data.getStringExtra("code");
		}
	}

	/**
	 * 派件网点
	 * @param v
	 */
	public void deliverySite(View v){

		Intent intent = new Intent(this, SiteListActivity.class);
		startActivityForResult(intent, 0x0001);
	}

	/**
	 * 收件人
	 * @param v
	 */
	public void recCustomer(View v){

		Intent intent = new Intent(this, CustomerListActivity.class);
		startActivityForResult(intent, 0x0002);
	}

	/**
	 * 上一页
	 * @param v
	 */
	public void prePage(View v){

	}

	/**
	 * 下一页
	 * @param v
	 */
	public void nextPage(View v){

		mBillInfo.setBillCode(edtBillcode.getText().toString());
		mBillInfo.setOpEmpGcode(MyApplication.mUser.getOpEmpGcode());
		mBillInfo.setOpEmpName(MyApplication.mUser.getOpEmpName());

		mBillInfo.setRecipientsAddress(edtRecAddress.getText().toString());
		mBillInfo.setRecipientsPhone(edtRecPhone.getText().toString());

		mBillInfo.setRecipientsName(edtRecName.getText().toString());
		mBillInfo.setRecipientsCompanyName(edtRecCompany.getText().toString());

		mBillInfo.setRecipientsCustName(edtRecCustomer.getText().toString());
		mBillInfo.setRecipientsPhone(edtRecPhone.getText().toString());
		mBillInfo.setRecipientsCustGcode(recipientsCustGcode);
		mBillInfo.setRecipientsAddress(edtRecAddress.getText().toString());
		mBillInfo.setRecipientsCompanyName(edtRecCompany.getText().toString());

		mBillInfo.setSendDate(CommandTools.getTime());
		mBillInfo.setRegisterGcode(MyApplication.mUser.getOpEmpGcode());

		mBillInfo.setSendSiteGcode(MyApplication.mUser.getOwnSiteGcode());

		if(TextUtils.isEmpty(mBillInfo.getBillCode())){
			CommandTools.showToast("请输入运单号");
			return;
		}

		if(mBillInfo.getBillCode().length() != 14){
			CommandTools.showToast("运单号长度为14位");
			return;
		}

		if(TextUtils.isEmpty(mBillInfo.getDestSiteGcode())){
			CommandTools.showToast("请选择派件网点");
			return;
		}

		if(TextUtils.isEmpty(mBillInfo.getRecipientsName())){
			CommandTools.showToast("请输入收件人");
			return;
		}

		Intent intent = new Intent(this, InputBill2Activity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 提交
	 * @param v
	 */
	public void submit(View v){

	}
}
