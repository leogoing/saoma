package controller.login;

import validator.LoginValidator;
import model.userModel;
import model.notice.Notice;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
/**
 * 登陆模块
 * @author zmc
 *
 */
public class loginController extends Controller {
	/**
	 * 跳转到登录页面
	 */
	public void index(){
		Page<Notice> page=Notice.dao.paginate(getParaToInt(0, 1), 4, "select *", "from notice where istop!=3 order by top_time desc,create_time desc");
		setAttr("noticePage", page);
//		System.out.println("@@page:  "+page);
		render("/Public/login.html");
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
		//获取登陆方式wap
		System.out.println("用户名："+loginname+"\n密码："+password);
		userModel user=null;
		if(loginname==null || password==null){
			user=getSessionAttr("loginuser");
		}else{
			user=userModel.dao.findFirst("select * from user where loginname=? and password=?", loginname,password);
		}
		if(user!=null){
			Object object = user.get("firsttime");
			if(object==null){
				userModel.dao.updateFirstTime(user.getInt("id"));
			}
			setSessionAttr("currentUser", user.get("loginname"));//登陆
			setSessionAttr("currentLevelid", user.get("levelid"));
			setSessionAttr("currentUserId", user.get("id"));
			setSessionAttr("currentUserName", user.get("realname"));
			setSessionAttr("loginuser", user);

			Integer levelid=user.get("levelid");
			if(levelid.equals(2)){//个人代理商
				
				setAttr("loginuser", user);
				render("/geren.html");
			}else if(levelid.equals(3)){//县级代理商
				
				setAttr("loginuser", user);
				render("/xianji.html");
			}else if(levelid.equals(4)){//市级代理商
				
				setAttr("loginuser", user);
				render("/shiji.html");
			}else if(levelid.equals(5)){//财务
				
				setAttr("loginuser", user);
				render("/caiwu.html");
			}else if(levelid.equals(6)){//管理员
				
				setAttr("loginuser", user);
				render("/index.html");
			}else if (levelid.equals(8)) {//广告主
				
				setAttr("loginuser", user);
				render("/ggz.html");
			}
			else{//账户冻结状态
					setAttr("info", "账户未启用");
					redirect("/");
			}
		}else{
				setAttr("info", "用户名或密码错误！");
				redirect("/");
		}
	}
	
	/**
	 * 登出
	 */
	public void logout(){
		getSession().invalidate();
		System.out.println("是否还有session--loginuser  ："+getSessionAttr("loginuser"));
//		redirect("http://Tg.kaihuine.com/saoma");
		redirect("/");
		//index();
	}
}
