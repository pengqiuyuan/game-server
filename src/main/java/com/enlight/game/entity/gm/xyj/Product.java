package com.enlight.game.entity.gm.xyj;

/**
 * @author dell
 *
 */
public class Product {
	
	private int id;
    private String  serverZoneId;
    private String  gameId;
    private String 	serverId;
    
    private String	itemId;//description;道具ID
    private String  num;//description;个数
    private String  prodcutStoreId;//description;商店ID
    private String  storeLocation;//description;商品出现位置
    private String  isRandom;//        description;出现是否随机
    
    private String  randomProbability;//        description;随机概率
    private String  comsumeType;//        description;消费类型
    private String  comsumeNum;//        description;消费数量
    private String  discount;//        description;折扣率
    private String  levelLimit;//        description;玩家获取该商品的等级下限
    
    private String  levelCap;//        description;玩家获取该商品的等级上限
    private String  discountStartDate;//        description;折扣生效时间
    private String  discountContinueDate;//        description;折扣持续时间
    private String  discountCycleDate;//        description;折扣循环时间
    private String  productPostDate;//        description;商品上架时间
    
    private String  productDownDate;//        description;商品下架时间
    private String  showLevel;//        description;显示优先级
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getServerZoneId() {
		return serverZoneId;
	}
	public void setServerZoneId(String serverZoneId) {
		this.serverZoneId = serverZoneId;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getProdcutStoreId() {
		return prodcutStoreId;
	}
	public void setProdcutStoreId(String prodcutStoreId) {
		this.prodcutStoreId = prodcutStoreId;
	}
	public String getStoreLocation() {
		return storeLocation;
	}
	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}
	public String getIsRandom() {
		return isRandom;
	}
	public void setIsRandom(String isRandom) {
		this.isRandom = isRandom;
	}
	public String getRandomProbability() {
		return randomProbability;
	}
	public void setRandomProbability(String randomProbability) {
		this.randomProbability = randomProbability;
	}
	public String getComsumeType() {
		return comsumeType;
	}
	public void setComsumeType(String comsumeType) {
		this.comsumeType = comsumeType;
	}
	public String getComsumeNum() {
		return comsumeNum;
	}
	public void setComsumeNum(String comsumeNum) {
		this.comsumeNum = comsumeNum;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getLevelLimit() {
		return levelLimit;
	}
	public void setLevelLimit(String levelLimit) {
		this.levelLimit = levelLimit;
	}
	public String getLevelCap() {
		return levelCap;
	}
	public void setLevelCap(String levelCap) {
		this.levelCap = levelCap;
	}
	public String getDiscountStartDate() {
		return discountStartDate;
	}
	public void setDiscountStartDate(String discountStartDate) {
		this.discountStartDate = discountStartDate;
	}
	public String getDiscountContinueDate() {
		return discountContinueDate;
	}
	public void setDiscountContinueDate(String discountContinueDate) {
		this.discountContinueDate = discountContinueDate;
	}
	public String getDiscountCycleDate() {
		return discountCycleDate;
	}
	public void setDiscountCycleDate(String discountCycleDate) {
		this.discountCycleDate = discountCycleDate;
	}
	public String getProductPostDate() {
		return productPostDate;
	}
	public void setProductPostDate(String productPostDate) {
		this.productPostDate = productPostDate;
	}
	public String getProductDownDate() {
		return productDownDate;
	}
	public void setProductDownDate(String productDownDate) {
		this.productDownDate = productDownDate;
	}
	public String getShowLevel() {
		return showLevel;
	}
	public void setShowLevel(String showLevel) {
		this.showLevel = showLevel;
	}

    
}
