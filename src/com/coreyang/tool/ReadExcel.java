package com.coreyang.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class ReadExcel {

	public static void main(String args[]) {

		try {
			String filePath = "D://clsd新款//";
			String provider = "clsd";
			Workbook book = Workbook.getWorkbook(new File("D://1.xls"));
			// 获得第一个工作表对象
			Sheet sheet = book.getSheet(0);
			// 得到第一列第一行的单元格
			int rows = sheet.getRows();
			int count = 0;
			for (int i = 2; i < rows; i++) {
				StringBuffer sb = new StringBuffer();
				List<String> list = new ArrayList<String>();
				Cell cell_des = sheet.getCell(20, i);
				Cell cell_seller_no = sheet.getCell(33, i);
				String seller_no = cell_seller_no.getContents();
				String picPath = filePath + seller_no + "//";
				mactchSrc(list, cell_des.getContents());
				for (int m = 0; m < list.size(); m++) {
					sb.append("<IMG align=middle src=\"FILE:///");
					String picFile = provider + "-" + seller_no + "-" + (m + 1)
							+ ".jpg";
					UrlResource.saveUrlFile(list.get(m), picPath, picFile);
					sb.append(picPath + picFile);
					sb.append("\"><BR/>\t\n");
				}
				String productDescFileName = seller_no + ".txt";
				if (sb != null && sb.toString() != null
						&& !"".equals(sb.toString())) {
					UrlResource.saveFileDesc(sb.toString(), filePath,
							productDescFileName);
					count++;
				}
				// mjz@0622
				// System.out.println(cell_des.getContents());
			}
			System.out.println("成功处理了：" + count + "个宝贝!");
			book.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void mactchSrc(List<String> list, String str) {
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
