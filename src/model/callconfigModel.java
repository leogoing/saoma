package model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
/**
 * 呼叫配置MODEL
 * @author csc
 *
 */
public class callconfigModel extends Model<callconfigModel> {

	private static final long serialVersionUID = 1L;

	public static final callconfigModel dao=new callconfigModel();

	public Page<callconfigModel> findConfig() {
		Page<callconfigModel> bjList=dao.paginate(1, 1, "select * ", "from callconfig");
		return bjList;
	}
}
