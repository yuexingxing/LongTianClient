package com.exam.longtian.entity;

import java.util.ArrayList;
import java.util.List;

public class DictInfo {

	public static List<DictInfo> packageList = new ArrayList<DictInfo>();
	public static List<DictInfo> dispList = new ArrayList<DictInfo>();
	public static List<DictInfo> payList = new ArrayList<DictInfo>();

	public static final String PACKAGE_KIND_PCODE = "PACKAGE_KIND_PCODE";//物品类别
	public static final String DISP_MODE_PCODE = "DISP_MODE_PCODE";//服务方式
	public static final String PAY_MODE_PCODE = "PAY_MODE_PCODE";//支付方式
	public static final String TRANS_TYPE_PCODE = "TRANS_TYPE_PCODE";//运输方式
	public static final String SITE_LEVEL = "SITE_LEVEL";//网点等级

	private String propertyGcode;// "P67",
	private String propertyShortcode;// "2",
	private String columnName;// "PAY_MODE_PCODE",
	private String caption;// "支付方式",
	private String propertyValue;// "到付",
	private int sortNo;// 4,
	private String opEmpGcode;// "E1",
	private String opEmpName;// "admin",
	private String opTime;// "2014-05-09 17:06:24"

	public String getPropertyGcode() {
		return propertyGcode;
	}
	public void setPropertyGcode(String propertyGcode) {
		this.propertyGcode = propertyGcode;
	}
	public String getPropertyShortcode() {
		return propertyShortcode;
	}
	public void setPropertyShortcode(String propertyShortcode) {
		this.propertyShortcode = propertyShortcode;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	public int getSortNo() {
		return sortNo;
	}
	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
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
