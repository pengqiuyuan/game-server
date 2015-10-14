package com.enlight.game.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author pengqiuyuan
 *
 */
@Entity
@Table(name = "game_stores")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)   // 二级缓存
public class Stores {
	
	/**
	 * 有效
	 */
	public static final String STATUS_VALIDE = "1";
	/**
	 * 无效
	 */
	public static final String STATUS_INVALIDE = "0";

	@Id
	@Column(name = "store_id", updatable = false)
	private Long id;
	
	/**
	 * 名称
	 */
	private String name;
	
	private String status;
	
	@Column(name = "cr_date")
	private Date createDate;
	
	@Column(name = "upd_date")
	private Date updDate;

	
	@OneToMany(cascade = CascadeType.ALL,  
            fetch = FetchType.LAZY)  
	@JoinColumn(name="storeId")
	@JsonIgnore
	private Set<User> users  = new HashSet<User>(); 
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getUpdDate() {
		return updDate;
	}
	public void setUpdDate(Date updDate) {
		this.updDate = updDate;
	}
		
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
