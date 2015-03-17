package com.enlight.game.entity;

public class GiftSearch {
	
	/**
	 * 礼品卡类型   1未使用 
	 */
	public static final String STATUS_1 = "1";
	/**
	 * 礼品卡类型  2已使用 
	 */
	public static final String STATUS_2 = "2";
	/**
	 * 礼品卡类型  0 不存在  
	 */
	public static final String STATUS_0 = "0";
	
	//礼品码id
	private String giftId;
	
	//礼品码
	private String giftcode;
	
	//GUID
	private String guid;
	
	//平台id
	private String platformId;
	
	//使用时间
	private String begindate;
	
	//生成礼品卡gm
	private String userId;
	
	//渠道Id
	private String channelId;
	
	//礼品码使用  2已使用 1未使用 0 不存在  
	private String status;

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public String getGiftcode() {
		return giftcode;
	}

	public void setGiftcode(String giftcode) {
		this.giftcode = giftcode;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
