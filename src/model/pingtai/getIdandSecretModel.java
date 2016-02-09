package model.pingtai;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;


public class getIdandSecretModel extends Model<getIdandSecretModel>{
	public static final getIdandSecretModel dao = new getIdandSecretModel();
	//根据传入的ID查询出APPID
	public String getappid(Integer id){
		getIdandSecretModel erweima = getIdandSecretModel.dao.findFirst("SELECT appid FROM publicaccount WHERE id= "+id+" and uplowframe=1");
		String appid = erweima.get("appid");
		return appid;
	}
	//根据传入的ID查询出APPSecret
	public String getappsecret(Integer id){
		getIdandSecretModel erweima = getIdandSecretModel.dao.findFirst("SELECT appsecret FROM publicaccount WHERE id= "+id+" and uplowframe=1");
		String appsecret = erweima.get("appsecret");
		return appsecret;
	}
	//获取所有publicaccount的数量
	public StringBuffer getshuliang(){
		StringBuffer sbuf = new StringBuffer();// 定义StringBuffer拼接字符串
		sbuf.append("SELECT count(*) FROM publicaccount");
		return sbuf;
		/*List<getIdandSecretModel> appsecret = getIdandSecretModel.dao.find("SELECT count(*) FROM publicaccount");
		return appsecret;*/
	}
	public Integer getlength() {
		getIdandSecretModel biao_count = getIdandSecretModel.dao.findFirst("select count(*) from publicaccount ");
		Integer length = biao_count.getInt("count");
		return length;
	}
}
