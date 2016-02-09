package hahada.config;

import hahada.controller.CaidanController;
import hahada.controller.HuifuController;
import hahada.controller.Sucai1Controller;
import hahada.controller.SucaiController;
import hahada.controller.WeixinController;
import hahada.model.DingyiTypeModel;
import hahada.model.HuifuModel;
import hahada.model.NewsModel;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;
import com.jfinal.weixin.demo.WeixinApiController;

public class Config extends JFinalConfig{

	@Override
	public void configConstant(Constants me) {
		me.setDevMode(true);
		me.setViewType(ViewType.FREE_MARKER);
		
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/caidan", CaidanController.class);
		me.add("/huifu", HuifuController.class);
		
		me.add("/sucai", SucaiController.class);
		me.add("/sucai1", Sucai1Controller.class);
		me.add("/", WeixinController.class);
		
	}

	@Override
	public void configPlugin(Plugins me) {
		Prop p = PropKit.use("a_little_config.txt");

		String url = p.get("jdbc.url");
		String username = p.get("jdbc.username");
		String password = p.get("jdbc.password");
		C3p0Plugin c3p0 = new C3p0Plugin(url, username, password);
		me.add(c3p0);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0);
		me.add(arp);
		
//		arp.addMapping("news", NewsModel.class);
//		arp.addMapping("huifu", DingyiTypeModel.class);
//		arp.addMapping("huifu", HuifuModel.class);
		
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		
	}

}
