package controller.gly;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import model.gly.Level;
import model.gly.User;
import model.GuanggaozhuModel;
import model.bankModel;
import model.levelModel;
import model.userModel;

/**
 * 管理员模块——修改账户信息
 * 
 * @author zmc
 *
 */
public class GlyDlsUpdateController1 extends Controller {

	/**账户管理——》操作（编辑）——》进入这个页面
	 * 进入页面的时候，拉出代理商的详细数据
	 */
	public void findinfobydlsid() {
		Integer id = getParaToInt("user.id");
		// 代理商的所有信息
		Page user = userModel.dao.hisjilu(id);
		setAttr("user", user);
		// 代理商上线信息
		Page leader = userModel.dao.findByedgeId(id);
		setAttr("leader", leader);
		// 代理商下线信息
		Page<userModel> edgeList = userModel.dao.findedge(getParaToInt(0,1),id);
		setAttr("edgelist", edgeList);

		Page<levelModel> levelList = levelModel.dao.findAll();
		setAttr("levelList", levelList);
		Page<bankModel> bankList = bankModel.dao.findAll();
		setAttr("bankList", bankList);
		
		/**获得 广告主的余额
		*调用model获得所有平台台一段时间内的花费
		*彩笔代码，面向过程了
		*/
		Record cost = GuanggaozhuModel.dao.GetAllTotalCost(id);
		// 将充值总金额减去总花费得到余额
		Double allcost = cost.getDouble("all");
		
		
		// 调用model获得广告主的所有充值金额
		Record money = GuanggaozhuModel.dao.GetMoenyById(id);
		Double totalMoney = 0.0;
		try {
			totalMoney = money.getDouble("totalMoney");
		} catch (Exception e) {
			
		}
		if (totalMoney ==null) {
			totalMoney = 0.0;
		}
		//获得的金额相减
		Double balance = totalMoney - allcost;
		//返回获得的金额
		setAttr("balance", balance);
		render("/guanliyuan/gengxindailishang.html");
	}

	/**
	 * 进入更改代理商上线页面
	 */
	public void updateleader() {
		Integer id = getParaToInt("user.id");
		// 代理商的所有信息
		Page user = userModel.dao.hisjilu(id);
		setAttr("user", user);

		render("/guanliyuan/updateleader.html");
	}

	/**
	 * 做更改代理商上线操作
	 */
	public void updateleader1() {
		String loginname = getPara("loginname");
		String updateleader = getPara("updateleader");
		JSONObject json = new JSONObject();
		try {
			userModel user = userModel.dao.findLUbyLoginname(loginname);
			Integer leaderid = userModel.dao.findByLoginname(updateleader);
			user.set("leader", leaderid);
			boolean update = user.update();
			if (update == true) {
				json.put("info", "更新成功");
				final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
				renderJson(retObj);
			} else {
				json.put("info", "更新失败");
				final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
				renderJson(retObj);
			}
		} catch (Exception e) {
			json.put("info", "更新失败");
			final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
			renderJson(retObj);
			e.printStackTrace();
		}
	}

	/**
	 * 更新代理商信息 判断所选代理商是什么类型 1、如果类型和原类型相同，只做数据更改
	 * 2、如果类型和原类型不同，则进行升级操作，升级也只做数据更改，以后按照新的类型，结合二级码给定价格，进行给反钱
	 * 3、如果所选类型为退费，则执行退费流程 4、退费说明：只做退费标记
	 * 
	 */
	public void updatedls() {
		userModel user = getModel(userModel.class, "u");
		Integer Id = user.getInt("id");
		// 对冻结和退费进行操作
		if (user.get("levelid").equals(7)) {
			user.set("levelid", 7);
			userModel sx = userModel.dao.findSXbyId(Id);
			Integer sxid = sx.get("id");
			List<userModel> xxList = userModel.dao.findXXById(Id);
			for (userModel xx : xxList) {// 将下线的leader改为sxid
				xx.set("leader", sxid);
				xx.update();
			}
		}
		if (user.get("levelid").equals(0)) {
			user.set("levelid", 1);
		}

		boolean update = user.update();
		JSONObject json = new JSONObject();
		if (update == true) {
			json.put("info", "更新成功");
			final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
			renderJson(retObj);
		} else {
			json.put("info", "更新失败");
			final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
			renderJson(retObj);
		}
		Page<User> userList =User.dao.ww(getParaToInt(0, 1));
		setAttr("userList", userList);

		Page<levelModel> levelList = levelModel.dao.findAll();
		setAttr("levelList", levelList);
		// render("/guanliyuan/zhanghuguanli.html");
		// render("${ctx}/glydlslist/todlslist");//转到之前页面需要提供list，现转到index。html保证页面流转
	}

	/**
	 * 进入修改财务信息
	 */
	public void updatecaiwu() {
		Page<Level> caiwu = Level.dao.aa();
//		setAttr("caiwu", caiwu);
		render("/guanliyuan/updatecaiwu.html");
	}

	/**
	 * 做修改财务信息操作
	 */
	public void updatecaiwu1() {
		String updatetel = getPara("tel");
		String updatename = getPara("name");
		JSONObject json = new JSONObject();
		try {
			levelModel caiwu = levelModel.dao.findById(5);
			caiwu.set("tel", updatetel).set("name", updatename);
			boolean update = caiwu.update();
			if (update == true) {
				json.put("info", "更新成功");
				final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
				renderJson(retObj);
			} else {
				json.put("info", "更新失败");
				final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
				renderJson(retObj);
			}
		} catch (Exception e) {
			json.put("info", "更新失败");
			final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
			renderJson(retObj);
			e.printStackTrace();
		}
	}

}
