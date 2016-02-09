package com.utils.weixin;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class util_access_token {
	
	public static String getAccess_token(String AppID,String AppSecret) throws IOException{
		String url1="https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";
		URL url = null;
		try {
			url = new URL(
					"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+AppID+""
							+ "&secret="+AppSecret);
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
		JSONObject jo =new JSONObject(sbb);
		if(jo.has("access_token")){
			sbb=jo.get("access_token").toString();
		}else{
			sbb="1";
		}
		return sbb;
	}
}
