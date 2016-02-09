package Test;

import com.jfinal.aop.Duang;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.weixin.sdk.api.SnsAccessToken;

public class TestCode extends Controller {
	public void test() {
		// 从上一个页面跳过来的，这个是从网址里获得的code
		String code = getPara("code");
		String json = HttpKit
				.post("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx696d8e58dec8b531&secret=ce977a181e9be43d9ba04a9ff823cb61&code="
						+ code + "&grant_type=authorization_code", "");

		SnsAccessToken sa = new SnsAccessToken(json);
		String accessToken = sa.getAccessToken();
		String openid = sa.getOpenid();
		System.out.println("test====" + openid);
		renderText(openid);
	}

	public void index() {
		render("/main.html");
	}

	public void main() {
		render("/main.html");
	}

	public static void main(String[] args) {
		Test test = Duang.duang(Test.class);
		test.test();
	}

}
