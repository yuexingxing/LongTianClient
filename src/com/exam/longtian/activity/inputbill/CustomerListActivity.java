package com.exam.longtian.activity.inputbill;

import java.util.ArrayList;
import java.util.List;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.activity.MainMenuActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.CustomInfo;
import com.exam.longtian.presenter.PCustom;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
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
 * 客户选择
 * 
 * @author yxx
 *
 * @date 2017-11-29 下午2:33:44
 * 
 */
public class CustomerListActivity extends BaseActivity {

	@ViewInject(R.id.customerlist_search) EditText edtSearch;

	@ViewInject(R.id.lv_public) ListView listView;
	CommonAdapter<CustomInfo> commonAdapter;
	List<CustomInfo> dataList = new ArrayList<CustomInfo>();
	List<CustomInfo> sortList = new ArrayList<CustomInfo>();

	private int currPos = -1;//当前选择的位置
	private int type = 1;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_customer_list);
		ViewUtils.inject(this);

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

		commonAdapter = new CommonAdapter<CustomInfo>(this, sortList, R.layout.item_layout_customer_list) {

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
				CustomInfo info = sortList.get(arg2);

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

		type = getIntent().getIntExtra("type", 1);

		PCustom.customer_list(this,  new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub
				sortList.clear();
				dataList.clear();
				if(data != null){

					sortList.addAll((List<CustomInfo>) data);
					dataList.addAll((List<CustomInfo>) data);
					commonAdapter.notifyDataSetChanged();
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

		CustomInfo info = sortList.get(currPos);

		if(InputBillActivity.mBillInfo != null){

			if(type == 1){
				InputBillActivity.mBillInfo.setRecipientsCustName(info.getCustName());
				InputBillActivity.mBillInfo.setRecipientsCustGcode(info.getCustGcode());
			}else {
				InputBillActivity.mBillInfo.setSenderCustName(info.getCustName());
				InputBillActivity.mBillInfo.setSenderCustGcode(info.getCustGcode());
			}

		}

		Intent intent = new Intent();
		intent.putExtra("linkMan", info.getCustLinkman());
		intent.putExtra("customer", info.getCustName());
		intent.putExtra("phone", info.getCustPhone());
		intent.putExtra("code", info.getCustGcode());
		intent.putExtra("address", info.getAddress());
		intent.putExtra("companyName", info.getCustCompanyName());
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

			CustomInfo info = dataList.get(i);
			if(info.getCustName().contains(data) || info.getOpEmpName().toLowerCase().contains(data)){
				sortList.add(info);
			}
		}

		commonAdapter.notifyDataSetChanged();
	}
}
