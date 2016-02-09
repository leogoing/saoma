package controller.login;

import model.userModel;
import validator.LoginValidator;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
/**
 * 演示模版登陆模块
 * @author zmc
 *
 */
public class MobanLoginController extends Controller {
	
	/**
	 * 跳转到登录页
	 */
	@Clear
	public void index(){
		render("/Public/Mobanlogin.html");
	}
	
	/**
	 * 根据loginname,password查询user表中是否有对应的记录，
	 * 如果有，则根据对应的级别跳转到相应页面
	 * 如果没有，则返回登陆页面，告知用户名或密码错误
	 */
	@SuppressWarnings("unused")
	@Before(LoginValidator.class)
	@Clear
	public void login(){
		String loginname=getPara("loginname");
		String password=getPara("password");
		Integer time=20000;
		if(getParaToInt("time")==null || getParaToInt("time").equals("")){
			
		}else{
			time=getParaToInt("time")*1000;
		}
		userModel user=userModel.dao.findFirst("select * from user where loginname=? and password=?", loginname,password);
		if(user!=null){
			setSessionAttr("currentUser", user.get("loginname"));//登陆
			setSessionAttr("currentLevelid", user.get("levelid"));
			setSessionAttr("currentUserId", user.get("id"));
			setSessionAttr("currentUserName", user.get("realname"));
			userModel loginuser=userModel.dao.findLUbyLoginname(loginname);
			setSessionAttr("loginuser", loginuser);
			
			Integer levelid=user.get("levelid");
			if(levelid.equals(2)){
				setAttr("loginuser", loginuser);
				setAttr("time", time);
				render("/show.html");
			}else if(levelid.equals(3)){
				setAttr("loginuser", loginuser);
				setAttr("time", time);
				render("/show.html");
			}else if(levelid.equals(4)){
				setAttr("loginuser", loginuser);
				setAttr("time", time);
				render("/show.html");
			}else{
				setAttr("info", "账户未启用");
				index();
			}
			
		}else{
			setAttr("info", "用户名或密码错误！");
			index();
		}
	}
	
	/**
	 * 登出
	 */
	@Clear
	public void logout(){
		removeSessionAttr("loginuser");
		System.out.println(getSessionAttr("loginuser"));
		redirect("http://weixin-dev-2.ican99.com/saoma/login");
		//redirect("http://localhost:8080/saoma/login");
		//index();
	}
}
