package model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class GetPingtaiModel extends Model<GetPingtaiModel>{
	public static final GetPingtaiModel dao = new GetPingtaiModel();
	
	public List<GetPingtaiModel> findPingtaiAll(){
		List<GetPingtaiModel> find = dao.find("select * from publicaccount where uplowframe!=2 ");
		return find;
	}
	//查询没有服务器的平台
	public List<GetPingtaiModel> findPingtaiNoServer(){
		List<GetPingtaiModel> find = dao.find("select p.*,round(cityagentsp+countryagentsp+personagentsp,2) totalsp from publicaccount p  where uplowframe!=2 and useserver=1");
		return find;
		
	}
	public GetPingtaiModel findpingtai(int id){
		GetPingtaiModel findFirst = 
				dao.findFirst("select * from publicaccount where id=?",id);
		return findFirst;
	}
	public List<GetPingtaiModel> findPT(String name) {
		StringBuffer s = new StringBuffer();
		s.append("select publicaccountname from publicaccount");
		s.append(" as p join (select adId from publicaccount where");
		s.append(" publicaccountname=?)as a  on a.adId=p.adId");
		List<GetPingtaiModel> find = dao.find(s.toString(), name);
		return find;
	}
	public void update(int adId){
		Db.update("update publicaccount set uplowframe=2 where adId=?",adId);
	}
}
