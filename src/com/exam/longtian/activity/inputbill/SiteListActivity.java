package com.exam.longtian.activity.inputbill;

import java.util.ArrayList;
import java.util.List;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.SiteInfo;
import com.exam.longtian.interfac.ISite;
import com.exam.longtian.presenter.PSite;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.Res;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/** 
 * 网点列表选择
 * 
 * @author yxx
 *
 * @date 2017-11-28 下午5:05:47
 * 
 */
public class SiteListActivity extends BaseActivity {

	@ViewInject(R.id.lv_public) ListView listView;
	List<SiteInfo> dataList = new ArrayList<SiteInfo>();
	CommonAdapter<SiteInfo> commonAdapter;

	private int currPos = -1;//当前选择的位置

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentViewId(R.layout.activity_site_list);
		ViewUtils.inject(this);

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("网点选择");

		commonAdapter = new CommonAdapter<SiteInfo>(this, dataList, R.layout.item_layout_site_list) {

			@Override
			public void convert(ViewHolder helper, SiteInfo item) {

				helper.setText(R.id.item_layout_sitelist_no, item.getSiteCode());
				helper.setText(R.id.item_layout_sitelist_name, item.getSiteName());
				if(item.isFlag()){
					helper.setLayoutResource(R.id.item_layout_sitelist_top, Res.getColor(R.color.blue));
				}else{
					helper.setLayoutResource(R.id.item_layout_sitelist_top, Res.getColor(R.color.transparent));
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
		PSite.site_list(this, new ISite() {

			@Override
			public void success(List<SiteInfo> list) {
				// TODO Auto-generated method stub
				dataList.clear();
				dataList.addAll(list);

				mHandler.sendEmptyMessage(0x0011);
			}

			@Override
			public void failed() {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 选中
	 * @param v
	 */
	public void submit(View v){

		if(currPos < 0){
			CommandTools.showToast("请选择一条数据");
			return;
		}

		Intent intent = new Intent();
		intent.putExtra("code", dataList.get(currPos).getSiteGcode());
		intent.putExtra("name", dataList.get(currPos).getSiteName());
		setResult(RESULT_OK, intent);
		finish();
	}

	/**
	 * 关闭
	 * @param v
	 */
	public void back(View v){
		finish();
	}

	public Handler mHandler = new Handler(){

		public void handleMessage(Message msg){

			if(msg.what == 0x0011){
				commonAdapter.notifyDataSetChanged();
			}
		}
	};
}
