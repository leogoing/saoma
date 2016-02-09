package model.yaoyiyao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.jfinal.plugin.activerecord.Model;
import com.utils.ImageCompose;
import com.utils.StringUtils;
import com.utils.erweima.GetEWM;

import model.GetPingtaiModel;
import model.PingtaiTupianModel;
import model.userModel;

public class DlsyaoyiyaoModel extends Model<DlsyaoyiyaoModel>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final DlsyaoyiyaoModel dao = new DlsyaoyiyaoModel();
	
	/**
	 * 根据获得的major,minor进行查询是数据那个代理商的
	 * @param major		数据库dlsyaoyiyao表的数据
	 * @param minor		数据库dlsyaoyiyao表的数据
	 * @param url		传入参数的链接
	 * @return			带背景的二维码
	 */
	public List<String> GetDailishang(Integer major,Integer minor,String url){
		//获得代理商ID
		List<DlsyaoyiyaoModel> list = DlsyaoyiyaoModel.dao.find("SELECT DLSid FROM dlsyaoyiyao WHERE minor ='"+minor+"' and major='"+major+"'");
		//获得第1位
		DlsyaoyiyaoModel ids =  list.get(0);
		//转成int类型
		Integer id = ids.get("DLSid");
		//获得二维码图片list集合
		List<erweimabean> evm = DlsyaoyiyaoModel.dao.allerweima(id);
		//将二维码背景图带上
		List<String> beijingEWM = beijingEWM(evm,url);
		//返回
		return beijingEWM;
	}

	/**
	 * 生成所有二维码
	 * @param id	接收参数：id
	 * @return	返回生成的二维码
	 */
	public List<erweimabean> allerweima(Integer ids){
		//获得所有生成二维码的参数
		List<GetPingtaiModel> findPingtaiAll = GetPingtaiModel.dao.findPingtaiAll();

		//创建一个list集合
		List<erweimabean> listevm=new ArrayList<erweimabean>();
		//循环出所有的二维码
		for(int i=0;i<findPingtaiAll.size();i++){
		//一个接收参数的bean
			erweimabean eee = new erweimabean();
			GetPingtaiModel getPingtaiModel = findPingtaiAll.get(i);
			//获得平台ID
			String pingtaiID = getPingtaiModel.get("id").toString();
			
			String publicaccountname=getPingtaiModel.get("publicaccountname");
			//获得参数，用来判断是否使用我们的服务器
			String str = getPingtaiModel.get("useserver").toString();
			String linshiEWM="";
			//使用str参数，进行判断
			if(str.equals("1")){
				String appId=getPingtaiModel.get("appid");
				String appsecret=getPingtaiModel.get("appsecret");
				String Token=getPingtaiModel.get("token");
				//获得二维码
				linshiEWM = GetEWM.linshiEWM(appId, appsecret, Token, ids, publicaccountname);
			}else if(str.equals("0")){
				String access_token = getPingtaiModel.get("access_token").toString();
				linshiEWM=GetEWM.EWMW(access_token, ids);
			}
			eee.setLinshiEWM(linshiEWM);
			eee.setPingtaiID(pingtaiID);
			listevm.add(eee);
		}
		return listevm;
	}
	/**
	 * 获取带背景的二维码
	 * @param listevm	二维码集合
	 * @param url		申请的路径
	 * @return	带背景的二维码
	 */
	public List<String> beijingEWM(List<erweimabean> listevm,String url) {
		// 一个lest集合
		List<String> llstURL = new ArrayList<String>();
		
		for (int j = 0; j < listevm.size(); j++) {
			erweimabean ee = listevm.get(j);
			String erweima = ee.getLinshiEWM();
			String PingtaiID = ee.getPingtaiID();
			int id = Integer.parseInt(PingtaiID);
			// 根据平台ID获取图片
			List<PingtaiTupianModel> list = PingtaiTupianModel.dao.findTupianOne(id);
			
			for (int i = 0; i <1; i++) {
				PingtaiTupianModel pingtaiTupian = list.get(i);
				// 从数据库查出来的数据，根据获得指定的路径
				String tupianURL = pingtaiTupian.get("pictureURL").toString();
				
				// 定义一个空字符串
				String compose = null;
				// 平台ID加上一个空字符串。输出路径
				String id2 = id + "";
					// 获得生成的带背景图片的URL路径
					compose = ImageCompose.Compose(erweima, tupianURL, id2);
				// 截取字符串.获得带背景的二维码
				String wanzhengURL = StringUtils.substringAfter(compose, "/");
				//向集合添加wanzhengURL。
				llstURL.add(url+wanzhengURL);
			}
		}
		return llstURL;
	}
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/**
	 * 管理员——》添加摇一摇事件——》直接查询所有设备
	 * @return
	 */
	public List<DlsyaoyiyaoModel> suoyoushebei(){
		//查询所有设备
		List<DlsyaoyiyaoModel> list = DlsyaoyiyaoModel.dao.find("select * from dlsyaoyiyao");
		return list;
	}
	/**
	 * 管理员——》添加摇一摇事件——》查询出所有设备——》点击修改
	 * @param 数据的ID
	 * @return	根据数据ID查询出来的数据
	 */
	public List<DlsyaoyiyaoModel> yyybianji(String id){
		//根据ID查询指定的数据
		List<DlsyaoyiyaoModel> bianji = DlsyaoyiyaoModel.dao.find("select * from dlsyaoyiyao where id= "+id+"");
//		Page<DlsyaoyiyaoModel> bianji = dao.paginate(1, 1, "select * ", "fron dlsyaoyiyao where id= ?",id);
		//根据
		return bianji;
	}
	/**
	 * 管理员——》添加摇一摇事件——》查询出所有设备——》点击修改-》跳转页面后——》修改数据后——》点击提交
	 */
	public void yyytijiao(String id,String uuid,String major,String minor,String loginname){
		//根据用户名，获得ID
		Integer DLSid = userModel.dao.yyyLDSid(loginname);

//		DlsyaoyiyaoModel.dao.find("UPDATE dlsyaoyiyao SET uuid = "+uuid+","+major+","+minor+","+loginname+","+yyyLDSid+",where id = '"+id+"'");
		DlsyaoyiyaoModel dls = DlsyaoyiyaoModel.dao.findFirst("select * from dlsyaoyiyao where id = '"+id+"'");
		dls.set("uuid", uuid).set("major", major).set("minor", minor).set("DLSid", DLSid).set("loginname", loginname).update();
	}
	/**
	 * 管理员——》添加摇一摇事件——》添加设备
	 */
	public void yyyshebei(String uuid,String major,String minor,String loginname){
		//获得ID
		Integer DLSid = userModel.dao.yyyLDSid(loginname);
		//String string = yyyLDSid.toString();
	//	Db.update("insert into dlsyaoyiyao(uuid,major,minor,DLSid,loginname) values("*/+uuid+","+major+","+minor+","+string+","+loginname+")");
		new DlsyaoyiyaoModel().set("uuid", uuid).set("major", major).set("minor", minor).set("DLSid", DLSid).set("loginname", loginname).save();
	}
		
}
