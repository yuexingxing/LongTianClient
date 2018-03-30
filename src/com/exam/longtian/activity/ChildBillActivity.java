package com.exam.longtian.activity;

import java.util.ArrayList;
import java.util.List;
import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.camera.CaptureActivity;
import com.exam.longtian.entity.SubBillInfo;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.Constant;
import com.exam.longtian.util.RegularUtil;
import com.exam.longtian.util.Res;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/** 
 * 子单
 * 
 * @author yxx
 *
 * @date 2017-12-6 上午10:49:06
 * 
 */
public class ChildBillActivity extends BaseActivity {

	@ViewInject(R.id.child_bill_billcode) EditText edtBillcode;
	@ViewInject(R.id.child_bill_checkbox) CheckBox checkBox;
	@ViewInject(R.id.child_bill_count1) EditText edtCount1;
	@ViewInject(R.id.child_bill_count2) EditText edtCount2;

	@ViewInject(R.id.lv_public) ListView listView;
	List<SubBillInfo> dataList = new ArrayList<SubBillInfo>();
	CommonAdapter<SubBillInfo> commonAdapter;

	private int count;
	private int currPos = -1;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentViewId(R.layout.activity_child_bill);
		ViewUtils.inject(this);
		
		MyApplication.getEventBus().register(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("子单");

		String[] arrBill = new String[0];
		if(!TextUtils.isEmpty(MainMenuActivity.mBillInfo.getSubBillcode())){
			arrBill = MainMenuActivity.mBillInfo.getSubBillcode().split(",");
		}

		for(int i=0; i<arrBill.length; i++){

			SubBillInfo info = new SubBillInfo();
			info.setBillcode(arrBill[i]);
			info.setFlag(false);

			dataList.add(info);
		}

		count = Integer.parseInt(MainMenuActivity.mBillInfo.getPieceNum());
		edtCount1.setText(count + "");
		edtCount2.setText(dataList.size() + "");

		commonAdapter = new CommonAdapter<SubBillInfo>(this, dataList, R.layout.item_layout_sendscan) {

			@Override
			public void convert(ViewHolder helper, SubBillInfo item) {

				helper.setText(R.id.item_layout_sendscan_content, item.getBillcode());
				if(item.isFlag()){
					helper.setLayoutResource(R.id.item_layout_sendscan, Res.getColor(R.color.blue));
				}else{
					helper.setLayoutResource(R.id.item_layout_sendscan, Res.getColor(R.color.transparent));
				}
			}
		};

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				SubBillInfo info = dataList.get(arg2);

				currPos = arg2;
				for(int i=0; i<dataList.size(); i++){
					dataList.get(i).setFlag(false);
				}

				info.setFlag(true);

				commonAdapter.notifyDataSetChanged();
			}
		});

		listView.setAdapter(commonAdapter);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constant.CAPTURE_BILLCODE && resultCode == RESULT_OK) {

			if(data == null){
				return;
			}

			Bundle bundle = data.getExtras();
			String strBillcode = bundle.getString("result");
			edtBillcode.setText(strBillcode);
			add(null);
		}
	}

	public void scan(View v){

		Intent openCameraIntent = new Intent(this, CaptureActivity.class);
		startActivityForResult(openCameraIntent, Constant.CAPTURE_BILLCODE);
	}

	/**
	 * 增加
	 * @param v
	 */
	public void add(View v){

		String billcode = edtBillcode.getText().toString();
		if(TextUtils.isEmpty(billcode)){
			CommandTools.showToast("单号不能为空");
			return;
		}

		if(!RegularUtil.checkChildBill(billcode)){
			CommandTools.showToast("单号不符合规则");
			return;
		}

		save(billcode, 0);
	}

	/**
	 * 生成全部
	 * @param v
	 */
	public void addAll(View v){

		String billcode = edtBillcode.getText().toString();
		if(TextUtils.isEmpty(billcode)){
			CommandTools.showToast("单号不能为空");
			return;
		}

		if(!RegularUtil.checkChildBill(billcode)){
			CommandTools.showToast("单号不符合规则");
			return;
		}

		String strBillcode = billcode.substring(0, 14);
		String strChildBillcode = billcode.substring(14, billcode.length());
		int childBill = Integer.parseInt(strChildBillcode);

		for(int i=0; i<count; i++){

			if(childBill < 10){
				strChildBillcode = "000" + childBill;
			}else if(childBill < 100){
				strChildBillcode = "00" + childBill;
			}else if(childBill < 1000){
				strChildBillcode = "0" + childBill;
			}

			save(strBillcode + strChildBillcode, 1);
			childBill++;
		}

	}

	/**
	 * 保存到本地列表
	 * @param billcode
	 */
	public void save(String billcode, int type){

		int len = dataList.size();
		for(int i=0; i<len; i++){

			SubBillInfo subBillInfo = dataList.get(i);
			if(subBillInfo.getBillcode().equals(billcode)){
				CommandTools.showDialog(ChildBillActivity.this, "子单号[" + billcode + "]已存在");
				return;
			}
		}

		SubBillInfo subBillInfo = new SubBillInfo();
		subBillInfo.setBillcode(billcode);
		subBillInfo.setFlag(false);

		dataList.add(subBillInfo);
		commonAdapter.notifyDataSetChanged();

		if(type == 0 && checkBox.isChecked()){

			String strBillcode = billcode.substring(0, 14);
			String strChildBillcode = billcode.substring(14, billcode.length());
			int childBill = Integer.parseInt(strChildBillcode);

			childBill++;
			if(childBill < 10){
				strChildBillcode = "000" + childBill;
			}else if(childBill < 100){
				strChildBillcode = "00" + childBill;
			}else if(childBill < 1000){
				strChildBillcode = "0" + childBill;
			}

			edtBillcode.setText(strBillcode + strChildBillcode);
		}else{
			edtBillcode.setText("");
		}

		edtCount2.setText(dataList.size() + "");
	}

	/**
	 * 删除
	 * @param v
	 */
	public void delete(View v){

		if(currPos >= 0){
			dataList.remove(currPos);
		}

		currPos = currPos - 1;
		if(currPos >= 0){
			dataList.get(currPos).setFlag(true);
		}

		commonAdapter.notifyDataSetChanged();
		edtCount2.setText(dataList.size() + "");
	}

	/**
	 * 清除
	 * @param v
	 */
	public void clear(View v){

		dataList.clear();
		edtCount2.setText(dataList.size() + "");
		commonAdapter.notifyDataSetChanged();
	}

	/**
	 * 确定
	 * @param v
	 */
	public void commit(View v){

		StringBuilder sb = new StringBuilder();
		int len = dataList.size();
		for(int i=0; i<len; i++){

			SubBillInfo subBillInfo = dataList.get(i);
			if(TextUtils.isEmpty(sb.toString())){
				sb.append(subBillInfo.getBillcode());
			}else{
				sb.append(",").append(subBillInfo.getBillcode());
			}
		}

		MainMenuActivity.mBillInfo.setPieceNum(dataList.size() + "");
		MainMenuActivity.mBillInfo.setSubBillcode(sb.toString());

		dataList.clear();
		dataList = null;
		finish();
	}

	/**
	 * 退出
	 * @param v
	 */
	public void exit(View v){

		finish();
	}

	/* (non-Javadoc)
	 * @see com.exam.longtian.activity.BaseActivity#onEventMainThread(android.os.Message)
	 */
	public void onEventMainThread(Message msg) {

		if(msg.what == Constant.SCANNER_BILLCODE){

			String billcode = (String) msg.obj;
			edtBillcode.setText(billcode);
			add(null);
		}
	}
}
