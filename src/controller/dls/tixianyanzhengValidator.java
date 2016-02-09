package controller.dls;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class tixianyanzhengValidator implements Interceptor{
		/**
		 * 拦截器防止重复提交表单
		 */
	 @Override
	 public void intercept(Invocation inv) {
	  boolean token = com.jfinal.token.TokenManager.validateToken(inv.getController(), "blogToken");
	  if(!token){
	   inv.invoke();
	  }else{
	   inv.getController().renderText("请不要重复提交");
	  }
	 }




}
