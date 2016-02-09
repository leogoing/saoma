package model;

import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import model.caiwu.ggzcaiwuModel;
import util.GetTime;

/*
 * 代理商信息
 */
public class userModel extends Model<userModel> {

	private static final long serialVersionUID = 1L;
	public static final userModel dao = new userModel();

	public Page<userModel> findbydlsname(String name, String levelid,
			int pageNumber) {
		StringBuffer sql = new StringBuffer();
		sql.append("select  u.*,levelname from user u,level l where u.levelid=l.id ");
		if ("全部".equals(levelid)) {
			levelid = "";
		}
		if (!"".equals(name)) {
			sql.append(" and (realname= '" + name + "' or username= '" + name
					+ "' or loginname='" + name + "')");
		}
		if (!"".equals(levelid)) {
			sql.append(" and levelid='" + levelid + "'");
		}
		System.out.println(sql.toString());
		// List<Dailishang> list = find(sql.toString());
		Page<userModel> paginate = paginate(pageNumber, 10, sql.toString(), "");

		return paginate;

	}

	public userModel findbydlsid(int id) {
		userModel dailishang = findFirst("select * from dls where id=?", id);
		return dailishang;
	}
	
	public void updateFirstTime(int id){
		Db.update("update user set firsttime=? where id=?",GetTime.getTime(),id);
	}

	public Page hisjilu(Integer id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select u.*,levelname,bankname from user u,bank b,level l where u.bankid=b.id and u.levelid=l.id and u.id = ");
		if (!"".equals(id)) {
			sql.append("'" + id + "'");
		}
		Page<userModel> paginate = paginate(1, 10, sql.toString(), "");
		return paginate;
	}

	public List<userModel> getalldlsname() {
		List<userModel> list = find("select name from dls");
		return list;
	}

	public List<userModel> gethistorydata(String begintime, String endtime,
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
		List<userModel> list = find(s.toString());
		return list;
	}

	public List<userModel> getxiaxian(String s) {
		List<userModel> list = find("select * from dls where shangxian=?", s);
		return list;
	}

	public final static JSONArray getAgentUserDetail(int pageNumber,
			int pageSize, int userAgent) {
		return convertQr(paginate(pageNumber, pageSize, userAgent).getList());
	}

	public static Page<userModel> paginate(int pageNumber, int pageSize,
			int userAgent) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from user where 1=1 ");
		if (userAgent == 1) {// 个人
			sql.append(" and agentlevel='" + userAgent + "' ");
		}
		if (userAgent == 2) {
			sql.append(" and agentlevel='" + userAgent + "' ");
		}
		sql.append("order by id");
		return userModel.dao.paginate(pageNumber, pageSize, sql.toString(), "");
	}
	

	/**
	 * 将List转换为JSONArray
	 * 
	 * @param qrcodes
	 * @return
	 */
	private final static JSONArray convertQr(List<userModel> qrcodes) {
		final Iterator<userModel> iter = qrcodes.iterator();
		final JSONArray resArr = new JSONArray();
		userModel tmpQrcode = null;
		while (iter.hasNext()) {
			tmpQrcode = iter.next();
			resArr.add(JSON.parse(tmpQrcode.toJson()));
		}
		return resArr;
	}

	public Integer findByLoginname(String loginname) {
		Integer id =null;
		//如果输入的用户名不是空字符串则查找
			userModel user = dao.findFirst("select * from user where loginname=?", loginname);
			if(user==null){
				user = dao.findFirst("select * from user where realname=?", loginname);
			}
			try {
				id = user.getInt("id");
			} catch (Exception e) {
			}
		return id;
	}

	/**
	 * 通过id查找上线
	 * 
	 * @param id
	 * @return
	 */
	public Page findByedgeId(Object id) {
		Page user = dao.paginate(1, 10, "select * ", "from user where id=?",dao.findById(id).get("leader"));
		return user;
	}
	
	public userModel findSXbyId(Integer id){
		userModel sx=dao.findFirst("select * from user where id=?",dao.findById(id).get("leader"));
		return sx;
	}

	/**
	 * 通过id查找下线
	 * 
	 * @param id
	 * @return
	 */
	public Page<userModel> findedge(Integer pageNumber,Object id) {// 下线
		Page<userModel> pageList = dao.paginate(pageNumber, 5, "select * ",
				"from user where leader=?", id);
		return pageList;
	}

	/**
	 * 通过loginname获取当前登陆用户所有信息
	 * @param loginname
	 * @return
	 */
	public userModel findLUbyLoginname(String loginname) {
		userModel user=dao.findFirst("select * from user where loginname=?",loginname);
		if(user==null){
			user=dao.findFirst("select * from user where realname=?",loginname);
		}
		return user;
	}

	
	/**
	 * 通过id查找所有下线
	 * @param id
	 * @return
	 */
	public List<userModel> findXXById(Integer id) {
		List<userModel> xxList=dao.find("select * from user where leader=?",id);
		return xxList;
	}
	/**
	 *  获得所有的广告主
	 * @param 页号
	 * @param 一页多少
	 * @return	所有广告主
	 */
	public Page<userModel> ggzcunqian(int pageNumber, int pageSize) {
		//获取所有levelid（等级）为8的所有广告主
		Page<userModel> ggzquan = userModel.dao.paginate(pageNumber, pageSize, "select id,loginname,realname,phone,bankno ", "from user where levelid = 8");
		return ggzquan;
	}
	/**
	 * 根据登录名（账号）获得广告主
	 * @param 页号
	 * @param 一页多少
	 * @param 登录名
	 * @return 根据登录名（账号）广告主
	 */
	public Page<userModel> cwggzsousuoModel(Integer pageNumber,Integer pageSize,String xingming) {
		//判断，如果是直接点击进来的话
		if (xingming == null || xingming.equals("输入：登录名（账号）")) {
			return userModel.dao.ggzcunqian(pageNumber, pageSize);
		}else {
			//获取根据登录名（账号）指定的广告主
			Page<userModel> ggzcai = userModel.dao.paginate(pageNumber, pageSize,"select id,loginname,realname,phone,bankno", "from user where loginname = '" +xingming+ "' and levelid = 8 ");
			return ggzcai;
		}
	}

	/**
	 * 根据传入的登录名（loginname）获得ID
	 * @param 字符串类型的用户名
	 * @return	字符串类型的ID
	 */
	public Integer yyyLDSid(String loginname){
		List<userModel> LDSid = userModel.dao.find("select id from user where loginname='"+loginname+"'");
		//获得第1位
		userModel ids =  LDSid.get(0);
		//转成int类型
		Integer id = ids.get("id");
		return id;
	}

}
