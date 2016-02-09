package controller.liwang;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.kit.ParaMap;
import com.jfinal.weixin.sdk.kit.PaymentKit;

import Static.Static;
/**
 * 摇一摇第一个接收处理类
 * @author csc
 *
 */
public class controlleryyy extends Controller {
	@Clear
	public void index() {
		String code = getPara("code");		// 用户同意的话，或的coid
		String state = getPara("state");	// 传入的参数。目前没有参数
		String appId = "wxf74c9993e7ab0bd3";//appid
		String secret = "ccca9331c66377b210314a3f765f3ab7";//appsecret
		
		SnsAccessToken snsAccessToken = getSnsAccessToken(appId, secret, code);//通过code获取access_token
		String AccessToken = snsAccessToken.getAccessToken();// refresh_token.用户刷新access_token
		String openid = snsAccessToken.getOpenid();// 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
		
		yonghuxinxiBean xinxibean = huoquxinxi(AccessToken, openid);//获取用户详细信息
		
		String nickname = xinxibean.getNickname();//获得用户昵称
		Integer sex = xinxibean.getSex();//获得用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
		String country = xinxibean.getCountry();//获得国家，如中国为CN
		String province = xinxibean.getProvince();//获得用户个人资料填写的省份
		String city = xinxibean.getCity();//获得普通用户个人资料填写的城市
		
			System.out.println("nickname  :"+nickname);
			System.out.println("sex  :"+sex);
			System.out.println("country  :"+country);
			System.out.println("province  :"+province);
			System.out.println("city  :"+city);
	}

	private static String url = "https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code";
	private static String authorize_uri = "http://open.weixin.qq.com/connect/oauth2/authorize";
	/**
	 * 生成Authorize链接。生成的链接微信访问将会弹出是否授权的选项
	 * @param appId 应用id
	 * @param redirect_uri 回跳地址
	 * @param snsapiBase 时候完全信息
	 * @return url
	 */
	public static String getAuthorizeURL(String appId, String redirect_uri, boolean snsapiBase) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", appId);
		params.put("response_type", "code");
		params.put("redirect_uri", redirect_uri);
		// snsapi_base（不弹出授权页面，只能拿到用户openid）
		// snsapi_userinfo（弹出授权页面，这个可以通过 openid 拿到昵称、性别、所在地）
		if (snsapiBase) {
			params.put("scope", "snsapi_base");
		} else {
			params.put("scope", "snsapi_userinfo");
		}
		params.put("state", "wx#wechat_redirect");
		String para = PaymentKit.packageSign(params, false);
		return authorize_uri + "?" + para;
	}
	/**当用户点击同意选项，获得的code后。
	 * 通过code获取access_token
	 * @param code 第一步获取的code参数
	 * @param appId 应用唯一标识
	 * @param secret 应用密钥AppSecret
	 * @return SnsAccessToken
	 */
	public static SnsAccessToken getSnsAccessToken(String appId, String secret, String code) {
		SnsAccessToken result = null;
		for (int i = 0; i < 3; i++) { // 最多三次请求
			Map<String, String> queryParas = ParaMap.create("appid", appId).put("secret", secret).put("code", code)
					.getData();
			String json = HttpKit.get(url, queryParas);
			result = new SnsAccessToken(json);
			if (result.isAvailable())
				break;
		}
		return result;
	}
	/**
	 * 接收AccessToken，openid 生成数据。获取用户详细信息
	 * @param AccessToken	access_token
	 * @param openid	用户的ID
	 * @return	返回获取的JSON字符串，返回yonghuxinxiBean类型数据
	 */
	public yonghuxinxiBean huoquxinxi(String AccessToken,String openid){
		//请求3次用户信息
		String xiangurl = "https://api.weixin.qq.com/sns/userinfo?";
		yonghuxinxiBean xinxibean = null;//yonghuxinxiBean类型
		for (int i = 0; i < 3; i++) { // 最多三次请求
			String yy = xiangurl+"access_token="+AccessToken+"&openid="+openid;//合成指定路径，用来获取参数
			String json = HttpKit.get(yy);//访问路径
			xinxibean = new yonghuxinxiBean(json);//将获得的JSON字符串加入到javabean中。
			if (xinxibean.panduan()) {//如果不是空
				break;//就跳出循环
			}
		}
		return xinxibean;
	}
	
	/*
	 * //通过拦截器获得完整URL String url =
	 * (String)this.getRequest().getAttribute("URL"); //截取指定的字符 String replace =
	 * url.replace("yyy/", "");
	 * 
	 * String major = getPara("major"); String minor = getPara("minor"); Integer
	 * ma = Integer.parseInt(major); Integer mi = Integer.parseInt(minor);
	 * 
	 * // 传入ID获得二维码 List<String> EWM = DlsyaoyiyaoModel.dao.GetDailishang(ma,
	 * mi,replace);
	 * 
	 * setAttr("id", EWM);
	 */

}
