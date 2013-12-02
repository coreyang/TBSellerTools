package com.coreyang.tool;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.coreyang.bean.MoFangData;

public class MofangJsonParse {
	
	/**
	 * 解析json数据
	 * @param jsonString 字符串
	 * @param needFilter 是否需要过滤关键词
	 * @param filterFilePath 关键词路径
	 */
	public List<MoFangData> parse(String jsonString,boolean needFilter,String filterFilePath){
		List<MoFangData> dataList = new ArrayList<MoFangData>();
		JSONArray jsonarray = JSONArray.fromObject(jsonString);
		JSONArray jsonarray1 = JSONArray.fromObject(jsonarray.get(0));
		for(Object records: jsonarray1){
			
			JSONObject jb = JSONObject.fromObject(records);
			String keywords = jb.getString("f0");
			int searchPopularity = jb.getInt("f1");
			int searchNum = jb.getInt("f2");
			double proportion = jb.getDouble("f3");
			int clickNumber = jb.getInt("f4");
			double tmallProportion = jb.getDouble("f5");
			double clickProportion = jb.getDouble("f6");
			String f7str = jb.getString("f7");
			JSONObject f7Object = JSONObject.fromObject(f7str);
			int goodsNumber = f7Object.getInt("num");
			double conversion = jb.getDouble("f8");
			String f9str = jb.getString("f9");
			if(f9str.equals("null")){
				f9str = "0";
			}
			double throughTrain = Double.valueOf(f9str);
			double oneGoodSearchNum = (double)searchNum/(double)goodsNumber;
			double buyerPerThousandGood = clickNumber*conversion/goodsNumber*1000;
			MoFangData md = new MoFangData(keywords,searchPopularity,searchNum,proportion,
					clickNumber,tmallProportion,clickProportion,goodsNumber,
					conversion,throughTrain,oneGoodSearchNum,buyerPerThousandGood);
			dataList.add(md);
		}
		return dataList;
	}
}
