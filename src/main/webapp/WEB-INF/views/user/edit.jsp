<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>用户修改</title>
<style type="text/css"> 
.error{ 
	color:Red; 
	margin-left:10px;  
} 
</style> 
</head>

<body>
	<div class="page-header">
   		<h2>用户修改</h2>
 	</div>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/user/update" >
	<input type="hidden" name="id" value="${user.id}" > 
	  <div
	class="control-group">
	<label class="control-label" for="name">用户名:</label>
	<div class="controls">
		<input type="text" name="name" class="input-large " value="${user.name }"   />
	</div>
</div>

<div
	class="control-group">
	<label class="control-label" for="loginName">登入名:</label>
	<div class="controls">
		<input type="text" name="loginName" value="${user.loginName }"  disabled='disabled' class="input-large"  />
	</div>
</div>

<div
	class="control-group">
	<label class="control-label" for="storeId">部门:</label>
	<div class="controls">
	
					<select name="storeId">		
				
						
					<c:forEach items="${stores}" var="item" >
					<option value="${item.id }"   ${user.storeId == item.id ? "selected":"" }>
					${item.name }
					</option>
					</c:forEach>
					</select>	
		</div>
</div>

<div
	class="control-group">
	<label class="control-label" for="type">操作员类型:</label>
	<div class="controls">
		<select  name="roles">
		<shiro:hasRole name="admin">
		    <option value="admin" ${ user.roles == 'admin' ? 'selected' : ''}>总管理员</option>
			<option value="business"  ${ user.roles == 'business' ? 'selected' : ''} >总业务员</option>
		</shiro:hasRole>	
		    <option value="store_admin"  ${ user.roles == 'store_admin' ? 'selected' : ''} >部门管理员</option>
			<option value="store_business"  ${ user.roles == 'store_business' ? 'selected' : ''} >部门业务员</option>
		</select>
	</div>
</div>


	<div
		class="control-group ">
		<label class="control-label" for="status">操作员状态:</label>
		<div class="controls">
			<select  name="status">
				<option value="1" ${user.status=='1'?'selected' : ''} >有效</option>
				<option value="2" ${user.status=='2'?'selected' : ''} >冻结</option>
			</select>
		</div>
	</div>
 			<div class="form-actions">
  			     <button type="submit" class="btn btn-primary" id="submit">保存</button>
				 <a href="<%=request.getContextPath()%>/manage/user/index" class="btn btn-primary">返回</a>
	        </div>
	</form>
	<script type="text/javascript">

$(function(){
	$("#inputForm").validate({
		rules:{
			name:{
				required:true
			},
			loginName:{
				required:true
			}
		},messages:{
			name:{
				required:"必须填写",
			},
			loginName:{
				required:"必须填写",
			}
		}
	});
})

</script> 
</body>
