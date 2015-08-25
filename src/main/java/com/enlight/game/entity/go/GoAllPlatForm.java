package com.enlight.game.entity.go;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author dell
 * 游戏服务器 1台服务器多个渠道 （storeId serverZoneId platFormId serverId）联合索引
 */
@Entity
@Table(name="game_go_all_platform")
public class GoAllPlatForm {
	
	private int serverZoneId;
	
	private int storeId;
	
	private int platFormId;
	
	private int serverId;
	
	private String ip;
	
	private String port;

	public int getServerZoneId() {
		return serverZoneId;
	}

	public void setServerZoneId(int serverZoneId) {
		this.serverZoneId = serverZoneId;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public int getPlatFormId() {
		return platFormId;
	}

	public void setPlatFormId(int platFormId) {
		this.platFormId = platFormId;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	

}
