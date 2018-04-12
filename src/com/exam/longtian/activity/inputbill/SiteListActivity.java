package com.exam.longtian.activity.inputbill;

import java.util.ArrayList;
import java.util.List;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.activity.MainMenuActivity;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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

	@ViewInject(R.id.sitelist_search) EditText edtSearch;

	@ViewInject(R.id.lv_public) ListView listView;
	List<SiteInfo> dataList = new ArrayList<SiteInfo>();
	List<SiteInfo> sortList = new ArrayList<SiteInfo>();
	CommonAdapter<SiteInfo> commonAdapter;

	private int currPos = -1;//当前选择的位置
	private String orderType;

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

		commonAdapter = new CommonAdapter<SiteInfo>(this, sortList, R.layout.item_layout_site_list) {

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
				SiteInfo info = sortList.get(arg2);

				currPos = arg2;
				for(int i=0; i<sortList.size(); i++){
					sortList.get(i).setFlag(false);
				}

				info.setFlag(true);

				commonAdapter.notifyDataSetChanged();
			}
		});

		listView.setAdapter(commonAdapter);

		edtSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

				currPos = -1;
				sortData(arg0.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		orderType = getIntent().getStringExtra("order_type");
		PSite.site_list(this, orderType, new ISite() {

			@Override
			public void success(List<SiteInfo> list) {
				// TODO Auto-generated method stub
				sortList.clear();
				sortList.addAll(list);

				dataList.addAll(list);

				commonAdapter.notifyDataSetChanged();
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

		if(InputBillActivity.mBillInfo != null){
			InputBillActivity.mBillInfo.setDestSiteGcode(sortList.get(currPos).getSiteGcode());
			InputBillActivity.mBillInfo.setDestSiteName(sortList.get(currPos).getSiteName());
		}

		Intent intent = new Intent();
		intent.putExtra("code", sortList.get(currPos).getSiteGcode());
		intent.putExtra("name", sortList.get(currPos).getSiteName());
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

	public void sortData(String data){

		sortList.clear();
		commonAdapter.notifyDataSetChanged();
		int len = dataList.size();
		for(int i=0; i<len; i++){

			SiteInfo info = dataList.get(i);
			if(info.getSiteName().contains(data) || info.getSiteCode().toLowerCase().contains(data)){
				sortList.add(info);
			}
		}

		commonAdapter.notifyDataSetChanged();
	}
}
