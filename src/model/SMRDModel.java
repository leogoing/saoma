package model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
/**
 * 
 * 查看扫码记录是否重复
 *
 */
public class SMRDModel extends Model<SMRDModel>{
	public static final SMRDModel dao =new SMRDModel();
	
	public List<SMRDModel> RD(String PTname,String openId){
		List<SMRDModel> find=null;
		try {
			find = 
					dao.find("select * from smrecord where attentionPL=? and attpeopleno=?  order by id desc limit 0,2",PTname,openId);
		} catch (Exception e) {
		}
		return find;
	}
}
