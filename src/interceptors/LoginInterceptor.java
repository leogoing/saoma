package interceptors;

import model.userModel;
import model.notice.Notice;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class LoginInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		userModel loginUser = controller.getSessionAttr("loginuser");
		System.out.println(inv.getControllerKey());
		if (inv.getControllerKey().equals("/wx") || inv.getControllerKey().equals("/weixin") ||
				inv.getControllerKey().equals("/ip") || inv.getControllerKey().equals("/kaihuine")
					|| inv.getActionKey().equals("/notice/index")) {
			
			inv.invoke();
		}
		else{
			if (loginUser != null)
				inv.invoke();
			else {
					Page<Notice> page=Notice.dao.paginate(controller.getParaToInt(0, 1), 4, "select *", "from notice where istop!=3 order by top_time desc,create_time desc");
					controller.setAttr("noticePage", page);
	//				System.out.println("@@page:  "+page);
					controller.render("/Public/login.html");
			}
		}
	}

}
