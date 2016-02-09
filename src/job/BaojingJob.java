package job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import model.Saomajilu;
import model.callconfigModel;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.utils.SendEmailUtil;
import com.utils.entity.MailUserEntity;

public class BaojingJob implements Job {
	/**
	 * 定时任务调用mail发送邮件
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// 定时任务调用mail发送邮件
		// 1、写一个job查询扫码记录表中人数时间满足报警记录的，发送到指定邮箱

		// 查询这个表
		callconfigModel bj = callconfigModel.dao.findFirst("select * from callconfig");
		// 获得表中callemail字段
		String callemail = bj.getStr("callemail");

		// 获取对象。
		Calendar beforenowTime = Calendar.getInstance();
		// 获取这个表的timespace字段
		int timespace = bj.getInt("timespace");
		// 将现在的时间添加进去
		beforenowTime.add(Calendar.MINUTE, -(timespace));

		// 格式时间
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:MM:mm");
		// 时间字符串
		String dateString = formatter.format(beforenowTime.getTime());
		// 扫码记录集合list
		List<Saomajilu> smjllist = new ArrayList<Saomajilu>();

		// 某用户所有扫码次数超过count就报警
		String sql = "select *,count(smrecord.levelid) as smcs from smrecord join user on smrecord.levelid=user.id group by smrecord.levelid having smcs>=? and smtime>=? and smtime<=?";

		String endtime = formatter.format(new Date());
		// saomajilu是扫码记录类
		smjllist = Saomajilu.dao.find(sql, bj.get("smcount"), dateString,endtime);
		// 将集合迭代
		Iterator<Saomajilu> it = smjllist.iterator();
		// 定义一个空字符串，：：循环里用到
		String content = "";
		// 进行判断，大于1就进入这个循环
		if (smjllist.size() >= 1) {
			while (it.hasNext()) {
				Saomajilu ssm = it.next();

				// 将空串增加
				content += "登录名：" + ssm.get("loginname") + "("+ ssm.get("realname") + ")-起始时间：" + dateString + "~"+ endtime + "-扫码次数" + ssm.get("smcs") + "<br>";
			}
			System.out.println(content);
			// 发送邮件
			MailUserEntity email = new MailUserEntity();
			email.setUserAdress("936174732@qq.com");
			email.setUserAdressPass("zhangmeichun000");
			SendEmailUtil.sendMail(email, "报警邮件", "以下平台有不正常记录<br>" + content,callemail);
		}
		//将集合remove掉
//		it.remove();
		// 将content滞空 ：：定义的串就是空了
		content = null;
		// 将集合滞空 ：：集合就变成空的了，不进入while循环
		it = null;
		// 将扫码记录滞空 ：：不进入if判断
		smjllist = null;
		// 将表中的字段滞空 ：：回复的邮件中的callemail就变成空的了
		callemail = null;
	}

}
