package com.utils;


import java.io.IOException;


import com.jfinal.core.Controller;

public class Caidan extends Controller{
	private static String getMenu = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";
	private static String createMenu = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";
	public void index(){
		String caidan=null;
		try {
			caidan=GGetCaidan.getCaidan("MFlkJlxNDos4uZrRt9-uiOTXLDa6czQ4YIje5opCB3ycFOFmFfNkgzPEzmM2IFwadk5ON4O_cgZ2RAS8pWdk6yn_j60l_pSR_1HcUy2FG5YWEFbAGAJRS");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setAttr("jj", caidan);
		render("hh.html");
	}
}
