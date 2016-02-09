package controller.ggz;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.MultipartRequest;
import com.jfinal.upload.UploadFile;



@SuppressWarnings({"unchecked", "rawtypes"})
public class UploadController extends Controller{
	
	private HttpServletRequest request;
	/**
	 *直接将图片保存到upload文件夹
	 */
		@Override
		public UploadFile getFile(String imgparam,String nameparam) {
			//获得保存文件夹
			String defaultpath=PathKit.getWebRootPath()+"/upload/";
			//获得上传文件
			List<UploadFile> uploadFiles = getFiles();
			//获得页面输入的图片名字
			String name =getPara(nameparam);
			for (UploadFile uploadFile : uploadFiles) {
				if (uploadFile.getParameterName().equals(imgparam)) {
					//重命名并保存
					uploadFile.getFile().renameTo(new File(defaultpath+name+".jpg"));
					return uploadFile;
				}
			}
			return null;
		}
		
		public List<UploadFile> getFiles() {
			System.out.println("这是请求==="+request);
			if (request instanceof MultipartRequest == false)
				request = new MultipartRequest(request);
			return ((MultipartRequest)request).getFiles();
		}

		public HttpServletRequest getRequest() {
			return request;
		}

		public void setRequest(HttpServletRequest request) {
			this.request = request;
		}
	
		
}
