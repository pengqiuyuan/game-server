package com.enlight.game.entity.analysis;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.enlight.game.entity.BaseEntry;

/**
 * 
 * @author apple
 * 记录游戏项目 1、累计用户 2、累计付费用户 3、累计收入金额  ， 使es optimize的时候可以关闭索引
 */
@Entity
@Table(name="game_es_user_total")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)   // 二级缓存
public class EsUserTotal{
	

	public final static String key_all="all";
	
	public final static String key_serverzone="serverZone";
	
	public final static String key_platform="platForm";
	
	public final static String key_server="server";
	
	protected Long id;
	
	private Long gameId;
	
	private String gameName;
	
	private Date totalDate;
	
	private Long totalUser;
	
	private Long totalPayUser;
	
	private Double totalIncome;
	
	private String key; //all serverzone platform server
	
	private String value;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Date getTotalDate() {
		return totalDate;
	}

	public void setTotalDate(Date totalDate) {
		this.totalDate = totalDate;
	}

	public Long getTotalUser() {
		return totalUser;
	}

	public void setTotalUser(Long totalUser) {
		this.totalUser = totalUser;
	}

	public Long getTotalPayUser() {
		return totalPayUser;
	}

	public void setTotalPayUser(Long totalPayUser) {
		this.totalPayUser = totalPayUser;
	}

	public Double getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(Double totalIncome) {
		this.totalIncome = totalIncome;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
