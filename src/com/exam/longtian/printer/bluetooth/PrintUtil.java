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
		tsc.addTear(ENABLE.OFF); // 撕纸模式开启
		tsc.addCls();// 清除打印缓冲区
		//		tsc.addSelectJustification(JUSTIFICATION.CENTER);//设置打印居中
		
		drawLine(tsc, 220, 0, 1, 170);//浙壹物流右侧竖线
		tsc.addText(240, 40, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
				billInfo.getBillCode());
		
		drawLine(tsc, 0, 90, 220, 1);//浙壹物流下面横线
		tsc.addText(20, 110, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"包装");
		tsc.addText(20, 140, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"类型");
		
		drawLine(tsc, 80, 90, 1, 80);//包装类型右侧小竖线
		tsc.addText(100, 120, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getPackageKindName());//包装方式

		String[] arrDate = {"", ""};
		if(!TextUtils.isEmpty(billInfo.getSendDate())){
			arrDate = billInfo.getSendDate().split(" ");
		}

		drawLine(tsc, 220, 100, 400, 1);//发货日期上面横线
		drawLine(tsc, 410, 100, 1, 70);//发货日期右侧小竖线
		tsc.addText(270, 120, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"发货日期 ");
		tsc.addText(420, 120, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				arrDate[0]);//发货日期
		
		drawLine(tsc, 0, 170, 800, 1);//条形码上面大横线
		tsc.add1DBarcode(200, 180, BARCODETYPE.CODE128M, 100, READABEL.EANBEL, ROTATION.ROTATION_0, billInfo.getBillCode());
		drawLine(tsc, 0, 310, 800, 1);//条形码下面大横线
		
		drawLine(tsc, 80, 310, 1, 600);//目的地信息右侧长竖线
		tsc.addText(20, 340, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"目的");
		tsc.addText(20, 370, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"地信");
		tsc.addText(20, 400, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"息");

		tsc.addText(170, 320, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
				billInfo.getDestSiteName());//省市名称

		drawLine(tsc, 415, 310, 1, 80);//服务方式左侧竖线
		tsc.addText(420, 330, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getServicePatternName());//服务方式

		drawLine(tsc, 80, 390, 700, 1);//收件地址上面横线
		tsc.addText(170, 410, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getRecipientsAddress());//收件地址
		drawLine(tsc, 0, 470, 800, 1);//收件地址下面横线

		tsc.addText(20, 490, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"货物");
		tsc.addText(20, 520, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"信息");

		tsc.addText(110, 490, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getTotalWeight() + " " + billInfo.getTotalVolume());//长宽高
		
		drawLine(tsc, 270, 470, 1, 80);//收件人左侧竖线
		drawLine(tsc, 390, 470, 1, 80);//收件人右侧竖线
		tsc.addText(280, 480, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getRecipientsName());//收件人名称
		
		tsc.addText(400, 480, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
				currPage + "/" + totalPage);//  1/10

		tsc.addText(100, 565, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"发货");
		tsc.addText(100, 595, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"网点");
		
		drawLine(tsc, 80, 550, 800, 1);//发货网点上面横线
		drawLine(tsc, 160, 550, 1, 100);//发货网点右侧竖线
		tsc.addText(210, 565, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getSendSiteName());
		
		drawLine(tsc, 370, 550, 1, 100);//备注左侧竖线
		tsc.addText(390, 575, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				"备注");
		drawLine(tsc, 460, 550, 1, 100);//备注右侧竖线
		tsc.addText(450, 575, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
				billInfo.getRemark());

		tsc.addFeed(2);

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
			e.printStackTrace();
		}
	}
	
	public static void drawLine(LabelCommand tsc, int x, int y, int width, int height){
		
		tsc.addBar(x, y, width, height);
	}
}
