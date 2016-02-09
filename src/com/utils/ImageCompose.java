package com.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.mysql.jdbc.MiniAdmin;

import util.GetTime;

public class ImageCompose {
	
	/**
	 * 
	 * @param maImgUrl二维码地址
	 * @param tuImgUrl背景图地址
	 * @param pingtaiId 平台ID(用来创建文件夹)
	 * @return
	 */
	public static String  Compose(String maImgUrl, String tuImgUrl,String pingtaiId
			)  {
		
		// 获取链接
		URL url = null;
		try {
			url = new URL(maImgUrl);
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			System.out.println("获取二维码网址链接失败");
		}
		URLConnection con = null;
		try {
			con = url.openConnection();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			System.out.println("获取二维码网址链接失败");
		}
		// 不超时
		con.setConnectTimeout(0);
		// 不允许缓存
		con.setUseCaches(false);
		con.setDefaultUseCaches(false);
		InputStream is = null;
		try {
			is = con.getInputStream();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			System.out.println("获取二维码网址链接失败");
		}
		// 先读入内存
		ByteArrayOutputStream buf = new ByteArrayOutputStream(8192);
		byte[] b = new byte[1024];
		int len;
		try {
			while ((len = is.read(b)) != -1) {
				buf.write(b, 0, len);
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			System.out.println("获取二维码网址链接失败");
		}
		// 读二维码图片
		is = new ByteArrayInputStream(buf.toByteArray());
		BufferedImage ma_img = null;
		try {
			ma_img = ImageIO.read(is);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("获取二维码网址链接失败");
		}
		
		// 从本地读取广告图
		BufferedImage tu_img = null;
		try {
			tu_img = (BufferedImage) ImageIO.read(new FileInputStream(tuImgUrl));
		} catch (IOException e) {
			System.out.println("背景图读取失败 ");
		}
		// 获取广告图宽度和长度
//		int height =tu_img.getHeight();
//		int width = tu_img.getWidth();
		int height = 850;
		int width = 530;

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
			// 将新图输出到服务器
			try {
				ImageIO.write(new_img, "png", new File(real_newImgUrl));
			} catch (FileNotFoundException e) {
				System.out.println("合成图片输出失败");
			} catch (IOException e) {
				System.out.println("合成图片输出失败");
			}
			
			try {
				ImageCompose.ChangeStorageSize(real_newImgUrl, real_newImgUrl,25);
			} catch (IOException e) {
				System.out.println("图片压缩失败");
			}
			return real_newImgUrl;
			

	}
	
	/**
	 * 
	 * @param maImgUrl 二维码图片地址
	 * @param tuImgUrl 背景图图片地址
	 * @param pingtaiName 平台原始ID
	 * @param left 左边距%
	 * @param top 上边距%
	 * @return 合成图片保存地址
	 * @throws IOException
	 */
	public static String  ComposeBySelf(String maImgUrl, String tuImgUrl,String pingtaiName,
			int left,int top) {
		
		// 获取链接
		URL url = null;
		try {
			url = new URL(maImgUrl);
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			System.out.println("获取二维码网址链接失败");
		}
		URLConnection con = null;
		try {
			con = url.openConnection();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			System.out.println("获取二维码网址链接失败");
		}
		// 不超时
		con.setConnectTimeout(0);
		// 不允许缓存
		con.setUseCaches(false);
		con.setDefaultUseCaches(false);
		InputStream is = null;
		try {
			is = con.getInputStream();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			System.out.println("获取二维码网址链接失败");
		}
		// 先读入内存
		ByteArrayOutputStream buf = new ByteArrayOutputStream(8192);
		byte[] b = new byte[1024];
		int len;
		try {
			while ((len = is.read(b)) != -1) {
				buf.write(b, 0, len);
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			System.out.println("获取二维码网址链接失败");
		}
		// 读二维码图片
		is = new ByteArrayInputStream(buf.toByteArray());
		BufferedImage ma_img = null;
		try {
			ma_img = ImageIO.read(is);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("获取二维码网址链接失败");
		}

		
		// 从本地读取广告图
		BufferedImage tu_img = null;
		try {
			tu_img = (BufferedImage) ImageIO.read(new FileInputStream(tuImgUrl));
		} catch (IOException e) {
			System.out.println("背景图读取失败 ");
		}
		// 获取广告图宽度和长度
		int height =850;
		int width = 530;
		try {
			height =tu_img.getHeight();
			width = tu_img.getWidth();
		} catch (Exception e) {
			System.out.println("获得图片尺寸失败，使用默认尺寸");
		}
			// 根据广告图片大小创建画布
			BufferedImage new_img = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d2 = new_img.createGraphics();
			// 在画布上载入底层图片
			g2d2.drawImage(tu_img, 0, 0, width, height, null);
			// 在画布上载入上层图片
			//依次设置左边距，上边距，宽，高
			g2d2.drawImage(ma_img, (int) ((width/100)*left), (int) ((height/100)*top),
			(int)(width-((width/100)*left)), (int)(height-((height/100)*top)), null);
			// 执行合成
			g2d2.dispose();
			//生成新图片地址
			SimpleDateFormat sft = new SimpleDateFormat("yyyymmddhhmmssSSS");
			String nowtime =sft.format(new Date());
			//先根据平台ID在imageCompose下创建一个文件夹
			String creatNewFolder=PathKit.getWebRootPath() +"/imageComopse/"+pingtaiName;
			File f =new File(creatNewFolder);
			boolean bc = f.mkdirs();
			//再给新图片指定保存路径和名字
			String newImgUrl=PathKit.getWebRootPath() +"/imageComopse/"+pingtaiName+"/"+nowtime+".jpg";
			//为了防止前台传送时将"\"转义，将所有的"\"变成"\\"
			String real_newImgUrl=newImgUrl.replace("\\", "\\\\");
			// 将新图输出到服务器
			try {
				ImageIO.write(new_img, "png", new File(real_newImgUrl));
			} catch (FileNotFoundException e) {
				System.out.println("合成图片输出失败");
			} catch (IOException e) {
				System.out.println("合成图片输出失败");
			}
			return real_newImgUrl;

	}
	
	/**
	 * 
	 * @param maImgUrl二维码地址
	 * @param tuImgUrl背景图地址
	 * @param pingtaiName
	 * @param left 左边距%
	 * @param top 上边距%
	 * @param foot 底部边距%
	 * @param right 右边距%
	 * @return 合成图片保存路径
	 * @throws IOException
	 */
	public static String  ComposeBySelf(String maImgUrl, String tuImgUrl,String pingtaiName,
			int left,int top,int foot,int right) throws IOException {
		
		// 获取链接
		URL url = null;
		try {
			url = new URL(maImgUrl);
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			System.out.println("获取二维码网址链接失败");
		}
		URLConnection con = null;
		try {
			con = url.openConnection();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			System.out.println("获取二维码网址链接失败");
		}
		// 不超时
		con.setConnectTimeout(0);
		// 不允许缓存
		con.setUseCaches(false);
		con.setDefaultUseCaches(false);
		InputStream is = null;
		try {
			is = con.getInputStream();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			System.out.println("获取二维码网址链接失败");
		}
		// 先读入内存
		ByteArrayOutputStream buf = new ByteArrayOutputStream(8192);
		byte[] b = new byte[1024];
		int len;
		try {
			while ((len = is.read(b)) != -1) {
				buf.write(b, 0, len);
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			System.out.println("获取二维码网址链接失败");
		}
		// 读二维码图片
		is = new ByteArrayInputStream(buf.toByteArray());
		BufferedImage ma_img = null;
		try {
			ma_img = ImageIO.read(is);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("获取二维码网址链接失败");
		}

		
		// 从本地读取广告图
		BufferedImage tu_img = null;
		try {
			tu_img = (BufferedImage) ImageIO.read(new FileInputStream(tuImgUrl));
		} catch (IOException e) {
			System.out.println("背景图读取失败 ");
		}
		// 获取广告图宽度和长度
		int height =850;
		int width = 530;
		try {
			height =tu_img.getHeight();
			width = tu_img.getWidth();
		} catch (Exception e) {
			System.out.println("获得图片尺寸失败，使用默认尺寸");
		}
			// 根据广告图片大小创建画布
			BufferedImage new_img = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d2 = new_img.createGraphics();
			// 在画布上载入底层图片
			g2d2.drawImage(tu_img, 0, 0, width, height, null);
			// 在画布上载入上层图片
			//依次设置左边距，上边距，宽，高
			g2d2.drawImage(ma_img, (int) ((width/100)*left), (int) ((height/100)*top),
			(int)(width-((width/100)*left)-((width/100)*right)), (int)(height-((height/100)*top)-((height/100)*foot)), null);
			// 执行合成
			g2d2.dispose();
			//生成新图片地址
			SimpleDateFormat sft = new SimpleDateFormat("yyyymmddhhmmssSSS");
			String nowtime =sft.format(new Date());
			//先根据平台ID在imageCompose下创建一个文件夹
			String creatNewFolder=PathKit.getWebRootPath() +"/imageComopse/"+pingtaiName;
			File f =new File(creatNewFolder);
			boolean bc = f.mkdirs();
			//再给新图片指定保存路径和名字
			String newImgUrl=PathKit.getWebRootPath() +"/imageComopse/"+pingtaiName+"/"+nowtime+".jpg";
			//为了防止前台传送时将"\"转义，将所有的"\"变成"\\"
			String real_newImgUrl=newImgUrl.replace("\\", "\\\\");
			// 将新图输出到服务器
			try {
				ImageIO.write(new_img, "png", new File(real_newImgUrl));
			} catch (FileNotFoundException e) {
				System.out.println("合成图片输出失败");
			} catch (IOException e) {
				System.out.println("合成图片输出失败");
			}
			return real_newImgUrl;

	}
	
	/**
	 * 改变图片尺寸再按原路径保存
	 * @param path 图片地址
	 */
	public static void ChangeSize(String path){
				// 从本地读取广告图
				BufferedImage old_img = null;
				FileInputStream fpic2 = null;
				try {
					fpic2 = new FileInputStream(path);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					System.out.println("图片读取失败");
				}
				try {
					old_img = (BufferedImage) ImageIO.read(fpic2);
				} catch (IOException e) {
					System.out.println("图片读取失败");
				}
				// 根据广告图片大小创建画布
				BufferedImage new_img = new BufferedImage(530, 850,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d2 = new_img.createGraphics();
				// 在画布上载入底层图片
				g2d2.drawImage(old_img, 0, 0, 530, 850, null);
				// 执行合成
				g2d2.dispose();
				// 将新图输出到本地
				try {
					ImageIO.write(new_img, "png", new File(path));
				} catch (FileNotFoundException e) {
					System.out.println("图片输出失败");
				} catch (IOException e) {
					System.out.println("图片输出失败");
				}
	}
	
	/**
	 * 改变图片尺寸，再按原路径保存
	 * @param path 图片地址
	 * @param height 图片高度
	 * @param width 图片宽度
	 */
		public static void ChangeSizeBySelf(String path,int height,int width){
					// 从本地读取广告图
					BufferedImage old_img = null;
					FileInputStream fpic2 = null;
					try {
						fpic2 = new FileInputStream(path);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						System.out.println("图片读取失败");
					}
					try {
						old_img = (BufferedImage) ImageIO.read(fpic2);
					} catch (IOException e) {
						System.out.println("图片读取失败");
					}
					// 根据广告图片大小创建画布
					BufferedImage new_img = new BufferedImage(width, height,
							BufferedImage.TYPE_INT_RGB);
					Graphics2D g2d2 = new_img.createGraphics();
					// 在画布上载入底层图片
					g2d2.drawImage(old_img, 0, 0, width, height, null);
					// 执行合成
					g2d2.dispose();
					// 将新图输出到本地
					try {
						ImageIO.write(new_img, "png", new File(path));
					} catch (FileNotFoundException e) {
						System.out.println("图片输出失败");
					} catch (IOException e) {
						System.out.println("图片输出失败");
					}
					
		}
		
		/**
		 * 改变图片储存大小
		 * @param srcFilePath图片路径
		 * @param descFilePath图片保存地址
		 * @param scaling 压缩比(0~100的整数)
		 * @return 
		 * @throws IOException
		 */
		public static boolean ChangeStorageSize(String srcFilePath, String descFilePath,int scaling) throws IOException {
	        File file = null;
	        BufferedImage src = null;
	        FileOutputStream out = null;
	        ImageWriter imgWrier;
	        ImageWriteParam imgWriteParams;

	        // 指定写图片的方式为 jpg
	        imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
	        imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(
	                null);
	        // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
	        imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);
	        // 这里指定压缩的程度，参数qality是取值0~1范围内，
	       float c =(float) (scaling*0.01);
	        imgWriteParams.setCompressionQuality(c);
	        imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);
	        ColorModel colorModel =ImageIO.read(new File(srcFilePath)).getColorModel();// ColorModel.getRGBdefault();
	        // 指定压缩时使用的色彩模式
//	        imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(
//	                colorModel, colorModel.createCompatibleSampleModel(16, 16)));
	        imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(
	                colorModel, colorModel.createCompatibleSampleModel(32, 32)));

	        try {
	            if (isBlank(srcFilePath)) {
	                return false;
	            } else {
	                file = new File(srcFilePath);System.out.println(file.length());
	                src = ImageIO.read(file);
	                out = new FileOutputStream(descFilePath);
	                imgWrier.reset();
	                // 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何
	                // OutputStream构造
	                imgWrier.setOutput(ImageIO.createImageOutputStream(out));
	                // 调用write方法，就可以向输入流写图片
	                imgWrier.write(null, new IIOImage(src, null, null),
	                        imgWriteParams);
	                out.flush();
	                out.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	        return true;
	    }
		
		/**
		 * 判断路径是否为空
		 * @param string 路径
		 * @return
		 */
		public static boolean isBlank(String string) {
	        if (string == null || string.length() == 0 || string.trim().equals("")) {
	            return true;
	        }
	        return false;
	    }
	
	public static void main(String[] args) throws IOException {
		float a =(float) (20*0.01);
		System.out.println(a);
	}
}
