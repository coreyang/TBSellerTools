package com.coreyang.tb.seller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.coreyang.bean.GoodsStatus;
import com.coreyang.tool.CoreyangUtil;

public class SearchGoodsFromFad implements SearchGoods{
	
	public static final Logger logger = Logger.getLogger(SearchGoodsFromFad.class);
	
	public static final String searchResultUrl = "http://www.fadhomme.cn/?search-result.html";
	
	public static final String TOTAL_KEYWORD = "总共找到<font color='red'>";
	
	public static final String DETAIL_LINK = "<div class=\"goodinfo\">";

	@Override
	public void gotoDetail(GoodsStatus gs, String goodUrl) {
		Document doc = null;
		try {
			doc = Jsoup.connect(goodUrl).timeout(0).get();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			gs.setStatus(GoodsStatus.NOT_FOUND);
			gs.setResult(GoodsStatus.SEARCH_EXCEPTION);
			return;
		}
		Elements items = doc.select("#listall_ll");
		if(items.size()!=1){
			logger.warn("listall_ll is not primary:"+gs.getGoodsId());
			gs.setStatus(GoodsStatus.NOT_FOUND);
			gs.setResult(GoodsStatus.SEARCH_EXCEPTION);
			return;
		}
		Element tbodyItem = items.get(0);
		Elements trs = tbodyItem.select("tr");
		StringBuilder resultSb = new StringBuilder();
		int skuNum = trs.size();
		int noStoreSkuNum = 0;
		for (Element tr : trs) {
			boolean noFlag = false;
			Elements tdNo = tr.select("td:has(a)");
			if(tdNo.size()!=0){
				noFlag = true;
				noStoreSkuNum++;
			}
			if (noFlag) {
				Elements sku = tr.select("[style]");
				resultSb.append(sku.get(0).html()+"|");
			}
		}
		if(noStoreSkuNum==0){
			//全部有货
			gs.setStatus(GoodsStatus.ALL_HAVE_STORE);
		}else{
			if(skuNum==noStoreSkuNum){
				//全部缺货
				gs.setStatus(GoodsStatus.ALL_NO_STORE);
			}else{
				//部分缺货
				String result = resultSb.toString().substring(0, resultSb.toString().length()-1);
				gs.setStatus(2);
				gs.setResult(result);
			}
		}
		
	}

	@Override
	public GoodsStatus search(String goodsId) {
		GoodsStatus gs = new GoodsStatus(goodsId,-1,"");
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(searchResultUrl);
		NameValuePair param[] = new NameValuePair[1];
		NameValuePair nv = new NameValuePair();
		String parseGoodsId = CoreyangUtil.parseGoodsId("FAD", goodsId);
		if(null==parseGoodsId||"".equals(parseGoodsId)){
			logger.info("商品编号转换出错..."+goodsId);
			return null;
		}
		nv.setName("name[]");
		nv.setValue(parseGoodsId);
		param[0] = nv;
		postMethod.addParameters(param);
		String resultUrl = "";
		try {
			httpClient.executeMethod(postMethod);
			Header[] headers = postMethod.getResponseHeaders();
			for(Header h : headers){
				if(h.getName().equals("Location"))
					resultUrl = h.getValue();
			}
		} catch (HttpException e) {
			logger.error(e.getMessage(), e);
			gs.setStatus(GoodsStatus.NOT_FOUND);
			gs.setResult(GoodsStatus.SEARCH_EXCEPTION);
			return gs;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			gs.setStatus(GoodsStatus.NOT_FOUND);
			gs.setResult(GoodsStatus.SEARCH_EXCEPTION);
			return gs;
		}
		String urlPrefix = resultUrl.substring(0,resultUrl.indexOf("?")+1);
		String getParam = resultUrl.substring(resultUrl.indexOf("?")+1);
		try {
			getParam = URLEncoder.encode(getParam, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error(e1.getMessage(), e1);
			gs.setStatus(GoodsStatus.NOT_FOUND);
			gs.setResult(GoodsStatus.SEARCH_EXCEPTION);
			return gs;
		}
		URL url = null;
		try {
			url = new URL(urlPrefix+getParam);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(), e);
			gs.setStatus(GoodsStatus.NOT_FOUND);
			gs.setResult(GoodsStatus.SEARCH_EXCEPTION);
			return gs;
		}
		StringBuilder searchResult = new StringBuilder();
		URLConnection cu;
		try {
			cu = url.openConnection();
			HttpURLConnection hcu = (HttpURLConnection) cu;
			hcu.connect();
			BufferedReader br = new BufferedReader(new InputStreamReader(hcu.getInputStream()));
			
			String s = "";
			while((s=br.readLine())!=null){
				searchResult.append(s);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			gs.setStatus(GoodsStatus.NOT_FOUND);
			gs.setResult(GoodsStatus.SEARCH_EXCEPTION);
			return gs;
		}
		
		if("".equals(searchResult.toString())){
			logger.warn("商品搜索结果返回空串："+goodsId);
			gs.setStatus(GoodsStatus.NOT_FOUND);
			gs.setResult(GoodsStatus.SEARCH_RETURN_NULL);
			return gs;
		}
		
		String resultS = searchResult.toString();
		int keywordIndex = resultS.indexOf(TOTAL_KEYWORD);
		if(keywordIndex==-1){
			logger.warn("无法找到该商品："+goodsId);
			gs.setStatus(GoodsStatus.NOT_FOUND);
			gs.setResult(GoodsStatus.NO_FOUND_PRODUCT);
			return gs;
		}
		String subStr = resultS.substring(keywordIndex);
		int beginIndex = TOTAL_KEYWORD.length();
		int endIndex = subStr.indexOf("</font>");
		String totalCountStr = subStr.substring(beginIndex, endIndex);
		int totalCount = Integer.parseInt(totalCountStr);
		logger.info(goodsId+":总计搜索到"+totalCount+"个商品");
		if(totalCount==1){
			int detailIndex = subStr.indexOf(DETAIL_LINK);
			subStr = subStr.substring(detailIndex);
			beginIndex = subStr.indexOf("href=\"")+6;
			endIndex = subStr.indexOf("\" title");
			String detailUrl = subStr.substring(beginIndex, endIndex);
			gotoDetail(gs, detailUrl);
		}else if(totalCount>1){
			gs.setStatus(GoodsStatus.NOT_PRIMARY_GOODSID);
		}else{
			gs.setStatus(GoodsStatus.NOT_FOUND);
			gs.setResult(GoodsStatus.NO_FOUND_PRODUCT);
		}
		return gs;
	}
	
	public static void main(String[] args) {
		SearchGoodsFromFad sf = new SearchGoodsFromFad();
		GoodsStatus gs = sf.search("FAD-7718");
		System.out.println(gs.getStatus());
		System.out.println(gs.getResult());
	}

}
