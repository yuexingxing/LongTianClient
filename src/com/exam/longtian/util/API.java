package com.exam.longtian.util;

public class API {

	public static String URL = "http://218.94.60.50:8088/api/";
	public static String site = "";
	
	public static String page = "1";
	public static String size = "100";

	public static final String auth_login = "auth/login";
	public static final String user_currentUserInfo = "user/currentUserInfo";//��ȡ��ǰ�û���Ϣ
	public static final String site_list = "site/list";//��ȡ����վ��
	public static final String customer_list = "customer/list/";//{siteGcode}��ȡ�����µĿͻ���Ϣ
	public static final String waybill_getReWaybillList = "waybill/getReWaybillList";//�ռ��б��ѯ
	public static final String waybill_add = "waybill/add";//�����˵�
	public static final String propertyDict_list = "propertyDict/list";//�����ֵ�
	public static final String sign_add = "sign/add";//ǩ��
	
	public static final String route_getRouteByParam = "route/getRouteByParam?";//��ȡrouteId
	
	public static final String waybill_detail = "waybill/";//��ȡ�˵���ϸ
	public static final String scan_getDispWaybillList = "scan/getDispWaybillList";//ɨ���¼
}
