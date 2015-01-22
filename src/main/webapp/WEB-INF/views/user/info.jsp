<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="huake" uri="/huake"%>


<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>用户信息</title>
<style type="text/css">
	p span{width: 100px; display: inline-block; font-weight: bold;}
	#customer{float: left; }
	#type{float: right; width: 50%;}
	.clear{clear: both;}
</style>
</head>
<body>
		<div class="page-header">
			<h2>用户名：${user.name }</h2>
		</div>
		<div>
			<div id="forumUser" >
			<p><span>登入名：</span>${user.loginName }</p>
			<p><span>所属项目：</span><huake:getStoreNameTag id="${user.storeId}"></huake:getStoreNameTag></p>
			<p><span>创建时间：</span><fmt:formatDate value="${user.registerDate}" pattern="yyyy/MM/dd  HH:mm:ss" /></p>
			<p><span>状态：</span>${user.status  == 1 ? '正常' : '注销'}</p>
		
		</div>
		
	
		</div>
		<div class="clear"></div>
</body>
</html>