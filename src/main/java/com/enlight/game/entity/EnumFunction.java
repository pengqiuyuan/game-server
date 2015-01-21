package com.enlight.game.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * @author dell
 * 枚举功能表，保存每一个功能的权限、功能名称、项目id、项目名称（游戏）
 */
@Entity
@Table(name = "game_enum")
public class EnumFunction extends BaseEntry{
	
	/**
	 * 权限 1,2,3,4,5...
	 */
	private String enumRole;
	/**
	 * 功能名称（1 服务器搜索与查看，2 服务器配置与开关，6 新增邮件）
	 */
	private String enumName;
	
	/**
	 * 项目名称
	 */
	private String gameName;
	
	/**
	 * 项目id
	 */
	private Integer gameId;

	public String getEnumRole() {
		return enumRole;
	}

	public void setEnumRole(String enumRole) {
		this.enumRole = enumRole;
	}

	public String getEnumName() {
		return enumName;
	}

	public void setEnumName(String enumName) {
		this.enumName = enumName;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
	
	
}
