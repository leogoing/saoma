package controller.weixin;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Clear;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.weixin.demo.WeixinMsgController;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.jfinal.MsgController;
import com.jfinal.weixin.sdk.msg.in.InImageMsg;
import com.jfinal.weixin.sdk.msg.in.InLinkMsg;
import com.jfinal.weixin.sdk.msg.in.InLocationMsg;
import com.jfinal.weixin.sdk.msg.in.InShortVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jfinal.weixin.sdk.msg.in.InVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InVoiceMsg;
import com.jfinal.weixin.sdk.msg.in.event.InCustomEvent;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.weixin.sdk.msg.in.event.InLocationEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMassEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMenuEvent;
import com.jfinal.weixin.sdk.msg.in.event.InQrCodeEvent;
import com.jfinal.weixin.sdk.msg.in.event.InShakearoundUserShakeEvent;
import com.jfinal.weixin.sdk.msg.in.event.InTemplateMsgEvent;
import com.jfinal.weixin.sdk.msg.in.event.InVerifyFailEvent;
import com.jfinal.weixin.sdk.msg.in.event.InVerifySuccessEvent;
import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;
import com.jfinal.weixin.sdk.msg.out.News;
import com.jfinal.weixin.sdk.msg.out.OutCustomMsg;
import com.jfinal.weixin.sdk.msg.out.OutNewsMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
import com.utils.llll;
import com.utils.weixin.UpdateWeixinInPublicaccount;
import com.utils.weixin.util_access_token;

import controller.kaihuine.KAIHUINE_access_tokenModel;
import controller.pingtaiSet.model.PingTaiSetModel;
import model.GetPingtaiModel;
import model.JobMoneyModel;
import model.SMRDModel;
import model.Saomajilu;
import model.gly.Smrecord;
import model.weixin.CaidanModel;
import net.sf.json.JSONObject;

public class weixinController extends MsgController{
	static Logger logger = Logger.getLogger(WeixinMsgController.class);
	@Override
	public ApiConfig getApiConfig() {
		String appId=getPara("AppID");
		String appSecret=getPara("AppSecret");
		String token=getPara("Token");
		ApiConfig ac = new ApiConfig();
		ac.setAppId(appId);
		ac.setAppSecret(appSecret);
		ac.setToken(token);
		return ac;
	}

	@Clear
	@Override
	protected void processInTextMsg(InTextMsg inTextMsg) {

		String toUserName = inTextMsg.getToUserName();
		PingTaiSetModel findPTInformationtype =
				PingTaiSetModel.dao.findPTInformationtype(toUserName);
		if(findPTInformationtype!=null){
			int informationtype=findPTInformationtype.getInt("informationtype");
			if(informationtype==0){
				
			}else if(informationtype==1){
				
				String url=findPTInformationtype.getStr("informationurl");
				Map<String, String> map =new HashMap<String, String>();
				map.put("Type", "1");
				map.put("FromUserName", inTextMsg.getFromUserName());
				map.put("MsgType", inTextMsg.getMsgType());
				map.put("ToUserName", toUserName);
				map.put("CreateTime", inTextMsg.getCreateTime().toString());
				map.put("MsgId", inTextMsg.getMsgId());
				map.put("Content", inTextMsg.getContent());
				
				
				HttpKit.post(url, map, "");
				
			}
		}
		System.out.println(inTextMsg.getFromUserName());
		renderOutTextMsg("已收到");
	}

	@Override
	protected void processInImageMsg(InImageMsg inImageMsg) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void processInVoiceMsg(InVoiceMsg inVoiceMsg) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processInVideoMsg(InVideoMsg inVideoMsg) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processInShortVideoMsg(InShortVideoMsg inShortVideoMsg) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processInLocationMsg(InLocationMsg inLocationMsg) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processInLinkMsg(InLinkMsg inLinkMsg) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processInCustomEvent(InCustomEvent inCustomEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	@Clear
	protected void processInFollowEvent(InFollowEvent inFollowEvent) {
		
		
		
		Integer createTime = inFollowEvent.getCreateTime();
		String event = inFollowEvent.getEvent();
		String fromUserName = inFollowEvent.getFromUserName();
		System.out.println(fromUserName);
		String msgType = inFollowEvent.getMsgType();
		String toUserName = inFollowEvent.getToUserName();
		System.out.println(toUserName);
		
		
		PingTaiSetModel findPTInformationtype =
				PingTaiSetModel.dao.findPTInformationtype(toUserName);
		if(findPTInformationtype!=null){
			int informationtype=findPTInformationtype.getInt("informationtype");
			if(informationtype==0){
				
			}else if(informationtype==1){
				String url=findPTInformationtype.getStr("informationurl");
				Map<String, String> map =new HashMap<String, String>();
				map.put("Type", "2");
				map.put("CreateTime",createTime.toString());
				map.put("Event", event);
				map.put("FromUserName", fromUserName);
				map.put("MsgType", msgType);
				map.put("ToUserName", toUserName);
				
				HttpKit.post(url, map, "");
			}
		}
		
		//判断是否重复取消关注
		
		List<SMRDModel> rd = SMRDModel.dao.RD(toUserName, fromUserName);
		
		if(rd!=null){
			SMRDModel smrdModel = rd.get(0);
			Object object = smrdModel.get("cancletime");
			if(object==null){
				
				nn(inFollowEvent);//原来的方法和逻辑再次封装2016-1-5 16:26(王宇宁)
				
			}
		}
		renderOutTextMsg("");
		
		
		
		
		
	}

	@Override
	@Clear
	protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent) {
		String openId=inQrCodeEvent.getFromUserName();
		
		String name=inQrCodeEvent.getToUserName();
		String ToUserName=inQrCodeEvent.getToUserName();
		
		PingTaiSetModel findPTInformationtype =
				PingTaiSetModel.dao.findPTInformationtype(ToUserName);
		if(findPTInformationtype!=null){
			int informationtype=findPTInformationtype.getInt("informationtype");
			if(informationtype==0){
				System.out.println("11");
			}else if(informationtype==1){
				String url=findPTInformationtype.getStr("informationurl");
				Map<String, String> map =new HashMap<String, String>();
				map.put("Type", "3");
				map.put("Event", inQrCodeEvent.getEvent());
				map.put("EventKey", inQrCodeEvent.getEventKey());
				map.put("FromUserName", inQrCodeEvent.getFromUserName());
				map.put("MsgType", inQrCodeEvent.getMsgType());
				map.put("Ticket", inQrCodeEvent.getTicket());
				map.put("ToUserName", inQrCodeEvent.getToUserName());
				map.put("CreateTime", inQrCodeEvent.getCreateTime().toString());
				
				HttpKit.post(url, map, "");
			}
		}
		//判断是否重复关注
		List<SMRDModel> rd = SMRDModel.dao.RD(name, openId);
		System.out.println(rd);
		if(rd!=null && !rd.isEmpty()){
			System.out.println("bushikong");
		}
		if(rd!=null && !rd.isEmpty()){
			SMRDModel smrdModel = rd.get(0);
			Object object = smrdModel.get("cancletime");
			if(object!=null){
				mm(inQrCodeEvent);//原来的方法和逻辑再次封装2016-1-5 14:55(王宇宁)
			}
		}else{
			mm(inQrCodeEvent);//原来的方法和逻辑再次封装2016-1-5 14:55(王宇宁)
		}
		
		
	}
	
	public String access_token(String AppId,String AppSecret){
		KAIHUINE_access_tokenModel findAccess_token = KAIHUINE_access_tokenModel.dao.findAccess_token(AppId, AppSecret);
		
		String   access_token= findAccess_token.get("access_token");
		
		String string = HttpKit.get("https://api.weixin.qq.com/cgi-bin/groups/get?access_token="+access_token 
					);
		
		JSONObject o= JSONObject.fromObject(string);
		
		if(o.has("errcode")){
			String access_token2="";
			try {
				access_token2 = util_access_token.getAccess_token(AppId, AppSecret);
			} catch (IOException e) {
				e.printStackTrace();
			}
			UpdateWeixinInPublicaccount.dao.UpdateAccess_Token(access_token2, AppId, AppSecret);
			return access_token2;
		}
		return access_token;
	}

	@Override
	protected void processInLocationEvent(InLocationEvent inLocationEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processInMassEvent(InMassEvent inMassEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processInMenuEvent(InMenuEvent inMenuEvent) {
		System.out.println(inMenuEvent.getToUserName());
		System.out.println(inMenuEvent.getEventKey());
		SaveGh_34ea6d0a89ea(inMenuEvent);//吃虾的
		saveGh_60d5ed2513f3( inMenuEvent);//钱是怎么赚的
		SaveGh_8ea89be7fde1(inMenuEvent);//斑马王国
//		caidan(inMenuEvent);
//		renderOutTextMsg("");

	}

	@Override
	protected void processInSpeechRecognitionResults(
			InSpeechRecognitionResults inSpeechRecognitionResults) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processInTemplateMsgEvent(
			InTemplateMsgEvent inTemplateMsgEvent) {
		// TODO Auto-generated method stub

	}
	private void gg(InQrCodeEvent inQrCodeEvent,String huifuyu){
		if (InQrCodeEvent.EVENT_INQRCODE_SUBSCRIBE.equals(inQrCodeEvent.getEvent())) {
			logger.debug("扫码未关注：" + inQrCodeEvent.getFromUserName());
			
			//System.out.println("参数"+inQrCodeEvent.getEventKey().substring(8));
			//进行数据保存的操作，保存在smrecord表中
			//扫码时间、关注人、关注时间、取关时间、关注平台、市级所属、市级获利、县级所属、县级获利、
			//个人用户名、个人获利、扫码平台、是否提现（默认未提现）、代理商等级
			System.out.println(huifuyu);
			Saomajilu.dao.smsave(inQrCodeEvent);//将参数传入Saomajilu中
			OutTextMsg outMsg = new OutTextMsg(inQrCodeEvent);
			outMsg.setContent(huifuyu);
			System.out.println("55");
			render(outMsg);
		}
		//判断是不是已经关注，是：进入这个
		if (InQrCodeEvent.EVENT_INQRCODE_SCAN.equals(inQrCodeEvent.getEvent())) {
			logger.debug("扫码已关注：" + inQrCodeEvent.getFromUserName());
			System.out.println("66");
			renderOutTextMsg("您已关注，谢谢");
		}
	}
	private void gg(InQrCodeEvent inQrCodeEvent,String title,String miaoshu,String imgurl,String newsurl){
		if (InQrCodeEvent.EVENT_INQRCODE_SUBSCRIBE.equals(inQrCodeEvent.getEvent())) {
			logger.debug("扫码未关注：" + inQrCodeEvent.getFromUserName());
			
			//System.out.println("参数"+inQrCodeEvent.getEventKey().substring(8));
			//进行数据保存的操作，保存在smrecord表中
			//扫码时间、关注人、关注时间、取关时间、关注平台、市级所属、市级获利、县级所属、县级获利、
			//个人用户名、个人获利、扫码平台、是否提现（默认未提现）、代理商等级
			
			Saomajilu.dao.smsave(inQrCodeEvent);//将参数传入Saomajilu中
			OutNewsMsg out =new OutNewsMsg(inQrCodeEvent);
			out.addNews(title,miaoshu,imgurl, newsurl);
			render(out);
		}
		//判断是不是已经关注，是：进入这个
		if (InQrCodeEvent.EVENT_INQRCODE_SCAN.equals(inQrCodeEvent.getEvent())) {
			logger.debug("扫码已关注：" + inQrCodeEvent.getFromUserName());
			renderOutTextMsg("您已关注，谢谢");
		}
	}

	@Override
	protected void processInShakearoundUserShakeEvent(InShakearoundUserShakeEvent inShakearoundUserShakeEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processInVerifySuccessEvent(InVerifySuccessEvent inVerifySuccessEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processInVerifyFailEvent(InVerifyFailEvent inVerifyFailEvent) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	
	public void SaveGh_8ea89be7fde1(InMenuEvent inMenuEvent){
		String toUserName = inMenuEvent.getToUserName();
		String eventKey = inMenuEvent.getEventKey();
		if(toUserName.equals("gh_8ea89be7fde1")){
			if(eventKey.equals("lianxikefubanmawangguo")){
				OutCustomMsg out =new OutCustomMsg();
				out.setCreateTime(inMenuEvent.getCreateTime());
				out.setFromUserName(inMenuEvent.getToUserName());
				out.setToUserName(inMenuEvent.getFromUserName());
				out.setMsgType("transfer_customer_service");
				render(out);
			}
		}
	}
	
	public void SaveGh_34ea6d0a89ea(InMenuEvent inMenuEvent){
		String toUserName = inMenuEvent.getToUserName();
		String eventKey = inMenuEvent.getEventKey();
		
		if(toUserName.equals("gh_34ea6d0a89ea")){
			OutNewsMsg out =new OutNewsMsg( inMenuEvent);
			if(eventKey.equals("haixiandalianmeng")){
				News news1=new News();
				news1.setTitle("海鲜大联盟");
				news1.setDescription("海鲜主要是指海洋中的鱼、虾、蛋贝壳类等动物性食物，不仅种类多、好吃，而且营养特别丰富，包括蛋白质、不饱和脂肪");
				news1.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/sKu34dbPQwQuTjAWqPprmeVULGUDoYBpMrfDyiclqCRRDqsibOYo1iaQsibqw1iaf9mrd8e7VpzibGPOgUMHmSceQ3OA/0?wx_fmt=jpeg");
				news1.setUrl("http://mp.weixin.qq.com/s?__biz=MzA3NDI0NzM4OQ==&mid=400740715&idx=1&sn=4e12618783e3509ec8d4070fbfcbcfff&scene=18#rd");
				out.addNews(news1);
				render(out);
			}else if(eventKey.equals("chiladehaochu")){
				News news=new News();
				news.setTitle("吃辣的十大“坏处”");
				news.setDescription("你喜欢吃辣吗？还在担心吃辣椒对身体不好而不敢吃辣吗？让我们一起揭秘事情的真相！辣椒营养丰富，有许多保健作用。");
				news.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/sKu34dbPQwQuTjAWqPprmeVULGUDoYBp0w3vQ0Kfib6qibyQYJsq7FyMa06K9XNjLor0xHtHfsib8CFG4C5n4J8Jg/0?wx_fmt=jpeg");
				news.setUrl("http://mp.weixin.qq.com/s?__biz=MzA3NDI0NzM4OQ==&mid=400739894&idx=1&sn=060bb5c0b839d6322df5355d3a741fa3&scene=18#rd");
				out.addNews(news);
				render(out);
			}else if(eventKey.equals("wodehuiyuan")){
				News news=new News();
				news.setTitle("我的会员");
				news.setDescription("赶快加入我们成为会员，让您惊喜不断，还在等什么，“辣”么好 吃，你还忍得住吗？长按二维码关注我们官");
				news.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/sKu34dbPQwQuTjAWqPprmeVULGUDoYBpZonjBD05F3os3ycQG2jtBobN71bE8ce2SmCWUben7uSg7ia6Mo0eV5w/0?wx_fmt=jpeg");
				news.setUrl("http://mp.weixin.qq.com/s?__biz=MzA3NDI0NzM4OQ==&mid=400741116&idx=1&sn=30e53be70216da85c4a99570a6fa9c05&scene=18#rd");
				out.addNews(news);
				
				render(out);
			}else if(eventKey.equals("dazhuanpan")){
				News news=new News();
				news.setTitle("大转盘");
				news.setDescription("抽奖活动马上开始了，赶快加入我们吧！");
				news.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/sKu34dbPQwQuTjAWqPprmeVULGUDoYBpGUrYlh1x2eicXnhwvx4nicUnFfmKQOCfhcq9d9iaUCtmW1MAdGyOVyf9A/0?wx_fmt=jpeg");
				news.setUrl("http://mp.weixin.qq.com/s?__biz=MzA3NDI0NzM4OQ==&mid=400740856&idx=1&sn=a97de4bd50112d353a47a8513d0bf580&scene=18#rd");
				out.addNews(news);
				render(out);
			}else if(eventKey.equals("zaixianxiadan")){
				News news=new News();
				news.setTitle("在线下单");
				news.setDescription("在线下单立享优惠");
				news.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/sKu34dbPQwQuTjAWqPprmeVULGUDoYBp2bLtia0lZoUiaD14ldNXU2c1TKNvgicJgSWRFicENwaezHkBkSFjaTN61A/0?wx_fmt=jpeg");
				news.setUrl("http://mp.weixin.qq.com/s?__biz=MzA3NDI0NzM4OQ==&mid=400741239&idx=1&sn=68a56375e71014c60f3b195621473dd4&scene=18#rd");
				out.addNews(news);
				render(out);
			}
		}
	}
	
	private   void  saveGh_60d5ed2513f3(InMenuEvent inMenuEvent){
		String fromUserName = inMenuEvent.getToUserName();
		String eventKey = inMenuEvent.getEventKey();
		
//		if(fromUserName.equals("gh_75557554ec5c")){
		if(fromUserName.equals("gh_60d5ed2513f3")){
			OutNewsMsg out =new OutNewsMsg( inMenuEvent);
			if(eventKey.equals("shangyemoshi")){
				
				News news1=new News();
				news1.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5d4icicHjoWb20Af5jD3HVuaO3j27TQKqQ78eh6jEeRM9dTgAqBO8eRjP9lPpDvb4pgRUqPSvRVBiawg/0?wx_fmt=jpeg");
				news1.setTitle("全世界最好的商业模式汇总（企业家必收藏）");
				news1.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673066&idx=1&sn=a0ce9a2fd2d0adcc80c666fc0eb46c85#rd");
				out.addNews(news1);
				News news2=new News();
				news2.setTitle("【商业模式】150辆大巴车免费乘，但盈利却上亿。");
				news2.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5d4icicHjoWb20Af5jD3HVuaOyQfqS1zqVGf1tBibSR7GDpY21ZZokrzwg6HO8UStAj4w9Zb3419O64A/0?wx_fmt=jpeg");
				news2.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673066&idx=2&sn=0026e652ac43664170dd0973cfbba20b#rd");
				out.addNews(news2);
				News news3=new News();
				news3.setTitle("【商业模式】羊毛出在狗身上，猪来买单");
				news3.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5d4icicHjoWb20Af5jD3HVuaOsOFxFNUUjCGuvptTkKIsibhboicOlJeFZ6IfrmFWicSiarh2d77icVibMFAw/0?wx_fmt=jpeg");
				news3.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673066&idx=3&sn=87593f79dbee679065d7b4648447a0b8#rd");
				out.addNews(news3);
				
				News news4=new News();
				news4.setTitle("【商业模式】所谓“商业模式”，就是赚钱的方式");
				news4.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5d4icicHjoWb20Af5jD3HVuaOnnd6pJxLxzTKKYnrwyth8WGibNWU8oOODdQIHDIkTd9ibQBlFp5EcJYw/0?wx_fmt=jpeg");
				news4.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673066&idx=4&sn=c2b89dbc98263fc2675356553f8b0b68#rd");
				out.addNews(news4);
				
				News news6=new News();
				news6.setTitle("【商业模式】商业模式要满足人性的贪婪、懒惰、自私");
				news6.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5d4icicHjoWb20Af5jD3HVuaO0FfcfjmttPWwmjZyrib1MhNuEHLIgw2PVxV9ibxAThktwV2z3TekoBHw/0?wx_fmt=jpeg");
				news6.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673066&idx=5&sn=63c8f29e88fe194cd6f892c3aef5e22b#rd");
				out.addNews(news6);
				
				News news7=new News();
				news7.setTitle("【商业模式】\"小米模式\" 一道菜一年卖2个亿");
				news7.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5d4icicHjoWb20Af5jD3HVuaOX7rquDOjqMqiaS6J3j3Q6Bn0c9MQOyZic5SGC3VPS6v6ibUeSP6gQj3Pw/0?wx_fmt=jpeg");
				news7.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673066&idx=6&sn=233b0f243e0f11271da50f6051368589#rd");
				out.addNews(news7);
				
				News news8=new News();
				news8.setTitle("【商业模式】不收钱也能赚钱？“免费模式”");
				news8.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5d4icicHjoWb20Af5jD3HVuaO79TwS72eWNKJ4IiaIT0A5TxqZskHJ3cRgsFBicZDib1HpQR5HrHFmRUuA/0?wx_fmt=jpeg");
				news8.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673066&idx=7&sn=9bed26d13b84ad727c51cd088bc722ac#rd");
				out.addNews(news8);
				
				News news9=new News();
				news9.setTitle("【商业模式】不懂商业模式 企业就会被淘汰！");
				news9.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5d4icicHjoWb20Af5jD3HVuaO9fzSWSacnL8rkicNj1MceHruz2UzzpymgEolbewXjiaia0yrQ7TXiahliag/0?wx_fmt=jpeg");
				news9.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673066&idx=8&sn=0762f9debb16871bdf8c9296b08006fd#rd");
				out.addNews(news9);
				render(out);
			}else if(eventKey.equals("yingxiaosheji")){
				News news1=new News();
				news1.setTitle("【营销设计】收钱思维——如何让客户持续主动的交钱给你");
				news1.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5fPvQUgod2c8sjD9nREuKejMBbkvG2OwCFcLk64t11PyJZwtYHHlhDo8fiavJojdibqLTz14eLr0wwQ/0?wx_fmt=jpeg");
				news1.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673891&idx=1&sn=783b8c990ba26b2b319a013ca06b158a#rd");
				out.addNews(news1);
				
				News news2=new News();
				news2.setTitle("【营销设计】商业思维---看故事学营销的哲理(实用)");
				news2.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5fPvQUgod2c8sjD9nREuKejo23QnhSlibAtPTicYLRBBJxPldlcpxwGXeZFz6GibAnibicJib0NESxz9g1Q/0?wx_fmt=jpeg");
				news2.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673891&idx=2&sn=62f34df5488eca60698c713e9c2d6d0a#rd");
				out.addNews(news2);
				
				News news3=new News();
				news3.setTitle("【营销设计】营销与销售的区别，读懂了可以多赚一个");
				news3.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5fPvQUgod2c8sjD9nREuKejfF7TTJiaOic2qrqBbczibcMs7HqUtkeKNJ5JYBxiaE8PVhUGMVR4vn44ww/0?wx_fmt=jpeg");
				news3.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673891&idx=3&sn=4b4150ffe35004360da195ab2cf78603#rd");
				out.addNews(news3);
				

				News news5=new News();
				news5.setTitle("【营销设计】小品《卖拐》教你做销售(分析得太到");
				news5.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5fPvQUgod2c8sjD9nREuKejrk1ntKNcvUoImIDTbhzMLETNr6DAXW1M1rdeOudxv9eO4Mic1a9ntsg/0?wx_fmt=jpeg");
				news5.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673891&idx=4&sn=3cd74334f6220055956382af61da6361#rd");
				out.addNews(news5);
				
				News news6=new News();
				news6.setTitle("【营销设计】营销就是在泡妞，太TM经典了！");
				news6.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5fPvQUgod2c8sjD9nREuKejEq8ib9nYmicv3eDibB9ucnNnV6nH7EMpvcib9sTCeGOGcazc3JHWuiby8bg/0?wx_fmt=jpeg");
				news6.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673891&idx=5&sn=ea50a0cea76f18b05e07d9058dc79e15#rd");
				out.addNews(news6);
				
				News news7=new News();
				news7.setTitle("【营销设计】令人喷饭的营销方案 !确实牛!!!");
				news7.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5fPvQUgod2c8sjD9nREuKejqs4icHjYUIlEN8QlRicI6P1ichKthuibCr7ALGOwaar8VHusibWDh5WT6FA/0?wx_fmt=jpeg");
				news7.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673891&idx=6&sn=d89b4404161a404fb0c50403548a1216#rd");
				out.addNews(news7);
				
				News news8=new News();
				news8.setTitle("【终极营销】不走寻常路！一个农村小伙如何");
				news8.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5fPvQUgod2c8sjD9nREuKejf4TUDoB1VuZ6R6hztvGIR285OG5s8uOW9yMTq4lHelnb7bKaKwDl8g/0?wx_fmt=jpeg");
				news8.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673891&idx=7&sn=302a652f14004afef08ba648dd1c7860#rd");
				out.addNews(news8);
				
				News news4=new News();
				news4.setTitle("【营销设计】销售是取款机，话术是取款密码");
				news4.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5fPvQUgod2c8sjD9nREuKejYsia9NKeHqGtX1KHJbflrkJesnkiaxiaDUqeKibewahYz0kmPJVyLwxXibw/0?wx_fmt=jpeg");
				news4.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673891&idx=8&sn=717086be5e3055c800a375348986622e#rd");
				out.addNews(news4);
				
				render(out);
			}else{
				News news1=new News();
				news1.setTitle("【解放老板】如何才能解放老板	");
				news1.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5ceXibQwTbrLzjpic2fFbgQxHicAg8qPfIoULYSEA8C2jcuJ4mWPVvvoLJX066mQibSQicQWS0X22QG91A/0?wx_fmt=jpeg");
				news1.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400748143&idx=1&sn=0a0f2d7ca35c5911a1d127c4ba0a3b9e#rd");
				out.addNews(news1);
				
				News news2=new News();
				news2.setTitle("【解放老板】敢用人，敢分钱，敢分权！");
				news2.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5ceXibQwTbrLzjpic2fFbgQxHmflIgA9XQAoaW0BoBDjW31gEiaAFVSwsv3bgjb40Xia9TTFSO9NdcJRg/0?wx_fmt=jpeg");
				news2.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400748143&idx=2&sn=2a86e1a607a70c567f1e2ff23a7cf63e#rd");
				out.addNews(news2);
				
				News news3=new News();
				news3.setTitle("【解放老板】长江商学院100万内部资料！精髓全在	");
				news3.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5ceXibQwTbrLzjpic2fFbgQxHPkWRuEb1piaHqKiblgeHSPqdCBhQ6W6ib9t92Iadl7VaJibmcpbRdIEnoQ/0?wx_fmt=jpeg");
				news3.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400748143&idx=3&sn=cda3a55c89a3e3e213e8baca94d57578#rd");
				out.addNews(news3);
				

				News news5=new News();
				news5.setTitle("【解放老板】李云龙的部队无论走到哪里，都不会忘记");
				news5.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5ceXibQwTbrLzjpic2fFbgQxH29fE7vT0tjDrx3FbxnXKT0aljLT4tgCkreI7ibkQz1hB7mVzfjboo1g/0?wx_fmt=jpeg");
				news5.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400748143&idx=4&sn=15ae86e4b9e82aedaf5677aab86e5ddc#rd");
				out.addNews(news5);
				
				News news6=new News();
				news6.setTitle("【解放老板】老板越坏，员工越爱，公司越强！！！");
				news6.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5ceXibQwTbrLzjpic2fFbgQxHljD11Z7hiaOFOPORrqZpslMuwZDkyU6buZPZmPW6jVtHI3WfBYBc9TA/0?wx_fmt=jpeg");
				news6.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400748143&idx=5&sn=a7b4cff77f1c6554ad25ef72ee9f0d33#rd");
				out.addNews(news6);
				
				News news7=new News();
				news7.setTitle("【解放老板】弄明白员工为什么要跟你干	");
				news7.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5ceXibQwTbrLzjpic2fFbgQxHKvia1EKG1uv9QunZ4LBlBjwVs8r92yibu07jCW91DezDIghCRMnHtM7A/0?wx_fmt=jpeg");
				news7.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400748143&idx=6&sn=c62fe42bd990cbe91de680f19e4983a2#rd");
				out.addNews(news7);
				
				News news8=new News();
				news8.setTitle("【解放老板】猎狗的故事，源自哈佛大学的管理经");
				news8.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5ceXibQwTbrLzjpic2fFbgQxHYMgHX5UEs2KwW2j5iazRxEhchzElQywqibicvGicKSibDRFDXxrz2J1AXHQ/0?wx_fmt=jpeg");
				news8.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400748143&idx=7&sn=1ed93a92da0eb507c049c744899bb804#rd");
				out.addNews(news8);
				
				News news4=new News();
				news4.setTitle("【解放老板】做老板，如何利用别人的资源？");
				news4.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5ceXibQwTbrLzjpic2fFbgQxHiaSUxBNm7BmspmpL3TYlH9F7zxLNM9MslzAXM6TndMw7XcSfvWuWYpg/0?wx_fmt=jpeg");
				news4.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400748143&idx=8&sn=b9764816e73cfc7a737731e35f5e9a55#rd");
				out.addNews(news4);
				
				render(out);
			}
		}
	}
	
	private  void  caidan(InMenuEvent inMenuEvent){
		String fromUserName = inMenuEvent.getToUserName();
		String eventKey = inMenuEvent.getEventKey();
		if(fromUserName.equals("gh_60d5ed2513f3")){
			//钱是这样赚的菜单事件
			OutNewsMsg out =new OutNewsMsg( inMenuEvent);
			if(eventKey.equals("shangyemoshi")){
				News news1=new News();
				news1.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5d4icicHjoWb20Af5jD3HVuaO3j27TQKqQ78eh6jEeRM9dTgAqBO8eRjP9lPpDvb4pgRUqPSvRVBiawg/0?wx_fmt=jpeg");
				news1.setTitle("全世界最好的商业模式汇总（企业家必收藏）");
				news1.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673066&idx=1&sn=a0ce9a2fd2d0adcc80c666fc0eb46c85#rd");
				out.addNews(news1);
				News news2=new News();
				news2.setTitle("【商业模式】150辆大巴车免费乘，但盈利却上亿。");
				news2.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5d4icicHjoWb20Af5jD3HVuaOyQfqS1zqVGf1tBibSR7GDpY21ZZokrzwg6HO8UStAj4w9Zb3419O64A/0?wx_fmt=jpeg");
				news2.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673066&idx=2&sn=0026e652ac43664170dd0973cfbba20b#rd");
				out.addNews(news2);
				News news3=new News();
				news3.setTitle("【商业模式】羊毛出在狗身上，猪来买单");
				news3.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5d4icicHjoWb20Af5jD3HVuaOsOFxFNUUjCGuvptTkKIsibhboicOlJeFZ6IfrmFWicSiarh2d77icVibMFAw/0?wx_fmt=jpeg");
				news3.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673066&idx=3&sn=87593f79dbee679065d7b4648447a0b8#rd");
				out.addNews(news3);

				News news4=new News();
				news4.setTitle("【商业模式】所谓“商业模式”，就是赚钱的方式");
				news4.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5d4icicHjoWb20Af5jD3HVuaOnnd6pJxLxzTKKYnrwyth8WGibNWU8oOODdQIHDIkTd9ibQBlFp5EcJYw/0?wx_fmt=jpeg");
				news4.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673066&idx=4&sn=c2b89dbc98263fc2675356553f8b0b68#rd");
				out.addNews(news4);

				News news6=new News();
				news6.setTitle("【商业模式】商业模式要满足人性的贪婪、懒惰、自私");
				news6.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5d4icicHjoWb20Af5jD3HVuaO0FfcfjmttPWwmjZyrib1MhNuEHLIgw2PVxV9ibxAThktwV2z3TekoBHw/0?wx_fmt=jpeg");
				news6.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673066&idx=5&sn=63c8f29e88fe194cd6f892c3aef5e22b#rd");
				out.addNews(news6);

				News news7=new News();
				news7.setTitle("【商业模式】\"小米模式\" 一道菜一年卖2个亿");
				news7.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5d4icicHjoWb20Af5jD3HVuaOX7rquDOjqMqiaS6J3j3Q6Bn0c9MQOyZic5SGC3VPS6v6ibUeSP6gQj3Pw/0?wx_fmt=jpeg");
				news7.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673066&idx=6&sn=233b0f243e0f11271da50f6051368589#rd");
				out.addNews(news7);

				News news8=new News();
				news8.setTitle("【商业模式】不收钱也能赚钱？“免费模式”");
				news8.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5d4icicHjoWb20Af5jD3HVuaO79TwS72eWNKJ4IiaIT0A5TxqZskHJ3cRgsFBicZDib1HpQR5HrHFmRUuA/0?wx_fmt=jpeg");
				news8.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673066&idx=7&sn=9bed26d13b84ad727c51cd088bc722ac#rd");
				out.addNews(news8);

				News news9=new News();
				news9.setTitle("【商业模式】不懂商业模式 企业就会被淘汰！");
				news9.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5d4icicHjoWb20Af5jD3HVuaO9fzSWSacnL8rkicNj1MceHruz2UzzpymgEolbewXjiaia0yrQ7TXiahliag/0?wx_fmt=jpeg");
				news9.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673066&idx=8&sn=0762f9debb16871bdf8c9296b08006fd#rd");
				out.addNews(news9);
				render(out);
			}else if(eventKey.equals("yingxiaosheji")){
				News news1=new News();
				news1.setTitle("【营销设计】收钱思维——如何让客户持续主动的交钱给你");
				news1.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5fPvQUgod2c8sjD9nREuKejMBbkvG2OwCFcLk64t11PyJZwtYHHlhDo8fiavJojdibqLTz14eLr0wwQ/0?wx_fmt=jpeg");
				news1.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673891&idx=1&sn=783b8c990ba26b2b319a013ca06b158a#rd");
				out.addNews(news1);

				News news2=new News();
				news2.setTitle("【营销设计】商业思维---看故事学营销的哲理(实用)");
				news2.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5fPvQUgod2c8sjD9nREuKejo23QnhSlibAtPTicYLRBBJxPldlcpxwGXeZFz6GibAnibicJib0NESxz9g1Q/0?wx_fmt=jpeg");
				news2.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673891&idx=2&sn=62f34df5488eca60698c713e9c2d6d0a#rd");
				out.addNews(news2);

				News news3=new News();
				news3.setTitle("【营销设计】营销与销售的区别，读懂了可以多赚一个");
				news3.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5fPvQUgod2c8sjD9nREuKejfF7TTJiaOic2qrqBbczibcMs7HqUtkeKNJ5JYBxiaE8PVhUGMVR4vn44ww/0?wx_fmt=jpeg");
				news3.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673891&idx=3&sn=4b4150ffe35004360da195ab2cf78603#rd");
				out.addNews(news3);


				News news5=new News();
				news5.setTitle("【营销设计】小品《卖拐》教你做销售(分析得太到");
				news5.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5fPvQUgod2c8sjD9nREuKejrk1ntKNcvUoImIDTbhzMLETNr6DAXW1M1rdeOudxv9eO4Mic1a9ntsg/0?wx_fmt=jpeg");
				news5.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673891&idx=4&sn=3cd74334f6220055956382af61da6361#rd");
				out.addNews(news5);

				News news6=new News();
				news6.setTitle("【营销设计】营销就是在泡妞，太TM经典了！");
				news6.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5fPvQUgod2c8sjD9nREuKejEq8ib9nYmicv3eDibB9ucnNnV6nH7EMpvcib9sTCeGOGcazc3JHWuiby8bg/0?wx_fmt=jpeg");
				news6.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673891&idx=5&sn=ea50a0cea76f18b05e07d9058dc79e15#rd");
				out.addNews(news6);

				News news7=new News();
				news7.setTitle("【营销设计】令人喷饭的营销方案 !确实牛!!!");
				news7.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5fPvQUgod2c8sjD9nREuKejqs4icHjYUIlEN8QlRicI6P1ichKthuibCr7ALGOwaar8VHusibWDh5WT6FA/0?wx_fmt=jpeg");
				news7.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673891&idx=6&sn=d89b4404161a404fb0c50403548a1216#rd");
				out.addNews(news7);

				News news8=new News();
				news8.setTitle("【终极营销】不走寻常路！一个农村小伙如何");
				news8.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5fPvQUgod2c8sjD9nREuKejf4TUDoB1VuZ6R6hztvGIR285OG5s8uOW9yMTq4lHelnb7bKaKwDl8g/0?wx_fmt=jpeg");
				news8.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673891&idx=7&sn=302a652f14004afef08ba648dd1c7860#rd");
				out.addNews(news8);

				News news4=new News();
				news4.setTitle("【营销设计】销售是取款机，话术是取款密码");
				news4.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5fPvQUgod2c8sjD9nREuKejYsia9NKeHqGtX1KHJbflrkJesnkiaxiaDUqeKibewahYz0kmPJVyLwxXibw/0?wx_fmt=jpeg");
				news4.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400673891&idx=8&sn=717086be5e3055c800a375348986622e#rd");
				out.addNews(news4);

				render(out);
			}else{
				News news1=new News();
				news1.setTitle("【解放老板】如何才能解放老板	");
				news1.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5ceXibQwTbrLzjpic2fFbgQxHicAg8qPfIoULYSEA8C2jcuJ4mWPVvvoLJX066mQibSQicQWS0X22QG91A/0?wx_fmt=jpeg");
				news1.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400748143&idx=1&sn=0a0f2d7ca35c5911a1d127c4ba0a3b9e#rd");
				out.addNews(news1);

				News news2=new News();
				news2.setTitle("【解放老板】敢用人，敢分钱，敢分权！");
				news2.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5ceXibQwTbrLzjpic2fFbgQxHmflIgA9XQAoaW0BoBDjW31gEiaAFVSwsv3bgjb40Xia9TTFSO9NdcJRg/0?wx_fmt=jpeg");
				news2.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400748143&idx=2&sn=2a86e1a607a70c567f1e2ff23a7cf63e#rd");
				out.addNews(news2);

				News news3=new News();
				news3.setTitle("【解放老板】长江商学院100万内部资料！精髓全在	");
				news3.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5ceXibQwTbrLzjpic2fFbgQxHPkWRuEb1piaHqKiblgeHSPqdCBhQ6W6ib9t92Iadl7VaJibmcpbRdIEnoQ/0?wx_fmt=jpeg");
				news3.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400748143&idx=3&sn=cda3a55c89a3e3e213e8baca94d57578#rd");
				out.addNews(news3);


				News news5=new News();
				news5.setTitle("【解放老板】李云龙的部队无论走到哪里，都不会忘记");
				news5.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5ceXibQwTbrLzjpic2fFbgQxH29fE7vT0tjDrx3FbxnXKT0aljLT4tgCkreI7ibkQz1hB7mVzfjboo1g/0?wx_fmt=jpeg");
				news5.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400748143&idx=4&sn=15ae86e4b9e82aedaf5677aab86e5ddc#rd");
				out.addNews(news5);

				News news6=new News();
				news6.setTitle("【解放老板】老板越坏，员工越爱，公司越强！！！");
				news6.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5ceXibQwTbrLzjpic2fFbgQxHljD11Z7hiaOFOPORrqZpslMuwZDkyU6buZPZmPW6jVtHI3WfBYBc9TA/0?wx_fmt=jpeg");
				news6.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400748143&idx=5&sn=a7b4cff77f1c6554ad25ef72ee9f0d33#rd");
				out.addNews(news6);

				News news7=new News();
				news7.setTitle("【解放老板】弄明白员工为什么要跟你干	");
				news7.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5ceXibQwTbrLzjpic2fFbgQxHKvia1EKG1uv9QunZ4LBlBjwVs8r92yibu07jCW91DezDIghCRMnHtM7A/0?wx_fmt=jpeg");
				news7.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400748143&idx=6&sn=c62fe42bd990cbe91de680f19e4983a2#rd");
				out.addNews(news7);

				News news8=new News();
				news8.setTitle("【解放老板】猎狗的故事，源自哈佛大学的管理经");
				news8.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5ceXibQwTbrLzjpic2fFbgQxHYMgHX5UEs2KwW2j5iazRxEhchzElQywqibicvGicKSibDRFDXxrz2J1AXHQ/0?wx_fmt=jpeg");
				news8.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400748143&idx=7&sn=1ed93a92da0eb507c049c744899bb804#rd");
				out.addNews(news8);

				News news4=new News();
				news4.setTitle("【解放老板】做老板，如何利用别人的资源？");
				news4.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/NhsQHgu3H5ceXibQwTbrLzjpic2fFbgQxHiaSUxBNm7BmspmpL3TYlH9F7zxLNM9MslzAXM6TndMw7XcSfvWuWYpg/0?wx_fmt=jpeg");
				news4.setUrl("http://mp.weixin.qq.com/s?__biz=MzIyNTA3Njg2Mw==&mid=400748143&idx=8&sn=b9764816e73cfc7a737731e35f5e9a55#rd");
				out.addNews(news4);

				render(out);
			}
		}else{

			//物流系统公众平台菜单事件
			OutTextMsg otm=new OutTextMsg(inMenuEvent);
			String key=inMenuEvent.getEventKey();
			if(key.equals("货物跟踪")){
				otm.setContent("欢迎查询运单物流状态，请直接输入完整票号信息，如：J30033-2即可");
			}

			if(key.equals("我要询价")){
				otm.setContent("请您提供需要询价的物品名称、物品图片、运输线路、物品密度等信息，以便准确给您回复价格，也可以直接咨询电话18888820391，谢谢！");
			}

			if(key.equals("联系我们")){
				otm.setContent("服务热线：18888820391");
			}


			if(key.equals("业务范围")){
				otm.setContent("欢迎关注京士森533库房，我们为您提供到俄罗斯及其他国家的陆运、空运、海铁运输清关服务、代收货款服务以及仓储配送服务。服务范围有大百和小百：服装鞋帽，箱包首饰，五金洁具，电子产品，机械设备等上千种商品。所有报关、报检、等一系列相关出口，进口资料都由我司提供专业服务。");
			}

			if(key.equals("常见问题")){
				otm.setContent("有任何问题请联系电话18888820391，王小姐。");
			}

			if(key.equals("公司动态")){
				otm.setContent("我公司主要经营中国至俄罗斯小百货块陆运,海铁白关双清服务!客户的满意就是我们的目标!");
			}

			if(key.equals("特大喜讯")){
				otm.setContent("中国-莫斯科 海铁纯白关 ，全力加速！9月20号装箱布料，10月22日分货！持续收货中！感谢过程中所有部门的全力配合！感谢广大客户的信任！ 有需要的客户请随时联系我们，鼎力为您服务。");
			}
			render(otm);
		}
	}
	private void mm(InQrCodeEvent inQrCodeEvent){
		String openId=inQrCodeEvent.getFromUserName();
		
		String name=inQrCodeEvent.getToUserName();
		List<GetPingtaiModel> findPT = GetPingtaiModel.dao.findPT(name);
		JobMoneyModel fm2 = JobMoneyModel.dao.findMoney(name);
		Double fm = fm2.getDouble("money");
		int adId= fm2.getInt("adId");
		Double money=0.0;
		for(int i=0;i<findPT.size();i++){
			GetPingtaiModel getPingtaiModel = findPT.get(i);
			Smrecord findMoney = 
					Smrecord.dao.findMoney(getPingtaiModel.getStr("publicaccountname"));
			money=findMoney.getDouble("money");
		}
		if(fm<0){
			GetPingtaiModel.dao.update(adId);
		}
		System.out.println("333");
		//查询平台回复语
				CaidanModel findHuifuyu = 
						CaidanModel.dao.findHuifuyu(name);
						String type=findHuifuyu.get("newstype").toString();
						System.out.println(type);
		if(type.equals("0")){
			System.out.println("444");
			String huifuyu=findHuifuyu.get("huifuyu");
			gg(inQrCodeEvent, huifuyu);
		}else if(type.equals("1")){
			
			String title=findHuifuyu.get("title");
			String miaoshu=findHuifuyu.get("miaoshu");
			String imgurl=findHuifuyu.get("imgurl");
			System.out.println("这是新闻图片地址"+imgurl);
			String newsurl=findHuifuyu.get("newsurl");
			gg(inQrCodeEvent, title, miaoshu, imgurl, newsurl);
		}
		try {
			for(int i=0;i<5;i++){
				List<GetPingtaiModel> getps=GetPingtaiModel.dao.find("select appid,appsecret from publicaccount where publicaccountname='"+name+"'");
				System.out.println("平台名："+name+"\nappid："+getps.get(0).getStr("appid")+"\nappsecret"+getps.get(0).getStr("appsecret"));
				String access_token=access_token(getps.get(0).getStr("appid"),getps.get(0).getStr("appsecret"));
				String json=HttpKit.get
					("https://api.weixin.qq.com/cgi-bin/user/info?access_token="+
							access_token+"&openid="+openId+"&lang=zh_CN");
				JSONObject o= JSONObject.fromObject(json);
				System.out.println("李望获取的access_token: "+access_token);
				System.out.println("李望从微信端获取的json结果： "+json);
				if(o.containsKey("errcode") || o.getInt("subscribe")!=1){
					System.out.println("李望 第 "+i+" 次查询用户头像昵称信息失败！");
					continue;
				}
				Db.update("update smrecord set attpeople=?,headimgurl=? where attpeopleno=?", 
						"".equals(o.getString("nickname"))?"佚名":o.getString("nickname"),
						"".equals(o.getString("headimgurl"))?
								"images/defheadimg.jpg"
								:o.getString("headimgurl"),
								openId);
				System.out.println("执行完数据库修改...");
				break;
			}
		} catch (Exception e) {
			System.out.println("抛出异常获取头像或昵称失败!");
			e.printStackTrace();
		}
		System.out.println("weixinController.processInQrCodeEvent() 方法执行完毕...");
	}
	private void nn(InFollowEvent inFollowEvent){
		if (inFollowEvent.EVENT_INFOLLOW_UNSUBSCRIBE.equals(inFollowEvent.getEvent())) {
			logger.debug("扫码将取消关注：" + inFollowEvent.getFromUserName());
			
			System.out.println("参数"+inFollowEvent.getEvent());
			//进行数据取消关注事件的变更
			
			Saomajilu.dao.canclesm(inFollowEvent);
			renderOutTextMsg("");
		}
	}
}
