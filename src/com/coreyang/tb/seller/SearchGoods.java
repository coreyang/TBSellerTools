package com.coreyang.tb.seller;

import com.coreyang.bean.GoodsStatus;

public interface SearchGoods {
	
	/**
	 * 查询商品
	 * @param goodsId
	 * @return 搜索结果
	 */
	public GoodsStatus search(String goodsId);
	
	/**
	 * 进入商品详情页
	 * @param goodUrl
	 * @return
	 */
	public void gotoDetail(GoodsStatus gs,String goodUrl);
}
