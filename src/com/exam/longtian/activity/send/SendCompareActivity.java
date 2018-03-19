package com.exam.longtian.activity.send;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.CompareResultInfo;
import com.exam.longtian.presenter.PresenterUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/** 
 * 发件对比结果
 * 
 * @author yxx
 *
 * @date 2017-12-1 上午10:34:29
 * 
 */
public class SendCompareActivity extends BaseActivity {

	@ViewInject(R.id.send_compare_nextstop) TextView tvNextStop;
	@ViewInject(R.id.send_compare_name) TextView tvName;

	@ViewInject(R.id.lv_public) ListView listView;
	List<CompareResultInfo> dataList = new ArrayList<CompareResultInfo>();
	CommonAdapter<CompareResultInfo> commonAdapter;

	private String orderType;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_send_compare);
		ViewUtils.inject(this);
		orderType = getIntent().getStringExtra("order_type");
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		if(PresenterUtil.ORDER_TYPE_ARRIVE.equals(orderType)){
			setTitle("到件对比结果");
		}else{
			setTitle("发件对比结果");
		}

		tvNextStop.setText(getIntent().getStringExtra("siteName"));
		tvName.setText(MyApplication.mUser.getEmpName());

		commonAdapter = new CommonAdapter<CompareResultInfo>(this, dataList, R.layout.item_layout_send_compare) {

			@Override
			public void convert(ViewHolder helper, CompareResultInfo item) {

				helper.setText(R.id.item_layout_sendcompare_billcode, item.getBILL_CODE());
				helper.setText(R.id.item_layout_sendcompare_num1, item.getSCAN_SUB_COUNT() + "");
				helper.setText(R.id.item_layout_sendcompare_num2, item.getUNSAN_SUB_COUNT() + "");
				helper.setText(R.id.item_layout_sendcompare_backbillcode, item.getRECEIPT_CODE());

				if(item.getBE_SCAN() == 0){
					helper.setText(R.id.item_layout_sendcompare_flag, "否");
				}else{
					helper.setText(R.id.item_layout_sendcompare_flag, "是");
				}

				if(item.getRECEIPT_SCAN() == 0){
					helper.setText(R.id.item_layout_sendcompare_flag2, "否");
				}else{
					helper.setText(R.id.item_layout_sendcompare_flag2, "是");
				}
			}
		};

		listView.setAdapter(commonAdapter);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		Intent intent = getIntent();
		String handoverId = intent.getStringExtra("handoverId");
		String ownSiteGcode = MyApplication.mUser.getOwnSiteGcode();

		PresenterUtil.scan_sendComparedResult(this, orderType, ownSiteGcode, handoverId, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				dataList.clear();
				dataList.addAll((Collection<? extends CompareResultInfo>) data);
				commonAdapter.notifyDataSetChanged();
			}
		});
	}

	/**
	 * 继续
	 * @param v
	 */
	public void continu(View v){

		Intent intent = new Intent();
		intent.putExtra("continu", "1");
		setResult(RESULT_OK, intent);
		finish();
	}

	/**
	 * 结束
	 * @param v
	 */
	public void back(View v){

		Intent intent = new Intent();
		intent.putExtra("continu", "0");
		setResult(RESULT_OK, intent);
		finish();
	}
}
