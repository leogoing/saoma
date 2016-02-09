package controller.kaihuine;


import java.io.IOException;
import java.text.ParseException;

import org.json.JSONObject;

import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.mysql.jdbc.UpdatableResultSet;
import com.utils.erweima.DuiBiTime;
import com.utils.weixin.UpdateWeixinInPublicaccount;
import com.utils.weixin.util_EWM;
import com.utils.weixin.util_access_token;

import util.GetTime;


public class KaihuineInterface extends Controller{
	
	
	public void index(){
		int type=getParaToInt("type");
		if(type==1){
			String AppSecret=getPara("AppSecret");
			String name=getPara("name");
			
			KAIHUINE_access_tokenModel findPT = KAIHUINE_access_tokenModel.dao.findPT(name, AppSecret);
			if(findPT!=null){
				String appid=findPT.get("appid");
				Object object = findPT.get("token_time");
				String token_time=object.toString();
				boolean duibi =false;
				try {
					duibi = DuiBiTime.duibi(GetTime.getTime(), token_time);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String access_token = findPT.getStr("access_token");
				if(duibi){
					try {
						access_token = util_access_token.getAccess_token(appid, AppSecret);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					KAIHUINE_access_tokenModel.dao.updateAccess_token(appid, AppSecret, access_token, GetTime.getTime());
				}
				renderJson(access_token);
			}else{
				renderJson("Your validation did not pass the correct data.");
			}
			
		}else{
			renderJson("");
		}
		
	}
}
