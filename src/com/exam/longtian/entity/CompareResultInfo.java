package com.exam.longtian.entity;

public class CompareResultInfo {

	private int SCAN_SUB_COUNT = 0;// 10,
	private int BE_SCAN = 0;// 1,
	private int RECEIPT_SCAN = 0;// 0,
	private String RECEIPT_CODE = "";// 0,
	private String BILL_CODE;// "55555555555555",
	private int COME_NO_SEND = 0;
	private int SEND_NO_COME = 0;
	private int UNSAN_SUB_COUNT = 0;// 0,
	private int ROW_ID = 0;// 3

	public int getSCAN_SUB_COUNT() {
		return SCAN_SUB_COUNT;
	}
	public void setSCAN_SUB_COUNT(int sCAN_SUB_COUNT) {
		SCAN_SUB_COUNT = sCAN_SUB_COUNT;
	}
	public int getBE_SCAN() {
		return BE_SCAN;
	}
	public void setBE_SCAN(int bE_SCAN) {
		BE_SCAN = bE_SCAN;
	}
	public int getRECEIPT_SCAN() {
		return RECEIPT_SCAN;
	}
	public void setRECEIPT_SCAN(int rECEIPT_SCAN) {
		RECEIPT_SCAN = rECEIPT_SCAN;
	}
	public String getBILL_CODE() {
		return BILL_CODE;
	}
	public void setBILL_CODE(String bILL_CODE) {
		BILL_CODE = bILL_CODE;
	}
	public int getUNSAN_SUB_COUNT() {
		return UNSAN_SUB_COUNT;
	}
	public void setUNSAN_SUB_COUNT(int uNSAN_SUB_COUNT) {
		UNSAN_SUB_COUNT = uNSAN_SUB_COUNT;
	}
	public int getROW_ID() {
		return ROW_ID;
	}
	public void setROW_ID(int rOW_ID) {
		ROW_ID = rOW_ID;
	}
	public int getCOME_NO_SEND() {
		return COME_NO_SEND;
	}
	public void setCOME_NO_SEND(int cOME_NO_SEND) {
		COME_NO_SEND = cOME_NO_SEND;
	}
	public int getSEND_NO_COME() {
		return SEND_NO_COME;
	}
	public void setSEND_NO_COME(int sEND_NO_COME) {
		SEND_NO_COME = sEND_NO_COME;
	}
	public String getRECEIPT_CODE() {
		return RECEIPT_CODE;
	}
	public void setRECEIPT_CODE(String rECEIPT_CODE) {
		RECEIPT_CODE = rECEIPT_CODE;
	}
}
