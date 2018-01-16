package com.exam.longtian.util;

public class API {

	public static String URL = "http://218.94.60.50:8088/api/";
	public static String site = "";
	
	public static String page = "1";
	public static String size = "100";

	public static final String auth_login = "auth/login";
	public static final String user_currentUserInfo = "user/currentUserInfo";//获取当前用户信息
	public static final String site_list = "site/list";//获取所有站点
	public static final String customer_list = "customer/list/";//{siteGcode}获取网点下的客户信息
	public static final String waybill_getReWaybillList = "waybill/getReWaybillList";//收件列表查询
	public static final String waybill_add = "waybill/add";//新增运单
	public static final String propertyDict_list = "propertyDict/list";//属性字典
	public static final String sign_add = "sign/add";//签收
	
	public static final String route_getRouteByParam = "route/getRouteByParam?";//获取routeId
	
	public static final String waybill_detail = "waybill/";//获取运单明细
	public static final String scan_getDispWaybillList = "scan/getDispWaybillList";//扫描记录
}
