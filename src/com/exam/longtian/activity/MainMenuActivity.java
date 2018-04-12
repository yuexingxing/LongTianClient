package com.exam.longtian.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.activity.arrive.ArriveScanActivity;
import com.exam.longtian.activity.inputbill.InputBillActivity;
import com.exam.longtian.activity.query.QueryMenuActivity;
import com.exam.longtian.activity.send.SendScanActivity;
import com.exam.longtian.activity.sign.SignScanActivity;
import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.printer.bluetooth.PrinterSettingMenuActivity;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.Constant;
import com.gprinter.aidl.GpService;
import com.gprinter.command.GpCom;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;
import com.lidroid.xutils.ViewUtils;

/** 
 * ���˵�
 * 
 * @author yxx
 *
 * @date 2017-11-27 ����6:15:40
 * 
 */
public class MainMenuActivity extends BaseActivity{

	public static GpService mGpService = null;
	private PrinterServiceConnection conn = null;
	private int mPrinterIndex = 0;
	public static final String CONNECT_STATUS = "connect.status";
	public static int printer_status = GpDevice.STATE_NONE;//��ӡ����ǰ״̬0-δ���� 1-������
	private static final int MAIN_QUERY_PRINTER_STATUS = 0xfe;
	private static final int REQUEST_PRINT_LABEL = 0xfd;
	private static final int REQUEST_PRINT_RECEIPT = 0xfc;
	
	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_main);
		ViewUtils.inject(this);
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		hidenTopBar();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

		connection();
		registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_DEVICE_REAL_STATUS));
	}
	
	public void onResume(){
		super.onResume();
		
	}

	/**
	 * ¼��
	 * @param v
	 */
	public void ludan(View v){

		startActivity((new Intent(this, InputBillActivity.class)));
	}

	/**
	 * ����ɨ��
	 * @param v
	 */
	public void send(View v){

		startActivity((new Intent(this, SendScanActivity.class)));
	}

	/**
	 * ����
	 * @param v
	 */
	public void arrive(View v){

		startActivity((new Intent(this, ArriveScanActivity.class)));
	}

	/**
	 * ǩ��
	 * @param v
	 */
	public void sign(View v){

		startActivity((new Intent(this, SignScanActivity.class)));
	}

	/**
	 * ������ѯ
	 * @param v
	 */
	public void query(View v){

		startActivity((new Intent(this, QueryMenuActivity.class)));
	}

	/**
	 * �˳�
	 * @param v
	 */
	public void back(View v){

		exit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) { // ��ȡ back��

			exit();
		}
		return false;
	}

	public void exit(){

		new AlertDialog.Builder(this)
		.setTitle("��ʾ")
		.setMessage("ȷ���˳�����")
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MyApplication.finishAllActivities();
				finish();
			}
		}).setNegativeButton("ȡ��", null).show();
	}

	public void onDestory(){
		super.onDestroy();

		unregisterReceiver(mBroadcastReceiver);
	}

	/**
	 * �����豸����
	 * @param view
	 */
	public void bluetoothSetting(View view) {

		if (mGpService == null) {
			CommandTools.showToast("��ӡ����������ʧ�ܣ������ӡ��");
			return;
		}

		Intent intent = new Intent(this, PrinterSettingMenuActivity.class);
		boolean[] state = getConnectState();
		intent.putExtra(CONNECT_STATUS, state);
		startActivity(intent);
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// GpCom.ACTION_DEVICE_REAL_STATUS Ϊ�㲥��IntentFilter
			if (action.equals(GpCom.ACTION_DEVICE_REAL_STATUS)) {

				// ҵ���߼��������룬��Ӧ�����ѯ��ʲô����
				int requestCode = intent.getIntExtra(GpCom.EXTRA_PRINTER_REQUEST_CODE, -1);
				// �ж������룬�������ҵ�����
				if (requestCode == MAIN_QUERY_PRINTER_STATUS) {

					int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
					String str;
					if (status == GpCom.STATE_NO_ERR) {
						str = "��ӡ������";
					} else {
						str = "��ӡ�� ";
						if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
							str += "�ѻ�";
						}
						if ((byte) (status & GpCom.STATE_PAPER_ERR) > 0) {
							str += "ȱֽ";
						}
						if ((byte) (status & GpCom.STATE_COVER_OPEN) > 0) {
							str += "��ӡ������";
						}
						if ((byte) (status & GpCom.STATE_ERR_OCCURS) > 0) {
							str += "��ӡ������";
						}
						if ((byte) (status & GpCom.STATE_TIMES_OUT) > 0) {
							str += "��ѯ��ʱ";
						}
					}

					Toast.makeText(getApplicationContext(), "��ӡ����" + mPrinterIndex + " ״̬��" + str, Toast.LENGTH_SHORT).show();
				} else if (requestCode == REQUEST_PRINT_LABEL) {
					int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
					if (status == GpCom.STATE_NO_ERR) {
						//						sendLabel();
					} else {
						Toast.makeText(MainMenuActivity.this, "query printer status error", Toast.LENGTH_SHORT).show();
					}
				} else if (requestCode == REQUEST_PRINT_RECEIPT) {
					int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
					if (status == GpCom.STATE_NO_ERR) {
						//						sendReceipt();
					} else {
						Toast.makeText(MainMenuActivity.this, "query printer status error", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	};

	/**
	 * 
	 * 
	 * */
	private void connection() {
		conn = new PrinterServiceConnection();
		Intent intent = new Intent(this, GpPrintService.class);
		bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
	}

	public static boolean[] getConnectState() {
		
		boolean[] state = new boolean[GpPrintService.MAX_PRINTER_CNT];
		for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
			state[i] = false;
		}
		for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
			try {
				if (mGpService.getPrinterConnectStatus(i) == GpDevice.STATE_CONNECTED) {
					state[i] = true;
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return state;
	}

	class PrinterServiceConnection implements ServiceConnection {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i("ServiceConnection", "onServiceDisconnected() called");
			mGpService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mGpService = GpService.Stub.asInterface(service);
		}
	}
	
}
