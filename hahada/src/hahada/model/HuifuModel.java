package hahada.model;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Model;

public class HuifuModel extends Model<HuifuModel>{
	public static final HuifuModel dao =new HuifuModel();
	
	public HuifuModel findHuifu(String key){
		HuifuModel findFirst=null;
		
			findFirst = dao.findFirst("select * from huifu where keyname=?",key);
			
		return findFirst;
	}

}
