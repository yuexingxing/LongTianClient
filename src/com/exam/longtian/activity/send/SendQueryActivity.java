package com.exam.longtian.activity.send;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.activity.inputbill.SiteListActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.camera.CaptureActivity;
import com.exam.longtian.entity.CompareResultInfo;
import com.exam.longtian.entity.JoinBillInfo;
import com.exam.longtian.presenter.PresenterUtil;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.Constant;
import com.exam.longtian.util.DateUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.exam.longtian.util.RegularUtil;
import com.exam.longtian.view.JoinBillItemDialog;
import com.exam.longtian.view.JoinBillItemDialog.JoinBillItemDialogCallBack;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/** 
 * 发件、到件--查询
 * 
 * @author yxx
 *
 * @date 2017-11-29 下午4:40:31
 * 
 */
public class SendQueryActivity extends BaseActivity {

	@ViewInject(R.id.send_query_starttime) EditText edtStartTime;
	@ViewInject(R.id.send_query_endtime) EditText edtEndTime;

	@ViewInject(R.id.send_query_sitename_tv) TextView tvSiteName;
	@ViewInject(R.id.send_query_sitename) EditText edtSiteName;
	@ViewInject(R.id.send_query_billcode) EditText edtBillcode;
	@ViewInject(R.id.send_query_join_billcode) EditText edtJoinBillcode;

	@ViewInject(R.id.item_layout_sendquery_num3) TextView tvChaYi;

	@ViewInject(R.id.lv_public) ListView listView;
	List<CompareResultInfo> dataList = new ArrayList<CompareResultInfo>();
	CommonAdapter<CompareResultInfo> commonAdapter;

	List<JoinBillInfo> joinList = new ArrayList<JoinBillInfo>();

	private int startYear;
	private int startMonth;
	private int startDay;

	private int endYear;
	private int endMonth;
	private int endDay;

	private String orderType;
	private String siteGcode = "";

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_send_query);
		ViewUtils.inject(this);

	}

	@Override
	public void initView() {

		orderType = getIntent().getStringExtra("order_type");
		if(PresenterUtil.ORDER_TYPE_ARRIVE.equals(orderType)){
			setTitle("到件查询");
			tvSiteName.setText("上一站");
			tvChaYi.setVisibility(View.VISIBLE);
		}else{
			setTitle("发件查询");
			tvSiteName.setText("下一站");
			tvChaYi.setVisibility(View.GONE);
		}

		commonAdapter = new CommonAdapter<CompareResultInfo>(this, dataList, R.layout.item_layout_send_query) {

			@Override
			public void convert(ViewHolder helper, CompareResultInfo item) {

				helper.setText(R.id.item_layout_sendquery_billcode, item.getBILL_CODE());
				helper.setText(R.id.item_layout_sendquery_num1, item.getSCAN_SUB_COUNT() + "");
				helper.setText(R.id.item_layout_sendquery_num2, item.getUNSAN_SUB_COUNT() + "");

				String chayi = "";
				if(PresenterUtil.ORDER_TYPE_ARRIVE.equals(orderType)){
					
					chayi = (item.getUNSAN_SUB_COUNT() - item.getSCAN_SUB_COUNT()) + "," + (item.getCOME_NO_SEND());
					helper.hideView(R.id.item_layout_sendquery_num3, false);
				}else{
					helper.hideView(R.id.item_layout_sendquery_num3, true);
				}
				
				helper.setText(R.id.item_layout_sendquery_num3, chayi);

				if(item.getBE_SCAN() == 1){
					helper.setText(R.id.item_layout_sendquery_flag, "是");
				}else{
					helper.setText(R.id.item_layout_sendquery_flag, "否");
				}
			}
		};

		listView.setAdapter(commonAdapter);
	}

	@Override
	public void initData() {

		Calendar c = Calendar.getInstance();

		startYear = c.get(Calendar.YEAR);
		startMonth = c.get(Calendar.MONTH) + 1;
		startDay = c.get(Calendar.DAY_OF_MONTH);

		endYear = startYear;
		endMonth = startMonth;
		endDay = startDay;

		String strDate = startYear + "-" + startMonth + "-" + startDay;

		edtStartTime.setText(strDate);
		edtEndTime.setText(strDate);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == 0x0001 && resultCode == RESULT_OK) {
			edtSiteName.setText(data.getStringExtra("name"));
			siteGcode = data.getStringExtra("code");
		}else if (requestCode == Constant.CAPTURE_BILLCODE && resultCode == RESULT_OK) {
			
			if(data == null){
				return;
			}

			Bundle bundle = data.getExtras();
			String strBillcode = bundle.getString("result");
			edtBillcode.setText(strBillcode);
		}
	}
	
	public void scan(View v){

		Intent openCameraIntent = new Intent(this, CaptureActivity.class);
		startActivityForResult(openCameraIntent, Constant.CAPTURE_BILLCODE);
	}

	/**
	 * 起始日期
	 * @param v
	 */
	public void startTime(View v){

		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {  

			@Override  
			public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {  

				startYear = i;
				startMonth = i1 + 1;
				startDay = i2;

				edtStartTime.setText(String.format("%d-%d-%d", startYear, startMonth, startDay));  
			}  

		}, startYear, startMonth - 1, startDay).show(); 
	}

	/**
	 * 结束日期
	 * @param v
	 */
	public void endTime(View v){

		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {  

			@Override  
			public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {  

				endYear = i;
				endMonth = i1 + 1;
				endDay = i2;

				edtEndTime.setText(String.format("%d-%d-%d", endYear, endMonth, endDay));  
			}  

		}, endYear, endMonth - 1, endDay).show(); 
	}

	/**
	 * 下一站
	 * @param v
	 */
	public void nextStop(View v){

		Intent intent = new Intent(this, SiteListActivity.class);
		intent.putExtra("order_type", orderType);
		startActivityForResult(intent, 0x0001);
	}

	/**
	 * 获取交接单号--辅助查询
	 * @param v
	 */
	public void query(View v){

		String handoverId = edtBillcode.getText().toString();

		if(!DateUtil.compareDate(startYear, startMonth, startDay, endYear, endMonth, endDay)){
			CommandTools.showToast("起始时间不能大于结束时间");
			return;
		}

		if(!TextUtils.isEmpty(handoverId) && !RegularUtil.checkAllBill(handoverId)){
			CommandTools.showToast("单号不符合规则");
			return;
		}

		String startTime = edtStartTime.getText().toString() + " 00:00:00";
		String endTime = edtEndTime.getText().toString() + " 23:59:59";

		dataList.clear();
		commonAdapter.notifyDataSetChanged();
		PresenterUtil.handover_queryHandoverList(this, orderType, startTime, endTime, siteGcode, handoverId, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				if(success){

					joinList.clear();
					joinList.addAll((Collection<? extends JoinBillInfo>) data);

					showJoinBillDialog();
				}
			}
		});

	}

	public void showJoinBillDialog(){

		if(joinList.size() == 0){
			CommandTools.showToast("未查到数据");
			return;
		}

		JoinBillItemDialog.showDialog(this, "请选择数据", false, joinList, new JoinBillItemDialogCallBack() {

			@Override
			public void callback(int pos) {
				// TODO Auto-generated method stub
				edtJoinBillcode.setText(joinList.get(pos).getHandoverId());
				queryJoinBill(null);
			}
		});
	}

	/**
	 * 查询
	 * @param v
	 */
	public void queryJoinBill(View v){

		String billcode = edtJoinBillcode.getText().toString();

		if(TextUtils.isEmpty(billcode)){
			CommandTools.showToast("请输入交接单号");
			return;
		}

		if(!RegularUtil.checkJoinBill(billcode)){
			CommandTools.showToast("交接单号符合规则");
			return;
		}

		PresenterUtil.scan_sendComparedResult(this, orderType, MyApplication.mUser.getOwnSiteGcode(), billcode, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				dataList.clear();
				dataList.addAll((Collection<? extends CompareResultInfo>) data);
				commonAdapter.notifyDataSetChanged();

				if(dataList.size() == 0){
					CommandTools.showToast("未查到数据");
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.exam.longtian.activity.BaseActivity#onScanSuccess(java.lang.String)
	 */
	public void onScanSuccess(String barcode) {
		// TODO Auto-generated method stub
		
		if(edtBillcode.isFocused()){
			edtBillcode.setText(barcode);
		}else if(edtJoinBillcode.isFocused()){
			edtJoinBillcode.setText(barcode);
		}
	}
}
