package controller.cw;


import model.Getxiangqing;
import model.Gongzhonghao;
import model.GuanggaozhuModel;
import model.Saomajilu;
import model.Tixianjilu;
import model.bankModel;
import model.levelModel;
import model.upgradeModel;
import model.userModel;
import model.caiwu.ggzcaiwuModel;
import util.GetTime;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.utils.SqlStrUtils;

/**
 * 财务模块
 * 
 * @author apple
 * 
 */
public class CaiwuController extends Controller {

	/**
	 * 获取申请提现的记录
	 */
	public void getsqtxjl() {
		Page<Tixianjilu> nolist = Tixianjilu.dao.noreflect(getParaToInt(0, 1), 10);
		setAttr("nolist",nolist);
		render("/caiwu/caiwuzhanghu.html");
	}
		

	/****申请提现***
	 * 获取申请提现的记录的提交按钮	财务——》提现处理——》提交按钮
	 */
	public void getsousuo(){
		String xingming=getPara("xingming");
		Page<Tixianjilu> nolist = Tixianjilu.dao.tixiansoushuo(getParaToInt(0, 1), 10,xingming);
		setAttr("nolist",nolist);
		render("/caiwu/caiwuzhanghu.html");
	}

		

	
	/**
	 * 获取已处理提现记录
	 */
	public void getycltxjl(){
		Page<Tixianjilu> yeslist = Tixianjilu.dao.yesreflect(getParaToInt(0, 1), 10);
		setAttr("yeslist",yeslist);
		
		render("/caiwu/yesreflect.html");
	}
	/***已申请提现***
	 * 获取已申请提现的记录的提交按钮	财务账户——》已处理提现——》已处理提现搜索按钮
	 * 
	 */
	public void getyichulisousuo(){
		
		String tixianxingming=getPara("tixianxingming");
		Page<Tixianjilu> nolist = Tixianjilu.dao.yesreflectsousuo(getParaToInt(0, 1), 10,tixianxingming);
		setAttr("yeslist",nolist);
		render("/caiwu/yesreflect.html");
	}
	
	

	/**
	 * 批量处理申请提现的记录
	 */
	public void clsqtxjl() {
		String[] s = getParaValues("reflect.id");
		JSONObject json=new JSONObject();
		if(s==null){
			json.put("info", "请选择要处理的数据");
			final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
			renderJson(retObj);
		}else{
			String setIn = SqlStrUtils.setIn(s);
			int chuli = Tixianjilu.dao.chuli(setIn);
			json.put("success", true);
			json.put("info", "处理成功");
			final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
			renderJson(retObj);
		}
		//getsqtxjl();
	}




	/**
	 * 处理代理商升级申请
	 */
	public void clsqshengji() {
		String[] values = getParaValues("sjid");
		JSONObject json=new JSONObject();
		if(values==null){
			json.put("info", "请选择要处理的数据");
			final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
			renderJson(retObj);
		}else{
			upgradeModel.dao.shengji(values);
			json.put("info", "处理成功");
			final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
			renderJson(retObj);
		}
		//getshengji();
	}
	/**
	 * 获取升级代理商升级申请的记录   财务——》经销商升级——》提交按钮
	 */
	public void getshengji() {
		String xingming=getPara("xingming");
		String dagai=getPara("dagai");
//		System.out.println(xingming);
//		if(xingming!=null && dagai!=null &&xingming.equals("申请用户名") && dagai.equals("1")){
			Page<upgradeModel> sjlistl = upgradeModel.dao.alllevelid(getParaToInt(0, 1), 10,xingming,dagai);
			
			setAttr("sjjll", sjlistl);
//		Page<upgradeModel> sjlistw = upgradeModel.dao.allwantlevel();
//		setAttr("sjjlw", sjlistw);
//		}else if(xingming.equals("申请用户名") && dagai.equals("2")){
			
//		}
		render("/caiwu/jingxiaoshangshengji.html");
	}
	
	
	/**
	 * 获取明晰
	 */
	public void mingxi(){
		Page<Tixianjilu> mxList=Tixianjilu.dao.paginate(getParaToInt(0, 1), 10, "select * ", "from reflect order by reflecttime desc");
		setAttr("mxList", mxList);
		
		//还需要查询平台和代理商的级别返回到页面
		Page<Gongzhonghao> gzzhList=Gongzhonghao.dao.paginate(1, 10, "select * ", "from publicaccount");
		setAttr("gzzhList", gzzhList);
		
		Page<levelModel> agentlevelList=levelModel.dao.findAll();
		setAttr("agentlevelList", agentlevelList);
		
		render("/caiwu/shouzhimingxi.html");
	}
	
	/**
	 * 根据条件检索信息
	 */
	public void jiansuo(){
		//获取前台检索参数
		String begintime=getPara("begintime");
		String endtime=getPara("endtime");
		String attentionPL=getPara("attentionPL");
		String agentlevel=getPara("agentlevel");
		
		/*long  between = btime - etime;
		if(between>0){//显示页面有待更改
			setAttr("info", "开始时间不能大于结束时间");
			mingxi();
		}*/
		
		Page<Saomajilu> mxcx=Saomajilu.dao.jiansuo(getParaToInt(0, 1), 10,begintime,endtime,attentionPL,agentlevel);
		setAttr("mxcx", mxcx);
		render("/caiwu/mingxichaxun.html");
	}
	/**
	 * 提现记录中的详情记录
	 */
	public void getxiangqing(){
		int idd=0;
		
		if(getParaToInt("id")!=null){
			idd=getParaToInt("id");
			setSessionAttr("idd", idd);
		}else{
			idd=getSessionAttr("idd");
		}
		
		Page<Getxiangqing> getxiangqing = Getxiangqing.dao.getxiangqing1(getParaToInt(0, 1), 2000, idd);
			setAttr("XQ", getxiangqing);
			render("/caiwu/xiangqing.html");
		
	}
	/**
	 *财务——》广告主充值——》获得所有广告主
	 *saoma/WebRoot/caiwu/ggzcaiwu.html
	 */
	public void cunrujine(){
		//获得所有的广告主
		Page<userModel> quanggz = userModel.dao.ggzcunqian(getParaToInt(0, 1), 10);
		setAttr("ggzjje",quanggz);
		render("/caiwu/ggzcaiwu.html");
	}
	/**
	 *财务——》广告主充值——》获得所有广告主————》指定的广告主{{详情}}按钮
	 * caiwuchongggz.html
	 *
	 */
	public void tochongzhi(){

		//获得广告主的ID
		int ggzid = getParaToInt("id");
		/**获得 广告主的余额
		调用model获得所有平台台一段时间内的花费*/
		Record cost = GuanggaozhuModel.dao.GetAllTotalCost(ggzid);
		// 将充值总金额减去总花费得到余额
		Double allcost = cost.getDouble("all");
		
		
		// 调用model获得广告主的所有充值金额
		Record money = GuanggaozhuModel.dao.GetMoenyById(ggzid);
		Double totalMoney = 0.0;
		try {
			totalMoney = money.getDouble("totalMoney");
		} catch (Exception e) {
			
		}
		if (totalMoney ==null) {
			totalMoney = 0.0;
		}
		Double balance = totalMoney - allcost;
		//获得所有的银行
		Page<bankModel> bankList=bankModel.dao.findAll();
		
		
		List<ggzcaiwuModel> ggzjilu = new CaiwuController().chongzhijilu(ggzid);
		setAttr("ggzjje", ggzjilu);
		setAttr("bankList", bankList);//银行
		setAttr("id", ggzid);//广告主ID
		setAttr("balance", balance);//余额
		render("/caiwu/caiwuchongggz.html");
	}
/**
 * 获得该广告主的充值记录
 * @param 广告主的ID
 * @return	查询的所有记录
 */
	public List<ggzcaiwuModel> chongzhijilu(int id){
		//获得广告主的ID

		List<ggzcaiwuModel> quanggz = ggzcaiwuModel.dao.adidchongzhijilu(id);

		return quanggz;
		}
	
	/**
	 * 财务——》广告主充值——》获得所有广告主————》指定的广告主{{详情}}按钮-->跳转的页面输入的参数
	 */
	public void chongzhi(){
		//获得广告主的ID
		Integer adid = getParaToInt("id");
		//获得字符串类型的金额
		String Mon=getPara("chongzhijine");
		//将字符串类型的金额，转成float类型的
		float Money = Float.parseFloat(Mon);
		//存入当前时间
		String dete=GetTime.getTime();
		//获得财务的ID
		Integer caiwuchongzhiren = getSessionAttr("currentUserId");
		
		//获得银行
		String Bank=getPara("yinhang");
		//将获得的参数存到数据库中
		String chong =ggzcaiwuModel.dao.cunqian(adid, Money, dete, caiwuchongzhiren, Bank);
		String ok = "0";
		if (chong == "1") {
			 ok = "1";
		}else {
			 ok = "2";
		}
		//彩笔代码，重新查询下，然后返回
		Page<userModel> ggzjje = userModel.dao.ggzcunqian(getParaToInt(0, 1), 10);
		setAttr("ggzjje",ggzjje);
		setAttr("ok",ok);
		render("/caiwu/ggzcaiwu.html");
	}
	/**
	 * 财务——》广告主充值——》获得所有广告主————》广告主搜索按钮
	 */
	public void cwggzsousuo(){
		String xingming=getPara("xingming");
		//获得根据“xingming参数指定的广告主
		Page<userModel> ggzzhiding = userModel.dao.cwggzsousuoModel(getParaToInt(0, 1), 10,xingming);
		setAttr("ggzjje",ggzzhiding);
		render("/caiwu/ggzcaiwu.html");
	}



}
