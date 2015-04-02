package com.enlight.game.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "game_gift_props")
public class GiftProps extends BaseEntry{
	
	/**
	 * 有效
	 */
	public static final String STATUS_VALIDE = "1";
	/**
	 * 无效
	 */
	public static final String STATUS_INVALIDE = "0";
	
	private Long itemId;
	
	private String itemName;
	
	
	/**
	 * 项目Id
	 */
	private String gameId;


	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	
}
