package com.utils.erweima;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class GetToken {
	private static String GetAccess_token(String appId,String appsecret) throws IOException{
    	URL url = null;
    	try {
			url = new URL("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appId+"&secret="+appsecret);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
    	
    	InputStream is = url.openStream();
    	
    	byte[] b = new byte[1024];
    	
    	int len = -1;
    	
    	StringBuilder sb = new StringBuilder();
    	
    	len=is.read(b);
    	
    	while(len!=-1){
    		sb.append(new String(b));
    		len = is.read();
    	}
    	return sb.toString().trim();
    }
    public static String getToken(String appId,String appsecret) throws IOException{
    	String getJson = GetAccess_token(appId, appsecret);
    	JSONObject jo = new JSONObject(getJson);
    	System.out.println(jo.toString());
    		jo.getString("access_token");
    		return jo.getString("access_token");
    }
}
