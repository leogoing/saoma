package controller.liwang;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.utils.DateUtils;

import model.userModel;
import model.dls.DlsInfoDetailModel;

/**
 * 代理商模块——代理商信息
 * 
 * @author zmc
 *
 */
public class DlsInfoDetail extends Controller {

	/**
	 * 根据代理商级别对应跳转页面
	 */
	@Clear
	public void toAgentDetailPage() {
		final int agentType = getParaToInt("agentType");
		setAttr("agentType", agentType);// 区分类型
		setAttr("currentUser", getSessionAttr("currentUser"));
		setAttr("currentLevelid", getSessionAttr("currentLevelid"));
		setAttr("currentUserId", getSessionAttr("currentUserId"));
		setAttr("currentUserName", getSessionAttr("currentUserName"));

		if (agentType == 2) {// 跳转县级代理商信息页面
			render("/xianjidailishang/xianjidailishang.html");
		} else if (agentType == 3) {// 跳转市级代理商信息页面
			render("/shijidailishang/shijidailishang.html");
		} else if (agentType == 1) {// 跳转个人代理商信息页面
			render("/gerendailishang/gerendailishang.html");
		}
	}

	/**
	 * 根据平台查找不同的值
	 */
	@Clear
	public void getValueFromPlant() {
		int userId = getParaToInt("userId");//获得ID

		// 昨日收入
		String now = DateUtils.getday("today");//定义时间
		//调用DlsInfoDetailModel类的QueryIncome方法
		List<Record> listR = Db.find(DlsInfoDetailModel.dao.getValueFromPlantQueryIncome(userId, now).toString());
		
		List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (Record re : listR) {
			map = new HashMap<String, Object>();
			// 获取当前用户所在的level等级
			Object countrylevel = re.get("countrylevel");
			Object personlevel = re.get("personlevel");
			Object citylevel = re.get("citylevel");
			String getInfo = getUserLevel(userId, personlevel, countrylevel,
					citylevel);
			System.out.println(getInfo+"<<<");
			if (getInfo != null) {
				// 根据getInfo 得到扫码值
				map.put("attentionPL", re.get("attentionPL"));//attentionPL所关注的平台
				map.put("value", re.get(getInfo));
				datalist.add(map);
			}
		}
//		System.out.println(sbuf.toString());
		// 对datalist 判断。 同平台相加

		List<Map<String, Object>> yestarlist = getValueFromDifferentPlant(datalist);

		// 本月收入
		//调用DlsInfoDetailModel类的AprilIncome方法
		List<Record> listy = Db.find(DlsInfoDetailModel.dao.getValueFromPlantAprilIncome(userId).toString());
		
		datalist = new ArrayList<Map<String, Object>>();
		for (Record re : listy) {
			map = new HashMap<String, Object>();
			// 获取当前用户所在的level等级
			Object countrylevel = re.get("countrylevel");
			Object personlevel = re.get("personlevel");
			Object citylevel = re.get("citylevel");
			String getInfo = getUserLevel(userId, personlevel, countrylevel,
					citylevel);
			// 根据getInfo 得到扫码值
			if (getInfo != null) {
				map.put("attentionPL", re.get("attentionPL").toString());
				map.put("value", re.get(getInfo));
				datalist.add(map);
			}
		}
		// 对datalist 判断。 同平台相加
		List<Map<String, Object>> monthlist = getValueFromDifferentPlant(datalist);
		// 昨日平台和本月 数据 拼接
		List<Map<Object, Object>> data = getUnderUserData(monthlist, yestarlist);
		renderHtml(jointTable(data));
	}

	/**
	 * 获取下线扫描
	 */
	@Clear
	public void getFindUnderScanFromDifferentPlant() {
		int userId = getParaToInt("userId");
		//StringBuffer sbuf = new StringBuffer();
		String now = DateUtils.getday("today");

		//调用DlsInfoDetailModel类的getFindUnderScanFromDifferentPlantQueryIncome方法//昨日收入
		List<Record> list = Db.find(DlsInfoDetailModel.dao.getFindUnderScanFromDifferentPlantQueryIncome(userId, now).toString());
		List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		for (Record re : list) {
			map = new HashMap<String, Object>();
			// 获取当前用户所在的level等级
			Object countrylevel = re.get("countrylevel");
			Object personlevel = re.get("personlevel");
			Object citylevel = re.get("citylevel");
			String getInfo = getUserLevel(userId, personlevel, countrylevel,
					citylevel);
			if (getInfo != null) {
				// 根据getInfo 得到扫码值
				map.put("attentionPL", re.get("attentionPL"));
				map.put("value", re.get(getInfo));
				datalist.add(map);
			}
		}
		// 对datalist 判断。 同平台相加
		List<Map<String, Object>> yestarlist = getValueFromDifferentPlant(datalist);
		
		//调用DlsInfoDetailModel类的getFindUnderScanFromDifferentPlantAprilIncome方法//本月收入
		list = Db.find(DlsInfoDetailModel.dao.getFindUnderScanFromDifferentPlantAprilIncome(userId).toString());
		
		datalist = new ArrayList<Map<String, Object>>();
		for (Record re : list) {
			map = new HashMap<String, Object>();
			// 获取当前用户所在的level等级
			Object countrylevel = re.get("countrylevel");
			Object personlevel = re.get("personlevel");
			Object citylevel = re.get("citylevel");
			String getInfo = getUserLevel(userId, personlevel, countrylevel,
					citylevel);
			// 根据getInfo 得到扫码值
			if (getInfo != null) {
				map.put("attentionPL", re.get("attentionPL").toString());
				map.put("value", re.get(getInfo));
				datalist.add(map);
			}
		}
		// 对datalist 判断。 同平台相加
		List<Map<String, Object>> monthlist = getValueFromDifferentPlant(datalist);
		// 昨日平台和本月 数据 拼接
		List<Map<Object, Object>> data = getUnderUserData(monthlist, yestarlist);
		renderHtml(jointTable(data));

	}

	/**
	 * 昨日平台和本月 数据 拼接
	 * 
	 * @param monthlist
	 * @param yestarlist
	 * @return
	 */
	@Clear
	private List<Map<Object, Object>> getUnderUserData(
			List<Map<String, Object>> monthlist,
			List<Map<String, Object>> yestarlist) {
		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
		DecimalFormat format = new DecimalFormat("0.00");

		Map<Object, Object> map = null;
		for (Map<String, Object> month : monthlist) {
			map = new HashMap<Object, Object>();
			Object mset = month.keySet().toArray()[0];
			map.put("pt", mset);
			if (yestarlist.size() > 0) {
				for (Map<String, Object> yestar : yestarlist) {
					Object yset = yestar.keySet().toArray()[0];
					if (mset.equals(yset)) {
						map.put("mounth", format.format(month.get(mset)));
						map.put("yestar", format.format(yestar.get(yset)));
					} else {
						// map.put("mounth", "0");
						// map.put("yestar", "0");
					}
				}
			} else {
				map.put("mounth", format.format(month.get(mset)));
				map.put("yestar", "0");
			}
			list.add(map);
		}
		return list;

	}

	/**
	 * 获取平台对应的收入（下线）
	 * 
	 * @param list
	 * @return
	 */
	@Clear
	private List<Map<String, Object>> getValueFromDifferentPlant(
			List<Map<String, Object>> list) {
		List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
		Map<String, Object> tmp_ = new HashMap<String, Object>();
		float sum = 0;
		if (list.size() > 0) {
			// 获取list中第一个值
			String pt = list.get(0).get("attentionPL").toString();
			for (int i = 0; i < list.size(); i++) {

				String _pt = list.get(i).get("attentionPL").toString();
				if (pt.equals(_pt)) {
					sum += Float.valueOf(list.get(i).get("value").toString());
				} else {
					//判断tmp_的key在当前的datalist中的map是否存在，存在的话value值相加后进入datalist，否则直接添加到datalist
					datalist.add(tmp_);
					tmp_ = new HashMap<String, Object>();
					pt = _pt;
					sum = Float.valueOf(list.get(i).get("value").toString());
					// sum=0;
				}
				tmp_.put(pt, sum);
			}
			datalist.add(tmp_);
		}
		return datalist;

	}

	/**
	 * 拼接表格（）_自身扫描、下线扫描
	 * 
	 * @param list
	 * @return
	 */
	/**
	 * <li>
					<div style="color: #FFFFFF; float: left; width: 45%; ">
						<span style="float: left;">
							平台
						</span>
						<span class="pingt">
							<span style="color: #172E7B; margin-right: 40px;">
								测试平台测试
							</span>
						</span>
					</div>
					<div style=" color: #FFFFFF;float: left; width: 25%; ">
						<span style="float: left; margin-left: 40px;">
							本月收入：
						</span>
						<span class="pingt">
							<span style="color: #172E7B; margin-right: 40px;">
								2633元
							</span>
						</span>
					</div>
					<div style="color: #FFFFFF; float: left; width: 25%; ">
						<span style="float: left; margin-left: 40px;">
							本月核算：
						</span>
						<span style="float:right;color: #172E7B; ">
								2633元
						</span>
					</div>
				</li>
	 * @param list
	 * @return
	 */
	private String jointTable(List<Map<Object, Object>> list) {
		StringBuffer sbuf = new StringBuffer();
//		sbuf.append("<tr class=\"text-center\"><td>平台</td><td>昨日收入</td><td>本月核算</td></tr>");
		if (list.size() > 0) {
			for (Map<Object, Object> map : list) {
//				sbuf.append("<tr class=\"text-center\"><td>" + map.get("pt")
//						+ "</td><td>" + isNull(map.get("yestar")) + "</td><td>"
//						+ isNull(map.get("mounth")) + "</td></tr>");
				sbuf.append("<li>"+
				"<div style='color: #FFFFFF; float: left; width: 45%; '>"+
					"<span style='float: left;'>"+
						"平台"+
					"</span>"+
					"<span class='pingt'>"+
						"<span style='color: #172E7B; margin-right: 40px;'>"+
						 map.get("pt")+
						"</span>"+
					"</span>"+
				"</div>"+
				"<div style=' color: #FFFFFF;float: left; width: 25%; '>"+
					"<span style='float: left; margin-left: 40px;'>"+
						"昨日收入："+
					"</span>"+
					"<span class='pingt'>"+
						"<span style='color: #172E7B; margin-right: 40px;'>"+
						isNull(map.get("yestar"))+
						"</span>"+
					"</span>"+
				"</div>"+
				"<div style='color: #FFFFFF; float: left; width: 25%; '>"+
					"<span style='float: left; margin-left: 40px;'>"+
						"本月核算："+
					"</span>"+
					"<span style='float:right;color: #172E7B; '>"+
					isNull(map.get("mounth"))+
					"</span>"+
				"</div>"+
			"</li>");
			}
		}
		return sbuf.toString();//将上面的所有HTML代码以String的方式传到页面上
	}

	/**
	 * 拼接流程记录的表格（）_所有
	 * 
	 * @param list
	 * @return
	 */
	private String jointScanTable(List<Map<String, String>> list) {
		StringBuffer sbuf = new StringBuffer();
//		sbuf.append("<tr class=\"text-center\"><td>平台</td><td>扫码时间</td><td>取关时间</td><td>扫码昵称</td><td>收入</td></tr>");
		if (list.size() > 0) {
			for (Map<String, String> map : list) {
//				sbuf.append("<tr class=\"text-center\"><td>"
//						+ map.get("attentionPL") + "</td><td>"
//						+ isNull(map.get("smtime")) + "</td><td>"
//						+ isNull(map.get("cancletime")) + "</td><td>"
//						+ map.get("attpeople") + "</td><td>" + map.get("value")
//						+ "</td><tr>"
//
//				);
//				System.out.println("    名字： "+map.get("attpeople")+"\n钱数： "+map.get("value"));
				if(map.get("value").equals("0.0")){
					continue;
				}
				sbuf.append("<li>"+
						"<div style='text-align:left; width:34%;'>"+
							"<img src='"+map.get("headimgurl")+"' class='img-circle' style='float:left; margin-top:13px; margin-left:30px; width:35px;'>"+							
							"<span style='font-size: 17px; margin-left: 30px;  width:70%; white-space:nowrap;overflow:hidden; text-overflow:ellipsis; float:left;'>"+
							map.get("attpeople")+
							"</span>"+
						"</div>"+
						"<div style=' width:12%; '>"+	
							"<span style='color: red; font-size: 17px;'>"+
							map.get("value")+
							"</span>"+
							"<span style='color: red; font-size: 17px;'>"+
								"元"+
							"</span>"+
						"</div>"+
						"<div style=' width:26%; white-space:nowrap; text-overflow:ellipsis; overflow:hidden;'>"+
							"<span style=' width:120px; font-size: 17px;  '>"+
							map.get("attentionPL")+
							"</span>"+
						"</div>"+
						"<div style=' width:26%; '>"+	
							"<span style='font-size: 17px; '>"+
							isNull(map.get("time"))+
							"</span>"+
//							"<span style='font-size: 17px; '>"+
//							 isNull(map.get("cancletime"))+
//							"</span>"+
						"</div>"+
		"</li>");
			}
		}
		return sbuf.toString();
	}

	private Object isNull(Object object) {
		if (object == null) {
			return "0";
		}
		return object;
	}

	/**
	 * 扫描信息流程
	 */
	@Clear
	public void getScanFlowInfo() {
		// 个人20条，县级40条，市级50条
		int agentType = getParaToInt("agentType");
		int userId = getParaToInt("userId");

		//调用DlsInfoDetailModel类的getScanFlowInfosaomiao方法
		List<Record> list = Db.find(DlsInfoDetailModel.dao.getScanFlowInfosaomiao(agentType, userId).toString());
		
		List<Map<String, String>> datalist = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		for (Record re : list) {
			map = new HashMap<String, String>();
			// 获取当前用户所在的level等级
			Object countrylevel = re.get("countrylevel");
			Object personlevel = re.get("personlevel");
			Object citylevel = re.get("citylevel");
			String getInfo = getUserLevel(userId, personlevel, countrylevel,
					citylevel);
			// 根据getInfo 得到扫码值
			/*if (re.get("cancletime") != null) {

				map.put("value", "- " + re.get(getInfo).toString());
			} else {
				map.put("value", re.get(getInfo).toString());
			}*/

			map.put("value", re.get(getInfo).toString());
			
			
			try {
				
				map.put("attentionPL", re.get("name").toString());
			} catch (Exception e) {
				// TODO: handle exception
				map.put("attentionPL", "平台表不存在该平台");
			}
			
//			map.put("smtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//			.format(re.get("smtime")));
			Object object = re.get("cancletime");
			if(object!=null){
				map.put("time", timeIsNull(re.get("cancletime")));
			}else{
				map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(re.get("smtime")));
			}
//			map.put("cancletime", timeIsNull(re.get("cancletime")));
			map.put("attpeople", re.get("attpeople").toString());
			map.put("headimgurl", re.getStr("headimgurl"));

			datalist.add(map);
		}
		renderHtml(jointScanTable(datalist));
		// renderJson(Saomajilu.getAgentUserDetail(1, 6,agentType, userId));
	}

	
	/**
	 * 扫描信息流程
	 */
	@Clear
	public void getScanFlowInfoForWap() {
		// 个人20条，县级40条，市级50条
				int agentType = getParaToInt("agentType");
				int userId = getParaToInt("userId");

				//调用DlsInfoDetailModel类的getScanFlowInfosaomiao方法
				List<Record> list = Db.find(DlsInfoDetailModel.dao.getScanFlowInfosaomiao(agentType, userId).toString());
				
				List<Map<String, String>> datalist = new ArrayList<Map<String, String>>();
				Map<String, String> map = null;
				for (Record re : list) {
				
					
					map = new HashMap<String, String>();
					// 获取当前用户所在的level等级
					Object countrylevel = re.get("countrylevel");
					Object personlevel = re.get("personlevel");
					Object citylevel = re.get("citylevel");
					String getInfo = getUserLevel(userId, personlevel, countrylevel,
							citylevel);
					// 根据getInfo 得到扫码值
					/*if (re.get("cancletime") != null) {

						map.put("value", "- " + re.get(getInfo).toString());
					} else {
						map.put("value", re.get(getInfo).toString());
					}*/

					//如果扫码计费为0则不增加到显示记录中
					if(re.get(getInfo).toString().equals("0.0")){
						continue;
					}
					map.put("value", re.get(getInfo).toString());
					
					
					try {
						map.put("attentionPL", re.get("name").toString());
					} catch (Exception e) {
						// TODO: handle exception
						map.put("attentionPL", "平台表不存在该平台");
					}
					
//					map.put("smtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//					.format(re.get("smtime")));
					String object = re.get("cancletime");
					//如果取消关注时间不为空则放入取消关注时间
					if(object!=null){
						//将时间格式进行转换方便WAP页显示
						java.util.Date util_date=null;
					try {
						util_date=	new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeIsNull(re.get("cancletime")));
					} catch (ParseException e) {
						System.out.println("时间类型转换失败");
					}
					Date sql_date= new Date(util_date.getTime());
					String wapdate=new SimpleDateFormat("yy-MM-dd HH:mm").format(sql_date);
					//时间格式转换结束
						map.put("smtime", wapdate);
					}else{//取消关注时间为空(没有取消关注),则放入扫码时间
						map.put("smtime", new SimpleDateFormat("yy-MM-dd HH:mm")
						.format(re.get("smtime")));
					}
//					map.put("cancletime", timeIsNull(re.get("cancletime")));
					map.put("attpeople", re.get("attpeople").toString());
					map.put("headimgurl", re.getStr("headimgurl"));

					datalist.add(map);
				}
					renderJson(datalist);;
		// renderJson(Saomajilu.getAgentUserDetail(1, 6,agentType, userId));
	}
	/**
	 * 转换取消关注时间
	 * 
	 * @param cancelTime
	 * @return
	 */

	private String timeIsNull(Object cancelTime) {
		if (cancelTime == null) {
			return "未取消";
		}
		return cancelTime.toString();
	}

	private String getUserLevel(int userId, Object personlevel,
			Object countrylevel, Object citylevel) {
		if (personlevel != null
				&& userId == Integer.valueOf(personlevel.toString())) {
			return "personget";
		} else if (countrylevel != null
				&& userId == Integer.valueOf(countrylevel.toString())) {
			return "countryget";
		} else if (citylevel != null
				&& userId == Integer.valueOf(citylevel.toString())) {
			return "cityget";
		} else {
			return null;
		}
	}

	/**
	 * 昨日收入 个人，市级，县 的收入
	 * 
	 * @param angentType
	 *            代理商等级
	 * @param loginUser
	 *            当前用户
	 * @return
	 */
	@Clear
	public void getYestardayIncomeForDifferentAgent() {
		int angentType = getParaToInt("angentType");
		String loginUser = getPara("userName");
		int userId = getParaToInt("userId");
		String inCome = doCalculateIncomeForDifferentAgent("2", angentType,
				loginUser, userId);
		final JSONObject result = new JSONObject();
		result.put("inCome", inCome);
		final JSONObject retObj = (JSONObject) JSONObject.toJSON(result);
		renderJson(retObj);
	}

	/**
	 * 获取 个人，市级，县 的累计收入
	 * 
	 * @return
	 */
	@Clear
	public void getIncomeForDifferentAgent() {
		int angentType = getParaToInt("angentType");
		String loginUser = getPara("userName");
		int userId = getParaToInt("userId");

		String inCome = doCalculateIncomeForDifferentAgent("1", angentType,
				loginUser, userId);
		final JSONObject result = new JSONObject();
		result.put("inCome", inCome);
		final JSONObject retObj = (JSONObject) JSONObject.toJSON(result);
		renderJson(retObj);
	}
	/**
	 * 获取 个人，市级，县 的一个月的累计收入
	 * 
	 * @return
	 */
	@Clear
	public void getIncomeForDifferentAgent2() {
		int angentType = getParaToInt("angentType");
		String loginUser = getPara("userName");
		int userId = getParaToInt("userId");

		String inCome = doCalculateIncomeForDifferentAgent("4", angentType,
				loginUser, userId);
		final JSONObject result = new JSONObject();
		result.put("inCome", inCome);
		final JSONObject retObj = (JSONObject) JSONObject.toJSON(result);
		renderJson(retObj);
	}
	
	/**
	 * 获取 个人，市级，县 的账户余额
	 * 
	 * @return
	 */
	@Clear
	public void getBalanceForDifferentAgent() {
		int angentType = getParaToInt("angentType");
		String loginUser = getPara("userName");
		int userId = getParaToInt("userId");
		String inCome = doCalculateIncomeForDifferentAgent("3", angentType,
				loginUser, userId);
		final JSONObject result = new JSONObject();
		result.put("inCome", inCome);
		final JSONObject retObj = (JSONObject) JSONObject.toJSON(result);
		renderJson(retObj);

	}

	private String doCalculateIncomeForDifferentAgent(String day,
			int angentType, String loginUser, int userId) {
		DecimalFormat format = new DecimalFormat("0.00");
		String result = format.format(DlsInfoDetailModel.dao.doCalculateIncomeForDifferentAgentExtractionTime(day, angentType, loginUser, userId));
		return result;

	}

	/**
	 * 扫描信息流程（演示模版）
	 */
	public void moban() {
		// 个人20条，县级40条，市级50条
		userModel user = getSessionAttr("loginuser");
		int levelid = user.getInt("levelid");// 数据库中的levelid 比获取的多1
												// （数据库：2个人。。3县级 4市级）。。
		int userId = user.getInt("id");
		int agentType = levelid - 1;
		String loginname = user.getStr("loginname");

		//调用DlsInfoDetailModel类的moban方法
		List<Record> list = Db.find(DlsInfoDetailModel.dao.moban(userId).toString());
		
		
		List<Map<String, String>> datalist = new ArrayList<Map<String, String>>();
		Map<String, String> map =  new HashMap<String, String>();
		for (Record re : list) {
			// 获取当前用户所在的level等级
			Object countrylevel = re.get("countrylevel");
			Object personlevel = re.get("personlevel");
			Object citylevel = re.get("citylevel");
			String getInfo = getUserLevel(userId, personlevel, countrylevel,
					citylevel);
			String inCome = doCalculateIncomeForDifferentAgent("1", agentType,
					loginname, userId);
			String nowCome = doCalculateIncomeForDifferentAgent("0", agentType,
					loginname, userId);
			// 根据getInfo 得到扫码值
			if (re.get("cancletime") != null) {

				map.put("value", "- " + re.get(getInfo).toString());
			} else {
				map.put("value", re.get(getInfo).toString());
			}

			map.put("smtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(re.get("smtime")));
			map.put("attentionPL", re.get("name").toString());
			String imgurl = re.get("headimgurl");
			map.put("headimgurl", imgurl);
			String attpeople = re.get("attpeople");
			map.put("attpeople", attpeople);
			map.put("inCome", inCome);// 总收入
			map.put("nowCome", nowCome);// 今日收入

			datalist.add(map);
		}

		renderJson(datalist);
	}


}
