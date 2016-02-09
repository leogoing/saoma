package com;

import com.jfinal.weixin.sdk.api.AccessTokenApi;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;

public class T {
	public  static void main(String[] args){
		ApiConfig ac=new ApiConfig();
		ac.setAppId("wxf74c9993e7ab0bd3");
		ac.setAppSecret("a04a73a63ee08e120faeda97a067b05b");
		ac.setToken("abc");
		ApiConfigKit ak=new ApiConfigKit();
		ak.setThreadLocalApiConfig(ac);
		System.out.println(AccessTokenApi.getAccessToken().getAccessToken());
		String menuJson = "{\n" +
				"    \"button\": [\n" +
				"        {\n" +
				"            \"name\": \"违章查询\",\n" +
				"            \"url\": \"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx4f40fbe390bb3215&redirect_uri=http://test.ican99.com/&response_type=code&scope=snsapi_base&state=1#wechat_redirect\",\n" +
				"            \"type\": \"view\"\n" +
				"        },\n" +
				"}";
		System.out.println(menuJson);
	}
}
