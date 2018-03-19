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

		if(msg.what == 0x0011){
			CommandTools.showToast((String) msg.obj);
		}
	}
	
	/**
	 * ��ʼ�����޸������Ϣ
	 */
	private void initHotfix() {

//		int serverPos = (Integer) SharedPreferencesUtils.getParam(MyApplication.getInstance(), Constant.SP_SERVER_URL, 0);
//		if(serverPos == 0){//0-��ʽ�� 1-���Կ�
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

				// �������ػص�֪ͨ
				if (code == PatchStatus.CODE_LOAD_SUCCESS) {
					// �����������سɹ�

					msg =  "onLoad: �ɹ�";
				} else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
					// �����²�����Ч��Ҫ����. �����߿���ʾ�û�����ǿ������;
					// ����: �û����Լ��������̨�¼�, Ȼ��Ӧ����ɱ
					msg = "onLoad: ��Ч��Ҫ����";
					
					Message message = new Message();
					message.what = 0x0012;
					message.obj = msg;
					mHandler.sendMessage(message);
				} else if (code == PatchStatus.CODE_LOAD_FAIL) {
					// �ڲ������쳣, �Ƽ���ʱ��ձ��ز���, ��ֹʧ�ܲ����ظ�����
					SophixManager.getInstance().cleanPatches();
				} else {
					// ����������Ϣ, �鿴PatchStatus��˵��
					msg = "onLoad: ����������Ϣ" + code + "/" + msg;
				}
				
				Log.v("zd", msg);
			}
		}).initialize();

		SophixManager.getInstance().queryAndLoadNewPatch();
	}
	
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

			if(msg.what == 0x0012){

				CommandTools.showHotFixDialog(CommandTools.getGlobalActivity(), "����", "���޸��ɹ���������Ч", new CommandToolsCallback() {

					@Override
					public void callback(int position) {

						if(position == 0){
							//							restartApp(getInstance());
							Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
							PendingIntent restartIntent = PendingIntent.getActivity(CommandTools.getGlobalActivity(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
							AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
							mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, restartIntent); // 1���Ӻ�����Ӧ��

							finishAllActivities();
						}
					}
				});
			}
		};
	};


}
