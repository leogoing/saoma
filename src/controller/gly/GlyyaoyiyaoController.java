package controller.gly;

import java.util.List;

import com.jfinal.core.Controller;

import model.yaoyiyao.DlsyaoyiyaoModel;



/**
 * 添加摇一摇的所有操作
 * 
 * @author csc
 *
 */

public class GlyyaoyiyaoController extends Controller {
	public static final GlyyaoyiyaoController dao = new GlyyaoyiyaoController();

	/**
	 * 跳转到摇一摇界面
	 *
	 */
	public void sysb() {
		// 获得所有设备
		List<DlsyaoyiyaoModel> yylist = DlsyaoyiyaoModel.dao.suoyoushebei();
		setAttr("yylist", yylist);
		render("/guanliyuan/addyaoyiyao.html");
	}

	/**
	 * 管理员——》添加摇一摇事件——》查询出所有设备——》点击修改
	 */
	public void yyyxiugai() {
		// 获得ID
		String id = getPara("yy.id");
		// 查询ID
		List<DlsyaoyiyaoModel> listbj = DlsyaoyiyaoModel.dao.yyybianji(id);
		setAttr("listbj", listbj);
		render("/guanliyuan/setYaoYiYao.html");
	}

	/**
	 * 管理员——》添加摇一摇事件——》查询出所有设备——》点击修改——》修改完内容后——》提交
	 * /saoma/WebRoot/guanliyuan/setYaoYiYao.html 注：DLSid
	 * 不可以被直接修改，需要查询查询用户表（user）后，获得的DLSID（user表中主键ID）存入数据库（dlsyaoyiyao）
	 */
	public void tijiao() {
		// 获得参数
		String id = getPara("id");// 接受传入的参数ID
		String uuid = getPara("uuid");// 接收传入的参数uuid
		String major = getPara("major");// 接收参入的参数major
		String minor = getPara("minor");// 接收传入的参数minor
		String loginname = getPara("loginname");// 接收传入的参数代理商账号

		DlsyaoyiyaoModel.dao.yyytijiao(id, uuid, major, minor, loginname);

		//跳转
		sysb();
	}

	/**
	 * 管理员——》添加摇一摇事件——》添加按钮
	 */
	public void yyytianjia() {
		// 获得参数
		String uuid = getPara("uuid");// 接收传入的参数uuid
		String major = getPara("major");// 接收参入的参数major
		String minor = getPara("minor");// 接收传入的参数minor
		String loginname = getPara("loginname");// 接收传入的参数代理商账号
		DlsyaoyiyaoModel.dao.yyyshebei(uuid, major, minor, loginname);
		//跳转
		sysb();
	}
}
