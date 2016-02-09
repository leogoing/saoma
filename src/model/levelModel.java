package model;


import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class levelModel extends Model<levelModel> {

	private static final long serialVersionUID = 1L;
	
	public static final levelModel dao=new levelModel();

	public Page<levelModel> findAll() {
		Page<levelModel> levelList=dao.paginate(1, 10, "select * ", " from level");
		return levelList;
	}
}
