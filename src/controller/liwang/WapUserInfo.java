package controller.liwang;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.kit.ParaMap;

import controller.dls.DlsInfoDetail;
import model.Tixianjilu;
import model.userModel;
import model.dls.DlsInfoDetailModel;
import model.gly.TixianUserTime;
import net.sf.json.JSONObject;

public class WapUserInfo extends Controller{
	/**
	 * 根据userId查找头像昵称
	 */
	@Clear
	public void info(){
		List<Record> record=Db.find(
				"select nickname,headimgurl,forward from user  where "
				+ " id="+getPara("userId"));
		if(record==null || record.isEmpty()){
			renderJson(JSONObject.fromObject("{'attpeople':'无记录','headimgurl':'#'}"));
			return;
		}
		renderJson(record.get(0));
	}
	
	@Clear
	public void forward(){
		System.out.println("跳转获取的code参数为： "+getPara("code"));
		System.out.println("跳转获取的state参数为： "+getPara("state"));
		SnsAccessToken snsAccessToken=getSnsAccessToken("wxdd905c6a6c4cdcb3","0d06a4b3e485a2bdf104510016cf9cc8",getPara("code"));
		String openid=snsAccessToken.getOpenid();
		Record record=Db.findFirst("select id from user where openid='"+openid+"'");
		if(record!=null && record.get("id")!=null){
			setAttr("hasBind","Y");
			redirect("/forward.html");
			return ;
		}
		String AccessToken = snsAccessToken.getAccessToken();// refresh_token.用户刷新access_token
		//请求2次用户信息
		String xiangurl = "https://api.weixin.qq.com/sns/userinfo?";
		String nickName=null;
		String headImgUrl=null;
		for (int i = 0; i < 2; i++) { // 最多三次请求
			String yy = xiangurl+"access_token="+AccessToken+"&openid="+openid;//合成指定路径，用来获取参数
			String json = HttpKit.get(yy);//访问路径
			JSONObject jsonObject=JSONObject.fromObject(json);
			if(jsonObject==null || !jsonObject.containsKey("headimgurl") || !jsonObject.containsKey("nickname"))
				continue;
			headImgUrl=jsonObject.getString("headimgurl");
			nickName=jsonObject.getString("nickname");
			if(headImgUrl==null || "".equals(headImgUrl) || 
					nickName==null || "".equals(nickName)){
				continue;
			}
			System.out.println("获取头像昵称成功！");
			break;
		}
		System.out.println("跳转查询的openid： "+openid);
		if(openid==null || "".equals(openid)){
			renderText("绑定失败！");
			return;
		}
		Db.update("update user set openid='"+openid+"',headimgurl='"+headImgUrl+"',nickname='"+nickName+"' where id="+getPara("state"));
		redirect("/share/index_share?u="+getPara("state")+"&a="+(getParaToInt("agenType")-1));
	}
	
	/**
	 * wap版提现处理
	 */
	@Clear
	public void getMoney(){
		System.out.println("提现跳转获取的code参数为： "+getPara("code"));
		SnsAccessToken snsAccessToken=getSnsAccessToken("wxdd905c6a6c4cdcb3","0d06a4b3e485a2bdf104510016cf9cc8",getPara("code"));
		String openid=snsAccessToken.getOpenid();
		System.out.println("提现跳转查询的openid： "+openid);
//		List<Record> records=Db.find("select * from user where openid='"+openid+"'");
		userModel user=userModel.dao.findFirst("select * from user where openid='"+openid+"'");
		Integer levelid=null;
		try{levelid=user.get("levelid");}catch(Exception e){};
		if(user==null){
			setAttr("notFound",1);
			render("/forward.html");
		}else if(user!=null  && levelid.equals(10)){
			setAttr("tempUser",1);
			setAttr("openid",openid);
			render("/forward.html");
		}else{
			System.out.println("点击提现的已存在用户是："+user.getStr("loginname"));
			setSessionAttr("currentUser", user.get("loginname"));//登陆
			setSessionAttr("currentLevelid", user.get("levelid"));
			setSessionAttr("currentUserId", user.get("id"));
			setSessionAttr("currentUserName", user.get("realname"));
			setSessionAttr("loginuser", user);
			setAttr("loginuser", user);
			setAttr("getMoney",1);
			
			if(levelid.equals(2)){//个人代理商
				render("/geren.html");
			}else if(levelid.equals(3)){//县级代理商
				render("/xianji.html");
			}else if(levelid.equals(4)){//市级代理商
				render("/shiji.html");
			}else{
				setAttr("notFound",1);
				render("/forward.html");
			}
		}
	}
	
	/**
	 * 完善提现信息
	 */
	@Clear
	public void getMoney_Info(){
		String openid=getPara("user.openid");
		System.out.println("提现跳转查询的openid： "+openid);
		String phone=getPara("user.phone");
		String realname=getPara("user.realname");
		String bankInfo=getPara("user.bankinfo");
		String bankNo=getPara("user.bankno");
		if(realname==null || "".equals(realname) || phone==null || "".equals(phone) 
				|| bankNo==null || "".equals(bankNo) || bankInfo==null || "".equals(bankInfo)){
			renderText("请仔细填写完整信息后提交！");
			return;
		}
		float inCome=DlsInfoDetailModel.dao.doCalculateIncomeForDifferentAgentExtractionTime("3",10,openid,
				userModel.dao.findFirst("select id from user where loginname='"+openid+"'").getInt("id"));
		if(inCome<=0){
			renderText("无可提现余额！");return;
		}
		if(inCome>20){
			inCome=20;
		}
		Db.save("reflec", new Record().set("swiftno", Tixianjilu.dao.finMaxSwiftno()).set("loginname", openid)
				.set("username", realname).set("bankinfo", bankInfo).set("bankno", bankNo).set("reflectmoney",inCome));
		renderText("OK!操作完成！");
	}
	
	/**
	 * 我要挣钱
	 */
	@Clear
	public void makeMoney(){
		System.out.println("提现跳转获取的code参数为： "+getPara("code"));
		SnsAccessToken snsAccessToken=getSnsAccessToken("wxdd905c6a6c4cdcb3","0d06a4b3e485a2bdf104510016cf9cc8",getPara("code"));
		String openid=snsAccessToken.getOpenid();
		System.out.println("提现跳转查询的openid： "+openid);
		List<Record> records=Db.find("select id,levelid,headimgurl,nickname from user where openid='"+openid+"'");
		String nickName=null;
		String headImgUrl=null;
		if(records==null || records.isEmpty() || records.get(0).getStr("nickname")==null || 
				records.get(0).getStr("headimgurl")==null){
			String AccessToken = snsAccessToken.getAccessToken();// refresh_token.用户刷新access_token
			//请求2次用户信息
			String xiangurl = "https://api.weixin.qq.com/sns/userinfo?";
			for (int i = 0; i < 2; i++) { // 最多三次请求
				String yy = xiangurl+"access_token="+AccessToken+"&openid="+openid;//合成指定路径，用来获取参数
				String json = HttpKit.get(yy);//访问路径
				JSONObject jsonObject=JSONObject.fromObject(json);
				if(jsonObject==null || !jsonObject.containsKey("headimgurl") || !jsonObject.containsKey("nickname"))
					continue;
				headImgUrl=jsonObject.getString("headimgurl");
				nickName=jsonObject.getString("nickname");
				if(headImgUrl==null || "".equals(headImgUrl) || 
						nickName==null || "".equals(nickName)){
					continue;
				}
				System.out.println("获取头像昵称成功！");
				break;
			}
		}
		if(records==null || records.isEmpty()){
//			Db.save("user", new Record().set("loginname", openid).set("realname", "会员").
//					set("password", "123456").set("openid", openid).set("bankid", -1).set("leader", getPara("userId")));
			new TixianUserTime().set("loginname", openid).set("username", "会员").set("password", "123456")
				.set("openid", openid).set("bankid", 1).set("leader", getPara("userId")).set("levelid", 10)
				.set("headimgurl", headImgUrl).set("nickname", nickName).save();
			if(getPara("userId")!=null){
				Db.update("UPDATE user  SET forward=If(ISNULL(forward),1,forward+1) where id="+getPara("userId"));
			}
			Record record=Db.findFirst("select id from user where loginname='"+openid+"'");
			redirect("/share/index_share?u="+record.get("id")+"&a=10");
		}else{
			redirect("/share/index_share?u="+records.get(0).get("id")+"&a="+records.get(0).get("levelid"));
		}
		
	}
	
	/**
	 * 转发加一
	 */
	@Clear
	public void  forwarder(){
		Db.update("UPDATE user  SET forward=If(ISNULL(forward),1,forward+1) where id="+getPara("userId"));
		renderJson("OK");
	}
	
	/**
	 * 通过code获取access_token
	 * @param code 第一步获取的code参数
	 * @param appId 应用唯一标识
	 * @param secret 应用密钥AppSecret
	 * @return SnsAccessToken
	 */
	public static SnsAccessToken getSnsAccessToken(String appId, String secret, String code) {
		SnsAccessToken result = null;
		for (int i = 0; i < 3; i++) { // 最多三次请求
			Map<String, String> queryParas = ParaMap.create("appid", appId).put("secret", secret).put("code", code)
					.getData();
			String json = HttpKit.get("https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code", queryParas);
			result = new SnsAccessToken(json);

			if (result.isAvailable())
				break;
		}
		return result;
	}
	
	public static void main(String[] args) {
			Calendar c = Calendar.getInstance();
		  // 这是已知的日期
		  Date d = new Date();
		  c.setTime(d);
		  c.set(Calendar.DAY_OF_MONTH, 1);
		  c.set(Calendar.HOUR_OF_DAY, 0);
		  c.set(Calendar.MINUTE, 0);
		  c.set(Calendar.SECOND, 0);
		  // 1号的日期
		  d = c.getTime();
		  c.setTimeInMillis(1448899200663l);
		  System.out.println(d.toLocaleString());
	}
	
}
