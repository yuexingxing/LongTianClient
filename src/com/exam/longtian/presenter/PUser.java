package com.exam.longtian.presenter;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.exam.longtian.MyApplication;
import com.exam.longtian.entity.User;
import com.exam.longtian.util.API;
import com.exam.longtian.util.OkHttpUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class PUser {

	/**
	 * 获取当前用户信息
	 * @param context
	 */
	public static void currentUserInfo(final Context context, final ObjectCallback callback){

		OkHttpUtil.get(context, API.user_currentUserInfo, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {

				if(success){

					JSONObject jsonObject = (JSONObject) data;

					MyApplication.mUser = new GsonBuilder().create().fromJson(jsonObject.toString(), new TypeToken<User>(){}.getType());
					if(callback != null){
						callback.callback(success, message, code, data);
					}
				}
			}
		});

	}
}
