package interceptors;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class ErrorInterceptors implements Interceptor{

	@Override
	public void intercept(Invocation ai) {
		  try {           
		        ai.invoke();
		    } catch (Exception e) {
		        ai.getController().keepModel(ai.getController().getClass()); 
		        ai.getController().setAttr("error", e.getMessage());
		        ai.getController().render("/test.html");
		    }

	}

}
