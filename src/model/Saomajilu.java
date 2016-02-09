package model;



import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import util.AddBaohuTime;
import util.GetTime;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.UserApi;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.weixin.sdk.msg.in.event.InQrCodeEvent;
import com.utils.EmojiFilter;
import com.utils.FilterGarbled;
import com.utils.SqlStrUtils;
import com.utils.TimeMax;
import com.utils.entity.WailaiG;
import com.utils.entity.WailaiQ;
import com.utils.erweima.DuiBiTime;


/*
 * 扫码记录
 */
public class Saomajilu extends Model<Saomajilu> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Saomajilu dao = new Saomajilu();

	public Page<Saomajilu> getinfo(String begintime, String endtime, String publicaccount, Integer id, Integer paraToInt, int pagesize) {
		StringBuffer s = new StringBuffer();
		s.append(" from smrecord s left join publicaccount p on s.attentionPL=p.publicaccountname where state=1 ");
		if (StrKit.notBlank(begintime)) {
			s.append(" and smtime>='" + begintime + "'");
		}
		if (StrKit.notBlank(endtime)) {
			s.append(" and smtime<='" + endtime + "'");
		}
		if (StrKit.notBlank(publicaccount)) {
			if ("全部平台".equals(publicaccount)) {
				publicaccount = "";
			}else{
				s.append(" and attentionPL='" + publicaccount + "'");
			}
		}
		if (!"".equals(id)&&id!=null) {
				s.append(" and levelid='" + id + "'");
		}
		s.append(" order by smtime desc");
		String sql=s.toString();
		System.out.println(sql);
		Page<Saomajilu> list =dao.paginate(paraToInt, pagesize, "select s.*,p.name ", sql);
		return list;
	}
	
	/**
	 * 获取最大峰值
	 * @param begintime
	 * @return
	 */
	public Saomajilu getmax(String time) {
		TimeMax.getMax(time);
		return null;
	}

	public void setnouse(String begintime, String endtime, String pingtai,
			String user) {
		Saomajilu saomajilu = findFirst(
				"select * from saomajilu where smtime>=? and time<=? and pingtai=? and user=?",
				begintime, endtime, pingtai, user);
		saomajilu.set("user", "0").save();

	}
	
	/**
	 * 获取最早扫码时间
	 * @return
	 */
	public Timestamp earlyTime(){
		Saomajilu earlySm=findFirst("select smtime from smrecord order by smtime asc");
		Timestamp earlyTime=earlySm.getTimestamp("smtime");
		return earlyTime;
	}
	
	public Page<Saomajilu> getMax(Timestamp beginTime,String time){
		int t=Integer.parseInt(time);
		Page<Saomajilu> page =null;
		Date now = new Timestamp(System.currentTimeMillis());
		long nowtime = now.getTime();
		int count = (int) ((nowtime - beginTime.getTime())/(60*1000));
		//Page<Saomajilu> maxSm=dao.paginate(1, 1, "select publicaccount.name,count(*) as number", "from smrecord join publicaccount on smrecord.attentionPL=publicaccount.publicaccountname where smtime between '"+beginTime+"' and DATE_ADD('"+beginTime+"', INTERVAL "+time+" MINUTE) group by attentionPL order by number desc");
		Long sum=0L;
		Page<Saomajilu> maxSm=null;
		Page<Saomajilu> tmp_Sm=null;
		//Date lastTime=Db.findFirst("select * from smrecord order by smtime desc limit 1").get("smtime");
		for(int i=0;i<count;i++){
			String sql=" from smrecord join publicaccount on smrecord.attentionPL=publicaccount.publicaccountname where smtime between '"+beginTime+"' and DATE_ADD('"+beginTime+"', INTERVAL "+t+" MINUTE) group by attentionPL order by number desc";
			maxSm =dao.paginate(1,1,"select publicaccount.name,count(*) as number ",sql);
			if(maxSm.getTotalRow()!=0){
				Long maxcount=maxSm.getList().get(0).get("number");
				if((i==0)){
					sum=maxcount;
				}else{
					if(sum<=maxcount){
						sum=maxcount;
					}
				}
				maxSm.getList().get(0).set("number", sum);
				tmp_Sm =maxSm;
			}else{//beginTime.compareTo(lastTime)>0
				sum=sum;
				maxSm=tmp_Sm;
				//return tmp_Sm;
			}
			beginTime.setTime(beginTime.getTime()+60*1000);
		
		}
		//Page<Saomajilu> maxSm=dao.paginate(1, 1, "select publicaccount.name,count(*) as number", "from smrecord join publicaccount on smrecord.attentionPL=publicaccount.publicaccountname group by FLOOR(DATE_FORMAT('"+beginTime+"', '%i')/"+time+"),attentionPL order by number desc");
		return maxSm;
	}
	
	public void setallnouser(String[] values) {
		int i = Db.update("update smrecord set state=2 where id in ("
				+ SqlStrUtils.setIn(values) + ")");
	}

	public Float getyesterdayshouru(String yesString) {
		// TODO Auto-generated method stub
		return null;
	}

	public Float sumshouru(String para) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Saomajilu> getmylist(String dlsname) {
		// TODO Auto-generated method stub
		return null;
	}

	public Float getmyonegzhyesterdayshouru(String dlsname, String gzh) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Float getmyonegzhbenueshouru(String dlsname, String gzh) {
		// TODO Auto-generated method stub
		return null;
	}

	public Float getxiaxianonegzhyesterdayshouru(String dlsname, String gzhname) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Float getxiaxianonegzhbenueshouru(String dlsname,
			String gzhname) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<Saomajilu> jiansuo(Integer paraToInt, int pagesize,String begintime, String endtime, String attentionPL, String agentlevel) {
		StringBuffer s = new StringBuffer();
		s.append("select * from smrecord s join publicaccount p on s.attentionPL=p.publicaccountname where 1=1 ");
		if (StrKit.notBlank(begintime)) {
			s.append(" and smtime>='" + begintime + "'");
		}
		if (StrKit.notBlank(endtime)) {
			s.append(" and smtime<='" + endtime + "'");
		}
		if (StrKit.notBlank(attentionPL)) {
			if ("全部平台".equals(attentionPL)) {
				attentionPL = "";
			}else{
				s.append(" and attentionPL='" + attentionPL + "'");
			}
		}
		if (StrKit.notBlank(agentlevel)) {
			if ("全部".equals(agentlevel)) {
				agentlevel = "";
			}else{
				s.append(" and levelid='" + agentlevel + "'");
			}
		}
		//倒序
		s.append("order by smtime desc");
		//List<Saomajilu> list =find(s.toString());
		String sql=s.substring(8);
		Page<Saomajilu> list =dao.paginate(paraToInt, pagesize, "select * ", sql);
		return list;
	}


	public final static  JSONArray getAgentUserDetail( int pageNumber, int pageSize,int agentType, int userId) {
		return convertQr(paginate(pageNumber, pageSize,agentType,userId).getList());
	}
	
	public static  Page<Saomajilu> paginate(int pageNumber, int pageSize,int agentType,int userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select s.smtime,s.cancletime,s.attpeople,s.personget,p.`name` as attentionPL from smrecord  s left join publicaccount p on p.publicaccountname = s.attentionPl where  ");
		if(agentType==2){
			sql.append("s.countrylevel ="+userId+" ");
		}else if(agentType==3){
			sql.append("s.citylevel = "+userId+" ");
		}else{
			sql.append("s.personlevel ="+userId+"  ");
		}
		sql.append("and state =1  order by smtime desc");
		return Saomajilu.dao.paginate(pageNumber, pageSize, sql.toString(),"");
	}
	/**
	 * 将List转换为JSONArray
	 * 
	 * @param qrcodes
	 * @return
	 */
	private final static JSONArray convertQr(List<Saomajilu> qrcodes) {
		final Iterator<Saomajilu> iter = qrcodes.iterator();
		final JSONArray resArr = new JSONArray();
		Saomajilu tmpQrcode = null;
		while (iter.hasNext()) {
			tmpQrcode = iter.next();
			resArr.add(JSON.parse(tmpQrcode.toJson()));
		}
		return resArr;
	}
	
	
	public Page<Saomajilu> findAllById(Integer pageNumber,Integer id, Integer levelid) {
		Page<Saomajilu> sm=dao.paginate(pageNumber, 10, "select s.*,p.name "," from smrecord s left join publicaccount p on s.attentionPL=p.publicaccountname where (personlevel=? or countrylevel=? or citylevel=?) order by smtime desc",id,id,id);
		return sm;
	}

	/**
	 * 根据条件获取扫码记录
	 * @param begintime
	 * @param endtime
	 * @param pingtai
	 * @param type
	 * @return
	 */
	public Page<Saomajilu> gethistorydata(Integer pageNumber ,String begintime, String endtime,
			String pingtai, String type) {
		StringBuffer s = new StringBuffer();
		s.append("select s.*,p.name from smrecord s left join publicaccount p on s.attentionPL=p.publicaccountname where 1=1 ");
		if (StrKit.notBlank(begintime)) {
			s.append(" and smtime>='" + begintime + "'");
		}
		if (StrKit.notBlank(endtime)) {
			s.append(" and smtime<='" + endtime + "'");
		}
		if (StrKit.notBlank(pingtai)) {
			if ("全部平台".equals(pingtai)) {
				pingtai = "";
			}else{
				s.append(" and attentionPL='" + pingtai + "'");
			}
		}
		if (StrKit.notBlank(type)) {
			if ("全部".equals(type)) {
				type = "";
			}else{
				s.append(" and levelid='" + type + "'");
			}
		}
		//List<Saomajilu> list =find(s.toString());
		String sql=s.substring(18);
		Page<Saomajilu> list =dao.paginate(pageNumber, 10, "select s.*,p.name ", sql);
		return list;
	}
	
	/**
	 * 第一次扫描，将扫描后记录保存
	 */
	public void smsave(InQrCodeEvent inQrCodeEvent){
		String smtime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//扫码时间
		String OpenId=inQrCodeEvent.getFromUserName();//关注人Openid
		Date cancleTime=null;//取关时间
		String attentionPL=inQrCodeEvent.getToUserName();//关注的平台
		//获取最后一条该用户该平台的取消关注记录
		Saomajilu lastCancle=findLastCancleByOpenid(OpenId,attentionPL);//最后取消

		Saomajilu lastScan=findLastScanByOpenid(OpenId,attentionPL);//最后扫描
		//判断最后扫描的时间和最后取消的时间不相等的话，证明扫描后没有取消。进入这个循环
		if (lastScan!=null && lastCancle!=null && !lastScan.get("smtime").equals(lastCancle.get("smtime"))) {
			//这种情况是扫描之后未取消的情况
		}else if(lastScan!=null && lastCancle == null){

		}else{
			//通过attentionPL查出这个平台被谁持有，上级所属，上级分成，自己获利，自己等级（user表中）
			//查询的是publicaccount表。二维码表
		Gongzhonghao gzhinfo=Gongzhonghao.dao.findFirst("select * from publicaccount where publicaccountname='"+attentionPL+"'");
		String uplowframe=gzhinfo.getStr("uplowframe");
		float cityagentsp=gzhinfo.getFloat("cityagentsp");//三个价格提成分成的等级
		float countryagentsp=gzhinfo.getFloat("countryagentsp");//三个价格提成分成的等级
		float personagentsp=gzhinfo.getFloat("personagentsp");//三个价格提成分成的等级
		
		double float1 =(double) gzhinfo.getFloat("zongjia");
		double int1 = (double)gzhinfo.getInt("fencheng");
		double mm=float1*int1/100.0;
		float kk=(float)mm;
	
		//如果下架，则不计费
			if("2".equals(uplowframe)){
			cityagentsp=0;
			 countryagentsp=0;
			 personagentsp=0;
			 kk=0;
		}
		int state =1;//默认此条扫码记录有效
		
		//获取关注平台用户昵称
		ApiResult result=UserApi.getUserInfo(OpenId);//获取ID
		String attpeople = EmojiFilter.filterEmoji(result.getStr("nickname"));
		attpeople = FilterGarbled.encoding(attpeople);//对特殊字符进行处理
		String headimgurl = result.getStr("headimgurl");//通过ID获取他的头像
//		if(headimgurl.equals("") || headimgurl==null){
//			headimgurl="http://b.hiphotos.baidu.com/zhidao/pic/item/4ec2d5628535e5dd8efd8d6974c6a7efcf1b62b6.jpg";
//		}
//		if(attpeople.equals("") || attpeople==null){
//			attpeople="　";
//		}
		//如果有头像的话。直接是路径
		if(headimgurl != null && !headimgurl.equals("")){
			
		}else{//如果没有头像的话。直接是固定
			headimgurl="http://b.hiphotos.baidu.com/zhidao/pic/item/4ec2d5628535e5dd8efd8d6974c6a7efcf1b62b6.jpg";
		}
		//如果名字不是NULL的话
		if(attpeople != null && !attpeople.equals("")){
			
		}else{//否则就是空串
			attpeople="　";
		}
		
		//获取用户id,levelid,取到市级县级个人三个
		System.out.println(inQrCodeEvent.getEventKey());
		String pingtainame=inQrCodeEvent.getToUserName().toString();
		Integer id=Integer.parseInt(inQrCodeEvent.getEventKey().substring(8));//需要根据参数解析,个人id
		userModel user=userModel.dao.findById(id);//同过代理商信息的表，找到对应的ID		user表
		Integer levelid=user.get("levelid");//获得USER表的levelid字段。。等级
		Integer sjid=null;//市级所属id
		Integer xjid=null;//县级所属id
		Integer grid=null;//个人所属id
		float sjfc=0;//市级分成
		float xjfc=0;//县级分成
		float grfc=0;//个人分成
		if(levelid.equals(2)||levelid.equals(10)){//个人代理商
			grid=id;
			userModel xjuser=user.findSXbyId(id);
			if(xjuser!=null){//上级不为空
				if(xjuser.get("levelid").equals(3)||xjuser.get("levelid").equals(2)){//上级是县级,上级是自己
					xjid=xjuser.get("id");
					userModel sjuser=xjuser.findSXbyId(xjid);
					if(sjuser!=null){//上级是县级并且县级上面不为空
						sjid=sjuser.get("id");
						sjfc=cityagentsp;
						xjfc=countryagentsp;
						grfc=personagentsp;
					}else{//上级是县级但县级由公司发展
						xjfc=countryagentsp;
						grfc=personagentsp;
					}
				}else if(xjuser.get("levelid").equals(4)){//上级是市级
					sjid=xjuser.getInt("id");
					sjfc=cityagentsp+countryagentsp;
					grfc=personagentsp;
				}
			}else{//上级为空，即由公司发展
				grfc=personagentsp;
			}
		}else if(levelid.equals(3)){//县级代理商
			userModel sjuser=user.findSXbyId(id);
			xjid=id;
			if(sjuser==null){//县级由公司直接发展
				xjfc=countryagentsp+personagentsp;
			}else{//县级->市级
				sjid=sjuser.get("id");
				sjfc=cityagentsp;;
				xjfc=countryagentsp+personagentsp;
			}
		}else if(levelid.equals(4)){//市级代理商
			sjid=id;
			
			sjfc=cityagentsp+countryagentsp+personagentsp;
		}
		int baohu = Findbaohuqi.dao.finBaohuqi(pingtainame);
		baohu=24*60*60*baohu;
		int baohu1 = Findbaohuqi.dao.finBaohuqi1(pingtainame);
		baohu1=24*60*60*baohu1;
		String baohuqi=null;
		String baohuqi1=null;
		try {
			baohuqi = AddBaohuTime.addBaohuqi(GetTime.getTime(), baohu);
			baohuqi1 = AddBaohuTime.addBaohuqi(GetTime.getTime(), baohu1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Saomajilu sm=new Saomajilu().set("smtime", smtime).set("attpeople", attpeople).set("cancletime", cancleTime).set("attentionPL", attentionPL).set("citylevel", sjid).set("cityget", sjfc)
		.set("headimgurl", headimgurl).set("countrylevel", xjid).set("countryget", xjfc).set("personlevel", grid).set("personget", grfc).set("state", state).set("levelid", id)
		.set("attpeopleno", OpenId).set("level", levelid).set("OriginalValidityPeriod", baohuqi).set("choushui", kk).set("OriginalValidityPeriod1", baohuqi1);
		sm.save();//保存起来
		
		// 判断广告主余额是否符合下架标准
		int  adId = gzhinfo.getInt("adId");
		Float float2 = gzhinfo.getFloat("zongjia");
		Double getBalanceByUserId = GuanggaozhuModel.dao.GetBalanceByUserId(adId);
		List<GuanggaozhuModel> findGGZPT = 
				GuanggaozhuModel.dao.findGGZPT(adId);
		for(int i=0;i<findGGZPT.size();i++){
			GuanggaozhuModel guanggaozhuModel = findGGZPT.get(i);
			Float float3 = guanggaozhuModel.getFloat("zongjia");
			if(getBalanceByUserId<float3){
				String name= guanggaozhuModel.get("publicaccountname");
				GuanggaozhuModel.dao.updateXiajiaPT(name);
			}
		}
		
		}
	}

	
	/**
	 * 取消关注后的处理
	 * @param inFollowEvent
	 */
	public void canclesm(InFollowEvent inFollowEvent) {
		String cancletime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//取关时间
		String openId=inFollowEvent.getFromUserName();
		ApiResult result = UserApi.getUserInfo(openId);
		String attpeople = result.getStr("nickname");
		
		String attentionPL=inFollowEvent.getToUserName();
		Saomajilu sm=dao.findFirst("select * from smrecord where attentionPL=? and attpeopleno=? and cancletime is null and state=1 order by smtime desc",attentionPL,openId);
		boolean duibi2=false;
		System.out.println(GetTime.getTime().toString());
		try {
			duibi2 = DuiBiTime.duibi2(GetTime.getTime().toString(), sm.get("OriginalValidityPeriod").toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Saomajilu canclesm=null;
		System.out.println(duibi2);
		if(duibi2){
			canclesm=new Saomajilu().setAttrs(sm).set("cancletime", cancletime).set("cityget", 0).set("countryget", 0).set("personget", 0).set("choushui", 0).set("id",null);
		}else{
			canclesm=new Saomajilu().setAttrs(sm).set("cancletime", cancletime).set("cityget", -sm.getFloat("cityget")).set("countryget", -sm.getFloat("countryget")).set("personget", -sm.getFloat("personget")).set("choushui", -sm.getFloat("choushui")).set("cancletime", cancletime).set("id",null);
		}
		canclesm.save();
	}

	/**
	 * 通过名字和级别查询扫码记录
	 * @param name
	 * @param levelid
	 * @param paraToInt
	 * @return
	 */
	public Page<Saomajilu> findlistbydlsname(Integer levelid,String level,Integer paraToInt,String begintime,String endtime,String pingtai) {
		StringBuffer sql = new StringBuffer();
		sql.append("select  * from smrecord s left join publicaccount p on s.attentionPL=p.publicaccountname where 1=1 ");
		if ("全部".equals(level)) {
			level = "";
		}
		if (!"".equals(levelid)&&(levelid!=null)) {
			sql.append(" and levelid='" + levelid + "'");
		}
		if (!"".equals(level)&&(level!=null)) {
			sql.append(" and level='" + level + "'");
		}
		if (StrKit.notBlank(begintime)) {
			sql.append(" and smtime>='" + begintime + "'");
		}
		if (StrKit.notBlank(endtime)) {
			sql.append(" and smtime<='" + endtime + "'");
		}
		if (StrKit.notBlank(pingtai)) {
			if ("全部平台".equals(pingtai)) {
				pingtai = "";
			}else{
				sql.append(" and attentionPL='" + pingtai + "'");
			}
		}
		sql.append("  order by smtime desc");
		String lastsql=sql.toString();
		lastsql=lastsql.substring(10);
		System.out.println(sql.toString());
		// List<Dailishang> list = find(sql.toString());
		Page<Saomajilu> paginate = paginate(paraToInt, 100,"select  * ",lastsql);

		return paginate;
	}
	/**
	 * 获取最后一条该用户该平台的取消关注记录
	 * @param OpenId
	 * @param attentionPL
	 * @return
	 */
	public Saomajilu findLastCancleByOpenid(String OpenId, String attentionPL){
		Saomajilu lastCancle=dao.findFirst("select * from smrecord where cancletime is not null and state =1 and attpeopleno=? and attentionPL=? order by id desc",OpenId,attentionPL);
		return lastCancle;
	}

	public Saomajilu findLastScanByOpenid(String OpenId, String attentionPL){
		Saomajilu lastScan=dao.findFirst("select * from smrecord where cancletime is null and state=1 and attpeopleno=? and attentionPL=? order by id desc",OpenId,attentionPL);
		return lastScan;
	}
	public void smsave(WailaiG inQrCodeEvent){
		String smtime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//扫码时间
		String OpenId=inQrCodeEvent.getFromUserName();//关注人Openid
		Date cancleTime=null;//取关时间
		String attentionPL=inQrCodeEvent.getToUserName();//关注的平台
		//获取最后一条该用户该平台的取消关注记录
		Saomajilu lastCancle=findLastCancleByOpenid(OpenId,attentionPL);//最后取消

		Saomajilu lastScan=findLastScanByOpenid(OpenId,attentionPL);//最后扫描
		//判断最后扫描的时间和最后取消的时间不相等的话，证明扫描后没有取消。进入这个循环
		if (lastScan!=null && lastCancle!=null && !lastScan.get("smtime").equals(lastCancle.get("smtime"))) {
			//这种情况是扫描之后未取消的情况
		}else if(lastScan!=null && lastCancle == null){

		}else{
			//通过attentionPL查出这个平台被谁持有，上级所属，上级分成，自己获利，自己等级（user表中）
			//查询的是publicaccount表。二维码表
		Gongzhonghao gzhinfo=Gongzhonghao.dao.findFirst("select * from publicaccount where publicaccountname='"+attentionPL+"'");
		float cityagentsp=gzhinfo.getFloat("cityagentsp");//三个价格提成分成的等级
		float countryagentsp=gzhinfo.getFloat("countryagentsp");//三个价格提成分成的等级
		float personagentsp=gzhinfo.getFloat("personagentsp");//三个价格提成分成的等级
		int state =1;//默认此条扫码记录有效
		
		//获取关注平台用户昵称
		ApiResult result=UserApi.getUserInfo(OpenId);//获取ID
		String attpeople = EmojiFilter.filterEmoji(result.getStr("nickname"));
		attpeople = FilterGarbled.encoding(attpeople);//对特殊字符进行处理
		String headimgurl = result.getStr("headimgurl");//通过ID获取他的头像
//		if(headimgurl.equals("") || headimgurl==null){
//			headimgurl="http://b.hiphotos.baidu.com/zhidao/pic/item/4ec2d5628535e5dd8efd8d6974c6a7efcf1b62b6.jpg";
//		}
//		if(attpeople.equals("") || attpeople==null){
//			attpeople="　";
//		}
		//如果有头像的话。直接是路径
		if(headimgurl != null && !headimgurl.equals("")){
			
		}else{//如果没有头像的话。直接是固定
			headimgurl="http://b.hiphotos.baidu.com/zhidao/pic/item/4ec2d5628535e5dd8efd8d6974c6a7efcf1b62b6.jpg";
		}
		//如果名字不是NULL的话
		if(attpeople != null && !attpeople.equals("")){
			
		}else{//否则就是空串
			attpeople="　";
		}
		
		//获取用户id,levelid,取到市级县级个人三个
		System.out.println(inQrCodeEvent.getEventKey());
		String pingtainame=inQrCodeEvent.getToUserName().toString();
		Integer id=Integer.parseInt(inQrCodeEvent.getEventKey().substring(8));//需要根据参数解析,个人id
		userModel user=userModel.dao.findById(id);//同过代理商信息的表，找到对应的ID		user表
		Integer levelid=user.get("levelid");//获得USER表的levelid字段。。等级
		Integer sjid=null;//市级所属id
		Integer xjid=null;//县级所属id
		Integer grid=null;//个人所属id
		float sjfc=0;//市级分成
		float xjfc=0;//县级分成
		float grfc=0;//个人分成
		if(levelid.equals(2)){//个人代理商
			grid=id;
			userModel xjuser=user.findSXbyId(id);
			if(xjuser!=null){//上级不为空
				if(xjuser.get("levelid").equals(3)){//上级是县级
					xjid=xjuser.get("id");
					userModel sjuser=xjuser.findSXbyId(xjid);
					if(sjuser!=null){//上级是县级并且县级上面不为空
						sjid=sjuser.get("id");
						sjfc=cityagentsp;
						xjfc=countryagentsp;
						grfc=personagentsp;
					}else{//上级是县级但县级由公司发展
						xjfc=countryagentsp;
						grfc=personagentsp;
					}
				}else if(xjuser.get("levelid").equals(4)){//上级是市级
					sjid=xjuser.getInt("id");
					sjfc=cityagentsp+countryagentsp;
					grfc=personagentsp;
				}
			}else{//上级为空，即由公司发展
				grfc=personagentsp;
			}
		}else if(levelid.equals(3)){//县级代理商
			userModel sjuser=user.findSXbyId(id);
			xjid=id;
			if(sjuser==null){//县级由公司直接发展
				xjfc=countryagentsp+personagentsp;
			}else{//县级->市级
				sjid=sjuser.get("id");
				sjfc=cityagentsp;;
				xjfc=countryagentsp+personagentsp;
			}
		}else if(levelid.equals(4)){//市级代理商
			sjid=id;	
			
			sjfc=cityagentsp+countryagentsp+personagentsp;
		}
		int baohu = Findbaohuqi.dao.finBaohuqi(pingtainame);
		baohu=24*60*60*baohu;
		String baohuqi=null;
		try {
			baohuqi = AddBaohuTime.addBaohuqi(GetTime.getTime(), baohu);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Saomajilu sm=new Saomajilu().set("smtime", smtime).set("attpeople", attpeople).set("cancletime", cancleTime).set("attentionPL", attentionPL).set("citylevel", sjid).set("cityget", sjfc)
		.set("headimgurl", headimgurl).set("countrylevel", xjid).set("countryget", xjfc).set("personlevel", grid).set("personget", grfc).set("state", state).set("levelid", id)
		.set("attpeopleno", OpenId).set("level", levelid).set("OriginalValidityPeriod", baohuqi);
		
		sm.save();//保存起来
		}

	}
	public void canclesm(WailaiQ inFollowEvent) {
		String cancletime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//取关时间
		String openId=inFollowEvent.getFromUserName();
		ApiResult result=UserApi.getUserInfo(openId);
		String attpeople = result.getStr("nickname");
		
		String attentionPL=inFollowEvent.getToUserName();
		Saomajilu sm=dao.findFirst("select * from smrecord where attentionPL=? and attpeopleno=? and cancletime is null and state=1 order by smtime desc",attentionPL,openId);
		boolean duibi2=false;
		System.out.println(GetTime.getTime().toString());
		try {
			duibi2 = DuiBiTime.duibi2(GetTime.getTime().toString(), sm.get("OriginalValidityPeriod").toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Saomajilu canclesm=null;
		if(duibi2){
			canclesm=new Saomajilu().setAttrs(sm).set("cancletime", cancletime);
		}else{
			canclesm=new Saomajilu().setAttrs(sm).set("cancletime", cancletime).set("cityget", 0).set("countryget", 0).set("personget", 0);
		}
		canclesm.update();
	}
}
