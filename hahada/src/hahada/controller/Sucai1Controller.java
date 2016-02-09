package hahada.controller;

import hahada.model.DingyiTypeModel;
import hahada.model.NewsModel;

import java.io.File;
import java.util.List;
import java.util.Random;

import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

public class Sucai1Controller extends Controller{
	public void index(){
		getFile();
		String type = getPara("type");
		
		if(type.equals("type1")){
			int l=0;
			for(int i=1;i<=8;i++){
				l++;
				UploadFile file = getFile("imgurl"+i);
				String newshead = getPara("newshead"+i).trim();
				System.out.println(newshead);
				if(file!=null && (newshead!=null || newshead.length()!=0)){
					String namename = getPara("namename");
					String newsnarration = getPara("newsnarration"+i).trim();
						String newsurl = getPara("newsurl"+i).trim();
						String name=name();
						getFile("imgurl"+i).getFile().renameTo(new File(PathKit.getWebRootPath()+"\\img",name+".jpg"));
						System.out.println(PathKit.getWebRootPath()+"\\img\\"+name+".jpg");
						NewsModel news =new NewsModel();
//						news.set("PTname", "");
						news.set("key", namename);
						news.set("head", newshead);
						news.set("imgurl", PathKit.getWebRootPath()+"\\img\\"+name+".jpg");
						news.set("miaoshu", newsnarration);
						news.set("url",newsurl);
						NewsModel.dao.saveNews(news);
						System.out.println("33");
						renderText("成功");
				}
			}
			if(l>0){
				renderText("成功");
			}else{
				renderText("数据不全");
			}
		}else if(type.equals("type2")){
			String name=name();
			String split = getFile("filename2").getFile().getName().split("\\.")[1];
			getFile("filename2").getFile().renameTo(new File(PathKit.getWebRootPath()+"\\img",name+"."+split));
			DingyiTypeModel ding =new DingyiTypeModel();
//			ding.set("PTname", "");
			ding.set("keyname", getPara("namename"));
			ding.set("type", 2);
			ding.set("voiceurl", PathKit.getWebRootPath()+"\\img\\"+name+"."+split);
			System.out.println(PathKit.getWebRootPath()+"\\img\\"+name+"."+split);
			DingyiTypeModel.dao.saveDingYi(ding);
			renderText("成功");
		}else if(type.equals("type3")){
			String name=name();
			String split = getFile("filename3").getFile().getName().split("\\.")[1];
			getFile("filename3").getFile().renameTo(new File(PathKit.getWebRootPath()+"\\img",name+"."+split));
			DingyiTypeModel ding =new DingyiTypeModel();
//			ding.set("PTname", "");
			ding.set("keyname", getPara("namename"));
			ding.set("type", 3);
			ding.set("voiceurl", PathKit.getWebRootPath()+"\\img\\"+name+"."+split);
			DingyiTypeModel.dao.saveDingYi(ding);
			System.out.println(PathKit.getWebRootPath()+"\\img\\"+name+"."+split);
			renderText("成功");
		}else{
			String name=name();
			String split = getFile("filename4").getFile().getName().split("\\.")[1];
			getFile("filename4").getFile().renameTo(new File(PathKit.getWebRootPath()+"\\img",name+"."+split));
			DingyiTypeModel ding =new DingyiTypeModel();
//			ding.set("PTname", "");
			ding.set("keyname", getPara("namename"));
			ding.set("type", 4);
			ding.set("voiceurl", PathKit.getWebRootPath()+"\\img\\"+name+"."+split);
			DingyiTypeModel.dao.saveDingYi(ding);
			System.out.println(PathKit.getWebRootPath()+"\\img\\"+name+"."+split);
			renderText("成功");
		}
	}
	private String name(){
		String currentName = System.currentTimeMillis()+"";
		currentName=currentName.substring(currentName.length()-10, currentName.length());
		Random r=new Random();
		int n=r.nextInt(100);
		return currentName+n;
	}
}
