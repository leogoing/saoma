package model.dls;

import java.text.SimpleDateFormat;
import java.util.List;

import com.alibaba.druid.pool.vendor.SybaseExceptionSorter;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.utils.DateUtils;

import util.GetTime;

public class DlsTixianControllerModel extends Model<DlsTixianControllerModel>{
	public static final DlsTixianControllerModel dao = new DlsTixianControllerModel();
	
	String tomorrow=DateUtils.getday("tomorrow");//时间
	String now = DateUtils.getday("today");//时间
	//获取余额的3个SQL语句	1
	public StringBuffer personal(String userName, int userId){
		StringBuffer sbufre = new StringBuffer();
		String now = DateUtils.getday("today");//时间
		// 查询最后一次提现时间
		sbufre.append("select  reflecttime from  reflect where loginname ='"
				+ userName + "' order by reflecttime desc  limit 1");
		List<Record> tmp = Db.find(sbufre.toString());
		String getTime;
		if (tmp.isEmpty()) {
			getTime = "";
		} else {
			getTime = isTimeNull(tmp.get(0).get("reflecttime"));
		}
		StringBuffer sbuf = new StringBuffer();
		//如果有过提现则查询从最后一次提现到现在的总收入
		if (getTime != null && !"".equals(getTime)) {
		sbuf.append("select sum(personget) as person from smrecord  where smtime  between  ( select  reflecttime from ")
			.append("reflect  where loginname ='"+ userName+ "' order by reflecttime desc  limit 1  )   and now()  and personlevel="
						+ userId + " ")
				.append(" and state =1 ");
		}else{//如果没有提现过则查询当前时间前的总收入
			sbuf.append("select sum(personget) as person  from smrecord where personlevel="
					+ userId
					+ " and state=1 and  smtime  < now() ");
		}
		return sbuf;
	}
	//获取余额的3个SQL语句	2
	public StringBuffer city(String userName, int userId){
		StringBuffer sbufre = new StringBuffer();
		String now = DateUtils.getday("today");//时间
		// 查询最后一次提现时间
		sbufre.append("select  reflecttime from  reflect where loginname ='"
				+ userName + "' order by reflecttime desc  limit 1");
		List<Record> tmp = Db.find(sbufre.toString());
		String getTime;
		if (tmp.isEmpty()) {
			getTime = "";
		} else {
			getTime = isTimeNull(tmp.get(0).get("reflecttime"));
		}
		StringBuffer sbuf = new StringBuffer();
		//如果有过提现则查询从最后一次提现到现在的总收入
		if (getTime != null && !"".equals(getTime)) {
		sbuf.append("select sum(cityget) AS city from smrecord  where smtime  between  ( select  reflecttime from ")
			.append("reflect  where loginname ='"+ userName+ "' order by reflecttime desc  limit 1 )   and now()  and citylevel="
						+ userId + " ")
				.append("  and state =1 ");
		}else{//如果没有提现过则查询当前时间前的总收入
			sbuf.append("select sum(cityget) AS city from smrecord where citylevel="
					+ userId
					+ "  and state=1 and smtime  < now() ");
		}
		return sbuf;
	}
	//获取余额的3个SQL语句	3
	public StringBuffer country(String userName, int userId){
		StringBuffer sbufre = new StringBuffer();
		String now = DateUtils.getday("today");//时间
		// 查询最后一次提现时间
		sbufre.append("select  reflecttime from  reflect where loginname ='"
				+ userName + "' order by reflecttime desc  limit 1");
		List<Record> tmp = Db.find(sbufre.toString());
		String getTime;
		if (tmp.isEmpty()) {
			getTime = "";
		} else {
			getTime = isTimeNull(tmp.get(0).get("reflecttime"));
		}
		StringBuffer sbuf = new StringBuffer();
		//如果有过提现则查询从最后一次提现到现在的总收入
		if (getTime != null && !"".equals(getTime)) {
		sbuf.append("select sum(countryget) as country from smrecord  where smtime  between  ( select  reflecttime from ")
		.append("reflect  where loginname ='"+ userName+ "' order by reflecttime desc  limit 1 )   and now()  and countrylevel="
					+ userId + " ")
		.append("  and state =1 ");
		}else{//如果没有提现过则查询当前时间前的总收入
			sbuf.append("select sum(countryget) as country from smrecord where countrylevel="
					+ userId
					+ "  and state=1 and  smtime  < now() ");
		}
		return sbuf;
	}
	/**
	 * // 查询最后一次提现时间::的两个工具类之	1
	 * @param time
	 * @return
	 */
	private String isTimeNull(Object time) {
		if (time == null) {
			return null;
		} else {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
		}

	}
	
	
}
