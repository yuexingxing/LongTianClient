package com.exam.longtian.presenter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.entity.ScanDetail;
import com.exam.longtian.util.API;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.OkHttpUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/** 
 * 查询类-P
 * 
 * @author yxx
 *
 * @date 2018-1-25 下午1:57:35
 * 
 */
public class PresenterQuery {

	/**
	 * 订单详情
	 * @param context
	 * @param billcode
	 * @param callback
	 */
	public static void waybill_detail(Context context, String billcode, final ObjectCallback callback){

		OkHttpUtil.get(context, API.waybill_detail + billcode, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub

				if(success){

					JSONObject jsonObject = (JSONObject) data;

					BillInfo billInfo = new GsonBuilder().create().fromJson(jsonObject.toString(), new TypeToken<BillInfo>(){}.getType());
					callback.callback(success, message, code, billInfo);
				}else{
					CommandTools.showToast("运单号不存在、或已作废");
				}

			}
		});
	}

	/**
	 * 扫描记录
	 * @param context
	 * @param billcode
	 * @param callback
	 */
	public static void scan_getScanRecordByWaybill(Context context, String waybillCode, final ObjectCallback callback){

		OkHttpUtil.get(context, API.scan_getScanRecordByWaybill + waybillCode, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub
				CommandTools.showToast(message);
				if(success){

					List<BillInfo> dataList = new ArrayList<BillInfo>();
					JSONArray jsonArray = (JSONArray) data;

					int len = jsonArray.length();
					for(int i=0; i<len; i++){

						JSONObject jsonObject = jsonArray.optJSONObject(i);
						BillInfo info = new BillInfo();
						info.setSendSiteName(jsonObject.optString("scanSiteName"));
						info.setSendDate(jsonObject.optString("scanTime"));
						info.setDataSource(jsonObject.optString("trackRecord"));

						dataList.add(info);
					}

					callback.callback(success, message, code, dataList);
				}
			}
		});
	}

	/**
	 * 数据统计--收件、派件
	 * @param context
	 * @param billcode
	 * @param callback
	 */
	public static void waybill_getWaybillCountBySelf(Context context, String orderType, String startTime, String endTime, final ObjectCallback callback){

		String url = API.waybill_getWaybillCountBySelf;
		if(PresenterUtil.ORDER_TYPE_DISP.equals(orderType)){
			url = API.waybill_getDispWaybillCountBySelf;
		}else{
			url = API.waybill_getWaybillCountBySelf;
		}

		String page = null;
		try {
			page = "?bgnTime=" + URLEncoder.encode(startTime, "utf-8") + "&endTime=" + URLEncoder.encode(endTime, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		OkHttpUtil.get(context, url + page, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {

				if(success){
					callback.callback(success, message, code, data);
				}else{
					CommandTools.showToast("暂无数据");
				}
			}
		});
	}



	/**
	 * 扫描记录--收件、派件
	 * @param context
	 * @param billcode
	 * @param callback
	 */
	public static void waybill_getReWaybillList(Context context, String orderType, String startTime, String endTime, final ObjectCallback callback){

		String url = API.waybill_getReWaybillList;
		if(PresenterUtil.ORDER_TYPE_DISP.equals(orderType)){
			url = API.waybill_getDispWaybillList;
		}else{
			url = API.waybill_getReWaybillList;
		}

		String page = null;
		try {
			page = "?bgnTime=" + URLEncoder.encode(startTime, "utf-8") + "&endTime=" + URLEncoder.encode(endTime, "utf-8") + "&page=" + API.page + "&size=" + API.size;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		OkHttpUtil.get(context, url + page, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {

				if(success){

					List<ScanDetail> dataList = new ArrayList<ScanDetail>();
					JSONObject jsonObject = (JSONObject) data;

					JSONArray jsonArray = jsonObject.optJSONArray("rows");

					int len = jsonArray.length();
					for(int i=0; i<len; i++){

						jsonObject = jsonArray.optJSONObject(i);
						ScanDetail info = new GsonBuilder().create().fromJson(jsonObject.toString(), new TypeToken<ScanDetail>(){}.getType());

						dataList.add(info);
					}

					callback.callback(success, message, code, dataList);
				}else{
					CommandTools.showToast(message);
				}
			}
		});
	}

}
