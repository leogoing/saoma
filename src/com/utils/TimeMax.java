package com.utils;

import java.sql.Timestamp;

import com.jfinal.plugin.activerecord.Page;

import model.Saomajilu;

public class TimeMax {
	public static Page<Saomajilu> getMax(String time){
		Timestamp beginTime=Saomajilu.dao.earlyTime();

		Page<Saomajilu> maxSm=Saomajilu.dao.getMax(beginTime, time);
		return maxSm;
	}
	
}
