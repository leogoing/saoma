package com.utils.weixin;


public class util_EWM {
	private static final String url="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
	public static String getEWM(String access_token,int id,String type){
		return url+ util_ticket.getticket(access_token, id,type);
	}
	public static void main(String[] args) {
		String ewm = getEWM("pNajlKHtivm67vR-dBVDAjYlJMvi5LS7TAjiRXooXsEkz95p1utGSEnjCtrZL5rrnkZgf2RKQYOgk9eFYCuLNPHIkLd1KxtooCcYsBj2vK8FXAdACAPHK", 53, "临时");
	}
}
