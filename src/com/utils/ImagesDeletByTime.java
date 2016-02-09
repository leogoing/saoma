package com.utils;


import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


public class ImagesDeletByTime implements IPlugin{

	private int delay;
	private int interval;
	private Timer time;
	
	public ImagesDeletByTime(int delay,int interval){
		this.delay=delay;
		this.interval=interval;
	}
	
	public ImagesDeletByTime(){
		this(50000,50000);
	}
	
	@Override
	public boolean start() {
		time=new Timer();
		time.schedule(new TimerTask(){ 
            public void run() {
            	Calendar c =  Calendar.getInstance();
            	int hour =c.get(Calendar.HOUR_OF_DAY);
            	int min =c.get(Calendar.MINUTE);
            	int second=c.get(Calendar.SECOND);
            	if(hour==13&&min==22){
            		FolderDeleteUtil.DeleteFolder(PathKit.getWebRootPath()+"\\imageComopse");
            		System.out.println("已删除"+PathKit.getWebRootPath()+"\\imageComopse");
            	}
            }
        },delay,interval);
		return true;
	}
	

	@Override
	public boolean stop() {
		time.cancel();
		System.out.println("定时器被停止");
		return true;
	}
	
	public static void main(String[] args) {
	}

}
