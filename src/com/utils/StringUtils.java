package com.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * @author peter.yuan
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	public static String encode(String str, String enc)
			throws UnsupportedEncodingException {
		return URLEncoder.encode(str, enc);
	}

	public static String decode(String str, String enc)
			throws UnsupportedEncodingException {
		return URLDecoder.decode(str, enc);
	}

	/**
	 * @param email
	 *            待验证邮箱
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (isNotBlank(email)) {
			// String check =
			// "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			String check = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			return matcher.matches();
		}
		return false;

	}

	/**
	 * 手机号码验证
	 * 
	 * @param mobileNumber
	 *            待验证手机号码
	 * @return
	 */
	public static boolean isMobile(String mobileNumber) {
		if (isNotBlank(mobileNumber)) {
			String check = "^(13[0-9]{9}|15[0-9]{9}|18[0-9]{9})$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(mobileNumber);
			return matcher.matches();
		}
		return false;
	}

	public static String arraysToStr(String separator, String... strs) {
		StringBuffer sb = new StringBuffer();
		if ((strs != null) && (strs.length > 0)) {
			for (int i = 0; i < strs.length; i++) {
				sb.append(strs[i]);
				if (i < (strs.length - 1)) {
					sb.append(separator == null ? "," : separator);
				}
			}
		}
		return sb.toString();
	}

	public static String listToStr(List<Object> list, String separator) {
		StringBuffer sb = new StringBuffer();
		if ((list != null) && (list.size() > 0)) {
			for (int i = 0; i < list.size(); i++) {
				sb.append(list.get(i));
				if (i < (list.size() - 1)) {
					sb.append(separator == null ? "," : separator);
				}
			}
		}
		return sb.toString();
	}

	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String getRandomAlphabetString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString().toUpperCase();
	}

	public static String replaceHtml(String html) {
		String regEx = "<.+?>"; // 表示标签
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		return m.replaceAll("").replaceAll("\\r", "").replaceAll("\\n", "")
				.replaceAll(" ", "").replaceAll("&nbsp;", " ").trim();
	}

	public static String replaceHtmlContent(String source) {
		if (isBlank(source)) {
			return "";
		}
		String a = replace(source, " ", "&nbsp;");
		a = replace(a, "<", "&lt;");
		a = replace(a, ">", "&gt;");
		a = replace(a, "\n", "\\n");
		a = replace(a, "\r", "\\r");
		return a;
	}

	public static String substring(String str, int toCount, String more) {
		int reInt = 0;
		StringBuffer reStr = new StringBuffer();
		if (isEmpty(str)) {
			return "";
		}
		char[] tempChar = str.toCharArray();
		for (int kk = 0; ((kk < tempChar.length) && (toCount > reInt)); kk++) {
			String s1 = String.valueOf(tempChar[kk]);
			byte[] b = s1.getBytes();
			reInt += b.length;
			reStr.append(tempChar[kk]);
		}
		if ((toCount == reInt) || (toCount == (reInt - 1))) {
			reStr.append(more);
		}
		return reStr.toString();
	}

	/**
	 * 电话号码中间部分替换成*，如18*******85
	 * 
	 * @param str
	 *            电话号码
	 * @param toCount
	 *            长度
	 * @return
	 */
	public static String tel_replace(String str, int toCount) {
		int reInt = 0;
		StringBuffer reStr = new StringBuffer();
		if (isEmpty(str)) {
			return "";
		}
		char[] tempChar = str.toCharArray();
		for (int kk = 0; ((kk < tempChar.length) && (toCount > reInt)); kk++) {
			String s1 = String.valueOf(tempChar[kk]);
			byte[] b = s1.getBytes();
			if (reInt > 1 && reInt < 9) {
				reStr.append('*');
			} else {
				reStr.append(tempChar[kk]);
			}
			reInt += b.length;

		}
		return reStr.toString();
	}
	/**
	 * 把姓名替换成某某同学   李同学
	 * 
	 * @param str
	 *            姓名
	 * @param toCount
	 *            长度
	 * @return
	 */
	public static String name_replace(String str, int toCount) {
		int reInt = 0;
		StringBuffer reStr = new StringBuffer();
		if (isEmpty(str)) {
			return "";
		}
		char[] tempChar = str.toCharArray();
		for (int kk = 0; ((kk < tempChar.length) && (toCount > reInt)); kk++) {
			String s1 = String.valueOf(tempChar[kk]);
			byte[] b = s1.getBytes();
			if (reInt > 0 && reInt <2) {
				reStr.append("");
			} if (reInt>=2) {
				reStr.append("");
			}else {
				reStr.append(tempChar[kk]);
			}
			reInt += b.length;
			
		}
		return reStr.toString()+"同学";
	}

	public static String getParameter(HttpServletRequest request, String name,
			String deft) {
		String temp = request.getParameter(name);
		if (isBlank(temp) || "undefined".equalsIgnoreCase(temp)) {
			temp = deft;
		}
		return temp;
	}

	public static void main(String[] args) {
		String str = "<p><font size=\"2\"><span style=\"\" mce_style=\"font-size: 10.5pt\">"
				+ "依据绩abc效管理体系的规定，公司决定于</span><span style=\"\" mce_style=\"font-size: 10.5pt\">"
				+ "2008</span><span style=\"\" mce_style=\"font-size: 10.5pt\">年</span><span style=\"\" "
				+ "mce_style=\"font-size: 10.5pt\">12</span><span style=\"\" mce_style=\"font-size: 10.5pt\">"
				+ "月</span><span style=\"\" mce_style=\"font-size: 10.5pt\">22</span><span style=\"\" "
				+ "mce_style=\"font-size: 10.5pt\">日</span><span style=\"\" mce_style=\"font-size: 10.5pt\">"
				+ "-2009</span><span style=\"\" mce_style=\"font-size: 10.5pt\">年</span><span style=\" "
				+ "mce_style=\"font-size: 10.5pt\">1</span><span style=\"\" mce_style=\"font-size: 10.5pt\">"
				+ "月</span><span style=\"\" mce_style=\"font-size: 10.5pt\"> 23</span><span style=\"\" "
				+ "mce_style=\"font-size: 10.5pt\">日期间进行&#160;</span><span style=\"\" mce_style=\"font-size: "
				+ "10.5pt\">2008</span><span style=\"\" mce_style=\"font-size: 10.5pt\">年年度绩效考评工作，"
				+ "具体事项如下：</span></font></p>";

		System.out.println(replaceStyle(str));
		String str_text = Html2Text(str);
		System.out.println(str_text);
		System.out.println(replaceHtml2String(str));
		String slice = abbreviate(str_text, 100, "...");
		System.out.println(slice);
	}

	/**
	 * @param str
	 *            : source string
	 * @param width
	 *            : string's byte width
	 * @param ellipsis
	 *            : a string added to abbreviate string bottom
	 * @return String Object
	 */
	public static String abbreviate(String str, int width, String ellipsis) {
		if ((str == null) || "".equals(str)) {
			return "";
		}

		int d = 0; // byte length
		int n = 0; // char length
		for (; n < str.length(); n++) {
			d = str.charAt(n) > 256 ? d + 2 : d + 1;
			if (d > width) {
				break;
			}
		}

		if (d > width) {
			n = n - (ellipsis.length() / 2);
			return str.substring(0, n > 0 ? n : 0) + ellipsis;
		}

		return str = str.substring(0, n);
	}

	/**
	 * @param str
	 *            : source string
	 * @param width
	 *            : string's byte width
	 * @param ellipsis
	 *            : a string added to abbreviate string bottom
	 * @return String Object
	 */
	public static String Html2Text(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		java.util.regex.Pattern p_html1;
		java.util.regex.Matcher m_html1;

		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			String regEx_html1 = "<[^>]+";
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
			m_html1 = p_html1.matcher(htmlStr);
			htmlStr = m_html1.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;// 返回文本字符串
	}

	public static String replaceHtml2String(String html) {
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);

		html = m.replaceAll("").trim();
		html = html.replaceAll("&nbsp;", " ");
		html = html.replaceAll("&amp;", "&");
		html = html.replaceAll("&lt;", "<");
		html = html.replaceAll("&gt;", ">");
		html = html.replaceAll("&#160;", " ");
		html = html.replaceAll("&#8230;", "…");
		html = html.replaceAll("&#124;", "|");
		html = html.replaceAll("&#8220;", "“");
		html = html.replaceAll("&#8217;", "’");
		html = html.replaceAll("&#8221;", "”");

		return html;

	}

	public static String replaceStyle(String html) {
		String regEx = "style.{0,2}=.{0,2}\'*\'";
		String regEx2 = "style.{0,2}=.{0,2}\"*\"";
		Pattern p = Pattern.compile(regEx);
		Pattern p2 = Pattern.compile(regEx2);
		Matcher m = p.matcher(html);
		Matcher m2 = p2.matcher(html);
		html = m.replaceAll("").trim();
		html = m2.replaceAll("").trim();
		return html;
	}

	public static String getFirstImg(String html) {
		String regEx = "<img.+?>";
		Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(html);
		if (m.find()) {
			String htmlImg = m.group(0);
			// regEx = "'http.+?'";
			htmlImg = org.apache.commons.lang3.StringUtils.replaceChars(htmlImg,
					"'", "\"");
			regEx = "src=\"[^\"]*\"";
			p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
			m = p.matcher(htmlImg);
			if (m.find()) {
				if (org.apache.commons.lang3.StringUtils.isNotBlank(m.group(0))) {
					if (m.group(0).contains("feedads.g.doubleclick.net")) {
						return null;
					}
					if (m.group(0).contains("feedproxy.google.com")) {
						return null;
					}
					if (m.group(0).contains("blogger.googleusercontent.com")) {
						return null;
					}
					if (m.group(0).contains("ypn-rss.overture.com")) {
						return null;
					}
					if (m.group(0).contains("feeds.feedburner.com")) {
						return null;
					}
					return m.group(0).replace("src=\"", "")
							.replaceAll("\"", "");
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param arg
	 *            待转换的字符串
	 * @author m&t 将字符串转换为整型
	 * @return
	 */
	public static int string2Intger(String arg) {
		if (null != arg) {
			return Integer.parseInt(arg);
		}
		return -1;
	}
	
	
	
}
