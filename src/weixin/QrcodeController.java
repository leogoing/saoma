package weixin;

import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.QrcodeApi;
import com.jfinal.weixin.sdk.jfinal.ApiController;

public class QrcodeController extends ApiController {

	@Override
	public ApiConfig getApiConfig() {
		ApiConfig ac = new ApiConfig();
		ac.setAppId("wx0604b7fc612eb789");
		ac.setAppSecret("d4624c36b6795d1d99dcf0547af5443d");
		ac.setToken("weixin");
		ApiConfigKit.setThreadLocalApiConfig(ac);
		return ac;

	}

	public void getqrcode() {
		String str = "{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": 123}}}";
		ApiResult apiResult = QrcodeApi.create(str);
//		renderText(apiResult.getStr("ticket"));
		redirect("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+apiResult.getStr("ticket"));
	}

}
