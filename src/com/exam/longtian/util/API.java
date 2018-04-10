package com.exam.longtian.util;

public class API {

	public static String URL = "http://122.225.117.175:8088/api/";//"http://218.94.60.50:8088/api/";
	public static String URL_APP_UPDATE = "http://122.225.117.166:8085/LT_AppUpdate/checkNew?os=android&type=app&ver=1.0&tag=normal";
	public static String site = "";

	public static String page = "1";
	public static String size = "100";

	public static final String auth_login = "auth/login";
	public static final String user_currentUserInfo = "user/currentUserInfo";//获取当前用户信息
	public static final String site_list = "site/list";//获取所有站点
	public static final String customer_list = "customer/list/";//{siteGcode}获取网点下的客户信息

	public static final String waybill_add = "waybill/add";//新增运单
	public static final String propertyDict_list = "propertyDict/list";//属性字典

	public static final String route_getRouteByParam = "route/getRouteByParam?";//获取routeId

	public static final String waybill_getDispWaybillCountBySelf = "waybill/getDispWaybillCountBySelf";//派件统计
	public static final String waybill_getDispWaybillList = "waybill/getDispWaybillList";//派件列表查询
	
	public static final String waybill_getWaybillCountBySelf = "waybill/getWaybillCountBySelf";//收件统计
	public static final String waybill_getReWaybillList = "waybill/getReWaybillList";//收件列表查询

	public static final String waybill_detail = "waybill/";//获取运单明细 {billcode}
	public static final String scan_getDispWaybillList = "scan/getDispWaybillList";//扫描记录
	public static final String scan_getScanRecordByWaybill = "scan/getScanRecordByWaybill/";//扫描记录

	public static final String waybillSub_scanedSendWaybillSubList = "waybillSub/scanedSendWaybillSubList";//发件扫描-已扫描子单
	public static final String waybillSub_unscanedSendWaybillSubList = "waybillSub/unscanedSendWaybillSubList";//发件扫描-未扫描子单

	public static final String waybillSub_scanedComeWaybillSubList = "waybillSub/scanedComeWaybillSubList";//到件扫描-已扫描子单
	public static final String waybillSub_unscanedComeWaybillSubList = "waybillSub/unscanedComeWaybillSubList";//到件扫描-未扫描子单

	public static final String handover_queryHandoverList = "handover/queryHandoverList";//根据siteGcode(可空)和billCode(主单/子单/回单)(可空)分页获取交接单列表
	public static final String handover_add = "handover/add";//新增交接单

	public static final String scan_sendComparedResult = "scan/sendComparedResult";//分页获取对比結果列表--发件
	public static final String scan_comeComparedResult = "scan/comeComparedResult";//分页获取对比結果列表--到件

	public static final String handover_comeScanBindHandover = "handover/comeScanBindHandover";//绑定交接单--到件
	public static final String handover_sendScanBindHandover = "handover/sendScanBindHandover";//绑定交接单--发件
	
	public static final String handover_handoverListId = "/handover/";//根据handoverId获取交接单明细数据GET /handover/{handoverListId}

	public static final String driver_list = "driver/list";//司机列表

	public static final String sign_add = "sign/add";//签收
	public static final String imageServer_uploadImg = "imageServer/uploadImg";//图片上传
	public static final String waybillImage_add = "waybillImage/add";//图片上传成功后调用此接口
}
