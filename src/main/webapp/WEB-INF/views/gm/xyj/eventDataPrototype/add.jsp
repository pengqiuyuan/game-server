<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>新增活动条目</title>
	<link rel="stylesheet" type="text/css" media="screen" href="${ctx}/static/datetimepicker/bootstrap-datetimepicker.min.css">
	<style type="text/css">
		fieldset{padding:.35em .625em .75em;margin:0 2px;border:1px solid silver}
		legend{padding:.5em;border:0;width:auto}
	</style>
</head>
<body>
	<div class="page-header">
   		<h4>新增活动条目</h4>
 	</div>
 	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/gm/xyj/eventDataPrototype/save"   enctype="multipart/form-data" >
			<div class="row">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventId">从属于：</label>
						<div class="controls">
								<input type="text" name="eventId" id="eventId" class="input-large" value="${eventPrototype.id}" placeholder="如：1000" readonly="readonly"/>
								<span>号活动</span>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label">活动名称：</label>
						<div class="controls">
								<input type="text" class="input-large " value="${eventPrototype.eventName}" placeholder="如：首冲大礼包" readonly="readonly"/>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="group">分组：</label>
						<div class="controls">
							<input type="text" name="group" id="group" class="input-large " value="${group == null ? '1' : group}" placeholder="如：1" ${eventPrototype.eventType == 13 ? '' : 'readonly="readonly"' }  />
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventDataName">条目名称：</label>
						<div class="controls">
							<input type="text" name="eventDataName" id="eventDataName" class="input-large " value="活动大礼" placeholder="最多输入8个汉字" readonly="readonly"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="vipMin">Vip等级下限：</label>
						<div class="controls">
							<input type="text" name="vipMin" id="vipMin" class="input-large " value="0" placeholder="如：1"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="vipMax">Vip等级上限：</label>
						<div class="controls">
							<input type="text" name="vipMax" id="vipMax" class="input-large " value="100" placeholder="如：100"/>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventDataTimes">持续时间（小时）：</label>
						<div class="controls">
							<input type="text" name="eventDataTimes" id="eventDataTimes" class="input-large " value="-1" placeholder="单位小时，如：1"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventDataDelay">于活动激活后第N天激活：</label>
						<div class="controls">
							<input type="text" name="eventDataDelay" id="eventDataDelay" class="input-large " value="0" placeholder="单位天，如：1"/>
						</div>
					</div>
				</div>
				<div class="span12">
					<div class="control-group">
						<label class="control-label" for="eventDataDes">描述：</label>
						<div class="controls">
							<input type="text" name="eventDataDes" id="eventDataDes" class="input-large " value="" placeholder="最多输入20个汉字"/>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventCondition">选择活动目标：</label>
						<div class="controls">
							<select name="eventCondition" id="eventCondition">	
							    <option value="">选择活动目标</option>	
								<c:forEach items="${eventDataPrototypeInstructions}" var="item" >
										<option value="${item.id }" >
											${item.eventCondition}
										</option>
								</c:forEach>
							</select>	
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
							<label class="control-label" for="eventConditionType">目标类型：</label>
							<div class="controls">
								<select name="eventConditionType" id="eventConditionType">
									<option value="1" selected="selected">活动开启记录</option>	
									<option value="2">生涯记录</option>	
								</select>	
							</div>							
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="conditionValue1" id="conditionValue1_name">目标值1：</label>
						<div class="controls">
							<input type="text" name="conditionValue1" id="conditionValue1" class="input-large " value="1"  placeholder="如：1"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="conditionValue2" id="conditionValue2_name">目标值2：</label>
						<div class="controls">
							<input type="text" name="conditionValue2" id="conditionValue2" class="input-large " value="1"  placeholder="如：1"/>
						</div>
					</div>
				</div>
			</div>
			<div class="row" id="rewards">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventRewards">奖励道具Id：</label>
						<div class="controls">
							<input type="text" name="eventRewards" id="eventRewards" class="input-large eventRewards"   placeholder="如：1"/>
						</div>
					</div>
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="eventRewardsNum">奖励道具数量：</label>
						<div class="controls">
							<input type="text" name="eventRewardsNum" id="eventRewardsNum" class="input-large eventRewardsNum"  placeholder="如：1"/>
						</div>
					</div>
				</div>
				<div class="span3">
					<span id="addfield" class="btn btn-primary">新增奖励</span>
					<span id="delfield" class="btn btn-danger">移除新增奖励</span>
				</div>
			</div>				
			<div class="form-actions">
 			  	 <button type="submit" class="btn btn-primary" id="submit2">保存并新增条目</button>
  			     <button type="submit" class="btn btn-primary" id="submit1">保存条目</button>
  			     <c:if test="${eventPrototype.status == '0'}">
  			     	<a href="<%=request.getContextPath()%>/manage/gm/xyj/eventDataPrototype/giveup?eventId=${eventPrototype.id}" class="btn btn-danger">放弃编辑</a>
  			     </c:if>
  			     <c:if test="${eventPrototype.status == '1'}">
  			     	<a href="<%=request.getContextPath()%>/manage/gm/xyj/eventPrototype/index" class="btn btn-danger">返回</a>
  			     </c:if>
	        </div>
	</form>
	<script type="text/javascript" src="${ctx}/static/datetimepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
	<script type="text/javascript">
		if ($.validator) {
	        $.validator.prototype.elements = function () {
	            var validator = this,
	              rulesCache = {};
	            // select all valid inputs inside the form (no submit or reset buttons)
	            return $(this.currentForm)
	            .find("input, select, textarea")
	            .not(":submit, :reset, :image, [disabled]")
	            .not(this.settings.ignore)
	            .filter(function () {
	                if (!this.name && validator.settings.debug && window.console) {
	                    console.error("%o has no name assigned", this);
	                }
	                //注释这行代码
	                // select only the first element for each name, and only those with rules specified
	                //if ( this.name in rulesCache || !validator.objectLength($(this).rules()) ) {
	                //    return false;
	                //}
	                rulesCache[this.name] = true;
	                return true;
	            });
	        }
	    }
		$("#submit2").click(function(e){
			if(!$("#submitAndEventData").length>0){  //不存在
				$("#submit2").after("<input type='hidden' name='submitAndEventData' value='submitAndEventData' id='submitAndEventData'>");
			} 
		});
		$("#submit1").click(function(e){
			if($("#submitAndEventData").length>0){  //存在
				$("#submitAndEventData").remove();
			} 
		});

		$('.form_datetime').datetimepicker({
			format : 'yyyy-mm-dd hh:ii:ss',
			language : 'zh-CN'
		});
		$(function(){
			$("#addfield").click(function(){
				$("#rewards").after("<div class='row' id='rewards2'><div class='span6'><div class='control-group'><label class='control-label' for='eventRewards'>奖励道具Id：</label><div class='controls'><input type='text' name='eventRewards' id='eventRewards' class='input-large eventRewards'   placeholder='如：1'/></div></div></div><div class='span6'><div class='control-group'><label class='control-label' for='eventRewardsNum'>奖励道具数量：</label><div class='controls'><input type='text' name='eventRewardsNum' id='eventRewardsNum' class='input-large eventRewardsNum'  placeholder='如：1'/></div></div></div></div>");
			});
			$("#delfield").click(function(){
				$("#rewards2").remove();
			});
			
			$("#eventCondition").change(function(){
				var eventConditionId = $("#eventCondition").val();
 	  			$.ajax({
	 	  			url: '<%=request.getContextPath()%>/manage/gm/xyj/eventDataPrototype/findEventDpi', 
	 	  			type: 'GET',
	 	  			contentType: "application/json;charset=UTF-8",			
	 	  			dataType: 'text',
	 	  			success: function(data){
	 	  				var parsedJson = $.parseJSON(data);
	 	  				jQuery.each(parsedJson, function(index, itemData) {
							if(eventConditionId == itemData.id){
								console.log(itemData.id  +" " +itemData.eventCondition + " "+itemData.eventConditionType + " " +itemData.conditionName1+" " + itemData.conditionValue1+" "+itemData.conditionName2 + " "+itemData.conditionValue2);
								if(itemData.eventConditionType == null){
									$("#eventConditionType option[value='1']").attr("selected", "selected");
									$("#eventConditionType option[value='2']").removeAttr("selected");
									$("#eventConditionType").removeAttr("disabled");
									$("#eventConditionType_hidden").remove();
								}else if(itemData.eventConditionType == '2'){
									$("#eventConditionType option[value='2']").attr("selected", "selected");
									$("#eventConditionType option[value='1']").removeAttr("selected");
									$("#eventConditionType").attr("disabled", "disabled");
									$("#eventConditionType").after("<input type='hidden' id='eventConditionType_hidden' name='eventConditionType' value='2'>");
								}
								if(itemData.conditionName1 == null){
									$("#conditionValue1_name").html("目标值1："); 
								}else{
									$("#conditionValue1_name").html(itemData.conditionName1+"："); 
								}
								if(itemData.conditionValue1 == null){
									$("#conditionValue1").removeAttr("readonly");
									$("#conditionValue1").val("1");
								}else if(itemData.conditionValue1 == '0'){
									$("#conditionValue1").val("0");
									$("#conditionValue1").attr("readonly","readonly");
								}
								if(itemData.conditionName2 == null){
									$("#conditionValue2_name").html("目标值2："); 
								}else{
									$("#conditionValue2_name").html(itemData.conditionName2+"："); 
								}
								if(itemData.conditionValue2 == null){
									$("#conditionValue2").removeAttr("readonly");
									$("#conditionValue2").val("1");
								}else if(itemData.conditionValue2 == '1'){
									$("#conditionValue2").val("1");
									$("#conditionValue2").attr("readonly","readonly");
								}
								
							}
	 	  				});
	 	  			},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
	 	  		});
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
		         return this.optional(element) || value >= $("#vipMin").val();    
		    }, "玩家等级上限必须大于或等于玩家等级下限");   
		    
		    // times（单位小时） 大于-1的整数 匹配integer
		    jQuery.validator.addMethod("isIntegerGt1", function(value, element) {       
		         return this.optional(element) || (/^[-\+]?\d+$/.test(value) && parseInt(value)>=-1);       
		    }, "请输入大于等于-1的整数");  
		    
		    // activeDelay 延迟激活(单位天) 默认为0 ，不可为负数 
		    jQuery.validator.addMethod("isIntegerGt0", function(value, element) {       
		         return this.optional(element) || (/^[-\+]?\d+$/.test(value) && parseInt(value)>=0);       
		    }, "请输入大于等于0的整数");  
		    
		    // conditionValue1 conditionValue2 大于1的整数 匹配integer
		    jQuery.validator.addMethod("isIntegerGt1Plus", function(value, element) {
		         return this.optional(element) || (/^[-\+]?\d+$/.test(value) && parseInt(value)>=1);       
		    }, "填写大于等于1的整数");  
		    
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
					event:{
						required:true
					},
					group:{
						required:true
					},
					eventDataName:{
						required:true,
						maxlength:8
					},
					vipMin:{
						required:true,
						number:true,
						min:0,
						max:100
					},
					vipMax:{
						required:true,
						number:true,
						min:0,
						max:100,
						isMaxGtMin:true
					},
					eventDataTimes:{
						required:true,
						isIntegerGt1:true
					},
					eventDataDelay:{
						required:true,
						isIntegerGt0:true
					},
					eventDataDes:{
						maxlength:20
					},
					eventCondition:{
						required:true
					},
					eventConditionType:{
						required:true
					},
					conditionValue1:{
						required:true,
						isIntegerGt1Plus:true
					},
					conditionValue2:{
						required:true,
						isIntegerGt1Plus:true
					},
					eventRewards:{
						required:true
					},
					eventRewardsNum:{
						required:true
					}
				},messages:{
					eventId:{
						required:"活动Id不能为空"
					},
					group:{
						required:"条目分组必须填写"
					},
					eventDataName:{
						required:"条目名称必须填写",
						maxlength:"最多填写8个汉字"
					},
					vipMin:{
						required:"vip下限必须填写",
						number:"必须为数字",
						min:"输入值不能小于0",
						max:"输入值不能大于100"
					},
					vipMax:{
						required:"vip上限必须填写",
						number:"必须为数字",
						min:"输入值不能小于0",
						max:"输入值不能大于100"
					},
					eventDataTimes:{
						required:"持续时间必须填写"
					},
					eventDataDelay:{
						required:"于活动激活后N天激活必须填写"
					},
					eventDataDes:{
						maxlength:"最多填写20个汉字"
					},
					eventCondition:{
						required:"活动目标必须填写"
					},
					eventConditionType:{
						required:"目标类型必须填写"
					},
					conditionValue1:{
						required:"目标值1必须填写"
					},
					conditionValue2:{
						required:"目标值2必须填写"
					},
					eventRewards:{
						required:"奖励道具ID必须填写"
					},
					eventRewardsNum:{
						required:"奖励道具数量必须填写"
					}
				}
			});
		})
	
	</script> 
</body>
