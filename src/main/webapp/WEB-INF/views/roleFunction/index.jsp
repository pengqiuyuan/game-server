<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<script type="text/javascript" src="${ctx}/static/js/jquery-ui-1.8.21.custom.min.js"></script>



<title>游戏功能权限分配管理</title>
</head>
<body>
   
	<div >
		<div class="page-header">
			<h2>游戏功能权限分配管理</h2>
		</div>
		<div>
		 <c:if test="${not empty message}">
			<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
		</c:if>
				<form id="queryForm" class="well form-inline"  method="get"
					action="${ctx}/manage/roleFunction/index">
					<label>名称：</label> 
						 <select name="search_LIKE_gameName">		
							<c:forEach items="${stores}" var="item" >
								<option value="${item.name}"  ${param.search_EQ_gameId == item.id ? 'selected' : '' }>
									${item.name }
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
					<th title="项目名称">项目名称</th>
					<th title="代表权限">代表权限</th>
					<th title="功能名称">功能名称</th>
					<th title="创建时间" width="240px">创建时间</th>
					<th title="修改时间" width="240px">修改时间</th>
				</tr>
			</thead>
			<tbody id="tbody">
				<c:forEach items="${roleFunctions.content}" var="item" varStatus="s">
					<tr id="${item.id}" >
						<td id="iDictionary" value="${item.id}">
							<div class="btn-group">
								<a class="btn" href="#">#${s.index+1}</a> <a
									class="btn dropdown-toggle" data-toggle="dropdown" href="#">
									<span class="caret"></span>
								</a>
								<ul class="dropdown-menu">
									<li><a href="<%=request.getContextPath()%>/manage/roleFunction/edit?id=${item.id}"><i
											class="icon-edit"></i>修改</a></li>
								<shiro:hasAnyRoles name="admin">
									<li><a href="javascript:void(0);" rel="${item.id}"
										class="del"><i class="icon-th"></i>删除单个功能 </a></li>
								</shiro:hasAnyRoles>
									
								</ul>
							</div>
						</td>
						<td>${item.gameName}</td>
						<td>
						<a href="<%=request.getContextPath()%>/manage/roleFunction/detail?id=${item.id}"
							data-fancybox-type="iframe" rel="fancy" title="权限详细" class="showInfo" >${item.role }</a>
						</td>
					    <td>${item.function}、${item.functionName}</td>
						<td><fmt:formatDate value="${item.crDate}" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
						<td><fmt:formatDate value="${item.updDate}" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<tags:pagination page="${roleFunctions}" paginationSize="5"/>
	<shiro:hasAnyRoles name="admin">
		<div class="form-actions">
			<a href="<%=request.getContextPath()%>/manage/roleFunction/add"
				class="btn btn-primary">新增权限</a>	
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
						url: '<%=request.getContextPath()%>/manage/roleFunction/del?id=' + id, 
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