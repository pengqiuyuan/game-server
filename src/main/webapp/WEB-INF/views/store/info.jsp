<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="huake" uri="/huake"%>

<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>部门信息</title>
<style type="text/css">
	p span{width: 100px; display: inline-block; font-weight: bold;}
	#customer{float: left; }
	#type{float: right; width: 50%;}
	.clear{clear: both;}
</style>
</head>
<body>
		<div class="page-header">
			<h2>部门名称：${store.name }</h2>
		</div>
		<div>
			<div id="store" >
			<p><span>地址：</span>${store.addr }</p>
			<p><span>电话：</span>${store.tel }</p>
			<p><span>创建时间：</span><fmt:formatDate value="${store.createDate}" pattern="yyyy/MM/dd  HH:mm:ss" /></p>
		</div>
		
	
		</div>
		<div class="clear"></div>
</body>
</html>