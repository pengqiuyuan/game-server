package com.enlight.game.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 服务器大区 IOS官方 IOS越狱 安卓官方 安卓滚服
 * @author dell
 *
 */
@Entity
@Table(name = "game_server_zone")
public class ServerZone{
	
	/**
	 * 有效
	 */
	public static final String STATUS_VALIDE = "1";
	/**
	 * 无效
	 */
	public static final String STATUS_INVALIDE = "0";

	protected Long id;
	
	private Date crDate;
	
	private Date updDate;
	
	private String status;
	
	private String serverName;

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCrDate() {
		return crDate;
	}

	public void setCrDate(Date crDate) {
		this.crDate = crDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getUpdDate() {
		return updDate;
	}

	public void setUpdDate(Date updDate) {
		this.updDate = updDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
