package model.gly;

import model.Gongzhonghao;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class Publicaccount extends Model<Publicaccount>{
	public static final Publicaccount dao= new Publicaccount();
	public Page<Publicaccount> ee(String id){
//		Page gzh=Gongzhonghao.dao.paginate(1, 1, "select * ", "from publicaccount where id=?",id);
		Page<Publicaccount> paginate = dao.paginate(1, 1, "select id,name,uplowframe,cityagentsp,countryagentsp,personagentsp,publicaccountname,appid,appsecret ", "from  publicaccount where id=?",id);
		return paginate;
	}
}
