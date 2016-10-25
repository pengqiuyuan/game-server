<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>新增活动</title>
	<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/static/datetimepicker/bootstrap-datetimepicker.min.css">
	<style type="text/css">
		fieldset{padding:.35em .625em .75em;margin:0 2px;border:1px solid silver}
		legend{padding:.5em;border:0;width:auto}
	</style>
</head>
<body>
	<div class="page-header">
   		<h4>新增活动</h4>
 	</div>
 	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/gm/xyj/eventPrototype/save"   enctype="multipart/form-data" >
			<div class="row">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="gameId">选择游戏项目：</label>
						<div class="controls">
							<select name="gameId" id="gameId">	
							    <option value="">请选择项目</option>	
								<c:forEach items="${stores}" var="item" >
										<option value="${item.id }"  >
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
										<option value="${item.id }"  >
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
								<option value="1">首冲礼包</option>	
								<option value="2">月卡、终生卡</option>	
								<option value="3">普通副本产出翻倍</option>	
								<option value="4">精英副本产出翻倍</option>	
								<option value="5">活动副本产生翻倍</option>	
								<option value="6">普通副本、精英副本、活动副本产量双倍</option>	
								<option value="7">限时购买</option>	
								<option value="8">一般条件活动</option>	
								<option value="9">VIP礼包</option>	
								<option value="10">连续登录奖励</option>	
								<option value="11">成长基金</option>	
								<option value="12">兑换奖励</option>	
								<option value="13">特殊</option>	
							</select>	
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="mainUiPosition">主界面图标显示优先级（值越大越靠右）：</label>
						<div class="controls">
							<input type="text" name="mainUiPosition" id="mainUiPosition" class="input-large" value="-1" readonly="readonly" placeholder="值越大越靠右"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventPic">活动图标：</label>
						<div class="controls">
								<input type="text" name="eventPic" id="eventPic" class="input-large " value="" readonly="readonly" placeholder="如：huodong_icon_tongyong"/>
								<span>.png</span>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventShow">活动大图：</label>
						<div class="controls">
								<input type="text" name="eventShow" id="eventShow" class="input-large " value="" placeholder="如：huodong_icon_tongyong"/>
								<span class="add-on">.png</span>
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
								<option value="0">一般激活</option>	
								<option value="1">开服激活</option>	
								<option value="2">玩家首次登陆激活</option>	
							</select>	
						</div>
					</div>
				</div>
				<div class="span6" hidden="true" id="activeData">
					<div class="control-group">
						<label class="control-label" for="activeData">一般激活时间：</label>
						<div class="controls">
							<input type="text" name="activeData" class="form_datetime"></input> 
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="roleLevelMin">玩家等级下限：</label>
						<div class="controls">
							<input type="text" name="roleLevelMin" id="roleLevelMin" class="input-large " value="1" placeholder="如：1"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="roleLevelMax">玩家等级上限：</label>
						<div class="controls">
							<input type="text" name="roleLevelMax" id="roleLevelMax" class="input-large " value="100" placeholder="如：100"/>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="times">持续时间（小时）：</label>
						<div class="controls">
							<input type="text" name="times" id="times" class="input-large " value="-1" placeholder="单位小时，如：1"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="activeDelay">设置激活时间后N天激活：</label>
						<div class="controls">
							<input type="text" name="activeDelay" id="activeDelay" class="input-large " value="0" placeholder="单位天，如：1"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="activeDay">强制于星期N激活：</label>
						<div class="controls">
							<input type="text" name="activeDay" id="activeDay" class="input-large " value="0" placeholder="星期一，如：1"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventRepeatInterval">每隔N天重复激活：</label>
						<div class="controls">
							<input type="text" name="eventRepeatInterval" id="eventRepeatInterval" class="input-large " value="0" placeholder="单位天，如：1"/>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventName">活动名称：</label>
						<div class="controls">
							<input type="text" name="eventName" id="eventName" class="input-large "  placeholder="如：首冲超级大礼包"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventTitle">活动标题：</label>
						<div class="controls">
							<input type="text" name="eventTitle" id="eventTitle" class="input-large " placeholder="如：首冲超级大礼包"/>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="span12">
					<div class="control-group">
						<label class="control-label" for="eventDes">活动描述（144字）：</label>
						<div class="controls">
							<textarea path="text" name="eventDes" id="eventDes" cssClass="input-xlarge" value="" style="height: 100px;width: 400px" /></textarea>
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
								<option value="cz_sp_jiaobiao1.png">超值</option>	
								<option value="cz_sp_jiaobiao2.png">热</option>	
								<option value="cz_sp_jiaobiao3.png">限时</option>	
							</select>	
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="listPriority">活动优先级：</label>
						<div class="controls">
							<input type="text" name="listPriority" id="listPriority" class="input-large " value="0"  placeholder="值越大，活动处于列表越靠前的位置，如：0"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
							<label class="control-label" for="doneHiding">关闭后是否隐藏：</label>
							<div class="controls">
								<select name="doneHiding" id="doneHiding">	
									<option value="1">隐藏</option>	
									<option value="0" selected="selected">不隐藏</option>	
								</select>	
							</div>							
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="followingEvent">后续活动Id：</label>
						<div class="controls">
							<input type="text" name="followingEvent" id="followingEvent" class="input-large " value="0"  placeholder="如：0"/>
						</div>
					</div>
				</div>
			</div>
 			<div class="form-actions">
 			  	 <button type="submit" class="btn btn-primary" id="submit">继续编辑下属条目</button>
				 <a href="<%=request.getContextPath()%>/manage/gm/xyj/eventPrototype/index" class="btn btn-primary">返回放弃编辑</a>
	        </div>
	</form>
	<script type="text/javascript" src="${ctx}/static/datetimepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
	<script type="text/javascript">
		$('.form_datetime').datetimepicker({
			format : 'yyyy-mm-dd hh:ii:ss',
			language : 'zh-CN'
		});
		$(function(){
			$("#eventType").change(function(e){
				if($("#eventType").val() == '13'){
					$("#mainUiPosition").val("0");
					$('#mainUiPosition').removeAttr("readonly");					
					$("#eventPic").val("");
					$('#eventPic').removeAttr("readonly");
				}else{
					$("#mainUiPosition").val("-1");
					$('#mainUiPosition').attr("readonly","readonly");					
					$("#eventPic").val("");
					$('#eventPic').attr("readonly","readonly");
				}
			});
			
			$("#activeType").change(function(e){
				if($("#activeType").val() == '0'){
					$('#activeData').removeAttr("hidden");
				}else{
					$("input[name='activeData']").val("");
					$('#activeData').attr("hidden","true");
				}
			});
			
		    // 判断整数value是否大于或等于0
		    jQuery.validator.addMethod("isIntGteZero", function(value, element) { 
		    	if($("#eventType").val() == '13'){
			    	value=parseInt(value);      
			        return this.optional(element) || value>=0;    
		    	}else{
			    	value=parseInt(value);      
			        return this.optional(element) || value>=-1;    	 
		    	}
		    }, "必须大于或等于0");   
		    
		    // 判断玩家等级上限>=玩家等级下限
		    jQuery.validator.addMethod("isMaxGtMin", function(value, element) { 
		         value=parseInt(value);      
		         return this.optional(element) || value >= $("#roleLevelMin").val();    
		    }, "玩家等级上限必须大于或等于玩家等级下限");   
		    
		    // times（单位小时） 大于-1的整数 匹配integer
		    jQuery.validator.addMethod("isIntegerGt1", function(value, element) {       
		         return this.optional(element) || (/^[-\+]?\d+$/.test(value) && parseInt(value)>=-1);       
		    }, "请输入大于等于-1的整数");  
		    
		    // activeDelay 延迟激活(单位天) 默认为0 ，不可为负数 
		    jQuery.validator.addMethod("isIntegerGt0", function(value, element) {       
		         return this.optional(element) || (/^[-\+]?\d+$/.test(value) && parseInt(value)>=0);       
		    }, "请输入大于等于0的整数");  
		    
		     // 星期几  
		    jQuery.validator.addMethod("isActiveDay", function(value, element) {    
		      var zip = /^[0-7]$/;    
		      return this.optional(element) || (zip.test(value));    
		    }, "请输入0-7（星期几）的整数。");  
		    
			$("#inputForm").validate({
				errorPlacement: function(error, element) {  
					if ( element.is(".add-on") ){
						error.appendTo( element.parent().next().next() );
					}
					else {
						error.appendTo(element.parent()); 
					}
				},
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
						required:true,
						number:true,
						isIntGteZero:true
					},
					activeType:{
						required:true
					},
					activeData:{
						required:true,
						date:true
					},
					roleLevelMin:{
						required:true,
						number:true,
						min:0
					},
					roleLevelMax:{
						required:true,
						number:true,
						min:0,
						isMaxGtMin:true
					},
					times:{
						required:true,
						isIntegerGt1:true
					},
					activeDelay:{
						required:true,
						isIntegerGt0:true
					},
					activeDay:{
						required:true,
						isActiveDay:true
					},
					eventRepeatInterval:{
						required:true,
						isIntegerGt0:true
					},
					eventName:{
						required:true,
						maxlength:8
					},
					eventTitle:{
						required:true,
						maxlength:12
					},
					eventDes:{
						required:true,
						maxlength:144
					},
					eventIcon:{
						required:true
					},
					listPriority:{
						isIntegerGt0:true
					},
					doneHiding:{
						required:true
					},
					followingEvent:{
						isIntegerGt0:true
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
						required:"显示优先级必须填写",
						number:"必须为数字"
					},
					activeType:{
						required:"激活方式必须填写"
					},
					activeData:{
						required:"激活时间必须填写",
						date: "请输入有效的日期"
					},
					roleLevelMin:{
						required:"玩家等级下线必须填写",
						number:"必须为数字",
						min:"输入值不能小于0"
					},
					roleLevelMax:{
						required:"玩家等级上线必须填写",
						number:"必须为数字",
						min:"输入值不能小于0"
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
						required:"活动名称必须填写",
						maxlength:"最多填写8个汉字"
					},
					eventTitle:{
						required:"活动标题必须填写",
						maxlength:"最多填写12个汉字"
					},
					eventDes:{
						required:"活动描述必须填写",
						maxlength:"最多填写144个汉字"
					},
					eventIcon:{
						required:"活动角标必须填写"
					},
					listPriority:{
					},
					doneHiding:{
						required:"关闭后是否隐藏必须填写"
					},
					followingEvent:{
					}
				}
			});
		})
	
	</script> 
</body>
