package com.coreyang.tb.seller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.coreyang.tool.CoreyangHelper;
import com.coreyang.tool.CoreyangUtil;

/**
 * 分析csv数据包，根据数据包中的宝贝描述，下载宝贝图片，生成相应的本地宝贝详情描述。
 * 1.需要将供货商提供的数据包另存为03版的excel
 * @author yang.li
 *
 */
public class DownloadPicAndMkDescCtrl {
	
	public static final Logger logger = Logger.getLogger(DownloadPicAndMkDescCtrl.class);
	
	/**
	 * 执行分析数据包功能
	 */
	public static void run(String datagramFilePath,String providerName){
		List<String> filePath = new ArrayList<String>();
		try {
			CoreyangUtil.readGoods(datagramFilePath, filePath);
		} catch (IOException e1) {
			logger.error("read file path error : "+e1.getMessage(), e1);
			return;
		}
		try {
            for(String file : filePath){
            	ReadDatagram.process(file, providerName);
            }
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 执行分析数据包功能
	 */
	public static void run(){
		System.out.println("请输入包含数据包EXCEL的txt路径：");
		String datagramFilePath = CoreyangUtil.getInputStr(CoreyangHelper.INPUT_TYPE_FILE,true);
		System.out.println("请输入供应商的英文缩写：");
		String providerName = CoreyangUtil.getInputStr(CoreyangHelper.INPUT_TYPE_DEFAULT, true);
		List<String> filePath = new ArrayList<String>();
		try {
			CoreyangUtil.readGoods(datagramFilePath, filePath);
		} catch (IOException e1) {
			logger.error("read file path error : "+e1.getMessage(), e1);
			return;
		}
		try {
            for(String file : filePath){
            	ReadDatagram.process(file, providerName);
            }
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	public static void main(String[] args) {
		run();
	}
}
