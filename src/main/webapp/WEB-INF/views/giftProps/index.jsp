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
	<script type="text/javascript" src="${ctx}/static/js/jquery-ui-1.8.21.custom.min.js"></script>
<title>礼品卡道具管理</title>
</head>
<body>
	<div >
		<div class="page-header">
			<h2>礼品卡道具管理</h2>
		</div>
		<div>
		 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
		</c:if>
				<form id="queryForm" class="well form-inline"  method="get" action="${ctx}/manage/giftProps/index">
					<label>项目选择：</label> 
					<select name="search_LIKE_gameId">		
						<option value="0">选择项目</option>
						<c:forEach items="${stores}" var="item" >
							<option value="${item.id}"  ${param.search_LIKE_gameId == item.id ? 'selected' : '' }>
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
					<th title="项目编号">项目编号</th>
					<th title="道具Id">道具Id</th>
					<th title="道具名称">道具名称</th>
					<th title="创建时间">创建时间</th>
				</tr>
			</thead>
			<tbody id="tbody">
				<c:forEach items="${giftProps.content}" var="item" varStatus="s">
					<tr id="${item.id}">
						<td id="iDictionary" value="${item.id}">
							<div class="btn-group">
								<a class="btn" href="#">#${item.id}</a> <a
									class="btn dropdown-toggle" data-toggle="dropdown" href="#">
									<span class="caret"></span>
								</a>
								<ul class="dropdown-menu">
											<shiro:hasAnyRoles name="admin">
												<c:if test="${item.id == 0 ? false : true}">
												<li><a href="javascript:void(0);" rel="${item.id}" class="del"><i class="icon-th"></i>删除 </a></li>
												</c:if>
									</shiro:hasAnyRoles>
									<li class="divider"></li>
								</ul>
							</div>
						</td>
						<td><huake:getStoreNameTag id="${item.gameId}"></huake:getStoreNameTag></td>
						<td>${item.itemId}</td>
						<td>${item.itemName}</td>
						<td><fmt:formatDate value="${item.crDate}" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		
		<tags:pagination page="${giftProps}" paginationSize="5"/>
		
	<shiro:hasAnyRoles name="admin,29">
		<div class="form-actions">
			<a href="<%=request.getContextPath()%>/manage/giftProps/add" class="btn btn-primary">项目新增道具</a>	
		</div>
	</shiro:hasAnyRoles>
	</div>
		<script type="text/javascript">
		$(document).ready(function(){
			
			$(".del").click(function(){
				if(confirm("该操作会删除。。。。！"))
			    {
				var id = $(this).attr("rel");
					$.ajax({
						url: '<%=request.getContextPath()%>/manage/giftProps/del?id=' + id, 
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