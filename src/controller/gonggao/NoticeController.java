package controller.gonggao;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

import interceptors.LoginInterceptor;
import model.notice.Notice;

public class NoticeController extends Controller{
	
	/**
	 * 进入管理员公告页
	 */
	public void index(){
		render("/guanliyuan/notice_GL.html");
	}
	public void index1(){
		System.out.println("@@@@@@@ 进入公告首页  @@@@@@@");
		Page<Notice> page=Notice.dao.paginate(getParaToInt(0, 1), 10, "select *", "from notice where istop!=3");
		setAttr("noticeList", page);
		setAttr("loginuser",getSessionAttr("loginuser"));
		render("/guanliyuan/notice_index.html");
	}
	@Clear
	public void ToNotice(){
		Page<Notice> page=Notice.dao.paginate(getParaToInt(0, 1), 10, "select *", "from notice where istop!=3");
		setAttr("noticeList", page);
		setAttr("loginuser",getSessionAttr("loginuser"));
		render("/guanliyuan/notice_show.html");
	}
	public void index2(){//宇宁  著
		Notice findNeiNotice =
					Notice.dao.findNeiNotice();
		String notice = findNeiNotice.get("content");
		setAttr("gonggao", notice);
		render("/guanliyuan/NeiNotice.html");
		
	}
	public void index3(){
		String gonggao = getPara("gonggao");
		Notice.dao.updateGonggao(gonggao);
		renderJson(1);
		
	}
	
	public void add(){
		setAttr("method", "save");
		render("/guanliyuan/notice_add.html");
	}
	
	public void save(){
		getModel(Notice.class).set("create_time",new Date()).
			set("top_time", "0".equals(getPara("notice.istop"))?new Date():null).save();
		redirect("/notice/index");
	}
	
	public void toUpdate(){
		setAttr("notice", Notice.dao.findById(getPara("notice.id")));
		setAttr("method", "update");
//		System.out.println(getAttr("notice")+"\n"+getPara("notice.id"));
		render("/guanliyuan/notice_add.html");
	}
	
	public void update(){
		Notice n=(Notice)getModel(Notice.class).set("top_time", "0".equals(getPara("notice.istop"))?new Date():null);
		n.update();
//		for(Object o:n.getAttrValues()){
//			System.out.println(o);
//		}
		redirect("/notice/index");
	}
	
	public void delete(){
		Notice.dao.deleteById(getPara("notice.id"));
		redirect("/notice/index");
	}
	
	public void top(){
		Notice.dao.findById(getPara("notice.id")).set("istop",0).set("top_time", new Date()).update();
		redirect("/notice/index");
	}
	
}
