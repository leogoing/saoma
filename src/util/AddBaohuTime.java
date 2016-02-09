package util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import util.GetTime;
//对比s1的时间是否比s2的时间多1小时
public class AddBaohuTime {
	//获取i秒后的时间。
	public static String addBaohuqi(String s1,int i) throws ParseException{
		long n=i*1000l;
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1=sdf.parse(s1);
		long nu=d1.getTime()+n;
		String time = GetTime.getTime(nu);
		return time;
	}
}
