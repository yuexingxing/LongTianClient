package com.exam.longtian.entity;

public class ScanDetail {

	private String billCode = "";// "34560000040004",
	private String sendSiteGcode = "";// "S190",
	private String sendSiteName = "";// "嘉兴海盐",
	private String sendDate = "";// "2017-07-03 18:29:16",
	private String goodsFlowTypePcode = "";// "P80",
	private String shiftPcode = "";// "",
	private String receiverGcode = "";// "E84",
	private String registerGcode = "";// "E82",
	private String regTime = "";// "2017-07-03 18:20:37",
	private String routeId = "";// "32ff3361-cc4d-4fef-a751-7894018126df",
	private String servicePatternPcode = "";// "P62",
	private String servicePatternName = "";// "门对门",
	private String packageKindPcode = "";// "P68",
	private String packageKindName = "";// "托盘",
	private String destSiteGcode = "";// "S177",
	private String pieceNum = "";// 9,
	private String transportModePcode = "";// "",
	private String totalWeight = "";// 150,
	private String totalVolume = "";// 0.5,
	private String payModePcode = "";// "P67",
	private String payModeName = "";// "到付",
	private String receiptCode = "";// "",
	private String freight = "";// 135,
	private String agencyFund = "";// 0,
	private String daofreight = "";//0
	private String beIntoWarehouse = "";// 0,
	private String premium = "";// 0,
	private String premiumFee = "";// 0,
	private String senderCustGcode = "";// "",
	private String senderCustName = "";// "",
	private String senderPhone = "";// "057386051199",
	private String senderSmsMandatory = "";// 0,
	private String senderName = "";// "张",
	private String senderCompanyName = "";// "",
	private String senderAddress = "";// "海港五金城",
	private String recipientsCustGcode = "";// "",
	private String recipientsCustName = "";// "",
	private String recipientsPhone = "";// "13968980010",
	private String recipientsSmsMandatory = "";// 0,
	private String recipientsName = "";// "张",
	private String recipientsCompanyName = "";// "",
	private String recipientsAddress = "";// "永嘉瓯北镇楠江中路34号",
	private String remark = "";// "",
	private String recipientsCoords = "";// "",
	private String gisDispSiteGcode = "";// "",
	private String dispDistance = "";// null,
	private String realRouteId = "";// "4b75c1ef-2bab-4f4c-9d58-723d8f28b226",
	private String dispScanSiteGcode = "";// "S177",
	private String dispScanSiteName = "";// "",
	private String signTime = "";// "2017-07-10 10:51:26",
	private String signSiteGcode = "";// "S172",
	private String receiptReturnTime = "";// null,
	private String dataSource = "";// 1,
	private String status = "";// 1,
	private String opEmpGcode = "";// "",
	private String opEmpName = "";// "",
	private String opTime = "";// "2017-08-26 17:47:44",
	private String transBillLock = "";// 1,
	private String subBillcode = "";// "34560000040004-001,34560000040004-002,34560000040004-003,34560000040004-004,34560000040004-005,34560000040004-006,34560000040004-007,34560000040004-008,34560000040004-009",
	private String dispEmpgcode = "";// "",
	private String scan_TYPE_PCODE = "";// "",
	private String dispScanTime = "";// null
	
	private String signer  = "";//0签收人
	private String signSiteName  = "";//签收网点
	
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getSendSiteGcode() {
		return sendSiteGcode;
	}
	public void setSendSiteGcode(String sendSiteGcode) {
		this.sendSiteGcode = sendSiteGcode;
	}
	public String getSendSiteName() {
		return sendSiteName;
	}
	public void setSendSiteName(String sendSiteName) {
		this.sendSiteName = sendSiteName;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getGoodsFlowTypePcode() {
		return goodsFlowTypePcode;
	}
	public void setGoodsFlowTypePcode(String goodsFlowTypePcode) {
		this.goodsFlowTypePcode = goodsFlowTypePcode;
	}
	public String getShiftPcode() {
		return shiftPcode;
	}
	public void setShiftPcode(String shiftPcode) {
		this.shiftPcode = shiftPcode;
	}
	public String getReceiverGcode() {
		return receiverGcode;
	}
	public void setReceiverGcode(String receiverGcode) {
		this.receiverGcode = receiverGcode;
	}
	public String getRegisterGcode() {
		return registerGcode;
	}
	public void setRegisterGcode(String registerGcode) {
		this.registerGcode = registerGcode;
	}
	public String getRegTime() {
		return regTime;
	}
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getServicePatternPcode() {
		return servicePatternPcode;
	}
	public void setServicePatternPcode(String servicePatternPcode) {
		this.servicePatternPcode = servicePatternPcode;
	}
	public String getServicePatternName() {
		return servicePatternName;
	}
	public void setServicePatternName(String servicePatternName) {
		this.servicePatternName = servicePatternName;
	}
	public String getPackageKindPcode() {
		return packageKindPcode;
	}
	public void setPackageKindPcode(String packageKindPcode) {
		this.packageKindPcode = packageKindPcode;
	}
	public String getPackageKindName() {
		return packageKindName;
	}
	public void setPackageKindName(String packageKindName) {
		this.packageKindName = packageKindName;
	}
	public String getDestSiteGcode() {
		return destSiteGcode;
	}
	public void setDestSiteGcode(String destSiteGcode) {
		this.destSiteGcode = destSiteGcode;
	}
	public String getPieceNum() {
		return pieceNum;
	}
	public void setPieceNum(String pieceNum) {
		this.pieceNum = pieceNum;
	}
	public String getTransportModePcode() {
		return transportModePcode;
	}
	public void setTransportModePcode(String transportModePcode) {
		this.transportModePcode = transportModePcode;
	}
	public String getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(String totalWeight) {
		this.totalWeight = totalWeight;
	}
	public String getTotalVolume() {
		return totalVolume;
	}
	public void setTotalVolume(String totalVolume) {
		this.totalVolume = totalVolume;
	}
	public String getPayModePcode() {
		return payModePcode;
	}
	public void setPayModePcode(String payModePcode) {
		this.payModePcode = payModePcode;
	}
	public String getPayModeName() {
		return payModeName;
	}
	public void setPayModeName(String payModeName) {
		this.payModeName = payModeName;
	}
	public String getReceiptCode() {
		return receiptCode;
	}
	public void setReceiptCode(String receiptCode) {
		this.receiptCode = receiptCode;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	public String getAgencyFund() {
		return agencyFund;
	}
	public void setAgencyFund(String agencyFund) {
		this.agencyFund = agencyFund;
	}
	public String getBeIntoWarehouse() {
		return beIntoWarehouse;
	}
	public void setBeIntoWarehouse(String beIntoWarehouse) {
		this.beIntoWarehouse = beIntoWarehouse;
	}
	public String getPremium() {
		return premium;
	}
	public void setPremium(String premium) {
		this.premium = premium;
	}
	public String getPremiumFee() {
		return premiumFee;
	}
	public void setPremiumFee(String premiumFee) {
		this.premiumFee = premiumFee;
	}
	public String getSenderCustGcode() {
		return senderCustGcode;
	}
	public void setSenderCustGcode(String senderCustGcode) {
		this.senderCustGcode = senderCustGcode;
	}
	public String getSenderCustName() {
		return senderCustName;
	}
	public void setSenderCustName(String senderCustName) {
		this.senderCustName = senderCustName;
	}
	public String getSenderPhone() {
		return senderPhone;
	}
	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}
	public String getSenderSmsMandatory() {
		return senderSmsMandatory;
	}
	public void setSenderSmsMandatory(String senderSmsMandatory) {
		this.senderSmsMandatory = senderSmsMandatory;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderCompanyName() {
		return senderCompanyName;
	}
	public void setSenderCompanyName(String senderCompanyName) {
		this.senderCompanyName = senderCompanyName;
	}
	public String getSenderAddress() {
		return senderAddress;
	}
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
	public String getRecipientsCustGcode() {
		return recipientsCustGcode;
	}
	public void setRecipientsCustGcode(String recipientsCustGcode) {
		this.recipientsCustGcode = recipientsCustGcode;
	}
	public String getRecipientsCustName() {
		return recipientsCustName;
	}
	public void setRecipientsCustName(String recipientsCustName) {
		this.recipientsCustName = recipientsCustName;
	}
	public String getRecipientsPhone() {
		return recipientsPhone;
	}
	public void setRecipientsPhone(String recipientsPhone) {
		this.recipientsPhone = recipientsPhone;
	}
	public String getRecipientsSmsMandatory() {
		return recipientsSmsMandatory;
	}
	public void setRecipientsSmsMandatory(String recipientsSmsMandatory) {
		this.recipientsSmsMandatory = recipientsSmsMandatory;
	}
	public String getRecipientsName() {
		return recipientsName;
	}
	public void setRecipientsName(String recipientsName) {
		this.recipientsName = recipientsName;
	}
	public String getRecipientsCompanyName() {
		return recipientsCompanyName;
	}
	public void setRecipientsCompanyName(String recipientsCompanyName) {
		this.recipientsCompanyName = recipientsCompanyName;
	}
	public String getRecipientsAddress() {
		return recipientsAddress;
	}
	public void setRecipientsAddress(String recipientsAddress) {
		this.recipientsAddress = recipientsAddress;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRecipientsCoords() {
		return recipientsCoords;
	}
	public void setRecipientsCoords(String recipientsCoords) {
		this.recipientsCoords = recipientsCoords;
	}
	public String getGisDispSiteGcode() {
		return gisDispSiteGcode;
	}
	public void setGisDispSiteGcode(String gisDispSiteGcode) {
		this.gisDispSiteGcode = gisDispSiteGcode;
	}
	public String getDispDistance() {
		return dispDistance;
	}
	public void setDispDistance(String dispDistance) {
		this.dispDistance = dispDistance;
	}
	public String getRealRouteId() {
		return realRouteId;
	}
	public void setRealRouteId(String realRouteId) {
		this.realRouteId = realRouteId;
	}
	public String getDispScanSiteGcode() {
		return dispScanSiteGcode;
	}
	public void setDispScanSiteGcode(String dispScanSiteGcode) {
		this.dispScanSiteGcode = dispScanSiteGcode;
	}
	public String getDispScanSiteName() {
		return dispScanSiteName;
	}
	public void setDispScanSiteName(String dispScanSiteName) {
		this.dispScanSiteName = dispScanSiteName;
	}
	public String getSignTime() {
		return signTime;
	}
	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}
	public String getSignSiteGcode() {
		return signSiteGcode;
	}
	public void setSignSiteGcode(String signSiteGcode) {
		this.signSiteGcode = signSiteGcode;
	}
	public String getReceiptReturnTime() {
		return receiptReturnTime;
	}
	public void setReceiptReturnTime(String receiptReturnTime) {
		this.receiptReturnTime = receiptReturnTime;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getTransBillLock() {
		return transBillLock;
	}
	public void setTransBillLock(String transBillLock) {
		this.transBillLock = transBillLock;
	}
	public String getSubBillcode() {
		return subBillcode;
	}
	public void setSubBillcode(String subBillcode) {
		this.subBillcode = subBillcode;
	}
	public String getDispEmpgcode() {
		return dispEmpgcode;
	}
	public void setDispEmpgcode(String dispEmpgcode) {
		this.dispEmpgcode = dispEmpgcode;
	}
	public String getScan_TYPE_PCODE() {
		return scan_TYPE_PCODE;
	}
	public void setScan_TYPE_PCODE(String scan_TYPE_PCODE) {
		this.scan_TYPE_PCODE = scan_TYPE_PCODE;
	}
	public String getDispScanTime() {
		return dispScanTime;
	}
	public void setDispScanTime(String dispScanTime) {
		this.dispScanTime = dispScanTime;
	}
	public String getDaofreight() {
		return daofreight;
	}
	public void setDaofreight(String daofreight) {
		this.daofreight = daofreight;
	}
	public String getSigner() {
		return signer;
	}
	public void setSigner(String signer) {
		this.signer = signer;
	}
	public String getSignSiteName() {
		return signSiteName;
	}
	public void setSignSiteName(String signSiteName) {
		this.signSiteName = signSiteName;
	}

}
