<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title>登录页</title>
</head>

<body>
	<form id="loginForm" action="${ctx}/login" method="post" class="form-horizontal">
		<fieldset>
			<legend>管理员登陆</legend>

	<%
	String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
	if(error != null){
	%>
		<div class="alert alert-error input-medium controls">
			<button class="close" data-dismiss="alert">×</button>登录失败，请重试.
		</div>
	<%
	}
	%>
		
		<div class="control-group">
			<label class="control-label" for="username">用户名：</label>
				<div class="controls">
					<input class="input-xlarge focused" name="username" id="username" type="text" value="">
					<span class="help-inline">请输入登录用户名.</span>
				</div>
		</div>		
		
		<div class="control-group">
			<label class="control-label" for="password">登录密码：</label>
			<div class="controls">
				<input class="input-xlarge" name="password" id="password" type="password" value="">
				<span class="help-inline">请输入登录密码.</span>
			</div>
		</div>	
		
		<div class="control-group" id="stId">

		</div>
		
		
		<div class="control-group">
			<div class="controls">
				<label class="checkbox" for="rememberMe"><input type="checkbox" id="rememberMe" name="rememberMe"/> 记住我</label>
				<input id="submit_btn" class="btn btn-primary" type="submit" value="登录"/> 
				<!-- 
			 	<span class="help-block">(管理员: <b>admin/admin</b>, 普通用户: <b>user/user</b>)</span>
			 -->
			</div>
		</div>
		</fieldset>
	</form>

	<script>
		$(document).ready(function() {
			$("#loginForm").validate();
			
			$("#username").change(function(e){
				var loginName =  $("#username").val();
				$("#stId").empty();
				e.preventDefault();
				$.ajax({
					url: '<%=request.getContextPath()%>/login/findStores?loginName=' + loginName, 
					type: 'GET',
					contentType: "application/json;charset=UTF-8",			
					dataType: 'text',
					success: function(data){
						var parsedJson = $.parseJSON(data);
						if(parsedJson==undefined || parsedJson=="" || parsedJson==null){  

						}else if(parsedJson !=null){
							 $("#stId").append("<label class='control-label' for='storeId'>项目选择：</label><div class='controls'><select name='storeId' id='storeId'></select></div>");
							 jQuery.each(parsedJson, function(index, itemData) {
							 $("#storeId").append("<option value='"+itemData.storeId+"'>"+itemData.storeName+"</option>"); 
							 });
						}
					},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
				});
			});
		});
	</script>
</body>
</html>
