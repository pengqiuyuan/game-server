<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="huake" uri="/huake"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>用户项目权限查看</title>
<style type="text/css"> 
.error{ 
	color:Red; 
	margin-left:10px;  
} 
</style> 
</head>

<body>
	<div class="page-header">
   		<h2>用户项目权限查看</h2>
 	</div>

	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/user/update" >
	<input type="hidden" id="userId" name="id" value="${user.id}" > 
	  <div
	class="control-group">
	<label class="control-label" for="name">用户名:</label>
	<div class="controls">
		<input type="text" name="name" class="input-large " value="${user.name }"  disabled="disabled" />
	</div>
</div>

<div
	class="control-group">
	<label class="control-label" for="loginName">登入名:</label>
	<div class="controls">
		<input type="text" name="loginName" value="${user.loginName }"  disabled='disabled' class="input-large"  />
	</div>
</div>

	<div class="control-group ">
			<label class="control-label" for="serverName">服务器大区：</label>
			<div class="controls">	
				<c:forEach items="${serverZones}" var="item" varStatus="i">
							   <input type="checkbox" name="serverName" value="${item}" checked="checked"  class="box" readonly="readonly"/>
					           	<span><huake:getServerZoneNameTag id="${item}"></huake:getServerZoneNameTag></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					         <c:if test="${(i.index+1)%5 == 0}">
							<br/>
							<br/>
							</c:if>
				</c:forEach>
				<c:forEach items="${serverZonesNot}" var="item" varStatus="i">
							   <input type="checkbox" name="serverName" value="${item}" class="box" readonly="readonly"/>
					           	<span><huake:getServerZoneNameTag id="${item}"></huake:getServerZoneNameTag></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					         <c:if test="${(i.index+1)%5 == 0}">
							<br/>
							<br/>
							</c:if>
				</c:forEach>			
			</div>
	</div>
	<div class="page-header">
	</div>
	<div id="field">
		<c:forEach items="${userRoles}" var="item" varStatus="i">
			<div id="item${item.storeId}">
			<div class="control-group">
				<label class="control-label" for="storename">拥有权限项目：</label>
				<input id="storename" type="text" name="storename" value="<huake:getStoreNameTag id="${item.storeId}"></huake:getStoreNameTag>"  class="input-large"  readonly="readonly"/>
				<input type="hidden" id="storeId" name="storeId" value="${item.storeId}" >
			</div>
			<div class="control-group">
				<label for="role" class="control-label">权限组：</label>					
					<select id='roleCode' name="role" class="roleCode" disabled="disabled">		
							<c:forEach items="${item.roleFunctions}" var="it" >
								<option value="${it}" ${it == item.role ? 'selected' : '' }>
									${it}
								</option>
							</c:forEach>
					</select>
			</div>
			<div class="control-group">
					<label for="functions" class="control-label">功能选项：</label>
						<div class="controls" id="functions">
							<c:forEach items="${item.functions}" var="item" varStatus="i">
								   <input disabled="disabled" type="checkbox" name="functions" value="${item}" checked="checked" class="box" />
						           	<span>${item}、<huake:getFunctionNameTag id="${item}"></huake:getFunctionNameTag></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						         <c:if test="${(i.index+1)%5 == 0}">
								<br/>
								<br/>
								</c:if>
							</c:forEach>
						</div>
			</div>
		</div>
		</c:forEach>

	</div>


	<div
		class="control-group ">
		<label class="control-label" for="status">操作员状态:</label>
		<div class="controls">
			<select  name="status" disabled="disabled">
				<option value="1" ${user.status=='1'?'selected' : ''} >有效</option>
				<option value="2" ${user.status=='2'?'selected' : ''} >冻结</option>
			</select>
		</div>
	</div>
	<input type="hidden" name="roles" value="" >
 			<div class="form-actions">
				 <a href="<%=request.getContextPath()%>/manage/user/index" class="btn btn-primary">返回</a>
	        </div>
	</form>
	<script type="text/javascript">
	</script> 
</body>
