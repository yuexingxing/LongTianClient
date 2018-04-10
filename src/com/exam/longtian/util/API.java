package com.exam.longtian.util;

public class API {

	public static String URL = "http://122.225.117.175:8088/api/";//"http://218.94.60.50:8088/api/";
	public static String URL_APP_UPDATE = "http://122.225.117.166:8085/LT_AppUpdate/checkNew?os=android&type=app&ver=1.0&tag=normal";
	public static String site = "";

	public static String page = "1";
	public static String size = "100";

	public static final String auth_login = "auth/login";
	public static final String user_currentUserInfo = "user/currentUserInfo";//��ȡ��ǰ�û���Ϣ
	public static final String site_list = "site/list";//��ȡ����վ��
	public static final String customer_list = "customer/list/";//{siteGcode}��ȡ�����µĿͻ���Ϣ

	public static final String waybill_add = "waybill/add";//�����˵�
	public static final String propertyDict_list = "propertyDict/list";//�����ֵ�

	public static final String route_getRouteByParam = "route/getRouteByParam?";//��ȡrouteId

	public static final String waybill_getDispWaybillCountBySelf = "waybill/getDispWaybillCountBySelf";//�ɼ�ͳ��
	public static final String waybill_getDispWaybillList = "waybill/getDispWaybillList";//�ɼ��б��ѯ
	
	public static final String waybill_getWaybillCountBySelf = "waybill/getWaybillCountBySelf";//�ռ�ͳ��
	public static final String waybill_getReWaybillList = "waybill/getReWaybillList";//�ռ��б��ѯ

	public static final String waybill_detail = "waybill/";//��ȡ�˵���ϸ {billcode}
	public static final String scan_getDispWaybillList = "scan/getDispWaybillList";//ɨ���¼
	public static final String scan_getScanRecordByWaybill = "scan/getScanRecordByWaybill/";//ɨ���¼

	public static final String waybillSub_scanedSendWaybillSubList = "waybillSub/scanedSendWaybillSubList";//����ɨ��-��ɨ���ӵ�
	public static final String waybillSub_unscanedSendWaybillSubList = "waybillSub/unscanedSendWaybillSubList";//����ɨ��-δɨ���ӵ�

	public static final String waybillSub_scanedComeWaybillSubList = "waybillSub/scanedComeWaybillSubList";//����ɨ��-��ɨ���ӵ�
	public static final String waybillSub_unscanedComeWaybillSubList = "waybillSub/unscanedComeWaybillSubList";//����ɨ��-δɨ���ӵ�

	public static final String handover_queryHandoverList = "handover/queryHandoverList";//����siteGcode(�ɿ�)��billCode(����/�ӵ�/�ص�)(�ɿ�)��ҳ��ȡ���ӵ��б�
	public static final String handover_add = "handover/add";//�������ӵ�

	public static final String scan_sendComparedResult = "scan/sendComparedResult";//��ҳ��ȡ�ԱȽY���б�--����
	public static final String scan_comeComparedResult = "scan/comeComparedResult";//��ҳ��ȡ�ԱȽY���б�--����

	public static final String handover_comeScanBindHandover = "handover/comeScanBindHandover";//�󶨽��ӵ�--����
	public static final String handover_sendScanBindHandover = "handover/sendScanBindHandover";//�󶨽��ӵ�--����
	
	public static final String handover_handoverListId = "/handover/";//����handoverId��ȡ���ӵ���ϸ����GET /handover/{handoverListId}

	public static final String driver_list = "driver/list";//˾���б�

	public static final String sign_add = "sign/add";//ǩ��
	public static final String imageServer_uploadImg = "imageServer/uploadImg";//ͼƬ�ϴ�
	public static final String waybillImage_add = "waybillImage/add";//ͼƬ�ϴ��ɹ�����ô˽ӿ�
}
