package model;

import java.util.List;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/*
 * 代理商信息
 */
public class Dailishang extends Model<Dailishang> {

	private static final long serialVersionUID = 1L;
	public static final Dailishang dao = new Dailishang();

	public Page findbydlsname(String name, String type,int pageNumber) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from dls where 1=1 ");

		if ("全部".equals(type)) {
			type = "";
		}

		if (!"".equals(name)) {
			sql.append(" and name='" + name + "'");
		}

		if (!"".equals(type)) {
			sql.append(" and type='" + type + "'");
		}

		System.out.println(sql.toString());
//		List<Dailishang> list = find(sql.toString());
		Page<Dailishang> paginate = paginate(pageNumber, 10, sql.toString(), "");

		return paginate;

	}

	public Dailishang findbydlsid(int id) {
		Dailishang dailishang = findFirst("select * from dls where id=?", id);
		return dailishang;
	}

	public List<Dailishang> getalldlsname() {
		List<Dailishang> list = find("select name from dls");
		return list;
	}

	public List<Dailishang> gethistorydata(String begintime, String endtime,
			String pingtai, String type) {
		StringBuffer s = new StringBuffer();
		s.append("select * from hisdata where 1=1 ");
		if (StrKit.notBlank(begintime)) {
			s.append(" and time>='" + begintime + "'");
		}
		if (StrKit.notBlank(endtime)) {
			s.append(" and time<='" + endtime + "'");
		}
		if (StrKit.notBlank(pingtai)) {
			s.append(" and pingtai='" + pingtai + "'");
		}
		if (StrKit.notBlank(type)) {
			s.append(" and type='" + type + "'");
		}
		List<Dailishang> list = find(s.toString());
		return list;
	}

	public List<Dailishang> getxiaxian(String s) {
		List<Dailishang> list = find("select * from dls where shangxian=?", s);
		return list;
	}

}
