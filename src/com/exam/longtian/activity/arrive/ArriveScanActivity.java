package com.exam.longtian.activity.arrive;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.activity.inputbill.SiteListActivity;
import com.exam.longtian.activity.send.ChooseJoinBillActivity;
import com.exam.longtian.activity.send.SendQueryActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.presenter.PresenterUtil;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/** 
 * 到件扫描
 * 
 * @author yxx
 *
 * @date 2017-12-1 下午1:56:22
 * 
 */
public class ArriveScanActivity extends BaseActivity {

	@ViewInject(R.id.arrive_scan_join_billcode) EditText edtJoinBillcode;
	@ViewInject(R.id.arrive_scan_prestop) EditText edtPreStop;
	@ViewInject(R.id.arrive_scan_man) EditText edtMan;
	@ViewInject(R.id.arrive_scan_billcode) EditText edtBillcode;
	@ViewInject(R.id.arrive_scan_check) CheckBox checkBox;

	@ViewInject(R.id.lv_public) ListView listView;
	List<BillInfo> dataList = new ArrayList<BillInfo>();
	CommonAdapter<BillInfo> commonAdapter;

	private String siteGCode;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentViewId(R.layout.activity_arrive_scan);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("到件扫描");

		commonAdapter = new CommonAdapter<BillInfo>(this, dataList, R.layout.item_layout_sendscan) {

			@Override
			public void convert(ViewHolder helper, BillInfo item) {

				helper.setText(R.id.item_layout_sendscan_content, item.getBillCode());
			}
		};

		listView.setAdapter(commonAdapter);

		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		edtMan.setText(MyApplication.mUser.getEmpName());

		PresenterUtil.waybillSub_unscanedSendWaybillSubList(this, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				commonAdapter.notifyDataSetChanged();
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == 0x0001 && resultCode == RESULT_OK) {
			edtPreStop.setText(data.getStringExtra("name"));
			siteGCode = data.getStringExtra("code");
		}
	}

	/**
	 * 下一站
	 * @param v
	 */
	public void preStop(View v){

		Intent intent = new Intent(this, SiteListActivity.class);
		startActivityForResult(intent, 0x0001);
	}

	/**
	 * 确定
	 * @param v
	 */
	public void commit(View v){

		if(TextUtils.isEmpty(siteGCode)){

			CommandTools.showToast("请选择上一站网点");
			return;
		}

		int len = dataList.size();
		if(len == 0){
			CommandTools.showToast("请先录入数据");
			return;
		}

		StringBuilder sb = new StringBuilder();
		for(int i=0; i<len; i++){

			BillInfo billInfo = dataList.get(i);
			if(TextUtils.isEmpty(sb.toString())){
				sb.append(billInfo.getBillCode());
			}else{
				sb.append(",").append(billInfo.getBillCode());
			}
		}

		Intent intent = new Intent(this, ChooseJoinBillActivity.class);
		intent.putExtra("billcodes", sb.toString());
		intent.putExtra("siteGCode", siteGCode);
		intent.putExtra("order_type", PresenterUtil.ORDER_TYPE_ARRIVE);
		intent.putExtra("siteName", edtPreStop.getText().toString());
		startActivity(intent);
	}

	/**
	 * 查询
	 * @param v
	 */
	public void query(View v){

		startActivity((new Intent(this, SendQueryActivity.class)));
	}

	public void save(View v){

		String billcode = edtBillcode.getText().toString();
		String nextStop = edtPreStop.getText().toString();

		if(TextUtils.isEmpty(billcode) || billcode.length() != 14){
			CommandTools.showToast("请输入14位运单号");
			return;
		}

		if(checkBox.isChecked()){

			int len = dataList.size();
			for(int i=0; i<len; i++){

				BillInfo billInfo2 = dataList.get(i);
				if(billInfo2.getBillCode().equals(billcode)){

					dataList.remove(i);
					commonAdapter.notifyDataSetChanged();
					edtBillcode.setText("");
					break;
				}
			}
		}else{

			if(TextUtils.isEmpty(nextStop)){
				CommandTools.showToast("请选择站点");
				return;
			}

			BillInfo billInfo = new BillInfo();
			billInfo.setBillCode(billcode);
			billInfo.setDestSiteGcode(nextStop);

			dataList.add(billInfo);
			commonAdapter.notifyDataSetChanged();

			edtBillcode.setText("");
		}

	}

	/**
	 * 清空
	 * @param v
	 */
	public void clear(View v){

	}
}
