package controller.gly;

import model.levelModel;
import model.userModel;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import model.gly.User;

/**
 * 管理员模块——账户管理
 * 
 * @author zmc
 *
 */
public class GlyDlslistController extends Controller {

	/**
	 * 打开列表页，首次打开
	 */
	public void todlslist() {
		Page<User> userList =User.dao.qq(getParaToInt(0, 1), 10);
		setAttr("userList", userList);

		Page<levelModel> levelList = levelModel.dao.findAll();
		setAttr("levelList", levelList);
		render("/guanliyuan/zhanghuguanli.html");
	}

	/**
	 * 根据条件进行查找
	 */
	public void findbydlsnameandtype() {
		String name = getPara("user.name");
		String levelid = getPara("user.levelid");
		System.out.println(name + "=====" + levelid);
		Page<userModel> userList = userModel.dao.findbydlsname(name, levelid,
				getParaToInt(0, 1));
		setAttr("userList", userList);
		Page<levelModel> levelList = levelModel.dao.findAll();
		setAttr("levelList", levelList);
		render("/guanliyuan/zhanghuguanli.html");
	}

	/**
	 * 点击详细按钮进行查看，并去修改页面
	 */
	public void gogetdlsinfobyid() {
		int id = getParaToInt("id");
		userModel dailishang = userModel.dao.findbydlsid(id);
		setAttr("dls", dailishang);
		render("/guanliyuan/gengxindailishang.html");
	}

}
