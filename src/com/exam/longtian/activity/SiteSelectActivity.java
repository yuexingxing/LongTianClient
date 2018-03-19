package com.exam.longtian.activity;

import java.util.ArrayList;
import java.util.List;

import com.exam.longtian.R;
import com.exam.longtian.R.drawable;
import com.exam.longtian.R.id;
import com.exam.longtian.R.layout;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.SiteInfo;
import com.exam.longtian.presenter.PresenterUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/** 
 * ÍøµãÑ¡Ôñ
 * 
 * @author yxx
 *
 * @date 2017-11-28 ÉÏÎç10:57:27
 * 
 */
public class SiteSelectActivity extends Activity {

	@ViewInject(R.id.lv_public) ListView listView;
	List<SiteInfo> dataList = new ArrayList<SiteInfo>();
	CommonAdapter<SiteInfo> commonAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_site_select);
		ViewUtils.inject(this);

		initView();
		initData();
	}

	private void initView(){

		commonAdapter = new CommonAdapter<SiteInfo>(this, dataList, R.layout.item_layout_site) {

			@Override
			public void convert(ViewHolder helper, SiteInfo item) {
				// TODO Auto-generated method stub
				helper.setText(R.id.item_layout_site_name, item.getSiteName());
				if(item.isFlag()){
					helper.setImageResource(R.id.item_layout_site_icon, R.drawable.checkbox_sel);
				}else{
					helper.setImageResource(R.id.item_layout_site_icon, R.drawable.checkbox_bg);
				}
			}
		};

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				SiteInfo info = dataList.get(arg2);

				int len = dataList.size();
				for(int i=0; i<len; i++){
					dataList.get(i).setFlag(false);
				}

				info.setFlag(!info.isFlag());

				commonAdapter.notifyDataSetChanged();
			}
		});

		listView.setAdapter(commonAdapter);
	}

	public void initData(){


//		PresenterUtil.site_list(this, new ObjectCallback() {
//
//			@Override
//			public void callback(boolean success, String message, String code, Object data) {
//				// TODO Auto-generated method stub
//				dataList.clear();
//				dataList.addAll((List<SiteInfo>) data);
//				mHandler.sendEmptyMessage(0x0011);
//			}
//		});
	}

	public Handler mHandler = new Handler(){

		public void handleMessage(Message msg){

			if(msg.what == 0x0011){
				commonAdapter.notifyDataSetChanged();
			}
		}
	};

	public void login(View v){

		dataList.clear();
		dataList = null;

		Intent intent = new Intent(this, MainMenuActivity.class);
		startActivity(intent);
		finish();
	}
}
