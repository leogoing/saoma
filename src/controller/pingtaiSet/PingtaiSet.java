package controller.pingtaiSet;

import java.math.BigDecimal;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import controller.pingtaiSet.model.PingTaiSetModel;
import controller.pingtaiSet.model.SetType;

public class PingtaiSet extends Controller{
	public void index(){
		render("/guanliyuan/two.html");
	}
	public void index1(){
		List<PingTaiSetModel> findPingtaiAll = PingTaiSetModel.dao.findPingtaiAll();
		setAttr("list", findPingtaiAll);
		render("/guanliyuan/three.html");
	}
	public void index2(){
		List<PingTaiSetModel> findPingtaiAll = PingTaiSetModel.dao.findPingtaiAll();
		setAttr("list", findPingtaiAll);
		render("/guanliyuan/four.html");
	}
	
	public void setFencheng(){
		int fencheng = getParaToInt("fencheng");
		int id=getParaToInt("id");
		PingTaiSetModel findPT = PingTaiSetModel.dao.findPT(id);
		
		Record c =Db.findById("publicaccount", id);
		float totalsp=c.getFloat("zongjia");
		//获得fenCheng之后的平台价格
		totalsp=(float) (totalsp*((100-fencheng)*0.01));
		//将分成后的价格按提成比率分配到各级代理商
		float personagentsp = (float) (totalsp * 0.8);
		personagentsp= new BigDecimal(personagentsp).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue();
		float cityagentsp = (float) ((totalsp-personagentsp)*0.5);
		cityagentsp= new BigDecimal(cityagentsp).setScale(3,BigDecimal.ROUND_HALF_UP).floatValue();
		float countryagentsp = cityagentsp ;
		findPT.set("fencheng", fencheng).set("cityagentsp", cityagentsp).set("countryagentsp", countryagentsp)
		.set("personagentsp", personagentsp);
		boolean update = findPT.update();
		if(update){
			renderJson(1);
		}else{
			renderJson(2);
		}
	}
	public void setType(){
		int type = getParaToInt("type");
		SetType.dao.updataType(type);
		renderJson(1);
	}
	public void setType1(){
		int id  = getParaToInt("id");
		int shunxu= getParaToInt("shunxu");
		
		PingTaiSetModel.dao.updateShunxu(id,shunxu);
		
		renderJson(1);
	}
	public void index3(){
		List<PingTaiSetModel> findPingtaiAll = PingTaiSetModel.dao.findPingtaiAll();
		setAttr("list", findPingtaiAll);
		render("/guanliyuan/five.html");
	}
}
