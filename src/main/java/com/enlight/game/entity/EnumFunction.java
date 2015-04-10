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
	 * 道具日志 数据库对应enum_role字段
	 */
	public final static String ENUM_ITEM = "9";
	/**
	 * 体力日志
	 */
	public final static String ENUM_AP = "10";
	/**
	 * 真实充值币日志
	 */
	public final static String ENUM_MONEY = "11";
	/**
	 * 虚拟充值币日志
	 */
	public final static String ENUM_DUMMY = "12";
	/**
	 * 游戏币日志
	 */
	public final static String ENUM_COIN = "13";
	/**
	 * 竞技场徽章(货币)日志
	 */
	public final static String ENUM_ARENACOIN = "14";
	/**
	 * 燃烧远征龙鳞币(货币)日志
	 */
	public final static String ENUM_EXPEDITIONCOIN = "15";
	/**
	 * 用户相关日志
	 */
	public final static String ENUM_USER = "16";
	
	/**
	 * 权限 1,2,3,4,5...
	 */
	private Integer enumRole;
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
	private Long gameId;
	
	/**
	 * 功能分类编号 GM平台功能10000、 礼品卡平台功能10001、统计中心10002
	 */
	private int categoryId;
	
	public Integer getEnumRole() {
		return enumRole;
	}

	public void setEnumRole(Integer enumRole) {
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

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

}
