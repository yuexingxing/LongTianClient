package com.exam.longtian;

import java.util.LinkedList;
import java.util.List;
import okhttp3.OkHttpClient;
import com.exam.longtian.entity.User;
import com.exam.longtian.scanner.ScanGunKeyEventHelper;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.CommandTools.CommandToolsCallback;
import com.exam.longtian.util.Constant;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

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
	 * ��ȡ����
	 * 
	 * @return
	 */
	public static MyApplication getInstance() {

		return instance;
	}

	/**
	 * ���Activity��������
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {

		if (!MyApplication.getInstance().activityList.contains(activity)) {
			MyApplication.getInstance().activityList.add(activity);
		}
	}

	/**
	 * �ͷ����е�Activity
	 */
	public static void finishAllActivities() {

		for (Activity activity : MyApplication.getInstance().activityList) {
			if (activity != null) {
				activity.finish();
			}
		}

		MyApplication.getInstance().activityList.clear();
		System.exit(0);
	}

	/**
	 * ����activity
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

	// ����ɾ������ɨ��������
	public void onEventMainThread(Message msg) {

	}
}
