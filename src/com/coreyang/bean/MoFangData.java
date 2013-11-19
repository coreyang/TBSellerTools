package com.coreyang.bean;
//序号	关键词	搜索人气	搜索指数	占比	点击指数	商城点击占比	点击率	当前宝贝数	转化率	直通车	单个宝贝搜索次数	千个宝贝成交数
//1		休闲裤	18,399	47,883	18.07%	32,099	55.59%	66.49%	3,096,518	1.61%	5.23
public class MoFangData {
	
	/**
	 * 关键词
	 */
	private String keywords;
	/**
	 * 搜索人气
	 */
	private int searchPopularity;
	/**
	 * 搜索指数
	 */
	private int searchNumber;
	/**
	 * 占比
	 */
	private double proportion;
	/**
	 * 点击指数
	 */
	private int clickNumber;
	/**
	 * 商城点击占比
	 */
	private double tmallProportion;
	/**
	 * 点击率
	 */
	private double clickProportion;
	/**
	 * 当前宝贝数
	 */
	private int goodsNumber;
	/**
	 * 转化率
	 */
	private double conversion;
	/**
	 * 直通车竞价
	 */
	private double throughTrain;
	/**
	 * 单个宝贝搜索次数(搜索指数/当前宝贝数)
	 */
	private	double oneGoodSearchNum;
	/**
	 * 千个宝贝成交数(点击指数*转化率/当前宝贝数*1000)
	 */
	private double buyerPerThousandGood;
	
	public MoFangData(){
		
	}
	
	public MoFangData(String keywords, int searchPopularity, int searchNumber,
			double proportion, int clickNumber, double tmallProportion,
			double clickProportion, int goodsNumber, double conversion,
			double throughTrain, double oneGoodSearchNum,
			double buyerPerThousandGood) {
		super();
		this.keywords = keywords;
		this.searchPopularity = searchPopularity;
		this.searchNumber = searchNumber;
		this.proportion = proportion;
		this.clickNumber = clickNumber;
		this.tmallProportion = tmallProportion;
		this.clickProportion = clickProportion;
		this.goodsNumber = goodsNumber;
		this.conversion = conversion;
		this.throughTrain = throughTrain;
		this.oneGoodSearchNum = oneGoodSearchNum;
		this.buyerPerThousandGood = buyerPerThousandGood;
	}



	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public int getSearchPopularity() {
		return searchPopularity;
	}
	public void setSearchPopularity(int searchPopularity) {
		this.searchPopularity = searchPopularity;
	}
	public int getSearchNumber() {
		return searchNumber;
	}
	public void setSearchNumber(int searchNumber) {
		this.searchNumber = searchNumber;
	}
	public double getProportion() {
		return proportion;
	}
	public void setProportion(double proportion) {
		this.proportion = proportion;
	}
	public int getClickNumber() {
		return clickNumber;
	}
	public void setClickNumber(int clickNumber) {
		this.clickNumber = clickNumber;
	}
	public double getTmallProportion() {
		return tmallProportion;
	}
	public void setTmallProportion(double tmallProportion) {
		this.tmallProportion = tmallProportion;
	}
	public double getClickProportion() {
		return clickProportion;
	}
	public void setClickProportion(double clickProportion) {
		this.clickProportion = clickProportion;
	}
	public int getGoodsNumber() {
		return goodsNumber;
	}
	public void setGoodsNumber(int goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	public double getConversion() {
		return conversion;
	}
	public void setConversion(double conversion) {
		this.conversion = conversion;
	}
	public double getThroughTrain() {
		return throughTrain;
	}
	public void setThroughTrain(double throughTrain) {
		this.throughTrain = throughTrain;
	}
	public double getOneGoodSearchNum() {
		return oneGoodSearchNum;
	}
	public void setOneGoodSearchNum(double oneGoodSearchNum) {
		this.oneGoodSearchNum = oneGoodSearchNum;
	}
	public double getBuyerPerThousandGood() {
		return buyerPerThousandGood;
	}
	public void setBuyerPerThousandGood(double buyerPerThousandGood) {
		this.buyerPerThousandGood = buyerPerThousandGood;
	}
}
