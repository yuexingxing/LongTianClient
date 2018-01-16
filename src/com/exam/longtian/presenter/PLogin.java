package com.exam.longtian.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.exam.longtian.MyApplication;
import com.exam.longtian.interfac.ILogin;
import com.exam.longtian.util.API;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.OkHttpUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;

public class PLogin {

	/**
	 * µÇÂ¼
	 * @param context
	 * @param num
	 * @param psd
	 * @param objectCallback
	 */
	public static void auth_login(final Context context, String username, String password, String siteGcode, final ILogin iLogin){

		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("username", username);
			jsonObject.put("password", password);
			jsonObject.put("siteGcode", siteGcode);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		OkHttpUtil.post(context, API.auth_login, jsonObject, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {

				Message msg = new Message();
				msg.obj = message;
				mHandler.sendMessage(msg);

				if(success){

					JSONObject jsonObject = (JSONObject) data;
					MyApplication.mToken = jsonObject.optString("access_token");

					PUser.currentUserInfo(context, new ObjectCallback() {

						@Override
						public void callback(boolean success, String message, String code,
								Object data) {
							// TODO Auto-generated method stub

						}
					});

					PresenterUtil.getDictData(context);
					iLogin.success();
				}else{
					iLogin.failed();
				}
			}

		});

	}

	public static Handler mHandler = new Handler(){

		public void handleMessage(Message msg){

			CommandTools.showToast((String) msg.obj);
		}
	};

}
