package com.exam.longtian;

import java.util.LinkedList;
import java.util.List;
import okhttp3.OkHttpClient;
import com.exam.longtian.entity.User;
import com.exam.longtian.scanner.ScannerConnectActivity;
import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.app.Application;
import android.os.Message;

public class MyApplication extends Application{

	public static MyApplication instance;
	public List<Activity> activityList = new LinkedList<Activity>();
	public static OkHttpClient mOkHttpClient;

	public static User mUser = new User();
	public static String mToken = "";
	private static EventBus eventBus;

	public void onCreate() {
		super.onCreate();
		instance = this;
		mOkHttpClient = new OkHttpClient();

		if (eventBus == null) {
			eventBus = EventBus.getDefault();
			eventBus.register(this);
		}

	}

	/**
	 * 获取单例
	 * 
	 * @return
	 */
	public static MyApplication getInstance() {

		return instance;
	}

	/**
	 * 添加Activity到容器中
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {

		if (!MyApplication.getInstance().activityList.contains(activity)) {
			MyApplication.getInstance().activityList.add(activity);
		}
	}

	/**
	 * 释放所有的Activity
	 */
	public static void finishAllActivities() {

		if(ScannerConnectActivity.bleWrapper != null){
			ScannerConnectActivity.bleWrapper.disconnect();
			ScannerConnectActivity.bleWrapper = null;
		}

		for (Activity activity : MyApplication.getInstance().activityList) {
			if (activity != null) {
				activity.finish();
			}
		}

		MyApplication.getInstance().activityList.clear();
		System.exit(0);
	}

	/**
	 * 回收activity
	 */
	public static void clearActivityList() {

		for (Activity activity : MyApplication.getInstance().activityList) {
			if (activity != null) {
				activity.finish();
			}
		}

		MyApplication.getInstance().activityList.clear();
	}

	public static EventBus getEventBus() {

		if (eventBus == null) {
			eventBus.register(MyApplication.getInstance());
		}

		return eventBus;
	}

	// 不能删，否则扫描有问题
	public void onEventMainThread(Message msg) {

	}
}
