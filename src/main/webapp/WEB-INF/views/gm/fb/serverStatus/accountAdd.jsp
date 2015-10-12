<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="huake" uri="/huake"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>新增灰度账号</title>
</head>

<body>

	<div class="page-header">
   		<h2>新增灰度账号</h2>
 	</div>
 	 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/gm/fb/serverStatus/accountSave"   enctype="multipart/form-data" >
			<div class="control-group">
				<label class="control-label" for="gameId">游戏项目：</label>
				<div class="controls">
					<select name="gameId" id="gameId">		
						<option value="">选择游戏项目</option>
						<c:forEach items="${stores}" var="item" >
							<option value="${item.storeId}">
								<huake:getGoStoreNameTag id="${item.storeId }"></huake:getGoStoreNameTag>
							</option>	
						</c:forEach>
					</select>
				</div>
			</div>	
			<div class="control-group">
				<label class="control-label" for="serverZoneId">运营大区：</label>
				<div class="controls">
					<select name="serverZoneId" id="serverZoneId">		
						<option value="">选择运营大区</option>
						<c:forEach items="${serverZones}" var="item" >
							<option value="${item.serverZoneId}">
								<huake:getGoServerZoneNameTag id="${item.serverZoneId }"></huake:getGoServerZoneNameTag>
							</option>	
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="">服务器列表：</label>
				<div class="controls" id="serverDiv">
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="platForm">渠道ID：</label>
				<div class="controls">
					<input type="text" name="platForm" class="input-large "  />
				</div>
			</div>	
			<div
				class="control-group">
				<label class="control-label" for="account">Account：</label>
				<div class="controls">
					<input type="text" name="account" class="input-large " />
				</div>
			</div>	
 			<div class="form-actions">
 				 <shiro:hasAnyRoles name="admin,fb_gm_account_add">
  			     	<button type="submit" class="btn btn-primary" id="submit">保存</button>
  			     </shiro:hasAnyRoles>
				 <a href="<%=request.getContextPath()%>/manage/gm/fb/serverStatus/accountIndex" class="btn btn-primary">返回</a>
	        </div>
	</form>
	<script type="text/javascript">

		$(function(){
			
			$("#serverZoneId").change(function(e){
				var serverZoneId = $("#serverZoneId").val();
				if($("#gameId").val()!=""){
					var gameId = $("#gameId").val();
					$("#serverDiv").empty();
					$.ajax({                                               
						url: '<%=request.getContextPath()%>/manage/gm/fb/serverStatus/findServers?serverZoneId='+serverZoneId+'&gameId='+gameId, 
						type: 'GET',
						contentType: "application/json;charset=UTF-8",		
						dataType: 'text',
						success: function(data){
							var parsedJson = $.parseJSON(data);
							jQuery.each(parsedJson, function(index, itemData) {
							$("#serverDiv").append("<c:forEach items='"+itemData+"' var='ite' varStatus='j'><label class='radio inline'><input type='radio' id='serverId' name='serverId' value='"+itemData.serverId+"'/><span>"+itemData.serverId+"</span><br/></label></c:forEach>"); 
							});
						},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
					});
				}
			});
			
			$("#gameId").change(function(e){
				var gameId = $("#gameId").val();
				if($("#gameId").val()!=""){
					$("#submit").removeAttr("disabled");
					$("#item").empty();
					$("#field").empty();				
				}else{
					$("#message").remove();
					$("#addfield").removeAttr("disabled");	
				}
		
				if($("#serverZoneId").val()!="" && $("#gameId").val()!=""){
					var serverZoneId = $("#serverZoneId").val();
					$("#serverDiv").empty();
					$.ajax({                                               
						url: '<%=request.getContextPath()%>/manage/gm/fb/serverStatus/findServers?serverZoneId='+serverZoneId+'&gameId='+gameId, 
						type: 'GET',
						contentType: "application/json;charset=UTF-8",		
						dataType: 'text',
						success: function(data){
							var parsedJson = $.parseJSON(data);
							jQuery.each(parsedJson, function(index, itemData) {
							$("#serverDiv").append("<c:forEach items='"+itemData+"' var='ite' varStatus='j'><label class='radio inline'><input type='radio' id='serverId' name='serverId' value='"+itemData.serverId+"'/><span>"+itemData.serverId+"</span><br/></label></c:forEach>"); 
							});
						},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
					});
				}	
				
			});
			
			
			$("#inputForm").validate({
				rules:{
					gameId:{
						required:true
					},
					serverZoneId:{
						required:true
					},
					serverId:{
						required:true
					},
					platForm:{
						required:true
					},
					account:{
						required:true
					}
				},messages:{
					gameId:{
						required:"必须填写"
					},
					serverZoneId:{
						required:"必须填写"
					},
					serverId:{
						required:"必须填写"
					},
					platForm:{
						required:"必须填写"
					},
					account:{
						required:"必须填写"
					}
				}
			});
		})


</script> 
</body>
