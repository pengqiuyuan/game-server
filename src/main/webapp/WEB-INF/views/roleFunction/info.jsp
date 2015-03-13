<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="huake" uri="/huake"%>

<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>功能权限管理信息</title>
<style type="text/css">
	p span{width: 100px; display: inline-block; font-weight: bold;}
	#customer{float: left; }
	#type{float: right; width: 50%;}
	.clear{clear: both;}
</style>
</head>
<body>
		<div class="page-header">
			<h2><span>项目名称：</span>${roleFunction.gameName }</h2>
		</div>
		<div>
			<div id="store" >
			<p><span>代表权限：</span>${roleFunction.role }</p>
			<p><span>功能名称：</span>
			    <br/>
			    <br/>	
				<c:forEach items="${roleFuncs}" var="item">
					<option value="${item.enumName}">
						${item.enumRole}、${item.enumName }
					</option>
					<br/>
				</c:forEach>			
			</p>
			<p><span>创建时间：</span><fmt:formatDate value="${roleFunction.crDate}" pattern="yyyy/MM/dd  HH:mm:ss" /></p>
			<p><span>创建时间：</span><fmt:formatDate value="${roleFunction.updDate}" pattern="yyyy/MM/dd  HH:mm:ss" /></p>
		</div>
		
	
		</div>
		<div class="clear"></div>
</body>
</html>