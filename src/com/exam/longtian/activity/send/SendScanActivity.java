package com.exam.longtian.activity.send;

import java.util.ArrayList;
import java.util.List;
import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.activity.inputbill.SiteListActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.camera.CaptureActivity;
import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.entity.ChildBillInfo;
import com.exam.longtian.presenter.PresenterUtil;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.CommandTools.CommandToolsCallback;
import com.exam.longtian.util.Constant;
import com.exam.longtian.util.RegularUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;

/** 
 * 发件扫描
 * 
 * @author yxx
 *
 * @date 2017-11-29 下午3:39:18
 * 
 */
public class SendScanActivity extends BaseActivity{

	@ViewInject(R.id.send_scan_nextstop) EditText edtNextStop;
	@ViewInject(R.id.send_scan_man) EditText edtMan;
	@ViewInject(R.id.send_scan_billcode) EditText edtBillcode;
	@ViewInject(R.id.send_scan_check) CheckBox checkBox;

	@ViewInject(R.id.lv_public) ListView listView;
	List<BillInfo> dataList = new ArrayList<BillInfo>();
	CommonAdapter<BillInfo> commonAdapter;

	private String siteGCode;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_send_scan);
		ViewUtils.inject(this);

		MyApplication.getEventBus().register(this);
	}

	@Override
	public void initView() {
		setTitle("发件扫描");

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

		edtMan.setText(MyApplication.mUser.getEmpName());

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == 0x0001 && resultCode == RESULT_OK) {

			edtNextStop.setText(data.getStringExtra("name"));
			siteGCode = data.getStringExtra("code");
		}else if(requestCode == 0x1001 && resultCode == RESULT_OK){

			dataList.clear();
			commonAdapter.notifyDataSetChanged();

			String continu = data.getStringExtra("continu");
			if(continu.equals("1")){

			}else{
				finish();
			}
		}else if (requestCode == Constant.CAPTURE_BILLCODE && resultCode == RESULT_OK) {

			Bundle bundle = data.getExtras();
			String strBillcode = bundle.getString("result");
			edtBillcode.setText(strBillcode);

			save(null);
		}
	}

	public void scan(View v){

		Intent openCameraIntent = new Intent(SendScanActivity.this, CaptureActivity.class);
		startActivityForResult(openCameraIntent, Constant.CAPTURE_BILLCODE);
	}

	/**
	 * 下一站
	 * @param v张三
	 */
	public void nextStop(View v){

		Intent intent = new Intent(this, SiteListActivity.class);
		intent.putExtra("order_type", PresenterUtil.ORDER_TYPE_SEND);
		startActivityForResult(intent, 0x0001);
	}

	/**
	 * 确定
	 * @param v
	 */
	public void commit(View v){

		if(TextUtils.isEmpty(siteGCode)){

			CommandTools.showToast("请选择下一站网点");
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
		intent.putExtra("order_type", PresenterUtil.ORDER_TYPE_SEND);
		intent.putExtra("siteName", edtNextStop.getText().toString());
		startActivityForResult(intent, 0x1001);
	}

	/**
	 * 查询
	 * @param v
	 */
	public void query(View v){

		Intent intent = new Intent(this, SendQueryActivity.class);
		intent.putExtra("order_type", PresenterUtil.ORDER_TYPE_SEND);
		startActivity(intent);
	}

	public void checkSubBillData(String billcode){

		PresenterUtil.waybillSub_unscanedSendWaybillSubList(this, PresenterUtil.ORDER_TYPE_SEND, MyApplication.mUser.getOwnSiteGcode(), billcode, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				List<ChildBillInfo> list = (List<ChildBillInfo>) data;

				int len = list.size();
				for(int i=0; i<len ;i++){

					ChildBillInfo childBillInfo = list.get(i);

					BillInfo billInfo = new BillInfo();
					billInfo.setBillCode(childBillInfo.getSubBillCode());
					billInfo.setDestSiteGcode(siteGCode);

					dataList.add(billInfo);
				}

				commonAdapter.notifyDataSetChanged();
			}
		});
	}

	/**
	 * 保存
	 * @param v
	 */
	public void save(View v){

		String billcode = edtBillcode.getText().toString();
		String nextStop = edtNextStop.getText().toString();

		if(TextUtils.isEmpty(billcode)){
			CommandTools.showToast("请输入单号");
			return;
		}

		if(!RegularUtil.checkAllBill(billcode)){
			CommandTools.showToast("单号不符合规则");
			return;
		}

		if(checkBox.isChecked()){

			deleteData(billcode);
		}else{

			if(TextUtils.isEmpty(nextStop)){
				CommandTools.showToast("请选择站点");
				return;
			}

			BillInfo billInfo = new BillInfo();
			billInfo.setBillCode(billcode);
			billInfo.setDestSiteGcode(siteGCode);

			if(!checkExist(billcode)){

				dataList.add(billInfo);
				commonAdapter.notifyDataSetChanged();
				CommandTools.showToast("保存成功");

				//如果是主单，则需要调用接口查子单
				if(RegularUtil.checkBill(billcode)){
					checkSubBillData(billcode);
				}

			}else{
				CommandTools.showToast("单号已存在");
				return;
			}

			edtBillcode.setText("");
			CommandTools.requestFolcus(edtBillcode);
		}

	}

	/**
	 * 判断单号是否存在
	 * @param billcode
	 * @return
	 */
	public boolean checkExist(String billcode){

		boolean flag = false;

		int len = dataList.size();
		for(int i=0; i<len; i++){

			BillInfo billInfo2 = dataList.get(i);
			if(billInfo2.getBillCode().equals(billcode)){
				flag = true;
				break;
			}
		}

		return flag;
	}

	/**
	 * 删除列表数据
	 * @param billcode
	 */
	public void deleteData(String billcode){

		int len = dataList.size();
		for(int i=0; i<len; i++){

			BillInfo billInfo2 = dataList.get(i);
			if(billInfo2.getBillCode().equals(billcode)){

				dataList.remove(i);
				commonAdapter.notifyDataSetChanged();
				edtBillcode.setText("");
				CommandTools.showToast("删除成功");
				break;
			}
		}
	}

	/**
	 * 清空
	 * @param v
	 */
	public void clear(View v){

		if(dataList.size() == 0){
			return;
		}

		CommandTools.showChooseDialog(this, "确定清除当前列表数据？", new CommandToolsCallback() {

			@Override
			public void callback(int position) {
				// TODO Auto-generated method stub
				if(position == 0){

					dataList.clear();
					commonAdapter.notifyDataSetChanged();
					CommandTools.showToast("清除成功");
				}
			}
		});

	}

	/* (non-Javadoc)
	 * @see com.exam.longtian.activity.BaseActivity#onEventMainThread(android.os.Message)
	 */
	public void onEventMainThread(Message msg) {

		if(msg.what == Constant.SCANNER_BILLCODE){

			String billcode = (String) msg.obj;
			edtBillcode.setText(billcode);
			save(null);
		}
	}

}
