package com.exam.longtian.activity;

import java.util.ArrayList;
import java.util.List;

import com.exam.longtian.R;
import com.exam.longtian.R.color;
import com.exam.longtian.R.id;
import com.exam.longtian.R.layout;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.SiteInfo;
import com.exam.longtian.interfac.ISite;
import com.exam.longtian.presenter.PSite;
import com.exam.longtian.presenter.PresenterUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.exam.longtian.util.Res;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/** 
 * 司机列表
 * 
 * @author yxx
 *
 * @date 2018-1-23 下午1:53:07
 * 
 */
public class DriverListActivity extends BaseActivity {

	@ViewInject(R.id.lv_public) ListView listView;
	List<SiteInfo> dataList = new ArrayList<SiteInfo>();
	CommonAdapter<SiteInfo> commonAdapter;

	private int currPos = -1;//当前选择的位置

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_driver_list);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		commonAdapter = new CommonAdapter<SiteInfo>(this, dataList, R.layout.item_layout_driver) {

			@Override
			public void convert(ViewHolder helper, SiteInfo item) {

				helper.setText(R.id.item_layout_driver_no, item.getSiteCode());
				helper.setText(R.id.item_layout_driver_name, item.getSiteName());
				if(item.isFlag()){
					helper.setLayoutResource(R.id.item_layout_driver_top, Res.getColor(R.color.blue));
				}else{
					helper.setLayoutResource(R.id.item_layout_driver_top, Res.getColor(R.color.transparent));
				}
			}
		};

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				SiteInfo info = dataList.get(arg2);

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

		PresenterUtil.driver_list(this, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				
			}
		});
	}

}
