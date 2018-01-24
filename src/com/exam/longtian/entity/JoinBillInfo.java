package com.exam.longtian.entity;

public class JoinBillInfo {

	 private String handoverId;// "2017102801720001",
	 private String siteGcode;// "S172",
	 private String listType;// 0,
	 private String oppositeSiteGcode;// "S101",
	 private String handoverTime;// "2017-10-28 11:14:56",
	 private String driverId;// null,
	 private String driverName;// "Íõ",
	 private String driverPhone;// "15868760030",
	 private String plateNumber;// "",
	 private String opEmpGcode;// "E73",
	 private String opEmpName;// "[WENZ001]Ëï²ý»Ô",
	 private String opTime;// "2017-10-28 11:07:11",
	 private String relaHandoverId;// ""
	 
	 private boolean flag = false;
	 
	public String getHandoverId() {
		return handoverId;
	}
	public void setHandoverId(String handoverId) {
		this.handoverId = handoverId;
	}
	public String getSiteGcode() {
		return siteGcode;
	}
	public void setSiteGcode(String siteGcode) {
		this.siteGcode = siteGcode;
	}
	public String getListType() {
		return listType;
	}
	public void setListType(String listType) {
		this.listType = listType;
	}
	public String getOppositeSiteGcode() {
		return oppositeSiteGcode;
	}
	public void setOppositeSiteGcode(String oppositeSiteGcode) {
		this.oppositeSiteGcode = oppositeSiteGcode;
	}
	public String getHandoverTime() {
		return handoverTime;
	}
	public void setHandoverTime(String handoverTime) {
		this.handoverTime = handoverTime;
	}
	public String getDriverId() {
		return driverId;
	}
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverPhone() {
		return driverPhone;
	}
	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}
	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
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
	public String getRelaHandoverId() {
		return relaHandoverId;
	}
	public void setRelaHandoverId(String relaHandoverId) {
		this.relaHandoverId = relaHandoverId;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
