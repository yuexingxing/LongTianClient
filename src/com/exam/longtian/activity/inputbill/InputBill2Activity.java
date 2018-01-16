package com.exam.longtian.activity.inputbill;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.exam.longtian.ChildBillActivity;
import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.entity.DictInfo;
import com.exam.longtian.presenter.PresenterUtil;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.exam.longtian.view.SingleItemDialog;
import com.exam.longtian.view.SingleItemDialog.SingleItemCallBack;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

/** 
 * 录单2
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
	@ViewInject(R.id.input_bill2_pay_type) EditText edtPayType;
	
	@ViewInject(R.id.input_bill2_check1) CheckBox checkBox1;

	private List<String> listServiceType = new ArrayList<String>();
	private List<String> listGoodsGenre = new ArrayList<String>();
	private List<String> listPayType = new ArrayList<String>();

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_input_bill2);
		ViewUtils.inject(this);
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
					InputBillActivity.mBillInfo.setBeIntoWarehouse("1");
				}else{
					InputBillActivity.mBillInfo.setBeIntoWarehouse("0");
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

				InputBillActivity.mBillInfo.setServicePatternName(DictInfo.dispList.get(pos).getPropertyValue());
				InputBillActivity.mBillInfo.setServicePatternPcode(DictInfo.dispList.get(pos).getPropertyGcode());
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

				InputBillActivity.mBillInfo.setPackageKindName(DictInfo.packageList.get(pos).getPropertyValue());
				InputBillActivity.mBillInfo.setPackageKindPcode(DictInfo.packageList.get(pos).getPropertyGcode());
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

				InputBillActivity.mBillInfo.setPayModeName(DictInfo.payList.get(pos).getPropertyValue());
				InputBillActivity.mBillInfo.setPayModePcode(DictInfo.payList.get(pos).getPropertyGcode());
			}
		});
	}

	/**
	 * 子单
	 * @param v
	 */
	public void childBill(View v){

		Intent intent = new Intent(this, ChildBillActivity.class);
		startActivity(intent);
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

		BillInfo billInfo = InputBillActivity.mBillInfo;
		billInfo.setPieceNum(edtCount.getText().toString());
		billInfo.setTotalWeight(edtWeight.getText().toString());
		billInfo.setTotalVolume(edtV3.getText().toString());
		billInfo.setGoodsFlowTypePcode("P80");

		if(TextUtils.isEmpty(billInfo.getServicePatternName())){
			CommandTools.showToast("请选择服务方式");
			return;
		}

		if(TextUtils.isEmpty(billInfo.getPackageKindName())){
			CommandTools.showToast("请选择包装类别");
			return;
		}

		if(TextUtils.isEmpty(billInfo.getPieceNum())){
			CommandTools.showToast("请输入件数");
			return;
		}

		if(TextUtils.isEmpty(billInfo.getPayModeName())){
			CommandTools.showToast("请选择支付方式");
			return;
		}

		if(TextUtils.isEmpty(billInfo.getTotalWeight())){
			CommandTools.showToast("总重量不能为空");
			return;
		}

		if(TextUtils.isEmpty(billInfo.getTotalVolume())){
			CommandTools.showToast("总体积不能为空");
			return;
		}

		String data = "goodsFlowType=" + "P80"
				+ "&sendSiteGCode=" + MyApplication.mUser.getOwnSiteGcode()
				+ "&destSiteGCode=" + billInfo.getDestSiteGcode()
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
					
					try {
						JSONObject jsonObject = new JSONObject(data.toString());
						
						String routeId = jsonObject.optString("routeId");
						InputBillActivity.mBillInfo.setRouteId(routeId);

						Intent intent = new Intent(InputBill2Activity.this, InputBill3Activity.class);
						startActivity(intent);
						finish();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});


	}

	/**
	 * 提交
	 * @param v
	 */
	public void submit(View v){

	}


}
