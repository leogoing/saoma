package Test;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;


public class Tinterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		System.out.println("这是拦截器");
		inv.invoke();
	}
	
}
