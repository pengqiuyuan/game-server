package com.enlight.game.entity.gm.xyj;

/**
 * @author dell
 *
 */
public class Placard {
	
	private int id;
	
	private String serverZoneId;
	
	private String gameId;
	
	//private String[] serverIds;
	
	private String serverId;
	
	private String version;
	
	private String contents;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

//	public String[] getServerIds() {
//		return serverIds;
//	}
//
//	public void setServerIds(String[] serverIds) {
//		this.serverIds = serverIds;
//	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}


}
