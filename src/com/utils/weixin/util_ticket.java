package com.utils.weixin;


import org.json.JSONObject;

import com.jfinal.kit.HttpKit;

public class util_ticket {
	public static String getticket(String  access_token,int id,String type){
		String url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
		String str = "{\"action_name\":\"QR_LIMIT_SCENE\",\"action_info\":{\"scene\":{\"scene_id\":"+id+"}}}";
		String str1="{\"expire_seconds\": 604800,\"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+id+"}}}";
		
		if(type.equals("永久")){
			String post = HttpKit.post(url+access_token, str);
			System.out.println(post);
			JSONObject jo=new JSONObject(post);
			if(jo.has("ticket")){
				String ticket=jo.get("ticket").toString();
				return ticket;
			}else{
				return "1";
			}
		}else if(type.equals("临时")){
			String post = HttpKit.post(url+access_token, str1);
			System.out.println(post);
			JSONObject jo=new JSONObject(post);
			if(jo.has("ticket")){
				String ticket=jo.get("ticket").toString();
				return ticket;
			}else{
				return "1";
			}
		}else{
			return "1";
		}
	}
	
	public static void main(String[] args) {
		getticket("pNajlKHtivm67vR-dBVDAjYlJMvi5LS7TAjiRXooXsEkz95p1utGSEnjCtrZL5rrnkZgf2RKQYOgk9eFYCuLNPHIkLd1KxtooCcYsBj2vK8FXAdACAPHK", 276,"临时");
	}
}
