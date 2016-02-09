package hahada.model;

import com.jfinal.plugin.activerecord.Model;

public class DingyiTypeModel extends Model<DingyiTypeModel>{
	public static final DingyiTypeModel dao =new DingyiTypeModel();
	
	public void saveDingYi(DingyiTypeModel dingyiTypeModel){
		dingyiTypeModel.save();
	}
}
