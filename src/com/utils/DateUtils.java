package com.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 获取时间工具类
 * 
 * @author zmc
 *
 */
public class DateUtils {

	/**
	 * 根据传入的选择和格式，返回需要的时间
	 * 
	 * @param choose
	 *            ep:取值分别为yesterday(昨日),today(今日),month(本月)
	 * @return
	 */
	public static String getday(String choose) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String dateString = null;
		if ("yesterday".equalsIgnoreCase(choose)) {
			calendar.add(calendar.DATE, -1);// 把日期往后增加一天.整数往后推,负数往前移动
			date = calendar.getTime(); 
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd 00:00:00");
			dateString = formatter.format(date);
		} else if ("today".equals(choose)) {
			date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd 00:00:00");
			dateString = formatter.format(date);
		} else if ("month".equalsIgnoreCase(choose)) {
			date = calendar.getTime();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-01 00:00:00");
			dateString = formatter.format(date);
		} else if ("now".equalsIgnoreCase(choose)) {
			date = calendar.getTime();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			dateString = formatter.format(date);
		}else if("tomorrow".equalsIgnoreCase(choose)){
			calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			date = calendar.getTime(); 
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd");
			dateString = formatter.format(date);
		}

		return dateString;
	}

	public static String getYMD() {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		String dateString = null;
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		dateString = formatter.format(date);
		return dateString;
	}

	/**
	 * @传入需要的时间选择，返回对应的日期
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getday("now"));
		System.out.println(getday("yesterday"));
		System.out.println(getday("today"));
		System.out.println(getday("month"));
		System.out.println(getYMD());
	}

}
