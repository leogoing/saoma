package controller.gly;

import model.bankModel;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
/**
 * 管理员模块——添加银行
 * @author zmc
 *
 */
public class GlyBankController extends Controller {
	/**
	 * 去增加可选银行页面
	 */
	public void index(){
		Page<bankModel> bankList=bankModel.dao.findAll();
		setAttr("bankList", bankList);
		render("/guanliyuan/addbank.html");
	}
	
	/**
	 * 将银行添加到数据库中
	 */
	public void addbank(){
		bankModel bank=getModel(bankModel.class,"bank");
		JSONObject json=new JSONObject();
		if(bank.get("bankname")==null){
			json.put("info", "请输入有效银行名称");
			final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
			renderJson(retObj);
		}else{
			Integer id =bankModel.dao.getMaxId();
			bank.set("id", id);
			boolean save;
			try {
				save = bank.save();
				if(save==true){
					json.put("info", "新增成功");
					final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
					renderJson(retObj);
				}else{
					json.put("info", "新增失败");
					final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
					renderJson(retObj);
				}
			} catch (Exception e) {
				json.put("info", "新增失败,请稍后再试");
				final JSONObject retObj = (JSONObject) JSONObject.toJSON(json);
				renderJson(retObj);
				e.printStackTrace();
			}
		}
		//render("/guanliyuan/addbank.html");
	}
}
