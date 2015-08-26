package com.enlight.game.entity.go;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author dell
 * 所有游戏服务器状态
 */
@Entity
@Table(name="game_go_all_server")
public class GoAllServer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int serverZoneId;
	
	private int storeId;
	
	private String serverId;
	
	private String ip;
	
	private String port;
	
	private String status;

	@Transient
	private List<String> platForms;

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

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Transient
	public List<String> getPlatForms() {
		return platForms;
	}

	public void setPlatForms(List<String> platForms) {
		this.platForms = platForms;
	}
	
}
