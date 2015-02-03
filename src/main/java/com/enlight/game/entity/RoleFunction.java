package com.enlight.game.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 权限组合表，有一个游戏项目超神学院（1）  对应一个权限（1） 对应多个选择功能（1,2,3） 
 * @author dell
 *
 */
@Entity
@Table(name = "game_role_function")
public class RoleFunction extends BaseEntry{
	
	/**
	 * 有效
	 */
	public static final String STATUS_VALIDE = "1";
	/**
	 * 无效
	 */
	public static final String STATUS_INVALIDE = "0";

	/**
	 * 项目Id （超神学院）1
	 */
	 private Long gameId;
	 
	 /**
	  * 项目名称
	  */
	 private String gameName;
	 
	/**
	 * 权限代表 1
	 */
	private String role; 
	
	/**
	 * 功能选项 （1）
	 */
	private Integer function;
	
	/**
	 * 功能名称
	 */
	private String functionName;

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getFunction() {
		return function;
	}

	public void setFunction(Integer function) {
		this.function = function;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
}
