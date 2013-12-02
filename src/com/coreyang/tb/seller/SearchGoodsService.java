package com.coreyang.tb.seller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import com.coreyang.bean.GoodsStatus;
import com.coreyang.tool.CoreyangHelper;
import com.coreyang.tool.CoreyangUtil;

/**
 * 检查商品库存
 * @author yang.li
 *
 */
public class SearchGoodsService {
	
	public static final Logger logger = Logger.getLogger(SearchGoodsService.class);
	
	public static final String LINE_SYMBO = "\r\n";
	
	/**
	 * 对给定的货号列表进行搜索
	 * @param goodsPath 
	 * 输出文件，文件格式:
	 * Start
	 * 0未搜索到的:
	 * goodsId1,goodsId2,.....
	 * 1全部缺货，建议下架
	 * goodsId1,goodsId2,.....
	 * 2.部分缺货:
	 * goodsId1:颜色1尺码1|颜色2尺码2
	 * goodsId2:颜色1尺码1|颜色2尺码2
	 * 3.全部有货：
	 * goodsId1,goodsId2,.....
	 * 4.商品编号不唯一：
	 * goodsId1,goodsId2,.....
	 * End
	 */
	public void seach(String goodsPath){
		List<GoodsStatus> gsList = new ArrayList<GoodsStatus>();
		List<String> goodsIds = new ArrayList<String>();
		SearchGoods sg = null;
		try {
			goodsIds = CoreyangUtil.readGoods(goodsPath,goodsIds);
		} catch (IOException e) {
			logger.error("read goodsId file Exception : "+e.getMessage(), e);
			return;
		}
		
		for(String goodsId : goodsIds){
			//可以做成单例模式
			if(goodsId.indexOf("FAD")>-1)
				sg = new SearchGoodsFromFad();
			else
				sg = new SearchGoodsFromHG();
			GoodsStatus gs = sg.search(goodsId);
			gsList.add(gs);
		}
		
		//分析结果，输出文件
		StringBuilder notFoundGoods_A_SB = new StringBuilder();
		StringBuilder notFoundGoods_B_SB = new StringBuilder();
		StringBuilder notFoundGoods_C_SB = new StringBuilder();
		StringBuilder allNoStore_SB = new StringBuilder();
		StringBuilder partNoStore_SB = new StringBuilder();
		StringBuilder allHaveStore_SB = new StringBuilder();
		StringBuilder noPrimaryGoods_SB = new StringBuilder();
		
		for(GoodsStatus gs : gsList){
			if(gs==null)
				continue;
			int status = gs.getStatus();
			switch(status){
				case GoodsStatus.NOT_FOUND : 
					if(GoodsStatus.SEARCH_EXCEPTION.equals(gs.getResult()))
						notFoundGoods_A_SB.append(gs.getGoodsId()+",");
					if(GoodsStatus.NO_FOUND_PRODUCT.equals(gs.getResult()))
						notFoundGoods_C_SB.append(gs.getGoodsId()+",");
					if(GoodsStatus.SEARCH_RETURN_NULL.equals(gs.getResult()))
						notFoundGoods_B_SB.append(gs.getGoodsId()+",");
					break;
				case GoodsStatus.ALL_NO_STORE : 
					allNoStore_SB.append(gs.getGoodsId()+",");
					break;
				case GoodsStatus.PART_NO_STORE : 
					partNoStore_SB.append(gs.getGoodsId()+":"+gs.getResult()+"|"+LINE_SYMBO);
					break;
				case GoodsStatus.ALL_HAVE_STORE : 
					allHaveStore_SB.append(gs.getGoodsId()+",");
					break;
				case GoodsStatus.NOT_PRIMARY_GOODSID : 
					noPrimaryGoods_SB.append(gs.getGoodsId()+",");
					break;
				default : logger.info("not found handle switch processor : "+gs.getGoodsId());break;
			}
		}
		StringBuilder writeStr = new StringBuilder();
		writeStr.append("未搜索到的："+LINE_SYMBO);
		writeStr.append("搜索发生异常："+LINE_SYMBO);
		writeStr.append(notFoundGoods_A_SB);
		writeStr.append(LINE_SYMBO);
		writeStr.append("搜索返回空："+LINE_SYMBO);
		writeStr.append(notFoundGoods_B_SB);
		writeStr.append(LINE_SYMBO);
		writeStr.append("未找到商品："+LINE_SYMBO);
		writeStr.append(notFoundGoods_C_SB);
		writeStr.append(LINE_SYMBO);
		writeStr.append("全部下架："+LINE_SYMBO);
		writeStr.append(allNoStore_SB);
		writeStr.append(LINE_SYMBO);
		writeStr.append("部分缺货："+LINE_SYMBO);
		writeStr.append(partNoStore_SB);
		writeStr.append(LINE_SYMBO);
		writeStr.append("全部有货："+LINE_SYMBO);	
		writeStr.append(allHaveStore_SB);
		writeStr.append(LINE_SYMBO);
		writeStr.append("商品编号不唯一："+LINE_SYMBO);	
		writeStr.append(noPrimaryGoods_SB);
		writeStr.append(LINE_SYMBO);
		String path = CoreyangHelper.CHECKSTORE;
		String file = "check_result_"+CoreyangUtil.formatDate(new Date(), "yyyyMMddHHmmss")+".txt";
		try {
			CoreyangUtil.writeFile(writeStr.toString(), path, file);
		} catch (IOException e) {
			logger.error("writeFile Error ", e);
		}
	}
	
	public static void main(String[] args) {
		SearchGoodsService ss = new SearchGoodsService();
		String filePath = "D:/货号.txt";
		ss.seach(filePath);
	}
	
}
