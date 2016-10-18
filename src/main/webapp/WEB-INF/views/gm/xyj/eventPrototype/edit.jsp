<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>修改活动</title>
	<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/static/datetimepicker/bootstrap-datetimepicker.min.css">
	<style type="text/css">
		fieldset{padding:.35em .625em .75em;margin:0 2px;border:1px solid silver}
		legend{padding:.5em;border:0;width:auto}
	</style>
</head>
<body>
	<div class="page-header">
   		<h4>修改活动</h4>
 	</div>
 	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/gm/xyj/eventPrototype/update"   enctype="multipart/form-data" >
			<input type="hidden" name="id" value="${eventPrototype.id}" > 
			<div class="row">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="gameId">选择游戏项目：</label>
						<div class="controls">
							<select name="gameId" id="gameId">	
							    <option value="">请选择项目</option>	
								<c:forEach items="${stores}" var="item" >
										<option value="${item.id }" ${eventPrototype.gameId == item.id ? 'selected' : '' } >
										${item.name }
										</option>
								</c:forEach>
							</select>	
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="serverZoneId">选择运营大区：</label>
						<div class="controls">
							<select name="serverZoneId" id="serverZoneId" >	
							    <option value="">选择运营大区</option>	
								<c:forEach items="${serverZones}" var="item" >
										<option value="${item.id }"  ${eventPrototype.serverZoneId == item.id ? 'selected' : '' }>
										${item.serverName }
										</option>
								</c:forEach>
							</select>	
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventType">选择活动类型：</label>
						<div class="controls">
							<select name="eventType" id="eventType">	
							    <option value="">选择活动类型</option>	
								<option value="1" ${eventPrototype.eventType == 1 ? 'selected' : '' }>首冲礼包</option>	
								<option value="2" ${eventPrototype.eventType == 2 ? 'selected' : '' }>月卡、终生卡</option>	
								<option value="3" ${eventPrototype.eventType == 3 ? 'selected' : '' }>普通副本产出翻倍</option>	
								<option value="4" ${eventPrototype.eventType == 4 ? 'selected' : '' }>精英副本产出翻倍</option>	
								<option value="5" ${eventPrototype.eventType == 5 ? 'selected' : '' }>活动副本产生翻倍</option>	
								<option value="6" ${eventPrototype.eventType == 6 ? 'selected' : '' }>普通副本、精英副本、活动副本产量双倍</option>	
								<option value="7" ${eventPrototype.eventType == 7 ? 'selected' : '' }>限时购买</option>	
								<option value="8" ${eventPrototype.eventType == 8 ? 'selected' : '' }>一般条件活动</option>	
								<option value="9" ${eventPrototype.eventType == 9 ? 'selected' : '' }>VIP礼包</option>	
								<option value="10" ${eventPrototype.eventType == 10 ? 'selected' : '' }>连续登录奖励</option>	
								<option value="11" ${eventPrototype.eventType == 11 ? 'selected' : '' }>成长基金</option>	
								<option value="12" ${eventPrototype.eventType == 12 ? 'selected' : '' }>兑换奖励</option>	
								<option value="13" ${eventPrototype.eventType == 13 ? 'selected' : '' }>特殊</option>	
							</select>	
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="mainUiPosition">主界面图标显示优先级：</label>
						<div class="controls">
							<input type="text" name="mainUiPosition" class="input-large" value="${eventPrototype.mainUiPosition}" placeholder="值越大越靠右"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventPic">活动图标：</label>
						<div class="controls">
							<input type="text" name="eventPic" class="input-large " value="${eventPrototype.eventPic}" placeholder="如：huodong_icon_tongyong.png"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventShow">活动大图：</label>
						<div class="controls">
							<input type="text" name="eventShow" class="input-large " value="${eventPrototype.eventShow}" placeholder="如：huodong_icon_tongyong.png"/>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="activeType">选择激活方式：</label>
						<div class="controls">
							<select name="activeType" id="activeType">	
							    <option value="">选择激活方式</option>	
								<option value="1" ${eventPrototype.activeType == 1 ? 'selected' : '' }>一般激活</option>	
								<option value="2" ${eventPrototype.activeType == 2 ? 'selected' : '' }>开服激活</option>	
								<option value="3" ${eventPrototype.activeType == 3 ? 'selected' : '' }>玩家首次登陆激活</option>	
							</select>	
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="activeData">一般激活时间：</label>
						<div class="controls">
							<div id="activeData" class="input-append date">
								<input type="text" name="activeData" value="${eventPrototype.activeData}"></input> 
								<span class="add-on"> 
									<i data-time-icon="icon-time" data-date-icon="icon-calendar"></i>
								</span>
							</div>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="roleLevelMin">玩家等级下限：</label>
						<div class="controls">
							<input type="text" name="roleLevelMin" class="input-large " value="${eventPrototype.roleLevelMin}" placeholder="如：1"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="roleLevelMax">玩家等级上限：</label>
						<div class="controls">
							<input type="text" name="roleLevelMax" class="input-large " value="${eventPrototype.roleLevelMax}" placeholder="如：100"/>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="times">持续时间（小时）：</label>
						<div class="controls">
							<input type="text" name="times" class="input-large " value="${eventPrototype.times}" placeholder="单位小时，如：1"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="activeDelay">设置激活时间后N天激活：</label>
						<div class="controls">
							<input type="text" name="activeDelay" class="input-large " value="${eventPrototype.activeDelay}" placeholder="单位天，如：1"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="activeDay">强制于星期N激活：</label>
						<div class="controls">
							<input type="text" name="activeDay" class="input-large " value="${eventPrototype.activeDay}" placeholder="星期一，如：1"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventRepeatInterval">每隔N天重复激活：</label>
						<div class="controls">
							<input type="text" name="eventRepeatInterval" class="input-large " value="${eventPrototype.eventRepeatInterval}" placeholder="单位天，如：1"/>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventName">活动名称：</label>
						<div class="controls">
							<input type="text" name="eventName" class="input-large " value="${eventPrototype.eventName}" placeholder="如：首冲超级大礼包"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventTitle">活动标题：</label>
						<div class="controls">
							<input type="text" name="eventTitle" class="input-large " value="${eventPrototype.eventTitle}" placeholder="如：首冲超级大礼包"/>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="span12">
					<div class="control-group">
						<label class="control-label" for="eventDes">活动描述（144字）：</label>
						<div class="controls">
							<textarea path="text" name="eventDes" cssClass="input-xlarge"  style="height: 100px;width: 400px" />${eventPrototype.eventDes}</textarea>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventIcon">选择活动角标：</label>
						<div class="controls">
							<select name="eventIcon" id="eventIcon">	
							    <option value="">选择活动角标</option>	
								<option value="cz_sp_jiaobiao1.png" ${eventPrototype.eventIcon == 'cz_sp_jiaobiao1.png' ? 'selected' : '' }>超值</option>	
								<option value="cz_sp_jiaobiao2.png" ${eventPrototype.eventIcon == 'cz_sp_jiaobiao2.png' ? 'selected' : '' }>热</option>	
								<option value="cz_sp_jiaobiao3.png" ${eventPrototype.eventIcon == 'cz_sp_jiaobiao3.png' ? 'selected' : '' }>限时</option>	
							</select>	
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="listPriority">活动优先级：</label>
						<div class="controls">
							<input type="text" name="listPriority" class="input-large " value="${eventPrototype.listPriority}"  placeholder="值越大，活动处于列表越靠前的位置，如：0"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
							<label class="control-label" for="doneHiding">关闭后是否隐藏：</label>
							<div class="controls">
								<select name="doneHiding" id="doneHiding">	
									<option value="1" ${eventPrototype.doneHiding == 1 ? 'selected' : '' }>隐藏</option>	
									<option value="0" ${eventPrototype.doneHiding == 0 ? 'selected' : '' }>不隐藏</option>	
								</select>	
							</div>							
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="followingEvent">后续活动Id：</label>
						<div class="controls">
							<input type="text" name="followingEvent" class="input-large " value="${eventPrototype.followingEvent}"  placeholder="如：0"/>
						</div>
					</div>
				</div>
			</div>
			<div class="page-header">
				 <a href="<%=request.getContextPath()%>/manage/gm/xyj/eventDataPrototype/add" class="btn btn-danger">修改下属条目</a>
			</div>		
 			<div class="form-actions">
  			     <button type="submit" class="btn btn-primary" id="submit">保存</button>
				 <a href="<%=request.getContextPath()%>/manage/gm/xyj/eventPrototype/index" class="btn btn-primary">返回</a>
	        </div>
	</form>
	<script type="text/javascript" src="${ctx}/static/datetimepicker/bootstrap-datetimepicker.min.js"></script>
	<script type="text/javascript">
		$('#activeData').datetimepicker({
			format : 'yyyy-MM-dd hh:mm:ss',
			language : 'en',
			pickDate : true,
			pickTime : true,
			hourStep : 1,
			minuteStep : 15,
			secondStep : 30,
			inputMask : true
		});
		$(function(){
			jQuery.validator.addMethod("ip", function(value, element) { 
				var tel = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/; 
				return this.optional(element) || (tel.test(value)); 
			}, "IP格式错误");
			$("#inputForm").validate({
				rules:{
					gameId:{
						required:true
					},
					serverZoneId:{
						required:true
					},
					eventType:{
						required:true
					},
					mainUiPosition:{
						required:true
					},
					eventPic:{
						required:true
					},
					eventShow:{
						required:true
					},
					activeType:{
						required:true
					},
					activeData:{
						required:true
					},
					roleLevelMin:{
						required:true
					},
					roleLevelMax:{
						required:true
					},
					times:{
						required:true
					},
					activeDelay:{
						required:true
					},
					activeDay:{
						required:true
					},
					eventRepeatInterval:{
						required:true
					},
					eventName:{
						required:true
					},
					eventTitle:{
						required:true
					},
					eventDes:{
						required:true
					},
					eventIcon:{
						required:true
					},
					listPriority:{
						required:true
					},
					doneHiding:{
						required:true
					},
					followingEvent:{
						required:true
					},
					serverId:{
						required:true,
						minlength:1,
						maxlength:7,
						remote: '<%=request.getContextPath()%>/manage/server/checkServerId'
					},
					ip:{
						ip:true
					},
					port:{
						required:true,
						number:true
					}
				},messages:{
					gameId:{
						required:"游戏项目必须填写"
					},
					serverZoneId:{
						required:"运营大区必须填写"
					},
					eventType:{
						required:"活动类型必须填写"
					},
					mainUiPosition:{
						required:"显示优先级必须填写"
					},
					eventPic:{
						required:"活动图标必须填写"
					},
					eventShow:{
						required:"活动大图必须填写"
					},
					activeType:{
						required:"激活方式必须填写"
					},
					activeData:{
						required:"激活时间必须填写"
					},
					roleLevelMin:{
						required:"玩家等级下线必须填写"
					},
					roleLevelMax:{
						required:"玩家等级上线必须填写"
					},
					times:{
						required:"持续时间必须填写"
					},
					activeDelay:{
						required:"设置激活时间后N天激活必须填写"
					},
					activeDay:{
						required:"强制星期N激活必须填写"
					},
					eventRepeatInterval:{
						required:"每隔N天重复激活必须填写"
					},
					eventName:{
						required:"活动名称必须填写"
					},
					eventTitle:{
						required:"活动标题必须填写"
					},
					eventDes:{
						required:"活动描述必须填写"
					},
					eventIcon:{
						required:"活动角标必须填写"
					},
					listPriority:{
						required:"活动优先级必须填写"
					},
					doneHiding:{
						required:"关闭后是否隐藏必须填写"
					},
					followingEvent:{
						required:"后续活动Id必须填写"
					},
					serverId:{
						required:"服务器名称必须填写",
						minlength:"游戏名称长度1-7位",
						remote: "serverId已存在"
					},
					ip:{
						required:"IP必须填写"
					},
					port:{
						required:"端口必须填写",
						number: "请输入合法的数字"
					}
				}
			});
		})
	
	</script> 
</body>
