package controller.tixian.model;


import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class TixianModel extends Model<TixianModel>{
	
	public static final TixianModel dao = new TixianModel();
	
	public List<TixianModel> findTixian(){
		List<TixianModel> find = dao.find("select * from handcharge");
		return find;
		
		
	}
	
	public void dropTable(){
		Db.update("delete from handcharge");
	}
	
	public void saveTixian(TixianModel tixian){
		tixian.save();
	}
}
