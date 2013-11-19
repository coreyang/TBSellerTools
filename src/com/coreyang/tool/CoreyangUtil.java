package com.coreyang.tool;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import cpdetector.io.ASCIIDetector;
import cpdetector.io.CodepageDetectorProxy;
import cpdetector.io.JChardetFacade;
import cpdetector.io.ParsingDetector;
import cpdetector.io.UnicodeDetector;

public class CoreyangUtil {
	
	public static final Logger logger = Logger.getLogger(CoreyangUtil.class);
	
	/**
	 * 获取输入字符串并且check
	 * @param strType
	 * @param checkFlag
	 * @return
	 */
	public static String getInputStr(String strType, boolean checkFlag){
		String readLine = readInput();
		if(checkFlag){
			if(strType.equals(CoreyangHelper.INPUT_TYPE_FILE)){
				//check路径类型
				if(filePathPreProc(readLine)){
					return readLine;
				}else{
					System.out.println("路径输入有误，请重新输入：");
					return getInputStr(strType, checkFlag);
				}
			}else if(strType.equals(CoreyangHelper.INPUT_TYPE_NUMBER)){
				//check数字类型
				if(isNumberStr(readLine)){
					return readLine;
				}else{
					System.out.println("输入格式有误，请重新输入：");
					return getInputStr(strType, checkFlag);
				}
			}else{
				if(null!=readLine&&!"".equals(readLine)){
					return readLine;
				}else{
					System.out.println("输入格式有误，请重新输入：");
					return getInputStr(strType, checkFlag);
				}
			}
		}else{
			return readLine;
		}
	}
	
	/**
	 * 读取键盘输入，只读一行
	 * @return
	 */
	public static String readInput(){
		InputStream is = System.in;
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String readLine = null;
		try {
			readLine = br.readLine();
			logger.info("user input :"+readLine);
			if(null!=readLine&&"bye".equals(readLine.trim())){
				//捕捉到Bye，直接退出系统。
				System.exit(0);
			}
		} catch (IOException e) {
			logger.error(e);
		}
		return readLine;
	}
	
	/**
	 * 判断输入的是否为路径
	 * @param pathstr
	 * @return
	 */
	public static boolean filePathPreProc(String pathstr){
		if(null!=pathstr&&!"".equals(pathstr)){
			pathstr=pathstr.replaceAll("\\\\", "/").trim();
	        java.util.regex.Pattern p=java.util.regex.Pattern.compile("(^\\.|^/|^[a-zA-Z])?:?/.+(/$)?");
	        java.util.regex.Matcher m=p.matcher(pathstr);
	        //不符合要求直接返回
	        if(!m.matches()){
	            return false;
	        }
	        return true;
		}else
			return false;
        
    }
	
	/**
	 * 判断是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumberStr(String readLine){
		if(null!=readLine&&!"".equals(readLine)){
			readLine=readLine.trim();
	        java.util.regex.Pattern p=java.util.regex.Pattern.compile("^[0-9]*$");
	        java.util.regex.Matcher m=p.matcher(readLine);
	        //不符合要求直接返回
	        if(!m.matches()){
	            return false;
	        }
	        return true;
		}else
			return false;
	}
	
	/**
	 * 探测文件编码
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String getCharset(String fileName) throws IOException{   
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		Charset charset = null;
		File f = new File(fileName);
		try {
			charset = detector.detectCodepage(f.toURL());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (charset != null) {
			return charset.name();
		} else {
			return null;
		}
  }    
	
	
	public static void main(String[] args) {
		System.out.println(getInputStr(CoreyangHelper.INPUT_TYPE_FILE,true));
		System.out.println(getInputStr(CoreyangHelper.INPUT_TYPE_FILE,false));
		System.out.println(filePathPreProc("D:\\工作\\BGW\\04"));
		System.out.println(isNumberStr("1"));
	}
}
