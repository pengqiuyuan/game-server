package com.enlight.game.entity;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 权限表、功能表的中间表 
 * @author dell
 *
 */
@Entity
@Table(name = "game_role_and_enum")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)   // 二级缓存
public class RoleAndEnum extends BaseEntry{
	
	public final static String STATUS_VALIDE="1";
	public final static String STATUS_INVALIDE="0";

	/**
	 * 权限表id
	 */
	private Long roleRunctionId;
	
	/**
	 * 功能表 权限 1,2,3,4,5...
	 */
	private String enumRole;
	
	/**
	 * 功能名称（1 服务器搜索与查看，2 服务器配置与开关，6 新增邮件）
	 */
	private String enumName;

	public Long getRoleRunctionId() {
		return roleRunctionId;
	}

	public void setRoleRunctionId(Long roleRunctionId) {
		this.roleRunctionId = roleRunctionId;
	}

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
	

}
