package controller.gr;

import model.bankModel;
import model.userModel;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

import model.bankModel;
import model.userModel;

/**
 * 个人信息
 * 
 * @author zmc
 *
 */
public class GrZlUpdateController extends Controller {

	/**
	 * 进入页面的时候，获取个人资料
	 */
	public void grsy() {
		userModel loginuser = getSessionAttr("loginuser");
		Integer id = loginuser.getInt("id");
		Page<userModel> grzl = userModel.dao.hisjilu(id);
		System.out.println(grzl.toString());
		setAttr("grzl", grzl);
		render("/gerenziliao/gerenziliao.html");
	}

	/**
	 * 完善个人资料进入页面
	 */
	public void wszl() {
		userModel loginuser = getSessionAttr("loginuser");
		Integer id = loginuser.getInt("id");
		Page<userModel> grzlup = userModel.dao.hisjilu(id);
		setAttr("grzlup", grzlup);
		Page<bankModel> bankList = bankModel.dao.paginate(1, 10, "select * ",
				"from bank");
		setAttr("bankList", bankList);

		render("/gerenziliao/wanshangerenziliao.html");
	}

	/**
	 * 更新个人资料
	 */

	public void updategr() {

		userModel user = getModel(userModel.class, "user");
		userModel loginuser = getSessionAttr("loginuser");
		loginuser.setAttrs(user);
		boolean update = loginuser.update();
		JSONObject json = new JSONObject();

		if (update == true) {
			json.put("info", "更新成功");
			final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
			renderJson(retObj);
			// render("/Public/login.html");
		} else {
			json.put("info", "更新失败");
			final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
			renderJson(retObj);
			// render("/gerenziliao/wanshangerenziliao.html");
		}

	}

}
