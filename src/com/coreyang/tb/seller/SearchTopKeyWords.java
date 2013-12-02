package com.coreyang.tb.seller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.log4j.Logger;

import com.coreyang.TBSellerMain;
import com.coreyang.bean.MoFangData;
import com.coreyang.tool.CoreyangHelper;
import com.coreyang.tool.CoreyangUtil;
import com.coreyang.tool.MofangService;

/**
 * 根据关键词搜索魔方数据
 * @author yang.li
 *
 */
public class SearchTopKeyWords {
	
	public static final Logger logger = Logger.getLogger(SearchTopKeyWords.class);
	
	/**
	 * 搜索魔方数据
	 */
	public static void run(String keyWords){
		String filterFile = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		MofangService ml = new MofangService();
		String sid_s = TBSellerMain.sid;
		String response = null;
		try {
			response = ml.getMofangData(0, keyWords,sid_s);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			return;
		}
		List<MoFangData> mofangList = ml.parse(response, true, filterFile+"//filterList.txt");
		ml.save(mofangList, keyWords);
	}
	
	/**
	 * 搜索魔方数据
	 */
	public static void run(){
		System.out.println("请输入关键字：");
		String keyWords = CoreyangUtil.getInputStr(CoreyangHelper.INPUT_TYPE_DEFAULT, true);
		String filterFile = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		MofangService ml = new MofangService();
		String sid_s = TBSellerMain.sid;
		String response = null;
		try {
			response = ml.getMofangData(0, keyWords,sid_s);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			return;
		}
		List<MoFangData> mofangList = ml.parse(response, true, filterFile+"//filterList.txt");
		ml.save(mofangList, keyWords);
	}
	
	public static void main(String[] args) {
		run();
	}
}
