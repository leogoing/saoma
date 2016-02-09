package util;

import java.util.Random;
/**
 * 根据服务器地址和appID，appsecret生成微信URL地址
 * @author lenovo
 *url：服务器地址和微信类的路径
 *appID：微信appID
 *appsecret：微信appsecret
 */
public class GetURL {
	/**
	 * 根据服务器地址和appID，appsecret生成微信URL地址
	 * @author lenovo
	 *url：服务器地址和微信类的路径
	 *appID：微信appID
	 *appsecret：微信appsecret
	 */
	public static String getURL(String url,String appID,String appsecret){
		//生成Random实体类
		Random r = new Random();
		//随机生成数字
		int n=r.nextInt(10000000);
		//拼接字符串
		String URL="您的服务器地址请填写:\n"+url+"?AppID="+appID+"&AppSecret="+appsecret+"&Token="+n+"\n 您的Token请填写:"+n;
		//返回字符串
		return URL;
	}
	/**
	 * 
	 * @param url 服务器地址
	 * @param appID
	 * @param appsecret
	 * @param token
	 * @return 要填写的服务器地址
	 */
	public static String getURLSecond(String url,String appID,String appsecret,String token){
		//生成Random实体类
		Random r = new Random();
		//拼接字符串
		String URL="您的服务器地址请填写:\n"+url+"?AppID="+appID+"&AppSecret="+appsecret+"&Token="+token+"\n 您的Token请填写:"+token;
		//返回字符串
		return URL;
	}
	
}
