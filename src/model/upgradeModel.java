package model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.utils.DateUtils;
import com.utils.PrimaryGenerater;

public class upgradeModel extends Model<upgradeModel> {

	private static final long serialVersionUID = 1L;
	
	public static final upgradeModel dao=new upgradeModel();
	
	//升级
	public Page<upgradeModel> alllevelid(Integer pageNumber,Integer pageSize,String xingming,String dagai) {
		//实现财务，处理申请升级页面中的查询
		
		if(xingming==null || xingming.equals("请输入申请用户名")){
			if(dagai==null || dagai.equals("全部")){
				StringBuffer sql=new StringBuffer();
				sql.append("from(select a.*,b.wantname  from (select u.*,l.levelname as levelidname from upgrade u join level l  on u.levelid=l.id) as a  join ");
				sql.append(" (select u.*,l.levelname as wantname from upgrade u join level l on u.wantlevel=l.id) as b  on a.id=b.id where a.ispass=2  ) as x join ");
				sql.append(" (select u.loginname,if((select username from user where id = u.leader) is null,'没有',(select username from user where id = u.leader)) as leaderName,");
				sql.append("IF((select username from user where id = u.edge) IS NULL,'没有',(select username from user where id = u.edge)) as edgeName from user u) as y on y.loginname=x.loginname order by x.applytime desc");
				//		Page<upgradeModel> listl = dao.paginate(pageNumber, pageSize, "select x.*,y.leaderName as leadername,y.edgeName as edgename ",sql.toString());
				Page<upgradeModel> listl = dao.paginate(pageNumber, pageSize, "select x.*,y.leaderName as leadername,y.edgeName as edgename ",sql.toString());
				return listl;
			}else if(dagai!=null || !dagai.equals("全部")){
				String u="select xx.* from (select x.*,y.leaderName as leadername,y.edgeName as edgename  from(select a.*,b.wantname  from (select u.*,l.levelname as levelidname from upgrade u join level l  on u.levelid=l.id) as a  join  (select u.*,l.levelname as wantname from upgrade u join level l on u.wantlevel=l.id) as b  on a.id=b.id where a.ispass=2  ) as x join  (select u.loginname,if((select username from user where id = u.leader) is null,'没有',(select username from user where id = u.leader)) as leaderName,IF((select username from user where id = u.edge) IS NULL,'没有',(select username from user where id = u.edge)) as edgeName from user u) as y on y.loginname=x.loginname)as xx where xx.wantname='"+dagai+"' order by xx.applytime desc";
				//		Page<upgradeModel> listl = dao.paginate(pageNumber, pageSize, "select x.*,y.leaderName as leadername,y.edgeName as edgename ",sql.toString());
				Page<upgradeModel> listl = dao.paginate(pageNumber, pageSize, u,"");
				return listl;
			}
		}else{
			String u="select xx.* from (select x.*,y.leaderName as leadername,y.edgeName as edgename  from(select a.*,b.wantname  from (select u.*,l.levelname as levelidname from upgrade u join level l  on u.levelid=l.id) as a  join  (select u.*,l.levelname as wantname from upgrade u join level l on u.wantlevel=l.id) as b  on a.id=b.id where a.ispass=2  ) as x join  (select u.loginname,if((select username from user where id = u.leader) is null,'没有',(select username from user where id = u.leader)) as leaderName,IF((select username from user where id = u.edge) IS NULL,'没有',(select username from user where id = u.edge)) as edgeName from user u) as y on y.loginname=x.loginname)as xx where xx.loginname='"+xingming+"' order by xx.applytime desc";
			//		Page<upgradeModel> listl = dao.paginate(pageNumber, pageSize, "select x.*,y.leaderName as leadername,y.edgeName as edgename ",sql.toString());
			Page<upgradeModel> listl = dao.paginate(pageNumber, pageSize, u,"");
			return listl;
		}
		StringBuffer sql=new StringBuffer();
//		sql.append(" from (select u.*,l.levelname as levelidname from upgrade u join level l  on u.levelid=l.id) as a ");
//		sql.append(" join (select u.*,l.levelname as wantname from upgrade u join level l on u.wantlevel=l.id) as b ");
//		sql.append(" on a.id=b.id where a.ispass=2 order by a.applytime desc ");
		//sql语句增加了上线人和下线人2个字段
		sql.append(" from(select a.*,b.wantname  from (select u.*,l.levelname as levelidname from upgrade u join level l  on u.levelid=l.id) as a  join ");
		sql.append(" (select u.*,l.levelname as wantname from upgrade u join level l on u.wantlevel=l.id) as b  on a.id=b.id where a.ispass=2  ) as x join ");
		sql.append(" (select u.loginname,if((select username from user where id = u.leader) is null,'没有',(select username from user where id = u.leader)) as leaderName,");
		sql.append("IF((select username from user where id = u.edge) IS NULL,'没有',(select username from user where id = u.edge)) as edgeName from user u) as y on y.loginname=x.loginname order by x.applytime desc");
		//		Page<upgradeModel> listl = dao.paginate(pageNumber, pageSize, "select x.*,y.leaderName as leadername,y.edgeName as edgename ",sql.toString());
		Page<upgradeModel> listl = dao.paginate(pageNumber, pageSize, "select x.*,y.leaderName as leadername,y.edgeName as edgename ",sql.toString());
		return listl;
	}

	public void shengji(String[] values) {
		for(int i=0;i<values.length;i++){
			Record sjjl=Db.findById("upgrade", values[i]);
			Integer wantlevel=sjjl.getInt("wantlevel");
			Integer levelid=sjjl.getInt("levelid");
			String loginname=sjjl.getStr("loginname");
			String ufinalleader=null;
			if(levelid.equals(1)){//发展下线
				Record user=new userModel().set("loginname", sjjl.get("loginname")).set("realname",sjjl.get("realname") ).set("password",sjjl.get("password") ).set("phone",sjjl.get("phone") ).set("email", sjjl.get("email")).set("idcard", sjjl.get("idcard")).set("bankid",sjjl.get("bankid") ).set("bankno",sjjl.get("bankno") ).set("bankinfo",sjjl.get("bankinfo") ).set("leader",sjjl.get("leader") ).set("levelid",sjjl.get("wantlevel") ).toRecord();
				Db.save("user",user );
				//新用户虚拟提现时间
				Record txu=Db.findFirst("select * from user where loginname=?",sjjl.get("loginname"));
				String swiftno=Tixianjilu.dao.finMaxSwiftno();
				Record tx=new Tixianjilu().set("loginname", txu.get("loginname")).set("levelid",txu.get("id")).set("reflecttime", DateUtils.getday("now"))
						.set("isreflect", 1).set("swiftno", swiftno).set("reflectmoney", 0).toRecord();
				Db.save("reflect", tx);
				
			}else{//升级
				if(sjjl.get("leader")!=null){
					Record userleader=Db.findById("user", sjjl.get("leader"));
					if(userleader.get("leader")!=null){
						ufinalleader=userleader.get("leader");
					}
					Db.update("update user set levelid = ?,leader=? where loginname=?",wantlevel,ufinalleader,loginname);
				}else{
					Db.update("update user set levelid = ? where loginname=?",wantlevel,loginname);
				}
			}
			Db.update("update upgrade set ispass =1 where loginname=?",loginname);
		}
		/*
		 * 根据代理商提交来的升级申请，判断这个ID号是升级到什么级别
		 * 根据他所升级的级别，更新代理商表，并写入升级记录表
		 */
		
	}

	/**
	 * 获取当前最大流水号并生成
	 * @return
	 */
	public String finMaxSwiftno() {
		String swifno=dao.findFirst("select max(swiftno) as maxswiftno from upgrade").getStr("maxswiftno");
		String buildswiftno=PrimaryGenerater.getInstance().generaterNextNumber(swifno);
		return buildswiftno;
	}
}
