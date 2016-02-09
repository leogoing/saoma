package com.utils.erweima;

import java.io.IOException;
import java.text.ParseException;



import com.jfinal.kit.HttpKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.QrcodeApi;
import com.utils.weixin.UpdateWeixinInPublicaccount;
import com.utils.weixin.util_access_token;
import com.utils.weixin.util_ticket;

import util.GetTime;
/**
 * 此类是获取临时二维码图片的 一个类。
 * 有appId，appsecret，Token，微信基本参数。
 * id为业务员的ID，
 * publicaccountname为公众号的平台名称(原始ID)。
 * 此类根据数据库publicaccount表，查询
 * @author Administrator
 *
 */
public class GetEWM {
	public static String linshiEWM(String appId,String appsecret, String Token,int id,String publicaccountname){
		String str = "{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+id+"}}}";
		String cc="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
		System.out.println(publicaccountname);
		ErweimaModel findEWMtime = 
				ErweimaModel.dao.findEWMtime(publicaccountname);
		String time=findEWMtime.get("token_time").toString();
		System.out.println(time);
		time=time.substring(0, 19);
		boolean duibi=false;
		String token="";
		String access_token="";
		try {
			 duibi = DuiBiTime.duibi(GetTime.getTime(), time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(duibi){
			
			ApiConfig ac =new ApiConfig();
			ac.setAppId(appId);
			ac.setAppSecret(appsecret);
			ac.setToken(Token);
			ApiConfigKit.setThreadLocalApiConfig(ac);
			
			String access_token2="";
			try {
				access_token2 = util_access_token.getAccess_token(appId, appsecret);
				Boolean b =UpdateWeixinInPublicaccount.dao.UpdateAccess_Token(access_token2, appId, appsecret);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String getticket = util_ticket.getticket(access_token2, id, "临时");
			
			String ccc=cc+getticket;
			
			return ccc;
		}else{
			ApiConfig ac =new ApiConfig();
			ac.setAppId(appId);
			ac.setAppSecret(appsecret);
			ac.setToken(Token);
			ApiConfigKit.setThreadLocalApiConfig(ac);
			
			ApiResult create=null;
			access_token=findEWMtime.get("access_token");
			create = create(access_token,  str);
			String ccc=cc+create.get("ticket");
			String substring = ccc.substring(ccc.length()-4, ccc.length());
			if(substring.equals("null")){
				try {
					 access_token = util_access_token.getAccess_token(appId, appsecret);
					 UpdateWeixinInPublicaccount.dao.UpdateAccess_Token(access_token, appId, appsecret);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ApiResult create1 = create(access_token,  str);
				ccc=cc+create1.get("ticket");
			}
			return ccc;
		}
	}
	/**
	 * 此类是获取永久二维码图片的 一个类。
	 * 有appId，appsecret，Token，微信基本参数。
	 * id为业务员的ID，
	 * publicaccountname为公众号的平台名称。
	 * 此类根据数据库publicaccount表，查询
	 * @author Administrator
	 *
	 */
	public static String yongjiuEWM(String appId,String appsecret, String Token,int id,String publicaccountname){
		String str = "{\"action_name\":\"QR_LIMIT_SCENE\",\"action_info\":{\"scene\":{\"scene_id\":"+id+"}}}";
		String cc="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
		ErweimaModel findEWMtime = 
				ErweimaModel.dao.findEWMtime(publicaccountname);
		String time1=findEWMtime.get("token_time").toString();
		String time = time1.substring(0, 19);
		boolean duibi=false;
		String token="";
		String access_token="";
		try {
			 duibi = DuiBiTime.duibi(GetTime.getTime(), time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(duibi){
			
			ApiConfig ac =new ApiConfig();
			ac.setAppId(appId);
			ac.setAppSecret(appsecret);
			ac.setToken(Token);
			ApiConfigKit.setThreadLocalApiConfig(ac);
			
			QrcodeApi qa =new QrcodeApi();
			ApiResult ar = qa.create(str);
			String ccc=cc+ar.get("ticket");
			return ccc;
		}else{
			ApiConfig ac =new ApiConfig();
			ac.setAppId(appId);
			ac.setAppSecret(appsecret);
			ac.setToken(Token);
			ApiConfigKit.setThreadLocalApiConfig(ac);
			
			access_token=findEWMtime.get("access_token");
			ApiResult create = create(access_token,  str);
			
			String ccc=cc+create.get("ticket");
			String substring = ccc.substring(ccc.length()-4, ccc.length());
			if(substring.equals("null")){
				try {
					 access_token = util_access_token.getAccess_token(appId, appsecret);
					 UpdateWeixinInPublicaccount.dao.UpdateAccess_Token(access_token, appId, appsecret);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ApiResult create1 = create(access_token,  str);
				ccc=cc+create1.get("ticket");
			}
			return ccc;
		}
	}
	
	public static ApiResult create(String access_token,String jsonStr) {
		String apiUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
		String jsonResult = HttpKit.post(apiUrl + access_token, jsonStr);
		return new ApiResult(jsonResult);
	}
	public static String EWMW(String access_token,int id){
		String str = "{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+id+"}}}";
		String cc="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
		ApiResult create = create(access_token,  str);
		String ccc=cc+create.get("ticket");
		System.out.println(ccc);
		return ccc;
	}
	public static void main(String[] args) {
		String str = "{'action_name': 'QR_LIMIT_STR_SCENE', 'action_info': {'scene': {'scene_str': "+123+"}}}";
		ApiResult create = create("5QiPsVY4iWvzmMnpCBKtIzePFiRPTFPxEOtDAeWXGx46smDhqWhUC3BYuo1e_5MczMGYd55E9wOS7JMY5FROYB_qAIAA0Qq0kfXFdQrQH8sMYXeAIAWIH", str);
		System.out.println(create.toString());
		String cc="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
		String ccc=cc+create.get("ticket");
		System.out.println(ccc);
		
	}
	
	public static String linshiEWMByApp(String appId,String appsecret,int id){
		String str = "{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+id+"}}}";
		String cc="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
		System.out.println("appid========"+appId);
		ErweimaModel findEWMtime = 
				ErweimaModel.dao.findPTByAppidAndAppsecret(appId, appsecret);
		String Token = findEWMtime.getStr("access_token");
		String time=findEWMtime.get("token_time").toString();
		System.out.println(time);
		time=time.substring(0, 19);
		boolean duibi=false;
		String token="";
		String access_token="";
		try {
			 duibi = DuiBiTime.duibi(GetTime.getTime(), time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(duibi){
			
			ApiConfig ac =new ApiConfig();
			ac.setAppId(appId);
			ac.setAppSecret(appsecret);
			ac.setToken(Token);
			ApiConfigKit.setThreadLocalApiConfig(ac);
			
			String access_token2="";
			try {
				access_token2 = util_access_token.getAccess_token(appId, appsecret);
				Boolean b =UpdateWeixinInPublicaccount.dao.UpdateAccess_Token(access_token2, appId, appsecret);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String getticket = util_ticket.getticket(access_token2, id, "临时");
			
			String ccc=cc+getticket;
			
			return ccc;
		}else{
			ApiConfig ac =new ApiConfig();
			ac.setAppId(appId);
			ac.setAppSecret(appsecret);
			ac.setToken(Token);
			ApiConfigKit.setThreadLocalApiConfig(ac);
			
			ApiResult create=null;
			access_token=findEWMtime.get("access_token");
			create = create(access_token,  str);
			String ccc=cc+create.get("ticket");
			String substring = ccc.substring(ccc.length()-4, ccc.length());
			if(substring.equals("null")){
				try {
					 access_token = util_access_token.getAccess_token(appId, appsecret);
					 UpdateWeixinInPublicaccount.dao.UpdateAccess_Token(access_token, appId, appsecret);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ApiResult create1 = create(access_token,  str);
				ccc=cc+create1.get("ticket");
			}
			return ccc;
		}
		
		
	}
	
}
