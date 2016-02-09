package controller.yyy;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class geturlaop implements Interceptor{
	/**
	 * 拦截器，获取当前的URL
	 */
	@Override
	public void intercept(Invocation inv) {
		inv.getController().getRequest().setAttribute("URL", inv.getController().getRequest().getRequestURL().toString());
		inv.invoke();
	}

}
