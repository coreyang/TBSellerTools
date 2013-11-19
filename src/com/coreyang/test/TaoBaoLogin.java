package com.coreyang.test;



import java.io.IOException;  
import org.apache.commons.httpclient.Cookie;  
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;  
import org.apache.commons.httpclient.HttpException;  
import org.apache.commons.httpclient.NameValuePair;  
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;  
import org.apache.commons.httpclient.methods.PostMethod;  
import org.apache.commons.httpclient.params.HttpMethodParams;
  
  
public class TaoBaoLogin {  
       
        private static final String TAOBAO_LOGIN_URL = "https://login.taobao.com/member/login.jhtml";
        private static final String TAOBAO_LOGIN_URL_REDIRECT = "http://mofang.taobao.com/s/login";
        public static void main(String args[]) throws HttpException, IOException {  
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
    		np.setValue("li,yang891211");
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
    		Cookie cookie6[] = httpClient.getState().getCookies();
    		for(Cookie c : cookie6){
    			if(c.getName().equals("sid_s")){
    				System.out.println(c.getName()+":"+c.getValue());
    			}
    		}
        }
        
      public static void printCookies(HttpClient client){
    	 Cookie cookies[] = client.getState().getCookies();
    	 for(Cookie c : cookies){
    		 System.out.println(c.getName()+"="+c.getValue());
    	 }
      }
}  
