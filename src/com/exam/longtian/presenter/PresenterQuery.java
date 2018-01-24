package com.exam.longtian.presenter;

import org.json.JSONObject;

import android.content.Context;

import com.exam.longtian.entity.BillInfo;
import com.exam.longtian.entity.DictInfo;
import com.exam.longtian.util.API;
import com.exam.longtian.util.OkHttpUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class PresenterQuery {

	/**
	 * ¶©µ¥ÏêÇé
	 * @param context
	 * @param billcode
	 * @param callback
	 */
	public static void waybill_detail(Context context, String billcode, final ObjectCallback callback){

		OkHttpUtil.get(context, API.waybill_detail + billcode, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub
				
				JSONObject jsonObject = (JSONObject) data;
				BillInfo billInfo = new GsonBuilder().create().fromJson(jsonObject.toString(), new TypeToken<BillInfo>(){}.getType());

				if(success){
					callback.callback(success, message, code, billInfo);
				}
			}
		});
	}

	/**
	 * É¨Ãè¼ÇÂ¼
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
	 * É¨Ãè¼ÇÂ¼
	 * @param context
	 * @param billcode
	 * @param callback
	 */
	public static void waybill_getReWaybillList(Context context, final ObjectCallback callback){

		String page = "?page=" + API.page + "&size=" + API.size;
		OkHttpUtil.get(context, API.waybill_getReWaybillList + page, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {
				// TODO Auto-generated method stub
				callback.callback(success, message, code, data);
			}
		});
	}

}
