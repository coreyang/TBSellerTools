package com.coreyang.bean;

public class GoodsStatus {
	
	private String goodsId;
	
	/**
	 * 0:没有搜索到
	 * 1:全部缺货，可以下架
	 * 2.部分缺货，会给出缺货尺码
	 * 3.全尺码有货
	 * 4.商品ID不唯一，能够搜索到多个商品
	 */
	private int status;
	
	/**
	 * 0:搜索发生异常|搜索返回空|未找到商品
	 * 1:找到商品，但是全部缺货
	 * 2.找到商品，列出缺货尺码：（颜色1&&尺码1|颜色1&&尺码1）
	 * 3.全尺码有货
	 */
	private String result;
	
	/**
	 * 没有找到商品
	 */
	public static final int NOT_FOUND = 0;
	
	/**
	 * 全部缺货
	 */
	public static final int ALL_NO_STORE = 1;
	
	/**
	 * 部分缺货
	 */
	public static final int PART_NO_STORE = 2;
	
	/**
	 * 全部有货
	 */
	public static final int ALL_HAVE_STORE = 3;
	
	/**
	 * 商品编号不唯一
	 */
	public static final int NOT_PRIMARY_GOODSID = 4;
	
	/**
	 * 搜索发生异常
	 */
	public static final String SEARCH_EXCEPTION = "A";
	
	/**
	 * 搜索返回空
	 */
	public static final String SEARCH_RETURN_NULL = "B";
	
	/**
	 * 未找到商品
	 */
	public static final String NO_FOUND_PRODUCT = "C";
	
	public GoodsStatus(String goodsId, int status, String result) {
		super();
		this.goodsId = goodsId;
		this.status = status;
		this.result = result;
	}
	
	public GoodsStatus() {
		super();
	}
	
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public int getStatus() {
		return status;
	}
	/**
	 * 0:没有搜索到
	 * 1:全部缺货，可以下架
	 * 2.部分缺货，会给出缺货尺码
	 * 3.全尺码有货
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	public String getResult() {
		return result;
	}
	/**
	 * 0:搜索发生异常|搜索返回空|未找到商品
	 * 1:找到商品，但是全部缺货
	 * 2.找到商品，列出缺货尺码：（颜色1&&尺码1|颜色1&&尺码1）
	 * 3.全尺码有货
	 */
	public void setResult(String result) {
		this.result = result;
	}
	
	
}
