package validator;

import model.bankModel;
import model.levelModel;
import model.userModel;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.validate.Validator;

public class AddDlsValidator extends Validator{

	@Override
	protected void validate(Controller c) {
		String idcardR="([0-9]{17}([0-9]|X))|([0-9]{15})";//身份证号验证
		String emailR="^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";//邮箱验证
		String phoneR="^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";//手机验证
		
		validateRequired("user.loginname", "loginnamemsg", "请输入用户名");
		
		validateRequired("user.password", "passwordmsg", "请输入密码");
		
		/*validateRegex("user.phone",phoneR, "phonemsg", "请输入正确的手机");
		
		validateRegex("user.email",emailR, "emailmsg", "请输入正确的邮箱");
		
		validateRegex("user.idcard", idcardR, "idcardmsg", "请输入正确的身份证号");*/
		
	}

	@Override
	protected void handleError(Controller c) {
		c.keepModel(userModel.class);
//		c.keepPara("user.loginname");
//		c.keepPara("user.password");
//		c.keepPara("user.phone");
//		c.keepPara("user.email");
//		c.keepPara("user.realname");
//		c.keepPara("user.bankinfo");
//		c.keepPara("user.bankno");
//		c.keepPara("user.idcard");
//		c.keepPara("user.leader");
		Page<levelModel> levelList=levelModel.dao.findAll();
		c.setAttr("levelList", levelList);
		Page<bankModel> bankList=bankModel.dao.findAll();
		c.setAttr("bankList", bankList);
		c.render("/guanliyuan/index.html");
		
	}

}
