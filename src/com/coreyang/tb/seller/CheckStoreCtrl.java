package com.coreyang.tb.seller;



import org.apache.log4j.Logger;

import com.coreyang.tool.CoreyangHelper;
import com.coreyang.tool.CoreyangUtil;

/**
 * 检查库存
 * @author yang.li
 *
 */
public class CheckStoreCtrl {
	
	public static final Logger logger = Logger.getLogger(DownloadPicAndMkDescCtrl.class);
	
	public static void run(String datagramFilePath){
		SearchGoodsService ss = new SearchGoodsService();
		ss.seach(datagramFilePath);
	}
	
	public static void run(){
		System.out.println("请输入货号路径：");
		String datagramFilePath = CoreyangUtil.getInputStr(CoreyangHelper.INPUT_TYPE_FILE,true);
		SearchGoodsService ss = new SearchGoodsService();
		ss.seach(datagramFilePath);
	}
}
