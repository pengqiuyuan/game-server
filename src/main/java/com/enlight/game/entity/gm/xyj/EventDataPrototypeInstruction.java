package com.enlight.game.entity.gm.xyj;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 活动条目 活动目标条件
 * @author dell
 *
 */
@Entity
@Table(name = "game_gm_event_data_prototype_instruction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)   // 二级缓存
public class EventDataPrototypeInstruction {
	
	/**
	 * 数据索引
	 */
	@Id
	private Long id;
	
	/**
	 * 活动条目需求条件
	 */
	private String eventCondition;
	
	/**
	 * 活动条目条件类型
	 * 标注目标记录类型
		1为条目开启时记录
		2为生涯记录
	 */
	private String eventConditionType;
	
	/**
	 * 条目条件参数1
	 */
	private String conditionValue1;
	
	
	/**
	 * 条目条件参数名称1
	 */
	private String conditionName1;
	
	/**
	 * 条目条件参数2
	 */
	private String conditionValue2;
	
	/**
	 * 条目条件参数名称2
	 */
	private String conditionName2;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEventCondition() {
		return eventCondition;
	}

	public void setEventCondition(String eventCondition) {
		this.eventCondition = eventCondition;
	}

	public String getEventConditionType() {
		return eventConditionType;
	}

	public void setEventConditionType(String eventConditionType) {
		this.eventConditionType = eventConditionType;
	}

	public String getConditionValue1() {
		return conditionValue1;
	}

	public void setConditionValue1(String conditionValue1) {
		this.conditionValue1 = conditionValue1;
	}

	public String getConditionName1() {
		return conditionName1;
	}

	public void setConditionName1(String conditionName1) {
		this.conditionName1 = conditionName1;
	}

	public String getConditionValue2() {
		return conditionValue2;
	}

	public void setConditionValue2(String conditionValue2) {
		this.conditionValue2 = conditionValue2;
	}

	public String getConditionName2() {
		return conditionName2;
	}

	public void setConditionName2(String conditionName2) {
		this.conditionName2 = conditionName2;
	}
	
}
