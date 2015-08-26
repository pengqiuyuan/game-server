package com.enlight.game.entity.go;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author dell
 * 游戏服务器所有运营大区
 */
@Entity
@Table(name="game_go_serverzone")
public class GoServerZone {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int serverZoneId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServerZoneId() {
		return serverZoneId;
	}

	public void setServerZoneId(int serverZoneId) {
		this.serverZoneId = serverZoneId;
	}
	
}
