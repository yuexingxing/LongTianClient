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
		tsc.addTear(ENABLE.ON); // ˺ֽģʽ����
		tsc.addCls();// �����ӡ������
		//		tsc.addSelectJustification(JUSTIFICATION.CENTER);//���ô�ӡ����
		// ���Ƽ�������
		tsc.addText(240, 20, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
				billInfo.getBillCode());
		tsc.addText(20, 80, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"��װ");
		tsc.addText(20, 110, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"����");

		tsc.addText(100, 90, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getPackageKindName());//��װ��ʽ

		String[] arrDate = {"", ""};
		if(!TextUtils.isEmpty(billInfo.getSendDate())){
			arrDate = billInfo.getSendDate().split(" ");
		}

		tsc.addText(300, 100, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"��������  " + arrDate[0]);//��������

		//��ӡ������
		tsc.add1DBarcode(200, 160, BARCODETYPE.CODE128, 100, READABEL.EANBEL, ROTATION.ROTATION_0, billInfo.getBillCode());

		tsc.addText(20, 340, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"Ŀ�ĵ���Ϣ");

		tsc.addText(170, 310, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
				billInfo.getDestSiteName());//ʡ������

		tsc.addText(420, 320, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getServicePatternName());//����ʽ

		tsc.addText(170, 380, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getRecipientsAddress());//�ռ���ַ

		tsc.addText(20, 490, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"����");
		tsc.addText(20, 520, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"��Ϣ");

		tsc.addText(110, 450, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getTotalWeight() + " " + billInfo.getTotalVolume());
		tsc.addText(280, 450, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getRecipientsName());//�ռ�������
		tsc.addText(400, 440, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
				currPage + "/" + totalPage);//  1/10

		tsc.addText(110, 555, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"����");
		tsc.addText(110, 585, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"����");
		tsc.addText(210, 565, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getSendSiteName());
		tsc.addText(400, 565, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"��ע");
		tsc.addText(450, 565, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getRemark());

		tsc.addFeed(2);

		//ռ�з�
		//		tsc.addText(100, 580, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
		//				"--");
		//		tsc.addText(100, 600, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
		//				"2ռ�з�");
		//		tsc.addText(100, 630, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
		//				"3ռ�з�");
		//		tsc.addText(100, 670, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
		//				"4ռ�з�");
		//		tsc.addText(100, 700, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
		//				"5ռ�з�");

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}