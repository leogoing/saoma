package com.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import com.jfinal.kit.PathKit;

public class ImageComposeTest {

	public static String  Compose(String maImgUrl, String tuImgUrl,String pingtaiId
			) throws IOException {
		
		
		// 获取链接
		URL url = new URL(maImgUrl);
		URLConnection con = url.openConnection();
		// 不超时
		con.setConnectTimeout(0);
		// 不允许缓存
		con.setUseCaches(false);
		con.setDefaultUseCaches(false);
		InputStream is = con.getInputStream();
		// 先读入内存
		ByteArrayOutputStream buf = new ByteArrayOutputStream(8192);
		byte[] b = new byte[1024];
		int len;
		while ((len = is.read(b)) != -1) {
			buf.write(b, 0, len);
		}
		// 读二维码图片
		is = new ByteArrayInputStream(buf.toByteArray());
		BufferedImage ma_img = ImageIO.read(is);

		
		
		
		
		// 从本地读取广告图
		BufferedImage tu_img = null;
		FileInputStream fpic2 = new FileInputStream(tuImgUrl);
		try {
			tu_img = (BufferedImage) ImageIO.read(fpic2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 获取广告图宽度和长度
		int width = 530;
		int height = 850;

		
		
		
		
		// 根据广告图片大小创建画布
		BufferedImage new_img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d2 = new_img.createGraphics();
		// 在画布上载入底层图片
		g2d2.drawImage(tu_img, 0, 0, width, height, null);
		// 在画布上载入上层图片
		//依次设置左边距，上边距，右边距，下边距
		g2d2.drawImage(ma_img, (int) (width*0.29), (int) (height*0.365),
				(int) (width * 0.44), (int) (width * 0.44), null);
		// 执行合成
		g2d2.dispose();
		
		//生成新图片地址
		SimpleDateFormat sft = new SimpleDateFormat("yyyymmddhhmmssSSS");
		String nowtime =sft.format(new Date());
		//先根据平台ID在imageCompose下创建一个文件夹
		String creatNewFolder=PathKit.getWebRootPath() +"/imageComopse/"+pingtaiId;
		File f =new File(creatNewFolder);
		boolean bc = f.mkdirs();
		//再给新图片指定保存路径和名字
		String newImgUrl=PathKit.getWebRootPath() +"/imageComopse/"+pingtaiId+"/"+nowtime+".jpg";
		//为了防止前台传送时将"\"转义，将所有的"\"变成"\\"
		String real_newImgUrl=newImgUrl.replace("\\", "\\\\");
		String fffff="F:\\new123123132123.jpg";
		// 将新图输出到服务器
		try {
			ImageIO.write(new_img, "png", new File(fffff));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return real_newImgUrl;

	}
	
	public static void main(String[] args) throws IOException {
		String s =ImageComposeTest.Compose("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQFF8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL1REbFE1Ry1sRUpScFFqakZweGVXAAIEeYRoVgMEgDoJAA==", "F:\\tupian\\钱是怎么赚的.jpg", "1");
		System.out.println(s);
	}
	
}
