package hahada.controller;

import java.util.List;

import jodd.madvoc.injector.ScopeData.Out;
import hahada.model.HuifuModel;
import hahada.model.HuifuNewsModel;

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
import com.jfinal.weixin.sdk.msg.out.OutImageMsg;
import com.jfinal.weixin.sdk.msg.out.OutVideoMsg;
import com.jfinal.weixin.sdk.msg.out.OutVoiceMsg;

public class WeixinController extends MsgController{

	@Override
	public ApiConfig getApiConfig() {
		ApiConfig ac =new ApiConfig();
		ac.setAppId("wx4c03cb8e98fed668");
		ac.setAppSecret("609e87380c35c29fa67cd8837cf63b9b");
		ac.setToken("weixin");
		return ac;
	}

	@Override
	protected void processInTextMsg(InTextMsg inTextMsg) {
		
		
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
	protected void processInFollowEvent(InFollowEvent inFollowEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent) {
		// TODO Auto-generated method stub
		
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
		String eventKey = inMenuEvent.getEventKey();
		System.out.println(eventKey);
		List<HuifuNewsModel> findDingyiCaidan = HuifuNewsModel.dao.findDingyiCaidan(eventKey);
		if(findDingyiCaidan==null){
			System.out.println("333");
			HuifuModel findHuifu = HuifuModel.dao.findHuifu(eventKey);
			if(findHuifu!=null){
				System.out.println("44");
				String nn= findHuifu.get("type").toString();
				System.out.println(nn);
				if(nn.equals("1")){
					String mm = findHuifu.get("text");
					renderOutTextMsg(mm);
					return;
				}else if(nn.equals("2")){
					String mm = findHuifu.get("img");
					OutImageMsg out =new OutImageMsg(inMenuEvent);
					out.setMediaId(mm);
					render(out);
					return;
				}else if(nn.equals("3")){
					String mm = findHuifu.get("voiceurl");
					System.out.println(mm);
					OutImageMsg out =new OutImageMsg(inMenuEvent);
					out.setMediaId(mm);
					render(out);
					return ;
				}else{
					String mm = findHuifu.get("videourl");
					OutVideoMsg out =new  OutVideoMsg(inMenuEvent);
					out.setMediaId(mm);
					render(out);
					return ;
				}
			}
			renderOutTextMsg("服务器维护中");
		}
		
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
