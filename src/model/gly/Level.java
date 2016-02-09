package model.gly;

import model.levelModel;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class Level extends Model<Level>{
	public static final Level dao =new Level();
	public Page<Level> aa(){
//		Page<levelModel> caiwu = levelModel.dao.paginate(1, 5, "select * ",
//				" from level where id=5");
		Page<Level> caiwu =dao.paginate(1, 5, "select * ",
				" from level where id=5");
		return caiwu;
	}
}
