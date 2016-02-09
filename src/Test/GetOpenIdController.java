package Test;

import com.jfinal.core.Controller;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.MenuApi;

public class GetOpenIdController extends Controller {
	public void index() {
		String codeurl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx696d8e58dec8b531&redirect_uri=http%3A%2F%2F116.255.158.66&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
		redirect(codeurl);

		// https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx520c15f417810387&redirect_uri=http%3A%2F%2Fchong.qq.com%2Fphp%2Findex.php%3Fd%3D%26c%3DwxAdapter%26m%3DmobileDeal%26showwxpaytitle%3D1%26vb2ctag%3D4_2030_5_1194_60&response_type=code&scope=snsapi_base&state=123#wechat_redirect
	}

	public static void main(String[] args) {
		StringBuffer s = new StringBuffer();
		s.append("{");
		s.append("\"button\":[");
		s.append("{");
		s.append("\"type\":\"click\",");
		s.append("\"name\":\"今日8\",");
		s.append("\"key\":\"V1001_TODAY_MUSIC\"");
		s.append("},");
		s.append("{");
		s.append("\"name\":\"菜单\",");
		s.append("\"sub_button\":[");
		s.append("{");
		s.append("\"type\":\"view\",");
		s.append("\"name\":\"搜索\",");
		s.append("\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx696d8e58dec8b531&redirect_uri=http%3A%2F%2F116.255.158.66%2Fsaoma%2Ft&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect\"");
		s.append("},");
		s.append("{");
		s.append("\"type\":\"view\",");
		s.append("\"name\":\"视频\",");
		s.append("\"url\":\"http://v.qq.com/\"");
		s.append("},");
		s.append("{");
		s.append("\"type\":\"click\",");
		s.append("\"name\":\"赞一下我们\",");
		s.append("\"key\":\"V1001_GOOD\"");
		s.append("}]");
		s.append("}]");
		s.append("}");
		ApiConfig ac=new ApiConfig();
		ac.setAppId("wx696d8e58dec8b531");
		ac.setAppSecret("ce977a181e9be43d9ba04a9ff823cb61");
		ac.setToken("abc");
		ApiConfigKit.setThreadLocalApiConfig(ac);
		
		ApiResult result = MenuApi.createMenu(s.toString());
		System.out.println(result.toString());

	}

}
