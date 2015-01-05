package com.enlight.game.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 
 * @author pengqiuyuan
 *
 */
@MappedSuperclass
public class BaseEntry {

	public final static String STATUS_VALIDE="1";
	public final static String STATUS_INVALIDE="0";
	
	protected Long id;
	
	private Date crDate;
	
	private Date upDate;
	
	private String status;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCrDate() {
		return crDate;
	}

	public void setCrDate(Date crDate) {
		this.crDate = crDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getUpDate() {
		return upDate;
	}

	

	public void setUpDate(Date upDate) {
		this.upDate = upDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
