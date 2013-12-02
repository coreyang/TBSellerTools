package com.coreyang.tb.seller;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.coreyang.bean.GoodsStatus;
import com.coreyang.tool.CoreyangUtil;

/**
 * 搜索红果的商品，只需要判断搜索出来是否含有缺货关键词即可。
 * 1.搜索出0个商品，返回TYPE:0
 * 2.搜索异常返回空
 * 3.搜索出1个商品，判断是否包含关键词，返回TYPE:1
 * 4.搜索出大于1个商品，返回TYPE:2
 * @author yang.li
 *
 */
public class SearchGoodsFromHG implements SearchGoods{
	
	public static final Logger logger = Logger.getLogger(SearchGoodsFromHG.class);
	
	public static final String searchUrlHG = "http://www.my958shop.com/";
	public static final String searchResultUrl = "http://www.my958shop.com/search.php";
	public static final String NO_STOCK_LINE[] = new String[]{"<p class=\"brief\">缺货下架</p>","<p class=\"brief\">断货下架</p>"};
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
		
		//探测类目
		Element cgItem = doc.select("#urhere").get(0);
		Elements aItems = cgItem.select("a");
		boolean isTrousers = false;
		for (Element a : aItems){
			String ahref = a.attr("href");
			if(ahref.indexOf("id=6")>-1){
				isTrousers = true;
				break;
				}
		}
		Elements items = doc.select("[style=width:160px; float:left;]");
		if(items.size()==1){
			String html = items.html();
			if(html.indexOf("下架")>-1){
				gs.setStatus(GoodsStatus.ALL_NO_STORE);
			}else{
				gs.setStatus(GoodsStatus.PART_NO_STORE);
				gs.setResult("只有一个尺码了，请检查");
			}
		}else{
			if(isTrousers){
				if(items.size()>7){
					gs.setStatus(GoodsStatus.ALL_HAVE_STORE);//尺码个数大于7，则认为有货
				}else{
					gs.setStatus(GoodsStatus.PART_NO_STORE);
					gs.setResult("部分缺货，请检查");
				}
			}else{
				if(items.size()>3){
					gs.setStatus(GoodsStatus.ALL_HAVE_STORE);//尺码个数大于3，则认为有货
				}else{
					gs.setStatus(GoodsStatus.PART_NO_STORE);
					gs.setResult("部分缺货，请检查");
				}
			}
			
		}
	}

	@Override
	public GoodsStatus search(String goodsId) {
		GoodsStatus gs = new GoodsStatus(goodsId,-1,"");
		HttpClient httpClient = new HttpClient();
		String parseGoodsId = CoreyangUtil.parseGoodsId("HG", goodsId);
		if(null==parseGoodsId||"".equals(parseGoodsId)){
			logger.info("商品编号转换出错..."+goodsId);
			return null;
		}
		String param = "?keywords="+parseGoodsId;
		GetMethod getMethod = new GetMethod(searchResultUrl+param);
		getMethod.setFollowRedirects(false);
		String resultUrl = "";
		try {
			httpClient.executeMethod(getMethod);
			Header[] headers = getMethod.getResponseHeaders();
			for(Header h : headers){
				if(h.getName().equals("Location"))
					resultUrl = h.getValue();
			}
		} catch (HttpException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		if(StringUtils.isBlank(resultUrl)){
			logger.warn("无法找到跳转地址："+goodsId);
			return null;
		}
		int totalCount = 0;
		Document doc = null;
		try {
			doc = Jsoup.connect(searchUrlHG+resultUrl).timeout(0).get();
		} catch (IOException e1) {
			logger.error(e1.getMessage(), e1);
			gs.setStatus(GoodsStatus.NOT_FOUND);
			gs.setResult(GoodsStatus.SEARCH_EXCEPTION);
			return gs;
		}
		Elements totalElements = doc.select("span:contains(总计)");
		if(totalElements.size()==1){
			//搜索到总个数
			Elements totalNumE = totalElements.get(0).select("b");
			if(totalNumE.size()==1){
				totalCount = Integer.parseInt(totalNumE.get(0).html());
				logger.info(goodsId+" 总共找到"+totalCount+"个商品");
				if(totalCount==1){
					Elements singleProduct = doc.select("p:contains(下架)");
					if(singleProduct.size()!=0){
						gs.setStatus(GoodsStatus.ALL_NO_STORE);
						return gs;
					}else{
						//进入详情页探测
						Element detailpE = doc.select(".name").get(0);
						Element detailAE = detailpE.select("a").get(0);
						String detailUrl = detailAE.attr("href");
						gotoDetail(gs, searchUrlHG+detailUrl);
						return gs;
					}
				}else if(totalCount>1){
					gs.setStatus(GoodsStatus.NOT_PRIMARY_GOODSID);
					return gs;
				}else{
					gs.setStatus(GoodsStatus.NOT_FOUND);
					gs.setResult(GoodsStatus.NO_FOUND_PRODUCT);
					return gs;
				}
			}else{
				logger.warn("span b is not primary"+goodsId);
				gs.setStatus(GoodsStatus.NOT_FOUND);
				gs.setResult(GoodsStatus.SEARCH_EXCEPTION);
				return gs;
			}
		}else{
			logger.warn("class f_l f6 is not primary"+goodsId);
			gs.setStatus(GoodsStatus.NOT_FOUND);
			gs.setResult(GoodsStatus.SEARCH_EXCEPTION);
			return gs;
		}
	}
	
	public static void main(String[] args) {
		SearchGoodsFromHG sm = new SearchGoodsFromHG();
		GoodsStatus gs = sm.search("HG-J3221");
		System.out.println(gs.getStatus());
		System.out.println(gs.getResult());
	}

}
