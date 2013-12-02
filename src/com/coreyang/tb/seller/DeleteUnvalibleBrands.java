package com.coreyang.tb.seller;

/**
 * 删除含有无效的品牌关键词的行
 * @author yang.li
 *
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jxl.Cell;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.coreyang.bean.MoFangData;
import com.coreyang.tool.CoreyangHelper;
import com.coreyang.tool.CoreyangUtil;

public class DeleteUnvalibleBrands {
	
	public static Logger logger = Logger.getLogger(DeleteUnvalibleBrands.class);
	
	
	/**
	 * 运行无效关键词删除功能
	 */
	public static void run(){
		System.out.println("请输入需要过滤关键词的绝对路径：");
		String filterKeywordsPath = CoreyangUtil.getInputStr(CoreyangHelper.INPUT_TYPE_FILE,true);
		System.out.println("请输入关键词数据EXCEL的绝对路径：");
		String excelKeywordsPath = CoreyangUtil.getInputStr(CoreyangHelper.INPUT_TYPE_FILE,true);
		System.out.println("请输入关键词最长字数(超过此长度的行会被删除):");
		String maxKeywordsSizeStr = CoreyangUtil.getInputStr(CoreyangHelper.INPUT_TYPE_NUMBER,true);
		int maxKeywordsSize = Integer.parseInt(maxKeywordsSizeStr);
		System.out.println("请输入Excel数据中包含关键词的列数(第一列为0):");
		String keywordsColStr = CoreyangUtil.getInputStr(CoreyangHelper.INPUT_TYPE_NUMBER,true);
		int keywordsCol = Integer.parseInt(keywordsColStr);
		try {
			logger.info("开始过滤......");
			delete(filterKeywordsPath,excelKeywordsPath,maxKeywordsSize,keywordsCol);
			logger.info("过滤完成......");
		} catch (IOException e) {
			logger.equals(e);
		}
	}
	
	/**
	 * 获取关键词数据
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static List<String> getBrands(String filePath) throws IOException{
		logger.info("开始读取过滤关键字...");
		List<String> brandList = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String tempStr = null;
		while((tempStr=br.readLine())!=null){
			brandList.add(tempStr);
		}
		logger.info("结束读取过滤关键字...一共获取了"+brandList.size()+"个关键词。");
		return brandList;
	}


	/**
	 * 删除需要过滤的行
	 * @param filterKeywordsPath 过滤关键字路径
	 * @param keywordsExcelPath 关键词数据路径
	 * @param maxKeywordsSize 关键词最大长度
	 * @param keywordsColNum 关键词所在列
	 * @throws IOException
	 */
	public static void delete(String filterKeywordsPath,String keywordsExcelPath,int maxKeywordsSize,int keywordsColNum) throws IOException {
		int rows = 0;
		// 单元格内容
		String strCon = "";
		// excel路径 注意只支持xls后缀的
		List<String> brandList = new ArrayList<String>();
		brandList = getBrands(filterKeywordsPath);
		boolean isBrand = false;
		boolean isMax = false;
		int count = 0;
		try {
			Workbook book = Workbook.getWorkbook(new File(keywordsExcelPath));
			WritableWorkbook wbook = Workbook.createWorkbook(
					new File(keywordsExcelPath), book);//  
			// 获得第一个工作表对象
			WritableSheet sheet = (WritableSheet) wbook.getSheet(0);
			rows = sheet.getRows();
			Cell cell1 = null;
			for (int i = rows - 1; i >= 0; i--) {
				cell1 = sheet.getCell(keywordsColNum, i);
				strCon = cell1.getContents();
				for(String brand : brandList){
					isBrand = strCon.contains(brand);
					if(isBrand)
						break;
				}
				isMax = (strCon.length()>maxKeywordsSize);
				if (isBrand||isMax) {
					logger.info("Delete :" + strCon);
					count++;
					sheet.removeRow(i);
				}
			}
			wbook.write();
			wbook.close();
			book.close();
			logger.info("Done!Delete:"+count+"个宝贝!");
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	/**
	 * 删除需要过滤的数据项
	 * @param filterKeywordsPath
	 * @param data
	 * @param maxKeywordsSize
	 * @throws IOException
	 */
	public static void delete(String filterKeywordsPath,List<MoFangData> data,int maxKeywordsSize) throws IOException{
		List<String> filterList = new ArrayList<String>();
		filterList = getBrands(filterKeywordsPath);
		Iterator<MoFangData> it = data.iterator();
		while(it.hasNext()){
			MoFangData mo = it.next();
			for(String filter : filterList){
				if(mo.getKeywords().contains(filter)||mo.getKeywords().length()>maxKeywordsSize){
					it.remove();
					break;
				}
			}
		}
		System.out.println("过滤之后还剩："+data.size());
	}
}