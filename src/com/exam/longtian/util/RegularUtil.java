package com.exam.longtian.util;

import android.text.TextUtils;

/** 
 * ������ʽ
 * 
 * @author yxx
 *
 * @date 2018-2-2 ����1:18:00
 * 
 */
public class RegularUtil {

	public static final String phoneRegular = "[1][34587]\\d{9}";
	public static final String telphoneRegular = "^((0\\d{2,3})-)(\\d{7,8})(-(\\d{3,}))?$";//�̶��绰���ʽ

	public static final String billRegular = "^[0-9]{14}$";//�������ʽ
	public static final String childBillRegular = "^[0-9]{14}0[0-9]{3}$";//�ӵ����ʽ
	public static final String backBillRegular = "^[0-9]{10}$";//�ص����ʽ
	public static final String joinBillRegular = "^[0-9]{14,16}$";//���ӵ���14��16λ��������ʱд14-16λ

	/**
	 * �ֻ��Ż����������ж�
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
	 * ���������ж�
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
	 * �ӵ������ж�
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
	 * �ص������ж�
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
	 * ���ӵ������ж�
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
	 * �������ӵ����ص�����һ���жϣ�����һ������
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
