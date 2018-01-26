package com.exam.longtian.activity.send;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.JoinBillInfo;
import com.exam.longtian.presenter.PresenterUtil;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
	List<JoinBillInfo> dataList = new ArrayList<JoinBillInfo>();
	CommonAdapter<JoinBillInfo> commonAdapter;

	@ViewInject(R.id.choose_joinbill_flag) CheckBox checkBox;
	@ViewInject(R.id.choose_joinbill_name) EditText edtName;

	private String siteName;
	private String siteGCode;
	private int curPos = -2;
	private String orderType;

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

		commonAdapter = new CommonAdapter<JoinBillInfo>(this, dataList, R.layout.item_layout_choose_joinbill) {

			@Override
			public void convert(ViewHolder helper, JoinBillInfo item) {

				TextView tvBillcode = (TextView) helper.getView(R.id.item_layout_choose_joinbill_billcode);
				TextView tvName = (TextView) helper.getView(R.id.item_layout_choose_joinbill_name);

				tvBillcode.setText(item.getDriverId());
				tvName.setText(item.getDriverName());

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
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				// TODO Auto-generated method stub
				curPos = arg2;
				JoinBillInfo info = dataList.get(arg2);
				for(int i =0; i<dataList.size(); i++){

					dataList.get(i).setFlag(false);
				}

				info.setFlag(true);
				commonAdapter.notifyDataSetChanged();
				checkBox.setChecked(false);

			}
		});

		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){

					curPos = -1;
					int len = dataList.size();
					for(int i=0; i<len; i++){
						dataList.get(i).setFlag(false);
					}
					commonAdapter.notifyDataSetChanged();
				}else{

				}
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		siteGCode = getIntent().getStringExtra("siteGCode");
		siteName = getIntent().getStringExtra("siteName");
		orderType = getIntent().getStringExtra("order_type");

		PresenterUtil.handover_queryHandoverList(this, "", "", "", "", new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				dataList.clear();
				dataList.addAll((Collection<? extends JoinBillInfo>) data);

				commonAdapter.notifyDataSetChanged();
			}
		});
	}

	public void bind(View v){

		if(curPos == -2){
			CommandTools.showToast("请选择司机");
			return;
		}

		if(checkBox.isChecked()){

			String driverName = edtName.getText().toString();
			if(TextUtils.isEmpty(driverName)){
				CommandTools.showToast("请录入司机");
			}else{
				addHandOver(driverName);
			}
		}else{

			bindData();
		}
	}

	public void bindData(){

		JSONArray jsonArray = new JSONArray();
		String[] arrBillcode = getIntent().getStringExtra("billcodes").split(",");
		for(int i= 0; i<arrBillcode.length; i++ ){

			jsonArray.put(arrBillcode[i]);
		}

		final JoinBillInfo joinBillInfo = dataList.get(curPos);

		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("billcodes", jsonArray);
			jsonObject.put("couriersGcode", MyApplication.mUser.getEmpGcode());
			jsonObject.put("handoverId", joinBillInfo.getHandoverId());
			jsonObject.put("nearbySiteGcode", siteGCode);
			jsonObject.put("plateNumber", joinBillInfo.getPlateNumber());
			jsonObject.put("scanTime", CommandTools.getTime());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		PresenterUtil.handover_BindHandover(this, orderType, jsonObject, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub
				CommandTools.showToast(message);
				if(success){
					
					Intent intent = new Intent(ChooseJoinBillActivity.this, SendCompareActivity.class);
					intent.putExtra("order_type", orderType);
					intent.putExtra("siteName", siteName);
					intent.putExtra("handoverId", joinBillInfo.getHandoverId());
					startActivity(intent);
					finish();
				}
			}
		});

	}

	/**
	 * 新增交接单
	 */
	public void addHandOver(String name){

		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("driverName", name);
			jsonObject.put("handoverId", CommandTools.getTimes());
			jsonObject.put("handoverTime", CommandTools.getTime());
			jsonObject.put("listType", "1");
			jsonObject.put("opEmpGcode", MyApplication.mUser.getOpEmpGcode());
			jsonObject.put("opEmpName", MyApplication.mUser.getOpEmpName());
			jsonObject.put("opTime", CommandTools.getTime());
			jsonObject.put("oppositeSiteGcode", siteGCode);
			jsonObject.put("plateNumber", "");
			jsonObject.put("relaHandoverId", "");
			jsonObject.put("siteGcode", MyApplication.mUser.getOwnSiteGcode());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		PresenterUtil.handover_add(this, jsonObject, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				if(success){

					initData();
					checkBox.setChecked(false);
					edtName.setText("");
					curPos = -2;
				}
			}
		});
	}
}
