package com.exam.longtian.activity.send;

import java.util.ArrayList;
import java.util.List;

import com.exam.longtian.R;
import com.exam.longtian.R.layout;
import com.exam.longtian.R.menu;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.activity.inputbill.SiteListActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.SiteInfo;
import com.exam.longtian.util.Res;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/** 
 * 选择交接单
 * 
 * @author yxx
 *
 * @date 2017-12-1 上午10:04:17
 * 
 */
public class ChooseJoinBillActivity extends BaseActivity {

	@ViewInject(R.id.lv_public) ListView listView;
	List<SiteInfo> dataList = new ArrayList<SiteInfo>();
	CommonAdapter<SiteInfo> commonAdapter;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentViewId(R.layout.activity_choose_join_bill);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("选择交接单");

		for(int i=0; i<5; i++){

			SiteInfo user = new SiteInfo();
			user.setName(i + "网点");
			dataList.add(user);
		}

		SiteInfo info = new SiteInfo();
		info.setName("新建");
		dataList.add(dataList.size(), info);

		commonAdapter = new CommonAdapter<SiteInfo>(this, dataList, R.layout.item_layout_choose_joinbill) {

			@Override
			public void convert(ViewHolder helper, SiteInfo item) {

				helper.setText(R.id.item_layout_choose_joinbill_billcode, item.getName());
				helper.setText(R.id.item_layout_choose_joinbill_name, item.getName());

				if(item.isFlag()){
					helper.setImageBackground(R.id.item_layout_choose_joinbill_flag, R.drawable.checkbox_sel);
				}else{
					helper.setImageBackground(R.id.item_layout_choose_joinbill_flag, R.drawable.checkbox_bg);
				}
			}
		};

		listView.setAdapter(commonAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				SiteInfo info = dataList.get(arg2);
				for(int i =0; i<dataList.size(); i++){

					dataList.get(i).setFlag(false);
				}

				info.setFlag(true);
				commonAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	public void bind(View v){

		Intent intent = new Intent(this, SendCompareActivity.class);
		startActivity(intent);
		finish();
	}

}
