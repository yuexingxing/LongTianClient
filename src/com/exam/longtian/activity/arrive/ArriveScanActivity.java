package com.exam.longtian.activity.arrive;

import java.util.ArrayList;
import java.util.List;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.activity.inputbill.SiteListActivity;
import com.exam.longtian.activity.send.ChooseJoinBillActivity;
import com.exam.longtian.activity.send.SendQueryActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.SiteInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.CompoundButton.OnCheckedChangeListener;

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
	@ViewInject(R.id.arrive_scan_prestop) EditText edtNextStop;
	@ViewInject(R.id.arrive_scan_man) EditText edtMan;
	@ViewInject(R.id.arrive_scan_billcode) EditText edtBillcode;
	@ViewInject(R.id.arrive_scan_check) CheckBox checkBox;

	@ViewInject(R.id.lv_public) ListView listView;
	List<SiteInfo> dataList = new ArrayList<SiteInfo>();
	CommonAdapter<SiteInfo> commonAdapter;

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

		for(int i=0; i<20; i++){

			SiteInfo user = new SiteInfo();
			user.setName(i + "网点");
			dataList.add(user);
		}

		commonAdapter = new CommonAdapter<SiteInfo>(this, dataList, R.layout.item_layout_sendscan) {

			@Override
			public void convert(ViewHolder helper, SiteInfo item) {

				helper.setText(R.id.item_layout_sendscan_content, item.getName());
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

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == 0x0001 && resultCode == RESULT_OK) {
			edtNextStop.setText(data.getStringExtra("name"));
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

		Intent intent = new Intent(this, ChooseJoinBillActivity.class);
		startActivity(intent);
	}

	/**
	 * 查询
	 * @param v
	 */
	public void query(View v){

		startActivity((new Intent(this, SendQueryActivity.class)));
	}

	/**
	 * 清空
	 * @param v
	 */
	public void clear(View v){

	}
}
