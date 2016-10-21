package com.enlight.game.entity.gm.xyj;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 活动条目数据
 * @author dell
 *
 */
@Entity
@Table(name = "game_gm_event_data_prototype")
public class EventDataPrototype {
	
	/**
	 * 数据索引
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long eventDataId;
	
	/**
	 * 从属关系
	 * 与event_prototpe对应，确定该条目从属于那个活动大类，如果填0则没有从属
	 */
	private Long eventId;
	
	/**
	 * 条目分组
	 * 确定活动条目对应分组情况，该字段以及与其对应的字段应该只会在具有独立主界面图标的单一活动里面用到
	 */
	@Column(name = "event_group")
	private String group;
	
	/**
	 * 条目名称
	 */
	private String eventDataName;
	
	/**
	 * 条目描述
	 */
	private String eventDataDes;
	
	/**
	 * 条目开启延迟
	 * 0 与evet_prototype大类一起开启 N 比大类延迟N天
	 */
	private String eventDataDelay;
	
	/**
	 * 持续时间
	 * 标识开启之后持续多久关闭，单位小时
		如果填写-1 则无限时长
		如果填写0 则活动关闭状态
		条目关闭时间服从所从属的活动关闭时间，如果活动关闭则其下条目全部强制关闭
	 */
	private String eventDataTimes;
	
	/**
	 * vip上限
	 * 标识完成该条目所需vip上下限 格式为 下限_上限，闭区间每个条目描述对应文本
	 */
	private String vipMin;
	
	/**
	 * vip下限
	 * 标识完成该条目所需vip上下限 格式为 下限_上限，闭区间每个条目描述对应文本
	 */
	private String vipMax;
	
	/**
	 * 活动条目条件类型
	 * 标注目标记录类型
		1为条目开启时记录
		2为生涯记录
	 */
	private String eventConditionType;
	
	/**
	 * 活动条目需求条件
	 */
	private String eventCondition;
	
	/**
	 * 条目条件参数1
	 */
	private String conditionValue1;
	
	/**
	 * 条目条件参数2
	 */
	private String conditionValue2;
	
	/**
	 * 活动条目奖励道具id
	 */
	private String eventRewards;
	
	/**
	 * 活动条目奖励道具数量
	 */
	private String eventRewardsNum;

	public Long getEventDataId() {
		return eventDataId;
	}

	public void setEventDataId(Long eventDataId) {
		this.eventDataId = eventDataId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getEventDataName() {
		return eventDataName;
	}

	public void setEventDataName(String eventDataName) {
		this.eventDataName = eventDataName;
	}

	public String getEventDataDes() {
		return eventDataDes;
	}

	public void setEventDataDes(String eventDataDes) {
		this.eventDataDes = eventDataDes;
	}

	public String getEventDataDelay() {
		return eventDataDelay;
	}

	public void setEventDataDelay(String eventDataDelay) {
		this.eventDataDelay = eventDataDelay;
	}

	public String getEventDataTimes() {
		return eventDataTimes;
	}

	public void setEventDataTimes(String eventDataTimes) {
		this.eventDataTimes = eventDataTimes;
	}

	public String getVipMin() {
		return vipMin;
	}

	public void setVipMin(String vipMin) {
		this.vipMin = vipMin;
	}

	public String getVipMax() {
		return vipMax;
	}

	public void setVipMax(String vipMax) {
		this.vipMax = vipMax;
	}

	public String getEventConditionType() {
		return eventConditionType;
	}

	public void setEventConditionType(String eventConditionType) {
		this.eventConditionType = eventConditionType;
	}

	public String getEventCondition() {
		return eventCondition;
	}

	public void setEventCondition(String eventCondition) {
		this.eventCondition = eventCondition;
	}

	public String getConditionValue1() {
		return conditionValue1;
	}

	public void setConditionValue1(String conditionValue1) {
		this.conditionValue1 = conditionValue1;
	}

	public String getConditionValue2() {
		return conditionValue2;
	}

	public void setConditionValue2(String conditionValue2) {
		this.conditionValue2 = conditionValue2;
	}

	public String getEventRewards() {
		return eventRewards;
	}

	public void setEventRewards(String eventRewards) {
		this.eventRewards = eventRewards;
	}

	public String getEventRewardsNum() {
		return eventRewardsNum;
	}

	public void setEventRewardsNum(String eventRewardsNum) {
		this.eventRewardsNum = eventRewardsNum;
	}
	
	
}
