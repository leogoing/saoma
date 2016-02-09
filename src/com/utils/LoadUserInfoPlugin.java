package com.utils;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.jfinal.kit.HttpKit;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.utils.llll;

import net.sf.json.JSONObject;


public class LoadUserInfoPlugin implements IPlugin{

	private int delay;
	private int interval;
	private Timer time;
	
	public LoadUserInfoPlugin(int delay,int interval){
		this.delay=delay;
		this.interval=interval;
	}
	
	public LoadUserInfoPlugin(){
		this(2000,60*60*1000);
	}
	
	@Override
	public boolean start() {
		time=new Timer();
		time.schedule(new TimerTask(){ 
            public void run() {
            	go();
            }
        },delay,interval);
		return true;
	}
	
	public void go(){
		
		List<Record> records=Db.find
			("select sm.id,sm.attpeopleno,pu.appid,pu.appsecret,pu.publicaccountname   from  publicaccount as pu join "
					+ "(select id,attentionPL,attpeopleno from smrecord  where attpeople is null" 
					+" or headimgurl is null or length(attpeople)=0 or length(headimgurl)=0 or attpeople='佚名' "
					+ "or headimgurl='http://b.hiphotos.baidu.com/zhidao/pic/item/4ec2d5628535e5dd8efd8d6974c6a7efcf1b62b6.jpg')"
					+ " as sm on pu.publicaccountname=sm.attentionPL");
		String result=null;
		Map<String,JSONObject> userInfo_Cache=new HashMap<String,JSONObject>();
		System.out.println("已存在的平台中一共有 "+records.size()+" 条用户记录头像或昵称没有");
		for(Record r: records){
			System.out.println("数据库中合成的access_token: "+llll.mmm(r.getStr("publicaccountname"), r.getStr("appid"), r.getStr("appsecret")));
			System.out.println("数据库中获取的openid： "+r.getStr("attpeopleno"));
			if(r.getStr("attpeopleno")==null || "".equals(r.getStr("attpeopleno"))){
				continue;
			}
			JSONObject jsonobject=null;
			if(userInfo_Cache.get(r.getStr("attpeopleno"))==null){
				result=HttpKit.get
					("https://api.weixin.qq.com/cgi-bin/user/info?access_token="+
					llll.mmm(r.getStr("publicaccountname"), r.getStr("appid"), r.getStr("appsecret"))+
					"&openid="+r.getStr("attpeopleno")+"&lang=zh_CN");
				System.out.println("微信查询用户result的结果:"+result);
				
				jsonobject = JSONObject.fromObject(result);
				userInfo_Cache.put(r.getStr("attpeopleno"), jsonobject);
			}else{
				jsonobject=userInfo_Cache.get(r.getStr("attpeopleno"));
			}
			if(jsonobject==null || jsonobject.isEmpty() || jsonobject.containsKey("errcode") || jsonobject.getInt("subscribe")!=1){
				continue;
			}
			
			System.out.println("微信获取的用户昵称_nickName: "+jsonobject.getString("nickname"));
			Db.update("update smrecord set attpeople=?,headimgurl=? where id=?", 
				"".equals(jsonobject.getString("nickname"))?"佚名":jsonobject.getString("nickname"),
				"".equals(jsonobject.getString("headimgurl"))?
						"http://b.hiphotos.baidu.com/zhidao/pic/item/4ec2d5628535e5dd8efd8d6974c6a7efcf1b62b6.jpg"
						:jsonobject.getString("headimgurl"),
					r.getInt("id"));
		}
		userInfo_Cache.clear();
	}

	@Override
	public boolean stop() {
		time.cancel();
		return true;
	}
	
	public static void main(String[] args) {
		
	}

}
