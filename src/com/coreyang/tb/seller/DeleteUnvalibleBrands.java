package com.coreyang.tb.seller;

/**
 * ɾ��������Ч��Ʒ�ƹؼ��ʵ���
 * @author yang.li
 *
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
	 * ������Ч�ؼ���ɾ������
	 */
	public static void run(){
		System.out.println("��������Ҫ���˹ؼ��ʵľ���·����");
		String filterKeywordsPath = CoreyangUtil.getInputStr(CoreyangHelper.INPUT_TYPE_FILE,true);
		System.out.println("������ؼ�������EXCEL�ľ���·����");
		String excelKeywordsPath = CoreyangUtil.getInputStr(CoreyangHelper.INPUT_TYPE_FILE,true);
		System.out.println("������ؼ��������(�����˳��ȵ��лᱻɾ��):");
		String maxKeywordsSizeStr = CoreyangUtil.getInputStr(CoreyangHelper.INPUT_TYPE_NUMBER,true);
		int maxKeywordsSize = Integer.parseInt(maxKeywordsSizeStr);
		System.out.println("������Excel�����а����ؼ��ʵ�����(��һ��Ϊ0):");
		String keywordsColStr = CoreyangUtil.getInputStr(CoreyangHelper.INPUT_TYPE_NUMBER,true);
		int keywordsCol = Integer.parseInt(keywordsColStr);
		try {
			logger.info("��ʼ����......");
			delete(filterKeywordsPath,excelKeywordsPath,maxKeywordsSize,keywordsCol);
			logger.info("�������......");
		} catch (IOException e) {
			logger.equals(e);
		}
	}
	
	/**
	 * ��ȡ�ؼ�������
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static List<String> getBrands(String filePath) throws IOException{
		logger.info("��ʼ��ȡ���˹ؼ���...");
		List<String> brandList = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String tempStr = null;
		while((tempStr=br.readLine())!=null){
			brandList.add(tempStr);
		}
		logger.info("������ȡ���˹ؼ���...һ����ȡ��"+brandList.size()+"���ؼ��ʡ�");
		return brandList;
	}


	/**
	 * ɾ����Ҫ���˵���
	 * @param filterKeywordsPath ���˹ؼ���·��
	 * @param keywordsExcelPath �ؼ�������·��
	 * @param maxKeywordsSize �ؼ�����󳤶�
	 * @param keywordsColNum �ؼ���������
	 * @throws IOException
	 */
	public static void delete(String filterKeywordsPath,String keywordsExcelPath,int maxKeywordsSize,int keywordsColNum) throws IOException {
		int rows = 0;
		// ��Ԫ������
		String strCon = "";
		// excel·�� ע��ֻ֧��xls��׺��
		List<String> brandList = new ArrayList<String>();
		brandList = getBrands(filterKeywordsPath);
		boolean isBrand = false;
		boolean isMax = false;
		int count = 0;
		try {
			Workbook book = Workbook.getWorkbook(new File(keywordsExcelPath));
			WritableWorkbook wbook = Workbook.createWorkbook(
					new File(keywordsExcelPath), book);//  
			// ��õ�һ�����������
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
			logger.info("Done!Delete:"+count+"������!");
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	/**
	 * ɾ����Ҫ���˵�������
	 * @param filterKeywordsPath
	 * @param data
	 * @param maxKeywordsSize
	 * @throws IOException
	 */
	public static void delete(String filterKeywordsPath,List<MoFangData> data,int maxKeywordsSize) throws IOException{
		List<String> filterList = new ArrayList<String>();
		filterList = getBrands(filterKeywordsPath);
		for(int i=0;i<data.size();i++){
			for(String filter : filterList){
				if(data.get(i).getKeywords().contains(filter)||(data.get(i).getKeywords().length()>maxKeywordsSize))
					data.remove(i);
			}
		}
		System.out.println("����֮��ʣ��"+data.size());
	}
}