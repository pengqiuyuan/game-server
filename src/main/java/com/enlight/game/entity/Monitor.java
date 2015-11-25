package com.enlight.game.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableList;

@Entity
@Table(name="game_monitor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)   // 二级缓存
public class Monitor {

	//大于
	public static final String gte = "gte";
	
	//等于
	public static final String equel = "eql";
	
	private Long id;
	
	private String storeId;
	
	/**
	 * item_mount(增加的道具数量) \ item_id(多个) \ money_mount(增加的充值币数量)\ coin_mount(增加的游戏币数量)
	 */
	private String monitorKey;
	
	/**
	 * 阙值
	 */
	private String monitorValue;
	
	/**
	 * 大于（gte） 等于（eql）
	 */
	private String eql;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMonitorKey() {
		return monitorKey;
	}

	public void setMonitorKey(String monitorKey) {
		this.monitorKey = monitorKey;
	}

	public String getMonitorValue() {
		return monitorValue;
	}

	public void setMonitorValue(String monitorValue) {
		this.monitorValue = monitorValue;
	}

	public String getEql() {
		return eql;
	}

	public void setEql(String eql) {
		this.eql = eql;
	}
	
	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@Transient
	@JsonIgnore
	public List<String> getValueList() {
		// 在数据库中实际以逗号分隔字符串存储，因此返回不能修改的List.
		return ImmutableList.copyOf(StringUtils.split(monitorValue, ","));
	}
	
}
