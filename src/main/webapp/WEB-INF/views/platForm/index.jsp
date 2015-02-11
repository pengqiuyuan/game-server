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
<title>渠道管理</title>
</head>
<body>
	<div >
		<div class="page-header">
			<h2>渠道管理</h2>
		</div>
		<div>
		 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
		</c:if>
				<form id="queryForm" class="well form-inline"  method="get" action="${ctx}/manage/platForm/index">
					<label>运营大区：</label> 
					<select name="search_LIKE_serverZoneId">		
						<option value="">选择运营大区</option>
						<c:forEach items="${serverZones}" var="item" >
							<option value="${item.id}"  ${param.search_LIKE_serverZoneId == item.id ? 'selected' : '' }>
								${item.serverName}
							</option>	
						</c:forEach>
					</select>
					<label>渠道ID：</label> <input name="search_LIKE_pfId" type="text" value="${param.search_LIKE_pfId}" /> 
					<label>渠道名称：</label> <input name="search_LIKE_pfName" type="text" value="${param.search_LIKE_pfName}" /> 
					<input type="submit" class="btn" value="查 找" />
				<tags:sort />
				</form>
		</div>
		<table class="table table-striped table-bordered table-condensed" id="table">
			<thead>
				<tr>
					<th title="编号" width="120px">编号</th>
					<th title="渠道编号">渠道编号</th>
					<th title="渠道名称">渠道名称</th>
					<th title="大区名称">大区名称</th>
					<th title="创建时间">创建时间</th>
					<th title="修改时间">修改时间</th>
				</tr>
			</thead>
			<tbody id="tbody">
				<c:forEach items="${platForms.content}" var="item" varStatus="s">
					<tr id="${item.id}">
						<td id="iDictionary" value="${item.id}">
							<div class="btn-group">
								<a class="btn" href="#">#${item.id}</a> <a
									class="btn dropdown-toggle" data-toggle="dropdown" href="#">
									<span class="caret"></span>
								</a>
								<ul class="dropdown-menu">
									<shiro:hasAnyRoles name="admin,12">
									<li><a href="<%=request.getContextPath()%>/manage/platForm/edit?id=${item.id}"><i class="icon-edit"></i>修改</a></li>
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
						<td>
						<a
							href="<%=request.getContextPath()%>/manage/platForm/detail?id=${item.id}"
							data-fancybox-type="iframe" rel="fancy" title="渠道编号" class="showInfo" >${item.pfId}</a>
						</td>
						<td>${item.pfName}</td>
						<td>${item.serverZone.serverName}</td>
						<td><fmt:formatDate value="${item.crDate}" pattern="yyyy/MM/dd  HH:mm:ss" /></td>
						<td><fmt:formatDate value="${item.updDate}" pattern="yyyy/MM/dd  HH:mm:ss" /></td>	
					</tr>
				</c:forEach>
			</tbody>
		</table>

		
		<tags:pagination page="${platForms}" paginationSize="5"/>
		
	<shiro:hasAnyRoles name="admin,11">
		<div class="form-actions">
			<a href="<%=request.getContextPath()%>/manage/platForm/add" class="btn btn-primary">新增渠道</a>	
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
						url: '<%=request.getContextPath()%>/manage/server/del?id=' + id, 
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