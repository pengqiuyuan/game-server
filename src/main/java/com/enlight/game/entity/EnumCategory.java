package com.enlight.game.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "game_enum_category")
public class EnumCategory extends BaseEntry{
	
	/**
	 * 功能分类编号 GM平台功能10000、 礼品卡平台功能10001、统计中心10002
	 */	
	private String categoryName;

	private Set<EnumFunction> enumFunctions = new HashSet<EnumFunction>();
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name="categoryId")
	@OrderBy(value = "id ASC")//注释指明加载EnumFunction时按id的升序排序  
	public Set<EnumFunction> getEnumFunctions() {
		return enumFunctions;
	}

	public void setEnumFunctions(Set<EnumFunction> enumFunctions) {
		this.enumFunctions = enumFunctions;
	}
	
	
}
