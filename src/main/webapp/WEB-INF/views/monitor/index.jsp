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
<title>监控管理</title>
</head>
<body>
	<div >
		<div class="page-header">
			<h2>监控管理</h2>
		</div>
		<div>
		 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
		</c:if>
				<form id="queryForm" class="well form-inline"  method="get" action="${ctx}/manage/monitor/index">
					<label>游戏项目：</label> 
					<select name="search_LIKE_storeId">		
						<option value="">选择游戏项目</option>
						<c:forEach items="${stores}" var="item" >
							<option value="${item.id}"  ${param.search_LIKE_storeId == item.id ? 'selected' : '' }>
								${item.name}
							</option>	
						</c:forEach>
					</select>
					<input type="submit" class="btn" value="查 找" />
				<tags:sort />
				</form>
		</div>
		<table class="table table-striped table-bordered table-condensed" id="table">
			<thead>
				<tr>
					<th title="编号" width="120px">编号</th>
					<th title="游戏项目">游戏项目</th>
					<th title="名称">监控字段名称</th>
					<th title="判断值">判断值</th>
					<th title="监控字段值">监控字段值</th>
				</tr>
			</thead>
			<tbody id="tbody">
				<c:forEach items="${monitors.content}" var="item" varStatus="s">
					<tr id="${item.id}">
						<td id="iDictionary" value="${item.id}">
							<div class="btn-group">
								<a class="btn" href="#">#${item.id}</a> <a
									class="btn dropdown-toggle" data-toggle="dropdown" href="#">
									<span class="caret"></span>
								</a>
								<ul class="dropdown-menu">
									<shiro:hasAnyRoles name="admin,23">
									<li><a href="<%=request.getContextPath()%>/manage/monitor/edit?id=${item.id}"><i class="icon-edit"></i>修改</a></li>
									</shiro:hasAnyRoles>
											<shiro:hasAnyRoles name="admin,24">
												<c:if test="${item.id == 0 ? false : true}">
												<li><a href="javascript:void(0);" rel="${item.id}" class="del"><i class="icon-th"></i>删除 </a></li>
												</c:if>
											</shiro:hasAnyRoles>
									<li class="divider"></li>
								</ul>
							</div>
						</td>
						<td><huake:getStoreNameTag id="${item.storeId}"></huake:getStoreNameTag></td>
						<td>${item.monitorKey}</td>
						<td>
							<c:if test="${item.eql == 'gte'}">大于</c:if>
							<c:if test="${item.eql == 'eql'}">等于</c:if>	
						</td>
						<td>${item.monitorValue}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<tags:pagination page="${monitors}" paginationSize="5"/>
		
	<shiro:hasAnyRoles name="admin,22">
		<div class="form-actions">
			<a href="<%=request.getContextPath()%>/manage/monitor/add" class="btn btn-primary">新增监控</a>	
		</div>
	</shiro:hasAnyRoles>
	</div>
		<script type="text/javascript">
		$(document).ready(function(){
	
			$('.showInfo').fancybox({
				autoDimensions:false,
				width:800,
				height:500
			});
			
			$(".del").click(function(){
				if(confirm("该操作会删除。。。。！"))
			    {
				var id = $(this).attr("rel");
					$.ajax({
						url: '<%=request.getContextPath()%>/manage/monitor/del?id=' + id, 
						type: 'DELETE',
						contentType: "application/json;charset=UTF-8",
						dataType: 'json',
						success: function(data){
							window.location.href = window.location.href;
						},error:function(xhr){
							alert('错误了，请重试');
						}
					});
			     }
			});
		});
		</script> 	
</body>
</html>