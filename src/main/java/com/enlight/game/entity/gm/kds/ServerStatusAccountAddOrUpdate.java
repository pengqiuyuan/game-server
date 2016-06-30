package com.enlight.game.entity.gm.kds;

import java.util.List;

/**
 * @author dell
 * 灰度账号
 */
public class ServerStatusAccountAddOrUpdate {
	
	private String serverZoneId;
	
	private String gameId;
	
	private String serverId;

	private String platForm;
	
	private String account;

	public String getPlatForm() {
		return platForm;
	}

	public void setPlatForm(String platForm) {
		this.platForm = platForm;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
}
