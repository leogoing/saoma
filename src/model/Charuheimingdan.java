package model;


import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class Charuheimingdan extends Model<Charuheimingdan>{
	public static final Charuheimingdan dao =new Charuheimingdan();
	public void chaxunhei(String loginname){
		Db.update("update user set yichang=1 where loginname=?",loginname);
	}
}
