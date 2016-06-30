<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="huake" uri="/huake"%>

<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>服务器状态设置</title>
	<style type="text/css">
		.form-ac {
		  padding: 19px 20px 20px;
		  margin-top: 20px;
		  margin-bottom: 20px;
		  padding-left: 180px;
		}
	</style>
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
			<form id="queryForm" class="form-horizontal"  method="get" action="${ctx}/manage/gm/fb/serverStatus/index">
				<div class="control-group">
					<label class="control-label" for="gameId">选择游戏项目：</label>
					<div class="controls">
							<select name="search_EQ_storeId" id="gameId">	
								<option value="">请选择项目</option>	
								<c:forEach items="${stores}" var="item" >
									<option value="${item.storeId }"   ${param.search_EQ_storeId == item.storeId ? 'selected' : '' }>
										<huake:getGoStoreNameTag id="${item.storeId }"></huake:getGoStoreNameTag>
									</option>
								</c:forEach>
							</select>	
					</div>
				</div>		
				<div class="control-group">
					<label class="control-label" for="serverZoneId">选择运营大区：</label>
					<div class="controls">
						<select name="search_EQ_serverZoneId" id="serverZoneId">	
							<option value="">请选择项目</option>	
							<c:forEach items="${serverZones}" var="item" >
								<option value="${item.serverZoneId }"  ${param.search_EQ_serverZoneId == item.serverZoneId ? 'selected' : '' }>
									<huake:getGoServerZoneNameTag id="${item.serverZoneId }"></huake:getGoServerZoneNameTag>
								</option>
							</c:forEach>
						</select>	
					</div>
				</div>	
				<shiro:hasAnyRoles name="admin,fb_gm_server_select">
					<div class="form-ac">
						<input type="submit" class="btn btn-primary" id="selBtn" value="查 找"/>
					</div>
				</shiro:hasAnyRoles>
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
		                    <th title="storeId">游戏</th>
		                    <th title="serverZoneId">运营大区</th>
							<th title="serverId">服务器Id</th>
							<th title="ip">服务器Ip</th>
							<th title="port">服务器port</th>
							<th title="status">状态</th>
						</tr>
					</thead>
					<tbody id="tbody">
						<c:forEach items="${serverStatus.content}" var="item" varStatus="s">
							<tr id="${item.id}">
								<td><label><input  type="checkbox" class="checkbox" name="checkId" value="${item.serverId}"><span></span></label></td>
								<td><huake:getGoStoreNameTag id="${item.storeId }"></huake:getGoStoreNameTag></td>
								<td><huake:getGoServerZoneNameTag id="${item.serverZoneId }"></huake:getGoServerZoneNameTag></td>
								<td>${item.serverId}</td>
								<td>${item.ip}</td>
								<td>${item.port}</td>
								<td>
									<c:choose>
										<c:when  test="${item.status eq '0'}">
											<span>维护</span>
										</c:when >
										<c:when  test="${item.status eq '1'}">
											<span>新区</span>
										</c:when >
										<c:when  test="${item.status eq '2'}">
											<span>良好</span>
										</c:when >
										<c:when  test="${item.status eq '3'}">
											<span>爆满</span>
										</c:when >
									</c:choose>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<tags:pagination page="${serverStatus}" paginationSize="5"/>
			</div>
			<div class="control-group " style="display: none;"  >
				<label class="control-label">游戏项目：</label> 
				<div class="controls">	
				<select name="search_EQ_storeId">		
						<option value="">选择游戏项目</option>
						<c:forEach items="${stores}" var="item" >
							<option value="${item.storeId}"  ${param.search_EQ_storeId == item.storeId ? 'selected' : '' }>
								<huake:getGoStoreNameTag id="${item.storeId }"></huake:getGoStoreNameTag>
							</option>	
						</c:forEach>
				</select>
				</div>
		    </div>	
		    <div class="control-group " style="display: none;"  >		
				<label class="control-label">运营大区：</label> 
				<div class="controls">	
				<select  name="search_EQ_serverZoneId">		
						<option value="">选择运营大区</option>
						<c:forEach items="${serverZones}" var="item" >
							<option value="${item.serverZoneId}"  ${param.search_EQ_serverZoneId == item.serverZoneId ? 'selected' : '' }>
								<huake:getGoServerZoneNameTag id="${item.serverZoneId }"></huake:getGoServerZoneNameTag>
							</option>	
						</c:forEach>
				</select>
				</div>
			</div>
			<div class="control-group cStatus" style="display: none;"  >
				<label class="control-label">服务器状态：</label> 
				<div class="controls">	
					<select name="checkStatus">	
							<option value="0">维护</option>
							<option value="1">新区</option>
							<option value="2">良好</option>
							<option value="3">爆满</option>
					</select>	
				</div>			
			</div>
			<shiro:hasAnyRoles name="admin,fb_gm_server_edit">
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
			$("#queryForm").validate({
				rules:{
					search_EQ_storeId:{
						required:true
					},
					search_EQ_serverZoneId:{
						required:true
					}
				},messages:{
					search_EQ_storeId:{
						required:"游戏项目"
					},
					search_EQ_serverZoneId:{
						required:"运营必须填写"
					}
				}
			});	
		</script> 	
</body>
</html>