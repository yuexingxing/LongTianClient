package com.exam.longtian.activity;

import com.exam.longtian.MyApplication;
import com.exam.longtian.R;
import com.exam.longtian.util.Constant;
import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

/** 
 * 基类
 * 
 * @author yxx
 *
 * @date 2017-11-27 下午5:09:41
 * 
 */
public abstract class BaseActivity extends Activity{

	private LinearLayout layoutTopBar;
	private LinearLayout layoutBody;
	protected View contentView;

	private TextView tvLeft;
	private TextView tvTitle;
	private TextView tvRight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_base);

		layoutTopBar = (LinearLayout) findViewById(R.id.durian_head_layout);
		layoutBody = (LinearLayout) findViewById(R.id.baseAct_body);
		MyApplication.getInstance().addActivity(this);

		onBaseCreate(savedInstanceState);
		findViewById();
		initView();
		initData();
	}

	@Override
	protected void onResume(){
		super.onResume();

	}

	private void findViewById() {

		tvLeft = (TextView) findViewById(R.id.tv_item_left);
		tvTitle = (TextView) findViewById(R.id.tv_item_center);
		tvRight = (TextView) findViewById(R.id.tv_item_right);
	}

	public void setContentViewId(int layoutId) {

		contentView = getLayoutInflater().inflate(layoutId, null);
		if (layoutBody.getChildCount() > 0) {
			layoutBody.removeAllViews();
		}

		if (contentView != null) {
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			layoutBody.addView(contentView, params);
		}
	}

	/**
	 * 创建界面
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void onBaseCreate(Bundle savedInstanceState);

	/**
	 * 初始化控件
	 */
	public abstract void initView();

	/**
	 * 初始化数据
	 */
	public abstract void initData();

	public void hidenTopBar(){

		layoutTopBar.setVisibility(View.GONE);
	}

	/**
	 * 设置标题
	 * 
	 * @param mReturn
	 */
	public void setTitle(String mTitle) {
		tvTitle.setText(mTitle);
	}

	/**
	 * 设置右侧标题(主要数字显示)
	 * @param mTitle
	 */
	public void setRightTitle(String mTitle){
		tvRight.setText(mTitle);
	}

	/**
	 * 隐藏左侧返回键
	 */
	public void hidenLeftMenu(){
		tvLeft.setVisibility(View.INVISIBLE);
	}

	/**
	 * 隐藏左侧返回键
	 */
	public void hidenRightMenu(){
		tvRight.setVisibility(View.INVISIBLE);
	}

	public void back(View v){
		finish();
	}

	/**
	 * 右侧按钮点击事件
	 * 子类直接复写该方法即可
	 * @param v
	 */
	public void clickRight(View v){

	}

	/**
	 * @param msg
	 */
	public void onEventMainThread(Message msg) {

	}

}
