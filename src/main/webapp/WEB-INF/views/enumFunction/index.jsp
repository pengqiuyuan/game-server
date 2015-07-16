<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>平台功能</title>
</head>
<body>
	<div >
		<div class="page-header">
			<h2>平台功能</h2>
		</div>
		<div>
		 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
		</c:if>
				<form id="queryForm" class="well form-inline"  method="get" action="${ctx}/manage/enumFunction/index">
					<label>游戏项目：</label> 
					<select name="search_LIKE_gameId">		
						<option value="">选择游戏项目</option>
						<option value="0" ${param.search_LIKE_gameId == 0 ? 'selected' : '' }>通用</option>
						<c:forEach items="${stores}" var="item" >
							<option value="${item.id}"  ${param.search_LIKE_gameId == item.id ? 'selected' : '' }>
								${item.name}
							</option>	
						</c:forEach>
					</select>
					<label>平台：</label> 
					<select name="search_LIKE_categoryId">		
						<option value="">选择平台</option>
						<c:forEach items="${enumCategories}" var="item" >
							<option value="${item.id}"  ${param.search_LIKE_categoryId == item.id ? 'selected' : '' }>
								${item.categoryName}
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
					<th title="功能名称">功能名称</th>
					<th title="功能权限">功能权限</th>
					<th title="平台ID">平台ID</th>
					<th title="游戏ID">游戏ID</th>
					<th title="创建时间">创建时间</th>
					<th title="修改时间">修改时间</th>
				</tr>
			</thead>
			<tbody id="tbody">
				<c:forEach items="${enumFunctions.content}" var="item" varStatus="s">
					<tr id="${item.id}">
						<td id="iDictionary" value="${item.id}">
							<div class="btn-group">
								<a class="btn" href="#">#${item.id}</a> <a
									class="btn dropdown-toggle" data-toggle="dropdown" href="#">
									<span class="caret"></span>
								</a>
								<ul class="dropdown-menu">
									<shiro:hasAnyRoles name="admin">
									<li><a href="<%=request.getContextPath()%>/manage/enumFunction/edit?id=${item.id}"><i class="icon-edit"></i>修改</a></li>
									</shiro:hasAnyRoles>
											<shiro:hasAnyRoles name="admin">
												<c:if test="${item.id == 0 ? false : true}">
												<li><a href="javascript:void(0);" rel="${item.id}" class="del"><i class="icon-th"></i>删除 </a></li>
												</c:if>
									</shiro:hasAnyRoles>
									<li class="divider"></li>
								</ul>
							</div>
						</td>
						<td>${item.enumName}</td>
						<td>${item.enumRole}</td>
						<td>${item.categoryId}</td>
						<td>${item.gameId}</td>
						<td><fmt:formatDate value="${item.crDate}" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
						<td><fmt:formatDate value="${item.updDate}" pattern="yyyy/MM/dd  HH:mm:ss" /></td>	
					</tr>
				</c:forEach>
			</tbody>
		</table>

		
		<tags:pagination page="${enumFunctions}" paginationSize="5"/>
		
	<shiro:hasAnyRoles name="admin">
		<div class="form-actions">
			<a href="<%=request.getContextPath()%>/manage/enumFunction/add" class="btn btn-primary">新增功能</a>	
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
						url: '<%=request.getContextPath()%>/manage/enumFunction/del?id=' + id, 
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