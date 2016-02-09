package model.gly;

import com.jfinal.plugin.activerecord.Model;

public class TixianTime extends Model<TixianTime>{
	public static final TixianTime dao =new TixianTime();
	
	public void saveTixianTime(TixianTime tixian){
		tixian.update();
	}
	public 	TixianTime findTixianTime(){
		TixianTime findFirst = findFirst("select * from tixian");
		return findFirst;
	}
}
