package com.exam.longtian.activity.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.entity.ScanDetail;
import com.exam.longtian.entity.SiteInfo;
import com.exam.longtian.presenter.PresenterQuery;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

/** 
 * É¨Ãè¼ÇÂ¼
 * 
 * @author yxx
 *
 * @date 2017-12-4 ÏÂÎç2:30:39
 * 
 */
public class ScanRecordActivity extends BaseActivity {

	@ViewInject(R.id.scan_record_billcode) EditText edtBillcode;

	@ViewInject(R.id.lv_public) ListView listView;
	List<BillInfo> dataList = new ArrayList<BillInfo>();
	CommonAdapter<BillInfo> commonAdapter;

	private String waybillCode;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentViewId(R.layout.activity_scan_record);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("É¨Ãè¼ÇÂ¼");

		commonAdapter = new CommonAdapter<BillInfo>(this, dataList, R.layout.item_layout_scanrecord) {

			@Override
			public void convert(ViewHolder helper, BillInfo item) {

				helper.setText(R.id.item_layout_scanrecord_1, item.getSendSiteName());
				helper.setText(R.id.item_layout_scanrecord_2, item.getSendDate());
				helper.setText(R.id.item_layout_scanrecord_3, item.getDataSource());
			}
		};

		listView.setAdapter(commonAdapter);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		waybillCode = getIntent().getStringExtra("waybillCode");
		edtBillcode.setText(waybillCode);
		PresenterQuery.scan_getScanRecordByWaybill(this, waybillCode, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				if(data == null){
					return;
				}

				dataList.clear();
				dataList.addAll((Collection<? extends BillInfo>) data);
				commonAdapter.notifyDataSetChanged();
			}
		});
	}

}
