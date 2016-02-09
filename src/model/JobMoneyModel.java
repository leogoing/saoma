package model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

public class JobMoneyModel extends Model<JobMoneyModel> {
	public static final JobMoneyModel dao =new JobMoneyModel();
	
	public JobMoneyModel findMoney(String publicaccountname){
		StringBuffer sb=new StringBuffer();
		sb.append("select sum(Money) as money ,p.adId from adrecharge as a join");
		sb.append("(select adId from publicaccount where ");
		sb.append("publicaccountname=?) as  p  on a.adId=p.adId");
		String bb=sb.toString();
		JobMoneyModel find = dao.findFirst(bb,publicaccountname);
		return find;
	}
}
