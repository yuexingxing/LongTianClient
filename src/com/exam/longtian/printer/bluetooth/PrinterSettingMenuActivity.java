package com.exam.longtian.printer.bluetooth;

import java.util.Vector;
import com.exam.longtian.R;
import com.exam.longtian.activity.BaseActivity;
import com.exam.longtian.activity.MainMenuActivity;
import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.scanner.ScannerConnectActivity;
import com.exam.longtian.util.CommandTools;
import com.gprinter.command.EscCommand;
import com.gprinter.command.EscCommand.FONT;
import com.gprinter.command.EscCommand.HRI_POSITION;
import com.gprinter.command.EscCommand.JUSTIFICATION;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;
import com.gprinter.command.LabelCommand;
import com.gprinter.command.EscCommand.ENABLE;
import com.gprinter.command.LabelCommand.BARCODETYPE;
import com.gprinter.command.LabelCommand.BITMAP_MODE;
import com.gprinter.command.LabelCommand.DIRECTION;
import com.gprinter.command.LabelCommand.EEC;
import com.gprinter.command.LabelCommand.FONTMUL;
import com.gprinter.command.LabelCommand.FONTTYPE;
import com.gprinter.command.LabelCommand.MIRROR;
import com.gprinter.command.LabelCommand.READABEL;
import com.gprinter.command.LabelCommand.ROTATION;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

/** 
 * 打印机、蓝牙扫描枪设置主界面
 * 
 * @author yxx
 *
 * @date 2018-3-7 下午3:59:54
 * 
 */
public class PrinterSettingMenuActivity extends BaseActivity {

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentViewId(R.layout.activity_printer_setting_menu);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle("蓝牙设备设置");
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	public void connectScanner(View v){

		Intent intent = new Intent(this, ScannerConnectActivity.class);
		boolean[] state = MainMenuActivity.getConnectState();
		intent.putExtra(MainMenuActivity.CONNECT_STATUS, state);
		startActivity(intent);
		finish();
	}

	public void connectPrinter(View v){

		if (MainMenuActivity.mGpService == null) {
			CommandTools.showToast("请先连接打印机！");
			return;
		}

		Intent intent = new Intent(this, PrinterConnectDialog.class);
		boolean[] state = MainMenuActivity.getConnectState();
		intent.putExtra(MainMenuActivity.CONNECT_STATUS, state);
		this.startActivity(intent);
	}

	/**
	 * 纸张校对
	 * @param v
	 */
	public void printCheck(View v){

		LabelCommand tsc = new LabelCommand();
		tsc.addSize(80, 90); // 设置标签尺寸，按照实际尺寸设置
		tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
		tsc.addDirection(DIRECTION.BACKWARD, MIRROR.NORMAL);// 设置打印方向
		tsc.addReference(0, 0);// 设置原点坐标
		tsc.addTear(ENABLE.OFF); // 撕纸模式开启
		tsc.addCls();// 清除打印缓冲区
		
		tsc.addHome();
		
		tsc.addPrint(1, 1); // 打印标签
		tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
		Vector<Byte> datas = tsc.getCommand(); // 发送数据
		byte[] bytes = GpUtils.ByteTo_byte(datas);
		String str = Base64.encodeToString(bytes, Base64.DEFAULT);
		int rel;
		try {
			rel = MainMenuActivity.mGpService.sendLabelCommand(PrinterConnectDialog.mPrinterId, str);
			GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
			if (r != GpCom.ERROR_CODE.SUCCESS) {
				Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printTest(View v){

		//				Intent intent = new Intent(GpPrintService.ACTION_PRINT_TESTPAGE);
		//				intent.putExtra(GpPrintService.PRINTER_ID, PrinterConnectDialog.mPrinterId);
		//				sendBroadcast(intent);

		BillInfo info = new BillInfo();
		info.setBillCode("36560000025003");
		info.setSendDate(CommandTools.getTime());
		info.setSenderName("张三");
		info.setPackageKindName("托盘");
		info.setServicePatternName("门对门");
		info.setTotalWeight("220");
		info.setTotalVolume("120");
		info.setDestSiteName("上海");
		info.setRecipientsAddress("上海市杨浦区创智科技");

		PrintUtil.printLabel(info, 1, 1, null);
		//						testLabel();
		//		sendLabel();
	}

	public void testLabel() {

		LabelCommand tsc = new LabelCommand();
		tsc.addSize(80, 90); // 设置标签尺寸，按照实际尺寸设置
		tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
		tsc.addDirection(DIRECTION.BACKWARD, MIRROR.NORMAL);// 设置打印方向
		tsc.addReference(0, 0);// 设置原点坐标
		tsc.addTear(ENABLE.OFF); // 撕纸模式开启
		tsc.addCls();// 清除打印缓冲区

		//		tsc.addText(20, 20, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
		//				"———————￣￣￣￣————");
		//		tsc.addText(20, 20, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
		//				"|");//|

		tsc.addText(20, 40, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"123465");//|

		tsc.addBar(20, 20, 100, 1);

		tsc.addFeed(2);

		// 绘制一维条码
		//		tsc.add1DBarcode(20, 250, BARCODETYPE.CODE128M, 100, READABEL.DISABLE, ROTATION.ROTATION_0, "123456798");

		tsc.addPrint(1, 1); // 打印标签
		tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
		Vector<Byte> datas = tsc.getCommand(); // 发送数据
		byte[] bytes = GpUtils.ByteTo_byte(datas);
		String str = Base64.encodeToString(bytes, Base64.DEFAULT);
		int rel;
		try {
			rel = MainMenuActivity.mGpService.sendLabelCommand(PrinterConnectDialog.mPrinterId, str);
			GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
			if (r != GpCom.ERROR_CODE.SUCCESS) {
				Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendLabel() {

		LabelCommand tsc = new LabelCommand();
		tsc.addSize(120, 60); // 设置标签尺寸，按照实际尺寸设置
		tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
		tsc.addDirection(DIRECTION.FORWARD, MIRROR.NORMAL);// 设置打印方向
		tsc.addReference(0, 0);// 设置原点坐标
		tsc.addTear(ENABLE.ON); // 撕纸模式开启
		tsc.addCls();// 清除打印缓冲区

		// 绘制简体中文
		tsc.addText(20, 20, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"Welcome to use SMARNET printer!");
		// 绘制图片
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
		tsc.addBitmap(20, 50, BITMAP_MODE.OVERWRITE, b.getWidth(), b);

		tsc.addQRCode(250, 80, EEC.LEVEL_L, 5, ROTATION.ROTATION_0, " www.smarnet.cc");
		// 绘制一维条码
		tsc.add1DBarcode(20, 250, BARCODETYPE.CODE128, 100, READABEL.DISABLE, ROTATION.ROTATION_0, "SMARNET");
		tsc.addPrint(1, 1); // 打印标签
		tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
		//		tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
		Vector<Byte> datas = tsc.getCommand(); // 发送数据
		byte[] bytes = GpUtils.ByteTo_byte(datas);
		String str = Base64.encodeToString(bytes, Base64.DEFAULT);
		int rel;
		try {
			rel = MainMenuActivity.mGpService.sendLabelCommand(PrinterConnectDialog.mPrinterId, str);
			GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
			if (r != GpCom.ERROR_CODE.SUCCESS) {
				Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
