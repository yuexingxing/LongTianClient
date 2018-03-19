package com.exam.longtian.entity;

public class ChildBillInfo {

	private String subBillCode;// "345600000320220005",
	private String billCode;// "34560000032022",
	private int weight;// 400,
	private int volume;// 500,
	private String opEmpGcode;// "E217",
	private String opEmpName;// "Ã∆¡·",
	private String opTime;// null
	
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
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
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
