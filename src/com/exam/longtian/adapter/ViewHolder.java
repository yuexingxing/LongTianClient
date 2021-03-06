﻿package com.exam.longtian.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewHolder {
	
	private final SparseArray<View> mViews;
	private static int mPosition;
	private View mConvertView;

	private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
	
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		
		mConvertView.setTag(this);
	}

	/**
	 * 拿到一个ViewHolder对象
	 * 
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
		
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}
		
		mPosition = position;
		return (ViewHolder) convertView.getTag();
	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * 
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		
		View view = mViews.get(viewId);
		if (view == null) 	{
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView view = getView(viewId);
		view.setText(text);
		return this;
	}
	
	public ViewHolder setEditText(int viewId, String text) {
		EditText view = getView(viewId);
		view.setText(text);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);
		
		return this;
	}
	
	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageBackground(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setBackgroundResource(drawableId);
		
		return this;
	}
	
	/**
	 * 为LinearLayout设置背景
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setLayoutResource(int viewId, int drawableId) {
		LinearLayout layout = getView(viewId);
		layout.setBackgroundColor(drawableId);
		
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}
	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public ViewHolder setBackgroundBitmap(int viewId, Bitmap bm) {
		ImageView view = getView(viewId);
		Drawable drawable =new BitmapDrawable(bm);
		view.setBackgroundDrawable(drawable);
		return this;
	}
	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setBackgroundResource(int viewId, int resid) {
		ImageView view = getView(viewId);
		view.setBackgroundResource(resid);
		return this;
	}
	
	/**
	 * 为ImageView设置图片
	 * @param viewId
	 * @param bg
	 */
	public void setImageView(int viewId, int bg){
		
		ImageView view = getView(viewId);
		view.setBackgroundResource(bg);
	}
	
	/**
	 * 设置checkbox的选中状态
	 * @param viewId
	 * @param flag
	 */
	public void setCheckBox(int viewId){
		
		CheckBox check = getView(viewId);
		
		if(check.isChecked()){
			check.setChecked(false);
		}else{
			check.setChecked(true);
		}
	}
	
	/**
	 * 获取checkbox的选中状态
	 * @param viewId
	 * @return
	 */
	public boolean getCheckBoxState(int viewId){
		
		CheckBox check = getView(viewId);
		return check.isChecked();
	}

	public int getPosition() {
		return mPosition;
	}
	
	/**
	 * 隐藏第一个布局
	 * 显示第二个布局
	 * @param layout
	 */
	public void hideLayout(int viewId1, int viewId2){
		
		LinearLayout layout1 = getView(viewId1);
		LinearLayout layout2 = getView(viewId2);
		if(layout1 != null && layout2 != null){
			
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideLayout(int viewId){
		
		LinearLayout layout = getView(viewId);
		if(layout != null){
			layout.setVisibility(View.GONE);
		}
	}
	
	public void showLayout(int viewId){
		
		LinearLayout layout = getView(viewId);
		if(layout != null){
			layout.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 设置View控件不可见
	 * @param viewId
	 * @param flag true隐藏
	 */
	public void hideView(int viewId, boolean flag){
		
		View view = getView(viewId);
		if(flag){
			view.setVisibility(View.GONE);
		}else{
			view.setVisibility(View.VISIBLE);
		}
	
	}

	/**
	 * 为ImageView设置图片
	 * @param viewId
	 * @param bg
	 */
	public void setEditable(int viewId, boolean flag){
		
		EditText view = getView(viewId);
		view.setFocusable(flag);
	}
}
