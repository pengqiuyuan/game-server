package com.enlight.game.entity.gm.xyj;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 活动
 * @author dell
 *
 */
@Entity
@Table(name = "game_gm_event_prototype")
public class EventPrototype {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String serverZoneId;
	
	private String gameId;
	
	/**
	 * 活动类型
	 * 界面在ui上呈现类型，同时确定哪种活动 
	 * 	枚举1 首冲礼包
		枚举2 月卡、终生卡
		枚举3 普通副本产出翻倍
		枚举4 精英副本产出翻倍
		枚举5 活动副本产生翻倍
		枚举6 上述三副本产出双倍
		枚举7 限时购买
		枚举8 一般条件活动
		枚举9  VIP礼包
		枚举10 连续登录奖励
		枚举11 成长基金
		枚举12 兑换奖励
		枚举13 特殊

	 */
	private String eventType;
	
	/**
	 * 激活方式 
	 * 0通常，普通激活，以表时间active_data作为激活基点时间
	   1开服激活，已服务器首次正式开启，作为时间基点
	   2玩家首次登陆激活，已完首次登陆作为时间基点
	 */
	private String activeType;
	
	/**
	 * 激活日期
		active_type==0 激活时间基点
		active_type==1、2字段无效
		格式 2016-07-08 23:59:59
	 */
	private String activeData;
	
	/**
	 * 星期几激活
	 * 0无时间点要求
		1-7 星期一~星期日
		通过计算后如果任务激活时间不满足该星期几的要求，则需要等到下一个距离最近的满足条件的日期进行激活

	 */
	private String activeDay;
	
	/**
	 * 激活延迟
	 * 标识激活时间的延迟，单位为天，根据上一个字段进行延迟计算
		如果：
		active_type==0 则在更新表格后第N天后激活
		active_type==1 则在开服后第N天激活
		active_type==2 则在玩家首次登陆后第N天激活

	 */
	private String activeDelay;
	
	/**
	 * 持续时间
	 * 单位小时
		如果填写-1 则无限时长
		如果填写0 则活动关闭状态
	 */
	private String times;
	
	/**
	 * 重复激活时间间隔
	 * 0 不重复激活
		n 以“天(24小时)”为单位，任务结束时开始计时，n天后活动再次激活；激活如果不符合激活时间点的星期需求，则满足n天要求后按照星期需求进行激活（可延后）
	 */
	private String eventRepeatInterval;
	
	/**
	 * 激活后续活动id
	 * 该活动关闭后激活的其他活动id
	 */
	private String followingEvent;
	
	/**
	 * 激活活动所需玩家等级上下限
	 * 格式 下限_上限 闭区间，玩家等级制满足就在区间之内才能获得该活动
	 * 活动上限
	 */
	private String roleLevelMin;
	
	/**
	 * 激活活动所需玩家等级上下限
	 * 格式 下限_上限 闭区间，玩家等级制满足就在区间之内才能获得该活动
	 * 活动下线
	 */
	private String roleLevelMax;
	
	/**
	 * 主界面显示位置优先级
	 * 如果为-1则不会在主界面显示
		如果大于等于0则在主界面显示独立按钮图标
		当大于0时标识对应图标在所处位置显示优先级，数值越大越靠右
	 */
	private String mainUiPosition;
	
	/**
	 * 活动标题
	 */
	private String eventTitle;
	
	/**
	 * 活动名称
	 */
	private String eventName;
	
	/**
	 * 活动描述
	 */
	private String eventDes;
	
	/**
	 * 活动图标
	 */
	private String eventPic;
	
	/**
	 * 活动角标
	 */
	private String eventIcon;
	
	/**
	 * 显示优先级
	 * 活动在列表中标签位置优先级，越大越靠前
	 */
	private String listPriority;
	
	/**
	 * 关闭后是否隐藏
	 * 条目完全完成后是否隐藏 0不隐藏 1隐藏
		只针对因下属条目完全完成而导致的关闭做处理，活动持续时间到达而关闭的强制隐藏
	 */
	private String doneHiding;
	
	/**
	 * 活动大图，默认给 0
	 */
	private String eventShow;
	
	@Transient
	private List<EventDataPrototype> eventDataPrototypes;

	public List<EventDataPrototype> getEventDataPrototypes() {
		return eventDataPrototypes;
	}

	public void setEventDataPrototypes(List<EventDataPrototype> eventDataPrototypes) {
		this.eventDataPrototypes = eventDataPrototypes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getActiveType() {
		return activeType;
	}

	public void setActiveType(String activeType) {
		this.activeType = activeType;
	}

	public String getActiveData() {
		return activeData;
	}

	public void setActiveData(String activeData) {
		this.activeData = activeData;
	}

	public String getActiveDay() {
		return activeDay;
	}

	public void setActiveDay(String activeDay) {
		this.activeDay = activeDay;
	}

	public String getActiveDelay() {
		return activeDelay;
	}

	public void setActiveDelay(String activeDelay) {
		this.activeDelay = activeDelay;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getEventRepeatInterval() {
		return eventRepeatInterval;
	}

	public void setEventRepeatInterval(String eventRepeatInterval) {
		this.eventRepeatInterval = eventRepeatInterval;
	}

	public String getFollowingEvent() {
		return followingEvent;
	}

	public void setFollowingEvent(String followingEvent) {
		this.followingEvent = followingEvent;
	}

	public String getMainUiPosition() {
		return mainUiPosition;
	}

	public void setMainUiPosition(String mainUiPosition) {
		this.mainUiPosition = mainUiPosition;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventDes() {
		return eventDes;
	}

	public void setEventDes(String eventDes) {
		this.eventDes = eventDes;
	}

	public String getEventPic() {
		return eventPic;
	}

	public void setEventPic(String eventPic) {
		this.eventPic = eventPic;
	}

	public String getEventIcon() {
		return eventIcon;
	}

	public void setEventIcon(String eventIcon) {
		this.eventIcon = eventIcon;
	}

	public String getListPriority() {
		return listPriority;
	}

	public void setListPriority(String listPriority) {
		this.listPriority = listPriority;
	}

	public String getDoneHiding() {
		return doneHiding;
	}

	public void setDoneHiding(String doneHiding) {
		this.doneHiding = doneHiding;
	}

	public String getServerZoneId() {
		return serverZoneId;
	}

	public void setServerZoneId(String serverZoneId) {
		this.serverZoneId = serverZoneId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getRoleLevelMin() {
		return roleLevelMin;
	}

	public void setRoleLevelMin(String roleLevelMin) {
		this.roleLevelMin = roleLevelMin;
	}

	public String getRoleLevelMax() {
		return roleLevelMax;
	}

	public void setRoleLevelMax(String roleLevelMax) {
		this.roleLevelMax = roleLevelMax;
	}

	public String getEventShow() {
		return eventShow;
	}

	public void setEventShow(String eventShow) {
		this.eventShow = eventShow;
	}
	
	
}
