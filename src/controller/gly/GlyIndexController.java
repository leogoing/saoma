package controller.gly;

import validator.AddDlsValidator;
import model.Tixianjilu;
import model.bankModel;
import model.levelModel;
import model.userModel;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import model.gly.User;
import com.utils.DateUtils;
/**
 * 管理员模块——添加信息
 * @author zmc
 *
 */
public class GlyIndexController extends Controller{
	/**
	 * 跳转到管理员首页
	 */
	public void index() {
		Page<levelModel> levelList=levelModel.dao.findAll();
		setAttr("levelList", levelList);
		Page<bankModel> bankList=bankModel.dao.findAll();
		setAttr("bankList", bankList);
		render("/guanliyuan/index.html");
	}
	
	/**
	 * 添加代理商
	 */
	@Before(AddDlsValidator.class)
		public void adddls() {
			System.out.println("---------------------------");
			userModel user=getModel(userModel.class, "user");
			String leader=user.get("leader");
			Integer leaderid;
			//需要将上级的loginname转为id再插入
			if(leader!=null){
				leaderid=userModel.dao.findByLoginname(leader);
				user.set("leader", leaderid);
			}else{
				user.set("leader", null);
			}
			
			try {
				boolean save=user.save();
//				Record txu=Db.findFirst("select * from user where loginname=?",user.get("loginname"));
				Record txu=User.dao.rr(user.get("loginname").toString());
				String swiftno=Tixianjilu.dao.finMaxSwiftno();
				Record tx=new Tixianjilu().set("loginname", txu.get("loginname")).set("levelid",txu.get("id")).set("reflecttime", DateUtils.getday("now"))
						.set("isreflect", 1).set("swiftno", swiftno).set("reflectmoney", 0).toRecord();
				Db.save("reflect", tx);
				
				JSONObject json=new JSONObject();
				if (save == true) {
					json.put("success", true);
					json.put("info", "保存代理商成功");
					final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
					renderJson(retObj);
				} else {
					json.put("success", false);
					json.put("info", "保存代理商失败");
					final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
					renderJson(retObj);
				}
			} catch (Exception e) {
				JSONObject json=new JSONObject();
				json.put("success", false);
				json.put("info", "保存代理商失败，请检查所填信息");
				final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
				renderJson(retObj);
				e.printStackTrace();
			}
			//index();
		}
	
	/**
	 * 获取上线
	 */
	public void getLeader(){
		String leadername=getPara("leadername");
		Integer leaderid;
		try {
			if(!leadername.equals("")){
				leaderid = userModel.dao.findByLoginname(leadername);
				renderJson("msg","该上级存在");
			}else{
				renderJson("msg","公司发展用户");
			}
		} catch (Exception e) {
			renderJson("msg","该上级不存在");
			e.printStackTrace();
		}
	}
}
