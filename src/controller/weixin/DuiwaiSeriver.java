package controller.weixin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.GetPingtaiModel;
import model.JobMoneyModel;
import model.Saomajilu;
import model.gly.Smrecord;
import model.pingtai.IPModel;

import com.jfinal.core.Controller;
import com.utils.entity.WailaiG;
import com.utils.entity.WailaiQ;
import com.utils.weixin.GetIP;

public class DuiwaiSeriver extends Controller{
	public void index(){
		HttpServletRequest re = getRequest();
		String IP = GetIP.getIpAddress(re);
		
		List<IPModel> findIP = IPModel.dao.findIP();
		for(int i=0;i<findIP.size();i++){
			IPModel ipModel = findIP.get(i);
			String ip = ipModel.get("serverIP").toString();
			if(ip.equals("ipAddress")){
				String para = getPara("type");
				if(para.equals("1")){
					WailaiG wag =new WailaiG();
					wag.setCreateTime(getParaToInt("createTime"));
					wag.setEvent(getPara("event"));
					wag.setEventKey(getPara("eventKey"));
					wag.setFromUserName(getPara("fromUserName"));
					wag.setMsgType(getPara("msgType"));
					wag.setTicket(getPara("ticket"));
					wag.setToUserName(getPara("toUserName"));
					Saomajilu.dao.smsave(wag);
					String name=wag.getToUserName();
					List<GetPingtaiModel> findPT = GetPingtaiModel.dao.findPT(name);
					JobMoneyModel fm2 = JobMoneyModel.dao.findMoney(name);
					Double fm = fm2.getDouble("money");
					int adId= fm2.getInt("adId");
					Double money=0.0;
					for(int j=0;j<findPT.size();j++){
						GetPingtaiModel getPingtaiModel = findPT.get(i);
						Smrecord findMoney = 
								Smrecord.dao.findMoney(getPingtaiModel.getStr("publicaccountname"));
						money=findMoney.getDouble("money");
					}
					if(fm<0){
						GetPingtaiModel.dao.update(adId);
					}
					return;
				}else if(para.equals("0")){
					WailaiQ waq=new WailaiQ();
					waq.setCreateTime(getParaToInt("createTime"));
					waq.setEvent(getPara("event"));
					waq.setFromUserName(getPara("fromUserName"));
					waq.setMsgType(getPara("msgType"));
					waq.setToUserName(getPara("toUserName"));
					weixinController.logger.debug("扫码将取消关注：" + waq.getFromUserName());
					Saomajilu.dao.canclesm(waq);
					return;
				}else if(para.equals("3")){
					String access_token = getPara("access_token");
					IPModel.dao.updateToken(access_token, IP);
					return;
				}
			}
		}
		renderText("");
	}
}
