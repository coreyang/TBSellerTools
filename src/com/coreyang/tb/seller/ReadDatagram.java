package com.coreyang.tb.seller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.coreyang.tool.CoreyangHelper;
import com.coreyang.tool.UrlResource;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class ReadDatagram {

	public static final Logger logger = Logger.getLogger(ReadDatagram.class);
	public static final String ITEM_BEGIN_ROW = "item.begin.row";
	public static final String ITEM_NAME = "item.name";
	public static final String ITEM_DESC = "item.desc";
	public static final String ITEM_SELLER_NO = "item.seller.no";

	public static void genData(Sheet sheet, Map<String, Integer> mapData) {
		int rows = sheet.getRows();
		int cols = sheet.getColumns();
		boolean rollFlag = false;
		for (int i = 0; i < rows; i++) {
			if (rollFlag) {
				break;
			} else {
				for (int j = 0; j < cols; j++) {
					Cell cell = sheet.getCell(j, i);
					String cellContents = cell.getContents();
					if (cellContents.equals("宝贝名称")) {
						mapData.put(ITEM_BEGIN_ROW, i + 1);
						mapData.put(ITEM_NAME, j);
					}
					if (cellContents.equals("宝贝描述")) {
						mapData.put(ITEM_DESC, j);
					}
					if (cellContents.equals("商家编码")) {
						mapData.put(ITEM_SELLER_NO, j);
						rollFlag = true;
						break;
					}
				}
			}

		}
	}

	public static void process(String file,String provider) throws Exception {
		Workbook book = Workbook.getWorkbook(new File(file));
		// 获得第一个工作表对象
		Sheet sheet = book.getSheet(0);
		Map<String, Integer> map = new HashMap<String, Integer>();
		genData(sheet, map);
		// 得到第一列第一行的单元格
		int rows = sheet.getRows();
		int count = 0;
		for (int i = map.get(ITEM_BEGIN_ROW); i < rows; i++) {
			StringBuffer sb = new StringBuffer();
			List<String> list = new ArrayList<String>();
			Cell itemName = sheet.getCell(map.get(ITEM_NAME), i);
			Cell cell_des = sheet.getCell(map.get(ITEM_DESC), i);
			Cell cell_seller_no = sheet.getCell(map.get(ITEM_SELLER_NO), i);
			String itemNameStr = itemName.getContents();
			String seller_no = cell_seller_no.getContents();
			String picPath = CoreyangHelper.DESC_PIC + seller_no + "//";
			mactchSrc(list, cell_des.getContents());
			if (list.size() != 0) {
				logger.info("Begin load item name : " + itemNameStr
						+ " ,item sellerno:" + seller_no + "!");
			}
			for (int m = 0; m < list.size(); m++) {
				sb.append("<IMG align=middle src=\"FILE:///");
				String picNoStrHead = "";
				int picNo = m + 1;
				if (picNo < 10) {
					picNoStrHead = "0" + String.valueOf(picNo);
				} else {
					picNoStrHead = String.valueOf(picNo);
				}
				String picFile = provider + "-" + seller_no + "-"
						+ picNoStrHead + ".jpg";
				UrlResource.saveUrlFile(list.get(m), picPath, picFile);
				logger.info("Downloaded: " + list.get(m) + "!");
				sb.append(picPath + picFile);
				sb.append("\"><BR/>\t\n");
			}
			String productDescFileName = seller_no + ".txt";
			if (sb != null && sb.toString() != null
					&& !"".equals(sb.toString())) {
				UrlResource.saveFileDesc(sb.toString(), CoreyangHelper.DESC_PIC,
						productDescFileName);
				count++;
			}
		}
		logger.info(file + "成功处理了：" + count + "个宝贝!");
		book.close();
	}
	
	 public static void mactchSrc(List<String> list , String str){
	    	Pattern pattern1 = Pattern.compile("src=\"(.+?)\"");
		   	 Matcher matcher = pattern1.matcher(str);
		   	 while(matcher.find()){
		   		list.add(matcher.group(1));
		   	 }
		   	Pattern pattern2 = Pattern.compile("src=\"\"(.+?)\"\"");
		   	Matcher matcher2 = pattern2.matcher(str);
		   	while(matcher2.find()){
		   		list.add(matcher.group());
			}
	    }
}
