/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.enlight.game.entity;
/**
 * 用户
 * @author pengqiuyuan
 */
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableList;

@Entity
@Table(name = "game_user")
public class User extends IdEntity{

	public static final String USER_ROLE_ADMIN = "admin";

	public static final String USER_ROLE_BUSINESS = "business";

	public static final String USER_ROLE_STORE_ADMIN = "store_admin";
	public static final String USER_ROLE_STORE_BUSINESS = "store_business";
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

	

	private String loginName;
	
	private String name;


	private String password;

	private String salt;
	private String roles;
	private Date registerDate;
	private String storeId;
	
	//服务器大区id  ios官方  IOS越狱 ,多个服务区大区，下划线隔开如：1,2,3
	private String serverZone;

	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 更新时间
	 */
	private Date updDate;
	/**
	 * 状态
	 */
	private String status;

	private String plainPassword;
	


	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public User() {
	}

	public User(Long id) {
		this.id = id;
	}

	@NotBlank
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// 不持久化到数据库，也不显示在Restful接口的属性.
	@Transient
	@JsonIgnore
	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}


	// 设定JSON序列化时的日期格式
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getUpdDate() {
		return updDate;
	}

	public void setUpdDate(Date updDate) {
		this.updDate = updDate;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getServerZone() {
		return serverZone;
	}

	public void setServerZone(String serverZone) {
		this.serverZone = serverZone;
	}
	@Transient
	@JsonIgnore
	public List<String> getRoleList() {
		// 角色列表在数据库中实际以逗号分隔字符串存储，因此返回不能修改的List.
		return ImmutableList.copyOf(StringUtils.split(roles, ","));
	}
	
	@Transient
	@JsonIgnore
	public List<String> getStoreIds() {
		// 角色列表在数据库中实际以逗号分隔字符串存储，因此返回不能修改的List.
		return ImmutableList.copyOf(StringUtils.split(storeId, ","));
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Transient
	@JsonIgnore
	public List<String> getServerZoneList() {
		// 角色列表在数据库中实际以逗号分隔字符串存储，因此返回不能修改的List.
		return ImmutableList.copyOf(StringUtils.split(serverZone, ","));
	}

}