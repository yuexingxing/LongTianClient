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
 * 打印操作类
 * 
 * @author yxx
 *
 * @date 2018-3-9 上午10:15:08
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

			mHandler.sendMessageDelayed(msg, 1000);//延迟一秒发送，打印
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
		tsc.addSize(80, 80); // 设置标签尺寸，按照实际尺寸设置
		tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
		tsc.addDirection(DIRECTION.BACKWARD, MIRROR.NORMAL);// 设置打印方向
		tsc.addReference(0, 0);// 设置原点坐标
		tsc.addTear(ENABLE.ON); // 撕纸模式开启
		tsc.addCls();// 清除打印缓冲区
		//		tsc.addSelectJustification(JUSTIFICATION.CENTER);//设置打印居中
		// 绘制简体中文
		tsc.addText(240, 20, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
				billInfo.getBillCode());
		tsc.addText(20, 80, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"包装");
		tsc.addText(20, 110, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"类型");

		tsc.addText(100, 90, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getPackageKindName());//包装方式

		String[] arrDate = {"", ""};
		if(!TextUtils.isEmpty(billInfo.getSendDate())){
			arrDate = billInfo.getSendDate().split(" ");
		}

		tsc.addText(300, 100, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"发货日期  " + arrDate[0]);//发货日期

		//打印条形码
		tsc.add1DBarcode(200, 160, BARCODETYPE.CODE128, 100, READABEL.EANBEL, ROTATION.ROTATION_0, billInfo.getBillCode());

		tsc.addText(20, 340, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"目的地信息");

		tsc.addText(170, 310, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
				billInfo.getDestSiteName());//省市名称

		tsc.addText(420, 320, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getServicePatternName());//服务方式

		tsc.addText(170, 380, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getRecipientsAddress());//收件地址

		tsc.addText(20, 490, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"货物");
		tsc.addText(20, 520, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"信息");

		tsc.addText(110, 450, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getTotalWeight() + " " + billInfo.getTotalVolume());
		tsc.addText(280, 450, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getRecipientsName());//收件人名称
		tsc.addText(400, 440, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
				currPage + "/" + totalPage);//  1/10

		tsc.addText(110, 555, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"发货");
		tsc.addText(110, 585, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"网点");
		tsc.addText(210, 565, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getSendSiteName());
		tsc.addText(400, 565, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"备注");
		tsc.addText(450, 565, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getRemark());

		tsc.addFeed(2);

		//占行符
		//		tsc.addText(100, 580, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
		//				"--");
		//		tsc.addText(100, 600, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
		//				"2占行符");
		//		tsc.addText(100, 630, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
		//				"3占行符");
		//		tsc.addText(100, 670, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
		//				"4占行符");
		//		tsc.addText(100, 700, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
		//				"5占行符");

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
