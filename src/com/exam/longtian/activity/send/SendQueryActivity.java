package com.exam.longtian.activity.send;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.exam.longtian.R;
import com.exam.longtian.R.id;
import com.exam.longtian.R.layout;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.activity.inputbill.SiteListActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.SiteInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

/** 
 * 发件查询
 * 
 * @author yxx
 *
 * @date 2017-11-29 下午4:40:31
 * 
 */
public class SendQueryActivity extends BaseActivity {

	@ViewInject(R.id.send_query_time) EditText edtTime;
	@ViewInject(R.id.send_query_sitename) EditText edtSiteName;
	@ViewInject(R.id.send_query_billcode) EditText edtBillcode;
	@ViewInject(R.id.send_query_join_billcode) EditText edtJoinBillcode;

	@ViewInject(R.id.lv_public) ListView listView;
	List<SiteInfo> dataList = new ArrayList<SiteInfo>();
	CommonAdapter<SiteInfo> commonAdapter;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_send_query);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		setTitle("发件查询");

		for(int i=0; i<20; i++){

			SiteInfo user = new SiteInfo();
			user.setName(i + "网点");
			dataList.add(user);
		}

		commonAdapter = new CommonAdapter<SiteInfo>(this, dataList, R.layout.item_layout_send_query) {

			@Override
			public void convert(ViewHolder helper, SiteInfo item) {

				helper.setText(R.id.item_layout_sendquery_billcode, item.getName());
			}
		};

		listView.setAdapter(commonAdapter);
	}

	@Override
	public void initData() {

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == 0x0001 && resultCode == RESULT_OK) {
			edtSiteName.setText(data.getStringExtra("name"));
		}
	}

	/**
	 * 选择时间
	 * @param v
	 */
	public void time(View v){

		Calendar c = Calendar.getInstance();

		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {  

			@Override  
			public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {  

				edtTime.setText(String.format("%d-%d-%d",i,i1+1,i2));  
			}  

		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();  
	}

	/**
	 * 下一站
	 * @param v
	 */
	public void nextStop(View v){

		Intent intent = new Intent(this, SiteListActivity.class);
		startActivityForResult(intent, 0x0001);
	}

	/**
	 * 交接单
	 * @param v
	 */
	public void joinBillcode(View v){

	}

	/**
	 * 查询
	 * @param v
	 */
	public void query(View v){


	}
}
