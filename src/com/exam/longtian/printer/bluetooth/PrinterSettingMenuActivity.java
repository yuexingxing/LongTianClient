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

	public void printTest(View v){

		//				Intent intent = new Intent(GpPrintService.ACTION_PRINT_TESTPAGE);
		//				intent.putExtra(GpPrintService.PRINTER_ID, PrinterConnectDialog.mPrinterId);
		//				sendBroadcast(intent);

		BillInfo info = new BillInfo();
		info.setBillCode("1234567890");
		info.setSendDate(CommandTools.getTime());
		info.setSenderName("张三");
		info.setPackageKindName("托盘");
		info.setServicePatternName("门对门");
		info.setTotalWeight("220");
		info.setTotalVolume("120");
		info.setDestSiteName("上海");
		info.setRecipientsAddress("上海市杨浦区创智科技");

		//		PrintUtil.printLabel(info, null);
		testLabel();
	}

	void sendReceipt() {

		EscCommand esc = new EscCommand();
		esc.addInitializePrinter();
		esc.addPrintAndFeedLines((byte) 3);
		esc.addSelectJustification(JUSTIFICATION.CENTER);// 设置打印居中
		esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF, ENABLE.ON, ENABLE.ON, ENABLE.OFF);// 设置为倍高倍宽
		esc.addText("Sample\n"); // 打印文字
		esc.addPrintAndLineFeed();

		/* 打印文字 */
		esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF);// 取消倍高倍宽
		esc.addSelectJustification(JUSTIFICATION.LEFT);// 设置打印左对齐
		esc.addText("Print text\n"); // 打印文字
		esc.addText("Welcome to use SMARNET printer!\n"); // 打印文字

		/* 打印繁体中文 需要打印机支持繁体字库 */
		String message = "佳博智匯票據打印機\n";
		// esc.addText(message,"BIG5");
		esc.addText(message, "GB2312");
		esc.addPrintAndLineFeed();

		/* 绝对位置 具体详细信息请查看GP58编程手册 */
		esc.addText("智汇");
		esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
		esc.addSetAbsolutePrintPosition((short) 6);
		esc.addText("网络");
		esc.addSetAbsolutePrintPosition((short) 10);
		esc.addText("设备");
		esc.addPrintAndLineFeed();

		/* 打印图片 */
		// esc.addText("Print bitmap!\n"); // 打印文字
		// Bitmap b = BitmapFactory.decodeResource(getResources(),
		// R.drawable.gprinter);
		// esc.addRastBitImage(b, b.getWidth(), 0); // 打印图片

		/* 打印一维条码 */
		esc.addText("Print code128\n"); // 打印文字
		esc.addSelectPrintingPositionForHRICharacters(HRI_POSITION.BELOW);//
		// 设置条码可识别字符位置在条码下方
		esc.addSetBarcodeHeight((byte) 60); // 设置条码高度为60点
		esc.addSetBarcodeWidth((byte) 1); // 设置条码单元宽度为1
		esc.addCODE128(esc.genCodeB("SMARNET")); // 打印Code128码
		esc.addPrintAndLineFeed();

		/*
		 * QRCode命令打印 此命令只在支持QRCode命令打印的机型才能使用。 在不支持二维码指令打印的机型上，则需要发送二维条码图片
		 */
		esc.addText("Print QRcode\n"); // 打印文字
		esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31); // 设置纠错等级
		esc.addSelectSizeOfModuleForQRCode((byte) 3);// 设置qrcode模块大小
		esc.addStoreQRCodeData("www.smarnet.cc");// 设置qrcode内容
		esc.addPrintQRCode();// 打印QRCode
		esc.addPrintAndLineFeed();

		/* 打印文字 */
		esc.addSelectJustification(JUSTIFICATION.CENTER);// 设置打印左对齐
		esc.addText("Completed!\r\n"); // 打印结束
		esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
		// esc.addGeneratePluseAtRealtime(LabelCommand.FOOT.F2, (byte) 8);

		esc.addPrintAndFeedLines((byte) 8);

		Vector<Byte> datas = esc.getCommand(); // 发送数据
		byte[] bytes = GpUtils.ByteTo_byte(datas);
		String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
		int rs;
		try {
			rs = MainMenuActivity.mGpService.sendEscCommand(PrinterConnectDialog.mPrinterId, sss);
			GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
			if (r != GpCom.ERROR_CODE.SUCCESS) {
				Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testEsc(){

		EscCommand esc = new EscCommand();
		esc.addPrintAndFeedLines((byte)3);
		esc.addSelectJustification(JUSTIFICATION.CENTER);//设置打印居中
		esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF,ENABLE.ON, ENABLE.ON, ENABLE.OFF);//设置为倍高倍宽
		esc.addText("Sample\n");   //  打印文字
		esc.addPrintAndLineFeed();

		/*打印文字*/
		esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF,ENABLE.OFF, ENABLE.OFF, ENABLE.OFF);//取消倍高倍宽
		esc.addSelectJustification(JUSTIFICATION.LEFT);//设置打印左对齐
		esc.addText("Print text\n");   //  打印文字
		esc.addText("Welcome to use Gprinter!\n");   //  打印文字
		esc.addPrintAndLineFeed();

		/*打印图片*/
		esc.addText("Print bitmap!\n");   //  打印文字
		Bitmap b = BitmapFactory.decodeResource(getResources(),
				R.drawable.gprinter);
		esc.addRastBitImage(b,b.getWidth(),0);   //打印图片

		/*打印一维条码*/
		esc.addText("Print code128\n");   //  打印文字
		esc.addSelectPrintingPositionForHRICharacters(HRI_POSITION.BELOW);//设置条码可识别字符位置在条码下方
		esc.addSetBarcodeHeight((byte)60); //设置条码高度为60点
		esc.addCODE128("Gprinter");  //打印Code128码
		esc.addPrintAndLineFeed();

		/*QRCode命令打印
	            此命令只在支持QRCode命令打印的机型才能使用。
	            在不支持二维码指令打印的机型上，则需要发送二维条码图片
		 */
		esc.addText("Print QRcode\n");   //  打印文字    
		esc.addSelectErrorCorrectionLevelForQRCode((byte)0x31); //设置纠错等级
		esc.addSelectSizeOfModuleForQRCode((byte)3);//设置qrcode模块大小
		esc.addStoreQRCodeData("www.gprinter.com.cn");//设置qrcode内容
		esc.addPrintQRCode();//打印QRCode
		esc.addPrintAndLineFeed();

		/*打印文字*/
		esc.addSelectJustification(JUSTIFICATION.CENTER);//设置打印左对齐
		esc.addText("Completed!\r\n");   //  打印结束
		esc.addPrintAndFeedLines((byte)8);
		Vector<Byte> datas = esc.getCommand(); //发送数据
		Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
		byte[] bytes = CommandTools.ObjectToByte(Bytes);
		String str = Base64.encodeToString(bytes, Base64.DEFAULT);
		int rel;
		try {
			rel = MainMenuActivity.mGpService.sendEscCommand(PrinterConnectDialog.mPrinterId, str);
			GpCom.ERROR_CODE r=GpCom.ERROR_CODE.values()[rel];
			if(r != GpCom.ERROR_CODE.SUCCESS){
				Toast.makeText(getApplicationContext(),GpCom.getErrorText(r),
						Toast.LENGTH_SHORT).show();    
			}            
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testLabel() {

		LabelCommand tsc = new LabelCommand();
		tsc.addSize(120, 60); // 设置标签尺寸，按照实际尺寸设置
		tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
		tsc.addDirection(DIRECTION.FORWARD, MIRROR.NORMAL);// 设置打印方向
		tsc.addReference(0, 0);// 设置原点坐标
		tsc.addTear(ENABLE.ON); // 撕纸模式开启
		tsc.addCls();// 清除打印缓冲区

		tsc.addText(20, 20, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"———————￣￣￣￣————");//|
		tsc.addText(20, 20, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"|");//|
		tsc.addText(20, 50, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"|");//|
		tsc.addText(20, 80, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"|");//|

		// 绘制一维条码
		tsc.add1DBarcode(20, 250, BARCODETYPE.CODE128M, 100, READABEL.DISABLE, ROTATION.ROTATION_0, "123456798");

		tsc.addPrint(1, 1); // 打印标签
		tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
		tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
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
		tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
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
