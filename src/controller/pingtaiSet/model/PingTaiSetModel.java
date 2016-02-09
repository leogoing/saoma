package controller.pingtaiSet.model;

import java.math.BigDecimal;
import java.util.List;

import org.dom4j.rule.Mode;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

public class PingTaiSetModel extends Model<PingTaiSetModel>{
	
	public static final PingTaiSetModel dao =new PingTaiSetModel();
	
	public List<PingTaiSetModel> findPingtaiAll(){
		List<PingTaiSetModel> find = dao.find("select * from publicaccount as p where uplowframe!= 2 order by zongjia  desc");
		
		return find;
	}
	
	public void updataFencheng(int fencheng,int id,float personagentsp,float cityagentsp,float countryagentsp){
		
		Db.update("update publicaccount  set fencheng=?,personagentsp=？,cityagentsp=？,countryagentsp=？  where id=?",fencheng,personagentsp,cityagentsp,countryagentsp,id);
	}
	public void updateShunxu(int id,int shunxu){
		Db.update("update publicaccount  set shunxu=? where id=? and uplowframe!= 2",shunxu,id);
	}
	public List<PingTaiSetModel> findtype2(){
		List<PingTaiSetModel> find = 
				dao.find("select * from publicaccount  where shunxu=-1 and uplowframe!= 2 order by zongjia  desc");
		return find;
	}
	public List<PingTaiSetModel> findwei(){
		List<PingTaiSetModel> find = 
				dao.find("select * from publicaccount   where shunxu!=-1  and  uplowframe!= 2 order by zongjia  desc");
		return find;
	}
	public List<PingTaiSetModel> findwei1(){
		List<PingTaiSetModel> find = 
				dao.find("select * from publicaccount  where shunxu<1 and uplowframe!= 2 order by zongjia  desc");
		return find;
	}
	public List<PingTaiSetModel> findtype3(){
		List<PingTaiSetModel> find = 
				dao.find("select * from publicaccount where  shunxu between 1 and 50  and uplowframe!= 2 order by shunxu");
		return find;
	}
	public PingTaiSetModel findPT(int id){
		PingTaiSetModel findFirst = findFirst("select * from publicaccount where id=? and uplowframe!= 2 ",id);
		return findFirst;
	}
	public PingTaiSetModel findPTInformationtype(String publicaccountname){
		PingTaiSetModel findFirst = null;
		try {
			findFirst =
					dao.findFirst("select * from publicaccount where publicaccountname=?",publicaccountname);
		} catch (Exception e) {
		}
		return findFirst;
	}
}
