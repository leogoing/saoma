package model;

import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.utils.PrimaryGenerater;

/*
 * 提现记录
 */
public class Tixianjilu extends Model<Tixianjilu> {

	public static final Tixianjilu dao = new Tixianjilu();

	public Float sumtixian(String dlsname) {
		// TODO Auto-generated method stub
		return null;
	}
	//提现记录搜索按钮实现
	/**完整的SQL语句noreflect和tixiansoushuo 方法
	 *SELECT 
	 *	r.id ,r.swiftno, r.loginname,u.realname, r.phone, r.bankno, r.bankinfo, r.reflecttime, r.reflectmoney
	 *
	 *FROM user as u left join reflect as r on u.loginname = r.loginname
	 *
	 *WHERE r.isreflect=2
	 *
	 *order by r.reflecttime desc
	 */
	//判断如果是只点击搜索，就调用noreflect，否则将参数传入SQL语句中
	public Page<Tixianjilu> tixiansoushuo(Integer pageNumber,Integer pageSize,String xingming) {
			//第一条判断是不是直接点击的搜索，第二条判断是不是随便的参数传递进来，当参数没有问题才走else
		if (xingming.equals("请输入搜索用户名") ||!xingming.matches("[0-9]{11}")) {
			return Tixianjilu.dao.noreflect(pageNumber, pageSize);
		}else {
			Page<Tixianjilu> notx=Tixianjilu.dao.paginate(pageNumber, pageSize, "select r.id ,r.swiftno, r.loginname,u.realname, r.phone, r.bankno, r.bankinfo, r.reflecttime, r.reflectmoney",
					"from user as u left join reflect as r on u.loginname = r.loginname WHERE r.loginname="+xingming+" and r.isreflect=2 order by r.reflecttime desc");
			return notx;
		}
	}
	//进入财务——》体现处理。。后首页自动搜索的数据
		public Page<Tixianjilu> noreflect(Integer pageNumber,Integer pageSize) {
			Page<Tixianjilu> notx=Tixianjilu.dao.paginate(pageNumber, pageSize, "select r.id as rid,u.id as uid ,r.swiftno, r.loginname,u.realname, r.phone, r.bankno, r.bankinfo, r.reflecttime, r.reflectmoney",
					"from user as u left join reflect as r on u.loginname = r.loginname WHERE  isreflect=2 order by reflecttime desc");
			return notx;
		}
		
		
	//进入财务——》已体现处理——》后首页自动搜索的数据
	public Page<Tixianjilu> yesreflect(Integer pageNumber,Integer pageSize) {
		//sql语句增加按时间倒序
		Page<Tixianjilu> yestx=Tixianjilu.dao.paginate(pageNumber, pageSize, "select r.*,u.realname",
				"from  reflect r left join user u on u.loginname=r.loginname WHERE isreflect=1 order by reflecttime desc");
		return yestx;
	}
	//进入财务——》已处理提现——》搜索按钮
	public Page<Tixianjilu> yesreflectsousuo(Integer pageNumber,Integer pageSize,String tixianxingming) {
		
		//第一条判断是不是直接点击的搜索，第二条判断是不是随便的参数传递进来，当参数没有问题才走else
	if (tixianxingming.equals("请输入搜索用户名") || !tixianxingming.matches("[0-9]{11}")) {
			return Tixianjilu.dao.yesreflect(pageNumber, pageSize);
		}else {//sql语句增加按时间倒序
			Page<Tixianjilu> yestx=Tixianjilu.dao.paginate(pageNumber, pageSize, "select * ", "from reflect where loginname="+tixianxingming+" and isreflect=1 order by reflecttime desc");
			return yestx;
		}
	}
		

	public int chuli(String setIn) {
		int update = Db.update("update reflect set isreflect=1 where id in("+ setIn + ")");
		return update;
	}


	

	public final static  JSONArray getDealDetail( int pageNumber, int pageSize,String  userName) {
		return convertQr(paginate(pageNumber, pageSize,userName).getList());
	}
	
	public static  Page<Tixianjilu> paginate(int pageNumber, int pageSize,String userName) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from reflect where ");
		sql.append("loginname='" + userName + "' ");
		sql.append("order by reflecttime desc");
		return Tixianjilu.dao.paginate(pageNumber, pageSize, sql.toString(),"");
	}
	
	
	/**
	 * 将List转换为JSONArray
	 * 
	 * @param qrcodes
	 * @return
	 */
	private final static JSONArray convertQr(List<Tixianjilu> qrcodes) {
		final Iterator<Tixianjilu> iter = qrcodes.iterator();
		final JSONArray resArr = new JSONArray();
		Tixianjilu tmpQrcode = null;
		while (iter.hasNext()) {
			tmpQrcode = iter.next();
			resArr.add(JSON.parse(tmpQrcode.toJson()));
		}
		return resArr;
	}

	//获取当前最大流水号并声称新的
	public String finMaxSwiftno() {
		String swifno=dao.findFirst("select max(swiftno) as maxswiftno from reflect").getStr("maxswiftno");
		String buildswiftno=PrimaryGenerater.getInstance().generaterNextNumber(swifno);
		return buildswiftno;
	}
	
}
