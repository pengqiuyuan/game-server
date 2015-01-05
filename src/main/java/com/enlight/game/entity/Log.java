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
	 * 订单日志
	 */
	public static final String TYPE_ORDER = "1";
	/**
	 * 商品日志
	 */
	public static final String TYPE_PRODUCT = "2";
	/**
	 * 用户日志
	 */
	public static final String TYPE_USER = "3";
	/**
	 * 门店日志
	 */
	public static final String TYPE_STORE = "4";
	/**
	 * 劵日志
	 */
	public static final String TYPE_COUPON = "5";
	
	/**
	 * 配置日志
	 */
	public static final String TYPE_CONFIG = "6";
	/**
	 * 主题日志
	 */
	public static final String TYPE_THEME = "7";
	/**
	 * 活动日志
	 */
	public static final String TYPE_ACTIVITE = "8";
	
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
