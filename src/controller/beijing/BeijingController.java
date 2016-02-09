package controller.beijing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import model.GetPingtaiModel;
import model.PingtaiTupianModel;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.utils.ImageCompose;
import com.utils.StringUtils;

public class BeijingController extends Controller{
	public void index(){
		String erweima=getPara("erweima");
		int id=getParaToInt("pingtaiid");
		List<PingtaiTupianModel> list = PingtaiTupianModel.dao.findTupian(id);
		List<HashMap<String, String>> ls= new ArrayList<HashMap<String, String>>();
		List<Record> lls= new ArrayList<Record>();
		String nn="";
		for(int i=0;i<list.size();i++){
			PingtaiTupianModel pingtaiTupian = list.get(i);
			String tupianURL = pingtaiTupian.get("pictureURL").toString();
			String compose=null;
			GetPingtaiModel findpingtai = 
					GetPingtaiModel.dao.findpingtai(id);
			
			String appId=findpingtai.get("appid");
			String appsecret=findpingtai.get("appsecret");
			String Token=findpingtai.get("token");
			String publicaccountname=findpingtai.get("publicaccountname");
			int userID=getSessionAttr("currentUserId");
//			String linshiEWM = GetEWM.linshiEWM(appId, appsecret, Token,  userID, publicaccountname);
			String id2=id+"";
				compose = ImageCompose.Compose(erweima,tupianURL, id2);
			String substringAfterLast = StringUtils.substringAfter(compose, "/");
			nn=StringUtils.substringBefore(compose, "/")+"\\\\";
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("URL", substringAfterLast);
//			ls.add(map);
			Record r = new Record();
			r.set("URL", substringAfterLast);
			r.set("num", i+1);
			lls.add(r);
			System.out.println("这是截取后的URL："+substringAfterLast);
			System.out.println("这是前面的路径："+nn);
		}
		setAttr("count", list.size());
		setAttr("nn", nn);
		setAttr("list", lls);
		render("/gerendailishang/xuanchuantu.html");
	}
	
}
