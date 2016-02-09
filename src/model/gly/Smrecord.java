package model.gly;

import model.Saomajilu;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class Smrecord extends Model<Smrecord>{
	public static final Smrecord dao = new Smrecord();
	
	public Integer findSize(){
		Integer size=dao.find("select * from smrecord").size();
		return size;
	}
	public Page<Smrecord> dd(int pageNumber,Integer pageSize){
		Page<Smrecord> paginate = 
				dao.paginate(pageNumber,pageSize, 
						"select s.*,p.name "," from smrecord s left join publicaccount p on s.attentionPL=p.publicaccountname where state=1 order by smtime desc");
		return paginate;
	}
	public Smrecord findMoney(String attentionPL){
		System.out.println(attentionPL);
		Smrecord find = 
				dao.findFirst("select sum(cityget+countryget+personget) as  money  from  smrecord  where attentionPL='"+attentionPL+"'");
		return find;
	}
}
