package com.utils.kit;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

public class TimeKit {
	
	
	public static String handleDate(Date date) {
		DateTime time = new DateTime(date);
		Long now = new DateTime().getMillis();
		long t = (time.getMillis() - now)/1000/3600;
		if(t < 24){
			return t+"小时后";
		} else {
			return time.getMonthOfYear()+"月"+ time.getDayOfMonth()+"日";
		}
	}
	
	// 使用joda-time计算两个日期间隔的天数() 
	public static int calculateDaysInterval(String start, String end) {
		String[] sArray = start.split("-");
		String[] eArray = end.split("-");
		LocalDate s = new LocalDate(Integer.parseInt(sArray[0]), Integer.parseInt(sArray[1]), Integer.parseInt(sArray[2])); 
		LocalDate e = new LocalDate(Integer.parseInt(eArray[0]), Integer.parseInt(eArray[1]), Integer.parseInt(eArray[2])); 
		return Days.daysBetween(s, e).getDays();
	}
}
