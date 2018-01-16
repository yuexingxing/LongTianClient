package com.exam.longtian;

import com.exam.longtian.interfac.ILogin;
import com.exam.longtian.presenter.PLogin;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;

/** 
 * 登录
 * 
 * @author yxx
 *
 * @date 2017-11-28 上午10:52:23
 * 
 */
public class LoginActivity extends Activity {

	@ViewInject(R.id.login_edt_name) EditText edtName;
	@ViewInject(R.id.login_edt_psd) EditText edtPsd;

	@ViewInject(R.id.login_checkBox_id) CheckBox checkId;
	@ViewInject(R.id.login_checkBox_psd) CheckBox checkPsd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
	}

	public void login(View v){

		String phone = edtName.getText().toString();
		String psd = edtPsd.getText().toString();

		PLogin.auth_login(this, phone, psd, "", new ILogin() {

			@Override
			public void success() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, SiteSelectActivity.class);
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
}
