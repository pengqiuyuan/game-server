package com.enlight.game.entity;

import java.util.List;
import java.util.Set;



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
	
	private String giftId;
	
	private String userId;
	
	private String serverZoneId;
	
	private String gameId;
	
	private String[] servers;
	
	/**
	 * guid
	 */
	private List<String> playerId;
	
	private String category;
	
	private String number;
	
	private Set<GiftItem> giftItems;
	
	/**
	 * 时间戳
	 */
	private Long beginDate;
	
	private Long endDate;

	private String status;
	



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

	public Set<GiftItem> getGiftItems() {
		return giftItems;
	}

	public void setGiftItems(Set<GiftItem> giftItems) {
		this.giftItems = giftItems;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String[] getServers() {
		return servers;
	}

	public void setServers(String[] servers) {
		this.servers = servers;
	}

	public Long getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Long beginDate) {
		this.beginDate = beginDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

}
