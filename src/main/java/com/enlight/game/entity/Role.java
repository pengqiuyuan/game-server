package com.enlight.game.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.Index;

/**
 * 用户角色定义
 * @author pengqiuyuan
 *
 */
@Entity
@javax.persistence.Table(name="t_roles")
@org.hibernate.annotations.Table(appliesTo="t_roles", indexes = { @Index(name="idx_role_name", columnNames = { "role_name" } ) } )
public class Role implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 管理员
	 */
	public final static String ROLE_ADMIN = "admin";
	
	/**
	 * 编辑人员
	 */
	public final static String ROLE_SUBMITOR = "submitor";
	
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	/**
	 * 角色名称
	 */
	@Column(name ="role_name", nullable = false, length = 20)
	private String name;
	/**
	 * 角色描述
	 */
	@Column(name = "description", length = 255)
	private String description;
	/**
	 * 角色对应的资源
	 */
//	@ManyToMany(targetEntity = Resource.class, fetch = FetchType.EAGER)
//	@Fetch(FetchMode.SUBSELECT)
//	@JoinTable(name = "t_role_resources", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns=@JoinColumn(name = "resource_id"))
//	private List<Resource> resources;
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
}
