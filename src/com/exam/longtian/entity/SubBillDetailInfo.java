package com.exam.longtian.entity;

public class SubBillDetailInfo {

	private String subBillCode = "";//": "365600000251000001",
	private String billCode = "";//": "36560000025100",
	private String weight = "0";//": 50,
	private String volume = "0";//": 0.01,
	private String opEmpGcode = "";//": "E217",
	private String opEmpName = "";//": "Ã∆¡·",
	private String opTime = "";//": null

	public String getSubBillCode() {
		return subBillCode;
	}
	public void setSubBillCode(String subBillCode) {
		this.subBillCode = subBillCode;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getOpEmpGcode() {
		return opEmpGcode;
	}
	public void setOpEmpGcode(String opEmpGcode) {
		this.opEmpGcode = opEmpGcode;
	}
	public String getOpEmpName() {
		return opEmpName;
	}
	public void setOpEmpName(String opEmpName) {
		this.opEmpName = opEmpName;
	}
	public String getOpTime() {
		return opTime;
	}
	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}
}
