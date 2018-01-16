package com.exam.longtian.activity.send;

import java.util.ArrayList;
import java.util.List;

import com.exam.longtian.R;
import com.exam.longtian.R.layout;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.SiteInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/** 
 * 发件对比结果
 * 
 * @author yxx
 *
 * @date 2017-12-1 上午10:34:29
 * 
 */
public class SendCompareActivity extends BaseActivity {

	@ViewInject(R.id.send_compare_nextstop) EditText edtNextStop;
	@ViewInject(R.id.send_compare_name) EditText edtName;

	@ViewInject(R.id.lv_public) ListView listView;
	List<SiteInfo> dataList = new ArrayList<SiteInfo>();
	CommonAdapter<SiteInfo> commonAdapter;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_send_compare);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("发件对比结果");

		for(int i=0; i<5; i++){

			SiteInfo user = new SiteInfo();
			user.setName(i + "网点");
			dataList.add(user);
		}

		SiteInfo info = new SiteInfo();
		info.setName("新建");
		dataList.add(dataList.size(), info);

		commonAdapter = new CommonAdapter<SiteInfo>(this, dataList, R.layout.item_layout_send_compare) {

			@Override
			public void convert(ViewHolder helper, SiteInfo item) {

				helper.setText(R.id.item_layout_sendcompare_billcode, item.getName());
				helper.setText(R.id.item_layout_sendcompare_backbillcode, item.getName());

				//				if(item.isFlag()){
				//					helper.setImageBackground(R.id.item_layout_sendcompare_flag, R.drawable.checkbox_sel);
				//				}else{
				//					helper.setImageBackground(R.id.item_layout_sendcompare_flag, R.drawable.checkbox_bg);
				//				}
			}
		};

		listView.setAdapter(commonAdapter);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	/**
	 * 继续
	 * @param v
	 */
	public void commit(View v){

	}

	/**
	 * 结束
	 * @param v
	 */
	public void back(View v){
		finish();
	}
}
