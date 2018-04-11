package com.exam.longtian.activity.inputbill;

import java.util.ArrayList;
import java.util.List;
import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.activity.MainMenuActivity;
import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.entity.DictInfo;
import com.exam.longtian.presenter.PresenterUtil;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.exam.longtian.util.RegularUtil;
import com.exam.longtian.view.SingleItemDialog;
import com.exam.longtian.view.SingleItemDialog.SingleItemCallBack;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

/** 
 * 录单-2
 * 
 * @author yxx
 *
 * @date 2017-11-28 下午3:30:11
 * 
 */
public class InputBill2Activity extends BaseActivity {

	@ViewInject(R.id.input_bill2_type) EditText edtType;
	@ViewInject(R.id.input_bill2_genre) EditText edtGenre;
	@ViewInject(R.id.input_bill2_count) EditText edtCount;
	@ViewInject(R.id.input_bill2_weight) EditText edtWeight;
	@ViewInject(R.id.input_bill2_v3) EditText edtV3;
	@ViewInject(R.id.input_bill2_fee) EditText edtFee;
	@ViewInject(R.id.input_bill2_fee2) EditText edtFee2;
	@ViewInject(R.id.input_bill2_fee3) EditText edtFee3;
	@ViewInject(R.id.input_bill2_pay_type) EditText edtPayType;
	@ViewInject(R.id.input_bill2_backbill) EditText edtBackBill;

	@ViewInject(R.id.input_bill2_check1) CheckBox checkBox1;
	@ViewInject(R.id.input_bill2_check2) CheckBox checkBox2;

	private List<String> listServiceType = new ArrayList<String>();
	private List<String> listGoodsGenre = new ArrayList<String>();
	private List<String> listPayType = new ArrayList<String>();

	private BillInfo mBillInfo;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_input_bill2);
		ViewUtils.inject(this);

		mBillInfo = MainMenuActivity.mBillInfo;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("录单二");

		checkBox1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					mBillInfo.setBeIntoWarehouse("1");
				}else{
					mBillInfo.setBeIntoWarehouse("0");
				}
			}
		});

		checkBox2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub

				if(arg1){
					edtBackBill.setFocusable(true);
					edtBackBill.setCursorVisible(true);
					edtBackBill.setFocusableInTouchMode(true);
					edtBackBill.requestFocus();
				}else{
					edtBackBill.setText("");
					edtBackBill.setCursorVisible(false);
					edtBackBill.setFocusable(false);
					edtBackBill.setFocusableInTouchMode(false);
				}
			}
		});

		edtCount.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub

				if(!arg1){

					String count = edtCount.getText().toString();
					if(TextUtils.isEmpty(count)){
						CommandTools.showToast("请先输入子单数量");
						return;
					}

					if(Integer.parseInt(count) == 0){
						CommandTools.showToast("件数必须大于0");
						return;
					}
				}
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		for(int i=0; i<DictInfo.dispList.size(); i++){

			DictInfo dictInfo = DictInfo.dispList.get(i);
			listServiceType.add(dictInfo.getPropertyValue());
		}

		for(int i=0; i<DictInfo.packageList.size(); i++){

			DictInfo dictInfo = DictInfo.packageList.get(i);
			listGoodsGenre.add(dictInfo.getPropertyValue());
		}

		for(int i=0; i<DictInfo.payList.size(); i++){

			DictInfo dictInfo = DictInfo.payList.get(i);
			listPayType.add(dictInfo.getPropertyValue());
		}
	}

	@Override
	public void onResume(){
		super.onResume();

		edtType.setText(mBillInfo.getServicePatternName());
		edtGenre.setText(mBillInfo.getPackageKindName());
		edtCount.setText(mBillInfo.getPieceNum());
		edtWeight.setText(mBillInfo.getTotalWeight());
		edtV3.setText(mBillInfo.getTotalVolume());
		edtFee.setText(mBillInfo.getFreight());
		edtFee2.setText(mBillInfo.getAgencyFund());
		edtFee3.setText(mBillInfo.getPremiumFee());

		edtPayType.setText(mBillInfo.getPayModeName());

		if(mBillInfo.getBeIntoWarehouse().equals("1")){
			checkBox1.setChecked(true);
		}else{
			checkBox1.setChecked(false);
		}

		if(!TextUtils.isEmpty(mBillInfo.getReceiptCode())){
			edtBackBill.setText(mBillInfo.getReceiptCode());
			checkBox2.setChecked(true);
		}else{
			checkBox2.setChecked(false);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == 0x1001 && resultCode == RESULT_OK){

			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	/**
	 * 服务方式
	 * @param v
	 */
	public void serviceType(View v){

		SingleItemDialog.showDialog(this, "选择服务方式", true, listServiceType, new SingleItemCallBack() {

			@Override
			public void callback(int pos) {
				// TODO Auto-generated method stub
				edtType.setText(listServiceType.get(pos).toString());

				mBillInfo.setServicePatternName(DictInfo.dispList.get(pos).getPropertyValue());
				mBillInfo.setServicePatternPcode(DictInfo.dispList.get(pos).getPropertyGcode());
			}
		});
	}

	/**
	 * 货物类别
	 * @param v
	 */
	public void goodsGenre(View v){

		SingleItemDialog.showDialog(this, "选择物品类别", true, listGoodsGenre, new SingleItemCallBack() {

			@Override
			public void callback(int pos) {
				// TODO Auto-generated method stub
				edtGenre.setText(listGoodsGenre.get(pos).toString());

				mBillInfo.setPackageKindName(DictInfo.packageList.get(pos).getPropertyValue());
				mBillInfo.setPackageKindPcode(DictInfo.packageList.get(pos).getPropertyGcode());
			}
		});
	}

	/**
	 * 支付方式
	 * @param v
	 */
	public void payType(View v){

		SingleItemDialog.showDialog(this, "选择支付方式", true, listPayType, new SingleItemCallBack() {

			@Override
			public void callback(int pos) {
				// TODO Auto-generated method stub
				edtPayType.setText(listPayType.get(pos).toString());

				mBillInfo.setPayModeName(DictInfo.payList.get(pos).getPropertyValue());
				mBillInfo.setPayModePcode(DictInfo.payList.get(pos).getPropertyGcode());
			}
		});
	}

	/**
	 * 子单件数
	 * @param v
	 */
	public void childBill(View v){

		String count = edtCount.getText().toString();
		if(TextUtils.isEmpty(count)){
			CommandTools.showToast("请先输入子单数量");
			return;
		}

		if(Integer.parseInt(count) == 0){
			CommandTools.showToast("件数必须大于0");
			return;
		}

		mBillInfo.setPieceNum(count);

		Intent intent = new Intent(this, ChildBillActivity.class);
		startActivity(intent);
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

		saveCurrentData();

		if(TextUtils.isEmpty(mBillInfo.getServicePatternName())){
			CommandTools.showToast("请选择服务方式");
			return;
		}

		if(TextUtils.isEmpty(mBillInfo.getPackageKindName())){
			CommandTools.showToast("请选择物品类别");
			return;
		}

		if(TextUtils.isEmpty(mBillInfo.getPieceNum())){
			CommandTools.showToast("请输入件数");
			return;
		}

		if(TextUtils.isEmpty(mBillInfo.getSubBillcode())){
			CommandTools.showToast("子单不能为空");
			return;
		}

		int pieckNum = Integer.parseInt(mBillInfo.getPieceNum());
		if(pieckNum == 1 && !mBillInfo.getSubBillcode().contains(",")){

		}else{
			String[] arrBill = mBillInfo.getSubBillcode().split(",");
			if(pieckNum != arrBill.length){
				CommandTools.showDialog(InputBill2Activity.this, "子单件数和子单实际录入数不一致");
				return;
			}
		}

		if(TextUtils.isEmpty(mBillInfo.getTotalWeight())){
			CommandTools.showToast("总重量不能为空");
			return;
		}

		if(TextUtils.isEmpty(mBillInfo.getTotalVolume())){
			CommandTools.showToast("总体积不能为空");
			return;
		}

		if(TextUtils.isEmpty(mBillInfo.getFreight())){
			CommandTools.showToast("请输入运费");
			return;
		}

		if(TextUtils.isEmpty(mBillInfo.getPayModeName())){
			CommandTools.showToast("请选择支付方式");
			return;
		}

		if(checkBox2.isChecked()){

			if(TextUtils.isEmpty(mBillInfo.getReceiptCode())){
				CommandTools.showToast("请输入回单号");
				return;
			}
		}

		if(checkBox2.isChecked() && !RegularUtil.checkBackBill(mBillInfo.getReceiptCode())){
			CommandTools.showToast("回单号不符合规则");
			return;
		}

		String data = "sendSiteGCode=" + MyApplication.mUser.getOwnSiteGcode()
				+ "&destSiteGCode=" + mBillInfo.getDestSiteGcode()
				+ "&weight=" + edtWeight.getText().toString()
				+ "&volume=" + edtV3.getText().toString();

		PresenterUtil.route_getRouteByParam(this, data, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				if(success){

					if(data == null){
						return;
					}

					boolean flag = (Boolean) data;
					if(flag){
						Intent intent = new Intent(InputBill2Activity.this, InputBill3Activity.class);
						startActivityForResult(intent, 0x1001);
					}else{
						CommandTools.showToast("路由为空，请选择其他派件网点");
					}

				}
			}
		});

	}

	/**
	 * 保存当前界面数据
	 */
	public void saveCurrentData(){

		mBillInfo.setPieceNum(edtCount.getText().toString());

		mBillInfo.setTotalWeight(edtWeight.getText().toString());
		mBillInfo.setTotalVolume(edtV3.getText().toString());
		mBillInfo.setFreight(edtFee.getText().toString());
		mBillInfo.setAgencyFund(edtFee2.getText().toString());
		mBillInfo.setPremiumFee(edtFee3.getText().toString());

		mBillInfo.setReceiptCode(edtBackBill.getText().toString());//回单

		if(checkBox1.isChecked()){
			mBillInfo.setBeIntoWarehouse("1");
		}
	}

	@Override
	public void onStop(){
		super.onStop();

		saveCurrentData();
	}

	/**
	 * 提交
	 * @param v
	 */
	public void submit(View v){

	}


}
