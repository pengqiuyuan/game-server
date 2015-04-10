package com.enlight.game.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "game_enum_category")
public class EnumCategory extends BaseEntry{
	
	/**
	 * 功能分类编号 GM平台功能10000、 礼品卡平台功能10001、统计中心10002
	 */	
	public final static Long ENUM_GM = 10000L;
	
	public final static Long ENUM_GIFT = 10001L;
	
	public final static Long ENUM_COUNT = 10002L;
	
	private String categoryName;

	private Set<EnumFunction> enumFunctions = new HashSet<EnumFunction>();
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	@Transient
	public Set<EnumFunction> getEnumFunctions() {
		return enumFunctions;
	}

	public void setEnumFunctions(Set<EnumFunction> enumFunctions) {
		this.enumFunctions = enumFunctions;
	}
	
	
}
