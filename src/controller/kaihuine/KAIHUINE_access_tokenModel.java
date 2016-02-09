package controller.kaihuine;


import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class KAIHUINE_access_tokenModel extends Model<KAIHUINE_access_tokenModel>{
	public static final KAIHUINE_access_tokenModel dao =new KAIHUINE_access_tokenModel();
	
	public KAIHUINE_access_tokenModel findAccess_token(String appid,String AppSecre){
		KAIHUINE_access_tokenModel findFirst = 
				dao.findFirst("select access_token,token_time from publicaccount where appid=? and appsecret=?",appid,AppSecre);
		return findFirst;
	}
	public void updateAccess_token(String appid,String AppSecre,String token,String time){
		Db.update("update publicaccount set  access_token=?,token_time=? where appid=? and  appsecret=?",token,time,appid,AppSecre);
	}
	public KAIHUINE_access_tokenModel findPT(String name,String AppSecret){
		KAIHUINE_access_tokenModel findFirst =null;
		try {
			findFirst = dao.findFirst("select * from publicaccount where publicaccountname=? and appsecret=?",name,AppSecret);
			
		} catch (Exception e) {
		}
		return findFirst;
		
	}
	
}
