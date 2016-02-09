/*package com.weixin;

import model.Saomajilu;
import model.weixin.CaidanModel;

import com.jfinal.aop.Clear;
import com.jfinal.log.Logger;
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
import com.jfinal.weixin.sdk.msg.in.event.InTemplateMsgEvent;
import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;

public class WeixinController extends MsgController{
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

	@Override
	protected void processInTextMsg(InTextMsg inTextMsg) {
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
		
		if (inFollowEvent.EVENT_INFOLLOW_UNSUBSCRIBE.equals(inFollowEvent.getEvent())) {
			logger.debug("扫码将取消关注：" + inFollowEvent.getFromUserName());
			
			System.out.println("参数"+inFollowEvent.getEvent());
			//进行数据取消关注事件的变更
			
			Saomajilu.dao.canclesm(inFollowEvent);
		}
		renderOutTextMsg("");
	}

	@Override
	@Clear
	protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent) {
		String name=inQrCodeEvent.getToUserName().toString();
		String findHuifuyu = CaidanModel.dao.findHuifuyu(name);
		System.out.println(findHuifuyu);
		if(findHuifuyu.equals("无") || findHuifuyu.equals("") || findHuifuyu==null){
			findHuifuyu="感谢您关注";
		}
		
		//判断是不是第一次关注，是：进入这个
		if (InQrCodeEvent.EVENT_INQRCODE_SUBSCRIBE.equals(inQrCodeEvent.getEvent())) {
			logger.debug("扫码未关注：" + inQrCodeEvent.getFromUserName());
			
			//System.out.println("参数"+inQrCodeEvent.getEventKey().substring(8));
			//进行数据保存的操作，保存在smrecord表中
			//扫码时间、关注人、关注时间、取关时间、关注平台、市级所属、市级获利、县级所属、县级获利、
			//个人用户名、个人获利、扫码平台、是否提现（默认未提现）、代理商等级
			
			Saomajilu.dao.smsave(inQrCodeEvent);//将参数传入Saomajilu中
			OutTextMsg outMsg = new OutTextMsg(inQrCodeEvent);
		}
		//判断是不是已经关注，是：进入这个
		if (InQrCodeEvent.EVENT_INQRCODE_SCAN.equals(inQrCodeEvent.getEvent())) {
			System.out.println("333");
			logger.debug("扫码已关注：" + inQrCodeEvent.getFromUserName());
		}
		OutTextMsg outMsg = new OutTextMsg(inQrCodeEvent);
		outMsg.setContent(findHuifuyu);
		render(outMsg);
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
		// TODO Auto-generated method stub

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

}
*/