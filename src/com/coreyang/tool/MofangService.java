package com.coreyang.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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

public class MofangService {
	
	public static final String MOFANG_QUERY_KEY_WORD = "http://mofang.taobao.com/s/proxy/query/1202/r1:beginDate/r2:endDate/f0:endDate/tfix:200/tid:1/dfix:1/query:";
	public static final String MOFANG_QUERY_LIMIT_COUNT = "http://mofang.taobao.com/s/proxy/query/taoci_key_limit/query:";
	public static final String MOFANG_BASIC_URL = "http://mofang.taobao.com/s/app/basic";
	
	/**
	 * 获取魔方数据
	 * @param searchType 7:过去7天 0：昨天
	 * @param keywords
	 * @param sid_s
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getMofangData(int searchType,String keywords,String sid_s) throws UnsupportedEncodingException{
		System.out.println(keywords);
		String response = null;
		HttpClient httpClient = new HttpClient();
		//将按照浏览器的方式来自动处理Cookie
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		String encodeParam = URLEncoder.encode(keywords, CoreyangHelper.CHARSET_UTF_8).replaceAll("\\+", "%20");
		System.out.println("encode"+encodeParam);
		String beginDate = CoreyangUtil.getYestoday();
		if(searchType==7){
			beginDate = CoreyangUtil.getLast7days();
		}
		String endDate = CoreyangUtil.getYestoday();
		String url = MOFANG_QUERY_KEY_WORD.replaceAll("beginDate", beginDate).replaceAll("endDate", endDate)+encodeParam;
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
	
	/**
	 * 解析json数据
	 * @param jsonString 字符串
	 * @param needFilter 是否需要过滤关键词
	 * @param filterFilePath 关键词路径
	 */
	public List<MoFangData> parse(String jsonString,boolean needFilter,String filterFilePath){
		List<MoFangData> dataList = new ArrayList<MoFangData>();
		JSONArray jsonarray = JSONArray.fromObject(jsonString);
		JSONArray jsonarray1 = JSONArray.fromObject(jsonarray.get(0));
		for(Object records: jsonarray1){
			
			JSONObject jb = JSONObject.fromObject(records);
			String keywords = jb.getString("f0");
			int searchPopularity = jb.getInt("f1");
			int searchNum = jb.getInt("f2");
			double proportion = jb.getDouble("f3");
			int clickNumber = jb.getInt("f4");
			double tmallProportion = jb.getDouble("f5");
			double clickProportion = jb.getDouble("f6");
			String f7str = jb.getString("f7");
			JSONObject f7Object = JSONObject.fromObject(f7str);
			int goodsNumber = f7Object.getInt("num");
			double conversion = jb.getDouble("f8");
			String f9str = jb.getString("f9");
			if(f9str.equals("null")){
				f9str = "0";
			}
			double throughTrain = Double.valueOf(f9str);
			double oneGoodSearchNum = (double)searchNum/(double)goodsNumber;
			double buyerPerThousandGood = clickNumber*conversion/goodsNumber*1000;
			MoFangData md = new MoFangData(keywords,searchPopularity,searchNum,proportion,
					clickNumber,tmallProportion,clickProportion,goodsNumber,
					conversion,throughTrain,oneGoodSearchNum,buyerPerThousandGood);
			dataList.add(md);
		}
		System.out.println("一共搜索到："+dataList.size());
		if(needFilter){
			try {
				DeleteUnvalibleBrands.delete(filterFilePath, dataList, 18);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dataList;
	}
	
	public void save(List<MoFangData> data,String keyword){
		WritableWorkbook wwb;
        FileOutputStream fos;
        try {    
        	File filePathDir = new File(CoreyangHelper.SEARCH_KEYWORDS);
    		if (!filePathDir.isDirectory()) {
    			filePathDir.mkdirs();
    		}
            fos = new FileOutputStream(CoreyangHelper.SEARCH_KEYWORDS+keyword+"_"+CoreyangUtil.formatDate(new Date(), "yyyyMMddHHmmss")+".xls");
            wwb = Workbook.createWorkbook(fos);
            WritableSheet ws = wwb.createSheet(keyword, 10);        // 创建一个工作表

            //设置单元格的文字格式
            ws.addCell(new Label(0,0,"关键词"));
            ws.addCell(new Label(1,0,"搜索人气"));
            ws.addCell(new Label(2,0,"搜索指数"));
            ws.addCell(new Label(3,0,"占比"));
            ws.addCell(new Label(4,0,"点击指数"));
            ws.addCell(new Label(5,0,"商城点击占比"));
            ws.addCell(new Label(6,0,"点击率"));
            ws.addCell(new Label(7,0,"当前宝贝数"));
            ws.addCell(new Label(8,0,"转化率"));
            ws.addCell(new Label(9,0,"直通车"));
            ws.addCell(new Label(10,0,"单个宝贝搜索次数"));
            ws.addCell(new Label(11,0,"千个宝贝成交数"));
            NumberFormat nf   =   NumberFormat.getPercentInstance(); 
    		nf.setMinimumFractionDigits(2);
            //填充数据的内容
            for (int i = 0; i < data.size(); i++){
            	MoFangData mf = data.get(i);
            	ws.addCell(new Label(0, i + 1, mf.getKeywords()));
                ws.addCell(new jxl.write.Number(1, i + 1, mf.getSearchPopularity()));
                ws.addCell(new jxl.write.Number(2, i + 1, mf.getSearchNumber()));
                ws.addCell(new Label(3, i + 1, nf.format(mf.getProportion())));
                ws.addCell(new jxl.write.Number(4, i + 1, mf.getClickNumber()));
                ws.addCell(new Label(5, i + 1, nf.format(mf.getTmallProportion())));
                ws.addCell(new Label(6, i + 1, nf.format(mf.getClickProportion())));
                ws.addCell(new jxl.write.Number(7, i + 1, mf.getGoodsNumber()));
                ws.addCell(new Label(8, i + 1, nf.format(mf.getConversion())));
                ws.addCell(new Label(9, i + 1, String.format("%.2f", mf.getThroughTrain())));
                ws.addCell(new jxl.write.Number(10, i + 1, mf.getOneGoodSearchNum()));
                ws.addCell(new jxl.write.Number(11, i + 1, mf.getBuyerPerThousandGood()));
            }
            wwb.write();
            wwb.close();
        } catch (IOException e){
        	e.printStackTrace();
        } catch (RowsExceededException e){
        	e.printStackTrace();
        } catch (WriteException e){
        	e.printStackTrace();
        }
	}
	
	public static void main(String[] args) throws IOException {
		String filterFile = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		MofangService ml = new MofangService();
		String keywords = "休闲裤修身";
		String sid_s = "NTUxNTU5MDI5MTQ3MDI4OSpeKjEzODU4MjE3NTI1ODI";
		String response = ml.getMofangData(0, keywords,sid_s);
		System.out.println("response : "+response);
		List<MoFangData> mofangList = ml.parse(response, true, filterFile+"//branList.txt");
		System.out.println(mofangList.size());
		ml.save(mofangList, keywords);
	}
}
