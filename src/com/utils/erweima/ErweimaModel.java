package com.utils.erweima;


import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class ErweimaModel extends Model<ErweimaModel>{
	public static final ErweimaModel dao = new ErweimaModel();
	public ErweimaModel findEWMtime(String publicaccountname){
		System.out.println(publicaccountname);
		ErweimaModel findFirst = 
				dao.findFirst("select * from publicaccount where publicaccountname=?",publicaccountname);
		return findFirst;
	}
	public void updateAccess_token(String access_token,String publicaccountname){
		Db.update("update publicaccount set access_token=? where publicaccountname=?",access_token,publicaccountname);
	}
	
	public ErweimaModel findPTByAppidAndAppsecret(String appid,String appsecret){
		
		ErweimaModel e =dao.findFirst("select * from publicaccount where appid=? and appsecret=?",appid,appsecret);
		
		return e;
		
	}
}
