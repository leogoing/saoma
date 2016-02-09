package model.pingtai;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class IPModel extends  Model<IPModel>{
	public static final IPModel dao =new IPModel();
	
	public List<IPModel> findIP(){
		List<IPModel> find = 
				dao.find("select  serverIP from publicaccount where useserver=1");
		return find;
	}
	public void updateToken(String access_token,String IP){
		Db.update("update publicaccount set access_token=? where serverIP=?",access_token,IP);
	}
}
