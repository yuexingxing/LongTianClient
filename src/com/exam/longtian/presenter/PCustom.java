package com.exam.longtian.presenter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import com.exam.longtian.MyApplication;
import com.exam.longtian.entity.CustomInfo;
import com.exam.longtian.util.API;
import com.exam.longtian.util.OkHttpUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class PCustom {

	/**
	 * 获取当前站点下的收件客户
	 * @param context
	 * @param callback
	 */
	public static void customer_list(Context context, final ObjectCallback callback){

		OkHttpUtil.get(context, API.customer_list + MyApplication.mUser.getOwnSiteGcode(), new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {

				if(success){

					JSONArray jsonArray = (JSONArray) data;
					List<CustomInfo> customerList = new ArrayList<CustomInfo>();

					for(int i=0; i<jsonArray.length(); i++){

						JSONObject jsonObject = jsonArray.optJSONObject(i);
						CustomInfo siteInfo = new GsonBuilder().create().fromJson(jsonObject.toString(), new TypeToken<CustomInfo>(){}.getType());

						customerList.add(siteInfo);
					}

					callback.callback(success, message, code, customerList);
				}else{

				}
			}

		});
	}
}
