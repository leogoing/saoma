package com;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.plugin.quartz.QuartzPlugin;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.render.ViewType;
import com.plugin.liwang.LoadUserInfoPlugin;
import com.utils.ImagesDeletByTime;
import com.utils.erweima.ErweimaModel;
import com.utils.weixin.UpdateWeixinInPublicaccount;

import controller.beijing.BeijingController;
import controller.cw.CaiwuController;
import controller.dls.DlsInfoDetail;
import controller.dls.DlsQrcodePlant;
import controller.dls.DlsShengjiController;
import controller.dls.DlsTixianController;
import controller.dls.DlsXiaxianController;
import controller.ggz.GunaggaozhuController;
import controller.gly.GlyBankController;
import controller.gly.GlyBjController;
import controller.gly.GlyDlsDataController;
import controller.gly.GlyDlsUpdateController;
import controller.gly.GlyDlslistController;
import controller.gly.GlyGzhController;
import controller.gly.GlyIndexController;
import controller.gly.GlyyaoyiyaoController;
import controller.gonggao.NoticeController;
import controller.gr.GrZlUpdateController;
import controller.kaihuine.KAIHUINE_access_tokenModel;
import controller.kaihuine.KaihuineInterface;
import controller.liwang.Share;
import controller.liwang.WapUserInfo;
import controller.login.MobanLoginController;
import controller.login.WapLoginController;
import controller.login.loginController;
import controller.notice.NeiNoticeController;
import controller.pingtaiSet.PingtaiSet;
import controller.pingtaiSet.model.PingTaiSetModel;
import controller.pingtaiSet.model.SetType;
import controller.tixian.TixianSet;
import controller.tixian.model.TixianModel;
import controller.weixin.DuiwaiSeriver;
import controller.weixin.weixinController;
import controller.yyy.controlleryyy;
import controller.yyy.geturlaop;
import interceptors.LoginInterceptor;
import job.BaojingJob;
import model.Findbaohuqi;
import model.GetPingtaiModel;
import model.Getxiangqing;
import model.Gongzhonghao;
import model.GuanggaozhuModel;
import model.JobMoneyModel;
import model.PingtaiTupianModel;
import model.SMRDModel;
import model.Saomajilu;
import model.Tixianjilu;
import model.bankModel;
import model.callconfigModel;
import model.handcharge;
import model.levelModel;
import model.upgradeModel;
import model.userModel;
import model.caiwu.ggzcaiwuModel;
import model.gly.Level;
import model.gly.Publicaccount;
import model.gly.Smrecord;
import model.gly.TixianTime;
import model.gly.TixianUserTime;
import model.gly.User;
import model.notice.Notice;
import model.weixin.CaidanModel;
import model.yaoyiyao.DlsyaoyiyaoModel;

public class Config extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		me.setViewType(ViewType.FREE_MARKER);
		// loadPropertyFile("a_little_config.txt");
		// me.setError500View("/WEB-INF/login.html");
		ContextPathHandler ctx = new ContextPathHandler();
		me.setEncoding("utf-8");
		me.setBaseViewPath("ctx");
		me.setDevMode(true);
		// me.setErrorRenderFactory(new E());

	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("ctx"));
		System.out.println();
		DruidStatViewHandler dvh = new DruidStatViewHandler("/druid");
		me.add(dvh);
		
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// me.add(new ErrorInterceptors());
		me.add(new LoginInterceptor());
		me.add(new geturlaop());
		
	}

	@Override
	public void configPlugin(Plugins me) {
		// loadPropertyFile(");
		Prop p = PropKit.use("a_little_config.txt");

		String url = p.get("jdbc.url");
		String username = p.get("jdbc.username");
		String password = p.get("jdbc.password");
		C3p0Plugin c3p0 = new C3p0Plugin(url, username, password);
		me.add(c3p0);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0);
		me.add(arp);

		QuartzPlugin quartzPlugin = new QuartzPlugin();
		quartzPlugin.add("*/5 * * * * ?", new BaojingJob());
		me.add(quartzPlugin);
		
		// 添加saoma数据库中表与java类的映射
		arp.addMapping("bank", bankModel.class);// 银行表
		arp.addMapping("callconfig", callconfigModel.class);// 报警配置表
		arp.addMapping("handcharge", handcharge.class);// 手续费表
		arp.addMapping("level", levelModel.class);// 级别表
		arp.addMapping("publicaccount", Gongzhonghao.class);// 公众账号表
		arp.addMapping("publicaccount", GuanggaozhuModel.class);//广告主
		arp.addMapping("publicaccount", CaidanModel.class);//微信自定义菜单
		arp.addMapping("publicaccount", ErweimaModel.class);//二维码
		arp.addMapping("publicaccount", CaidanModel.class);//自定义菜单
		arp.addMapping("publicaccount", Findbaohuqi.class);//保护期
		arp.addMapping("reflect", Tixianjilu.class);// 提现表
		arp.addMapping("smrecord","id", Saomajilu.class);// 扫码记录表
		arp.addMapping("upgrade", upgradeModel.class);// 升级、下线表
		arp.addMapping("user", userModel.class);// 用户表
		//arp.addMapping("heimingdan", Charuheimingdan.class);宇宁实现的报警重复发送的问题，已经注销，选了用我的了
		arp.addMapping("level", Level.class);
		arp.addMapping("publicaccount", Publicaccount.class);
		arp.addMapping("smrecord", Smrecord.class);
		arp.addMapping("user", User.class);
		arp.addMapping("publicaccount", ErweimaModel.class);
		arp.addMapping("smrecord", Getxiangqing.class);//财务——提现——》查询详情页面	arp.addMapping("publicaccount", ErweimaModel.class);
		arp.addMapping("publicaccount", GetPingtaiModel.class);
		arp.addMapping("advertmaster", PingtaiTupianModel.class);
		arp.addMapping("adrecharge", ggzcaiwuModel.class);
		arp.addMapping("adrecharge", JobMoneyModel.class);
		arp.addMapping("publicaccount", UpdateWeixinInPublicaccount.class);//微信模块对平台表的操作
		arp.addMapping("publicaccount", KAIHUINE_access_tokenModel.class);
		arp.addMapping("tixian", "id",TixianTime.class);
		arp.addMapping("user", TixianUserTime.class);
		arp.addMapping("handcharge", TixianModel.class);
		arp.addMapping("dlsyaoyiyao", DlsyaoyiyaoModel.class);
		arp.addMapping("publicaccount", PingTaiSetModel.class);
		arp.addMapping("paixutype","id", SetType.class);
		arp.addMapping("smrecord", SMRDModel.class);

		arp.addMapping("notice", Notice.class);//公告栏表
		
		me.add(new LoadUserInfoPlugin());//定时器补全头像昵称插件
//		me.add(new ImagesDeletByTime());//定时删除宣传图
		
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/ty",MobanLoginController.class);//模版登录页
		me.add("/wap", WapLoginController.class);// 登录页面
		
		me.add("/", loginController.class);// 登录页面
		me.add("/glyindex", GlyIndexController.class);// 管理员首页
		me.add("/glydlslist", GlyDlslistController.class);// 管理员代理商
		me.add("/glydata", GlyDlsDataController.class);// 管理员数据
		me.add("/glybj", GlyBjController.class);// 管理员报警
		me.add("/glyupdate", GlyDlsUpdateController.class);// 管理员更新代理商
		me.add("/glygzh", GlyGzhController.class);// 管理员公众号
		me.add("/glybank", GlyBankController.class);
		
		me.add("/caiwu", CaiwuController.class);// 财务首页

		me.add("/dlsDevelopLower", DlsXiaxianController.class);// 发展下线
		me.add("/dlsUpLevel", DlsShengjiController.class); // 代理商升级功能
		me.add("/dlswithDrawCash", DlsTixianController.class); // 提现
		me.add("/dlsInfo", DlsInfoDetail.class); // 代理商信息
		me.add("/dlsQrcodePlant", DlsQrcodePlant.class);// 二维码平台
		me.add("/gr", GrZlUpdateController.class);// 个人资料
		
		me.add("/ggz", GunaggaozhuController.class);//广告主
		
		me.add("/weixin", weixinController.class);// 微信
		me.add("/tobeijing", BeijingController.class);
		me.add("/ip", DuiwaiSeriver.class);// 对外服务器接口
		me.add("/kaihuine",KaihuineInterface.class);
		me.add("/tixian", TixianSet.class);
		me.add("/yyy",controlleryyy.class);//摇一摇
		me.add("/glyyyy",GlyyaoyiyaoController.class);//管理员摇一摇
		me.add("pingtaiSet", PingtaiSet.class);
		
		me.add("/notice/index", NoticeController.class);//公告栏
		me.add("/notice",NoticeController.class);//公告栏
		me.add("/wapUserIncome",WapUserInfo.class);//wap转发用
		me.add("/forward",controller.liwang.controlleryyy.class);
		me.add("/neinotice", NeiNoticeController.class);

		me.add("/share",Share.class);//分享
		me.add("/share2",com.plugin.liwang.Share.class);
	}


}
