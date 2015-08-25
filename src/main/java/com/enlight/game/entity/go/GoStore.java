package com.enlight.game.entity.go;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author dell
 * 游戏服务器的所有项目
 */
@Entity
@Table(name="game_go_store")
public class GoStore{
	
	private int id;
	
	private int storeId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	
}
