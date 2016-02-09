package com.utils;

import java.io.UnsupportedEncodingException;

import com.jfinal.kit.HttpKit;
import com.jfinal.weixin.sdk.api.AccessToken;
import com.jfinal.weixin.sdk.api.AccessTokenApi;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.QrcodeApi;
import com.jfinal.weixin.sdk.api.UserApi;
//用这个类，获得永久二维码路径
public class GetQrcodeUrl {
	public static String getQrUrl(ApiConfig ac,String qrid) throws UnsupportedEncodingException {
		//一个线程
		ApiConfigKit.setThreadLocalApiConfig(ac);
		AccessToken accessToken = AccessTokenApi.getAccessToken();
		String token2 = accessToken.getAccessToken();
		//永久二维码请求的格式
		String s = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+qrid+"}}}";
		String string = HttpKit.post(
				"https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="
						+ token2, s);
		//变成一个字符串
		ApiResult result = ApiResult.create(string);
		String ticket = result.getStr("ticket");
		String ticString="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
		//指定编码
		ticString = java.net.URLEncoder.encode(ticString, "UTF-8");
		
		return ticString;

	}
	
	
	public static void main(String args[]) throws UnsupportedEncodingException{
		ApiConfig ac = new ApiConfig();
		ac.setAppId("wx4f40fbe390bb3215");//烩菜达人
		ac.setAppSecret("e2b2a0802317437348a07ce0927f7c1a");
		ac.setToken("abc");
		ApiConfigKit.setThreadLocalApiConfig(ac);
		String json="{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": 2}}}";
		ApiResult result = QrcodeApi.create(json);
		String str = result.getStr("ticket");
		System.out.println(str);
	}
	
	public static String qrcode(String appid,String appsecret,String paraid){
		ApiConfig ac = new ApiConfig();
		ac.setAppId(appid);//烩菜达人
		ac.setAppSecret(appsecret);
		ApiConfigKit.setThreadLocalApiConfig(ac);
		AccessToken accessToken = AccessTokenApi.getAccessToken();
		ac.setToken(accessToken.getAccessToken());
		ApiConfigKit.setThreadLocalApiConfig(ac);
		
		String json="{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+paraid+"}}}";
		ApiResult result = QrcodeApi.create(json);
		String str = result.getStr("ticket");
		return str;
	} 
	
	public static String getOpenUser(String appid, String appsecret,String OpenId){
		ApiConfig ac = new ApiConfig();
		ac.setAppId(appid);//烩菜达人
		ac.setAppSecret(appsecret);
		ApiConfigKit.setThreadLocalApiConfig(ac);
		AccessToken accessToken = AccessTokenApi.getAccessToken();
		String access_token=accessToken.getAccessToken();
		//String userinfo=HttpKit.get("https://api.weixin.qq.com/cgi-bin/user/info?access_token="+access_token+"&openid="+OpenId+"&lang=zh_CN");
		ApiResult result=UserApi.getUserInfo(OpenId);
		String nickname = result.getStr("nickname");
		System.out.println(nickname);
		return "测试中";
	}

}
