package com.exam.longtian.presenter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.exam.longtian.entity.CompareResultInfo;
import com.exam.longtian.entity.DictInfo;
import com.exam.longtian.entity.JoinBillInfo;
import com.exam.longtian.util.API;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.OkHttpUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class PresenterUtil {

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
	public static void waybillSub_unscanedSendWaybillSubList(Context context, final ObjectCallback callback){

		String page = "?page=1&size=1000";
		OkHttpUtil.get(context, API.waybillSub_unscanedSendWaybillSubList + page, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub
				callback.callback(success, message, code, data);
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
	 * ����ɨ��-��ȡ���ӵ�����
	 * @param context
	 * @param callback
	 */
	public static void handover_queryHandoverList(Context context, final ObjectCallback callback){

		String page = "?page=1&size=1000";
		OkHttpUtil.get(context, API.handover_queryHandoverList + page, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				List<JoinBillInfo> dataList = new ArrayList<JoinBillInfo>();
				if(success){

					JSONObject jsonObject = (JSONObject) data;

					JSONArray jsonArray = jsonObject.optJSONArray("rows");

					int len = jsonArray.length();
					for(int i=0; i<len; i++){

						jsonObject = jsonArray.optJSONObject(i);
						JoinBillInfo joinBillInfo = new GsonBuilder().create().fromJson(jsonObject.toString(), new TypeToken<JoinBillInfo>(){}.getType());

						dataList.add(joinBillInfo);
					}
				}

				callback.callback(success, message, code, dataList);
			}
		});
	}

	/**
	 * �󶨽��ӵ�
	 * @param context
	 * @param jsonObject
	 * @param callback
	 */
	public static void handover_comeScanBindHandover(Context context, JSONObject jsonObject, final ObjectCallback callback){

		OkHttpUtil.post(context, API.handover_comeScanBindHandover, jsonObject, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub
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
				
				CommandTools.showToast(message);
				callback.callback(success, message, code, data);
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
	public static void scan_sendComparedResult(Context context, String siteGcode, String handoverId, final ObjectCallback callback){

		String page = "?siteGcode="+siteGcode+ "&handoverId="+handoverId+"&page=1&size=1000";
		OkHttpUtil.get(context, API.scan_sendComparedResult + page, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				CommandTools.showToast(message);
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
}
