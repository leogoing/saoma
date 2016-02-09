package controller.tixian;

import java.util.List;

import com.jfinal.core.Controller;

import controller.tixian.model.TixianModel;

public class TixianSet extends Controller{
	public void index(){
		List<TixianModel> findTixian = TixianModel.dao.findTixian();
		renderJson(findTixian);
	}
	public void setTixian(){
		
		TixianModel.dao.dropTable();
		for(int i=1;i<10;i++){
			String name = getPara("name"+i);
			if(name!=null &&  !name.equals("")){
				String value = getPara("value"+i);
				TixianModel t=new TixianModel();
				t.set("money", name);
				t.set("charge", value);
				TixianModel.dao.saveTixian(t);
			}else{
				return;
			}
		}
		renderJson(1);
	}
	
	public void yue(){
		int  remainder = getParaToInt("remainder");
		List<TixianModel> findTixian = TixianModel.dao. findTixian();
		int kk=0;
		for(int i=0;i<findTixian.size();i++){
			TixianModel tixianModel = findTixian.get(i);
			Integer int1 = tixianModel.getInt("money");
			if(remainder>int1){
				kk+=1;
			}
		}
		TixianModel tixianModel = findTixian.get(kk);
		String mun = tixianModel.get("charge");
		if(kk==0){
			mun="-1";
		}
		renderJson(mun);
	}
	

}
