package com.exam.longtian.util;

/** 
 * 
 * 
 * @author yxx
 *
 * @date 2018-2-7 上午10:53:35
 * 
 */
public class DateUtil {
	
	/**
	 * 起始时间和结束时间比较大小
	 * @param startYear
	 * @param startMonth
	 * @param startDay
	 * @param endYear
	 * @param endMonth
	 * @param endDay
	 * @return
	 */
	public static boolean compareDate(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay){
		
		if(startYear > endYear){
			return false;
		}
		
		if(startMonth > endMonth && startYear == endYear){
			return false;
		}
		
		if(startDay > endDay && startMonth == endMonth && startYear == endYear){
			return false;
		}
		
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
