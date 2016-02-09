package model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/*
 * 公众平台信息
 */
public class Gongzhonghao extends Model<Gongzhonghao>{

	private static final long serialVersionUID = 1L;
	public static final Gongzhonghao dao=new Gongzhonghao();
	
	public Page<Gongzhonghao> getup() {
		Page<Gongzhonghao> lowlist=Gongzhonghao.dao.paginate(1, 10, "select * ", "from publicaccount where uplowframe=1");
		return lowlist;
	}
	
	public Page<Gongzhonghao> getlow() {
		Page<Gongzhonghao> lowlist=Gongzhonghao.dao.paginate(1, 10, "select * ", "from publicaccount where uplowframe=2");
		return lowlist;
	}

	public Page<Gongzhonghao> getgzhAll() {
		Page<Gongzhonghao> gzh=Gongzhonghao.dao.paginate(1,10, "select * ", " from publicaccount ");
		return gzh;
	}
	//查询数据库publicaccount表
	public List<Gongzhonghao> getgzhlist() {
		List<Gongzhonghao> gzh=Gongzhonghao.dao.find("select * from publicaccount");
		return gzh;
	}
}
