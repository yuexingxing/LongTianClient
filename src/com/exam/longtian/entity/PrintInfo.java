package com.exam.longtian.entity;

/** 
 * 打印相关数据
 * 
 * @author yxx
 *
 * @date 2018-3-9 上午9:57:11
 * 
 */
public class PrintInfo {

	private String orderNo;
	private String packageType;//包装类型
	private String sendTime;//发货日期
	private String destCity;//目的地城市
	private String destAddress;//目的地地址

	private String length;
	private String width;
	private String height;
	private String weight;
	private String v3;

	private String sender;//发件人
	private String sendSiteName;//发货网点
	private String memo;//备注
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getPackageType() {
		return packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getDestCity() {
		return destCity;
	}
	public void setDestCity(String destCity) {
		this.destCity = destCity;
	}
	public String getDestAddress() {
		return destAddress;
	}
	public void setDestAddress(String destAddress) {
		this.destAddress = destAddress;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getV3() {
		return v3;
	}
	public void setV3(String v3) {
		this.v3 = v3;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSendSiteName() {
		return sendSiteName;
	}
	public void setSendSiteName(String sendSiteName) {
		this.sendSiteName = sendSiteName;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

}
