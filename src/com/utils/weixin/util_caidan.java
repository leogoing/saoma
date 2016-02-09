package com.utils.weixin;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import com.jfinal.kit.HttpKit;


public class util_caidan {
	
	/**
	 * 此方法为微信自定义菜单方法。通过access_token获取用户的自定义菜单。之后在创建自定义菜单
	 * 使用此方法请确保微信公众平台有菜单而且开启了开发者模式。次方法不开启开发者模式也能成功但是
	 * 之后开启开发者模式后，开发者模式会把一切菜单都清空。
	 * 此方法返回值为String 0为成功，1为失败,2为 access_token失效,3为我无效的按钮
	 * @param access_token
	 * @return
	 */
	public static String createCaidan(String access_token){
		String url="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";
		String caidan="";
		try {
			caidan=getCaidan(access_token).toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(caidan);
		JSONObject jo =new JSONObject(caidan);
		if(jo.has("selfmenu_info")){
			caidan=jo.get("selfmenu_info").toString();
			String post = HttpKit.post(url+access_token, caidan).toString();
			System.out.println(post);
			JSONObject jo1=new JSONObject(post);
			if(jo1.has("errcode")){
				if(jo1.get("errcode").equals("0")){
					return "0";
				}else if(jo1.get("errcode").equals("40001")){
					return "2";
				}else if(jo1.get("errcode").equals("40017") || jo1.get("errcode").equals("40016")){
					return "3";
				}else{
					return "1";
				}
			}else{
				return "1";			
			}
		}else{
			return "1";
		}
	}
	private static String  getCaidan(String access_token) throws IOException{
		String url1="https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";
		URL url = null;
		try {
			url = new URL(
					"https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token="
							+ access_token);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		InputStream is = url.openStream();

		byte[] b = new byte[20480];

		int len = -1;

		StringBuilder sb = new StringBuilder();

		len = is.read(b);

		while (len != -1) {
			sb.append(new String(b));
			len = is.read();
		}
		String jsonString = new String(outputStream.toByteArray(),"UTF-8");
		System.out.println("我擦擦："+jsonString);
		String sbb = sb.toString().trim();
		return sbb;
	}
	public static void main(String[] args) {
		createCaidan("l3I47dVakQfodqjfDQnCD9sGY4edCW9Cv77RSqRVcxlt79QNz2xL5PeYqm9f_4QRWs2Cf9YU62YL-bvfk48QyK308m2YHNe-lEGmkkQaqWkQRUaABAXYB");
	}
}
