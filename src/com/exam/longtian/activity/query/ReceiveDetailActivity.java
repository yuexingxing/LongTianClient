package com.exam.longtian.activity.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.activity.MainMenuActivity;
import com.exam.longtian.activity.inputbill.InputBill3Activity;
import com.exam.longtian.adapter.CommonAdapter;
import com.exam.longtian.adapter.ViewHolder;
import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.entity.ScanDetail;
import com.exam.longtian.entity.SiteInfo;
import com.exam.longtian.entity.SubBillDetailInfo;
import com.exam.longtian.presenter.PresenterQuery;
import com.exam.longtian.presenter.PresenterUtil;
import com.exam.longtian.printer.bluetooth.PrintUtil;
import com.exam.longtian.printer.bluetooth.PrinterConnectDialog;
import com.exam.longtian.printer.bluetooth.PrinterSettingMenuActivity;
import com.exam.longtian.printer.bluetooth.PrintUtil.CallBack;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.CommandTools.CommandToolsCallback;
import com.exam.longtian.util.Res;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.gprinter.io.GpDevice;
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

	private int currPos = -1;

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

		setRightTitle("打印");

		commonAdapter = new CommonAdapter<ScanDetail>(this, dataList, R.layout.item_layout_receivedetail) {

			@Override
			public void convert(ViewHolder helper, ScanDetail item) {

				if(item.isFlag()){
					helper.setLayoutResource(R.id.item_layout_receivedetail_top, Res.getColor(R.color.blue));
				}else{
					helper.setLayoutResource(R.id.item_layout_receivedetail_top, Res.getColor(R.color.transparent));
				}

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

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				ScanDetail info = dataList.get(arg2);

				int len = dataList.size();
				for(int i=0; i<len; i++){
					dataList.get(i).setFlag(false);
				}

				info.setFlag(!info.isFlag());

				currPos = arg2;
				commonAdapter.notifyDataSetChanged();
			}
		});


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

	/* 
	 * 打印(non-Javadoc)
	 * @see com.exam.longtian.activity.BaseActivity#clickRight(android.view.View)
	 */
	public void clickRight(View v){
		
		if(currPos < 0){
			CommandTools.showToast("请选择一条数据打印");
			return;
		}

		if (MainMenuActivity.mGpService == null) {
			CommandTools.showToast("打印机服务启动失败，请检查打印机");
			return;
		}

		if(MainMenuActivity.printer_status != GpDevice.STATE_VALID_PRINTER){

			CommandTools.showChooseDialog(this, "请先连接打印机", new CommandToolsCallback() {

				@Override
				public void callback(int position) {
					// TODO Auto-generated method stub
					if(position == 0){

						Intent intent = new Intent(ReceiveDetailActivity.this, PrinterConnectDialog.class);
						boolean[] state = MainMenuActivity.getConnectState();
						intent.putExtra(MainMenuActivity.CONNECT_STATUS, state);
						startActivity(intent);
						//						finish();
					}
				}
			});

			return;
		}

		CommandTools.showChooseDialog(ReceiveDetailActivity.this, "确认打印该条数据？", new CommandToolsCallback() {

			@Override
			public void callback(int position) {

				if(position == 0){
					//startPrint();
					ScanDetail scanDetail = dataList.get(currPos);
					getSubBillcode(scanDetail.getBillCode());
				}
			}
		});
	}

	public void getSubBillcode(String billCode){

		PresenterUtil.waybillSub_getSubListByBillCode(ReceiveDetailActivity.this, billCode, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {

				List<SubBillDetailInfo> list = (ArrayList<SubBillDetailInfo>) data;
				startPrint(list);
			}
		});
	}

	public void startPrint(List<SubBillDetailInfo> list){

		ScanDetail scanDetail = dataList.get(currPos);
		BillInfo info = new BillInfo();

		info.setSubBillcode(scanDetail.getBillCode());
		info.setPackageKindName(scanDetail.getPackageKindName());
		info.setSendDate(scanDetail.getSendDate());
		info.setDestSiteName(scanDetail.getSendSiteName());
		info.setSenderAddress(scanDetail.getSenderAddress());
		info.setTotalVolume(scanDetail.getTotalVolume());
		info.setTotalWeight(scanDetail.getTotalWeight());
		info.setRecipientsAddress(scanDetail.getRecipientsAddress());
		info.setRecipientsName(scanDetail.getRecipientsName());
		info.setSendSiteName(scanDetail.getSendSiteName());
		info.setRemark(scanDetail.getRemark());

		List<BillInfo> printList = new ArrayList<BillInfo>();
		int len = list.size();
		for(int i=0; i<len; i++){

			SubBillDetailInfo subBillDetailInfo = list.get(i);
			info.setBillCode(subBillDetailInfo.getBillCode());
			info.setTotalWeight(subBillDetailInfo.getWeight());
			info.setTotalVolume(subBillDetailInfo.getVolume());

			printList.add(info);
		}

		PrintUtil.printLabelList(printList, new CallBack() {

			@Override
			public void callback(int pos) {
				// TODO Auto-generated method stub
				if(pos == 0){
					CommandTools.showToast("打印成功");
				}
			}
		});

	}

}
