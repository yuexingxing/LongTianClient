package com.exam.longtian.activity.inputbill;

import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.activity.MainMenuActivity;
import com.exam.longtian.camera.CaptureActivity;
import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.presenter.PresenterUtil;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.Constant;
import com.exam.longtian.util.RegularUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/** 
 * ¼��-1
 * 
 * @author yxx
 *
 * @date 2017-11-28 ����2:05:06
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

	private String recipientsCustGcode;
	public BillInfo mBillInfo;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_input_bill);
		ViewUtils.inject(this);

		mBillInfo = MainMenuActivity.mBillInfo;
		MyApplication.getEventBus().register(this);
	}

	@Override
	public void initView() {
		setTitle("¼��һ");
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		edtSiteName.setText(MyApplication.mUser.getOwnSiteName());

		edtBillcode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				mBillInfo.setBillCode(arg0.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void onResume(){
		super.onResume();

		edtBillcode.setText(mBillInfo.getBillCode());
		edtRecAddress.setText(mBillInfo.getRecipientsAddress());
		edtRecPhone.setText(mBillInfo.getRecipientsPhone());
		edtRecName.setText(mBillInfo.getRecipientsName());
		edtRecCustomer.setText(mBillInfo.getRecipientsCustName());
		edtRecCompany.setText(mBillInfo.getRecipientsCompanyName());
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == 0x0001 && resultCode == RESULT_OK) {

			edtDeliSiteName.setText(data.getStringExtra("name"));
			mBillInfo.setDestSiteGcode(data.getStringExtra("code"));
		}else if(requestCode == 0x0002 && resultCode == RESULT_OK){

			if(TextUtils.isEmpty(edtRecCustomer.getText().toString())){
				edtRecCustomer.setText(data.getStringExtra("customer"));
			}

			if(TextUtils.isEmpty(edtRecCompany.getText().toString())){
				edtRecCompany.setText(data.getStringExtra("companyName"));
			}

			if(TextUtils.isEmpty(edtRecPhone.getText().toString())){
				edtRecPhone.setText(data.getStringExtra("phone"));
			}

			recipientsCustGcode = data.getStringExtra("code");
		}else if(requestCode == 0x1001 && resultCode == RESULT_OK){

			mBillInfo = null;
			mBillInfo = new BillInfo();
			edtDeliSiteName.setText("");
			onResume();
		}else if (requestCode == Constant.CAPTURE_BILLCODE && resultCode == RESULT_OK) {

			if(data == null){
				return;
			}

			Bundle bundle = data.getExtras();
			String strBillcode = bundle.getString("result");
			edtBillcode.setText(strBillcode);
		}
	}

	public void scan(View v){

		Intent openCameraIntent = new Intent(this, CaptureActivity.class);
		startActivityForResult(openCameraIntent, Constant.CAPTURE_BILLCODE);
	}

	/**
	 * �ɼ�����
	 * @param v
	 */
	public void deliverySite(View v){

		Intent intent = new Intent(this, SiteListActivity.class);
		intent.putExtra("order_type", PresenterUtil.ORDER_TYPE_INPUT);
		startActivityForResult(intent, 0x0001);
	}

	/**
	 * �ռ���
	 * @param v
	 */
	public void recCustomer(View v){

		Intent intent = new Intent(this, CustomerListActivity.class);
		intent.putExtra("type", 1);
		startActivityForResult(intent, 0x0002);
	}

	/**
	 * ��һҳ
	 * @param v
	 */
	public void prePage(View v){

	}

	/**
	 * ��һҳ
	 * @param v
	 */
	public void nextPage(View v){

		saveCurrentData();

		if(TextUtils.isEmpty(mBillInfo.getBillCode())){
			CommandTools.showToast("�������˵���");
			return;
		}

		if(!RegularUtil.checkBill(mBillInfo.getBillCode())){
			CommandTools.showToast("�˵��Ų����Ϲ���");
			return;
		}

		if(TextUtils.isEmpty(edtDeliSiteName.getText().toString())){
			CommandTools.showToast("��ѡ���ɼ�����");
			return;
		}

		if(TextUtils.isEmpty(mBillInfo.getRecipientsAddress())){
			CommandTools.showToast("�������ռ���ַ");
			return;
		}

		if(TextUtils.isEmpty(mBillInfo.getRecipientsPhone())){
			CommandTools.showToast("�������ռ��绰");
			return;
		}

		//		if(!RegularUtil.checkPhone(mBillInfo.getRecipientsPhone())){
		//			CommandTools.showToast("�ռ��绰��ʽ����ȷ");
		//			return;
		//		}

		if(TextUtils.isEmpty(mBillInfo.getRecipientsName())){
			CommandTools.showToast("�������ռ���");
			return;
		}

		Intent intent = new Intent(InputBillActivity.this, InputBill2Activity.class);
		startActivityForResult(intent, 0x1001);
	}

	/**
	 * �ύ
	 * @param v
	 */
	public void submit(View v){

	}

	@Override
	public void onStop(){
		super.onStop();

		saveCurrentData();
	}

	/**
	 * ���浱ǰ��������
	 */
	public void saveCurrentData(){

		mBillInfo.setBillCode(edtBillcode.getText().toString());
		mBillInfo.setOpEmpGcode(MyApplication.mUser.getOpEmpGcode());
		mBillInfo.setOpEmpName(MyApplication.mUser.getOpEmpName());
		mBillInfo.setOpTime(CommandTools.getTime());

		mBillInfo.setRecipientsName(edtRecName.getText().toString());

		mBillInfo.setRecipientsCustName(edtRecCustomer.getText().toString());
		mBillInfo.setRecipientsPhone(edtRecPhone.getText().toString());
		mBillInfo.setRecipientsCustGcode(recipientsCustGcode);
		mBillInfo.setRecipientsAddress(edtRecAddress.getText().toString());
		mBillInfo.setRecipientsCompanyName(edtRecCompany.getText().toString());

		mBillInfo.setSendDate(CommandTools.getTime());

		mBillInfo.setRegisterGcode(MyApplication.mUser.getOpEmpGcode());
		mBillInfo.setSendSiteGcode(MyApplication.mUser.getOwnSiteGcode());
		mBillInfo.setReceiverGcode(MyApplication.mUser.getEmpGcode());
	}

	/* (non-Javadoc)
	 * @see com.exam.longtian.activity.BaseActivity#onDestory()
	 */
	public void onDestory(){
		super.onDestroy();

		MyApplication.getEventBus().unregister(this);
	}


	/* (non-Javadoc)
	 * @see com.exam.longtian.activity.BaseActivity#onEventMainThread(android.os.Message)
	 */
	public void onEventMainThread(Message msg) {

		if(msg.what == Constant.SCANNER_BILLCODE){

			String billcode = (String) msg.obj;
			edtBillcode.setText(billcode);
		}
	}
}
