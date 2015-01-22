package com.enlight.game.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableList;

@Entity
@Table(name = "game_user_role")
public class UserRole extends BaseEntry{
	/**
	 * 有效
	 */
	public static final String STATUS_VALIDE = "1";
	/**
	 * 无效
	 */
	public static final String STATUS_INVALIDE = "0";
	/**
	 * 冻结
	 */
	public static final String STATUS_FREE = "2";

	//GM用户id
	private Long userId;
	
	//服务器大区id  ios官方  IOS越狱 ,多个服务区大区，下划线隔开如：1,2,3
	private String serverZone;
	
	//项目id
	private Long storeId;
	
	//权限组
	private String role;
	
	//权限组对应功能 1,2,3
	private String functions;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getServerZone() {
		return serverZone;
	}

	public void setServerZone(String serverZone) {
		this.serverZone = serverZone;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFunctions() {
		return functions;
	}

	public void setFunctions(String functions) {
		this.functions = functions;
	}
	
	@Transient
	@JsonIgnore
	public List<String> getServerZoneList() {
		// 角色列表在数据库中实际以逗号分隔字符串存储，因此返回不能修改的List.
		return ImmutableList.copyOf(StringUtils.split(serverZone, ","));
	}
	
	@Transient
	@JsonIgnore
	public List<String> getRoleList() {
		// 角色列表在数据库中实际以逗号分隔字符串存储，因此返回不能修改的List.
		return ImmutableList.copyOf(StringUtils.split(functions, ","));
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
