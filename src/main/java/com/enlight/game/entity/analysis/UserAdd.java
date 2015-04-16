package com.enlight.game.entity.analysis;

public class UserAdd {
	
	
	private String date;
	
	/**
	 * 项目
	 */
	private String gameId;
	
	/**
	 * 所有、运营大区、渠道、服务器
	 */
	private String key;
	
	/**
	 * 所有、运营大区、渠道、服务器
	 */
	private String value;
	
	private String userAdd;
	
	private String userTotal;
	
	private String ts;
	

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUserAdd() {
		return userAdd;
	}

	public void setUserAdd(String userAdd) {
		this.userAdd = userAdd;
	}

	public String getUserTotal() {
		return userTotal;
	}

	public void setUserTotal(String userTotal) {
		this.userTotal = userTotal;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}
	
}
