package com.coreyang;

import org.apache.log4j.Logger;

import com.coreyang.tb.seller.DeleteUnvalibleBrands;
import com.coreyang.tb.seller.DownloadPicAndMkDesc;
import com.coreyang.tool.CoreyangHelper;
import com.coreyang.tool.CoreyangUtil;

public class TBSellerMain {

	public static Logger logger = Logger.getLogger(TBSellerMain.class);

	public static void main(String[] args) {
		logger.info("TBSellerTools Main begin...");
		System.out.println("******淘宝卖家工具功能类型******");
		System.out.println("1：删除含有无效的关键词的数据行");
		System.out.println("2：分析csv数据包，下载宝贝图片，生成相应的宝贝详情描述");
		System.out.println("输入\"bye\"，系统自动退出!");
		System.out.println("请输入功能类型：");
		chooseFunc();
		logger.info("TBSellerTools Main end...");
	}

	/**
	 * 根据输入，选择功能
	 */
	public static void chooseFunc() {
		String readLine = CoreyangUtil.getInputStr(
				CoreyangHelper.INPUT_TYPE_NUMBER, true);
		if ("1".equals(readLine.trim())) {
			// 调用1功能模块
			logger.info("choose func 1");
			DeleteUnvalibleBrands.run();
		} else if ("2".equals(readLine.trim())) {
			// 调用2功能模块
			logger.info("choose func 2");
			DownloadPicAndMkDesc.run();
		}else{
			System.out.println("不支持该功能，请重新输入：");
			logger.info("can not find func");
			chooseFunc();
		}

	}
}
