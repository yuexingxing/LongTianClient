package com.exam.longtian.activity.query;

import java.util.ArrayList;
import java.util.List;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
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
	List<SiteInfo> dataList = new ArrayList<SiteInfo>();
	CommonAdapter<SiteInfo> commonAdapter;

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

		commonAdapter = new CommonAdapter<SiteInfo>(this, dataList, R.layout.item_layout_scanrecord) {

			@Override
			public void convert(ViewHolder helper, SiteInfo item) {

				helper.setText(R.id.item_layout_scanrecord_sitename, item.getName());
				//				helper.setText(R.id.item_layout_scanrecord_time, item.getTime());
				//				helper.setText(R.id.item_layout_scanrecord_content, item.getRecord());
			}
		};

		listView.setAdapter(commonAdapter);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		PresenterQuery.scan_getDispWaybillList(this, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				commonAdapter.notifyDataSetChanged();
			}
		});
	}

}
