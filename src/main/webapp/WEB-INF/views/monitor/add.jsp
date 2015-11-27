<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>新增监控</title>
</head>

<body>

	<div class="page-header">
   		<h2>新增监控</h2>
 	</div>
 	 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/monitor/save"   enctype="multipart/form-data" >
			<div class="control-group">
				<label class="control-label" for="storeId">选择游戏项目：</label>
				<div class="controls">
					<select name="storeId" id="storeId">	
					    <option value="">请选择项目</option>	
						<c:forEach items="${stores}" var="item" >
								<option value="${item.id }"  >
								${item.name }
								</option>
						</c:forEach>
					</select>	
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="monitorKey">监控字段名称：</label>
				<div class="controls">
					<select name="monitorKey" id="monitorKey">
						<option value="">请选择监控字段</option>
						<option value="增加的道具数量">item_mount</option>
						<option value="日志道具id">item_id</option>
						<option value="增加的充值币数量" >money_mount</option>
						<option value="增加的游戏币数量" >coin_mount</option>
					</select>
					<span id='addMonitorValue' class='add btn btn-primary' style='display:none;margin-left: 1em'>增加监控字段值</span>
				</div>
			</div>	
			<div class="control-group" id="eqlDiv">
				<label class="control-label" for="eql">判断值：</label>
				<div class="controls">
					<select name="eql" id="eql">
						<option value="">请选择</option>
						<option value="gte">大于</option>
						<option value="eql" >等于</option>
					</select>
				</div>
			</div>
			
			<div class="page-header"></div>
			<div class="control-group" id="monitorDiv">
				<label class="control-label" for="monitorValue">监控字段值：</label>
				<div class="controls">
					<input type="text" name="monitorValue" class="input-large "/>
				</div>	
			</div>	
			
 			<div class="form-actions">
  			     <button type="submit" class="btn btn-primary" id="submit">保存</button>
				 <a href="<%=request.getContextPath()%>/manage/monitor/index" class="btn btn-primary">返回</a>
	        </div>
	</form>
	<script type="text/javascript">
	    $(function(){	
			$("#inputForm").validate({
				rules:{
					storeId:{
						required:true
					},
					monitorKey:{
						required:true
					},
					eql:{
						required:true
					},
					monitorValue:{
						required:true
					}
				},messages:{
					storeId:{
						required:"游戏项目必须填写"
					},
					monitorKey:{
						required:"监控字段名称必须填写"
					},
					eql:{
						required:"判断值必须填写"
					},
					monitorValue:{
						required:"监控字段值必须填写"
					}
				}
			});
			
			$("#monitorKey").change(function(e){
				if($("#monitorKey").val() == '日志道具id' ){
					$("#addMonitorValue").show();
					$('#eqlDiv').empty();
					$('#eqlDiv').append("<label class='control-label' for='eql'>判断值：</label><div class='controls'><select name='eql' id='eql'><option value=''>请选择</option><option value='eql' >等于</option></select></div>");
				}else{
					$('#addMonitorValue').hide();
					$('#eqlDiv').empty();
					$('#eqlDiv').append("<label class='control-label' for='eql'>判断值：</label><div class='controls'><select name='eql' id='eql'><option value=''>请选择</option><option value='gte'>大于</option><option value='eql' >等于</option></select></div>");
				}
			});
			
			$("#addMonitorValue").click(function(e){
				$("#monitorDiv").after("<div class='control-group'><div class='controls'><input type='text' name='monitorValue' class='input-large'/><span id='delMonitorValue' class='del btn btn-danger' style=' margin-left: 1em'>移除监控字段值</span></div></div>");
				$("#delMonitorValue").click(function(e){
					$(this).parent().parent().remove();
				});
			});
			
		})
	</script> 
</body>