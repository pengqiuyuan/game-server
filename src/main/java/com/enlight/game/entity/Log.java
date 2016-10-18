package com.enlight.game.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 操作日志
 * @author pengqiuyuan
 *
 */
@Entity
@Table(name = "game_log")
public class Log extends BaseEntry{
	
	/**
	 * 有效
	 */
	public static final String STATUS_VALIDE = "1";
	/**
	 * 无效
	 */
	public static final String STATUS_INVALIDE = "0";
	
	/**
	 * 用户日志
	 */
	public static final String TYPE_USER = "1";
	
	/**
	 * 项目日志
	 */
	public static final String TYPE_STORE = "2";
	
	/**
	 * GM 活动日志
	 */
	public static final String TYPE_GM_EVENT = "gm_event";
	
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 创建人
	 */
	private String crUser;
	
	
	private String type;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCrUser() {
		return crUser;
	}
	public void setCrUser(String crUser) {
		this.crUser = crUser;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
