package Test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import model.AccesstokenModel;
import model.TicketModel;

import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.weixin.sdk.api.AccessToken;
import com.jfinal.weixin.sdk.api.AccessTokenApi;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;

/**
 * 根据参数，生成微信带参数二维码并储存
 * @author apple
 *
 */
public class GetQrcodeWinthPara extends Controller{
	
	/**
	 * 生成带参数二维码
	 * 
	 * @用户id
	 * @平台的appid
	 * @平台的appserect
	 * @平台的token
	 * 
	 * @throws UnsupportedEncodingException
	 */
	
	public static String getQrcodePic(int id,String appid,String appserect,String apptoken) throws UnsupportedEncodingException{
		
		ApiConfig ap = new ApiConfig();
		ap.setAppId(appid);
		ap.setAppSecret(appserect);
		ap.setToken(apptoken);
		ApiConfigKit.setThreadLocalApiConfig(ap);
		
		AccessToken token = AccessTokenApi.getAccessToken();
		System.out.println(token.getJson());
		
		Gson gson = new Gson();
		AccesstokenModel am = gson.fromJson(token.getJson(), AccesstokenModel.class);
		
		String access_token = am.getAccess_token();
		
		System.out.println(access_token);
		String url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+access_token;
		
		List list=new ArrayList();
		StringBuffer s =new StringBuffer();
		s.append("{");
		s.append("\"expire_seconds\": 1800,");
		s.append("\"action_name\": \"QR_SCENE\",");
		s.append("\"action_info\": {");
		s.append("\"scene\": {");
		s.append("\"scene_id\": 100000");
		s.append("}");
		s.append("}");
		s.append("}");
		
		list.add(s);
		String string =HttpKit.post(url, s.toString());
		Gson g=new Gson();
		TicketModel ticketModel = g.fromJson(string, TicketModel.class);
		
		String t2=java.net.URLEncoder.encode(ticketModel.getTicket(),"UTF-8");
		
		//https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET
		String string2 = HttpKit.post("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+t2, s.toString());
		System.out.println("tiket:"+ticketModel.getTicket());
		String newurl="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticketModel.getTicket();
		return newurl;
	}
	
	
	/**
	 * controller测试
	 * @throws UnsupportedEncodingException
	 */
	public void getqrcode() throws UnsupportedEncodingException{
		ApiConfig ap = new ApiConfig();
		ap.setAppId("wx696d8e58dec8b531");
		ap.setAppSecret("ce977a181e9be43d9ba04a9ff823cb61");
		ap.setToken("abc");
		ApiConfigKit.setThreadLocalApiConfig(ap);
		
		AccessToken token = AccessTokenApi.getAccessToken();
		System.out.println(token.getJson());
		
		Gson gson = new Gson();
		AccesstokenModel am = gson.fromJson(token.getJson(), AccesstokenModel.class);
		
		String access_token = am.getAccess_token();
		
		System.out.println(access_token);
		String url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+access_token;
		
		List list=new ArrayList();
		StringBuffer s =new StringBuffer();
		s.append("{");
		s.append("\"expire_seconds\": 1800,");
		s.append("\"action_name\": \"QR_SCENE\",");
		s.append("\"action_info\": {");
		s.append("\"scene\": {");
		s.append("\"scene_id\": 100000");
		s.append("}");
		s.append("}");
		s.append("}");
		
		list.add(s);
		String string =HttpKit.post(url, s.toString());
		Gson g=new Gson();
		TicketModel ticketModel = g.fromJson(string, TicketModel.class);
		
		String t2=java.net.URLEncoder.encode(ticketModel.getTicket(),"UTF-8");
		
		//https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET
		String string2 = HttpKit.post("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+t2, s.toString());
		System.out.println("tiket:"+ticketModel.getTicket());
		String newurl="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticketModel.getTicket();
		setAttr("imgurl",newurl);
		//redirect(newurl);
		render("/WEB-INF/guanliyuan/zhanghuguanli.html");
	}
}
