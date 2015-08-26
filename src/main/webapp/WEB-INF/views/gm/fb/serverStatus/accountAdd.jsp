<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
					<select name="gameId">		
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
					<select name="serverZoneId">		
						<option value="">选择运营大区</option>
						<c:forEach items="${serverZones}" var="item" >
							<option value="${item.serverZoneId}">
								<huake:getGoServerZoneNameTag id="${item.serverZoneId }"></huake:getGoServerZoneNameTag>
							</option>	
						</c:forEach>
					</select>
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="serverId">服务器ID：</label>
				<div class="controls">
					<input type="text" name="serverId" class="input-large " />
				</div>
			</div>	
			<div
				class="control-group">
				<label class="control-label" for="platFormId">渠道ID：</label>
				<div class="controls">
					<input type="text" name="platFormId" class="input-large "  />
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
			platFormId:{
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
			platFormId:{
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
