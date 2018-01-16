package com.exam.longtian.activity.query;

import java.util.ArrayList;
import java.util.List;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.SiteInfo;
import com.exam.longtian.presenter.PresenterQuery;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.widget.ListView;

/** 
 * 收件明细
 * 
 * @author yxx
 *
 * @date 2017-12-4 下午3:12:29
 * 
 */
public class ReceiveDetailActivity extends BaseActivity {


	@ViewInject(R.id.lv_public) ListView listView;
	List<SiteInfo> dataList = new ArrayList<SiteInfo>();
	CommonAdapter<SiteInfo> commonAdapter;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentViewId(R.layout.activity_receive_detail);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("收件明细");

		for(int i=0; i<20; i++){

			SiteInfo user = new SiteInfo();
			user.setName("杭州萧山");
//			user.setTime("2017-12-12 12:12:12");
//			user.setRecord("到达【杭州分拨】上一站是【杭州萧山】");

			dataList.add(user);
		}

		commonAdapter = new CommonAdapter<SiteInfo>(this, dataList, R.layout.item_layout_receivedetail) {

			@Override
			public void convert(ViewHolder helper, SiteInfo item) {

				helper.setText(R.id.item_layout_receivedetail_billcode, item.getName());
//				helper.setText(R.id.item_layout_receivedetail_sitename, item.getTime());
//				helper.setText(R.id.item_layout_receivedetail_count, item.getRecord());
			}
		};

		listView.setAdapter(commonAdapter);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

//		PresenterQuery.waybill_detail(this, billcode, callback)
	}

}
