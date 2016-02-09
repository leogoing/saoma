package controller.liwang;

import java.util.Map;

import com.jfinal.weixin.sdk.utils.JsonUtils;
/**
 * 用户信息bean
 * @author csc
 *
 */
public class yonghuxinxiBean {
	private String openid;//用户的唯一标识
	private String nickname;//用户昵称
	private Integer sex;//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	private String language;//字体
	private String province;//用户个人资料填写的省份
	private String city;//普通用户个人资料填写的城市
	private String country;//国家，如中国为CN
	private String headimgurl;//用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
	private String privilege;//用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
	private String json;//JSON
	public yonghuxinxiBean(String jsonStr){
		this.json = jsonStr;
	    try{
	    	@SuppressWarnings("unchecked")
	    	Map<String, Object> temp = JsonUtils.decode(jsonStr, Map.class);
	    	openid = (String) temp.get("openid");
	    	nickname = (String) temp.get("nickname");
	    	sex = (Integer) temp.get("sex");
	    	language = (String) temp.get("language");
	    	province = (String) temp.get("province");
	    	city = (String) temp.get("city");
	    	country = (String) temp.get("country");
	    	headimgurl = (String) temp.get("headimgurl");
	    	privilege = (String) temp.get("privilege");
	    } catch (Exception e)
	    {
	    	throw new RuntimeException(e);
	    }
	}
	/**
	 * 判断参数是不是空
	 * @return
	 */
    public boolean panduan()
    {
        if (sex == null)
            return false;
        if (province == null)
            return false;
        if (city ==null)
            return false;
        return json != null;
    }
	
	public String getOpenid() {
		return openid;
	}
	public String getNickname() {
		return nickname;
	}
	public Integer getSex() {
		return sex;
	}
	public String getLanguage() {
		return language;
	}
	public String getProvince() {
		return province;
	}
	public String getCity() {
		return city;
	}
	public String getCountry() {
		return country;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public String getPrivilege() {
		return privilege;
	}
	public String getJson() {
		return json;
	}

		



}
