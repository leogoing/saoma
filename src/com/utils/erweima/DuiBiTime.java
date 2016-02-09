package com.utils.erweima;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import util.GetTime;
//对比s1的时间是否比s2的时间多1小时
public class DuiBiTime {
	public static boolean duibi(String s1,String s2) throws ParseException{
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1=sdf.parse(s1);
		Date d2=sdf.parse(s2);
		long tim=d1.getTime()-d2.getTime();
		if(tim>3600000l){
			return true;
		}else{
			return false;
		}
	}
	public static boolean duibi2(String s1,String s2) throws ParseException{
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1=sdf.parse(s1);
		Date d2=sdf.parse(s2);
		long tim=d1.getTime()-d2.getTime();
		if(tim>0){
			return true;
		}else{
			return false;
		}
	}
	//获取i秒前的时间。
	public static String jia5fenzhong(String s1,int i) throws ParseException{
		long n=i*1000l;
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1=sdf.parse(s1);
		long nu=d1.getTime()-n;
		String time = GetTime.getTime(nu);
		return time;
	}
}
