package com.exam.longtian.scanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.activity.MainMenuActivity;
import com.exam.longtian.printer.bluetooth.ListViewAdapter;
import com.exam.longtian.printer.bluetooth.PortConfigurationActivity;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.Constant;
import com.gprinter.io.PortParameters;
import com.gprinter.save.PortParamDataBase;
import com.gprinter.service.GpPrintService;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/** 
 * 扫描枪连接
 * 
 * @author yxx
 *
 * @date 2018-3-30 上午11:02:36
 * 
 */
public class ScannerConnectActivity extends BaseActivity {

	public static BleWrapper bleWrapper;
	private BluItem blueItem = new BluItem();

	private List<Map<String, Object>> mList = null;
	private ListViewAdapter mListViewAdapter = null;
	public static  int mPrinterId = 0;

	public final int MAX_PRINTER_CNT = 1;
	private PortParameters mPortParam[] = new PortParameters[MAX_PRINTER_CNT];
	private static final int INTENT_PORT_SETTINGS = 0;

	private static String SerUUID = "0000feea-0000-1000-8000-00805f9b34fb";
	private static String ChUUID = "00002aa1-0000-1000-8000-00805f9b34fb";

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentViewId(R.layout.activity_scanner_connect);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("扫描枪连接");
		initPortParam();

		ListView list = (ListView) findViewById(R.id.scanner_connect_listview);
		mList = getOperateItemData();
		mListViewAdapter = new ListViewAdapter(this, mList, mHandler);
		list.setAdapter(mListViewAdapter);
		list.setOnItemClickListener(new TitelItemOnClickLisener());
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == INTENT_PORT_SETTINGS) {
			// getIP settings info from IP settings dialog
			if (resultCode == RESULT_OK) {
				Bundle bundle = new Bundle();
				bundle = data.getExtras();
				int param = bundle.getInt(GpPrintService.PORT_TYPE);
				mPortParam[mPrinterId].setPortType(param);
				param = bundle.getInt(GpPrintService.PORT_NUMBER);
				mPortParam[mPrinterId].setPortNumber(param);
				String str = bundle.getString(GpPrintService.BLUETOOT_ADDR);
				blueItem.setAddress(str);
				mPortParam[mPrinterId].setBluetoothAddr(str);
				SetPortParamToView(mPortParam[mPrinterId]);
				if (CheckPortParamters(mPortParam[mPrinterId])) {
					PortParamDataBase database = new PortParamDataBase(this);
					database.deleteDataBase("" + mPrinterId);
					database.insertPortParam(mPrinterId, mPortParam[mPrinterId]);
				} else {
					CommandTools.showToast(getString(R.string.port_parameters_wrong));
				}

			} else {
				CommandTools.showToast(getString(R.string.port_parameters_is_not_save));
			}
		} 
	}

	public Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message message) {

			switch (message.what) {

			case ListViewAdapter.MESSAGE_CONNECT:
				
				if(TextUtils.isEmpty(blueItem.getAddress()) || blueItem.getAddress().contains("undefined")){
					CommandTools.showToast("请先配对蓝牙扫描枪");
					return false;
				}

				bleWrapper = new BleWrapper(ScannerConnectActivity.this, new BleCallBack());
				if (bleWrapper.initialize()) {
					bleWrapper.connect(blueItem.getAddress());
				}
				break;
			}

			return false;
		}
	});

	class TitelItemOnClickLisener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub]

			mPrinterId = arg2;
			Intent intent = new Intent(ScannerConnectActivity.this, PortConfigurationActivity.class);
			startActivityForResult(intent, INTENT_PORT_SETTINGS);
		}
	}

	private List<Map<String, Object>> getOperateItemData() {
		int[] PrinterID = new int[] { R.string.scanner001, R.string.gprinter002, R.string.gprinter003,
				R.string.gprinter004, R.string.gprinter005, R.string.gprinter006, R.string.gprinter007,
				R.string.gprinter008, R.string.gprinter009, R.string.gprinter010, R.string.gprinter011,
				R.string.gprinter012, R.string.gprinter013, R.string.gprinter014, R.string.gprinter015,
				R.string.gprinter016, R.string.gprinter017, R.string.gprinter018, R.string.gprinter019,
				R.string.gprinter020 };
		int[] PrinterImage = new int[] { R.drawable.scan_camear, R.drawable.ic_printer, R.drawable.ic_printer,
				R.drawable.ic_printer, R.drawable.ic_printer, R.drawable.ic_printer, R.drawable.ic_printer,
				R.drawable.ic_printer, R.drawable.ic_printer, R.drawable.ic_printer, R.drawable.ic_printer,
				R.drawable.ic_printer, R.drawable.ic_printer, R.drawable.ic_printer, R.drawable.ic_printer,
				R.drawable.ic_printer, R.drawable.ic_printer, R.drawable.ic_printer, R.drawable.ic_printer,
				R.drawable.ic_printer };
		Map<String, Object> map;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < MAX_PRINTER_CNT; i++) {
			map = new HashMap<String, Object>();
			map.put(ListViewAdapter.IMG, PrinterImage[i]);
			map.put(ListViewAdapter.TITEL, getString(PrinterID[i]));
			if (mPortParam[i].getPortOpenState() == false)
				map.put(ListViewAdapter.STATUS, getString(R.string.connect));
			else
				map.put(ListViewAdapter.STATUS, getString(R.string.cut));
			String str = getPortParamInfoString(mPortParam[i]);
			map.put(ListViewAdapter.INFO, str);
			map.put(ListViewAdapter.BT_ENABLE, "enable");
			list.add(map);
		}
		return list;
	}

	private void initPortParam() {

		Intent intent = getIntent();
		boolean[] state = intent.getBooleanArrayExtra(MainMenuActivity.CONNECT_STATUS);
		for (int i = 0; i < MAX_PRINTER_CNT; i++) {
			PortParamDataBase database = new PortParamDataBase(this);
			mPortParam[i] = new PortParameters();
			mPortParam[i] = database.queryPortParamDataBase("" + i);
			mPortParam[i].setPortOpenState(state[i]);

			blueItem.setAddress(mPortParam[i].getBluetoothAddr());
		}
	}

	private String getPortParamInfoString(PortParameters Param) {
		String info = new String();
		info = getString(R.string.port);
		int type = Param.getPortType();
		Log.d("zd", "Param.getPortType() " + type);
		if (type == PortParameters.BLUETOOTH) {
			info += getString(R.string.bluetooth);
			info += "  " + getString(R.string.address);
			info += Param.getBluetoothAddr();
		} else if (type == PortParameters.USB) {
			info += getString(R.string.usb);
			info += "  " + getString(R.string.address);
			info += Param.getUsbDeviceName();
		} else if (type == PortParameters.ETHERNET) {
			info += getString(R.string.ethernet);
			info += "  " + getString(R.string.ip_address);
			info += Param.getIpAddr();
			info += "  " + getString(R.string.port_number);
			info += Param.getPortNumber();
		} else {
			info = getString(R.string.init_port_info);
		}

		return info;
	}

	void SetPortParamToView(PortParameters Param) {
		Map<String, Object> map;
		map = mList.get(mPrinterId);
		String info = getPortParamInfoString(Param);
		map.put(ListViewAdapter.INFO, info);
		mList.set(mPrinterId, map);
		mListViewAdapter.notifyDataSetChanged();
	}

	Boolean CheckPortParamters(PortParameters param) {
		boolean rel = false;
		int type = param.getPortType();
		if (type == PortParameters.BLUETOOTH) {
			if (!param.getBluetoothAddr().equals("")) {
				rel = true;
			}
		} else if (type == PortParameters.ETHERNET) {
			if ((!param.getIpAddr().equals("")) && (param.getPortNumber() != 0)) {
				rel = true;
			}
		} else if (type == PortParameters.USB) {
			if (!param.getUsbDeviceName().equals("")) {
				rel = true;
			}
		}
		return rel;
	}

	class BleCallBack extends BleWrapperUiCallbacks.Null {

		@Override
		public void uiDeviceConnected(BluetoothGatt gatt, BluetoothDevice device) {
			gatt.discoverServices();
		}

		@Override
		public void uiDeviceDisconnected(BluetoothGatt gatt, BluetoothDevice device) {
			super.uiDeviceDisconnected(gatt, device);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ScannerConnectActivity.this.finish();
				}
			});
		}

		@Override
		public void uiGotNotification(BluetoothGatt gatt, BluetoothDevice device, BluetoothGattService service, BluetoothGattCharacteristic characteristic) {
			byte[] value = characteristic.getValue();
			String billcode = new String(value);
			Log.d("zd", "uiGotNotification: " + billcode);
			
			//过滤特殊字符
			String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"; 
			Pattern p = Pattern.compile(regEx); 
			Matcher m = p.matcher(billcode);
			billcode = m.replaceAll("").trim();

			Message msg = new Message();
			msg.what = Constant.SCANNER_BILLCODE;
			msg.obj = billcode;
			
			MyApplication.getEventBus().post(msg);
		}

		@Override
		public void uiAvailableServices(BluetoothGatt gatt, BluetoothDevice device, List<BluetoothGattService> services) {
			for (BluetoothGattService bls : services) {
				if (bls.getUuid().toString().equals(SerUUID)) {
					List<BluetoothGattCharacteristic> characteristics = bls.getCharacteristics();
					for (BluetoothGattCharacteristic ch : characteristics) {
						if (ch.getUuid().toString().equals(ChUUID)) {
							Log.d("che->", "Enabling notification for Heart Rate");
							boolean success = gatt.setCharacteristicNotification(ch, true);
							if (!success) {
								Log.d("che->", "Enabling notification failed!");
								return;
							} else {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {

										blueItem.setConn(true);
										mListViewAdapter.notifyDataSetChanged();
										CommandTools.showToast("扫描枪连接成功");
										finish();
									}
								});
								BluetoothGattDescriptor descriptor = ch.getDescriptor(BleDefinedUUIDs.Descriptor.CHAR_CLIENT_CONFIG);
								descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
								gatt.writeDescriptor(descriptor);
								return;
							}


						}
						Log.d("che->", "uiAvailableServices: " + ch.getUuid());
					}
				}
			}

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "连接失败，请重试", Toast.LENGTH_LONG).show();
					ScannerConnectActivity.this.finish();
				}
			});
		}
	}
}
