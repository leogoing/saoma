package model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
/**
 * 银行model
 * @author csc
 *
 */
public class bankModel extends Model<bankModel> {

	private static final long serialVersionUID = 1L;
	
	public static final bankModel dao=new bankModel();

	public Page<bankModel> findAll() {
		Page<bankModel> bankList=dao.paginate(1, 10,"select * "," from bank");
		return bankList;
	}

	public Integer getMaxId() {
		bankModel bank =dao.findFirst("select * from bank order by id desc");
		Integer id=bank.getInt("id")+1;
		return id;
	}
}
