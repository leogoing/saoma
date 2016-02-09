package model.caiwu;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import model.Getxiangqing;

public class ggzcaiwuModel extends Model<ggzcaiwuModel> {
	public static final ggzcaiwuModel dao = new ggzcaiwuModel();




	/**
	 * 广告主存钱详情 广告主通过财务存钱，存的钱存到这个表中 id=广告主ID
	 */
	public String cunqian(Integer adid, Float Money, String dete, Integer caiwuchongzhiren, String Bank) {
		// 将获得的数据添加到数据库adrecharge表中
		Record adrecharge = new Record().set("adid", adid).set("Money", Money).set("dete", dete)
										.set("caiwuchongzhiren", caiwuchongzhiren).set("Bank", Bank);
		Db.save("adrecharge", adrecharge);
		return "1";

	}
	/**
	 * 根据广告主ID（ADID）查询该广告主的充值记录
	 */
	public List<ggzcaiwuModel> adidchongzhijilu(int adidchongzhijilu){
		//获得指定的广告主名字
		List<ggzcaiwuModel> ggzquan = ggzcaiwuModel.dao.find("select id,Money,dete,caiwuchongzhiren from adrecharge where  adid ="+adidchongzhijilu+"");
		return ggzquan;
	}
}
