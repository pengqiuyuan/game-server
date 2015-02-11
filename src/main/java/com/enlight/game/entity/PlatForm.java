package com.enlight.game.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 渠道管理
 * @author dell
 *
 */
@Entity
@Table(name="game_plat_form")
public class PlatForm extends BaseEntry{
	
	/**
	 * 有效
	 */
	public static final String STATUS_VALIDE = "1";
	/**
	 * 无效
	 */
	public static final String STATUS_INVALIDE = "0";
	
	/**
	 * 渠道编号
	 */
	private String pfId;
	
	private String pfName;
	
	private String serverZoneId;
	
	private ServerZone serverZone;

	public String getPfId() {
		return pfId;
	}

	public void setPfId(String pfId) {
		this.pfId = pfId;
	}

	public String getPfName() {
		return pfName;
	}

	public void setPfName(String pfName) {
		this.pfName = pfName;
	}

	public String getServerZoneId() {
		return serverZoneId;
	}

	public void setServerZoneId(String serverZoneId) {
		this.serverZoneId = serverZoneId;
	}

	@Transient
	public ServerZone getServerZone() {
		return serverZone;
	}

	public void setServerZone(ServerZone serverZone) {
		this.serverZone = serverZone;
	}
	
}
