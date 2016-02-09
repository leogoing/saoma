package util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import com.jfinal.kit.HttpKit;

public class GetCaidan {
	private static String getCaidan(String access_token) throws IOException{
		URL url = null;
		try {
			url = new URL(
					"https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token="
							+ access_token);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		InputStream is = url.openStream();

		byte[] b = new byte[20480];

		int len = -1;

		StringBuilder sb = new StringBuilder();

		len = is.read(b);

		while (len != -1) {
			sb.append(new String(b));
			len = is.read();
		}
		String sbb = sb.toString().trim();
		return sbb;
	}
	public static String getcaidan1(String access_token){
		final String  str="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";
		String caidan="";
		try {
			caidan=getCaidan(access_token);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		caidan=caidan.replaceAll("\"", "\'");
		System.out.println(caidan);
		JSONObject jo=new JSONObject(caidan);
		if(jo.has("selfmenu_info")){
			caidan= jo.get("selfmenu_info").toString();
			System.out.println(caidan);
			String post = 
					HttpKit.post(str+access_token, caidan);
			System.out.println(post);
			
			JSONObject jo1 =new JSONObject(post);
			String type=jo1.get("errmsg").toString();
			if(type.equals("ok")){
				return "1";
			}else{
				return "0";
			}
		}else{
			return "0";
		}
	}
	public static void main(String[] args) {
		String getcaidan1 = getcaidan1("mF0cnUbyytZxfFY-O_PUQGEgKJAG5vOpnAtMChqkwywbSUX2IkRuFad_LV2jYtmvCHnoogKhFCehOZHKkUej4OyPinBLe2DDlbUoBLqEbocQUAgADATXD");
		System.out.println(getcaidan1);
	}
}
