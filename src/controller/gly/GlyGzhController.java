package controller.gly;

import util.GetTime;
import model.Gongzhonghao;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.utils.GetQrcodeUrl;

import model.Gongzhonghao;
import model.gly.Publicaccount;
import util.GetTime;
import com.utils.GetQrcodeUrl;
/**
 * 管理员模块——公众号
 * @author zmc
 *
 */
public class GlyGzhController extends Controller {

	/**
	 * 添加公众帐号
	 */
	public void addgzh() {
		Gongzhonghao gzh=getModel(Gongzhonghao.class, "gzh");
		boolean save;
		JSONObject json=new JSONObject();
		try {
			save = gzh.save();
			if (save == true) {
				json.put("success", true);
				json.put("info", "添加成功");
				final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
				renderJson(retObj);
			} else {
				json.put("success", true);
				json.put("info", "添加失败");
				final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
				renderJson(retObj);
			}
		} catch (Exception e) {
			json.put("success", true);
			json.put("info", "添加失败请检查参数");
			final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
			renderJson(retObj);
			//render("/guanliyuan/guanligongzhonghao.html");
			e.printStackTrace();
		}
		//render("/guanliyuan/guanligongzhonghao.html");
	}

	/**
	 * 公众帐号上下架管理
	 */
	public void shangxiajia() {
		int type = getParaToInt("type");

		if (type == 0) {
			type = 1;
		} else {
			type = 0;
		}

		boolean update = getModel(Gongzhonghao.class, "gzh").update();
		if (update == true) {
			render("");
		} else {
			render("");
		}
	}

	/**
	 * 获取公众号列表
	 */
	public void listgzh() {
		/*Page<Gongzhonghao> uplist = Gongzhonghao.dao.getup();
		Page<Gongzhonghao> lowlist = Gongzhonghao.dao.getlow();
		
		setAttr("uplist", uplist);
		setAttr("lowlist", lowlist);*/
		Page<Gongzhonghao> gzhList = Gongzhonghao.dao.getgzhAll();
		setAttr("gzhList",gzhList);
		render("/guanliyuan/guanligongzhonghao.html");
	}
	
	/**
	 * 对公众号信息进行修改
	 */
	public void bjgzh(){
		String id=getPara("gzh.id");
		Page gzh=Publicaccount.dao.ee(id);
		setAttr("gzh", gzh);
		render("/guanliyuan/bianjigongzhonghao.html");
	}
	
	/**
	 * 对公众号进行修改
	 */
	public void updategzh(){
		Gongzhonghao gzh=getModel(Gongzhonghao.class, "gzh");
		
		Integer id=gzh.getInt("id");
		Gongzhonghao oldgzh=Gongzhonghao.dao.findById(id);
		oldgzh.setAttrs(gzh);
		String mu = gzh.get("uplowframe").toString();
		int parseUnsignedInt = Integer.parseUnsignedInt(mu);
		if(parseUnsignedInt==1){
			oldgzh.set("downtime", null);
		}
		if(parseUnsignedInt ==3){
			oldgzh.set("downtime", GetTime.getNextDay());
		}
		try {
			boolean update=oldgzh.update();
			if (update == true) {
				setAttr("info", "更新成功");
			} else {
				setAttr("info", "更新失败");
			}
		} catch (Exception e) {
			setAttr("info", "更新失败请检查参数");
			listgzh();
			e.printStackTrace();
		}
		listgzh();
	}
	
	/**
	 * 跳转生成二维码页面
	 */
	public void goQrcode(){
		setAttr("gzhappid", getPara("qrcode.appid"));
		setAttr("gzhappsecret", getPara("qrcode.appsecret"));
		render("/guanliyuan/qrcode.html");
	}
	
	/**
	 * 根据输入信息生成二维码
	 */
	public void qrcode(){
		try {
			String appid=getPara("qrcode.appid");
			String appsecret=getPara("qrcode.appsecret");
			String paraid=getPara("qrcode.paraid");
			String qrcodeUrl=GetQrcodeUrl.qrcode(appid, appsecret, paraid);
			
			setAttr("gzhappid", appid);
			setAttr("gzhappsecret", appsecret);
			setAttr("qrcodeUrl", qrcodeUrl);
			//保存规则
			
			render("/guanliyuan/qrcode.html");
		} catch (Exception e) {
			setAttr("info", "生成二维码失败");
			render("/guanliyuan/qrcode.html");
			e.printStackTrace();
		}
		
	}
	
	
	public void displayCode(){
		try {
			String appid=getPara("appid");
			String appsecret=getPara("appsecret");
			String paraid=getPara("id");
			String qrcodeUrl=GetQrcodeUrl.qrcode(appid, appsecret, paraid);
			String url="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+qrcodeUrl;
			renderText(url);
		} catch (Exception e) {
			renderJson("获取失败");
			e.printStackTrace();
		}
		
	}
}
