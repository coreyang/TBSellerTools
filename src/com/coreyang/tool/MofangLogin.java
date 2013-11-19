package com.coreyang.tool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.coreyang.bean.MoFangData;
import com.coreyang.tb.seller.DeleteUnvalibleBrands;
import com.coreyang.tb.seller.SaveKeywords;

public class MofangLogin {
	public static final String MOFANG_QUERY_KEY_WORD = "http://mofang.taobao.com/s/proxy/query/1202/r1:date/r2:date/f0:date/tfix:200/tid:1/dfix:1/query:";
	public static final String MOFANG_QUERY_LIMIT_COUNT = "http://mofang.taobao.com/s/proxy/query/taoci_key_limit/query:";
	public static final String MOFANG_BASIC_URL = "http://mofang.taobao.com/s/app/basic";
	
	public String getMofangData(String date,String keywords,String sid_s) throws UnsupportedEncodingException{
		String response = null;
		HttpClient httpClient = new HttpClient();
		//将按照浏览器的方式来自动处理Cookie
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		String encodeParam = URLEncoder.encode(keywords, CoreyangHelper.CHARSET_UTF_8);
		String url = MOFANG_QUERY_KEY_WORD.replaceAll("date", date)+encodeParam;
		GetMethod getMethod = new GetMethod(url);
		getMethod.setRequestHeader("Referer", MOFANG_BASIC_URL);
		getMethod.setRequestHeader("Cookie", "sid_s="+sid_s+";");
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
		try {
			// 执行getMethod
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + getMethod.getStatusLine());
			}
			// 读取内容
			byte[] responseBody = getMethod.getResponseBody();
			String content = new String(responseBody,CoreyangHelper.CHARSET_UTF_8);
			// 处理内容
			response = content;
			System.out.println(content);
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
		return response;
	}
	
	public static void main(String[] args) throws IOException {
		MofangLogin ml = new MofangLogin();
		String date = "2013-11-18";
		String keywords = "休闲裤修身";
		String sid_s = "NDQ2MDA1MDgzNjIwNTQ4MipeKjEzODQ4NTAyODkxODE";
		String response = ml.getMofangData(date, keywords,sid_s);
		MofangJsonParse mj = new MofangJsonParse();
		List<MoFangData> mofangList = mj.parse(response, false, null);
		System.out.println(mofangList.size());
		DeleteUnvalibleBrands.delete("E://filterList.txt", mofangList, 18);
		SaveKeywords sv = new SaveKeywords();
		sv.save(mofangList, keywords);
	}
}
