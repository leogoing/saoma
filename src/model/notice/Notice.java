package model.notice;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class Notice extends Model<Notice>{
	
	public static final Notice dao=new Notice();
	
	public Notice findNeiNotice(){
		Notice find = dao.findFirst("select * from notice where istop=3");
		return find;
	}
	
	public void updateGonggao(String content){
		Db.update("update notice  set content=? where istop=3",content);
	}
	
	
	
}
