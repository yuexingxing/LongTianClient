package com.exam.longtian;

import java.util.LinkedList;
import java.util.List;
import okhttp3.OkHttpClient;
import com.exam.longtian.entity.User;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.CommandTools.CommandToolsCallback;
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

//		initHotfix();
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

		if(msg.what == 0x0011){
			CommandTools.showToast((String) msg.obj);
		}
	}
	
	/**
	 * 初始化热修复相关信息
	 */
	private void initHotfix() {

//		int serverPos = (Integer) SharedPreferencesUtils.getParam(MyApplication.getInstance(), Constant.SP_SERVER_URL, 0);
//		if(serverPos == 0){//0-正式库 1-测试库
//			return;
//		}

		String appVersion;
		try {
			appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
		} catch (Exception e) {
			appVersion = "1.0.0";
		}

		SophixManager.getInstance().setContext(this)
		.setAppVersion(appVersion)
		.setAesKey(null)
		//.setAesKey("0123456789123456")
		.setEnableDebug(true)
		.setPatchLoadStatusStub(new PatchLoadStatusListener() {
			@Override
			public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {

				String msg = "";

				// 补丁加载回调通知
				if (code == PatchStatus.CODE_LOAD_SUCCESS) {
					// 表明补丁加载成功

					msg =  "onLoad: 成功";
				} else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
					// 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
					// 建议: 用户可以监听进入后台事件, 然后应用自杀
					msg = "onLoad: 生效需要重启";
					
					Message message = new Message();
					message.what = 0x0012;
					message.obj = msg;
					mHandler.sendMessage(message);
				} else if (code == PatchStatus.CODE_LOAD_FAIL) {
					// 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
					SophixManager.getInstance().cleanPatches();
				} else {
					// 其它错误信息, 查看PatchStatus类说明
					msg = "onLoad: 其它错误信息" + code + "/" + msg;
				}
				
				Log.v("zd", msg);
			}
		}).initialize();

		SophixManager.getInstance().queryAndLoadNewPatch();
	}
	
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

			if(msg.what == 0x0012){

				CommandTools.showHotFixDialog(CommandTools.getGlobalActivity(), "重启", "热修复成功，重启生效", new CommandToolsCallback() {

					@Override
					public void callback(int position) {

						if(position == 0){
							//							restartApp(getInstance());
							Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
							PendingIntent restartIntent = PendingIntent.getActivity(CommandTools.getGlobalActivity(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
							AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
							mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, restartIntent); // 1秒钟后重启应用

							finishAllActivities();
						}
					}
				});
			}
		};
	};


}
