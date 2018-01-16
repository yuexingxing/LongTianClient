package com.exam.longtian;

import java.util.ArrayList;
import java.util.List;

import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.activity.inputbill.InputBillActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.SiteInfo;
import com.exam.longtian.entity.SubBillInfo;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.Res;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
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

	private int currPos = -1;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentViewId(R.layout.activity_child_bill);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("子单");

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
		
		SubBillInfo subBillInfo = new SubBillInfo();
		subBillInfo.setBillcode(billcode);
		subBillInfo.setFlag(false);

		dataList.add(subBillInfo);
		commonAdapter.notifyDataSetChanged();
		
		edtBillcode.setText("");
	}

	/**
	 * 生成全部
	 * @param v
	 */
	public void addAll(View v){


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
	}

	/**
	 * 清除
	 * @param v
	 */
	public void clear(View v){

		dataList.clear();
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

		dataList.clear();
		dataList = null;
		InputBillActivity.mBillInfo.setSubBillcode(sb.toString());
		finish();
	}

	/**
	 * 退出
	 * @param v
	 */
	public void exit(View v){

		finish();
	}

}
