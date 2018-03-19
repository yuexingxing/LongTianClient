package com.exam.longtian.activity.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import android.os.Bundle;
import android.widget.ListView;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.ScanDetail;
import com.exam.longtian.presenter.PresenterQuery;
import com.exam.longtian.presenter.PresenterUtil;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/** 
 * 收件/派件--明细
 * 
 * @author yxx
 *
 * @date 2017-12-4 下午3:12:29
 * 
 */
public class ReceiveDetailActivity extends BaseActivity {

	@ViewInject(R.id.lv_public) ListView listView;
	List<ScanDetail> dataList = new ArrayList<ScanDetail>();
	CommonAdapter<ScanDetail> commonAdapter;

	private String startTime;
	private String endTime;
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
		startTime = getIntent().getStringExtra("startTime");
		endTime = getIntent().getStringExtra("endTime");

		if(PresenterUtil.ORDER_TYPE_DISP.equals(orderType)){
			setTitle("派件明细");
		}else{
			setTitle("收件明细");
		}

		commonAdapter = new CommonAdapter<ScanDetail>(this, dataList, R.layout.item_layout_receivedetail) {

			@Override
			public void convert(ViewHolder helper, ScanDetail item) {

				helper.setText(R.id.item_layout_receivedetail_1, item.getBillCode());
				helper.setText(R.id.item_layout_receivedetail_2, item.getSendSiteName());
				helper.setText(R.id.item_layout_receivedetail_3, item.getPieceNum());
				helper.setText(R.id.item_layout_receivedetail_4, item.getTotalWeight());
				helper.setText(R.id.item_layout_receivedetail_5, item.getTotalVolume());
				helper.setText(R.id.item_layout_receivedetail_6, item.getAgencyFund());
				helper.setText(R.id.item_layout_receivedetail_7, item.getFreight());
				helper.setText(R.id.item_layout_receivedetail_8, item.getPayModeName());
				helper.setText(R.id.item_layout_receivedetail_9, item.getDaofreight());
				helper.setText(R.id.item_layout_receivedetail_10, item.getReceiptCode());//回单号
				helper.setText(R.id.item_layout_receivedetail_11, item.getSenderName());
				helper.setText(R.id.item_layout_receivedetail_12, item.getSenderPhone());
				helper.setText(R.id.item_layout_receivedetail_13, item.getRecipientsName());
				helper.setText(R.id.item_layout_receivedetail_14, item.getRecipientsPhone());
				helper.setText(R.id.item_layout_receivedetail_15, item.getSigner());//签收人
				helper.setText(R.id.item_layout_receivedetail_16, item.getSignSiteName());//签收网点
				helper.setText(R.id.item_layout_receivedetail_17, item.getSignTime());
			}
		};

		listView.setAdapter(commonAdapter);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		PresenterQuery.waybill_getReWaybillList(this, orderType, startTime, endTime, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub
				if(data == null){
					return;
				}
				
				dataList.clear();
				dataList.addAll((Collection<? extends ScanDetail>) data);
				commonAdapter.notifyDataSetChanged();
				
				if(dataList.size() == 0){
					CommandTools.showToast("未查到有效数据");
				}
			}
		});
	}

}
