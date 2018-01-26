package com.exam.longtian.presenter;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import com.exam.longtian.entity.JoinBillInfo;
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

				CommandTools.showToast(message);
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
	 * 扫描记录
	 * @param context
	 * @param billcode
	 * @param callback
	 */
	public static void scan_getDispWaybillList(Context context, final ObjectCallback callback){

		String page = "?page=" + API.page + "&size=" + API.size ;
		OkHttpUtil.get(context, API.scan_getDispWaybillList + page, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub
				callback.callback(success, message, code, data);
			}
		});
	}


	/**
	 * 扫描记录
	 * @param context
	 * @param billcode
	 * @param callback
	 */
	public static void waybill_getReWaybillList(Context context, String orderType, final ObjectCallback callback){

		String url = API.waybill_getReWaybillList;
		if(PresenterUtil.ORDER_TYPE_DISP.equals(orderType)){
			url = API.waybill_getDispWaybillList;
		}else{
			url = API.waybill_getReWaybillList;
		}

		String page = "?page=" + API.page + "&size=" + API.size;
		OkHttpUtil.get(context, url + page, new ObjectCallback() {

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
