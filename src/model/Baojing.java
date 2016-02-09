package model;

import Static.Static;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/*
 * 报警记录
 */
public class Baojing extends Model<Baojing> {

	private static final long serialVersionUID = 1L;
	public static final Baojing dao = new Baojing();

	public Page<Baojing> getinfo(int pageno, int pagesize) {
		Page<Baojing> page = paginate(pageno, Static.pagesize, "select *",
				" from saomajilu where id=1");
		return page;
	}

}
