package com.coreyang.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import cpdetector.io.ASCIIDetector;
import cpdetector.io.CodepageDetectorProxy;
import cpdetector.io.JChardetFacade;
import cpdetector.io.ParsingDetector;
import cpdetector.io.UnicodeDetector;

public class CoreyangUtil {
	
	public static final Logger logger = Logger.getLogger(CoreyangUtil.class);
	
	private static final String TAOBAO_LOGIN_URL = "https://login.taobao.com/member/login.jhtml";
    private static final String TAOBAO_LOGIN_URL_REDIRECT = "http://mofang.taobao.com/s/login";
	
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
		BufferedReader br = null;
				br = new BufferedReader(new InputStreamReader(is));
			
		
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
	
	public static String read(){
		 byte[] b = new byte[1024];

         // 读取用户输入到数组b中，

         // 读取的字节数量为n

         int n = 0 ;
		try {
			n = System.in.read(b);
		} catch (IOException e) {
			e.printStackTrace();
		}

         // 转换为整数

         String s = null;
		try {
			s = new String(b, 0, n,"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
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
	
	/**
	 * 探测文件编码
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String getCharset(InputStream input) throws IOException{   
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		Charset charset = null;
		try {
			charset = detector.detectCodepage(input,input.available());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (charset != null) {
			return charset.name();
		} else {
			return null;
		}
  }
	
	public static void writeFile(String str,String filePath,String fileName) throws IOException{
		File filePathDir = new File(filePath);
		if (!filePathDir.isDirectory()) {
			filePathDir.mkdirs();
		}
		File toFile = new File(filePath+fileName);
		if (toFile.exists()) {
			logger.info("File exists:"+filePath+fileName);
			return;
		}
		toFile.createNewFile();
		FileOutputStream outImgStream = new FileOutputStream(toFile);
		outImgStream.write(str.getBytes(CoreyangHelper.CHARSET_UTF_8));
		outImgStream.close();
	}
	
	public static String parseGoodsId(String supplyStr,String goodsId){
		if(goodsId.indexOf(supplyStr)==-1){
			return null;
		}
		goodsId = goodsId.substring(supplyStr.length()+1);
		return goodsId;
	}
	
	/**
	 * 格式化时间
	 * 
	 * @param date
	 * @param formatStr
	 * @return
	 */
	public static String formatDate(Date date, String formatStr) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(date);
	}
	
	public static List<String> readGoods(String goodsFile,List<String> goodsList) throws IOException{
		FileInputStream fis = new FileInputStream(new File(goodsFile));
		String charSet = CoreyangUtil.getCharset(goodsFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis,charSet));
		String temp = null;
		while((temp=br.readLine())!=null){
			goodsList.add(temp);
		}
		return goodsList;
	}
	  
	    
	                 
	    public static byte[] encryptBASE64(String key) throws UnsupportedEncodingException  {               
	        return Base64.encodeBase64(key.getBytes(CoreyangHelper.CHARSET_UTF_8));             
	    }               
	                  
	                 
	    public static String decryptBASE64(byte[] key) {               
	        return new String(Base64.decodeBase64(key));               
	    }   
	
	    public static String getSid() throws HttpException, IOException{
	    	String sid = null;
	    	HttpClient httpClient = new HttpClient();
			//将按照浏览器的方式来自动处理Cookie
			httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			GetMethod getMethod = null;
			PostMethod postMethod = null;
			postMethod = new PostMethod(TAOBAO_LOGIN_URL);
			postMethod.setRequestHeader("Referer", TAOBAO_LOGIN_URL);
			NameValuePair nvs[] = new NameValuePair[4];
			NameValuePair np = new NameValuePair();
			np.setName("TPL_password");
			np.setValue(CoreyangUtil.decryptBASE64("bGkseWFuZzg5MTIxMQ==".getBytes()));
			NameValuePair np1 = new NameValuePair();
			np1.setName("TPL_redirect_url");
			np1.setValue("http://mofang.taobao.com/s/login");
			NameValuePair np2 = new NameValuePair();
			np2.setName("TPL_username");
			np2.setValue("liyangbaby12");
			NameValuePair np3 = new NameValuePair();
			np3.setName("tid");
			np3.setValue("XOR_1_000000000000000000000000000000_6A583052377C7C057303070E");
			nvs[0] = np;
			nvs[1] = np1;
			nvs[2] = np2;
			nvs[3] = np3;
			postMethod.addParameters(nvs);
			httpClient.executeMethod(postMethod);
			
			getMethod = new GetMethod(TAOBAO_LOGIN_URL_REDIRECT);
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());  
			httpClient.executeMethod(getMethod);
			Cookie cookies[] = httpClient.getState().getCookies();
			for(Cookie c : cookies){
				if(c.getName().equals("sid_s")){
					sid = c.getValue();
				}
			}
			return sid;
	    }
	    
	    public static String getYestoday(){
	    	Calendar c = Calendar.getInstance();
	    	c.add(Calendar.DAY_OF_MONTH, -1);
	    	String yestoday = formatDate(c.getTime(), "yyyy-MM-dd");
			return yestoday;
	    }
	    
	    public static String getLast7days(){
	    	Calendar c = Calendar.getInstance();
	    	c.add(Calendar.DAY_OF_MONTH, -7);
	    	String yestoday = formatDate(c.getTime(), "yyyy-MM-dd");
			return yestoday;
	    }
	
	
	public static void main(String[] args) throws Exception {
		/*System.out.println(getInputStr(CoreyangHelper.INPUT_TYPE_FILE,true));
		System.out.println(getInputStr(CoreyangHelper.INPUT_TYPE_FILE,false));
		System.out.println(filePathPreProc("D:\\工作\\BGW\\04"));
		System.out.println(isNumberStr("1"));*/
		//System.out.println(parseGoodsId("FAD","HG-9808"));
		/*System.out.println(getYestoday());
		System.out.println(getLast7days());
		System.out.println(getSid());*/
		/*System.out.println("请输入关键字：");
		String keyWords = CoreyangUtil.getInputStr(CoreyangHelper.INPUT_TYPE_DEFAULT, true);*/
	}
}
