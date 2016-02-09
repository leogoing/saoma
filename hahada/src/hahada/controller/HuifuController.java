package hahada.controller;


import hahada.model.DingyiTypeModel;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.google.gson.stream.JsonReader;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.render.JsonRender;
import com.jfinal.upload.UploadFile;
import com.jfinal.weixin.sdk.kit.IpKit;

public class HuifuController extends Controller{
	private  static final String caidantou="{\"button\": [";
	private  static final String caidanwei="]}";
	private static final String caidanname="\"name\":\"";
	
	public void wangyuning(){
		String zhucai1name = getPara("zhucai1name");
		String zhucai1type=getPara("zhucai1type");
		String zhucai1value = getPara("zhucai1value");
		String zhucai1zi1name = getPara("zhucai1zi1name");
		String zhucai1zi2name = getPara("zhucai1zi2name");
		String zhucai1zi3name = getPara("zhucai1zi3name");
		String zhucai1zi4name = getPara("zhucai1zi4name");
		String zhucai1zi5name = getPara("zhucai1zi5name");
		String caidan="";
		String caidan1="";
		String caidan2="";
		String caidan3="";
		if((zhucai1zi1name.equals("+") ||  zhucai1zi1name.equals(" ") )&& (zhucai1zi2name.equals("+") || zhucai1zi2name.equals(" "))
				&& (zhucai1zi3name.equals("+") ||  zhucai1zi3name.equals(" "))
				&& (zhucai1zi4name.equals("+") ||zhucai1zi4name.equals(" "))&& 
				(zhucai1zi5name.equals("+") ||  zhucai1zi5name.equals(" "))){
			String totoString = toStringType(zhucai1type);
			String stringKey = toStringKey(zhucai1type);
			String saveText = saveText(zhucai1type, zhucai1value);
			caidan1=totoString+zhucai1name+stringKey+saveText+"\"}";
		}else{
			caidan1="{\"name\":\""+zhucai1name+"\",\"sub_button\": [";
			String zi1="";
			if(!zhucai1zi1name.equals(" ") && !zhucai1zi1name.equals("+")){
				String zhucai1zi1type = getPara("zhucai1zi1type");
				String zhucai1zi1value = getPara("zhucai1zi1value");
				String stringType = toStringType(zhucai1zi1type);
				String stringKey = toStringKey(zhucai1zi1type);
				String saveText = saveText(zhucai1zi1type,zhucai1zi1value);
				zi1=stringType+zhucai1zi1name+stringKey+saveText+"\"}";
			}
			String zi2="";
			if(!zhucai1zi2name.equals(" ") && !zhucai1zi2name.equals("+")){
				String zhucai1zi2type = getPara("zhucai1zi2type");
				String zhucai1zi2value = getPara("zhucai1zi2value");
				String stringType = toStringType(zhucai1zi2type);
				String stringKey = toStringKey(zhucai1zi2type);
				String saveText = saveText(zhucai1zi2type, zhucai1zi2value);
				zi2=","+stringType+zhucai1zi2name+stringKey+saveText+"\"}";
			}
			String zi3="";
			if(!zhucai1zi3name.equals(" ") && !zhucai1zi3name.equals("+")){
				String zhucai1zi3type = getPara("zhucai1zi3type");
				String zhucai1zi3value = getPara("zhucai1zi3value");
				String stringType = toStringType(zhucai1zi3type);
				String stringKey = toStringKey(zhucai1zi3type);
				String saveText = saveText(zhucai1zi3type, zhucai1zi3value);
				zi3=","+stringType+zhucai1zi3name+stringKey+saveText+"\"}";
			}
			String zi4="";
			if(!zhucai1zi4name.equals(" ") && !zhucai1zi4name.equals("+")){
				String zhucai1zi4type = getPara("zhucai1zi4type");
				String zhucai1zi4value = getPara("zhucai1zi4value");
				String stringType = toStringType(zhucai1zi4type);
				String stringKey = toStringKey(zhucai1zi4type);
				String saveText = saveText(zhucai1zi4type, zhucai1zi4value);
				zi4=","+stringType+zhucai1zi4name+stringKey+saveText+"\"}";
			}
			String zi5="";
			if(!zhucai1zi5name.equals(" ") && !zhucai1zi5name.equals("+")){
				String zhucai1zi5type = getPara("zhucai1zi5type");
				String zhucai1zi5value = getPara("zhucai1zi5value");
				String stringType = toStringType(zhucai1zi5type);
				String stringKey = toStringKey(zhucai1zi5type);
				String saveText = saveText(zhucai1zi5type, zhucai1zi5value);
				zi5=","+stringType+zhucai1zi5name+stringKey+saveText+"\"}";
			}
			caidan1+=zi1+zi2+zi3+zi4+zi5+"]}";
		}
		
		String zhucai2name = getPara("zhucai2name");
		String zhucai2type=getPara("zhucai2type");
		String zhucai2value = getPara("zhucai2value");
		String zhucai2zi1name = getPara("zhucai2zi1name");
		String zhucai2zi2name = getPara("zhucai2zi2name");
		String zhucai2zi3name = getPara("zhucai2zi3name");
		String zhucai2zi4name = getPara("zhucai2zi4name");
		String zhucai2zi5name = getPara("zhucai2zi5name");
		
		if((zhucai2zi1name.equals("+") ||  zhucai2zi1name.equals(" ") )&& (zhucai2zi2name.equals("+") || zhucai2zi2name.equals(" "))
				&& (zhucai2zi3name.equals("+") ||  zhucai2zi3name.equals(" "))
				&& (zhucai2zi4name.equals("+") ||zhucai2zi4name.equals(" "))&& 
				(zhucai2zi5name.equals("+") ||  zhucai2zi5name.equals(" "))){
			if(!zhucai2name.equals(" ") && !zhucai2name.equals("+")){
				System.out.println(zhucai2name);
				String stringType = toStringType(zhucai2type);
				String stringKey = toStringKey(zhucai2type);
				String saveText = saveText(zhucai2type, zhucai2value);
				caidan2=","+stringType+zhucai2name+stringKey+saveText+"\"}";
			}
		}else{
			
			String zi1="";
			caidan2=",{\"name\":\""+zhucai1name+"\",\"sub_button\": [";
			if(!zhucai2zi1name.equals(" ") && !zhucai2zi1name.equals("+")){
				String zhucai2zi1type = getPara("zhucai2zi1type");
				String zhucai2zi1value = getPara("zhucai2zi1value");
				String stringType = toStringType(zhucai2zi1type);
				String stringKey = toStringKey(zhucai2zi1type);
				String saveText = saveText(zhucai2zi1type, zhucai2zi1value);
				zi1=stringType+zhucai2zi1name+stringKey+saveText+"\"}";
			}
			String zi2="";
			if(!zhucai2zi2name.equals(" ") && !zhucai2zi2name.equals("+")){
				String zhucai2zi2type = getPara("zhucai2zi2type");
				String zhucai2zi2value = getPara("zhucai2zi2value");
				String stringType = toStringType(zhucai2zi2type);
				String stringKey = toStringKey(zhucai2zi2type);
				String saveText = saveText(zhucai2zi2type, zhucai2zi2value);
				zi2=","+stringType+zhucai2zi2name+stringKey+saveText+"\"}";
			}
			String zi3="";
			if(!zhucai2zi3name.equals(" ") && !zhucai2zi3name.equals("+")){
				String zhucai2zi3type = getPara("zhucai2zi3type");
				String zhucai2zi3value = getPara("zhucai2zi3value");
				String stringType = toStringType(zhucai2zi3type);
				String stringKey = toStringKey(zhucai2zi3type);
				String saveText = saveText(zhucai2zi3type, zhucai2zi3value);
				zi3=","+stringType+zhucai2zi3name+stringKey+saveText+"\"}";
			}
			String zi4="";
			if(!zhucai2zi4name.equals(" ") && !zhucai2zi4name.equals("+")){
				String zhucai2zi4type = getPara("zhucai2zi4type");
				String zhucai2zi4value = getPara("zhucai2zi4value");
				String stringType = toStringType(zhucai2zi4type);
				String stringKey = toStringKey(zhucai2zi4type);
				String saveText = saveText(zhucai2zi4type, zhucai2zi4value);
				zi4=","+stringType+zhucai2zi4name+stringKey+saveText+"\"}";
			}
			String zi5="";
			if(!zhucai2zi5name.equals(" ") && !zhucai2zi5name.equals("+")){
				String zhucai2zi5type = getPara("zhucai2zi5type");
				String zhucai2zi5value = getPara("zhucai2zi5value");
				String stringType = toStringType(zhucai2zi5type);
				String stringKey = toStringKey(zhucai2zi5type);
				String saveText = saveText(zhucai2zi5type, zhucai2zi5value);
				zi2=","+stringType+zhucai2zi5name+stringKey+saveText+"\"}";
			}
			caidan2+=zi1+zi2+zi3+zi4+zi5+"]}";
		}
		
		String zhucai3name = getPara("zhucai3name");
		String zhucai3type=getPara("zhucai3type");
		String zhucai3value = getPara("zhucai3value");
		String zhucai3zi1name = getPara("zhucai3zi1name");
		String zhucai3zi2name = getPara("zhucai3zi2name");
		String zhucai3zi3name = getPara("zhucai3zi3name");
		String zhucai3zi4name = getPara("zhucai3zi4name");
		String zhucai3zi5name = getPara("zhucai3zi5name");
		
		if((zhucai3zi1name.equals("+") ||  zhucai3zi1name.equals(" ") )&& (zhucai3zi2name.equals("+") || zhucai3zi2name.equals(" "))
				&& (zhucai3zi3name.equals("+") ||  zhucai3zi3name.equals(" "))
				&& (zhucai3zi4name.equals("+") ||zhucai3zi4name.equals(" "))&& 
				(zhucai3zi5name.equals("+") ||  zhucai3zi5name.equals(" "))){
			if(!zhucai3name.equals(" ") && !zhucai3name.equals("+")){
				String stringType = toStringType(zhucai3type);
				String stringKey = toStringKey(zhucai3type);
				String saveText = saveText(zhucai3type, zhucai3value);
				caidan3=","+stringType+zhucai3name+stringKey+saveText+"\"}";
			}
		}else{
			String zi1="";
			caidan3=",{\"name\":\""+zhucai3name+"\",\"sub_button\": [";
			if(!zhucai3zi1name.equals(" ") && !zhucai3zi1name.equals("+")){
				String zhucai3zi1type = getPara("zhucai3zi1type");
				String zhucai3zi1value = getPara("zhucai3zi1value");
				String stringType = toStringType(zhucai3zi1type);
				String stringKey = toStringKey(zhucai3zi1type);
				String saveText = saveText(zhucai3zi1type, zhucai3zi1value);
				zi1=stringType+zhucai3zi1name+stringKey+saveText+"\"}";
			}
			String zi2="";
			if(!zhucai3zi2name.equals(" ") && !zhucai3zi2name.equals("+")){
				String zhucai3zi2type = getPara("zhucai3zi2type");
				String zhucai3zi2value = getPara("zhucai3zi2value");
				String stringType = toStringType(zhucai3zi2type);
				String stringKey = toStringKey(zhucai3zi2type);
				String saveText = saveText(zhucai3zi2type,zhucai3zi2value);
				zi2=","+stringType+zhucai3zi2name+stringKey+saveText+"\"}";
			}
			String zi3="";
			if(!zhucai3zi3name.equals(" ") && !zhucai3zi3name.equals("+")){
				String zhucai3zi3type = getPara("zhucai3zi3type");
				String zhucai3zi3value = getPara("zhucai3zi3value");
				String stringType = toStringType(zhucai3zi3type);
				String stringKey = toStringKey(zhucai3zi3type);
				String saveText = saveText(zhucai3zi3type, zhucai3zi3value);
				zi3=","+stringType+zhucai3zi3name+stringKey+saveText+"\"}";
			}
			String zi4="";
			if(!zhucai3zi4name.equals(" ") && !zhucai3zi4name.equals("+")){
				String zhucai3zi4type = getPara("zhucai3zi4type");
				String zhucai3zi4value = getPara("zhucai3zi4value");
				String stringType = toStringType(zhucai3zi4type);
				String stringKey = toStringKey(zhucai3zi4type);
				String saveText = saveText(zhucai3zi4type,zhucai3zi4value);
				zi4=","+stringType+zhucai3zi4name+stringKey+saveText+"\"}";
			}
			String zi5="";
			if(!zhucai3zi5name.equals(" ") && !zhucai2zi5name.equals("+")){
				String zhucai3zi5type = getPara("zhucai3zi5type");
				String zhucai3zi5value = getPara("zhucai3zi5value");
				String stringType = toStringType(zhucai3zi5type);
				String stringKey = toStringKey(zhucai3zi5type);
				String saveText = saveText(zhucai3zi5type, zhucai3zi5value);
				zi5=","+stringType+zhucai3zi5name+stringKey+saveText+"\"}";
			}
			caidan3+=zi1+zi2+zi3+zi4+zi5+"]}";
		}
		caidan=caidantou+caidan1+caidan2+caidan3+caidanwei;
		System.out.println(caidan);
		int createCaidan = createCaidan("tegUrfcZVyCUYK5GVieIl_h8uuX92bLfv4SlB0uMR1_vfClN4EaJS8JGKB2T3hi70iGJUYdDfCTBMfwSsO64LrcpnRUckYBWNmRCwBt5-xsEVBcAJAMDJ",caidan);
		renderJson(createCaidan);
	}
	private String toStringType(String type){
		String mm="";
		if(type.equals("url")){
			mm="{\"type\":\"view\",\"name\":\"";
		}else if(type.equals("text")){
			mm="{\"type\":\"click\",\"name\":\"";
		}else{
			mm="{\"type\":\"click\",\"name\":\"";
		}
		return mm;
	}
	private String toStringKey(String type){
		String mm="";
		if(type.equals("url")){
			mm="\",\"url\":\"";
		}else if(type.equals("text")){
			mm="\",\"key\":\"";
		}else{
			mm="\",\"key\":\"";
		}
		return mm;
	}
	private static String saveText(String type,String text){
		if(type.equals("text")){
			Random r =new Random();
			String n=r.nextInt(10000000)+"";
			System.out.println(n);
			DingyiTypeModel ding =new DingyiTypeModel();
//			Math.random().toString(36).substr(2);
//			ding.set("PTname", value)
			ding.set("keyname", n);
			ding.set("type", 1);
			ding.set("text", text);
			DingyiTypeModel.dao.saveDingYi(ding);
			return n;
		}else{
			return text;
		}
	}
	private int createCaidan(String token,String caidan){
		String url=" https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+token;
		String post = HttpKit.post(url, caidan);
		System.out.println(post);
		JSONObject jo =new JSONObject(post);
		int l=0;
		InetAddress addr=null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String ip=addr.getHostAddress();//获得本机IP
		String address=addr.getHostName();//获得本机名称
		System.out.println(ip);
		System.out.println("1111");
		System.out.println(address);
		String ip1=IpKit.getRealIp(getRequest());//获得本机IP
		System.out.println(ip1);
		if(jo.has("errmsg")){
			if(jo.get("errmsg").equals("ok")){
				l=1;
			}
		}
		return l;
	}
}
