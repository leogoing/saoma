package model.weixin;


import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class CaidanModel extends Model<CaidanModel>{
	public static final CaidanModel dao = new CaidanModel();
	public int updateCaidan(String appid,String appsecret,String caidan){
		int update = 
				Db.update("update publicaccount set caidan=? where appid=?  and  appsecret=?",
						caidan,appid,appsecret);
		return update;
	}
	public String findcaidan(String appid,String appsecret){
		CaidanModel findFirst = 
				dao.findFirst("select caidan from publicaccount where appid=?  and  appsecret=?",appid,appsecret);
		findFirst.get("caidan");
		return findFirst.get("caidan");
	}
	public CaidanModel findHuifuyu(String name){
		CaidanModel findFirst = 
				dao.findFirst("select * from publicaccount where publicaccountname=?",name);
		return findFirst;
	}
}
