package model.dls;

import com.jfinal.plugin.activerecord.Model;

public class DlsQrcodePlantModel extends Model<DlsQrcodePlantModel>{
	public static final DlsQrcodePlantModel dao = new DlsQrcodePlantModel();
	//循环出二维码的SQL语句
	public String getQrcodeInfo(){
		String ss = "select pub.*,round(cityagentsp+countryagentsp+personagentsp,2) p from publicaccount pub where uplowframe=1 ";
		return ss;
	}
}
