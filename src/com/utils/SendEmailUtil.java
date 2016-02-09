package com.utils;

import com.utils.entity.MailUserEntity;

import jodd.mail.Email;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;
import jodd.mail.SmtpSslServer;
import jodd.props.Props;
import jodd.props.PropsUtil;
import jodd.util.StringUtil;

public class SendEmailUtil {
	// 获取配置文件中的 个人邮箱配置参数
	private static final String RETURN_RESULT_ERROR_TITLE = "邮件标题不能为空";
	private static final String RETURN_RESULT_ERROR_CONTEXT = "邮件内容不能为空";
	private static final String RETURN_RESULT_ERROR_ADDRESS = "邮件地址不能为空";
	private static final String RETURN_RESULT_SUCCESS = "邮件发送成功";
	private static final String RETURN_RESULT_ERROR_USER_CONFIG = "用户邮箱信息为空";
	private static final String RETURN_RESULT_SYMBOL = ":";

	private static Props pro = PropsUtil.createFromClasspath("/mail.properties");
	private static final String MY_QQ_MAIL_SMTP = pro.getValue("myQQEmail","stmp");
	private static final String MY_WANGYI_MAIL_SMTP = pro.getValue("myWangyiEmail", "stmp");
	private static final String MY_SINA_MAIL_SMTP = pro.getValue("mySinaEmail","stmp");

	public static void main(String[] args) {
		
		 /*SendMailSession mailSession = new
		  SmtpServer("smtp.sina.com","zhaolestyle",
		  "zl1990o6o6zl").createSession();
		  Email email = Email.create();
		  email.from("zhaolestyle@sina.com").to("345786999@qq.com");
		  email.addText("1111"); 
		  mailSession.open();
		  mailSession.sendMail(email); mailSession.close();*/
		 
		MailUserEntity aa = new MailUserEntity();
		aa.setUserAdress("zhaolestyle@sina.com");
		aa.setUserAdressPass("zl1990o6o6zl");
		sendMail(aa, "2323", "321323", "936174732@qq.com");
		System.out.println("----------------------");
	}


	public static String sendMail(MailUserEntity userMailConfig,
			String mailTitle, String mailContext, String targetAddress) {
		if (StringUtil.isBlank(mailTitle))
			return targetAddress + RETURN_RESULT_SYMBOL
					+ RETURN_RESULT_ERROR_TITLE;
		if (StringUtil.isBlank(mailContext))
			return targetAddress + RETURN_RESULT_SYMBOL
					+ RETURN_RESULT_ERROR_CONTEXT;
		if (StringUtil.isBlank(targetAddress))
			return targetAddress + RETURN_RESULT_SYMBOL
					+ RETURN_RESULT_ERROR_ADDRESS;
		Email email = Email.create();
		email.addText(mailContext);
		email.subject(mailTitle);
		String myMailStmp = null;
		String myMailAddress = null;
		String myMailPass = null;
		if (userMailConfig != null) {
			// 获取用户自己的邮箱信息
			myMailAddress = userMailConfig.getUserAdress();
			myMailPass = userMailConfig.getUserAdressPass();
		} else {
			return RETURN_RESULT_ERROR_USER_CONFIG;
		}
		final String mailType = isMailType(myMailAddress);
		// 获取stmp
		myMailStmp = getUserStmp(mailType);
		email.from(myMailAddress).to(targetAddress);
		// qq邮箱和网易邮箱 创建session 有区别
		SendMailSession mailSession = null;
		if (mailType.toLowerCase().equals("qq")) {
			mailSession = new SmtpSslServer(myMailStmp, myMailAddress,
					myMailPass).createSession();
		} else if (mailType.toLowerCase().equals("163")||mailType.toLowerCase().equals("sina")) {
			mailSession = new SmtpServer(myMailStmp, getMailName(myMailAddress),
					myMailPass).createSession();
		}
		mailSession.open();
		mailSession.sendMail(email);
		mailSession.close();
		return RETURN_RESULT_SUCCESS;
	}

	// 判断用户选择的是什么邮箱
	private static String isMailType(String userMailAddress) {
		if (StringUtil.isNotBlank(userMailAddress)) {
			int index_symbol = userMailAddress.indexOf("@");
			// 获取@后面所有信息
			userMailAddress = userMailAddress.substring(index_symbol + 1);
			// 获取.出现的位置
			int index_point_symbol = userMailAddress.indexOf(".");
			final String mailName = userMailAddress.substring(0,
					index_point_symbol);
			return mailName;
		} else {
			return RETURN_RESULT_ERROR_ADDRESS;
		}
	}

	// 获取邮箱用户名 （针对网易邮箱）
	private static String getMailName(String userMailAddress) {
		int index_symbol = userMailAddress.indexOf("@");
			return userMailAddress.substring(0,index_symbol);
	}

	// 根据用户邮箱获取对应stmp
	private static String getUserStmp(String mailType) {

		if (mailType.toLowerCase().equals("qq")) {
			return MY_QQ_MAIL_SMTP;
		} else if (mailType.equals("163")) {
			return MY_WANGYI_MAIL_SMTP;
		} else if (mailType.toLowerCase().equals("sina")) {
			return MY_SINA_MAIL_SMTP;
		} else {
			return null;
		}
	}

}
