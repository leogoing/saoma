package model.gly;

import java.util.List;

import model.userModel;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class User extends Model<User>{
	public static final User dao =new User();
	public Page<User> hh(int pageNumber,int size){
		Page<User> userList = dao.paginate(pageNumber,
				size, "select u.*,l.levelname",
				" from user u left join level l on u.levelid=l.id");
		return userList;
	}
	public List<User> ff(){
		List<User> ums = 
				dao.find("select id,loginname,realname from user ");
		return ums;
	}
	public Integer gg(){
		Integer userSize = dao.find("select * from user").size();
		return userSize;
	}
	public Page<User> jj(int size){
		Page<User> userList = dao.paginate(1, size,
				"select * ", " from user");
		return userList;
	}
	public List<User> kk(){
		List<User> ums = dao.find("select id,loginname,realname from user ");
		return ums;
	}
	public Page<User> qq(int pageNumber,int size){
		Page<User> userList =dao.paginate(pageNumber,size, "select u.*,l.levelname",
				" from user u left join level l on u.levelid=l.id");
		return userList;
	}
	public Page<User> ww(int pageNumber){
		Page<User> userList = dao.paginate(pageNumber,10, "select u.*,l.levelname",
				" from user u left join level l on u.levelid=l.id");
		return userList;
	}
	public Record rr(Object loginname){
		Record txu=Db.findFirst("select * from user where loginname=?",loginname);
		return txu;
	}
}
