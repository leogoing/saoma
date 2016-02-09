package Test;

import com.jfinal.core.Controller;

public class JavaScript extends Controller {
	

	public void js() {
		setAttr("a", 1);
		renderJavascript("if ${a}==1 {alert(\"你好\")}");
	}

	
	public void index() throws Exception {
//		int a=1;
//		int b=0;
//		int c=a/b;
		render("/index.html");
		
	}
}
