package com.exam.longtian.presenter;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

/** 
 * P层网络请求类
 * 
 * @author yxx
 *
 * @date 2018-1-24 下午4:34:31
 * 
 */
public class PresenterUtil {

	public static final String ORDER_TYPE_SEND = "order_type_send";//发件
	public static final String ORDER_TYPE_ARRIVE = "order_type_arrive";//到件
	public static final String ORDER_TYPE_RECEIVE = "order_type_receive";//收件
	public static final String ORDER_TYPE_DISP = "order_type_disp";//派件

	/**
	 * 获取全部属性字典
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
	 * 获取路由
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
	 * 签收
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
	 * 发件扫描-未扫描子单
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
	 * 发件扫描-已扫描子单
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
	 * 发件扫描-获取交接单数据
	 * @param context
	 * @param callback
	 */
	public static void handover_queryHandoverList(Context context, String handoverTimeFrom, String handoverTimeTo, String siteGcode, String billCode, final ObjectCallback callback){

		String page = null;
		try {
			page = "?handoverTimeFrom=" + URLEncoder.encode(handoverTimeFrom, "utf-8") + "&handoverTimeTo=" + URLEncoder.encode(handoverTimeTo, "utf-8") 
					+ "&siteGcode=" + siteGcode + "&billCode=" + billCode 
					+ "&page=1&size=1000";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
	 * 绑定交接单
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
	 * 新增交接单
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
	 * 发件扫描-获取对比結果列表
	 * @param context
	 * @param siteGcode
	 * @param handoverId
	 * @param callback
	 */
	public static void scan_sendComparedResult(Context context, String orderType, String siteGcode, String handoverId, final ObjectCallback callback){

		String page = "?siteGcode="+siteGcode+ "&handoverId="+handoverId+"&page=1&size=1000";

		String url = API.scan_comeComparedResult;
		if(PresenterUtil.ORDER_TYPE_ARRIVE.equals(orderType)){
			url = API.scan_sendComparedResult;
		}else{
			url = API.scan_comeComparedResult;
		}

		OkHttpUtil.get(context, url + page, new ObjectCallback() {

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
	 * 获取司机列表
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
	 * 上传图片
	 * @param file
	 * @param callback
	 */
	public static void uploadImg(File file, final ObjectCallback callback){

		OkHttpUtil.uploadFile(file, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub
				CommandTools.showToast(message);

				if(success){
					callback.callback(success, message, code, data);
				}
			}
		});
	}

	/**
	 * 图片上传成功后调用该接口
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
