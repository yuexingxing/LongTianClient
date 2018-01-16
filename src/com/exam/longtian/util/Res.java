package com.exam.longtian.util;

import com.exam.longtian.MyApplication;

import android.graphics.drawable.Drawable;

public class Res {

	public Res(){
		
	}
	
	/**
	 * ��ȡvalueֵ
	 * @param name id
	 * @return
	 */
	public static String getString(int name){
		
		return MyApplication.getInstance().getString(name);
	}
	
	/**
	 * ��ȡ��ɫֵ
	 * @param name id
	 * @return
	 */
	public static int getColor(int name){
		
		return MyApplication.getInstance().getResources().getColor(name);
	}
	
	/**
	 * ��ȡ��ɫֵ
	 * @param name id
	 * @return
	 */
	public static Drawable getDrawable(int name){
		
		return MyApplication.getInstance().getResources().getDrawable(name);
	}
}
