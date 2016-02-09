package controller.gly;

import java.util.List;

import model.Gongzhonghao;
import model.Saomajilu;
import model.levelModel;
import model.userModel;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import model.gly.User;

/**
 * 管理员模块——数据查询
 * 
 * @author zmc
 *
 */
public class GlyDlsDataController extends Controller {

	/**
	 * 第一步的数据查询调用代理商列表页的那个方法
	 */
	public void todlslist() {
		Page<User> userList = User.dao.hh(getParaToInt(0, 1),10);
		setAttr("userList", userList);
		// 查找所有公众平台和代理商级别
		Page<Gongzhonghao> gzhList = Gongzhonghao.dao.getgzhAll();
		Page<levelModel> levelList = levelModel.dao.findAll();
		setAttr("gzhList", gzhList);
		setAttr("levelList", levelList);
		render("/guanliyuan/shujuchaxun.html");
	}

	/**
	 * 根据代理商查归属于他的下线扫码记录信息
	 */
	public void findbyguishu() {
		// List list = userModel.dao.hisxiaxianjilu();
		// setAttr("xiaxian", list);
	}

	/**
	 * 查询该用户所有扫码记录
	 */
	public void findAllSMR() {
		Integer id = getParaToInt("u.id");
		Integer levelid = getParaToInt("u.levelid");
		setSessionAttr("dlsid", id);
		setSessionAttr("dlslevelid", levelid);
		Page<Saomajilu> smList = Saomajilu.dao.findAllById(getParaToInt(0, 1),id, levelid);
		setAttr("smList", smList);

		// 查询上线、下线名称
		Page sxList = userModel.dao.findByedgeId(id);// 上线
		Page<userModel> xxList = userModel.dao.findedge(getParaToInt(0, 1),id);// 下线
		setAttr("sxList", sxList);
		setAttr("xxList", xxList);

		// 查找所有公众平台和代理商级别
		Page<Gongzhonghao> gzhList = Gongzhonghao.dao.getgzhAll();
		setAttr("gzhList", gzhList);
		Page<levelModel> levelList = levelModel.dao.findAll();
		setAttr("levelList", levelList);

		render("/guanliyuan/jiansuowanglaishuju.html");
	}
	/**
	 * 查询该用户所有扫码记录
	 */
	public void findAllSMRFenYe() {
		
		Integer id = getSessionAttr("dlsid");
		Integer levelid=getSessionAttr("dlslevelid");
		Page<Saomajilu> smList = Saomajilu.dao.findAllById(getParaToInt("sm", 1),id, levelid);
		setAttr("smList", smList);

		// 查询上线、下线名称
		Page sxList = userModel.dao.findByedgeId(id);// 上线
		Page<userModel> xxList = userModel.dao.findedge(getParaToInt("xx", 1),id);// 下线
		setAttr("sxList", sxList);
		setAttr("xxList", xxList);

		// 查找所有公众平台和代理商级别
		Page<Gongzhonghao> gzhList = Gongzhonghao.dao.getgzhAll();
		setAttr("gzhList", gzhList);
		Page<levelModel> levelList = levelModel.dao.findAll();
		setAttr("levelList", levelList);

		render("/guanliyuan/jiansuowanglaishuju.html");
	}
	
	/**
	 * 检索往来数据
	 */
	public void historydata() {
		String begintime = getPara("begintime");
		String endtime = getPara("endtime");
		String pingtai = getPara("pingtai");
		String type = getPara("type");
		Record historydata = new Record();
		historydata.set("begintime", begintime);
		historydata.set("endtime", endtime);
		historydata.set("pingtai", pingtai);
		historydata.set("type", type);
		setSessionAttr("historydata", historydata);
		Page<Saomajilu> smList = Saomajilu.dao.gethistorydata(getParaToInt(0,1),begintime,
				endtime, pingtai, type);
		GetLevelInSaojilu(smList.getList());
		setAttr("smList", smList);
		/*
		 * 调用后台扫码的记录信息，这一记录是由用户扫码过程中生成的， 扫码记录生成过程是
		 * 用户扫码,判断被扫码的二维码的这个参数，生成记录的时候，找他的上家，判断应该给他的上家多少钱 然后再把信息写入到本方法对应的数据库中
		 * 回显示始数据页面
		 */
		render("/guanliyuan/doublejiansuo.html");
	}
	
	/**
	 * 检索往来数据
	 */
	public void historydataFenYe() {
		Record historydata =getSessionAttr("historydata");
		String begintime = historydata.getStr("begintime");
		String endtime = historydata.getStr("endtime");
		String pingtai = historydata.getStr("pingtai");
		String type = historydata.getStr("type");
		Page<Saomajilu> smList = Saomajilu.dao.gethistorydata(getParaToInt(0,1),begintime,
				endtime, pingtai, type);
		GetLevelInSaojilu(smList.getList());
		setAttr("smList", smList);
		/*
		 * 调用后台扫码的记录信息，这一记录是由用户扫码过程中生成的， 扫码记录生成过程是
		 * 用户扫码,判断被扫码的二维码的这个参数，生成记录的时候，找他的上家，判断应该给他的上家多少钱 然后再把信息写入到本方法对应的数据库中
		 * 回显示始数据页面
		 */
		render("/guanliyuan/doublejiansuo.html");
	}
	/**
	 * 通过名字、起始时间、平台名、代理商类型查询数据
	 */
	public void findbydlsnameandtype() {
		String name = getPara("user.name");
		String begintime = getPara("begintime");
		String endtime = getPara("endtime");
		String pingtai = getPara("pingtai");
		String level = getPara("user.levelid");
		Integer levelid = null;
		String msg="";
		if (!("").equals(name)) {
			userModel user =userModel.dao.findLUbyLoginname(name);
			if(user!=null){
				levelid = user.get("id");
			}else{
				 msg="nouser";
			}
		}
		Record jiansuo= new Record();
		jiansuo.set("begintime", begintime);
		jiansuo.set("endtime", endtime);
		jiansuo.set("pingtai", pingtai);
		jiansuo.set("levelid", levelid);
		jiansuo.set("level", level);
		jiansuo.set("name", name);
		setSessionAttr("jiansuo", jiansuo);
		setAttr("jiansuo", jiansuo);
		Integer pageNum=getParaToInt("pageNum");
		
		Page<Saomajilu> smList=null;
		if(pageNum!=null){
			 smList = Saomajilu.dao.findlistbydlsname(levelid,
					level, pageNum,begintime,endtime,pingtai);
			GetLevelInSaojilu(smList.getList());
		}else{
			 smList = Saomajilu.dao.findlistbydlsname(levelid,
					level, getParaToInt(0, 1),begintime,endtime,pingtai);
			GetLevelInSaojilu(smList.getList());
		}
		//如果输入的用户不存在，则不显示数据
		if(msg=="nouser"){
			smList=null;
		}
		setAttr("smList", smList);
		Page<levelModel> levelList = levelModel.dao.findAll();
		setAttr("levelList", levelList);
		Integer userSize =User.dao.gg();
//		Page<userModel> userList = userModel.dao.paginate(1, userSize,
//				"select * ", " from user");
		Page<User> userList = User.dao.jj(userSize);
		setAttr("userList", userList);
		// 查找所有公众平台
		Page<Gongzhonghao> gzhList = Gongzhonghao.dao.getgzhAll();
		setAttr("gzhList", gzhList);
		render("/guanliyuan/firstjiansuo.html");
	}
	
	
	/**
	 *通过名字、起始时间、平台名、代理商类型查询数据fenye
	 */
	public void findbydlsnameandtypeFenYe() {
		Record jiansuo = getSessionAttr("jiansuo");
		String level=jiansuo.getStr("level");
		Integer levelid=jiansuo.getInt("levelid");
		String begintime = jiansuo.getStr("begintime");
		String endtime = jiansuo.getStr("endtime");
		String pingtai = jiansuo.getStr("pingtai");
		setAttr("jiansuo", jiansuo);
		Page<Saomajilu> smList=null;
		Integer pageNum=getParaToInt("pageNum");
		if(pageNum!=null){
			 smList = Saomajilu.dao.findlistbydlsname(levelid,
					level, pageNum,begintime,endtime,pingtai);
			GetLevelInSaojilu(smList.getList());
		}else{
			 smList = Saomajilu.dao.findlistbydlsname(levelid,
					level, getParaToInt(0, 1),begintime,endtime,pingtai);
			GetLevelInSaojilu(smList.getList());
		}

		setAttr("smList", smList);
		Page<levelModel> levelList = levelModel.dao.findAll();
		setAttr("levelList", levelList);
		Integer userSize =User.dao.gg();
//		Page<userModel> userList = userModel.dao.paginate(1, userSize,
//				"select * ", " from user");
		Page<User> userList = User.dao.jj(userSize);
		setAttr("userList", userList);
		// 查找所有公众平台
		Page<Gongzhonghao> gzhList = Gongzhonghao.dao.getgzhAll();
		setAttr("gzhList", gzhList);
		render("/guanliyuan/firstjiansuo.html");
	}
	
	

	private void GetLevelInSaojilu(List<Saomajilu> sms) {
		List<User> ums=User.dao.kk();
		for (Saomajilu sm : sms) {
			Integer city = sm.getInt("citylevel");
			Integer country = sm.getInt("countrylevel");
			Integer person = sm.getInt("personlevel");
			for (User sum : ums) {
				Integer id = sum.getInt("id");
				String loginname = sum.get("loginname");
				String realname = sum.get("realname");
				if (city == id) {
					sm.set("cityL", loginname);
					sm.set("cityR", realname);
				} else if (country == id) {
					sm.set("countryL", loginname);
					sm.set("countryR", realname);
				} else if (person == id) {
					sm.set("personL", loginname);
					sm.set("personR", realname);
				}
			}
		}
	}

}
