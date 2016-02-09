package controller.ggz;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.channels.CancelledKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.MultipartRequest;
import com.jfinal.upload.UploadFile;
import com.utils.GGetCaidan;
import com.utils.StringUtils;
import com.utils.erweima.DuiBiTime;
import com.utils.erweima.GetToken;
import com.utils.weixin.util_access_token;
import com.utils.weixin.util_caidan;

import model.GuanggaozhuModel;
import util.GetTime;
import util.GetURL;

/**
 * zz 这是广告主页面
 *
 */
public class GunaggaozhuController extends Controller {
	/**
	 * 
	 * // 获取平台信息并跳转到平台修改页面
	 */
	public void ToUpdate() {
		// 调用Model层进行查询
		Integer id = getParaToInt("id");
		// 将平台查出来
		Record pingtai = GuanggaozhuModel.dao.GetPingtaiById(id);
		// 获得是否使用我们的服务器的值
		Integer useserver = pingtai.getInt("useserver");
		String fuwuqiURL=null;
		// 如果是使用的我们的服务器
		if(useserver==1){
			// 从查出来的平台获取appid和appsecret
			String appid = pingtai.getStr("appid");
			String appsecret = pingtai.getStr("appsecret");
			String token =pingtai.getStr("token");
			// 根据配置文件获得自己的服务器路径
			String weixinURL = PropKit.use("a_little_config.txt").get("weixin.url");
			//获得要天的服务器路径
			fuwuqiURL = GetURL.getURLSecond(weixinURL, appid, appsecret,token);
			try {
				// 获取新的access_token
				String access_token = util_access_token.getAccess_token(appid, appsecret);
				// 获取当前时间
				String nowTime = GetTime.getTime();
				// 将新的access_token和token_time存入数据库
				boolean b = GuanggaozhuModel.dao.UpdatePingtaiAccess_token(id, access_token, nowTime);
			} catch (Exception e) {
			}
		}
		// 再将更新过的平台信息查出来
		Record newpingtai = GuanggaozhuModel.dao.GetPingtaiById(id);
		setAttr("fuwuqiURL", fuwuqiURL);
		setSessionAttr("fuwuqiURL", fuwuqiURL);
		// 在SESSION中也存放一个数据对象，方便随时调用
		setSessionAttr("pingtai", newpingtai);
		Double totalsp = newpingtai.getDouble("totalsp");
		String stringtotalsp = totalsp.toString();
		setAttr("stringtotalsp", stringtotalsp);
		// 将单个对象数据放到作用域中并跳转到更新页面
		setAttr("pingtai", newpingtai);
		render("/ggz/update.html");
	}

	/**
	 * 
	 * // 到回复语更新界面
	 */
	public void ToHuifuUpdate() {
		// 从session中把平台查出来
		Record pingtai = getSessionAttr("pingtai");
		String imgurl = pingtai.getStr("imgurl");
		String url1 = StringUtils.substringAfter(imgurl, "//");
		String url2 = StringUtils.substringAfter(url1, "/");
		String newimgurl = StringUtils.substringAfter(url2, "/");
		Integer id = pingtai.getInt("id");
		System.out.println("这是跳转时查出的图片路径截取后的新闻图片路径：" + url2);
		setAttr("newsImgUrl", url2);
		setAttr("pingtai", pingtai);
		render("/ggz/huifuyuupdate.html");
	}

	/**
	 * 
	 * // 从图片管理页面回到平台修改页面
	 */

	public void ToUpdateFromOther() {
		// 从session中获得平台信息

		Record pingtai = getSessionAttr("pingtai");
		Integer useserver = pingtai.getInt("useserver");
		// 将查出来的Double类型价格进行类型转换，便于显示
		Double totalsp = pingtai.getDouble("totalsp");
		String stringtotalsp = totalsp.toString();
		String fuwuqiURL=getSessionAttr("fuwuqiURL");
		setAttr("fuwuqiURL", fuwuqiURL);
		setAttr("stringtotalsp", stringtotalsp);
		setAttr("pingtai", pingtai);
		render("/ggz/update.html");
	}

	/**
	 * 
	 * // 修改平台信息
	 */
	public void UpdatePingtai() {

		// 接受页面传递参数
		Integer id = getParaToInt("id");
		Integer adtime = getParaToInt("adtime");
		int adtime1=adtime * 3;
		System.out.println(adtime1);
		String totalsp1 = getPara("totalsp");
		Integer uplowframe = getParaToInt("uplowframe");
		Integer informationtype = getParaToInt("h3");
		String informationurl = getPara("h2");
		//如果上下架为空则默认下架
		if(uplowframe==null){
			uplowframe=2;
		}
		Integer adtype = getParaToInt("adtype");
		String ptname = getPara("ptname");
		String serverIP = getPara("serverIP");
		// 将接收到的价格转化为Double类型
		Double totalsp = Double.parseDouble(totalsp1);
		// 如果一天后下架
		if (uplowframe==3) {
			//获得明天的时间
			String downtime = GetTime.getNextDay();
			// 更新
			Boolean i = GuanggaozhuModel.dao.UpdatePingtai(id, adtime1, totalsp, adtype, uplowframe, ptname,
					serverIP,downtime,informationtype,informationurl,adtime);
			// 增加返回信息
			setAttr("msg", "更新成功");
		}
		//如果继续下架
		if(uplowframe==2){
			//下架时间为空
			String downtime = null;
			// 更新
			Boolean i = GuanggaozhuModel.dao.UpdatePingtai(id, adtime1, totalsp, adtype, uplowframe, ptname,
					serverIP,downtime,informationtype,informationurl,adtime);
			// 增加返回信息
			setAttr("msg", "更新成功");
		}
		// 如果上架
		if (uplowframe==1) {
			//下架时间为空
			String downtime = null;
			// 获得广告主ID
			Integer userId = getSessionAttr("currentUserId");
			// 获得广告主余额
			Double balance = GuanggaozhuModel.dao.GetBalanceByUserId(userId);
			// 如果余额大于0元则继续上架
			if (balance > 0) {
				// 更新
				Boolean i = GuanggaozhuModel.dao.UpdatePingtai(id, adtime1, totalsp, adtype, uplowframe, ptname,
						serverIP,downtime,informationtype,informationurl,adtime);
				// 增加返回信息
				setAttr("msg", "更新成功");
			}
			// 余额不足0元则不能上架
			else {
				setAttr("msg", "您的余额已不足0元,请充值后上架平台");
			}
		}
		// 再将保存的信息查出来
		Record pingtai = GuanggaozhuModel.dao.GetPingtaiById(id);
		Double b = pingtai.getDouble("totalsp");
		System.out.println("这是查出来的平台价格：" + b);
		String stringtotalsp = b.toString();
		System.out.println("这是转换String后的价格：" + stringtotalsp);
		setAttr("stringtotalsp", stringtotalsp);
		// 更新SESSION中的平台
		setSessionAttr("pingtai", pingtai);
		setAttr("pingtai", pingtai);
		// 更新成功返回本页面
		render("/ggz/update.html");
	}

	/**
	 * 
	 * // 回复语更新
	 */
	public void UpdateHuifuyu() {
		// 根据配置文件获得自己的服务器路径
		String weixinURL = PropKit.use("a_little_config.txt").get("weixin.url");
		// 获得session中的pingtai
		Record pingtai = getSessionAttr("pingtai");
		Integer id = pingtai.getInt("id");
		String publicaccountname =pingtai.getStr("publicaccountname");
		String newsimgurl = pingtai.getStr("imgurl");
		// 先获得接受文件
		UploadFile newsimg = getFile("newsimg", PathKit.getWebRootPath() + "/newsImages/" + publicaccountname);
		if (newsimg != null) {
			try {
				// 获取当前时间
				String nowtime = GetTime.getSSStime();
				// 给文件改名后保存
				newsimg.getFile().renameTo(
						new File(PathKit.getWebRootPath() + "/newsImages/" + publicaccountname + "/" + nowtime + ".jpg"));
				System.out.println("这是新的新闻图片保存地址:" + PathKit.getWebRootPath() + "/newsImages/" + publicaccountname + "/"
						+ nowtime + ".jpg");
				// 获得项目在服务器上的路径
				String xiangmuURL = StringUtils.substringBeforeLast(weixinURL, "/");
				// 拼接图片网络路径
				newsimgurl = xiangmuURL + "/newsImages/" + publicaccountname + "/" + nowtime + ".jpg";
			} catch (Exception e) {
			}
		}
		// 获得页面传来的数据
		Integer newstype = getParaToInt("newstype");
		String huifuyu = getPara("huifuyu");
		String title = getPara("title");
		String miaoshu = getPara("miaoshu");
		String newsurl = getPara("newsurl");
		// 保存到数据库
		boolean i = GuanggaozhuModel.dao.UpdateHuifuyu(id, newsimgurl, newstype, huifuyu, title, miaoshu, newsurl);
		// 再将更新过后的平台查出来
		Record newpingtai = GuanggaozhuModel.dao.GetPingtaiById(id);
		// 截取图片路径便于显示
		String imgurl = newpingtai.getStr("imgurl");
		String imgName = StringUtils.substringAfterLast(imgurl, "/");
		String newimgurl = "newsImages/" + publicaccountname + "/" + imgName;
		System.out.println("这是更新完后再次从数据库查出截取后的新闻图片路径：" + newimgurl);
		setAttr("newsImgUrl", newimgurl);
		// 更新SESSION中的平台
		setSessionAttr("pingtai", newpingtai);
		// 将平台放至作用域
		setAttr("pingtai", newpingtai);
		render("/ggz/HuifuyuUPdate.html");
	}

	/**
	 * 此处有BUG：计算的是从扫码到现在都没有取消关注的扫码总量，无法根据关注时间的有效期来计算
	 * 
	 * @param id
	 *            // 获取所有平台的信息
	 */

	public void GetTotal() {
		Integer userId = getSessionAttr("currentUserId");
		// 调用Model层进行查询所有平台信息
		List<Record> list = GuanggaozhuModel.dao.GetPingtaiByUserId(userId);
		setAttr("msg", "success");
		// 将所有平台信息放到作用域
		setAttr("list", list);
		render("/ggz.html");
	}

	/**
	 * 获取所有平台的花费和关注度以广告主余额
	 */
	public void GetCostAndAttention() {
		Integer userId = getSessionAttr("currentUserId");
		// 调用model层获得所有平台一段时间内的关注度
		Record count = GuanggaozhuModel.dao.GetAllTotalCount(userId);
		// 调用model获得所有品台一段时间内的花费
		Record cost = GuanggaozhuModel.dao.GetAllTotalCost(userId);
		// 查询广告主余额
		Double balance = GuanggaozhuModel.dao.GetBalanceByUserId(userId);
		// 将关注度放到作用域
		setAttr("count", count);
		// 将花费放到作用域
		setAttr("cost", cost);
		// 将余额放到作用域
		setAttr("balance", balance);
		// 在session中也放一个余额
		setSessionAttr("balance", balance);
		render("/ggz/xiangxixinxi.html");

	}
	

	/**
	 * 
	 * // 获得一个平台的关注度和花费
	 */
	public void GetOneTotal() {
		String publicaccountname = getPara("publicaccountname");
		// 获得关注度
		Record count = GuanggaozhuModel.dao.GetTotalCount(publicaccountname);
		// 查数据库获得花费
		Record todayCost = GuanggaozhuModel.dao.GetTotalCostToday(publicaccountname);
		Record yesterdayCost = GuanggaozhuModel.dao.GetTotalCostYesterday(publicaccountname);
		Record weekCost = GuanggaozhuModel.dao.GetTotalCostThisWeek(publicaccountname);
		Record monthCost = GuanggaozhuModel.dao.GetTotalCostThisMoth(publicaccountname);
		Record allCost = GuanggaozhuModel.dao.GetTotalCostAlltime(publicaccountname);
		// 获得花费的数值
		Double today = todayCost.getDouble("total");
		Double yesterday = yesterdayCost.getDouble("total");
		Double week = weekCost.getDouble("total");
		Double month = monthCost.getDouble("total");
		Double all = allCost.getDouble("total");

		// 将花费放到一个作用域便于显示
		Record cost = new Record();
		cost.set("today", today);
		cost.set("yesterday", yesterday);
		cost.set("week", week);
		cost.set("month", month);
		cost.set("all", all);

		setAttr("count", count);
		setAttr("cost", cost);
		render("/ggz/Info.html");
	}

	/**
	 * 
	 * // 增加平台信息
	 */

	public void AddPingtai() {	

		// 根据配置文件获得自己的服务器路径
		String weixinURL = PropKit.use("a_little_config.txt").get("weixin.url");
		// 创建新闻图片路径,没有上传则保存空
		String imgpath = null;
		// 先获得文件流
		UploadFile img = getFile("img", PathKit.getWebRootPath() + "/newsImages");
		String publicaccountname = getPara("publicaccountname");
		// 文件流不为空则上传图片
		if (img != null) {
			try {
				// 获得当前时间
				String nowTime = GetTime.getSSStime();
				// 在服务器目录下创建一个文件夹，文件夹名为平台原始ID，将图片改名为当前时间放入文件夹中
				File file =new File(PathKit.getWebRootPath() + "/newsImages/"+publicaccountname);
				file.mkdirs();
				File fi = new File(PathKit.getWebRootPath() + "/newsImages/"+publicaccountname+"/" + nowTime + ".jpg");
				img.getFile().renameTo(fi);
				// 获得图片名称
				String imgname = nowTime + ".jpg";
				// 获得项目在服务器上的路径
				String xiangmuURL = StringUtils.substringBeforeLast(weixinURL, "/");
				// 拼接图片网络地址并保存到数据库
				imgpath = xiangmuURL + "/newsImages/"+publicaccountname+"/" + imgname;
			} catch (Exception e) {
			}
		}
		System.out.println("这是上传新闻图片:" + imgpath);
		// 获取页面的参数
		Integer adId = getSessionAttr("currentUserId");

		// 使用我们的服务器则useserver自动为1
		Integer useserver = 1;

		Integer newstype = getParaToInt("newstype");
		String title = getPara("title");
		String miaoshu = getPara("miaoshu");
		String newsurl = getPara("newsurl");
		
		String name = getPara("name");
		String appid = getPara("appid");
		String appsecret = getPara("appsecret");
		String stringtotalsp = getPara("totalsp");
		// 将接收到的价格转化为Double类型
		Double totalsp = Double.parseDouble(stringtotalsp);
		String huifuyu = getPara("huifuyu");
		Integer adtime = getParaToInt("adtime");
		
		
		int adtime1=adtime*3;
		Integer uplowframe = getParaToInt("uplowframe");
		Integer adtype = getParaToInt("adtype");
		// 将填入的数据先放到作用域里，方便页面跳转时回填
		setAttr("newstype", newstype);
		setAttr("title", title);
		setAttr("miaoshu", miaoshu);
		setAttr("newsurl", newsurl);
		setAttr("publicaccountname", publicaccountname);
		setAttr("name", name);
		setAttr("appid", appid);
		setAttr("appsecret", appsecret);
		setAttr("stringtotalsp", stringtotalsp);
		setAttr("huifuyu", huifuyu);
		setAttr("adtime", adtime);
		setAttr("uplowframe", uplowframe);
		setAttr("adtype", adtype);

		// 查询数据库中是否已存在该平台
		GuanggaozhuModel newpingtai = GuanggaozhuModel.dao.FindPingtaiByPublicaccountname(publicaccountname);

		// 判断是否为空
		if ("".equals(appid) || "".equals(appsecret) || "".equals(name) || "".equals(adtype)
				|| "".equals(publicaccountname)) {
			// 为空则返回页面
			renderJson("Addmsg", "平台名称，appid，appsecret,微信平台号不能为空");
		} else {
			// 判断平台是否存在，不存在则继续新增
			if (newpingtai == null) {
				// 获得access_token
				String access_token = "1";
				try {
					access_token = util_access_token.getAccess_token(appid, appsecret);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				// 判断appid或appsecret是否正确
				if (access_token != "1") {// 不为1则正确
					// 获得当前时间作为token_time
					String token_time = GetTime.getTime();
					// 获取服务器地址
					String fuwuqiURL = GetURL.getURL(weixinURL, appid, appsecret);
					// 从服务器地址中截取token
					String token = StringUtils.substringAfterLast(fuwuqiURL, ":");
					// 将平台数据存入到数据库
					GuanggaozhuModel.dao.AddPintai(adId, publicaccountname, name, appid, appsecret, adtime1, totalsp,
							uplowframe, adtype, huifuyu, access_token, token_time, token, imgpath, title, miaoshu,
							newsurl, newstype, useserver,adtime);
					// 获取菜单
					String createCaidan = util_caidan.createCaidan(access_token);
					// 再将新增的平台查出来
					Record pingtai = GuanggaozhuModel.dao.FindPingtaiByAppidAndAppsecret(appid, appsecret);
					// 降低至填到作用域
					setAttr("fuwuqiURL", fuwuqiURL);
					// 在sessin中也放一个方便随时显示
					setSessionAttr("fuwuqiURL", fuwuqiURL);
					setAttr("Addmsg", "提示填写URL");
					// 在Session中也放一个方便随时调用
					setSessionAttr("pingtai", pingtai);
					// 将接受到的数据回填到对象中，方便前台数据回填
					setAttr("pingtai", pingtai);
					// 获得根据appid和appsecret获得URL
					render("/ggz/update.html");
				}
				// 为1则错误
				else {
					setAttr("Addmsg", "appid或appsecret填写错误");
					render("/ggz/Add.html");
				}

			} else {
				setAttr("Addmsg", "该平台已存在");
				render("/ggz/Add.html");
			}

		}

	}

	/**
	 * 
	 * // 删除平台
	 */

	public void DeletPingtai() {
		Integer id = getParaToInt("id");
		boolean b = GuanggaozhuModel.dao.DeletPingtaiById(id);

		render("/ggz.html");
	}
	
	/**
	 *到选择新增平台页面
	 */
	
	public void XingZeng(){
		Integer userId = getSessionAttr("currentUserId");
		// 获取广告主余额
		Double balance = GuanggaozhuModel.dao.GetBalanceByUserId(userId);
		// 如果余额大于500则到新增页面
		if (balance >= 500) {
			render("/ggz/xingzeng.html");
		}
		// 如果余额小于500则返回主页
		else {
			setAttr("Addmsg", "余额不足500元，请充值后添加平台");
			render("/ggz.html");
		}
		
	}
	/**
	 * 
	 * // 跳转平台新增页面(需要我们的服务器)
	 */
	public void ToOurAdd() {
			render("/ggz/Add.html");
	}
	

	/**
	 * 
	 * // 跳转平台新增页面（用户有自己的服务器）
	 */
	public void ToOtherAdd() {
			render("/ggz/OtherAdd.html");
	}

	/**
	 * // 增加平台信息（用户有自己的服务器)
	 * 
	 */
	public void AddPingtaiBySelf() {

		Integer adId = getSessionAttr("currentUserId");
		// 接受页面参数
		String publicaccountname = getPara("publicaccountname");
		String serverIP = getPara("serverIP");
		String name = getPara("name");
		String stringtotalsp = getPara("totalsp");
		// 将接收到的价格转化为Double类型
		Double totalsp = Double.parseDouble(stringtotalsp);

		Integer adtime = getParaToInt("adtime");
		
		
		Integer uplowframe = getParaToInt("uplowframe");
		Integer adtype = getParaToInt("adtype");
		Integer useserver = 0;
		// 再将数据回填方便失败时刷新页面
		setAttr("publicaccountname", publicaccountname);
		setAttr("serverIP", serverIP);
		setAttr("name", name);
		setAttr("stringtotalsp", stringtotalsp);
		setAttr("adtime", adtime);
		setAttr("uplowframe", uplowframe);
		setAttr("adtype", adtype);
		

		// 查询数据库看平台是否已存在
		GuanggaozhuModel samePingtai = GuanggaozhuModel.dao.FindPingtaiByPublicaccountname(publicaccountname);
		if (samePingtai == null) {
			// 存入数据库
			boolean b = GuanggaozhuModel.dao.AddpingtaiBySelf(publicaccountname, name, totalsp, adtime, uplowframe,
					adtype, serverIP, useserver, adId);
			// 在将新建的平台查出来
			Record pingtai = GuanggaozhuModel.dao.FindPingtaiByPublicaccountnameNotNull(publicaccountname);
			setSessionAttr("pingtai", pingtai);

			setAttr("pingtai", pingtai);
			setAttr("msg", "新增平台成功");
			render("/ggz/update.html");
		} else {
			setAttr("Addmsg", "该平台已存在");
			render("/ggz/OtherAdd.html");
		}
	}


	/**
	 * 
	 * // 接受上传图片保存到指定路径，并记录到数据库
	 */
	public void AddImg() {
		// 从session中获得平台信息
		Record pingtai = getSessionAttr("pingtai");
		// 获得平台ID
		Integer pingtaiId = pingtai.getInt("id");
		String publicaccountname =pingtai.getStr("publicaccountname");
		// 先获得文件流
		UploadFile img = getFile("img", PathKit.getWebRootPath() + "/upload/" + publicaccountname);
		// 如果文件流不为空则上传图片
		if (img != null) {
			try {
				// 获得当前时间
				String nowTime = GetTime.getSSStime();
				// 在服务器目录下创建一个文件夹，文件夹名为平台ID，将图片改名为当前时间放入文件夹中
				File fi = new File(
						PathKit.getWebRootPath() + "/upload/" + publicaccountname + "/" + nowTime + ".jpg");
				img.getFile().renameTo(fi);
				// 获得图片名称
				String name = nowTime + ".jpg";
				// 获得图片保存地址
				String path = PathKit.getWebRootPath() + "/upload/" + publicaccountname + "/" + name;
				//将图片改变尺寸再保存
				com.utils.ImageCompose.ChangeSize(path);
				// 将图片信息保存到数据库
				GuanggaozhuModel.dao.SaveImg(pingtaiId, path, name);
				setAttr("pingtai", pingtai);
				render("/ggz/Imgs.html");
			} catch (Exception e) {
				// 上传失败则返回本页面
				setAttr("pingtai", pingtai);
				render("/ggz/Imgs.html");
			}
		} else {
			// 为空则直接返回
			setAttr("pingtai", pingtai);
			render("/ggz/Imgs.html");
		}

	}

	/**
	 * 查询图片信息并跳转到图片列表页面
	 */
	public void ToImgs() {

		Record pingtai = getSessionAttr("pingtai");
		Integer id = pingtai.getInt("id");
		List<Record> imgs = GuanggaozhuModel.dao.findAllImgs(id);
		for (int i = 0; i < imgs.size(); i++) {
			String realpath = imgs.get(i).getStr("pictureURL");
			String path = StringUtils.substringAfter(realpath, "/");
			imgs.get(i).set("pictureURL", path);
		}
		setAttr("pingtai", pingtai);
		setAttr("ImgGetMsg", "查询");
		setAttr("imgs", imgs);
		render("/ggz/Imgs.html");
	}
	/**
	 * 到需求页面
	 */
	public void ToXuqiu(){
		Record pingtai = getSessionAttr("pingtai");
		setAttr("pingtai", pingtai);
		render("/ggz/xuqiu.html");
	}
	/**
	 * 从服务器器删除图片并在数据库删除记录
	 */
	public void DeleteImg() {
		Record pingtai = getSessionAttr("pingtai");
		Integer id = getParaToInt("id");
		Record r = GuanggaozhuModel.dao.GetImgById(id);
		String path = r.getStr("pictureURL");
		File f = new File(path);
		try {
			f.delete();
		} catch (Exception e) {
			setAttr("pingtai", pingtai);
			setAttr("ImgDelMsg", "从服务器删除失败");
			render("/ggz/Imgs.html");
		}
		boolean b = GuanggaozhuModel.dao.DeleteImgById(id);
		if (b) {
			setAttr("pingtai", pingtai);
			setAttr("ImgDelMsg", "数据库删除失败");
			render("/ggz/Imgs.html");
		} else {
			setAttr("pingtai", pingtai);
			render("/ggz/Imgs.html");
		}
	}

	/**
	 * 
	 * 将平台的背景图和二维码进行合成
	 */
	public void ImageCompose() {
		Integer id = getParaToInt("id");
		// 取出该平台
		Record pingtai = GuanggaozhuModel.dao.GetPingtaiById(id);
		String appId = pingtai.getStr("appid");
		String appsecret = pingtai.getStr("appsecret");
		String Token = pingtai.getStr("token");
		String publicaccountname = pingtai.getStr("publicaccountname");
		// 获得背景图片地址
		List<Record> oldList = GuanggaozhuModel.dao.findAllImgs(id);
		// 创建新图片地址的list
		ArrayList<HashMap<String, String>> newList = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < oldList.size(); i++) {
			String tuURL = oldList.get(i).get("pictureURL");
			// 获得二维码图片地址
			// String erweimaURL =GetEWM.linshiEWM(appId, appsecret, Token,
			// id, publicaccountname);
			String erweimaURL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQG/7zoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL3YwVV9qYWprUDF4R2ZLczZ5bXNfAAIEb7FJVgMEgDoJAA==";
			// 合成图片获得全部路径作为绝对路径
			String fullURL = com.utils.ImageCompose.Compose(erweimaURL, tuURL, id.toString());
			System.out.println(fullURL);
			// 截取部分路径作为相对路径
			String newURL = StringUtils.substringAfter(fullURL, "/");
			System.out.println(newURL);
			// 创建新map
			HashMap<String, String> map = new HashMap<String, String>();
			// 将绝对路径和相对路径都存到map里
			map.put("newURL", newURL);
			map.put("fullURL", fullURL);
			// 再将map放到newlist中
			newList.add(map);
		}
		// 将值防到作用域中返回页面
		setAttr("newList", newList);
		render("/ggz/ImageCompose.html");

	}

	/**
	 * 
	 * // 添加自定义菜单到数据库并返回执行结果
	 */
//	public void GetMenu() {
//		// 获取session中的平台信息
//		GuanggaozhuModel pingtai = getSessionAttr("pingtai");
//		String access_token = pingtai.getStr("access_token");
//		// 创建自定义菜单
//		String createCaidan = util_caidan.createCaidan(access_token);
//		renderJson(createCaidan);
//	}

	/**
	 * 
	 * // 下载图片
	 */

	public void Download() {
		String file = getPara("fullURL");
		System.out.println("这是从前台传过来的：" + file);
		File f = new File(file);
		renderFile(f);
	}
	
	public void UploadImage(){
		UploadController uc = new UploadController();
		UploadFile file = uc.getFile("img1", "imgname");
		
	}
	
	
	
}
