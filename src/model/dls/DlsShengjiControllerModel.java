package model.dls;

import com.jfinal.plugin.activerecord.Model;

public class DlsShengjiControllerModel extends Model<DlsShengjiControllerModel>{
	public static final DlsShengjiControllerModel dao = new DlsShengjiControllerModel();

	//个人代理升级SQL语句
	public String agenetUpLevelforPersonalsql(int leverId){
		final String sql = "select dredge from level s where s.`id` ='"
				+ leverId + "'";
		return sql;
	}
	//个人代理升级SQL语句
	public String agenetUpLevelforPersonaldevelsql(String selectedLevel){
		final String develsql = "select dredge from level s where s.`id` ='"
				+ selectedLevel + "'";
		return develsql;
	}
	//个人代理升级SQL语句
	public String agenetUpLevelforPersonaltelsql(){
		final String telsql = "select tel,name from level where id=5";
		return telsql;
	}


}
