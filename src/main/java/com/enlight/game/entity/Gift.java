package com.enlight.game.entity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * 礼品卡
 * @author dell
 *
 */
public class Gift {
	
	
	/**
	 * 审核中
	 */
	public static final String STATUS_CHECKING = "0";
	/**
	 * 已通过
	 */
	public static final String STATUS_PASS = "1";
	/**
	 * 被拒绝
	 */
	public static final String STATUS_REFUSAL = "2";
	
	/**
	 * 礼品卡类型
	 */
	public static final String CATEGORY_ONE = "1";
	public static final String CATEGORY_TWO = "2";
	public static final String CATEGORY_THREE = "3";
	/**
	 * admin用户
	 */
	public static final String CATEGORY_ADMIN = "1";
	/**
	 * 普通用户
	 */
	public static final String CATEGORY_ORDINART = "0";
	
	private Long userId;
	
	private Long serverZoneId;
	
	private Long gameId;
	
	private List<GiftServer> giftServers;
	
	/**
	 * guid
	 */
	private List<String> playerId;
	
	private String category;
	
	private String number;
	
	private String coin;
	
	private String diamond;
	
	private String arenacoin;
	
	private String expeditioncoin;
	
	private String tradecoin;
	
	private Set<GiftItem> giftItems;
	
	private Date beginDate;
	
	private Date endDate;
	
	private String status;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getServerZoneId() {
		return serverZoneId;
	}

	public void setServerZoneId(Long serverZoneId) {
		this.serverZoneId = serverZoneId;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public List<GiftServer> getGiftServers() {
		return giftServers;
	}

	public void setGiftServers(List<GiftServer> giftServers) {
		this.giftServers = giftServers;
	}

	public List<String> getPlayerId() {
		return playerId;
	}

	public void setPlayerId(List<String> playerId) {
		this.playerId = playerId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

	public String getDiamond() {
		return diamond;
	}

	public void setDiamond(String diamond) {
		this.diamond = diamond;
	}

	public String getArenacoin() {
		return arenacoin;
	}

	public void setArenacoin(String arenacoin) {
		this.arenacoin = arenacoin;
	}

	public String getExpeditioncoin() {
		return expeditioncoin;
	}

	public void setExpeditioncoin(String expeditioncoin) {
		this.expeditioncoin = expeditioncoin;
	}

	public String getTradecoin() {
		return tradecoin;
	}

	public void setTradecoin(String tradecoin) {
		this.tradecoin = tradecoin;
	}

	public Set<GiftItem> getGiftItems() {
		return giftItems;
	}

	public void setGiftItems(Set<GiftItem> giftItems) {
		this.giftItems = giftItems;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
