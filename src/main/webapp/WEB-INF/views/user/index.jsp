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
<!--  
<link href="${ctx}/static/fancybox/jquery.fancybox.css" rel="stylesheet" />
<link href="${ctx}/static/fancybox/facebox.css" rel="stylesheet" />
<script src="${ctx}/static/js/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript"
	src="${ctx}/static/fancybox/jquery.fancybox.pack.js?v=2.1.3"></script>
-->
<!--
	<script type="text/javascript" src="${ctx}/static/fancyBox-2.1.5/lib/jquery-1.10.1.min.js"></script>
  -->


<title>后台用户管理</title>
</head>
<body>
   
	<div >
		<div class="page-header">
			<h2>后台用户管理</h2>
		</div>
		<div>
		 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
				<form id="queryForm" class="well form-inline"  method="get"
					action="${ctx}/manage/user/index">
					<label>用户名：</label> <input name="search_LIKE_name"
						type="text" value="${param.search_LIKE_name}" /> 
			      		
						<label>状态：</label> 
						
						<select name="search_EQ_status">
						<option value="">---------请选择---------</option>
						<option value="1" ${param.search_EQ_status == '1' ? 'selected' : '' } >正常</option>
						<option value="0" ${param.search_EQ_status == '0' ? 'selected' : '' }  >冻结</option>
						</select>
					
						 <input type="submit" class="btn"
						value="查 找" />
				<tags:sort />
				</form>
			
			
		</div>
		<table class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th title="编号" width="120px">编号</th>
					<th title="用户名">用户名</th>
					<th title="登入名">登入名</th>
					<th title="登入名">所属项目</th>
					<th title="创建时间" width="240px">创建时间</th>
					<th title="状态">状态</th>
					
				</tr>
			</thead>
			<tbody>

				<c:forEach items="${users.content}" var="item" varStatus="s">
				
					<tr  >
					
						<td>
							<div class="btn-group">
								<a class="btn" href="#">#${s.index+1}</a> <a
									class="btn dropdown-toggle" data-toggle="dropdown" href="#">
									<span class="caret"></span>
								</a>
								<ul class="dropdown-menu">
									<c:if test="${item.roles != 'admin' ? 'true':'false' }">
									<li><a
										href="<%=request.getContextPath()%>/manage/user/edit?id=${item.id}"><i
											class="icon-edit"></i>修改</a></li>
									<li><a href="javascript:void(0);" rel="${item.id}"
										class="del"><i class="icon-th"></i>冻结</a></li>
									<li><a href="javascript:void(0);" rel="${item.id}"
										class="unlock" ><i class="icon-th"></i>激活</a></li>
									<li><a href="javascript:void(0);" rel="${item.id}"
										class="userdel" ><i class="icon-th"></i>删除</a></li>
							            </c:if>
									<li><a
										href="<%=request.getContextPath()%>/manage/user/resetPwd?id=${item.id}"><i
											class="icon-edit"></i>重置密码</a></li>
						
								</ul>
							</div>
						</td>
						
						<td>
					
						<a
							href="<%=request.getContextPath()%>/manage/user/detail?id=${item.id}"
							 data-fancybox-type="iframe" rel="fancy" title="用户详细" class="showInfo" >${item.name }</a>
						</td>

						<td>
						${item.loginName}
						</td>
					    <td>
						<huake:getStoreNameTag id="${item.storeId}"></huake:getStoreNameTag>
						</td>
						<td><fmt:formatDate value="${item.registerDate}" pattern="yyyy/MM/dd" /></td>
						<td>${item.status == '1' ? '正常' : '冻结' }</td>
					
						
					</tr>
				</c:forEach>
			</tbody>
		</table>

		
		<tags:pagination page="${users}" paginationSize="5"/>
		
		
		
		<div class="form-actions">
			<a href="<%=request.getContextPath()%>/manage/user/add"
				class="btn btn-primary">新增用户</a>
				
		</div>
	
	</div>
		<script type="text/javascript">
		$(document).ready(function(){
			
			$(".showInfo").fancybox({
				autoDimensions:false,
				width:800,
				height:600
			});
			
			
			
			$(".del").click(function(){
				var id = $(this).attr("rel");
					$.ajax({
						url: '<%=request.getContextPath()%>/manage/user/del?id=' + id, 
						type: 'DELETE',
						contentType: "application/json;charset=UTF-8",
						dataType: 'json',
						success: function(data){
							window.location.href = window.location.href;
						},error:function(xhr){
							alert('错误了，请重试');
						}
					});
				
			});
			
			$(".userdel").click(function(){
				var id = $(this).attr("rel");
					$.ajax({
						url: '<%=request.getContextPath()%>/manage/user/delUser?id=' + id, 
						type: 'DELETE',
						contentType: "application/json;charset=UTF-8",
						dataType: 'json',
						success: function(data){
							window.location.href = window.location.href;
						},error:function(xhr){
							alert('错误了，请重试');
						}
					});
				
			});
			
			
			$(".unlock").click(function(){
				var id = $(this).attr("rel");
					$.ajax({
						url: '<%=request.getContextPath()%>/manage/user/active?id=' + id, 
						type: 'DELETE',
						contentType: "application/json;charset=UTF-8",
						dataType: 'json',
						success: function(data){
							window.location.href = window.location.href;
						},error:function(xhr){
							alert('错误了，请重试');
						}
					});
				
			});
			
			
		});
	
	
	
		
		
		</script> 	
</body>
</html>