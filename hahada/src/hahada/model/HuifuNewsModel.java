package hahada.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

public class HuifuNewsModel extends Model<HuifuNewsModel>{
	public static final HuifuNewsModel dao = new HuifuNewsModel();
	
	public List<HuifuNewsModel> findDingyiCaidan(String key){
		List<HuifuNewsModel> find=null;
		try {
			find = 
					find("select * from news where keyname=?",key);
		} catch (Exception e) {
			find=null;
		}
		return find;
	}
}
