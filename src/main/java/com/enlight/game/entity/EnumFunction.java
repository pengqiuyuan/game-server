package com.enlight.game.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @author dell
 * 枚举功能表，保存每一个功能的权限、功能名称、项目id、项目名称（游戏）
 */
@Entity
@Table(name = "game_enum")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)   // 二级缓存
public class EnumFunction extends BaseEntry{
	/**
	 * 道具日志 数据库对应enum_role字段
	 */
	public final static String ENUM_ITEM = "22";
	/**
	 * 体力日志
	 */
	public final static String ENUM_AP = "21";
	/**
	 * 真实充值币日志
	 */
	public final static String ENUM_MONEY = "23";
	/**
	 * 虚拟充值币日志
	 */
	public final static String ENUM_DUMMY = "24";
	/**
	 * 游戏币日志
	 */
	public final static String ENUM_COIN = "25";
	/**
	 * 竞技场徽章(货币)日志
	 */
	public final static String ENUM_ARENACOIN = "26";
	/**
	 * 燃烧远征龙鳞币(货币)日志
	 */
	public final static String ENUM_EXPEDITIONCOIN = "27";
	/**
	 * 用户相关日志
	 */
	public final static String ENUM_USER = "28";
	
	/**
	 * 用户画像
	 */
	public final static String ENUM_USER_PORTRAIT = "29";
	
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
	private String gameId;
	
	/**
	 * 功能分类编号 GM平台功能10000、 礼品卡平台功能10001、统计中心10002
	 */
	private String categoryId;
	

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

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	

}
