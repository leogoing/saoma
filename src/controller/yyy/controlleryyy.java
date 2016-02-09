package controller.yyy;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;

import model.yaoyiyao.DlsyaoyiyaoModel;

public class controlleryyy extends Controller {
	@Clear
	@Before(geturlaop.class)
	public void index() {
		//通过拦截器获得完整URL
		String url  = (String)this.getRequest().getAttribute("URL");
		//截取指定的字符
		String replace = url.replace("yyy/", "");
		
		String major = getPara("major");
		String minor = getPara("minor");
		Integer ma = Integer.parseInt(major);
		Integer mi = Integer.parseInt(minor);
		
		// 传入ID获得二维码
		List<String> EWM = DlsyaoyiyaoModel.dao.GetDailishang(ma, mi,replace);
		
		setAttr("id", EWM);
		render("/yyy/index.html");
	}



}
