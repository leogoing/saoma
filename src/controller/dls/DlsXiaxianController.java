package controller.dls;

import java.sql.SQLException;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;

import model.upgradeModel;
import model.userModel;
/**
 * 代理商模块——发展下线
 * @author zmc
 *
 */
public class DlsXiaxianController extends Controller {

	/**
	 * 提交发展下线申请
	 */
	public void sqxiaxian() {

	}

	/**
	 * 列出自己下线 根据OPENID查询从属于自己的下线 即是查询上线为这个OPENID的记录
	 */
	public void myxiaxian() {
		String openid = getPara("openid");
		List<userModel> list = userModel.dao.getxiaxian(openid);
		setAttr("xxdls", list);
		// 显示下线代理商
	}

	/**
	 * 根据代理商级别对应跳转页面
	 */
	public void toDevelopLowerPage() {
		final int agentPage = getParaToInt("developPage");
		setAttr("currentUser", getSessionAttr("currentUser"));
		setAttr("currentLevelid", getSessionAttr("currentLevelid"));
		if (agentPage == 2) {// 跳转县级发展下级页面
			render("/xianjidailishang/xianjifazhanxiaxian.html");
		} else if (agentPage == 3) {// 跳转市级发展下级页面
			render("/shijidailishang/shijifazhanxiaxian.html");
		}
	}

	/**
	 * 发展下线
	 */
	public void developllower() {
		final int agentType = getParaToInt("developLowerType");
		final int leverId = getParaToInt("leverId");
		final String loginName = getPara("loginName");
		final String userName = getPara("userName");
		final String userPass = getPara("userPass");
		final String phone = getPara("phone");
		final String email = getPara("email");
		final String realname = getPara("realname");
		final String wantlevel = getPara("wantlevel");
		final String idcard = getPara("idcard");
		final String bankid = getPara("bankid");// 银行名称id
		final String bankinfo = getPara("bankinfo");// 所在地
		final String bankno = getPara("bankno");// 所在地
		
		if (agentType == 2) {
			doSaveInfo(loginName, userName, userPass, phone, email, realname,
					wantlevel,  idcard, bankid, bankinfo,bankno,leverId);
			setAttr("info", "保存成功！");
			render("/xianjidailishang/xianjifazhanxiaxian.html");
		} else if (agentType == 3) {
			doSaveInfo(loginName, userName, userPass, phone, email, realname,
					wantlevel,  idcard, bankid, bankinfo,bankno,leverId);
			setAttr("info", "保存成功！");
			render("/shijidailishang/shijifazhanxiaxian.html");
		}
	}

	/**
	 * 获取详情
	 */
	public void getUserAgentDetail() {
		final int userAgent = getParaToInt("useragent");
		renderJson(userModel.getAgentUserDetail(1, 6, userAgent));
	}
	private boolean doSaveInfo(String loginName, String userName,
			String userPass, String phone, String email, String realname,
			String wantlevel, String idcard, String bankid,
			String bankinfo,String bankno,int levelId) {
		final Record newRec = new Record();
		String swiftno=upgradeModel.dao.finMaxSwiftno();
		userModel loginuser=getSessionAttr("loginuser");
		newRec.set("loginname", loginName);
		newRec.set("password", userPass);
		newRec.set("phone", phone);
		newRec.set("email", email);
		newRec.set("realname", realname);
		newRec.set("wantlevel", wantlevel);
		newRec.set("idcard", idcard);
		newRec.set("bankid", bankid);
		newRec.set("bankno", bankno);
		newRec.set("bankinfo", bankinfo);
		newRec.set("ispass", 2);
		newRec.set("levelid", 1);
		newRec.set("leader", loginuser.get("id"));
		newRec.set("swiftno", swiftno);
		boolean saveStatus = Db.tx(new IAtom() {
			
			@Override
			public boolean run() throws SQLException {
				return Db.save("upgrade", newRec);
			}
		});
		return saveStatus;
	}
}
