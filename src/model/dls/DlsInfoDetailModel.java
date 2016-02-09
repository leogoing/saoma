package model.dls;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.utils.DateUtils;

public class DlsInfoDetailModel extends Model<DlsInfoDetailModel> {
	public static final DlsInfoDetailModel dao = new DlsInfoDetailModel();

	// 根据平台查找不同的值getValueFromPlant-->昨日收入
	public StringBuffer getValueFromPlantQueryIncome(int userId, String now) {
		StringBuffer sbuf = new StringBuffer();// 定义StringBuffer拼接字符串
		sbuf.append(
				"select s.countrylevel ,s.personlevel ,s.citylevel, s.countryget ,s.personget,s.cityget,s.smtime,s.cancletime,p.name as attentionPL ")
				.append("from smrecord s left join publicaccount p on p.publicaccountname = s.attentionPL where s.levelid = "
						+ userId
						+ " and state = 1 and cancletime is null and  smtime  between date_sub(curdate(),interval 1 day) and '"
						+ now + "'  order by attentionPL ");
		return sbuf;

	}

	// 根据平台查找不同的值getValueFromPlant-->本月收入
	public StringBuffer getValueFromPlantAprilIncome(int userId) {
		StringBuffer sbuf = new StringBuffer();// 定义StringBuffer拼接字符串
		sbuf.append(
				"select s.countrylevel ,s.personlevel ,s.citylevel, s.countryget ,s.personget,s.cityget,s.smtime,s.cancletime,p.name as attentionPL  ")
				.append("from smrecord s left join publicaccount p on p.publicaccountname = s.attentionPl where s.levelid = "
						+ userId
						+ " and state = 1  and  smtime  between  DATE_ADD(curdate(),interval -day(curdate())+1 day)  and now()");
		return sbuf;
	}

	// 获取下线扫描getFindUnderScanFromDifferentPlant-->昨日收入
	public StringBuffer getFindUnderScanFromDifferentPlantQueryIncome(int userId, String now) {
		StringBuffer sbuf = new StringBuffer();// 定义StringBuffer拼接字符串
		sbuf.append(
				"select s.countrylevel ,s.personlevel ,s.citylevel, s.countryget ,s.personget,s.cityget,s.smtime,s.cancletime,p.name as attentionPL   ")
				.append("from smrecord s left join publicaccount p on p.publicaccountname = s.attentionPl where s.levelid <> "
						+ userId
						+ " and state = 1 and cancletime is null and  smtime  between date_sub(curdate(),interval 1 day) and  '"
						+ now + "' order by attentionPL ");
		return sbuf;
	}

	// 获取下线扫描getFindUnderScanFromDifferentPlant-->本月收入
	public StringBuffer getFindUnderScanFromDifferentPlantAprilIncome(int userId) {
		StringBuffer sbuf = new StringBuffer();// 定义StringBuffer拼接字符串
		sbuf.append(
				"select s.countrylevel ,s.personlevel ,s.citylevel, s.countryget ,s.personget,s.cityget,s.smtime,s.cancletime,p.name as attentionPL  ")
				.append("from smrecord s left join publicaccount p on p.publicaccountname = s.attentionPl where s.levelid <> "
						+ userId
						+ " and state = 1 and cancletime is null and  smtime  between  DATE_ADD(curdate(),interval -day(curdate())+1 day)  and now()");
		return sbuf;
	}

	// 扫描信息流程getScanFlowInfo
	public StringBuffer getScanFlowInfosaomiao(int agentType, int userId) {
		StringBuffer sql = new StringBuffer();
		//修改完重复数据问题了、、group by id
		sql.append(
				"select * from(select s.id,s.countrylevel,s.headimgurl ,s.personlevel ,s.citylevel, s.countryget ,s.personget,s.cityget,s.smtime,s.cancletime,s.attpeople,p.name from smrecord  s left join ")
				.append("publicaccount p on p.publicaccountname = s.attentionPl where state=1 and cancletime is null  and personlevel=" + userId
						+ " or countrylevel=" + userId + " or citylevel=" + userId + " group by id ");
		if (agentType == 1) {
			sql.append(" order by  id desc  limit 20 ) as m where m.cancletime is null order by id desc");
		} else if (agentType == 2) {
			sql.append(" order by id desc  limit 40 ) as m where m.cancletime is null order by id desc");
		} else {
			sql.append(" order by id desc  limit 50 ) as m where m.cancletime is null order by id desc");
		}
		return sql;
	}

	// 查询最后一次提现时间
	public float doCalculateIncomeForDifferentAgentExtractionTime(String day,int angentType, String loginUser, int userId) {
		Record user=Db.findById("user", userId);
		loginUser=user.getStr("loginname");
		StringBuffer sbuf = new StringBuffer();
		String now = DateUtils.getday("today");//时间
		// 查询最后一次提现时间
		sbuf.append("select  reflecttime from  reflect where loginname ='"
				+ loginUser + "' order by reflecttime desc  limit 1");
		List<Record> tmp = Db.find(sbuf.toString());
		String getTime;
		if (tmp.isEmpty()) {
			getTime = "2015-08-01 09:00:00";
		} else {
			getTime = isTimeNull(tmp.get(0).get("reflecttime"));
		}

		float sum = 0;
		String nowYMD = DateUtils.getYMD() + "%";
		// day 1:累计收入 2：昨日利润查询 3：余额查询 0：今日收入4：本月收入
		if (day.equals("1")) {
			sbuf = new StringBuffer();
			sbuf.append("select sum(personget) sum  from smrecord where personlevel="
					+ userId + "  and state=1");
			List<Record> list = Db.find(sbuf.toString());
			sum += changeVal(list.get(0).get("sum"));
			sbuf = new StringBuffer();
			sbuf.append("select sum(countryget) sum from smrecord where countrylevel="
					+ userId + "   and state=1");
			list = Db.find(sbuf.toString());
			sum += changeVal(list.get(0).get("sum"));
			sbuf = new StringBuffer();
			sbuf.append("select sum(cityget) sum from smrecord where citylevel="
					+ userId + "  and state=1");
			list = Db.find(sbuf.toString());
			sum += changeVal(list.get(0).get("sum"));
		System.out.println("总额sum："+sum);
		}else if(day.equals("4")){ 
			Calendar c = Calendar.getInstance();
			  // 这是已知的日期
			  Date d = new Date();
			  c.setTime(d);
			  c.set(Calendar.DAY_OF_MONTH, 1);
			  c.set(Calendar.HOUR_OF_DAY, 0);
			  c.set(Calendar.MINUTE, 0);
			  c.set(Calendar.SECOND, 0);
			  d=c.getTime();
			  System.out.println("本月时间"+d.toLocaleString());
			sbuf = new StringBuffer();
			sbuf.append("select sum(personget) sum  from smrecord where personlevel="
					+ userId + "  and state=1 and smtime>'"+d.toLocaleString()+"'");
			List<Record> list = Db.find(sbuf.toString());
			sum += changeVal(list.get(0).get("sum"));
			sbuf = new StringBuffer();
			sbuf.append("select sum(countryget) sum from smrecord where countrylevel="
					+ userId + "   and state=1 and smtime>'"+d.toLocaleString()+"'");
			list = Db.find(sbuf.toString());
			sum += changeVal(list.get(0).get("sum"));
			sbuf = new StringBuffer();
			sbuf.append("select sum(cityget) sum from smrecord where citylevel="
					+ userId + "   and state=1 and smtime>'"+d.toLocaleString()+"'");
			list = Db.find(sbuf.toString());
			sum += changeVal(list.get(0).get("sum"));
		System.out.println("本月sum："+sum);
		} else if (day.equals("2")) {
			sbuf = new StringBuffer();
			sbuf.append("select sum(personget) sum  from smrecord where personlevel="
					+ userId
					+ "  and state=1 and  smtime  between DATE_ADD(curdate(),interval -day(curdate())+1 day) and '"
					+ now + "' ");
			List<Record> list = Db.find(sbuf.toString());
			sum += changeVal(list.get(0).get("sum"));
			sbuf = new StringBuffer();
			sbuf.append("select sum(countryget) sum from smrecord where countrylevel="
					+ userId
					+ "  and state=1 and  smtime  between DATE_ADD(curdate(),interval -day(curdate())+1 day) and '"
					+ now + "' ");
			list = Db.find(sbuf.toString());
			sum += changeVal(list.get(0).get("sum"));
			sbuf = new StringBuffer();
			sbuf.append("select sum(cityget) sum from smrecord where citylevel="
					+ userId
					+ "   and state=1 and  smtime  between DATE_ADD(curdate(),interval -day(curdate())+1 day) and '"
					+ now + "' ");
			list = Db.find(sbuf.toString());
			sum += changeVal(list.get(0).get("sum"));
		} else if (day.equals("0")) {
			sbuf = new StringBuffer();
			sbuf.append("select sum(personget) sum  from smrecord where personlevel="
					+ userId
					+ "  and state=1 and  smtime like '"
					+ nowYMD + "'");
			List<Record> list = Db.find(sbuf.toString());
			sum += changeVal(list.get(0).get("sum"));
			sbuf = new StringBuffer();
			sbuf.append("select sum(countryget) sum from smrecord where countrylevel="
					+ userId
					+ "  and state=1 and  smtime  like '"
					+ nowYMD + "'");
			list = Db.find(sbuf.toString());
			sum += changeVal(list.get(0).get("sum"));
			sbuf = new StringBuffer();
			sbuf.append("select sum(cityget) sum from smrecord where citylevel="
					+ userId
					+ "   and state=1 and  smtime  like '"
					+ nowYMD + "'");
			list = Db.find(sbuf.toString());
			sum += changeVal(list.get(0).get("sum"));
		} else {
			sbuf = new StringBuffer();
			if (getTime != null && !"".equals(getTime)) {
				sbuf.append("select sum(personget) sum  from smrecord where personlevel="
						+ userId
						+ " and state=1 and  smtime  between '"
						+ getTime + "' and now() ");
			} else {
				sbuf.append("select sum(personget) sum  from smrecord where personlevel="
						+ userId
						+ " and state=1 and  smtime  < now() ");
			}
			List<Record> list = Db.find(sbuf.toString());
			sum += changeVal(list.get(0).get("sum"));
			sbuf = new StringBuffer();
			if (getTime != null && !"".equals(getTime)) {
				sbuf.append("select sum(countryget) sum from smrecord where countrylevel="
						+ userId
						+ "   and state=1 and  smtime  between '"
						+ getTime + "' and now() ");
			} else {
				sbuf.append("select sum(countryget) sum from smrecord where countrylevel="
						+ userId
						+ "  and state=1 and  smtime  < now() ");
			}
			list = Db.find(sbuf.toString());
			sum += changeVal(list.get(0).get("sum"));
			sbuf = new StringBuffer();
			if (getTime != null && !"".equals(getTime)) {
				sbuf.append("select sum(cityget) sum from smrecord where citylevel="
						+ userId
						+ "  and state=1 and  smtime  between '"
						+ getTime + "' and now()  ");
			} else {
				sbuf.append("select sum(cityget) sum from smrecord where citylevel="
						+ userId
						+ "  and state=1 and smtime  < now() ");
			}

			list = Db.find(sbuf.toString());
			sum += changeVal(list.get(0).get("sum"));

		}
		
		return sum;
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
	/**
	 * // 查询最后一次提现时间::的两个工具类之	2
	 * @param object
	 * @return
	 */
	private static float changeVal(Object object) {
		if (object == null || object.equals(""))
			return 0;
		return Float.valueOf(object.toString());
	}
	//扫描信息流程（演示模版）
	public StringBuffer moban(int userId){
		StringBuffer sql = new StringBuffer();
		sql.append("select s.headimgurl,s.countrylevel ,s.personlevel ,s.citylevel, s.countryget ,"
				+ "s.personget,s.cityget,s.smtime,s.cancletime,s.attpeople,p.name from smrecord  s left join ")
				.append("publicaccount p on p.publicaccountname = s.attentionPl where state=1 and cancletime is null and personlevel="
					+ userId
					+ " or countrylevel="
					+ userId
					+ " or citylevel=" + userId + " ");
		sql.append("order by smtime desc limit 15");
		return sql;
	}
}
