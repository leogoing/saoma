package hahada.model;

import com.jfinal.plugin.activerecord.Model;

public class NewsModel extends Model<NewsModel>{
	public static final NewsModel dao =new NewsModel();
	
	public void saveNews(NewsModel newsModel){
		newsModel.save();
	}
}
