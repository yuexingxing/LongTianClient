package com.exam.longtian.activity.inputbill;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.CustomInfo;
import com.exam.longtian.entity.SiteInfo;
import com.exam.longtian.presenter.PCustom;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
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
 * 客户选择
 * 
 * @author yxx
 *
 * @date 2017-11-29 下午2:33:44
 * 
 */
public class CustomerListActivity extends BaseActivity {

	@ViewInject(R.id.lv_public) ListView listView;
	List<CustomInfo> dataList = new ArrayList<CustomInfo>();
	CommonAdapter<CustomInfo> commonAdapter;

	private int currPos = -1;//当前选择的位置

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_customer_list);
		ViewUtils.inject(this);

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

		commonAdapter = new CommonAdapter<CustomInfo>(this, dataList, R.layout.item_layout_customer_list) {

			@Override
			public void convert(ViewHolder helper, CustomInfo item) {

				helper.setText(R.id.item_layout_customerlist_no, item.getCustName());
				helper.setText(R.id.item_layout_customerlist_name, item.getOpEmpName());
				if(item.isFlag()){
					helper.setLayoutResource(R.id.item_layout_customerlist_top, Res.getColor(R.color.blue));
				}else{
					helper.setLayoutResource(R.id.item_layout_customerlist_top, Res.getColor(R.color.transparent));
				}
			}
		};

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				CustomInfo info = dataList.get(arg2);

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

		PCustom.customer_list(this, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub
				dataList.clear();
				if(data != null){

					dataList.addAll((List<CustomInfo>) data);
					mHandler.sendEmptyMessage(0x0011);
				}
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
		intent.putExtra("linkMan", dataList.get(currPos).getCustLinkman());
		intent.putExtra("customer", dataList.get(currPos).getCustName());
		intent.putExtra("phone", dataList.get(currPos).getCustPhone());
		intent.putExtra("code", dataList.get(currPos).getCustGcode());
		intent.putExtra("address", dataList.get(currPos).getAddress());
		intent.putExtra("companyName", dataList.get(currPos).getCustCompanyName());
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
