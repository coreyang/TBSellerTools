package com.coreyang.tool;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlResource {
	// 获取网络文件，转存到fileDes中，fileDes需要带文件后缀名
	public static void saveUrlFile(String fileUrl, String filePath,
			String fileDes) throws Exception {
		File filePathDir = new File(filePath);
		if (!filePathDir.isDirectory()) {
			filePathDir.mkdirs();
		}
		File toFile = new File(filePath + fileDes);
		if (toFile.exists()) {
			// throw new Exception("file exist");
			return;
		}
		toFile.createNewFile();
		FileOutputStream outImgStream = new FileOutputStream(toFile);
		outImgStream.write(getUrlFileData(fileUrl));
		outImgStream.close();
	}

	public static void saveFileDesc(String contents, String filePath,
			String fileName) throws Exception {
		File filePathDir = new File(filePath);
		if (!filePathDir.isDirectory()) {
			filePathDir.mkdirs();
		}
		File toFile = new File(filePath + fileName);
		if (toFile.exists()) {
			return;
		}
		toFile.createNewFile();
		FileOutputStream outImgStream = new FileOutputStream(toFile);
		outImgStream.write(contents.getBytes());
		outImgStream.close();
	}

	// 获取链接地址文件的byte数据
	public static byte[] getUrlFileData(String fileUrl) throws Exception {
		URL url = new URL(fileUrl);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.connect();
		InputStream cin = httpConn.getInputStream();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = cin.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		cin.close();
		byte[] fileData = outStream.toByteArray();
		outStream.close();
		return fileData;
	}

	// 获取链接地址的字符数据，wichSep是否换行标记
	public static String getUrlDetail(String urlStr, boolean withSep)
			throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.connect();
		InputStream cin = httpConn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(cin,
				CoreyangHelper.CHARSET_UTF_8));
		StringBuffer sb = new StringBuffer();
		String rl = null;
		while ((rl = reader.readLine()) != null) {
			if (withSep) {
				sb.append(rl).append(System.getProperty("line.separator"));
			} else {
				sb.append(rl);
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(UrlResource.getUrlDetail("http://www.baidu.com",
					true));
			// saveUrlFile("http://www.baidu.com/img/baidu_jgylogo3.gif",
			// "D:\\1.gif");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
