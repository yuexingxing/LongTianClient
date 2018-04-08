package com.exam.longtian.activity;

import org.json.JSONObject;
import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.interfac.ILogin;
import com.exam.longtian.presenter.PLogin;
import com.exam.longtian.util.API;
import com.exam.longtian.util.CommandTools;
import com.exam.longtian.util.Constant;
import com.exam.longtian.util.HttpUtils;
import com.exam.longtian.util.OkHttpUtil;
import com.exam.longtian.util.OkHttpUtil.ObjectCallback;
import com.exam.longtian.util.SharedPreferencesUtils;
import com.exam.longtian.view.CustomProgress;
import com.exam.longtian.view.UpdateAppDialog;
import com.exam.longtian.view.UpdateAppDialog.ResultCallBack;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/** 
 * 登录
 * 
 * @author yxx
 *
 * @date 2017-11-28 上午10:52:23
 * 
 */
public class LoginActivity extends Activity{

	@ViewInject(R.id.login_edt_name) EditText edtName;
	@ViewInject(R.id.login_edt_psd) EditText edtPsd;

	@ViewInject(R.id.login_checkBox_id) CheckBox checkId;
	@ViewInject(R.id.login_checkBox_psd) CheckBox checkPsd;

	@ViewInject(R.id.login_tv_version) TextView tvVersion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);

		initData();
		checkAppUpdate();
		
//		Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
//		startActivity(intent);
//		finish();
	}
	
	public void initData(){

		String loginId = SharedPreferencesUtils.getParam(this, Constant.SP_LOGIN_ID, "").toString();
		String loginPsd = SharedPreferencesUtils.getParam(this, Constant.SP_LOGIN_PSD, "").toString();

		if(!TextUtils.isEmpty(loginId)){
			checkId.setChecked(true);
		}

		if(!TextUtils.isEmpty(loginPsd)){
			checkPsd.setChecked(true);
		}

		edtName.setText(loginId);
		edtPsd.setText(loginPsd);

		tvVersion.setText(CommandTools.getVersionName());
		tvVersion.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				//				String updateTime = "上次更新时间：\n 2018-01-31 16:43:00";
				//				CommandTools.showDialog(LoginActivity.this, updateTime);
				return false;
			}
		});
	}

	public void login(View v){

		final String phone = edtName.getText().toString();
		final String psd = edtPsd.getText().toString();

		if(TextUtils.isEmpty(phone) || TextUtils.isEmpty(psd)){
			CommandTools.showToast("账号或密码不能为空");
			return;
		}

		PLogin.auth_login(this, phone, psd, "", new ILogin() {

			@Override
			public void success() {

				if(checkId.isChecked()){
					SharedPreferencesUtils.setParam(LoginActivity.this, Constant.SP_LOGIN_ID, phone);
				}else{
					SharedPreferencesUtils.setParam(LoginActivity.this, Constant.SP_LOGIN_ID, "");
				}

				if(checkPsd.isChecked() && checkId.isChecked()){
					SharedPreferencesUtils.setParam(LoginActivity.this, Constant.SP_LOGIN_PSD, psd);
				}else{
					SharedPreferencesUtils.setParam(LoginActivity.this, Constant.SP_LOGIN_PSD, "");
				}

				Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void failed() {
				// TODO Auto-generated method stub
			}
		});
	}

	public void exit(View v){

		exit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) { // 获取 back键

			exit();
		}

		return false;
	}

	public void exit(){

		new AlertDialog.Builder(this)
		.setTitle("提示")
		.setMessage("确认退出程序？")
		.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				MyApplication.finishAllActivities();
				finish();
			}
		}).setNegativeButton("取消", null).show();
	}
	
	public void onDestory(){
		super.onDestroy();
		
	}

	public void checkAppUpdate(){

		final int mClientVersion = CommandTools.getVersionCode();

		OkHttpUtil.checkUpdate(this, API.URL_APP_UPDATE, new ObjectCallback() {

			@Override
			public void callback(boolean success, String message, String code, Object data) {

				JSONObject jsonObject = (JSONObject) data;
				if(jsonObject.isNull("url")){
					return;
				}

				String beforce = jsonObject.optString("beforce");
				String vercode = jsonObject.optString("vercode");
				if (TextUtils.isEmpty(vercode)) {
					vercode = "0";
				}

				int mServerVersion = Integer.parseInt(vercode);//服务器上版本号
				String strName = jsonObject.optString("vername");//服务器上版本名称
				String remark = jsonObject.optString("remark");
				final String downloadUrl = jsonObject.optString("url");

				if(mServerVersion > mClientVersion){

					UpdateAppDialog.showDialog(LoginActivity.this, beforce, "更新检测", "发现新版本号 " + strName + "，确定更新吗?", remark, new ResultCallBack() {
						@Override
						public void callback(boolean flag) {
							if (flag == true) {
								HttpUtils.download(LoginActivity.this, downloadUrl, mHandler);
							} else {

							}
						}
					});
				}
			}
		});
	}

	public Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

			if (msg.what == 0x0001) {
				UpdateAppDialog.showDialog(LoginActivity.this, "1", "正在更新程序", "正在更新程序", "", null);
			} else if (msg.what == 0x0002) {
				CustomProgress.dissDialog();
			} else if (msg.what == 0x11) {
				Bundle bundle = msg.getData();
				int totalSize = bundle.getInt("totalSize");
				int curSize = bundle.getInt("curSize");
				if (curSize >= totalSize) {
					UpdateAppDialog.dissDialog();
					MyApplication.finishAllActivities();
					return;
				}
				UpdateAppDialog.updateProgress(totalSize, curSize);
			}
		}
	};
	
}
