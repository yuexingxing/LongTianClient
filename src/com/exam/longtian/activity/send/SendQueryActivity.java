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
import com.exam.longtian.entity.CompareResultInfo;
import com.exam.longtian.entity.JoinBillInfo;
import com.exam.longtian.presenter.PresenterUtil;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
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

/** 
 * ������ѯ
 * 
 * @author yxx
 *
 * @date 2017-11-29 ����4:40:31
 * 
 */
public class SendQueryActivity extends BaseActivity {

	@ViewInject(R.id.send_query_time) EditText edtTime;
	@ViewInject(R.id.send_query_sitename) EditText edtSiteName;
	@ViewInject(R.id.send_query_billcode) EditText edtBillcode;
	@ViewInject(R.id.send_query_join_billcode) EditText edtJoinBillcode;

	@ViewInject(R.id.lv_public) ListView listView;
	List<CompareResultInfo> dataList = new ArrayList<CompareResultInfo>();
	CommonAdapter<CompareResultInfo> commonAdapter;

	private String orderType;
	private String siteGcode;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_send_query);
		ViewUtils.inject(this);
		orderType = getIntent().getStringExtra("order_type");
	}

	@Override
	public void initView() {

		if(PresenterUtil.ORDER_TYPE_ARRIVE.equals(orderType)){
			setTitle("������ѯ");
		}else{
			setTitle("������ѯ");
		}

		commonAdapter = new CommonAdapter<CompareResultInfo>(this, dataList, R.layout.item_layout_send_query) {

			@Override
			public void convert(ViewHolder helper, CompareResultInfo item) {

				helper.setText(R.id.item_layout_sendquery_billcode, item.getBILL_CODE());
				helper.setText(R.id.item_layout_sendquery_num1, item.getSCAN_SUB_COUNT() + "");
				helper.setText(R.id.item_layout_sendquery_num2, item.getUNSAN_SUB_COUNT() + "");

				if(item.getRECEIPT_SCAN() == 1){
					helper.setText(R.id.item_layout_sendquery_flag, "��");
				}else{
					helper.setText(R.id.item_layout_sendquery_billcode, "��");
				}
			}
		};

		listView.setAdapter(commonAdapter);
	}

	@Override
	public void initData() {

		PresenterUtil.waybillSub_scanedSendWaybillSubList(this, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				commonAdapter.notifyDataSetChanged();
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == 0x0001 && resultCode == RESULT_OK) {
			edtSiteName.setText(data.getStringExtra("name"));
			siteGcode = data.getStringExtra("code");
		}
	}

	/**
	 * ѡ��ʱ��
	 * @param v
	 */
	public void time(View v){

		Calendar c = Calendar.getInstance();

		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {  

			@Override  
			public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {  

				edtTime.setText(String.format("%d-%d-%d",i,i1+1,i2));  
			}  

		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();  
	}

	/**
	 * ��һվ
	 * @param v
	 */
	public void nextStop(View v){

		Intent intent = new Intent(this, SiteListActivity.class);
		startActivityForResult(intent, 0x0001);
	}

	/**
	 * ���ӵ�
	 * @param v
	 */
	public void joinBillcode(View v){

	}

	/**
	 * ��ȡ���ӵ���--������ѯ
	 * @param v
	 */
	public void queryJoinBill(View v){

		String billcode = edtBillcode.getText().toString();
		String handoverTimeFrom = edtTime.getText().toString() + " 00:00:00";
		String handoverTimeTo = edtTime.getText().toString() + " 23:59:59";

		if(TextUtils.isEmpty(siteGcode)){
			CommandTools.showToast("��ѡ����һվ");
			return;
		}

		if(TextUtils.isEmpty(billcode)){
			CommandTools.showToast("�����뽻�ӵ���");
			return;
		}

		PresenterUtil.handover_queryHandoverList(this, handoverTimeFrom, handoverTimeTo, siteGcode, billcode, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				if(success){

					JoinBillInfo joinBillInfo = (JoinBillInfo) data;
					if(joinBillInfo != null){
						edtJoinBillcode.setText(joinBillInfo.getHandoverId());
						query(null);
					}
				}
			}
		});

	}

	/**
	 * ��ѯ
	 * @param v
	 */
	public void query(View v){

		String handoverId = edtJoinBillcode.getText().toString();

		if(TextUtils.isEmpty(handoverId)){
			CommandTools.showToast("�����뽻�ӵ���");
			return;
		}

		PresenterUtil.scan_sendComparedResult(this, orderType, MyApplication.mUser.getOwnSiteGcode(), handoverId, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				dataList.clear();
				dataList.addAll((Collection<? extends CompareResultInfo>) data);
				commonAdapter.notifyDataSetChanged();
			}
		});
	}
}
