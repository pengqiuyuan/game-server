<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>服务器状态设置</title>
</head>
<body>
	<div >
		<div class="page-header">
			<h2>服务器状态设置</h2>
		</div>
		<div>
				<c:if test="${not empty message}">
				<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
				</c:if>
				<form id="queryForm" class="well form-inline"  method="get" action="${ctx}/manage/gm/fb/serverStatus/index">
					<label>游戏项目：</label> 
					<select name="search_LIKE_storeId">		
						<option value="">选择游戏项目</option>
						<c:forEach items="${stores}" var="item" >
							<option value="${item.id}"  ${param.search_LIKE_storeId == item.id ? 'selected' : '' }>
								${item.name}
							</option>	
						</c:forEach>
					</select>
					<label>运营大区：</label> 
					<select name="search_LIKE_serverZoneId">		
						<option value="">选择运营大区</option>
						<c:forEach items="${serverZones}" var="item" >
							<option value="${item.id}"  ${param.search_LIKE_serverZoneId == item.id ? 'selected' : '' }>
								${item.serverName}
							</option>	
						</c:forEach>
					</select>
					<input type="submit" class="btn" value="查 找" />
				</form>
		</div>
		
		<form id="inputForm" method="post" Class="form-horizontal" action="<%=request.getContextPath()%>/manage/gm/fb/serverStatus/update"   enctype="multipart/form-data" >
			<div class="control-group">
				<table class="table table-striped table-bordered table-condensed" id="table">
					<thead>
						<tr>
							<th class="check-header hidden-xs">
		                      <label><input id="checkAll" name="checkAll" type="checkbox"><span></span></label>
		                    </th>
							<th title="服务器ID">服务器ID</th>
							<th title="服务器名称">服务器名称</th>
							<th title="状态">状态</th>
						</tr>
					</thead>
					<tbody id="tbody">
						<c:forEach items="${serverStatus.content}" var="item" varStatus="s">
							<tr id="${item.id}">
								<td><label><input  type="checkbox" class="checkbox" name="checkId" value="${item.id}"><span></span></label></td>
								<td>${item.serverId}</td>
								<td>${item.serverName}</td>
								<td>${item.status}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<tags:pagination page="${serverStatus}" paginationSize="5"/>
			</div>
			<div class="control-group " style="display: none;"  >
				<label class="control-label">游戏项目：</label> 
				<div class="controls">	
				<select name="search_LIKE_storeId">		
						<option value="">选择游戏项目</option>
						<c:forEach items="${stores}" var="item" >
							<option value="${item.id}"  ${param.search_LIKE_storeId == item.id ? 'selected' : '' }>
								${item.name}
							</option>	
						</c:forEach>
				</select>
				</div>
		    </div>	
		    <div class="control-group " style="display: none;"  >		
				<label class="control-label">运营大区：</label> 
				<div class="controls">	
				<select  name="search_LIKE_serverZoneId">		
						<option value="">选择运营大区</option>
						<c:forEach items="${serverZones}" var="item" >
							<option value="${item.id}"  ${param.search_LIKE_serverZoneId == item.id ? 'selected' : '' }>
								${item.serverName}
							</option>	
						</c:forEach>
				</select>
				</div>
			</div>
			<div class="control-group cStatus" style="display: none;"  >
				<label class="control-label">服务器状态：</label> 
				<div class="controls">	
					<select name="checkStatus">	
							<option value="1">维护</option>
							<option value="2">新区</option>
							<option value="3">良好</option>
							<option value="4">爆满</option>
					</select>	
				</div>			
			</div>
			<shiro:hasAnyRoles name="admin">
				<div class="form-actions cStatus" style="display: none;">
					<input type="submit" class="btn btn-primary" value="修改服务器信息" />
				</div>
			</shiro:hasAnyRoles>
		</form>


	</div>
		<script type="text/javascript">
			$("#checkAll").click(function(){
		         var bischecked=$('#checkAll').is(':checked');
		         var f=$('input[class="checkbox"]');
		         bischecked?f.attr('checked',true):f.attr('checked',false);
		         var m=$('.cStatus');
		         bischecked?m.show():m.hide();
			});
			$(".checkbox").click(function(){
		         var bischecked=$('.checkbox').is(':checked');
		         var m=$('.cStatus');
		         bischecked?m.show():m.hide();
			});
			
		</script> 	
</body>
</html>