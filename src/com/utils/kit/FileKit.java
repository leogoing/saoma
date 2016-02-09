package com.utils.kit;

import java.io.File;

import com.jfinal.upload.UploadFile;

public class FileKit {

	/**
	 * 重命名文件
	 * 
	 * @param UploadFile
	 *            file
	 * @return String filePath
	 */
	public static File fileRename(UploadFile uploadFile) {
		File file = uploadFile.getFile();
		String newFileName = System.currentTimeMillis()+"";
		File newFile = new File(uploadFile.getSaveDirectory() + File.separator
				+ newFileName
				+ file.getName().substring(file.getName().lastIndexOf(".")));
		file.renameTo(newFile);
		return newFile;
	}

}
