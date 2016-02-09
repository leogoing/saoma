package controller.gly;

import java.util.List;

import model.Gongzhonghao;
import model.Saomajilu;
import model.callconfigModel;
import model.userModel;
import Static.Static;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import model.gly.Smrecord;
import model.gly.User;
import com.utils.TimeMax;
/**
 * 管理员模块——报警
 * @author zmc
 *
 */
public class GlyBjController extends Controller {
	
	/**
	 * 跳转到报警页面
	 */
	public void index(){
		Page<callconfigModel> bjList=callconfigModel.dao.findConfig();
		setAttr("bjList", bjList);
//		Page<Saomajilu> maxSm=TimeMax.getMax("10");
//		setAttr("maxSm", maxSm);
		render("/guanliyuan/baojing.html");
	}

	/**
	 * 报警 1、选择时间段 
	 * 	   2、看该时间段单个用户扫描数据超过五六千的记录 
	 * 	   3、给相关邮箱发信息，手动去查看。
	 */
	public void savebaojing() {
		callconfigModel baojing=getModel(callconfigModel.class, "callconfig");
		if(baojing.get("timespace")!=null&&baojing.get("smcount")!=null&&baojing.get("callemail")!=null){
			callconfigModel oldcall=callconfigModel.dao.findById(1);
			oldcall.setAttrs(baojing);
			boolean save = oldcall.update();
			JSONObject json=new JSONObject();
			if (save == true) {
				json.put("success", true);
				json.put("info", "保存成功");
				final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
				renderJson(retObj);
			} else {
				json.put("success", false);
				json.put("info", "保存失败");
				final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
				renderJson(retObj);
			}
			//index();
		}else{
			JSONObject json=new JSONObject();
			json.put("success", false);
			json.put("info", "保存失败，请检查参数");
			final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
			renderJson(retObj);
			//index();
		}
	}

	/**
	 * 查询扫码记录
	 * 
	 * @param begintime
	 * @param endtime
	 * @param pingtai
	 * @param user
	 */
	public void findsaomajilun() {

		String begintime = getPara("begintime");
		String endtime = getPara("endtime");
		String publicaccount = getPara("publicaccount");
		String loginname = getPara("loginname");
//		if(loginname.equals("")){
//			setAttr("info","扫描账户为登陆名称，不能为空");
//			selectall();
//			return;
//		}
		Record findsaomajilun= new Record();
		findsaomajilun.set("begintime", begintime);
		findsaomajilun.set("endtime", endtime);
		findsaomajilun.set("publicaccount", publicaccount);
		findsaomajilun.set("loginname", loginname);
		
		setSessionAttr("findsaomajilun", findsaomajilun);
		Integer id =null;
		String msg="";
		//输入的用户名不为空字符串则查找ID
		if (!("").equals(loginname)) {
			id=userModel.dao.findByLoginname(loginname);
			if(id==null){//如果查找结果为空
				 msg="nouser";
			}
		}
		
		Page<Saomajilu> smList = Saomajilu.dao.getinfo(begintime, endtime,
				publicaccount, id, getParaToInt(0, 1), 100);
		GetLevelInSaojilu1(smList.getList());
		if(msg=="nouser"){
			smList=null;
		}
		setAttr("smList", smList);
		Page<Gongzhonghao> gzhList=Gongzhonghao.dao.getgzhAll();
		setAttr("gzhList", gzhList);
		setAttr("findsaomajilun", findsaomajilun);
		render("/guanliyuan/chazhaoquanbushujusearch.html");
		
	}
	
	/**
	 * 查询扫码记录
	 * 
	 * @param begintime
	 * @param endtime
	 * @param pingtai
	 * @param user
	 */
	public void findsaomajilunFenYe() {
		Record findsaomajilun=getSessionAttr("findsaomajilun");
		setAttr("findsaomajilun", findsaomajilun);
		String begintime = findsaomajilun.getStr("begintime");
		String endtime =findsaomajilun.getStr("endtime");
		String publicaccount = findsaomajilun.getStr("publicaccount");
		String loginname = findsaomajilun.getStr("loginname");
		Integer id =null;
		String msg="";
		//输入的用户名不为空字符串则查找ID
		if (!("").equals(loginname)) {
			id=userModel.dao.findByLoginname(loginname);
			if(id==null){//如果查找结果为空
				 msg="nouser";
			}
		}
		Integer pageNum=getParaToInt("pageNum");
		Page<Saomajilu> smList=null;
		if(pageNum!=null){
			smList = Saomajilu.dao.getinfo(begintime, endtime,
					publicaccount, id, pageNum, 100);
		}else{
			smList = Saomajilu.dao.getinfo(begintime, endtime,
					publicaccount, id, getParaToInt(0, 1), 100);
		}
		GetLevelInSaojilu1(smList.getList());
		if(msg=="nouser"){
			smList=null;
		}
		setAttr("smList", smList);
		Page<Gongzhonghao> gzhList=Gongzhonghao.dao.getgzhAll();
		setAttr("gzhList", gzhList);
		render("/guanliyuan/chazhaoquanbushujusearch.html");
		
	}
	
	
	/**
	 * 获取最大峰值记录
	 */
	public void findsaomajilunmax() {
		Page<callconfigModel> bjList=callconfigModel.dao.findConfig();
		setAttr("bjList", bjList);
//		String time = getPara("begintime");
//		if(time.equals("")){
//			setAttr("bjinfo", "查询区间有误，请重新输入");
//		}else{
//			Page<Saomajilu> maxSm=TimeMax.getMax(time);
//			if(maxSm==null){
//				setAttr("bjinfo", "查询区间有误，请重新输入");
//			}else{
//				setAttr("maxSm", maxSm);
//			}
//		}
		render("/guanliyuan/baojing.html");
	}

	/**
	 * 设置符合条件的记录为无效
	 */
	public void setnouse() {
		String begintime = getPara("begintime");
		String endtime = getPara("endtime");
		String pingtai = getPara("pingtai");
		String user = getPara("user");
		Saomajilu.dao.setnouse(begintime, endtime, pingtai, user);
	}

	/**
	 * 设置选中的记录为无效
	 */

	public void setall() {
		String[] values = getParaValues("wxid");
		JSONObject json=new JSONObject();
		if(values!=null){
			Saomajilu.dao.setallnouser(values);
			json.put("info", "操作成功");
			final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
			renderJson(retObj);
		}else{
			json.put("info", "请选择要置为无效的数据");
			final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
			renderJson(retObj);
		}
		//selectall();
	}
	
	/**
	 * 获取全部数据
	 */
	public void selectall(){
		Integer pageNum=getParaToInt("pageNum");
		Page<Smrecord> smList=null;
		if(pageNum!=null){
			 smList =Smrecord.dao.dd(pageNum, 100);
		}else{
			smList =Smrecord.dao.dd(getParaToInt(0, 1), 100);
		}
		GetLevelInSaojilu(smList.getList());
		setAttr("smList", smList);
		Page<Gongzhonghao> gzhList=Gongzhonghao.dao.getgzhAll();
		setAttr("gzhList", gzhList);
		render("/guanliyuan/chazhaoquanbushuju.html");
	}
	
	
	private void GetLevelInSaojilu(List<Smrecord> sms){
		List<User> ums = User.dao.ff();
		for (Smrecord sm : sms) {
			Integer city = sm.getInt("citylevel");
			Integer country = sm.getInt("countrylevel");
			Integer person = sm.getInt("personlevel");
			for (User sum : ums) {
				Integer id = sum.getInt("id");
				String loginname = sum.get("loginname");
				String realname = sum.get("realname");
				if(city==id){
					sm.set("cityL", loginname);
					sm.set("cityR", realname);
				}else if(country==id){
					sm.set("countryL", loginname);
					sm.set("countryR", realname);
				}else if(person==id){
					sm.set("personL", loginname);
					sm.set("personR", realname);
				}
			}
		}
	}
	private void GetLevelInSaojilu1(List<Saomajilu> sms){
		List<User> ums = User.dao.ff();
		for (Saomajilu sm : sms) {
			Integer city = sm.getInt("citylevel");
			Integer country = sm.getInt("countrylevel");
			Integer person = sm.getInt("personlevel");
			for (User sum : ums) {
				Integer id = sum.getInt("id");
				String loginname = sum.get("loginname");
				String realname = sum.get("realname");
				if(city==id){
					sm.set("cityL", loginname);
					sm.set("cityR", realname);
				}else if(country==id){
					sm.set("countryL", loginname);
					sm.set("countryR", realname);
				}else if(person==id){
					sm.set("personL", loginname);
					sm.set("personR", realname);
				}
			}
		}
	}

}
