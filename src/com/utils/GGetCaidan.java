package com.utils;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class GGetCaidan {
	public static String  getCaidan(String access_token) throws IOException{
		String url1="https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";
		URL url = null;
		try {
			url = new URL(
					"https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token="
							+ access_token);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		InputStream is = url.openStream();

		byte[] b = new byte[20480];

		int len = -1;

		StringBuilder sb = new StringBuilder();

		len = is.read(b);

		while (len != -1) {
			sb.append(new String(b));
			len = is.read();
		}
		String sbb = sb.toString().trim();
		return sbb;
	}
}
