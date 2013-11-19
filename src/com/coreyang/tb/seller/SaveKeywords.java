package com.coreyang.tb.seller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
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

import com.coreyang.bean.MoFangData;

public class SaveKeywords {
	
	public static final String keywordsSavePath="E://";
	//关键词	搜索人气	搜索指数	占比		点击指数	 	商城点击占比		点击率	当前宝贝数	转化率	直通车	单个宝贝搜索次数	千个宝贝成交数
	public void save(List<MoFangData> data,String keyword){
		WritableWorkbook wwb;
        FileOutputStream fos;
        try {    
            fos = new FileOutputStream(keywordsSavePath+keyword+".xls");
            wwb = Workbook.createWorkbook(fos);
            WritableSheet ws = wwb.createSheet(keyword, 10);        // 创建一个工作表

            //设置单元格的文字格式
            WritableFont wf = new WritableFont(WritableFont.ARIAL,12,WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.BLUE);
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
}
