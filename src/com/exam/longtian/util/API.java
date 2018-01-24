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

	public static final String waybillSub_scanedSendWaybillSubList = "waybillSub/scanedSendWaybillSubList";//����ɨ��-��ɨ���ӵ�
	public static final String waybillSub_unscanedSendWaybillSubList = "waybillSub/unscanedSendWaybillSubList";//����ɨ��-δɨ���ӵ�

	public static final String waybillSub_scanedComeWaybillSubList = "waybillSub/scanedComeWaybillSubList";//����ɨ��-��ɨ���ӵ�
	public static final String waybillSub_unscanedComeWaybillSubList = "waybillSub/unscanedComeWaybillSubList";//����ɨ��-δɨ���ӵ�
	
	public static final String handover_add = "handover/add";//�������ӵ�
	public static final String scan_sendComparedResult = "scan/sendComparedResult";//��ҳ��ȡ�����ԱȽY���б�
	public static final String handover_queryHandoverList = "handover/queryHandoverList";//����siteGcode(�ɿ�)��billCode(����/�ӵ�/�ص�)(�ɿ�)��ҳ��ȡ���ӵ��б�
	public static final String handover_comeScanBindHandover = "handover/comeScanBindHandover";//�󶨽��ӵ�
	
	public static final String driver_list = "driver/list";//˾���б�
}
