package com.exam.longtian.util;

import android.text.TextUtils;

/** 
 * 正则表达式
 * 
 * @author yxx
 *
 * @date 2018-2-2 下午1:18:00
 * 
 */
public class RegularUtil {

	public static final String phoneRegular = "[1][34587]\\d{9}";
	public static final String telphoneRegular = "^((0\\d{2,3})-)(\\d{7,8})(-(\\d{3,}))?$";//固定电话表达式

	public static final String billRegular = "^[0-9]{14}$";//主单表达式
	public static final String childBillRegular = "^[0-9]{14}0[0-9]{3}$";//子单表达式
	public static final String backBillRegular = "^[0-9]{10}$";//回单表达式
	public static final String joinBillRegular = "^[0-9]{14,16}$";//交接单号14或16位，这里暂时写14-16位

	/**
	 * 手机号或座机正则判断
	 * @param billcode
	 * @return
	 */
	public static boolean checkPhone(String phone){

		if(TextUtils.isEmpty(phone)){
			return false;
		}

		if(phone.length() == 11){
			return phone.matches(phoneRegular);
		}else{
			return true;
			//			return phone.matches(telphoneRegular);
		}
	}


	/**
	 * 主单正则判断
	 * @param billcode
	 * @return
	 */
	public static boolean checkBill(String billcode){

		if(TextUtils.isEmpty(billcode)){
			return false;
		}

		return billcode.matches(billRegular);
	}

	/**
	 * 子单正则判断
	 * @param billcode
	 * @return
	 */
	public static boolean checkChildBill(String billcode){

		if(TextUtils.isEmpty(billcode)){
			return false;
		}

		return billcode.matches(childBillRegular);
	}

	/**
	 * 回单正则判断
	 * @param billcode
	 * @return
	 */
	public static boolean checkBackBill(String billcode){

		if(TextUtils.isEmpty(billcode)){
			return false;
		}

		return billcode.matches(backBillRegular);
	}

	/**
	 * 交接单正则判断
	 * @param billcode
	 * @return
	 */
	public static boolean checkJoinBill(String billcode){

		if(TextUtils.isEmpty(billcode)){
			return false;
		}

		return billcode.matches(joinBillRegular);
	}

	/**
	 * 主单、子单、回单正则一起判断，满足一个即可
	 * @param billcode
	 * @return
	 */
	public static boolean checkAllBill(String billcode){

		return (
				billcode.matches(billRegular) 
				|| billcode.matches(childBillRegular) 
				|| billcode.matches(backBillRegular) 
				|| billcode.matches(joinBillRegular)
				);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String str = "123";
		System.out.println(str.matches(billRegular));
	}

}
