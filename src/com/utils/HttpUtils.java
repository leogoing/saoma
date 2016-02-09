package com.utils;

import com.jfinal.kit.StringKit;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 用于处理HTTP请求的工具类
 * @author Wang
 */
public class HttpUtils {
    public static String CHARSET_ENCODING = "UTF-8";
    // private static String
    public static String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 7.0; Win32)";// ie7

    public static void main(String[] args) throws URISyntaxException, ClientProtocolException, IOException {
        String url="http://www.baidu.com/";
        String str=HttpUtils.get(url, null);
        System.out.println(str);
    }

    private static HttpClient getHttpClient(final String charset) {
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
        // 设置 默认的超时重试处理策略
        httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset == null ? CHARSET_ENCODING : charset);
        // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        return httpClient;
    }

    /**
     * 访问https的网站
     * @param httpclient
     */
    private static void enableSSL(HttpClient httpclient){
        //调用ssl
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[] { truseAllManager }, null);
            SSLSocketFactory sf = new SSLSocketFactory(sslcontext,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Scheme https = new Scheme("https", 443, sf);
            httpclient.getConnectionManager().getSchemeRegistry().register(https);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重写验证方法，取消检测ssl
     */
    private static TrustManager truseAllManager = new X509TrustManager(){
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] arg0, String arg1) {
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] arg0, String arg1) {
        }

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    } ;

    private static String _MakeURL(String p_url, Map<String, Object> params) {
        StringBuilder url = new StringBuilder(p_url);
        if(url.indexOf("?")<0)
            url.append('?');

        for(String name : params.keySet()){
            url.append('&');
            url.append(name);
            url.append('=');
            url.append(String.valueOf(params.get(name)));
            //不做URLEncoder处理
            //url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
        }
        return url.toString().replace("?&", "?");
    }
    /**
     * 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
     */
    private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
        // 自定义的恢复策略
        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            // 设置恢复策略，在发生异常时候将自动重试3次
            if (executionCount >= 3) {
                // 如果连接次数超过了最大值则停止重试
                return false;
            }
            if (exception instanceof NoHttpResponseException) {
                // 如果服务器连接失败重试
                return true;
            }
            if (exception instanceof SSLHandshakeException) {
                // 不要重试ssl连接异常
                return false;
            }
            HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
            boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
            if (!idempotent) {
                // 重试，如果请求是考虑幂等
                return true;
            }
            return false;
        }
    };

    /**
     * 使用ResponseHandler接口处理响应，HttpClient使用ResponseHandler会自动管理连接的释放，解决了对连接的释放管理
     */
    private static ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
        // 自定义响应处理
        public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String charset = EntityUtils.getContentCharSet(entity) == null ? CHARSET_ENCODING : EntityUtils.getContentCharSet(entity);
                return new String(EntityUtils.toByteArray(entity), charset);
            } else {
                return null;
            }
        }
    };

    /**
     * 使用post方法获取相关的数据
     *
     * @param url
     * @param paramsList
     * @return
     */
    public static String post(String url, List<NameValuePair> paramsList) {
        return httpRequest(url, paramsList, "POST", null);
    }

    /**
     * 使用post方法并且通过代理获取相关的数据
     *
     * @param url
     * @param paramsList
     * @param proxy
     * @return
     */
    public static String post(String url, List<NameValuePair> paramsList, HttpHost proxy) {
        return httpRequest(url, paramsList, "POST", proxy);
    }

    /**
     * 使用get方法获取相关的数据
     *
     * @param url
     * @param paramsList
     * @return
     */
    public static String get(String url, List<NameValuePair> paramsList) {
        return httpRequest(url, paramsList, "GET", null);
    }

    /**
     * 使用get方法并且通过代理获取相关的数据
     *
     * @param url
     * @param paramsList
     * @param proxy
     * @return
     */
    public static String get(String url, List<NameValuePair> paramsList, HttpHost proxy) {
        return httpRequest(url, paramsList, "GET", proxy);
    }

    /**
     * 提交数据到服务器
     *
     * @param url
     * @param paramsList
     * @param proxy
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String httpRequest(String url, List<NameValuePair> paramsList, String method, HttpHost proxy) {
        String responseStr = null;
        // 判断输入的值是是否为空
        if (null == url || "".equals(url)) {
            return null;
        }

        // 创建HttpClient实例
        HttpClient httpclient = getHttpClient(CHARSET_ENCODING);

        //判断是否是https请求
        if(url.startsWith("https")){
            enableSSL(httpclient);
        }
        String formatParams = null;
        // 将参数进行utf-8编码
        if (null != paramsList && paramsList.size() > 0) {
            formatParams = URLEncodedUtils.format(paramsList, CHARSET_ENCODING);
        }
        // 如果代理对象不为空则设置代理
        if (null != proxy) {
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        try {
            // 如果方法为Get
            if ("GET".equalsIgnoreCase(method)) {
                if (formatParams != null) {
                    url = (url.indexOf("?")) < 0 ? (url + "?" + formatParams) : (url.substring(0, url.indexOf("?") + 1) + formatParams);
                }
                HttpGet hg = new HttpGet(url);
                responseStr = httpclient.execute(hg, responseHandler);
                // 如果方法为Post
            } else if ("POST".equalsIgnoreCase(method)) {
                HttpPost hp = new HttpPost(url);
                if (formatParams != null) {
                    StringEntity entity = new StringEntity(formatParams);
                    entity.setContentType("application/x-www-form-urlencoded");
                    hp.setEntity(entity);
                }
                responseStr = httpclient.execute(hp, responseHandler);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseStr;
    }


    /**
     * 提交数据到服务器
     *
     * @param url
     * @param fileMap
     * @param stringMap
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String httpFileRequest(String url, Map<String, String> fileMap,Map<String, String> stringMap,int type, HttpHost proxy) {
        String responseStr = null;
        // 判断输入的值是是否为空
        if (null == url || "".equals(url)) {
            return null;
        }
        // 创建HttpClient实例
        HttpClient httpclient = getHttpClient(CHARSET_ENCODING);

        //判断是否是https请求
        if(url.startsWith("https")){
            enableSSL(httpclient);
        }

        // 如果代理对象不为空则设置代理
        if (null != proxy) {
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        //发送文件
        HttpPost hp = new HttpPost(url);
        MultipartEntity multiEntity = new MultipartEntity();
        try {
            //type=0是本地路径，否则是网络路径
            if(type==0){
                for (String key : fileMap.keySet()) {
                    multiEntity.addPart(key, new FileBody(new File(fileMap.get(key))));
                }
            }else{
                for (String key : fileMap.keySet()) {
                    multiEntity.addPart(key,new ByteArrayBody(getUrlFileBytes(fileMap.get(key)),key));
                }
            }
            // 加入相关参数 默认编码为utf-8
            for (String key : stringMap.keySet()) {
                multiEntity.addPart(key, new StringBody(stringMap.get(key), Charset.forName(CHARSET_ENCODING)));
            }
            hp.setEntity(multiEntity);
            responseStr = httpclient.execute(hp, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    /**
     * 将相关文件和参数提交到相关服务器
     * @param url
     * @param fileMap
     * @param stringMap
     * @return
     */
    public static String postFile(String url, Map<String, String> fileMap, Map<String, String> stringMap) {
        return httpFileRequest( url,fileMap,stringMap,0, null);
    }
    /**
     * 将相关文件和参数提交到相关服务器
     * @param url
     * @param urlMap
     * @param stringMap
     * @return
     */
    public static String postUrlFile(String url, Map<String, String> urlMap, Map<String, String> stringMap) {
        return httpFileRequest( url,urlMap,stringMap,1, null);
    }

    /**
     * 获取网络文件的字节数组
     *
     * @param url
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static byte[] getUrlFileBytes(String url) throws IOException {
        byte[] bytes = null;
        // 创建HttpClient实例
        HttpClient httpclient = getHttpClient(CHARSET_ENCODING);
        // 获取url里面的信息
        HttpGet hg = new HttpGet(url);
        HttpResponse hr = httpclient.execute(hg);
        bytes = EntityUtils.toByteArray(hr.getEntity());
        // 转换内容为字节
        return bytes;
    }

    /**
     * 获取图片的字节数组
     *
     * @createTime 2011-11-24
     * @param url
     * @return
     * @throws IOException
     * @throws ClientProtocolException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static byte[] getImg(String url) throws IOException {
        byte[] bytes = null;
        // 创建HttpClient实例
        HttpClient httpclient = getHttpClient(CHARSET_ENCODING);
        // 获取url里面的信息
        HttpGet hg = new HttpGet(url);
        HttpResponse hr = httpclient.execute(hg);
        bytes = EntityUtils.toByteArray(hr.getEntity());
        // 转换内容为字节
        return bytes;
    }

	/**
	 * 获取客户端IP地址，此方法用在proxy环境中
	 * @param req
	 * @return
	 */
	public static String getRemoteAddr(HttpServletRequest req) {
		String ip = req.getHeader("X-Forwarded-For");
		if(StringKit.notBlank(ip)){
			String[] ips = ip.split(",");
			if(ips!=null){
				for(String tmpip : ips){
					if(StringKit.isBlank(tmpip))
						continue;
					tmpip = tmpip.trim();
					if(isIPAddr(tmpip) && !tmpip.startsWith("10.") && !tmpip.startsWith("192.168.") && !"127.0.0.1".equals(tmpip)){
						return tmpip.trim();
					}
				}
			}
		}
		ip = req.getHeader("x-real-ip");
		if(isIPAddr(ip))
			return ip;
		ip = req.getRemoteAddr();
		if(ip.indexOf('.')==-1)
			ip = "127.0.0.1";
		return ip;
	}

	/**
	 * 判断是否为搜索引擎
	 * @param req
	 * @return
	 */
	public static boolean isRobot(HttpServletRequest req){
		String ua = req.getHeader("user-agent");
		if(StringKit.isBlank(ua)) return false;
		return (ua != null
				&& (ua.indexOf("Baiduspider") != -1 || ua.indexOf("Googlebot") != -1
						|| ua.indexOf("sogou") != -1
						|| ua.indexOf("sina") != -1
						|| ua.indexOf("iaskspider") != -1
						|| ua.indexOf("ia_archiver") != -1
						|| ua.indexOf("Sosospider") != -1
						|| ua.indexOf("YoudaoBot") != -1
						|| ua.indexOf("yahoo") != -1 
						|| ua.indexOf("yodao") != -1
						|| ua.indexOf("MSNBot") != -1
						|| ua.indexOf("spider") != -1
						|| ua.indexOf("Twiceler") != -1
						|| ua.indexOf("Sosoimagespider") != -1
						|| ua.indexOf("naver.com/robots") != -1
						|| ua.indexOf("Nutch") != -1
						|| ua.indexOf("spider") != -1));	
	}

	/**
	 * 获取COOKIE
	 * 
	 * @param name
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null)	return null;
		for (Cookie ck : cookies) {
			if (name.equalsIgnoreCase(ck.getName())) 
				return ck;			
		}
		return null;
	}

	/**
	 * 获取COOKIE
	 * 
	 * @param name
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null)	return null;
		for (Cookie ck : cookies) {
			if (name.equalsIgnoreCase(ck.getName())) 
				return ck.getValue();			
		}
		return null;
	}

	/**
	 * 设置COOKIE
	 * 
	 * @param name
	 * @param value
	 * @param maxAge
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name,
			String value, int maxAge) {
		setCookie(request,response,name,value,maxAge,true);
	}

	/**
	 * 设置COOKIE
	 * 
	 * @param name
	 * @param value
	 * @param maxAge
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name,
			String value, int maxAge, boolean all_sub_domain) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		if(all_sub_domain){
			String serverName = request.getServerName();
			String domain = getDomainOfServerName(serverName);
			if(domain!=null && domain.indexOf('.')!=-1){
				cookie.setDomain('.' + domain);
			}
		}
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	public static void deleteCookie(HttpServletRequest request,
			HttpServletResponse response, String name, boolean all_sub_domain) {
		setCookie(request,response,name,"",0,all_sub_domain);
	}

	/**
	 * 获取用户访问URL中的根域名
	 * 例如: www.dlog.cn -> dlog.cn
	 * @param host
	 * @return
	 */
	public static String getDomainOfServerName(String host){
		if(isIPAddr(host))
			return null;
		String[] names = host.split(".");
		int len = names.length;
		if(len==1) return null;
		if(len==3){
			return makeup(names[len-2],names[len-1]);
		}
		if(len>3){
			String dp = names[len-2];
			if(dp.equalsIgnoreCase("com")||dp.equalsIgnoreCase("gov")||dp.equalsIgnoreCase("net")||dp.equalsIgnoreCase("edu")||dp.equalsIgnoreCase("org"))
				return makeup(names[len-3],names[len-2],names[len-1]);
			else
				return makeup(names[len-2],names[len-1]);
		}
		return host;
	}

	/**
	 * 判断字符串是否是一个IP地址
	 * @param addr
	 * @return
	 */
	public static boolean isIPAddr(String addr){
		if(StringKit.isBlank(addr))
			return false;
		String[] ips = addr.split(".");
		if(ips.length != 4)
			return false;
		try{
			int ipa = Integer.parseInt(ips[0]);
			int ipb = Integer.parseInt(ips[1]);
			int ipc = Integer.parseInt(ips[2]);
			int ipd = Integer.parseInt(ips[3]);
			return ipa >= 0 && ipa <= 255 && ipb >= 0 && ipb <= 255 && ipc >= 0
					&& ipc <= 255 && ipd >= 0 && ipd <= 255;
		}catch(Exception e){}
		return false;
	}
	
	private static String makeup(String...ps){
		StringBuilder s = new StringBuilder();
		for(int idx = 0; idx < ps.length; idx++){
			if(idx > 0)
				s.append('.');
			s.append(ps[idx]);
		}
		return s.toString();
	}

	/**
	 * 获取HTTP端口
	 * @param req
	 * @return
	 * @throws java.net.MalformedURLException
	 */
	public static int getHttpPort(HttpServletRequest req) {
		try {
			return new URL(req.getRequestURL().toString()).getPort();
		} catch (MalformedURLException excp) {
			return 80;
		}
	}	

    /**
     * 取得带相同前缀的Request Parameters.
     * <p/>
     * 返回的结果的Parameter名已去除前缀.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
        Enumeration paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();
        if (prefix == null) {
            prefix = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);
                if (values == null || values.length == 0) {
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }
}
