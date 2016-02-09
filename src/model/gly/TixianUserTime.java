package model.gly;

import com.jfinal.plugin.activerecord.Model;

public class TixianUserTime extends Model<TixianUserTime>{
	public static final TixianUserTime dao =new TixianUserTime();
	
	public TixianUserTime findUserTime(int id){
		TixianUserTime findFirst = findFirst("select  * from user where id=?",id);
		return findFirst ;
	}
}
