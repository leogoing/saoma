package controller.liwang;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.utils.llll;
import com.utils.weixin.util_access_token;

import net.sf.json.JSONObject;

public class Share extends Controller{
	@Clear
	public void index_share(){
		HttpServletRequest request=getRequest();
		String url = request.getScheme() +"://" + request.getServerName()  
//		                        + ":" +request.getServerPort() 
		                        + request.getServletPath();
		if (request.getQueryString() != null){
			            url += "?" + request.getQueryString();
		 }

		System.out.println("####url地址："+url);
//		String json=HttpKit.get(
//				"https://api.weixin.qq.com/cgi-bin/token?"
//				+ "grant_type=client_credential&appid=wxf8ebbfd3efd2127a&secret=675b59019f1b820e9003a94a83382bef");
//		JSONObject jo_a=JSONObject.fromObject(json);
		String access_token = llll.mmm("gh_943a1c326f75", "wxdd905c6a6c4cdcb3", "0d06a4b3e485a2bdf104510016cf9cc8");
		System.out.println("access_token:     "+access_token);
		String jsapi_ticket=HttpKit.get(
				"https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+access_token+"&type=jsapi");
		System.out.println("jsapi_ticket:  "+jsapi_ticket);
		JSONObject jo_j=JSONObject.fromObject(jsapi_ticket);
		String timestamp=String.valueOf(new Date().getTime());
		String signature=null;
		try {
			signature=getSignature(jo_j.getString("ticket"), timestamp, "9713628", url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setAttr("timestamp", timestamp);
		setAttr("signature", signature);
		System.out.println("signature:   "+getAttr("signature"));
		setAttr("userId",getPara("u"));
		setAttr("agentType",getPara("a"));
		render("/w.html");
	}
	@Clear
	public void index(){
		/*String json=HttpKit.get(
				"https://api.weixin.qq.com/cgi-bin/token?"
				+ "grant_type=client_credential&appid=wxf8ebbfd3efd2127a&secret=675b59019f1b820e9003a94a83382bef");
		JSONObject jo_a=JSONObject.fromObject(json);
		setAttr("userId",getPara("u"));
		setAttr("agentType",getPara("a"));
		String access_token="";
		try {
			access_token = util_access_token.getAccess_token("wxf8ebbfd3efd2127a", "675b59019f1b820e9003a94a83382bef");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String jsapi_ticket=HttpKit.get(
				"https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+jo_a.get("access_token")+"&type=jsapi");
		System.out.println("jsapi_ticket:  "+jsapi_ticket);
		JSONObject jo_j=JSONObject.fromObject(jsapi_ticket);
		String timestamp=String.valueOf(new Date().getTime());
		String signature=null;
		try {
			signature=getSignature(jo_j.getString("ticket"), timestamp, "745446", "http://lw.ican99.com/share?u="+getPara("u")+"&a="+getPara("a"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setAttr("timestamp", timestamp);
		setAttr("signature", signature);
		System.out.println("signature:   "+getAttr("signature"));
		render("/w.html");
//		renderText("@@@@@@@@@@@@@@");*/
	}
	
	public static String getSignature(String jsapi_ticket, String timestamp,
	        String nonce, String jsurl) throws IOException {
	    /****
	     * 对 jsapi_ticket、 timestamp 和 nonce 按字典排序 对所有待签名参数按照字段名的 ASCII
	     * 码从小到大排序（字典序）后，使用 URL 键值对的格式（即key1=value1&key2=value2…）拼接成字符串
	     * string1。这里需要注意的是所有参数名均为小写字符。 接下来对 string1 作 sha1 加密，字段名和字段值都采用原始值，不进行
	     * URL 转义。即 signature=sha1(string1)。
	     * **如果没有按照生成的key1=value&key2=value拼接的话会报错
	     */
	    String[] paramArr = new String[] { "jsapi_ticket=" + jsapi_ticket,
	            "timestamp=" + timestamp, "noncestr=" + nonce, "url=" + jsurl };
	    Arrays.sort(paramArr);
	    // 将排序后的结果拼接成一个字符串
	    String content = paramArr[0].concat("&"+paramArr[1]).concat("&"+paramArr[2])
	            .concat("&"+paramArr[3]);
	    System.out.println("拼接之后的content为:"+content);
	    String gensignature = null;
	    try {
	        MessageDigest md = MessageDigest.getInstance("SHA-1");
	        // 对拼接后的字符串进行 sha1 加密
	        byte[] digest = md.digest(content.toString().getBytes());
	        gensignature = byteToStr(digest);
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    // 将 sha1 加密后的字符串与 signature 进行对比
	    if (gensignature != null) {
	        return gensignature;// 返回signature
	    } else {
	        return "false";
	    }
	    // return (String) (ciphertext != null ? ciphertext: false);
	}
	
	private static String byteToStr(byte[] byteArray) {
	    String strDigest = "";
	    for (int i = 0; i < byteArray.length; i++) {
	        strDigest += byteToHexStr(byteArray[i]);
	    }
	    return strDigest;
	}
	
	private static String byteToHexStr(byte mByte) {
	    char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
	            'B', 'C', 'D', 'E', 'F' };
	    char[] tempArr = new char[2];
	    tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
	    tempArr[1] = Digit[mByte & 0X0F];
	    String s = new String(tempArr);
	    return s;
	}
	
	public static void main(String[] args) {
		System.out.println(new Date().getTime());
	}
	
}
