package controller.dls;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.upgradeModel;
import model.userModel;
import model.dls.DlsShengjiControllerModel;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 代理商模块——代理商升级
 * 
 * @author zhaole
 *
 */
public class DlsShengjiController extends Controller {

	/**
	 * 进行升级申请操作
	 */
	public void sqshengji() {
		getModel(upgradeModel.class, "sj").save();
	}

	/**
	 * 根据代理商级别跳转页面
	 */
	public void toUpLevelPage() {
		setAttr("currentUser", getSessionAttr("currentUser"));
		setAttr("currentLevelid", getSessionAttr("currentLevelid"));
		final int agentPage = getParaToInt("agentPage");
		if (agentPage == 1) {// 跳转个人代理升级
			render("/gerendailishang/gerenshengji.html");
		} else if (agentPage == 2) {// 跳转县级代理升级
			render("/xianjidailishang/xianjishengji.html");
		}

	}

	/**
	 * 代理升级
	 */
	public void agenetUpLevel() {
		final int agentType = getParaToInt("agentType");
		String selectedLevel = getPara("selectedLevel");
		String loginUser = getPara("userName");
		int leverId = getParaToInt("leverId");
		agenetUpLevelforPersonal(loginUser, leverId, selectedLevel);
		
		 /*if(agentType==1){ render("/gerendailishang/gerenshengji.html"); }else
		 if(agentType==2){ render("/xianjidailishang/xianjishengji.html"); }*/
		 
	}

	/**
	 * 个人代理升级
	 * @param loginUser
	 * @param leverId
	 * @param selectedLevel
	 */
	public void agenetUpLevelforPersonal(String loginUser, int leverId,
			String selectedLevel) {
		// String agentName =getAttr("userName");(用户信息可能为全局变量)
/*		final String sql = "select dredge from level s where s.`id` ='"
				+ leverId + "'";
		final String develsql = "select dredge from level s where s.`id` ='"
				+ selectedLevel + "'";
		final String telsql = "select tel,name from level where id=5";
		
		
		List<Record> currentVal = Db.find(sql);
		List<Record> develVal = Db.find(develsql);
		String cwTel = Db.find(telsql).get(0).getStr("tel");
		String cwName = Db.find(telsql).get(0).getStr("name");*/
		
		List<Record> currentVal = Db.find(DlsShengjiControllerModel.dao.agenetUpLevelforPersonalsql(leverId));
		List<Record> develVal = Db.find(DlsShengjiControllerModel.dao.agenetUpLevelforPersonaldevelsql(selectedLevel));
		String cwTel = Db.find(DlsShengjiControllerModel.dao.agenetUpLevelforPersonaltelsql()).get(0).getStr("tel");
		String cwName = Db.find(DlsShengjiControllerModel.dao.agenetUpLevelforPersonaltelsql()).get(0).getStr("name");
		
		
		int result = Integer.valueOf(develVal.get(0).get("dredge").toString())
				- Integer.valueOf(currentVal.get(0).get("dredge").toString());
		// 存入库表agentapply
		final JSONObject response = new JSONObject();

		if (doSaveAagentApply(loginUser, leverId, selectedLevel, result)) {
			response.put("money", result);
			response.put("tel", cwTel);
			response.put("name", cwName);
			response.put("result", "提交成功");
			final JSONObject retObj = (JSONObject) JSONObject.toJSON(response);
			renderJson(retObj);
		} else {
			response.put("result", "提交失败");
			final JSONObject retObj = (JSONObject) JSONObject.toJSON(response);
			renderJson(retObj);
		}
	}

	/**
	 * 存库操作
	 * @param loginUser
	 * @param applyLevel
	 * @param agentLevel
	 * @param result
	 * @return
	 */
	private boolean doSaveAagentApply(final String loginUser,
			final int applyLevel, final String agentLevel, int result) {
		Date date =new Date();
		DateFormat f= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time=f.format(date);
		Record newRec = new Record();
		// 生成流水号
		String swiftno = upgradeModel.dao.finMaxSwiftno();
		userModel loginuser = getSessionAttr("loginuser");// 对数据进行填充并保存
		newRec.set("loginname", loginUser)
				.set("password", loginuser.get("password"))
				.set("realname", loginuser.get("realname"));
		newRec.set("wantlevel", agentLevel)
				.set("email", loginuser.get("email"))
				.set("phone", loginuser.get("phone"))
				.set("idcard", loginuser.get("idcard"));
		newRec.set("levelid", applyLevel)
				.set("bankid", loginuser.get("bankid"))
				.set("bankno", loginuser.get("bankno"))
				.set("bankinfo", loginuser.get("bankinfo"));
		newRec.set("ispass", 2).set("leader", loginuser.get("leader"));
		newRec.set("money", result).set("swiftno", swiftno);
		newRec.set("applytime",time);
		boolean saveStatus = Db.save("upgrade", newRec);
		return saveStatus;
	}

}
