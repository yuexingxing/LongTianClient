package com.exam.longtian.util;

import java.io.File;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.exam.longtian.MyApplication;
import com.exam.longtian.view.CustomProgress;

/** 
 * OkHttp请求封装
 * 
 * @author yxx
 *
 * @date 2018-1-3 下午2:40:17
 * 
 */
public class OkHttpUtil {

	static class CallBackData{

		private boolean success;
		private String message;
		private String code;
		private Object data;
		private ObjectCallback callback;

		public boolean isSuccess() {
			return success;
		}
		public void setSuccess(boolean success) {
			this.success = success;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public Object getData() {
			return data;
		}
		public void setData(Object data) {
			this.data = data;
		}
		public ObjectCallback getCallback() {
			return callback;
		}
		public void setCallback(ObjectCallback callback) {
			this.callback = callback;
		}
	}

	public static abstract class ObjectCallback {
		public abstract void callback(boolean success, String message, String code, Object data);
	}

	public static Request getRequest(String url){

		return null;
	}

	/**
	 * 
	 * @param url
	 * @param jsonObject
	 * @param callback
	 */
	public static void post(Context context, String url, JSONObject jsonObject, final ObjectCallback callback){

		RequestBody requestBody = FormBody.create(MediaType.parse("application/json"), jsonObject.toString()); 

		Request request = new okhttp3.Request.Builder()
		.url(API.URL + url)
		.post(requestBody)
		.addHeader("accept", "application/json")
		.addHeader("Authorization", "Bearer " + MyApplication.mToken)
		.addHeader("Content-Type", "application/json")
		.build();

		Log.i("post-data", API.URL + url);
		Log.i("post-data", jsonObject.toString());
		mHandler.sendEmptyMessage(0x0011);
		MyApplication.mOkHttpClient.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException e) {
				mHandler.sendEmptyMessage(0x0013);
				Log.e("zd", "testHttpPost ... onFailure() e=" + e);
			}

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0x0012);
				String result = arg1.body().string();
				Log.e("post-data", result);

				try {

					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.optBoolean("success");
					String message = jsonObject.optString("message");
					String code = jsonObject.optString("code");

					Object data = null;
					if(success){
						data = jsonObject.opt("data");
					}

					CallBackData callBackData = new CallBackData();
					callBackData.setSuccess(success);
					callBackData.setCode(code);
					callBackData.setData(data);
					callBackData.setMessage(message);
					callBackData.setCallback(callback);

					Message msg = new Message();
					msg.what = 0x10001;
					msg.obj = callBackData;
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 
	 * @param url
	 * @param jsonObject
	 * @param callback
	 */
	public static void get(Context context, String url, final ObjectCallback callback){

		Request request = new okhttp3.Request.Builder()
		.url(API.URL + url)
		.addHeader("accept", "application/json")
		.addHeader("Authorization", "Bearer " + MyApplication.mToken)
		.addHeader("Content-Type", "application/json")
		.build();

		Log.i("post-data", API.URL + url);
		mHandler.sendEmptyMessage(0x0011);
		MyApplication.mOkHttpClient.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException e) {
				mHandler.sendEmptyMessage(0x0013);
				Log.e("zd", "testHttpPost ... onFailure() e=" + e);
			}

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0x0012);
				String result = arg1.body().string();
				Log.e("post-data", result);

				try {

					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.optBoolean("success");
					String message = jsonObject.optString("message");
					String code = jsonObject.optString("code");

					Object data = null;
					if(success){
						data = jsonObject.opt("data");
					}

					CallBackData callBackData = new CallBackData();
					callBackData.setSuccess(success);
					callBackData.setCode(code);
					callBackData.setData(data);
					callBackData.setMessage(message);
					callBackData.setCallback(callback);

					Message msg = new Message();
					msg.what = 0x10001;
					msg.obj = callBackData;
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
	public static void uploadFile(File file, final ObjectCallback callback){

		MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
		if (file != null) {
			builder.addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
		}

		MultipartBody requestBody = builder.build();
		//构建请求
		Request request = new Request.Builder()
		.addHeader("Authorization", "Bearer " + MyApplication.mToken)
		.url(API.URL + API.imageServer_uploadImg)//地址
		.post(requestBody)//添加请求体
		.build();

		Log.e("post-data", API.URL + API.imageServer_uploadImg);
		mHandler.sendEmptyMessage(0x0011);
		MyApplication.mOkHttpClient.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException e) {
				mHandler.sendEmptyMessage(0x0012);
				Log.e("post-data", "testHttpPost ... onFailure() e=" + e);
			}

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0x0012);
				String result = arg1.body().string();
				Log.e("post-data", result);

				try {

					JSONObject jsonObject = new JSONObject(result);
					boolean success = jsonObject.optBoolean("success");
					String message = jsonObject.optString("message");
					String code = jsonObject.optString("code");

					Object data = null;
					if(success){
						data = jsonObject.opt("data");
					}

					CallBackData callBackData = new CallBackData();
					callBackData.setSuccess(success);
					callBackData.setCode(code);
					callBackData.setData(data);
					callBackData.setMessage(message);
					callBackData.setCallback(callback);

					Message msg = new Message();
					msg.what = 0x10001;
					msg.obj = callBackData;
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 
	 * @param url
	 * @param jsonObject
	 * @param callback
	 */
	public static void checkUpdate(Context context, String url, final ObjectCallback callback){

		Request request = new okhttp3.Request.Builder()
		.url(url)
		.addHeader("accept", "application/json")
		.addHeader("Authorization", "Bearer " + MyApplication.mToken)
		.addHeader("Content-Type", "application/json")
		.build();

		Log.i("post-data", url);
		mHandler.sendEmptyMessage(0x0011);
		MyApplication.mOkHttpClient.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException e) {
				mHandler.sendEmptyMessage(0x0012);
				Log.e("zd", "testHttpPost ... onFailure() e=" + e);
			}
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0x0012);
				String result = arg1.body().string();
				Log.e("post-data", result);

				try {

					JSONObject jsonObject = new JSONObject(result);

					CallBackData callBackData = new CallBackData();
					callBackData.setSuccess(true);
					callBackData.setCode("0");
					callBackData.setData(jsonObject);
					callBackData.setMessage("");
					callBackData.setCallback(callback);

					Message msg = new Message();
					msg.what = 0x10001;
					msg.obj = callBackData;
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static Handler mHandler = new Handler(){

		public void handleMessage(Message msg){

			if(msg.what == 0x0011){
				CustomProgress.showDialog(CommandTools.getGlobalActivity(), "数据加载中", true, null);
			}else if(msg.what == 0x0012){
				CustomProgress.dissDialog();
			}else if(msg.what == 0x0013){
				CustomProgress.dissDialog();
				CommandTools.showToast("服务器连接失败");
			}else if(msg.what == 0x10001){

				CallBackData callBackData = (CallBackData) msg.obj;

				if(callBackData.getCallback() != null){
					callBackData.getCallback().callback(callBackData.isSuccess(), callBackData.getMessage(), callBackData.getCode(), callBackData.getData());
				}
			}
		}
	};
}
