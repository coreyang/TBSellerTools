package com.coreyang.tb.seller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.coreyang.tool.CoreyangHelper;
import com.coreyang.tool.CoreyangUtil;
import com.coreyang.tool.UrlResource;

/**
 * 分析csv数据包，根据数据包中的宝贝描述，下载宝贝图片，生成相应的本地宝贝详情描述。
 * 1.需要将供货商提供的数据包另存为03版的excel
 * @author yang.li
 *
 */
public class DownloadPicAndMkDesc {
	
	public static final Logger logger = Logger.getLogger(DownloadPicAndMkDesc.class);
	
	/**
	 * 执行分析数据包功能
	 */
	public static void run(){
		System.out.println("请输入数据包EXCEL路径：");
		String datagramFilePath = CoreyangUtil.getInputStr(CoreyangHelper.INPUT_TYPE_FILE,true);
		System.out.println("请输入数据保存的目录：");
		String savePath = CoreyangUtil.getInputStr(CoreyangHelper.INPUT_TYPE_FILE,true);
		System.out.println("请输入供应商的英文缩写：");
		String providerName = CoreyangUtil.getInputStr(CoreyangHelper.INPUT_TYPE_DEFAULT, true);
		try {
			analysisDatagram(savePath, datagramFilePath, providerName);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	/**
	 * 分析数据包
	 * @param fileDictory 数据保存路径
	 * @param excelPath 需要分析的EXCEL路径
	 * @param providerName 供应商缩写
	 * @throws Exception 
	 */
	public static void analysisDatagram(String fileDictory,String excelPath,String providerName) throws Exception{
		Workbook book = Workbook.getWorkbook(new File(excelPath));
		// 获得第一个工作表对象
		Sheet sheet = book.getSheet(0);
		// 得到第一列第一行的单元格
		int rows = sheet.getRows();
		int count = 0;
		for (int i = 2; i < rows; i++) {
			StringBuilder sb = new StringBuilder();
			List<String> list = new ArrayList<String>();
			//第二十列是宝贝描述
			Cell cell_des = sheet.getCell(20, i);
			//第三十三列是商家编码
			Cell cell_seller_no = sheet.getCell(33, i);
			String seller_no = cell_seller_no.getContents();
			String picPath = fileDictory + seller_no + "//";
			mactchSrc(list, cell_des.getContents());
			logger.info("begin load this goods:"+seller_no);
			for (int m = 0; m < list.size(); m++) {
				sb.append("<IMG align=middle src=\"FILE:///");
				String picFile = providerName + "-" + seller_no + "-" + (m + 1)
						+ ".jpg";
				String remotePicUrl = list.get(m);
				UrlResource.saveUrlFile(remotePicUrl, picPath, picFile);
				logger.info("Downloaded goods pic:"+remotePicUrl);
				sb.append(picPath + picFile);
				sb.append("\"><BR/>\t\n");
			}
			String productDescFileName = seller_no + ".txt";
			if (sb != null && sb.toString() != null
					&& !"".equals(sb.toString())) {
				UrlResource.saveFileDesc(sb.toString(), fileDictory,
						productDescFileName);
				logger.info("Downloaded goods desc:"+productDescFileName);
				count++;
			}
		}
		logger.info("成功处理了：" + count + "个宝贝!");
		book.close();
	}
	
	public static void mactchSrc(List<String> list, String str){
		Pattern pattern1 = Pattern.compile("src=\"(.+?)\"");
		Matcher matcher = pattern1.matcher(str);
		while (matcher.find()) {
			list.add(matcher.group(1));
		}
		Pattern pattern2 = Pattern.compile("src=\"\"(.+?)\"\"");
		Matcher matcher2 = pattern2.matcher(str);
		while (matcher2.find()) {
			list.add(matcher.group());
		}
	}
}
