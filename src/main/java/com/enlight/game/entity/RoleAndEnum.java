package com.enlight.game.entity;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 权限表、功能表的中间表 
 * @author dell
 *
 */
@Entity
@Table(name = "game_role_and_enum")
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
	private Integer enumRole;
	
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
	

}
