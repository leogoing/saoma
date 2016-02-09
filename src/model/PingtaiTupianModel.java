package model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

public class PingtaiTupianModel extends Model<PingtaiTupianModel>{
	public static final PingtaiTupianModel dao = new PingtaiTupianModel();
	
	public List<PingtaiTupianModel> findTupian(int id){
		System.out.println(id);
		List<PingtaiTupianModel> find = 
				dao.find("select * from advertmaster where pingtaiId=?",id);
		return find;
	}
	/**
	 * 只获得一条数据
	 * @param id	平台ID
	 * @return	1条数据集合
	 */
	public List<PingtaiTupianModel> findTupianOne(int id){
		System.out.println(id);
		List<PingtaiTupianModel> find = 
				dao.find("select * from advertmaster where pingtaiId=? limit 1",id);
		return find;
	}
}
