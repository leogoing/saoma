package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.jfinal.handler.HandlerFactory;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

import com.jfinal.plugin.activerecord.Record;
import com.utils.Zuijinshijian;

import controller.ggz.GunaggaozhuController;
/*
 * 这是广告主Model层
 */
public class GuanggaozhuModel extends Model<GuanggaozhuModel> {

	public static final GuanggaozhuModel dao = new GuanggaozhuModel();
	
	/**
	 * @param id
	 * @param adtime
	 * @param totalsp
	 * @param adtype
	 * @param uplowframe
	 * @param ptname
	 * @param appid
	 * @param appsecret
	 * @param publicaccountname
	 * @param huifuyu
	 * @return
	// 设置平台的保护效期，价格，是否上下架，二维码类型（永久，临时）
	 */
	public Boolean UpdatePingtai(Integer id, Integer adtime, Double totalsp, Integer adtype, Integer uplowframe,
			String ptname,String serverIP,String downtime,int informationtype,String informationurl,int aptime) {
		System.out.println(aptime);
		Double zongjia=totalsp;
		GuanggaozhuModel oldpingtai = GuanggaozhuModel.dao.findById(id);
		//获得我们的分成
		int fencheng =oldpingtai.getInt("fencheng");
		System.out.println("我们的分成"+fencheng);
		totalsp=totalsp*((100-fencheng)*0.01);
		
		//将分成后的价格按提成比率分配到各级代理商
		Double personagentsp = totalsp * 0.8;
		personagentsp= new BigDecimal(personagentsp).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
		Double cityagentsp = (totalsp-personagentsp)*0.5;
		Double countryagentsp = cityagentsp ;
		
		GuanggaozhuModel pingtai =GuanggaozhuModel.dao.findById(id);
		
		pingtai.set("adtime", adtime).set("cityagentsp", cityagentsp).set("countryagentsp", countryagentsp).set("personagentsp", personagentsp)
		.set("adtype", adtype).set("uplowframe", uplowframe).set("name", ptname)
		.set("serverIP", serverIP).set("downtime", downtime).set("zongjia", zongjia)
		.set("informationtype", informationtype).set("informationurl",informationurl)
		.set("aptime", aptime);
		
		Boolean b =pingtai.update();
		return b;
		
	}
	
	/**
	 * 根据平台价格和平台ID计算各个代理商的分成
	 * @param totalsp
	 * @param ptid
	 * @return
	 */
	public Record dailishangjiage(Double totalsp,Integer ptid){
		Double zongjia=totalsp;
		GuanggaozhuModel oldpingtai = GuanggaozhuModel.dao.findById(ptid);
		//获得我们的分成
		int fencheng =oldpingtai.getInt("fencheng");
		System.out.println("我们的分成"+fencheng);
		totalsp=totalsp*((100-fencheng)*0.01);
		
		//将分成后的价格按提成比率分配到各级代理商
		Double personagentsp = totalsp * 0.8;
		personagentsp= new BigDecimal(personagentsp).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
		Double cityagentsp = (totalsp-personagentsp)*0.5;
		Double countryagentsp = cityagentsp ;
		
		Record jiage = new Record();
		
		jiage.set("personagentsp", personagentsp);
		jiage.set("cityagentsp", cityagentsp);
		jiage.set("countryagentsp", countryagentsp);
		
		return jiage;
		
	}
	
	/**
	 * 
	// 获取一段时间内的关注度
	 */
	public Record GetTotalCount(String pingtaiName) {
		Zuijinshijian z = new Zuijinshijian();
		// 获得昨天、今天、本周、本月起始时间
		String todayStart = z.StartOfToday();
		String yesterdayStart = z.StartOfYesterday();
		String weekStart = z.FirstDayOfWeek();
		String monthStart = z.FirstDayOfMonth();
		// 获得昨天、今天、本周、本月结束时间
		String todayEnd = z.EndOfToday();
		String yesterdayEnd = z.EndOfYesterDay();
		String weekEnd = z.LastDayOfWeek();
		String monthEnd = z.LastDayOfMonth();
			
		
		
		Record totalCount = Db.findFirst("select " + "sum(case when smtime between '" + todayStart + "' and '"
				+ todayEnd + "' and attentionPL='" + pingtaiName
				+ "' and cancletime is null then 1 else 0 end) todayCount," + "sum(case when smtime between '"
				+ yesterdayStart + "' and '" + yesterdayEnd + "' and attentionPL='" + pingtaiName
				+ "' and cancletime is null then 1 else 0 end) yesterdayCount," + "sum(case when smtime between '"
				+ weekStart + "' and '" + weekEnd + "'and attentionPL='" + pingtaiName
				+ "' and cancletime is null then 1 else 0 end) weekCount," + "sum(case when smtime between '"
				+ monthStart + "' and '" + monthEnd + "'and attentionPL='" + pingtaiName
				+ "' and cancletime is null then 1 else 0 end) monthCount" + " from smrecord");
		
		Integer yesterdayCount=0;
		Integer todayCount=0;
		Integer weekCount=0;
		Integer monthCount=0;
		try {
			yesterdayCount=totalCount.getBigDecimal("yesterdayCount").intValue();
			todayCount=totalCount.getBigDecimal("todayCount").intValue();
			weekCount=totalCount.getBigDecimal("weekCount").intValue();
			monthCount=totalCount.getBigDecimal("monthCount").intValue();
		} catch (Exception e) {
			// TODO: handle exception
		}
		totalCount.set("yesterdayCount", yesterdayCount);
		totalCount.set("todayCount", todayCount);
		totalCount.set("weekCount", weekCount);
		totalCount.set("monthCount", monthCount);
		
		
		return totalCount;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	// 获取广告主用户所有平台的关注度
	 */

	public Record GetAllTotalCount(Integer userId) {
		// 查询所有平台信息
		List<Record> list = GuanggaozhuModel.dao.GetPingtaiByUserId(userId);
		// 创建总数对象
		Record count = new Record();
		// 创建所有平台昨日、今日、本周、本月关注度的总数
		Integer yesterdayCount = 0;
		Integer todayCount = 0;
		Integer weekCount = 0;
		Integer monthCount = 0;
		// 将每个平台的昨日今日本周本月关注度相加得到所有平台的昨日今日本周本月关注度
		for (int i = 0; i < list.size(); i++) {
				String pingtaiName = list.get(0).getStr("publicaccountname");
				Record totalcount = GuanggaozhuModel.dao.GetTotalCount(pingtaiName);
				Integer y = totalcount.getInt("yesterdayCount");
				Integer t = totalcount.getInt("todayCount");
				Integer w = totalcount.getInt("weekCount");
				Integer m = totalcount.getInt("monthCount");
				yesterdayCount = yesterdayCount + y;
				todayCount = todayCount + t;
				weekCount = weekCount + w;
				monthCount = monthCount + m;
		}
		count.set("yesterdayCount", yesterdayCount);
		count.set("todayCount", todayCount);
		count.set("weekCount", weekCount);
		count.set("monthCount", monthCount);

		return count;

	}
	
	/**
	 * 
	 * @param userId
	 * @return
	// 获取广告主所有平台的昨日今日本周本月花费
	 */
	public Record GetAllTotalCost(Integer userId) {
		// 查询所有平台信息
		List<Record> list = GuanggaozhuModel.dao.GetPingtaiByUserId(userId);
		// 创建所有平台昨日、今日、本周、本月花费的总数
		Double yesterdayCost = 0.0;
		Double todayCost = 0.0;
		Double weekCost = 0.0;
		Double monthCost = 0.0;
		Double allCost=0.0;
		// 将每个平台的花费相加
		for (int i = 0; i < list.size(); i++) {
			String pingtaiName = list.get(i).get("publicaccountname");
			Record today = GuanggaozhuModel.dao.GetTotalCostToday(pingtaiName);
			Record yesterday = GuanggaozhuModel.dao.GetTotalCostYesterday(pingtaiName);
			Record week = GuanggaozhuModel.dao.GetTotalCostThisWeek(pingtaiName);
			Record month = GuanggaozhuModel.dao.GetTotalCostThisMoth(pingtaiName);
			Record all=GuanggaozhuModel.dao.GetTotalCostAlltime(pingtaiName);
			Double y = yesterday.getDouble("total");
			Double t = today.getDouble("total");
			Double w = week.getDouble("total");
			Double m = month.getDouble("total");
			Double a = all.getDouble("total");
			if(y==null){
				y=0.0;
				yesterdayCost = yesterdayCost + y;
			}else{
				yesterdayCost = yesterdayCost + y;
			}
			if(t==null){
				t=0.0;
				todayCost = todayCost + t;
			}else{
				todayCost = todayCost + t;
			}
			if(w==null){
				w=0.0;
				weekCost = weekCost + w;
			}else{
				weekCost = weekCost + w;
			}
			if(m==null){
				m=0.0;
				monthCost = monthCost + m;
			}else{
				monthCost = monthCost + m;
			}
			if(a==null){
				a=0.0;
				allCost=allCost+a;
			}else{
				allCost=allCost+a;
			}
		}
		// 将结果放到一个对象里
		Record cost = new Record();
		cost.set("yesterday", yesterdayCost);
		cost.set("today", todayCost);
		cost.set("week", weekCost);
		cost.set("month", monthCost);
		cost.set("all", allCost);

		return cost;

	}
	
	/**
	 * 
	 * @param userId
	 * @return
	//获得广告主的余额
	 */
	public Double GetBalanceByUserId(Integer adId){
		// 查询所有平台信息
	List<Record> list = GuanggaozhuModel.dao.GetPingtaiByUserId(adId);
	//创建总花费
	Double allCost=0.0;
	for(int i=0;i<list.size();i++){
		//获得每个平台的账户名
		String pingtaiName = list.get(i).get("publicaccountname");
		//获得每个平台的花费
		Record all=GuanggaozhuModel.dao.GetTotalCostAlltime(pingtaiName);
		Double a = all.getDouble("total");
		//将每个平台的花费加到总花费中
		if(a==null){
			a=0.0;
			allCost=allCost+a;
		}else{
			allCost=allCost+a;
		}
	}
	//再查询广告主的总充值金额
	Record r = GetMoenyById(adId);
	Double totalMoney =r.getDouble("totalMoney");
	//用总金额减去总花费得到余额
	if(totalMoney==null){
		totalMoney=0.0;
	}
	Double balance =totalMoney-allCost;
	
	return balance;
		
	}
	/**
	 * 
	 * @param pingtaiName
	 * @return
	//获取该平台从开始到现在的花费
	 */
	
	public Record GetTotalCostAlltime(String pingtaiName) {
		Record r = Db.findFirst(
				"select round(sum(cityget+countryget+personget+choushui),2) total from smrecord where attentionPL='" + pingtaiName + "'");
		Double total = r.getDouble("total");
		if(total==null){
			total=0.0;
			r.set("total", total);
		}
		return r;
	}
	
	/**
	 * 
	 * @param pingtaiName
	 * @return
	// 查询广告平台今天关注度
	 */
	public List<Record> GetTotalToday(String pingtaiName) {

		Zuijinshijian z = new Zuijinshijian();
		// 获得今天起始时间
		String startTime = z.StartOfToday();
		// 获得今天结束时间
		String endTime = z.EndOfToday();

		// 获得今天关注度
		List<Record> todayCount = Db.find("select count(smtime) count from smrecord where smtime between '" + startTime
				+ "' and '" + endTime + "' and attentionPL='" + pingtaiName + "' and cancletime is null");
		return todayCount;

	}
	
	/**
	 * 
	 * @param pingtaiName
	 * @return
	// 查询广告平台昨日关注度
	 */
	public List<Record> GetTotalYesterday(String pingtaiName) {
		// TODO Auto-generated method stub
		Zuijinshijian z = new Zuijinshijian();
		// 获得昨天起始时间
		String startTime = z.StartOfYesterday();
		// 获得昨天结束时间
		String endTime = z.EndOfYesterDay();
		// 获得昨天关注度
		List<Record> yesterdayCount = Db.find("select count(smtime) count from smrecord where smtime between '"
				+ startTime + "' and '" + endTime + "' and attentionPL='" + pingtaiName + "' and cancletime is null");
		return yesterdayCount;
	}

	/**
	 * 
	 * @param pingtaiName
	 * @return
	// 查询广告平台本周关注度
	 */
	public List<Record> GetTotalThisWeek(String pingtaiName) {
		Zuijinshijian z = new Zuijinshijian();
		// 获得本周起始时间
		String startDay = z.FirstDayOfWeek();
		// 获得本周结束时间
		String endDay = z.LastDayOfWeek();
		// 获得本周关注度
		List<Record> weekCount = Db.find("select count(smtime) count from smrecord where smtime between '" + startDay
				+ "' and '" + endDay + "' and attentionPL='" + pingtaiName + "' and cancletime is null");
		return weekCount;
	}

	/**
	 * 
	 * @param pingtaiName
	 * @return
	// 查询广告平台本月关注度
	 */
	public List<Record> GetTotalThisMoth(String pingtaiName) {
		Zuijinshijian z = new Zuijinshijian();
		// 获得本月起始时间
		String startDay = z.FirstDayOfMonth();
		// 获得本月结束时间
		String endDay = z.LastDayOfMonth();
		// 获得本周关注度
		List<Record> monthCount = Db.find("select count(smtime) count from smrecord where smtime between '" + startDay
				+ "' and '" + endDay + "' and attentionPL='" + pingtaiName + "' and cancletime is null");
		return monthCount;
	}
	
	/**
	 * 
	 * @param pingtaiName
	 * @return
	// 获得今天的花费
	 */
	public Record GetTotalCostToday(String pingtaiName) {
		// TODO Auto-generated method stub
		Zuijinshijian z = new Zuijinshijian();
		// 获得今天起始时间
		String startTime = z.StartOfToday();
		// 获得今天结束时间
		String endTime = z.EndOfToday();
		// 获得今天花费
		Record r = Db.findFirst(
				"select round(sum(cityget+countryget+personget+choushui),2) total from smrecord where smtime between '"
						+ startTime + "' and '" + endTime + "' and attentionPL='" + pingtaiName + "'");
		Double total = r.getDouble("total");
		if(total==null){
			total=0.0;
			r.set("total", total);
		}

		return r;
	}
	/**
	 * 
	 * @param pingtaiName
	 * @return
	// 获得昨日花费
	 */
	public Record GetTotalCostYesterday(String pingtaiName) {
		// TODO Auto-generated method stub
		Zuijinshijian z = new Zuijinshijian();
		// 获得昨日起始时间
		String startTime = z.StartOfYesterday();
		// 获得昨日结束时间
		String endTime = z.EndOfYesterDay();
		// 获得昨天花费
		Record r = Db.findFirst(
				"select round(sum(cityget+countryget+personget+choushui),2) total from smrecord where smtime between '"
						+ startTime + "' and '" + endTime + "' and attentionPL='" + pingtaiName + "'");
		Double total = r.getDouble("total");
		if(total==null){
			total=0.0;
			r.set("total", total);
		}
		return r;
	}
	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @param pingtaiName
	 * @return
	//总价查询SQL
	 */
	public StringBuffer getTotalString(String startTime,String endTime,String pingtaiName){
		
		StringBuffer string = new StringBuffer();
		string.append("select round(sum(cityget+countryget+personget),2) total from smrecord where smtime between '");
		string.append(startTime);
		string.append("' and '");
		string.append(endTime);
		string.append("' and attentionPL='");
		string.append(pingtaiName);
		string.append("'");
		
		return string;
	}
	
	/**
	 * 
	 * @param pingtaiName
	 * @return
	// 获得本周花费
	 */
	public Record GetTotalCostThisWeek(String pingtaiName) {
		// TODO Auto-generated method stub
		Zuijinshijian z = new Zuijinshijian();
		// 获得本周起始时间
		String startTime = z.FirstDayOfWeek();
		// 获得本周结束时间
		String endTime = z.LastDayOfWeek();
		// 获得本周花费
		Record r = Db.findFirst(
				"select round(sum(cityget+countryget+personget+choushui),2) total from smrecord where smtime between '"
						+ startTime + "' and '" + endTime + "' and attentionPL='" + pingtaiName + "'");
		Double total = r.getDouble("total");
		if(total==null){
			total=0.0;
			r.set("total", total);
		}
		return r;
	}
	/**
	 * 
	 * @param pingtaiName
	 * @return
	// 获得本月花费
	 */
	public Record GetTotalCostThisMoth(String pingtaiName) {
		Zuijinshijian z = new Zuijinshijian();
		// 获得本月起始时间
		String startTime = z.FirstDayOfMonth();
		// 获得本月结束时间
		String endTime = z.LastDayOfMonth();
		// 获得本月花费
		Record r = Db.findFirst(
				"select round(sum(cityget+countryget+personget+choushui),2) total from smrecord where smtime between '"
						+ startTime + "' and '" + endTime + "' and attentionPL='" + pingtaiName + "'");
		Double total = r.getDouble("total");
		if(total==null){
			total=0.0;
			r.set("total", total);
		}
		return r;
	}
	
	/**
	 * 
	 * @param userid
	 * @return
	// 根据用户ID查询所有平台信息
	 */
	public List<Record> GetPingtaiByUserId(Integer userid) {
		// 链接数据库查询
		List<Record> list = Db
				.find("select round(cityagentsp+countryagentsp+personagentsp,2) totalget,p.* from publicaccount p where adId="
						+ userid);
		for(int i=0;i<list.size();i++){
			//获得我们的分成
			int fencheng =list.get(i).get("fencheng");
			//获得分成后的平台价格
			Double totalget=list.get(i).getDouble("totalget");
			//获得分成前的平台价格
			totalget=totalget/((100-fencheng)*0.01);
			totalget=new BigDecimal(totalget).setScale(2, RoundingMode.HALF_UP).doubleValue();
			list.get(i).set("totalget", totalget);
		}
		
		return list;

	}
	
	/**
	 * 
	// 根据平台ID查询平台信息和并计算价格总和
	 */
	public Record GetPingtaiById(Integer id) {

		// 去数据库查询
		Record pingtai = Db.findFirst("select round(cityagentsp+countryagentsp+personagentsp,2) totalsp,p.* from publicaccount p where id="+id);
		//获得我们的分成
		int fencheng = pingtai.getInt("fencheng");
		Double totalsp =pingtai.getDouble("totalsp");
		//获得fenCheng前的平台价格
		totalsp=totalsp/((100-fencheng)*0.01);
		totalsp= new BigDecimal(totalsp).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		pingtai.set("totalsp", totalsp);
		
		return pingtai;
	}
	
	/**
	 * 
	 *	增加平台
	 * @param adId
	 * @param publicaccountname
	 * @param name
	 * @param appid
	 * @param appsecret
	 * @param adtime
	 * @param totalsp
	 * @param uplowframe
	 * @param adtype
	 * @param huifuyu
	 * @param access_token
	 * @param token_time
	 * @param token
	 * @param imgpath
	 * @param title
	 * @param miaoshu
	 * @param newsurl
	 * @param newstype
	 * @param useserver
	 */
	public void AddPintai(Integer adId, String publicaccountname, String name, String appid, String appsecret,
			Integer adtime, Double totalsp, Integer uplowframe, Integer adtype,String huifuyu,String access_token,String token_time,String token
			,String imgpath,String title,String miaoshu,String newsurl,Integer newstype,Integer useserver,int aptime) {
		System.out.println(aptime);
		Double zongjia=totalsp;
		//获得fenCheng之后的平台价格
		totalsp=totalsp*((100-10)*0.01);
		//将分成后的价格按提成比率分配到各级代理商
		Double personagentsp = totalsp * 0.8;
		personagentsp= new BigDecimal(personagentsp).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
		Double cityagentsp = (totalsp-personagentsp)*0.5;
		Double countryagentsp = cityagentsp ;

		GuanggaozhuModel pingTai = new GuanggaozhuModel();

		pingTai.set("adId", adId).set("publicaccountname", publicaccountname).set("name", name).set("appid", appid)
				.set("appsecret", appsecret).set("adtime", adtime).set("cityagentsp", cityagentsp)
				.set("countryagentsp", countryagentsp).set("personagentsp", personagentsp).set("uplowframe", uplowframe)
				.set("adtype", adtype).set("token_time", token_time).set("huifuyu", huifuyu)
				.set("access_token", access_token).set("token", token).set("imgurl", imgpath).set("useserver", useserver)
				.set("title", title).set("miaoshu", miaoshu).set("newsurl", newsurl).set("newstype", newstype).set("zongjia", zongjia)
				.set("aptime", aptime);

		pingTai.save();

	}
	
	/**
	 * 
	 * @param adId
	 * @return
	//获得第一个平台的信息
	 */
	public GuanggaozhuModel GetOnePingtaiByAdid(Integer adId) {

		GuanggaozhuModel erweima = GuanggaozhuModel.dao.findFirst("select * from publicaccount where adId=" + adId);

		return erweima;
	}
	/**
	 * 
	 * @param pingtaiId
	 * @param path
	 * @param name
	//保存上传图片
	 */
	public void SaveImg(Integer pingtaiId,String path,String name) {
		Record r = new Record();
		r.set("pingtaiId", pingtaiId);
		r.set("picturename", name);
		r.set("pictureURL", path);
		Db.save("advertmaster", r);
		
	}
	/**
	 * 
	 * @param pingtaiId
	 * @return
	//获取广告主所有图片
	 */
	public List<Record> findAllImgs(Integer pingtaiId) {
		
		List<Record> list = Db.find("select * from advertmaster where pingtaiId="+pingtaiId);
		return list;
	}
	/**
	 * 
	 * @param id
	 * @return
	//根据图片ID获取图片
	 */
	public Record GetImgById(Integer id) {
		
		Record r = Db.findFirst("select * from advertmaster where id="+id);
		return r;
	}
	/**
	 * 
	 * @param id
	 * @return
	//根据图片ID从图片表删除图片
	 */
	public boolean DeleteImgById(Integer id){
		
		boolean b = false;
		 b = Db.deleteById("advertmaster", id);
		return b;
	}
	/**
	 * 
	 * @param adId
	 * @param publicaccountname
	 * @param name
	 * @param appid
	 * @param appsecret
	 * @param adtime
	 * @param totalsp
	 * @param uplowframe
	 * @param adtype
	 * @return
	//将页面填写的平台信息放入一个对象中返回方便前台页面数据回填
	 */
	public Record ReturnPingtaiInfo(Integer adId, String publicaccountname, String name,
			String appid, String appsecret, Integer adtime, Double totalsp, Integer uplowframe,
			Integer adtype) {
		
		Record pingtai = new Record();
		pingtai.set("adId", adId);
		pingtai.set("publicaccountname", publicaccountname);
		pingtai.set("appid", appid);
		pingtai.set("appsecret", appsecret);
		pingtai.set("adtime", adtime);
		pingtai.set("totalsp", totalsp);
		pingtai.set("uplowframe", uplowframe);
		pingtai.set("adtype", adtype);
		
		return pingtai;
	}
	/**
	 * 
	 * @param publicaccountname
	 * @return
	//根据平台名称查询平台信息
	 */
	public GuanggaozhuModel FindPingtaiByPublicaccountname( String publicaccountname) {
		GuanggaozhuModel pingtai = GuanggaozhuModel.dao.findFirst("select round(cityagentsp+countryagentsp+personagentsp,2) totalsp,p.* from publicaccount p where publicaccountname=?", publicaccountname);
		
		return pingtai;
	}
	
	
	/**
	 * 根据平台原始ID查询平台信息(平台存在)
	 * @param publicaccountname
	 * @return
	 */
	public Record FindPingtaiByPublicaccountnameNotNull(String publicaccountname ){
		
		Record pingtai =Db.findFirst("select round(cityagentsp+countryagentsp+personagentsp,2) totalsp,p.* from publicaccount p where publicaccountname=?", publicaccountname);
		int fencheng = pingtai.getInt("fencheng");
		Double totalsp = pingtai.getDouble("totalsp");
		totalsp=totalsp/((100-fencheng)*0.01);
		pingtai.set("totalsp", totalsp);
		
		return pingtai;
		
	}
	/**
	 * 
	 * @param id
	 * @return
	//根据ID删除平台
	 */
	public boolean DeletPingtaiById(Integer id) {
		boolean b =GuanggaozhuModel.dao.deleteById(id);
		return b;
	}
	/**
	 * 
	 * @param appid
	 * @param appsecret
	 * @return
	//根据appdi和appsecret查询平台
	 */
	public Record FindPingtaiByAppidAndAppsecret(String appid, String appsecret) {
		
		Record newpingtai = Db.findFirst("select round(cityagentsp+countryagentsp+personagentsp,2) totalsp,p.* from publicaccount p where appid=? and appsecret=?", appid,appsecret);
		Double totalsp =newpingtai.getDouble("totalsp");
		//获得我们的分成
		int fencheng = newpingtai.getInt("fencheng");
		//获得没分成前的平台价格
		totalsp=totalsp/((100-fencheng)*0.01);
		newpingtai.set("totalsp", totalsp);
		
		return newpingtai;
	}
	/**
	 * 
	 * @param name
	 * @param appid
	 * @param appsecret
	 * @param adtime
	 * @param totalsp1
	 * @param uplowframe
	 * @param adtype
	 * @param huifuyu
	 * @return
	//根据输入的平台信息创建新的model
	 */
	public Record CreatPingtaiForReturn(String name, String appid, String appsecret, Integer adtime, String totalsp1, Integer uplowframe, Integer adtype, String huifuyu){
		
		Record returnPingtai = new Record();
		returnPingtai.set("name", name);
		returnPingtai.set("appid", appid);
		returnPingtai.set("appsecret", appsecret);
		returnPingtai.set("totalsp", totalsp1);
		returnPingtai.set("huifuyu", huifuyu);
		returnPingtai.set("adtime", adtime);
		returnPingtai.set("uplowframe", uplowframe);
		returnPingtai.set("adtype", adtype);
		
		return returnPingtai;
		
	}
	/**
	 * 
	 * @param id
	 * @param access_token
	 * @param nowTime
	 * @return
	//更新平台的access_token和token_time
	 */
	
	public boolean UpdatePingtaiAccess_token(Integer id, String access_token, String nowTime) {
		GuanggaozhuModel old = GuanggaozhuModel.dao.findById(id);
		boolean b =old.set("access_token", access_token).set("token_time", nowTime).update();
		return b;
	}
	/**
	 * 
	 * @param userId
	 * @return
	//获得广告主的充值总金额
	 */
	public Record GetMoenyById(Integer userId) {
		
		Record c =Db.findFirst("select sum(Money)  as totalMoney from adrecharge where adid="+userId);
		return c;
	}
	
	/**
	 * 
	 * @param id
	 * @param newsimgurl
	 * @param newstype
	 * @param huifuyu
	 * @param title
	 * @param miaoshu
	 * @param newsurl
	 * @return
	//修改平台回复语
	 */
	public boolean UpdateHuifuyu(Integer id,String newsimgurl, Integer newstype, String huifuyu, String title, String miaoshu,
			String newsurl) {
		GuanggaozhuModel pingtai = GuanggaozhuModel.dao.findById(id);
		pingtai.set("imgurl", newsimgurl).set("newstype", newstype).set("huifuyu", huifuyu).set("title", title).set("miaoshu", miaoshu)
		.set("newsurl", newsurl);
		
		 return  pingtai.update();
	}
	
	/**
	 * 
	 * @param publicaccountname
	 * @param name
	 * @param totalsp
	 * @param adtime
	 * @param uplowframe
	 * @param adtype
	 * @param serverIP
	 * @param useserver
	 * @param adId
	 * @return
	//新增平台信息（用户有自己的服务器）
	 */
	public boolean AddpingtaiBySelf(String publicaccountname, String name, Double totalsp, Integer adtime,
			Integer uplowframe, Integer adtype,String serverIP,Integer useserver,Integer adId) {
		
		//我们先分成
		totalsp=totalsp*((100-10)*0.01);
		
		//将分成后的价格按提成比率分配到各级代理商
		Double personagentsp = totalsp * 0.8;
		personagentsp= new BigDecimal(personagentsp).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
		Double cityagentsp = (totalsp-personagentsp)*0.5;
		Double countryagentsp = cityagentsp ;
		
		GuanggaozhuModel pingtai = new GuanggaozhuModel();
		pingtai.set("publicaccountname", publicaccountname).set("name", name)
		.set("adtime", adtime).set("uplowframe", uplowframe).set("adtype", adtype).set("cityagentsp", cityagentsp)
		.set("countryagentsp", countryagentsp).set("personagentsp", personagentsp).set("serverIP", serverIP)
		.set("useserver", useserver).set("adId", adId);
		
		boolean b =pingtai.save();
		
		return b;
	}
	
	/**
	 * 
	 * @param id
	 * @param serverIP
	 * @param publicaccountname
	 * @param name
	 * @param totalsp
	 * @param adtime
	 * @param uplowframe
	 * @param adtype
	 * @return
	//更新平台信息（有自己的服务器）
	 */
	public Boolean UpdatePingtaiBySelf(Integer id, String serverIP, String name,
			Double totalsp, Integer adtime, Integer uplowframe, Integer adtype) {

		//将价格按提成比率分配到各级代理商
		Double cityagentsp = totalsp * 0.1;
		System.out.println("市级:"+cityagentsp);
		Double countryagentsp = totalsp * 0.1;
		System.out.println("县级:"+countryagentsp);
		Double personagentsp = totalsp*0.8;
		System.out.println("个人:"+personagentsp);
		GuanggaozhuModel pingtai = GuanggaozhuModel.dao.findById(id);
		
	 pingtai.set("serverIP", serverIP).set("name", name).set("cityagentsp", cityagentsp).set("countryagentsp", countryagentsp)
		.set("personagentsp", personagentsp).set("adtime", adtime).set("uplowframe", uplowframe).set("adtype", adtype);
		
	 Boolean b =pingtai.update();
		return b;
	}

	public void updateXiajia(int adId){
		Db.update("update publicaccount set  uplowframe=2  where adId=?",adId);
	}
	
	public void updateXiajiaPT(String name){
		Db.update("update publicaccount set  uplowframe=2  where publicaccountname=?",name);
	}
	
	public List<GuanggaozhuModel> findGGZPT(int adId){
		List<GuanggaozhuModel> find = 
				dao.find("select * from publicaccount where adId=?",adId);
		return find;
	}

}
