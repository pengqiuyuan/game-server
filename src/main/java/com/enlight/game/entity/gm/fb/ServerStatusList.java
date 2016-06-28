package com.enlight.game.entity.gm.fb;

/**
 * @author dell
 *
 */
public class ServerStatusList {
	
	private String serverZoneId;
	
	private String gameId;
	
	private String serverId;
	
	private String status;

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
