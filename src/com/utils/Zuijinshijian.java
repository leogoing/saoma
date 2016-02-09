package com.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Zuijinshijian {

	public String StartOfToday() {
		// 获得时间格式对象
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 通过日历获取今天日期
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		// 进行格式转换
		String today = sf.format(cal.getTime());

		return today;
	}

	public String EndOfToday() {
		// 创建时间格式对象
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 通过日历获取今天日期
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		// 进行格式转换
		String today = sf.format(cal.getTime());

		return today;

	}

	// 获得昨天零时时间和日期
	public String StartOfYesterday() {

		// 创建时间格式对象
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 通过日历获取昨天日期
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		// 进行格式转换
		String yesterday = sf.format(cal.getTime());

		return yesterday;

	}

	public String EndOfYesterDay() {
		// 创建时间格式对象
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 通过日历获取昨天日期
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		// 进行格式转换
		String yesterday = sf.format(cal.getTime());

		return yesterday;

	}

	// 获得本周最后一天的结束时间和日期
	public String LastDayOfWeek() {

		// 获得本周最后一天的日期和时间
		Calendar currentDate = Calendar.getInstance();
		currentDate.setFirstDayOfWeek(Calendar.MONDAY);
		currentDate.set(Calendar.HOUR_OF_DAY, 23);
		currentDate.set(Calendar.MINUTE, 59);
		currentDate.set(Calendar.SECOND, 59);
		currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		// 进行格式转换
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String lastDay = sf.format((Date) currentDate.getTime());

		return lastDay;

	}

	// 获得本周的第一天起始时间和日期
	public String FirstDayOfWeek() {
		// 获取本周第一天时间日期
		Calendar currentDate = Calendar.getInstance();
		currentDate.setFirstDayOfWeek(Calendar.MONDAY);
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		// 进行格式转换
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String firstDay = sf.format(currentDate.getTime());

		return firstDay;

	}

	public String FirstDayOfMonth() {
		// 获取当前日期
		Calendar calender = Calendar.getInstance();
		// 设置为1既为本月第一天
		calender.set(Calendar.DAY_OF_MONTH, 1);
		calender.set(Calendar.HOUR_OF_DAY, 0);
		calender.set(Calendar.MINUTE, 0);
		calender.set(Calendar.SECOND, 0);
		// 进行格式转换
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String firstDayM = sf.format(calender.getTime());
		return firstDayM;

	}

	public String LastDayOfMonth() {
		// 获取当前日期
		Calendar calender = Calendar.getInstance();
		// 设置本月最大天数既为最后一天
		calender.set(Calendar.DAY_OF_MONTH, calender.getActualMaximum(Calendar.DAY_OF_MONTH));
		calender.set(Calendar.HOUR_OF_DAY, 23);
		calender.set(Calendar.MINUTE, 59);
		calender.set(Calendar.SECOND, 59);
		// 进行格式转换
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String lastDayM = sf.format(calender.getTime());

		return lastDayM;

	}


}
