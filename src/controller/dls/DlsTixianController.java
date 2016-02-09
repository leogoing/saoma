package controller.dls;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import model.Tixianjilu;
import model.userModel;
import model.dls.DlsTixianControllerModel;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;

public class DlsTixianController extends Controller {

	/**
	 * 根据代理商级别对应跳转页面
	 */
	public void toWithDrawCashPage() {
		final int type = getParaToInt("type");
		setAttr("currentUser", getSessionAttr("currentUser"));
		setAttr("currentLevelid", getSessionAttr("currentLevelid"));
		setAttr("currentUserId", getSessionAttr("currentUserId"));

		setAttr("type", type);// 区分类型
		if (type == 2) {// 跳转县级提现页面
			render("/gerendailishang/shenqingtixian.html");
		} else if (type == 3) {// 跳转市级提现页面
			render("/gerendailishang/shenqingtixian.html");
		} else if (type == 1) {// 跳转个人级提现页面
			render("/gerendailishang/shenqingtixian.html");
		}
	}

	/**
	 * 申请提现
	 */
	public void getWithDrawCash() {
		try {
			String recieveVal = getPara("recieveVal");// 获取余额
			String userName = getPara("userName");
			int userId = getParaToInt("userId");
			int process = getParaToInt("process");
			// 查询上次提现记录
			Record tixianjilu = Db.findFirst(
					"select * from reflect where loginname='" + userName + "' order by reflecttime desc limit 1");
			if (tixianjilu != null) {//如果有提现记录,则判断提现状态
				Integer isreflect = tixianjilu.getInt("isreflect");
				if (isreflect == 1) {// 如果是已处理
					//处理提现
					doWithDrawCash(recieveVal, userId, userName, process);
					
					final JSONObject result = new JSONObject();
					result.put("msg", "申请提现成功");
					renderJson(result);
				} else {//没有处理则不能提现
					final JSONObject result = new JSONObject();
					result.put("msg", "您上次提现还未处理，暂时不能提现");
					renderJson(result);
				}
			} else {//没有提现记录，则直接提现
				//处理提现
				doWithDrawCash(recieveVal, userId, userName, process);

				final JSONObject result = new JSONObject();
				result.put("msg", "申请提现成功");
				renderJson(result);
			}
		} catch (Exception e) {
			final JSONObject result = new JSONObject();
			result.put("msg", "申请提现失败");
			renderJson(result);
			e.printStackTrace();
		}
	}

	/**
	 * 进行提现
	 * 
	 * @param recieveVal
	 * @param userId
	 * @param userName
	 * @param process
	 */
	private void doWithDrawCash(String recieveVal, int userId, String userName, int process) {
		String sql = "insert into reflect (loginname,reflecttime,reflectmoney,isreflect,handcharge) values(?,curdate(),?,'2',?)";
		final Record record = new Record();
		userModel loginuser = getSessionAttr("loginuser");
		String swiftno = Tixianjilu.dao.finMaxSwiftno();
		record.set("loginname", userName);
		record.set("reflectmoney", recieveVal);
		record.set("handcharge", process);
		record.set("reflecttime", new Date());
		record.set("isreflect", 2);
		record.set("swiftno", swiftno);
		record.set("username", loginuser.get("username"));
		record.set("phone", loginuser.get("phone"));
		record.set("bankno", loginuser.get("bankno"));
		record.set("bankinfo", loginuser.get("bankinfo"));
		record.set("levelid", loginuser.get("levelid"));
		System.out.println("这是提现用户等级" + loginuser.get("levelid"));

		Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				return Db.save("reflect", record);
			}
		});
	}

	/**
	 * 查询交易流程 //可能需要根据一系列条件进行查询
	 */
	public void tofindDealDetail() {
		// int type = getParaToInt("type");
		String userName = getPara("userName");
		renderJson(Tixianjilu.getDealDetail(1, 6, userName));
	}

	/**
	 * 获取余额
	 */
	public void getRemainderValue() {
		String userName = getPara("userName");
		int userId = getParaToInt("userId");
		float remainder = doRemainderValue(userName, userId);
		final JSONObject result = new JSONObject();
		String remainders = new DecimalFormat("##0.00").format(remainder);
		result.put("remainder", remainders);
		final JSONObject retObj = (JSONObject) JSONObject.toJSON(result);
		renderJson(retObj);
	}

	private float doRemainderValue(String userName, int userId) {

		List<Record> personal = Db.find(DlsTixianControllerModel.dao.personal(userName, userId).toString());
		float personals = changeVal(personal.get(0).get("person"));

		List<Record> city = Db.find(DlsTixianControllerModel.dao.city(userName, userId).toString());
		float citys = changeVal(city.get(0).get("city"));

		List<Record> country = Db.find(DlsTixianControllerModel.dao.country(userName, userId).toString());

		float countrys = changeVal(country.get(0).get("country"));
		float sum = countrys + personals + citys;
		return sum;
	}

	private static float changeVal(Object object) {
		if (object == null || object.equals(""))
			return 0;
		return Float.valueOf(object.toString());
	}
}
