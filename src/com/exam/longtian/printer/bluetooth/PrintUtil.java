package com.exam.longtian.printer.bluetooth;

import java.util.List;
import java.util.Vector;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Base64;
import com.exam.longtian.activity.MainMenuActivity;
import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.entity.PrintInfoData;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;
import com.gprinter.command.LabelCommand;
import com.gprinter.command.EscCommand.ENABLE;
import com.gprinter.command.LabelCommand.BARCODETYPE;
import com.gprinter.command.LabelCommand.DIRECTION;
import com.gprinter.command.LabelCommand.MIRROR;
import com.gprinter.command.LabelCommand.READABEL;
import com.gprinter.command.LabelCommand.ROTATION;

/** 
 * ��ӡ������
 * 
 * @author yxx
 *
 * @date 2018-3-9 ����10:15:08
 * 
 */
public class PrintUtil {

	public static abstract class CallBack {
		public abstract void callback(int pos);
	}

	public static void printLabelList(List<BillInfo> list, final CallBack callBack){

		int len = list.size();
		for(int i=0; i<len; i++){

			BillInfo billInfo = list.get(i);

			PrintInfoData printInfoData = new PrintInfoData();
			printInfoData.setBillInfo(billInfo);
			printInfoData.setCallBack(callBack);
			printInfoData.setCurrPage(i+1);
			printInfoData.setTotalPage(len);

			Message msg = new Message();
			msg.obj = printInfoData;

			mHandler.sendMessageDelayed(msg, 1000);//�ӳ�һ�뷢�ͣ���ӡ
		}
	}

	public static Handler mHandler = new Handler(){

		public void handleMessage(Message msg){

			PrintInfoData printInfoData = (PrintInfoData) msg.obj;
			printLabel(printInfoData.getBillInfo(), printInfoData.getCurrPage(), printInfoData.getTotalPage(), printInfoData.getCallBack());
		}
	};

	public static void printLabel(BillInfo billInfo, int currPage, int totalPage, final CallBack callBack){

		if(billInfo == null){
			return;
		}

		LabelCommand tsc = new LabelCommand();
		tsc.addSize(80, 80); // ���ñ�ǩ�ߴ磬����ʵ�ʳߴ�����
		tsc.addGap(0); // ���ñ�ǩ��϶������ʵ�ʳߴ����ã����Ϊ�޼�϶ֽ������Ϊ0
		tsc.addDirection(DIRECTION.BACKWARD, MIRROR.NORMAL);// ���ô�ӡ����
		tsc.addReference(0, 0);// ����ԭ������
		tsc.addTear(ENABLE.OFF); // ˺ֽģʽ����
		tsc.addCls();// �����ӡ������
		//		tsc.addSelectJustification(JUSTIFICATION.CENTER);//���ô�ӡ����
		
		drawLine(tsc, 220, 0, 1, 170);//��Ҽ�����Ҳ�����
		tsc.addText(240, 40, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
				billInfo.getBillCode());
		
		drawLine(tsc, 0, 90, 220, 1);//��Ҽ�����������
		tsc.addText(20, 110, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"��װ");
		tsc.addText(20, 140, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"����");
		
		drawLine(tsc, 80, 90, 1, 80);//��װ�����Ҳ�С����
		tsc.addText(100, 120, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getPackageKindName());//��װ��ʽ

		String[] arrDate = {"", ""};
		if(!TextUtils.isEmpty(billInfo.getSendDate())){
			arrDate = billInfo.getSendDate().split(" ");
		}

		drawLine(tsc, 220, 100, 400, 1);//���������������
		drawLine(tsc, 410, 100, 1, 70);//���������Ҳ�С����
		tsc.addText(270, 120, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"�������� ");
		tsc.addText(420, 120, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				arrDate[0]);//��������
		
		drawLine(tsc, 0, 170, 800, 1);//��������������
		tsc.add1DBarcode(200, 180, BARCODETYPE.CODE128M, 100, READABEL.EANBEL, ROTATION.ROTATION_0, billInfo.getBillCode());
		drawLine(tsc, 0, 310, 800, 1);//��������������
		
		drawLine(tsc, 80, 310, 1, 600);//Ŀ�ĵ���Ϣ�Ҳ೤����
		tsc.addText(20, 340, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"Ŀ��");
		tsc.addText(20, 370, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"����");
		tsc.addText(20, 400, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"Ϣ");

		tsc.addText(170, 320, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
				billInfo.getDestSiteName());//ʡ������

		drawLine(tsc, 415, 310, 1, 80);//����ʽ�������
		tsc.addText(420, 330, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getServicePatternName());//����ʽ

		drawLine(tsc, 80, 390, 700, 1);//�ռ���ַ�������
		tsc.addText(170, 410, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getRecipientsAddress());//�ռ���ַ
		drawLine(tsc, 0, 470, 800, 1);//�ռ���ַ�������

		tsc.addText(20, 490, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"����");
		tsc.addText(20, 520, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"��Ϣ");

		tsc.addText(110, 490, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getTotalWeight() + " " + billInfo.getTotalVolume());//�����
		
		drawLine(tsc, 270, 470, 1, 80);//�ռ����������
		drawLine(tsc, 390, 470, 1, 80);//�ռ����Ҳ�����
		tsc.addText(280, 480, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getRecipientsName());//�ռ�������
		
		tsc.addText(400, 480, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
				currPage + "/" + totalPage);//  1/10

		tsc.addText(100, 565, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"����");
		tsc.addText(100, 595, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"����");
		
		drawLine(tsc, 80, 550, 800, 1);//���������������
		drawLine(tsc, 160, 550, 1, 100);//���������Ҳ�����
		tsc.addText(210, 565, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getSendSiteName());
		
		drawLine(tsc, 370, 550, 1, 100);//��ע�������
		tsc.addText(390, 575, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"��ע");
		drawLine(tsc, 460, 550, 1, 100);//��ע�Ҳ�����
		tsc.addText(450, 575, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getRemark());

		tsc.addFeed(2);

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

				if(callBack != null){
					callBack.callback(-1);
				}
			}else{
				if(callBack != null){
					callBack.callback(0);
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public static void drawLine(LabelCommand tsc, int x, int y, int width, int height){
		
		tsc.addBar(x, y, width, height);
	}
}
