package com.utils;

import java.io.IOException;
import java.text.ParseException;

import com.utils.erweima.DuiBiTime;
import com.utils.erweima.ErweimaModel;
import com.utils.weixin.UpdateWeixinInPublicaccount;
import com.utils.weixin.util_access_token;

import util.GetTime;

public class llll {
	public static String mmm(String publicaccountname,String AppID,String AppSecret){
		ErweimaModel findEWMtime = 
				ErweimaModel.dao.findEWMtime(publicaccountname);
		String time=findEWMtime.get("token_time").toString();
//		System.out.println(time);
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
			try {
				access_token=util_access_token.getAccess_token(AppID, AppSecret);
				UpdateWeixinInPublicaccount.dao.UpdateAccess_Token(access_token, AppID, AppSecret);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			access_token=findEWMtime.get("access_token");
		}
		return access_token;
	}
}
