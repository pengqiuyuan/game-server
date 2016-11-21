package com.enlight.game.entity.analysis;

import java.util.Date;

public class UserRetained {
	
	public final static String KEY_ALL="all";
	
	public final static String KEY_SEVSERZONE="serverZone";
	
	public final static String KEY_PLATFORM="platForm";
	
	public final static String KEY_SEVSER="server";
	
	public final static String CT_2DAY="2Day";
	
	public final static String CT_8DAY="8Day";
	
	public final static String CT_31DAY="31Day";
	
	private String date;
	
	private String ts;
	
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
	
	/**
	 * 留存率分类 次日、8日、31日
	 */
	private String ctRetained;
	
	/**
	 * 留存率
	 */
	private String retained;

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

	public String getCtRetained() {
		return ctRetained;
	}

	public void setCtRetained(String ctRetained) {
		this.ctRetained = ctRetained;
	}

	public String getRetained() {
		return retained;
	}

	public void setRetained(String retained) {
		this.retained = retained;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

}
