package com.utils;

/**
 * sql工具类
 * @author zmc
 *
 */
public class SqlStrUtils {

	/**
	 * 将传入的字符串数组转换为逗号分割的字符串
	 * @param s
	 * @return
	 */
	public static String setIn(String[] s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length; i++) {
			sb.append(s[i] + ",");
		}
		String substring = sb.substring(0, sb.length() - 1);
		return substring;
	}	
}
