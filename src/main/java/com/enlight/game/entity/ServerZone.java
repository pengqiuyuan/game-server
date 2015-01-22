package com.enlight.game.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 服务器大区 IOS官方 IOS越狱 安卓官方 安卓滚服
 * @author dell
 *
 */
@Entity
@Table(name = "game_server_zone")
public class ServerZone extends BaseEntry{
	
	/**
	 * 有效
	 */
	public static final String STATUS_VALIDE = "1";
	/**
	 * 无效
	 */
	public static final String STATUS_INVALIDE = "0";

	private String serverName;

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	
}
