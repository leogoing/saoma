package model;

import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class Qrcode extends Model<Qrcode> {
	private static final long serialVersionUID = 1L;
	public static final Qrcode dao = new Qrcode();

	//public final static JSONArray getQrcodeDetail(int pageNumber, int pageSize) {
		//return convertQr(paginate(pageNumber, pageSize).getList());
	//}

	public static Page<Qrcode> paginate() {
		Page<Qrcode> levelList=dao.paginate(1, 10, "select * ", " from level");
		return levelList;
		//return Qrcode.dao.paginate(1, 1, "select * from qrcode", "");
	}

	
	/**
	 * 将List转换为JSONArray
	 * 
	 * @param qrcodes
	 * @return
	 */
	private final static JSONArray convertQr(List<Qrcode> qrcodes) {
		final Iterator<Qrcode> iter = qrcodes.iterator();
		final JSONArray resArr = new JSONArray();
		Qrcode tmpQrcode = null;
		while (iter.hasNext()) {
			tmpQrcode = iter.next();
			resArr.add(JSON.parse(tmpQrcode.toJson()));
		}
		return resArr;
	}
	
}
