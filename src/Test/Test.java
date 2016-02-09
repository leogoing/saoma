package Test;

import com.jfinal.aop.Before;

public class Test {

	@Before(Tinterceptor.class)
	public void test(){
		System.out.println("test");
	}
}
