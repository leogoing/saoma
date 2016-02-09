package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetTime {
	/**
	 * 获得当前时间
	 * @return
	 */
	public static String getTime(){
		Date date=new Date();
		DateFormat f =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = f.format(date);
		return time;
	}
	
	public static String getTime(long i){
		Date date=new Date(i);
		DateFormat f =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = f.format(date);
		return time;
	}
	
	/**
	 * 获得当前毫秒时间 
	 * @return
	 */
	public static String getSSStime(){
		Date date =new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String time = sdf.format(date);
		return time;
	}
	
	/**
	 * 获得明天的时间
	 * @return 明天的时间字符串
	 */
	public	static String getNextDay() {
		Date date = new Date();// 新建此时的的系统时间
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, +1);//+1今天的时间加一天
		date = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(date);
		return time;
	}
}
