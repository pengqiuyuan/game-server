<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>活动（${eventId}）下属条目设置</title>
</head>
<body>
	<div >
		<div class="page-header">
			<h4>活动（${eventId}）下属条目设置</h4>
		</div>
		<div>
			<c:if test="${not empty message}">
				<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
			</c:if>
			<form id="queryForm" class="well form-inline" method="get" action="${ctx}/manage/gm/xyj/eventDataPrototype/index">
				<input type="hidden" name="search_EQ_eventId" value="${eventId}" > 
				<label>活动状态：</label> 
				<select name="search_EQ_eventDataTimes">
					<option value="" ${param.search_EQ_eventDataTimes == '' ? 'selected' : '' }>全部活动条目</option>
					<option value="0" ${param.search_EQ_eventDataTimes == '0' ? 'selected' : '' }>关闭的活动条目</option>
				</select>
				<input type="submit" class="btn btn-primary" value="查 找" />
				<tags:sort />
			</form>
		</div>
		<table class="table table-striped table-bordered table-condensed" id="table">
			<thead>
				<tr>
					<th title="编号" width="120px">编号</th>
					<th title="从属于（活动）">从属于（活动）</th>
					<th title="条目分组">条目分组</th>
					<th title="条目名称">条目名称</th>
					<th title="持续时间">持续时间（小时）</th>
					<th title="操作" width="400px">操作</th>
				</tr>
			</thead>
			<tbody id="tbody">
				<c:forEach items="${eventDataPrototypes.content}" var="item" varStatus="s">
					<tr id="${item.eventDataId}" ${item.eventDataTimes == 0 ? 'class="error"' : '' }>
						<td id="iDictionary" value="${item.eventDataId}">
							<div class="btn-group">
								<a class="btn" href="#">#${item.eventDataId}</a> 
								<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
									<span class="caret"></span>
								</a>
								<ul class="dropdown-menu">
									<shiro:hasAnyRoles name="admin,xyj_gm_eventdataprototype_update">
										<li><a href="<%=request.getContextPath()%>/manage/gm/xyj/eventDataPrototype/edit?eventId=${item.eventId}&eventDataId=${item.eventDataId}"><i class="icon-edit"></i>修改活动条目</a></li>
									</shiro:hasAnyRoles>
									<li class="divider"></li>
									<li><a href="#">sample</a></li>
								</ul>
							</div>
						</td>
						<td>${item.eventId}</td>
						<td>${item.group}</td>
						<td>${item.eventDataName }</td>
						<td>${item.eventDataTimes == '0' ? '活动条目关闭' : item.eventDataTimes == '-1' ? '无限时长': item.eventDataTimes}</td>
						<td>
							<div class="action-buttons">
								<shiro:hasAnyRoles name="admin,xyj_gm_eventdataprototype_update">
									<a class="exportCode btn table-actions" href="<%=request.getContextPath()%>/manage/gm/xyj/eventDataPrototype/edit?eventId=${item.eventId}&eventDataId=${item.eventDataId}" ><i class="icon-ok"></i>修改活动条目</a>
								</shiro:hasAnyRoles>
							</div>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<tags:pagination page="${eventDataPrototypes}" paginationSize="5"/>
		<div class="form-actions">
			<a href="<%=request.getContextPath()%>/manage/gm/xyj/eventPrototype/index" class="btn btn-primary">返回</a>	
		</div>
	</div>
		<script type="text/javascript">
		$(document).ready(function(){
			$(".del").click(function(){
				if(confirm("该操作会删除活动。。。。！"))
			    {
				var id = $(this).attr("rel");
					$.ajax({
						url: '<%=request.getContextPath()%>/manage/gm/xyj/eventDataPrototype/del?id=' + id, 
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