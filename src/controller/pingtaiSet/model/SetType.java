package controller.pingtaiSet.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class SetType extends Model<SetType>{
	public static final SetType dao =new SetType();
	
	public void updataType(int type){
		Db.update("update paixutype  set type=? where id=1",type);
	}
	public int findType(){
		SetType findFirst = dao.findFirst("select type from paixutype");
		return findFirst.get("type");
	}
}
