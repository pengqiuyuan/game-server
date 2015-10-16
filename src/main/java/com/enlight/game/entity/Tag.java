package com.enlight.game.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="game_tag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)   // 二级缓存
public class Tag {
	
	//道具 获得途径
	public static final String CATEGORY_ITEM = "1";
	
	//新手引导
	public static final String CATEGORY_NEWPLAYER_GUIDE  = "2";

	private Long id;
	
	private Long tagId;
	
	private String tagName;
	
	/**
	 * 分类  道具
	 */
	private String category;
	
	private String storeId;
	
	private String storeName;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
	
}
