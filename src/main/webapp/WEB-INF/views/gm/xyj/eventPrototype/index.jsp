<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>活动设置</title>
</head>
<body>
	<div >
		<div class="page-header">
			<h4>活动设置</h4>
		</div>
		<div>
			<c:if test="${not empty message}">
				<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
			</c:if>
			<c:if test="${not empty message1}">
				<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message1}</div>
			</c:if>
			<c:if test="${not empty message2}">
				<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message2}</div>
			</c:if>
			<form id="queryForm" class="well form-inline" method="get" action="${ctx}/manage/gm/xyj/eventPrototype/index">
				<label class="control-label" for="gameId">选择游戏项目：</label> 
				<select name="search_EQ_gameId" id="gameId">
					<option value="">请选择项目</option>
					<c:forEach items="${stores}" var="item">
						<option value="${item.id}" ${param.search_EQ_gameId== item.id ? 'selected' : '' }> ${item.name}</option>
					</c:forEach>
				</select> 
				<label>运营大区：</label> 
				<select name="search_EQ_serverZoneId">
					<option value="">选择运营大区</option>
					<c:forEach items="${serverZones}" var="item">
						<option value="${item.id}" ${param.search_EQ_serverZoneId == item.id ? 'selected' : '' }> ${item.serverName}</option>
					</c:forEach>
				</select>
				<label>活动状态：</label> 
				<select name="search_EQ_times">
					<option value="" ${param.search_EQ_times == '' ? 'selected' : '' }>全部活动</option>
					<option value="0" ${param.search_EQ_times == '0' ? 'selected' : '' }>关闭的活动</option>
				</select>
				<input type="submit" class="btn btn-primary" value="查 找" />
				<tags:sort />
			</form>
		</div>
		<table class="table table-striped table-bordered table-condensed" id="table">
			<thead>
				<tr>
					<th title="编号" width="120px">编号</th>
					<th title="名称">名称</th>
					<th title="激活方式">激活方式</th>
					<th title="激活日期">激活日期</th>
					<th title="持续时间">持续时间（小时）</th>
					<th title="重复间隔">重复间隔（天）</th>
					<th title="后续活动Id">后续活动Id</th>
					<th title="玩家等级">玩家等级</th>
					<th title="操作" width="400px">操作</th>
				</tr>
			</thead>
			<tbody id="tbody">
				<c:forEach items="${eventPrototypes.content}" var="item" varStatus="s">
					<tr id="${item.id}" ${item.times == 0 ? 'class="error"' : '' }>
						<td id="iDictionary" value="${item.id}">
							<div class="btn-group">
								<a class="btn" href="#">#${item.id}</a> 
								<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
									<span class="caret"></span>
								</a>
								<ul class="dropdown-menu">
									<shiro:hasAnyRoles name="admin,xyj_gm_eventprototype_update">
										<li><a href="<%=request.getContextPath()%>/manage/gm/xyj/eventPrototype/edit?id=${item.id}"><i class="icon-edit"></i>修改活动</a></li>
									</shiro:hasAnyRoles>
									<c:if test="${item.times != '0'}">
										<shiro:hasAnyRoles name="admin,xyj_gm_eventprototype_close">
											<!--  
											<li><a href="javascript:void(0);" rel="${item.id}" class="closeEvent"><i class="icon-th"></i>关闭活动 </a></li>-->
											<li><a href="<%=request.getContextPath()%>/manage/gm/xyj/eventPrototype/close?id=${item.id}"><i class="icon-th"></i>关闭活动 </a>
										</shiro:hasAnyRoles>
									</c:if>
									<c:if test="${item.times != '0'}">
										<shiro:hasAnyRoles name="admin,xyj_gm_eventdataprototype_add">
											<li><a href="<%=request.getContextPath()%>/manage/gm/xyj/eventDataPrototype/add?eventId=${item.id}" target=_blank><i class="icon-edit"></i>新增活动条目</a></li>
										</shiro:hasAnyRoles>
										<shiro:hasAnyRoles name="admin,xyj_gm_eventdataprototype_update">
											<li><a href="<%=request.getContextPath()%>/manage/gm/xyj/eventDataPrototype/index?search_EQ_eventId=${item.id}" target=_blank><i class="icon-edit"></i>修改活动条目</a></li>
										</shiro:hasAnyRoles>
									</c:if>
									<li class="divider"></li>
									<li><a href="#">sample</a></li>
								</ul>
							</div>
						</td>
						<td>${item.eventName}</td>
						<td>${item.activeType == '0' ? '一般激活' : item.activeType == '1' ? '开服激活': item.activeType == '2' ? '玩家首次登陆激活' : item.activeType }</td>
						<td>${item.activeType == '0' ? item.activeData : item.activeType == '1' ? '开服激活': item.activeType == '2' ? '玩家首次登陆激活' : '未知' }</td>
						<td>${item.times == '0' ? '活动关闭' : item.times == '-1' ? '无限时长': item.times}</td>
						<td>${item.eventRepeatInterval}天</td>
						<td>${item.followingEvent}</td>
						<td>${item.roleLevelMin}_${item.roleLevelMax}</td>
						<td>
							<div class="action-buttons">

								<shiro:hasAnyRoles name="admin,xyj_gm_eventprototype_update">
									<a class="exportCode btn table-actions" href="${ctx}/manage/gm/xyj/eventPrototype/edit?id=${item.id}" ><i class="icon-ok"></i>修改</a>
								</shiro:hasAnyRoles>
								<c:if test="${item.times != '0'}">
									<shiro:hasAnyRoles name="admin,xyj_gm_eventprototype_close">
										<!-- <a class="exportCode btn table-actions closeEvent" rel="${item.id}" ><i class="icon-remove"></i>关闭活动</a> -->
										<a class="exportCode btn table-actions" href="${ctx}/manage/gm/xyj/eventPrototype/close?id=${item.id}" ><i class="icon-remove"></i>关闭活动</a>
									</shiro:hasAnyRoles>
								</c:if>
							</div>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<tags:pagination page="${eventPrototypes}" paginationSize="5"/>
	<shiro:hasAnyRoles name="admin,xyj_gm_eventprototype_add">
		<div class="form-actions">
			<a href="<%=request.getContextPath()%>/manage/gm/xyj/eventPrototype/add" class="btn btn-primary">新增活动</a>	
		</div>
	</shiro:hasAnyRoles>
	</div>
		<script type="text/javascript">
		$(document).ready(function(){
			$(".closeEvent").click(function(){
				if(confirm("该操作会关闭活动。。。。！"))
			    {
				var id = $(this).attr("rel");
					$.ajax({
						url: '<%=request.getContextPath()%>/manage/gm/xyj/eventPrototype/close?id=' + id, 
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
			
			$(".del").click(function(){
				if(confirm("该操作会删除活动。。。。！"))
			    {
				var id = $(this).attr("rel");
					$.ajax({
						url: '<%=request.getContextPath()%>/manage/gm/xyj/eventPrototype/del?id=' + id, 
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