package model;

import com.jfinal.plugin.activerecord.Model;

public class Findbaohuqi extends Model<Findbaohuqi>{
	public static final Findbaohuqi dao = new Findbaohuqi();
	public int finBaohuqi(String pingtainame){
		Findbaohuqi findFirst = 
				dao.findFirst("select adtime from publicaccount where publicaccountname=?",pingtainame);
		return findFirst.getInt("adtime");
	}
	public int finBaohuqi1(String pingtainame){
		Findbaohuqi findFirst = 
				dao.findFirst("select aptime from publicaccount where publicaccountname=?",pingtainame);
		return findFirst.getInt("aptime");
	}
}
