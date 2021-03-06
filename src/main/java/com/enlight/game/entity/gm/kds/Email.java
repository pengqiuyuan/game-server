package com.enlight.game.entity.gm.kds;

import java.util.List;

/**
 * @author dell
 * 邮件
 */
public class Email {
	
	private int id;
	
	private String serverZoneId;
	
	private String gameId;
	
	private String serverId;
	
	private String sender;
	
	private List<String> receiver;
	
	private String title;
	
	private String contents;
	
	private List<Annex> annex;
	
	private String platForm;
	
	public List<String> getReceiver() {
		return receiver;
	}

	public void setReceiver(List<String> receiver) {
		this.receiver = receiver;
	}

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

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public List<Annex> getAnnex() {
		return annex;
	}

	public void setAnnex(List<Annex> annex) {
		this.annex = annex;
	}

	public String getPlatForm() {
		return platForm;
	}

	public void setPlatForm(String platForm) {
		this.platForm = platForm;
	}

}
