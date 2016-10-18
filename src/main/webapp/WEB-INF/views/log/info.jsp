<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="huake" uri="/huake"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>日志信息</title>
<style type="text/css">
	p span{width: 100px; display: inline-block; font-weight: bold;}
	#customer{float: left; }
	#type{float: right; width: 50%;}
	.clear{clear: both;}
</style>
</head>
<body>
		<div class="page-header">
			<h2>操作人：${log.crUser }</h2>
		</div>
		<div>
			<div id="log" >
			<div style="float: left; display: inline-block;font-weight: bold;width: 100px;">内容：</div>
			<div style="margin-left: 100px;margin-right:100px;padding:10px;background-color: #F5F5F5;border: 1px solid #E3E3E3;">
			${log.content }
			</div>
		
			<p><span>创建时间：</span><fmt:formatDate value="${log.crDate}" pattern="yyyy/MM/dd  HH:mm:ss" /></p>
			<p><span>日志类型：</span>
				${log.type == "1" ? "用户日志" : log.type == "2" ? "项目日志" : log.type == "gm_event" ? "GM活动日志" : "未知"}			
			</p>
		
		</div>
		
	
		</div>
		<div class="clear"></div>
</body>
</html>