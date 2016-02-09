
package controller.dls;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import model.Gongzhonghao;

import com.jfinal.core.Controller;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.utils.GetQrcodeUrl;
/**
 * 代理商模块——申请平台
 * @author zmc
 *
 */
public class DlsQrcodeController extends Controller {

	/**
	 * 获取该代理商管理的二维码
	 * 1、从数据库里获取公众平台的list 
	 * 2、获取每一个list当中的map 
	 * 3、遍历这个list 
	 * 4、将代理商二维码的地址，通过循环设置出来]
	 * 
	 * @param dlsid
	 * @throws UnsupportedEncodingException
	 */
	public void showQrcode(Integer dlsid) throws UnsupportedEncodingException {
		//查询数据库中所有的表publicaccount表
		List<Gongzhonghao> list = Gongzhonghao.dao.getgzhlist();
		//新建一个list集合
		ArrayList<String> listurl = new ArrayList<String>();
		//循环遍历
		for (int i = 0; i < list.size(); i++) {
			//根据表中的ID获得appid
			String appid = list.get(i).getStr("appid");
			//根据表中的ID获得aoosctect
			String appscrect = list.get(i).getStr("appscrect");
			//根据表中的ID获得token
			String token = list.get(i).getStr("token");
			//建立一个对象，和微信挂钩
			ApiConfig ac = new ApiConfig();
			//建立对象需要的3个属性1：appid
			ac.setAppId(appid);
			//建立对象需要的3个属性1：appscrect
			ac.setAppSecret(appscrect);
			//建立对象需要的3个属性1：token
			ac.setToken(token);
			//将代理商的ID写入。获得永久二维码
			String qrUrl = GetQrcodeUrl.getQrUrl(ac, dlsid.toString());
			//将永久二维码加入listurl集合
			listurl.add(qrUrl);
		}
		//返回listurl集合
		setAttr("listurl", listurl);

	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		ApiConfig ac = new ApiConfig();
		ac.setAppId("wx696d8e58dec8b531");
		ac.setAppSecret("ce977a181e9be43d9ba04a9ff823cb61");
		ac.setToken("abc");
		String qrUrl = GetQrcodeUrl.getQrUrl(ac, "88");
		System.out.println(qrUrl);

	}
}
