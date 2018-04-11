package com.exam.longtian.presenter;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import android.text.TextUtils;

import com.exam.longtian.activity.inputbill.ChildBillActivity;
import com.exam.longtian.entity.ChildBillInfo;
import com.exam.longtian.entity.CompareResultInfo;
import com.exam.longtian.entity.DictInfo;
import com.exam.longtian.entity.JoinBillInfo;
import com.exam.longtian.util.API;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.OkHttpUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/** 
 * P������������
 * 
 * @author yxx
 *
 * @date 2018-1-24 ����4:34:31
 * 
 */
public class PresenterUtil {

	public static final String ORDER_TYPE_INPUT = "order_type_input";//¼��
	public static final String ORDER_TYPE_SEND = "order_type_send";//����
	public static final String ORDER_TYPE_ARRIVE = "order_type_arrive";//����
	public static final String ORDER_TYPE_RECEIVE = "order_type_receive";//�ռ�
	public static final String ORDER_TYPE_DISP = "order_type_disp";//�ɼ�

	/**
	 * ��ȡȫ�������ֵ�
	 * @param context
	 */
	public static void getDictData(Context context){

		String page = "?page=1&size=1000";
		OkHttpUtil.get(context, API.propertyDict_list + page, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				if(success){

					JSONArray jsonArray = (JSONArray) data;

					int len = jsonArray.length();
					for(int i=0; i<len; i++){

						JSONObject jsonObject = jsonArray.optJSONObject(i);
						DictInfo dictInfo = new GsonBuilder().create().fromJson(jsonObject.toString(), new TypeToken<DictInfo>(){}.getType());

						if(dictInfo.getColumnName().equals(DictInfo.DISP_MODE_PCODE)){
							DictInfo.dispList.add(dictInfo);
						}else if(dictInfo.getColumnName().equals(DictInfo.PACKAGE_KIND_PCODE)){
							DictInfo.packageList.add(dictInfo);
						}else if(dictInfo.getColumnName().equals(DictInfo.PAY_MODE_PCODE)){
							DictInfo.payList.add(dictInfo);
						}
					}
				}
			}
		});
	}

	/**
	 * ��ȡ·��
	 * @param context
	 * @param jsonObject
	 * @param callback
	 */
	public static void route_getRouteByParam(Context context, String data, final ObjectCallback callback){

		OkHttpUtil.get(context, API.route_getRouteByParam + data, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub
				callback.callback(success, message, code, data);
			}
		});
	}

	/**
	 * ǩ��
	 * @param context
	 * @param jsonObject
	 * @param callback
	 */
	public static void sign_add(Context context, JSONObject jsonObject, final ObjectCallback callback){

		OkHttpUtil.post(context, API.sign_add, jsonObject, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub
				callback.callback(success, message, code, data);
			}
		});
	}

	/**
	 * ����ɨ��-δɨ���ӵ�
	 * @param context
	 * @param callback
	 */
	public static void waybillSub_unscanedSendWaybillSubList(Context context, String orderType, String siteGcode, String billcode, final ObjectCallback callback){

		StringBuilder sb = new StringBuilder();
		sb.append("?page=1&size=1000");

		if(!TextUtils.isEmpty(siteGcode)){
			sb.append("&siteGcode=" + siteGcode);
		}

		if(!TextUtils.isEmpty(billcode)){
			sb.append("&billCode=" + billcode);
		}
		
		String url = API.waybillSub_unscanedSendWaybillSubList + sb.toString();
		if(PresenterUtil.ORDER_TYPE_ARRIVE.equals(orderType)){
			url = API.waybillSub_unscanedComeWaybillSubList + sb.toString();
		}else{
			url = API.waybillSub_unscanedSendWaybillSubList + sb.toString();
		}

		OkHttpUtil.get(context, url, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				List<ChildBillInfo> dataList = new ArrayList<ChildBillInfo>();
				if(success){

					JSONObject jsonObject = (JSONObject) data;
					JSONArray jsonArray = jsonObject.optJSONArray("rows");

					int len = jsonArray.length();
					for(int i=0; i<len; i++){

						jsonObject = jsonArray.optJSONObject(i);
						ChildBillInfo info = new GsonBuilder().create().fromJson(jsonObject.toString(), new TypeToken<ChildBillInfo>(){}.getType());

						dataList.add(info);
					}
				}

				callback.callback(success, message, code, dataList);
			}
		});
	}

	/**
	 * ����ɨ��-��ɨ���ӵ�
	 * @param context
	 * @param callback
	 */
	public static void waybillSub_scanedSendWaybillSubList(Context context, final ObjectCallback callback){

		String page = "?page=1&size=1000";
		OkHttpUtil.get(context, API.waybillSub_scanedSendWaybillSubList + page, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub
				callback.callback(success, message, code, data);
			}
		});
	}

	/**
	 * ����/����ɨ��-��ȡ���ӵ�����
	 * @param context
	 * @param callback
	 */
	public static void handover_queryHandoverList(Context context, final String orderType, String handoverTimeFrom, String handoverTimeTo, String siteGcode, String billCode, final ObjectCallback callback){

		String page = null;
		try {

			page = "?handoverTimeFrom=" + URLEncoder.encode(handoverTimeFrom, "utf-8") 
					+ "&handoverTimeTo=" + URLEncoder.encode(handoverTimeTo, "utf-8") 
					+ "&page=1&size=1000";
			if(!TextUtils.isEmpty(siteGcode)){
				page = page + "&oppositeSiteGcode=" + siteGcode;
			}

			if(!TextUtils.isEmpty(billCode)){
				page = page + "&billCode=" + billCode;
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		OkHttpUtil.get(context, API.handover_queryHandoverList + page, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				if(success){

					List<JoinBillInfo> dataList = new ArrayList<JoinBillInfo>();
					JSONObject jsonObject = (JSONObject) data;

					JSONArray jsonArray = jsonObject.optJSONArray("rows");

					int len = jsonArray.length();
					for(int i=0; i<len; i++){

						jsonObject = jsonArray.optJSONObject(i);
						JoinBillInfo joinBillInfo = new GsonBuilder().create().fromJson(jsonObject.toString(), new TypeToken<JoinBillInfo>(){}.getType());

						//��������
						if(PresenterUtil.ORDER_TYPE_SEND.equals(orderType) && joinBillInfo.getListType().equals("1")){
							dataList.add(joinBillInfo);
						}
						//��������
						else if(PresenterUtil.ORDER_TYPE_ARRIVE.equals(orderType) && joinBillInfo.getListType().equals("2")){
							dataList.add(joinBillInfo);
						}

					}

					callback.callback(success, message, code, dataList);
				}
			}
		});
	}

	/**
	 * ����handoverId��ȡ���ӵ���ϸ����
	 * @param context
	 * @param callback
	 */
	public static void handover_handoverListId(Context context, String billCode, final ObjectCallback callback){

		OkHttpUtil.get(context, API.handover_handoverListId + billCode, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				if(success){

					if(data != null){

						callback.callback(success, message, code, data);
					}
				}
			}
		});
	}

	/**
	 * �󶨽��ӵ�
	 * @param context
	 * @param jsonObject
	 * @param callback
	 */
	public static void handover_BindHandover(Context context, String orderType, JSONObject jsonObject, final ObjectCallback callback){

		String url = API.handover_sendScanBindHandover;
		if(PresenterUtil.ORDER_TYPE_ARRIVE.equals(orderType)){
			url = API.handover_comeScanBindHandover;
		}else{
			url = API.handover_sendScanBindHandover;
		}

		OkHttpUtil.post(context, url, jsonObject, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {

				callback.callback(success, message, code, data);
			}
		});
	}

	/**
	 * �������ӵ�
	 * @param context
	 * @param jsonObject
	 * @param callback
	 */
	public static void handover_add(Context context, JSONObject jsonObject, final ObjectCallback callback){

		OkHttpUtil.post(context, API.handover_add, jsonObject, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {

				if(success){
					CommandTools.showToast(message);
					callback.callback(success, message, code, data);
				}else{
					CommandTools.showToast("˾�������ڣ��������ӵ�ʧ��");
				}
			}
		});
	}

	/**
	 * ����ɨ��-��ȡ�ԱȽY���б�
	 * @param context
	 * @param siteGcode
	 * @param handoverId
	 * @param callback
	 */
	public static void scan_sendComparedResult(Context context, String orderType, String siteGcode, String handoverId, final ObjectCallback callback){

		String page = "?siteGcode="+siteGcode+ "&handoverId="+handoverId+"&page=1&size=1000";

		String url = API.scan_comeComparedResult;
		if(PresenterUtil.ORDER_TYPE_ARRIVE.equals(orderType)){
			url = API.scan_comeComparedResult;
		}else{
			url = API.scan_sendComparedResult;
		}

		OkHttpUtil.get(context, url + page, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				List<CompareResultInfo> dataList = new ArrayList<CompareResultInfo>();
				if(success){

					JSONObject jsonObject = (JSONObject) data;
					JSONArray jsonArray = jsonObject.optJSONArray("rows");

					int len = jsonArray.length();
					for(int i=0; i<len; i++){

						jsonObject = jsonArray.optJSONObject(i);
						CompareResultInfo info = new GsonBuilder().create().fromJson(jsonObject.toString(), new TypeToken<CompareResultInfo>(){}.getType());

						dataList.add(info);
					}

				}

				callback.callback(success, message, code, dataList);
			}
		});
	}

	/**
	 * ��ȡ˾���б�
	 * @param context
	 * @param callback
	 */
	public static void driver_list(Context context, final ObjectCallback callback){

		String page = "?page=1&size=1000";
		OkHttpUtil.get(context, API.driver_list + page, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				List<JoinBillInfo> dataList = new ArrayList<JoinBillInfo>();
				//				if(success){
				//
				//					JSONObject jsonObject = (JSONObject) data;
				//
				//					JSONArray jsonArray = jsonObject.optJSONArray("rows");
				//
				//					int len = jsonArray.length();
				//					for(int i=0; i<len; i++){
				//
				//						jsonObject = jsonArray.optJSONObject(i);
				//						JoinBillInfo joinBillInfo = new GsonBuilder().create().fromJson(jsonObject.toString(), new TypeToken<JoinBillInfo>(){}.getType());
				//
				//						dataList.add(joinBillInfo);
				//					}
				//				}

				callback.callback(success, message, code, dataList);
			}
		});
	}

	/**
	 * �ϴ�ͼƬ
	 * @param file
	 * @param callback
	 */
	public static void uploadImg(File file, final ObjectCallback callback){

		OkHttpUtil.uploadFile(file, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				if(success){
					callback.callback(success, message, code, data);
				}else{
					CommandTools.showToast(message);
				}
			}
		});
	}

	/**
	 * ͼƬ�ϴ��ɹ�����øýӿ�
	 * @param context
	 * @param jsonObject
	 * @param callback
	 */
	public static void waybillImage_add(Context context, JSONObject jsonObject, final ObjectCallback callback){

		OkHttpUtil.post(context, API.waybillImage_add, jsonObject, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {

				CommandTools.showToast(message);
				if(success){
					callback.callback(success, message, code, data);
				}
			}
		});
	}
}
