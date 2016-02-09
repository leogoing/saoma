package model;

import java.sql.Timestamp;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class Getxiangqing extends Model<Getxiangqing>{
	public static final Getxiangqing dao =new Getxiangqing();
	//查询详情页面的SQL语句
	public Page<Getxiangqing> getxiangqing1(int pageNumber,int pageSize,int idd){
		//打印获取的ID
		System.out.println(idd);
//		Page<Getxiangqing> paginate=null;

		Timestamp lastReflecttime = Getxiangqing.dao.GetLastReflecttime(idd);
		Timestamp beforeLastReflecttime =Getxiangqing.dao.GetReflecttimeBeforeLast(idd);
		String sssql=" from (select b.*,c.username  from (select y.*,w.name from(select z.* from ((select smtime,attpeople, attentionPL,cityget,level,levelid from smrecord  where citylevel=?) union (select smtime,attpeople, attentionPL,countryget,level,levelid from smrecord  where countrylevel=?))as z union (select  smtime,attpeople, attentionPL,personget,level,levelid from smrecord where personlevel=?)) as y join publicaccount as  w on y.attentionPL=w.publicaccountname) as b join user as c on b.levelid=c.id) as mm where mm.smtime between '"+beforeLastReflecttime+"' and '"+lastReflecttime+"'";
		//终极版SQL语句，无重复数据
		String sql=" from(select y.*,w.name from (select z.* from ((select smtime,attpeople, attentionPL,cityget,level,levelid from smrecord  where citylevel=?) union (select smtime,attpeople, attentionPL,countryget,level,levelid from smrecord  where countrylevel=?))as z union (select  smtime,attpeople, attentionPL,personget,level,levelid from smrecord where personlevel=?)) as y join publicaccount as  w on y.attentionPL=w.publicaccountname) as b join user as c on b.levelid=c.id Order By smtime Desc ";
/*		String sssql=" from (select b.*,c.username  from (select y.*,w.name from(select z.* from ((select smtime,attpeople, attentionPL,cityget,level,levelid from smrecord  where citylevel=?) union (select smtime,attpeople, attentionPL,countryget,level,levelid from smrecord  where countrylevel=?))as z union (select  smtime,attpeople, attentionPL,personget,level,levelid from smrecord where personlevel=?)) as y join publicaccount as  w on y.attentionPL=w.publicaccountname) as b join user as c on b.levelid=c.id) as mm where mm.smtime between '"+beforeLastReflecttime+"' and '"+lastReflecttime+"' ";
		String sql=" from(select y.*,w.name from (select sz.* from ((select smtime,attpeople, attentionPL,cityget,level,levelid from smrecord  where citylevel=?) union (select smtime,attpeople, attentionPL,countryget,level,levelid from smrecord  where countrylevel=?))as z union (select  smtime,attpeople, attentionPL,personget,level,levelid from smrecord where personlevel=?)) as y join publicaccount as  w on y.attentionPL=w.publicaccountname) as b join user as c on b.levelid=c.id  group by smtime Order By smtime Desc ";*/
		Page<Getxiangqing> paginate=
				dao.paginate(pageNumber, pageSize, "select DISTINCT *",sql,idd,idd,idd);
		return paginate;

					
	}
	//获得最后一次提现的时间
	public Timestamp GetLastReflecttime (int idd){
		Record c =Db.findFirst("select reflecttime from reflect where reflect.loginname=(select loginname from user where id="+idd+") order by reflecttime desc limit 0,1");
		Timestamp time =c.getTimestamp("reflecttime");
		return time;
		
	}
	//获得倒数第二次提现的时间
	public Timestamp GetReflecttimeBeforeLast(int idd){
		Record c =Db.findFirst("select reflecttime from reflect where reflect.loginname=(select loginname from user where id="+idd+") order by reflecttime desc limit 1,1");
		Timestamp time =new Timestamp(1970);
		if(c!=null){
			time =c.getTimestamp("reflecttime");
		}
		return time;
		
	}
	public float findFengChenMoney(){
		Getxiangqing findFirst = 
				dao.findFirst("select sum(cityget+countrylevel+countryget+fengchengmoney) as money from smrecord");
		return findFirst.getFloat("money");
	}
}
