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
 * ��ӡ��������ɨ��ǹ����������
 * 
 * @author yxx
 *
 * @date 2018-3-7 ����3:59:54
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
		setTitle("�����豸����");
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
			CommandTools.showToast("�������Ӵ�ӡ����");
			return;
		}

		Intent intent = new Intent(this, PrinterConnectDialog.class);
		boolean[] state = MainMenuActivity.getConnectState();
		intent.putExtra(MainMenuActivity.CONNECT_STATUS, state);
		this.startActivity(intent);
	}

	/**
	 * ֽ��У��
	 * @param v
	 */
	public void printCheck(View v){

		LabelCommand tsc = new LabelCommand();
		tsc.addSize(80, 90); // ���ñ�ǩ�ߴ磬����ʵ�ʳߴ�����
		tsc.addGap(0); // ���ñ�ǩ��϶������ʵ�ʳߴ����ã����Ϊ�޼�϶ֽ������Ϊ0
		tsc.addDirection(DIRECTION.BACKWARD, MIRROR.NORMAL);// ���ô�ӡ����
		tsc.addReference(0, 0);// ����ԭ������
		tsc.addTear(ENABLE.OFF); // ˺ֽģʽ����
		tsc.addCls();// �����ӡ������
		
		tsc.addHome();
		
		tsc.addPrint(1, 1); // ��ӡ��ǩ
		tsc.addSound(2, 100); // ��ӡ��ǩ�� ��������
		Vector<Byte> datas = tsc.getCommand(); // ��������
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
		info.setSenderName("����");
		info.setPackageKindName("����");
		info.setServicePatternName("�Ŷ���");
		info.setTotalWeight("220");
		info.setTotalVolume("120");
		info.setDestSiteName("�Ϻ�");
		info.setRecipientsAddress("�Ϻ������������ǿƼ�");

		PrintUtil.printLabel(info, 1, 1, null);
		//						testLabel();
		//		sendLabel();
	}

	public void testLabel() {

		LabelCommand tsc = new LabelCommand();
		tsc.addSize(80, 90); // ���ñ�ǩ�ߴ磬����ʵ�ʳߴ�����
		tsc.addGap(0); // ���ñ�ǩ��϶������ʵ�ʳߴ����ã����Ϊ�޼�϶ֽ������Ϊ0
		tsc.addDirection(DIRECTION.BACKWARD, MIRROR.NORMAL);// ���ô�ӡ����
		tsc.addReference(0, 0);// ����ԭ������
		tsc.addTear(ENABLE.OFF); // ˺ֽģʽ����
		tsc.addCls();// �����ӡ������

		//		tsc.addText(20, 20, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
		//				"������������������������������");
		//		tsc.addText(20, 20, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
		//				"|");//|

		tsc.addText(20, 40, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"123465");//|

		tsc.addBar(20, 20, 100, 1);

		tsc.addFeed(2);

		// ����һά����
		//		tsc.add1DBarcode(20, 250, BARCODETYPE.CODE128M, 100, READABEL.DISABLE, ROTATION.ROTATION_0, "123456798");

		tsc.addPrint(1, 1); // ��ӡ��ǩ
		tsc.addSound(2, 100); // ��ӡ��ǩ�� ��������
		Vector<Byte> datas = tsc.getCommand(); // ��������
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
		tsc.addSize(120, 60); // ���ñ�ǩ�ߴ磬����ʵ�ʳߴ�����
		tsc.addGap(0); // ���ñ�ǩ��϶������ʵ�ʳߴ����ã����Ϊ�޼�϶ֽ������Ϊ0
		tsc.addDirection(DIRECTION.FORWARD, MIRROR.NORMAL);// ���ô�ӡ����
		tsc.addReference(0, 0);// ����ԭ������
		tsc.addTear(ENABLE.ON); // ˺ֽģʽ����
		tsc.addCls();// �����ӡ������

		// ���Ƽ�������
		tsc.addText(20, 20, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
				"Welcome to use SMARNET printer!");
		// ����ͼƬ
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
		tsc.addBitmap(20, 50, BITMAP_MODE.OVERWRITE, b.getWidth(), b);

		tsc.addQRCode(250, 80, EEC.LEVEL_L, 5, ROTATION.ROTATION_0, " www.smarnet.cc");
		// ����һά����
		tsc.add1DBarcode(20, 250, BARCODETYPE.CODE128, 100, READABEL.DISABLE, ROTATION.ROTATION_0, "SMARNET");
		tsc.addPrint(1, 1); // ��ӡ��ǩ
		tsc.addSound(2, 100); // ��ӡ��ǩ�� ��������
		//		tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
		Vector<Byte> datas = tsc.getCommand(); // ��������
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
