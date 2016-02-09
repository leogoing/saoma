package controller.login;

import org.apache.lucene.queryparser.flexible.core.processors.RemoveDeletedQueryNodesProcessor;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;

import model.userModel;
import validator.LoginValidator;

public class WapLoginController extends Controller {
	
	@Clear
	public void index(){
		render("/Public/waplogin.html");
//		redirect("http://tgwap.kaihuine.com/login");
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
		//打印登陆信息
		System.out.println("这是waplogin=============================");
		System.out.println("用户名"+loginname+"\n密码"+password);
		userModel user=userModel.dao.findFirst("select * from user where loginname=? and password=?", loginname,password);
		if(user!=null){
			setSessionAttr("currentUser", user.get("loginname"));//��½
			setSessionAttr("currentLevelid", user.get("levelid"));
			setSessionAttr("currentUserId", user.get("id"));
			setSessionAttr("currentUserName", user.get("realname"));
			userModel loginuser=userModel.dao.findLUbyLoginname(loginname);
			setSessionAttr("loginuser", loginuser);
				Object logininfo=getSessionAttr("loginuser");
				setAttr("loginuser", loginuser);
				render("/wap.html");
		}else{
				setAttr("info", "账号密码错误");
				index();
		}
	}
	
}
