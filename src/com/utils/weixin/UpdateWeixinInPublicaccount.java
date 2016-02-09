package com.utils.weixin;


import com.jfinal.plugin.activerecord.Model;

import util.GetTime;

public class UpdateWeixinInPublicaccount extends Model<UpdateWeixinInPublicaccount> {
	
	public static UpdateWeixinInPublicaccount dao =new UpdateWeixinInPublicaccount();
	
	public Boolean UpdateAccess_Token(String access_token,String appid,String appsecret){
		
		
		UpdateWeixinInPublicaccount pingtai =UpdateWeixinInPublicaccount.dao.findFirst("select * from publicaccount where appid=? and appsecret=?",appid,appsecret);
		String newtime = GetTime.getTime();
		pingtai.set("access_token", access_token).set("token_time", newtime);
		 Boolean b =pingtai.update();
		
		return b;
	}
	
	
	
}
