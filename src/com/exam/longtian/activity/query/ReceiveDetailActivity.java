package com.exam.longtian.activity.query;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.widget.ListView;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.presenter.PresenterQuery;
import com.exam.longtian.presenter.PresenterUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
	List<BillInfo> dataList = new ArrayList<BillInfo>();
	CommonAdapter<BillInfo> commonAdapter;

	private String billcode = "666666666666610001";
	private String orderType;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentViewId(R.layout.activity_receive_detail);
		ViewUtils.inject(this);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		orderType = getIntent().getStringExtra("order_type");
		if(PresenterUtil.ORDER_TYPE_DISP.equals(orderType)){
			setTitle("派件明细");
		}else{
			setTitle("收件明细");
		}


		commonAdapter = new CommonAdapter<BillInfo>(this, dataList, R.layout.item_layout_receivedetail) {

			@Override
			public void convert(ViewHolder helper, BillInfo item) {

				helper.setText(R.id.item_layout_receivedetail_billcode, item.getDispScanSiteName());
				//				helper.setText(R.id.item_layout_receivedetail_sitename, item.getTime());
				//				helper.setText(R.id.item_layout_receivedetail_count, item.getRecord());
			}
		};

		listView.setAdapter(commonAdapter);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		PresenterQuery.waybill_detail(this, billcode, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

			}
		});
	}

}
