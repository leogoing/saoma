package com.utils;

import java.security.MessageDigest;

/**
 * 加密的工具类
 * @author zmc
 *
 */
public class Encrypt {
	
	/**
	 * 对传入的字符串进行加密
	 * @param s
	 * @return
	 */
	public final static String MD5(String s) {
		try {
			byte[] btInput = s.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < md.length; i++) {
				int val = ((int) md[i]) & 0xff;
				if (val < 16)
					sb.append("0");
				sb.append(Integer.toHexString(val));
			}
			return sb.toString();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void main(String args[]){
		System.out.println(MD5("123456"));
		System.out.println(MD5("654321"));
	}
}
