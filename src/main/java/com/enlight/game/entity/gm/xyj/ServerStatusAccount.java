package com.enlight.game.entity.gm.xyj;

import java.util.List;

/**
 * @author dell
 * 灰度账号
 */
public class ServerStatusAccount {
	
	private String serverZoneId;
	
	private String gameId;
	
	private String serverId;

	private List<ServerStatusGrayList> grayList;

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

	public List<ServerStatusGrayList> getGrayList() {
		return grayList;
	}

	public void setGrayList(List<ServerStatusGrayList> grayList) {
		this.grayList = grayList;
	}

}
