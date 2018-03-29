package com.exam.longtian.entity;

import com.exam.longtian.printer.bluetooth.PrintUtil.CallBack;

public class PrintInfoData {

	public BillInfo billInfo;
	public CallBack callBack;
	public int currPage = 1;
	public int totalPage = 1;
	
	public BillInfo getBillInfo() {
		return billInfo;
	}
	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}
	public CallBack getCallBack() {
		return callBack;
	}
	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}
	public int getCurrPage() {
		return currPage;
	}
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
}
