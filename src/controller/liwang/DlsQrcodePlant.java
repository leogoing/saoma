package controller.liwang;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;
import com.utils.entity.Shenqingerweima;
import com.utils.erweima.GetEWM;

import controller.pingtaiSet.model.PingTaiSetModel;
import controller.pingtaiSet.model.SetType;
import model.GetPingtaiModel;
import model.GuanggaozhuModel;
import util.GetTime;
/**
 * 代理商模块——申请平台
 * @author zmc
 *
 */

public class DlsQrcodePlant  extends Controller{

	/**
	 * 根据代理商级别跳转页面
	 */
	@Clear
	public void toQrcodePlantPage() {
		final int agentType = getParaToInt("agentType");
		setAttr("currentUser", getSessionAttr("currentUser"));
		setAttr("currentLevelid", getSessionAttr("currentLevelid"));
		setAttr("currentUserId", getSessionAttr("currentUserId"));
		setAttr("agentType", agentType);// 区分类型
		if (agentType == 2) {// 跳转县级代理商平台信息页面
			render("/xianjidailishang/xianjishenqingpingtai.html");
		} else if (agentType == 3) {// 跳转市级代理商平台信息页面
			render("/shijidailishang/shijishenqingpingtai.html");
		} else if (agentType == 1) {// 跳转个人代理商平台信息页面
			render("/gerendailishang/shenqingpingtai.html");
		}
	}
	@Clear
	public void toto(){
		final int agentPage = getParaToInt("agentPage");
		render("/gerendailishang/ge.html");
	}
	
	/**
	 * 循环出二维码
	 */
	@Clear
	public void getQrcodeInfo(){
		Integer agentType=getParaToInt("agentType");
		int findType = SetType.dao.findType();
		//取出session中的必要数据
		String le=getSessionAttr("currentLevelid").toString();
		int id=getSessionAttr("currentUserId");
		
		if(findType==1){
			List<PingTaiSetModel> findPingtaiAll = PingTaiSetModel.dao.findPingtaiAll();
			List<Shenqingerweima> list = new ArrayList<Shenqingerweima>();
			mm(id, le, list, findPingtaiAll);
			setAttr("list", list);
			render("/gerendailishang/shenqingpingtai.html");
		}else  if(findType==2){
			List<Shenqingerweima> list = new ArrayList<Shenqingerweima>();
			List<PingTaiSetModel> findPingtaiAll = PingTaiSetModel.dao.findtype2();
			mm(id, le, list, findPingtaiAll);
			 List<PingTaiSetModel> findwei = PingTaiSetModel.dao.findwei();
			 mm(id, le, list, findwei);
			 for(int i=0;i<list.size();i++){
				 Shenqingerweima shenqingerweima = list.get(i);
				 System.out.println(shenqingerweima);
			 }
			setAttr("list", list);
			render("/gerendailishang/shenqingpingtai.html");
		}else if(findType==3){
			List<Shenqingerweima> list = new ArrayList<Shenqingerweima>();
			List<PingTaiSetModel> findtype3 = PingTaiSetModel.dao.findtype3();
			mm(id, le, list, findtype3);
			 List<PingTaiSetModel> findwei1 = PingTaiSetModel.dao.findwei1();
			 mm(id, le, list, findwei1);
			setAttr("list", list);
			render("/gerendailishang/shenqingpingtai.html");
		}

	}
	
	
	/**
	 * 将List转换为JSONArray
	 * 
	 * @param qrcodes
	 * @return
	 */
	private final static JSONArray convertQr(List<Record> qrcodes) {
		final Iterator<Record> iter = qrcodes.iterator();
		final JSONArray resArr = new JSONArray();
		Record tmpQrcode = null;
		while (iter.hasNext()) {
			tmpQrcode = iter.next();
			resArr.add(JSON.parse(tmpQrcode.toJson()));
		}
		return resArr;
	}
	
	/**
	 * 
	//获取所有的已上架的没有服务器（使用我们服务器）的平台
	 */
	@Clear
	public void getAllPt() {
		System.out.println("进入方法了");
		List<GetPingtaiModel> findPingtaiAll = GetPingtaiModel.dao.findPingtaiNoServer();
		String json = JsonKit.toJson(findPingtaiAll);
		System.out.println("ptlist"+findPingtaiAll);
		renderJson(findPingtaiAll);
	}
	/**
	 * 根据APPID，APPsecret和用户ID生成所有平台的二维码
	 */
	@Clear
	public void getLinshiEWM(){
		String appid = getPara("appid");
		String appsecret = getPara("appsecret");
		Integer id =getParaToInt("id");
		String erweima =GetEWM.linshiEWMByApp(appid, appsecret, id);
		System.out.println("这是生成的二维码路径"+erweima);
		String json=JsonKit.toJson(erweima);
		renderJson(json);
	}
	
		
	private String timeshengyu(String s1,String s2) throws ParseException{
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1=sdf.parse(s1);
		Date d2=sdf.parse(s2);
		long tim=d1.getTime()-d2.getTime();
		int mm=(int)tim/1000;
		int nn=mm/(60*60);
		int mn=mm-nn*60*60;
		int nm=mn/60;
		return "，还有"+nn+"小时"+nm+"分钟下架";
	}
		
	@Clear
	public void mm(int id,String le,List<Shenqingerweima> list ,List<PingTaiSetModel> findPingtaiAll){
		for(int i=0;i<findPingtaiAll.size();i++){
			Shenqingerweima ss=new Shenqingerweima();
			PingTaiSetModel pingTaiSetModel = findPingtaiAll.get(i);
			//平台名称
			ss.setPublicaccountname(pingTaiSetModel.getStr("name"));
			
			// 平台ID
			ss.setPingtaiid(pingTaiSetModel.getInt("id").toString());
			
			//有效期
			String youxiaoqi="30天";
			String adtype = pingTaiSetModel.getStr("adtype");
			if(adtype.equals("1")){
				youxiaoqi="永久";
			}
			
			ss.setYouxiaoqi(youxiaoqi);
			//保护期 
			ss.setBaohuqi(pingTaiSetModel.get("adtime").toString());
			
			//价格
			float jiage=(float)0;
			if(le.equals("2")){
				jiage=pingTaiSetModel.getFloat("personagentsp");
			}else if(le.equals("3")){
				jiage=pingTaiSetModel.getFloat("personagentsp")+pingTaiSetModel.getFloat("countryagentsp");
			}else if(le.equals("4")){
				jiage=pingTaiSetModel.getFloat("personagentsp")+pingTaiSetModel.getFloat("countryagentsp")+pingTaiSetModel.getFloat("cityagentsp");
			}
			jiage = new BigDecimal(jiage).setScale(3,BigDecimal.ROUND_HALF_UP).floatValue();
			ss.setJiage(jiage+"");
			
			//二维码
			String linshiEWM = GetEWM.linshiEWM(pingTaiSetModel.getStr("appid"), pingTaiSetModel.getStr("appsecret"), pingTaiSetModel.getStr("token"), id, pingTaiSetModel.getStr("publicaccountname"));
			ss.setErweima(linshiEWM);
			
			//静文提供的方法-------查询广告主余额
			Double getBalanceByUserId = 
					GuanggaozhuModel.dao.GetBalanceByUserId(pingTaiSetModel.getInt("adId"));
			System.out.println(pingTaiSetModel.getInt("adId")+"广告主余额"+getBalanceByUserId);
			// 还剩扫码次数
			double zongjia = (double)pingTaiSetModel.getFloat("zongjia");
			
			int number=(int)(getBalanceByUserId/zongjia);
			
			String downtime="";
			String shijian="";
			Object object = pingTaiSetModel.get("downtime");
			System.out.println(object );
			if(object!=null ){
				try {
					downtime=object.toString();
					System.out.println(downtime);
					shijian=timeshengyu(downtime, GetTime.getTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			ss.setXiangqing("还可以扫描"+number+"次"+shijian);
			ss.setCishu(number);
			ss.setShijian(shijian);
			
			list.add(ss);
		}
	}
	
}

