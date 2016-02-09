package controller.notice;

import model.notice.Notice;

import com.jfinal.core.Controller;

public class NeiNoticeController extends Controller {
	public void index(){
		Notice findNeiNotice =
				Notice.dao.findNeiNotice();
		String notice = findNeiNotice.get("content");
		renderText(notice);
	
	}
}
