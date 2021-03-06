<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="huake" uri="/huake"%>

<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>服务器信息</title>
<style type="text/css">
	p span{width: 100px; display: inline-block; font-weight: bold;}
	#customer{float: left; }
	#type{float: right; width: 50%;}
	.clear{clear: both;}
</style>
</head>
<body>
		<div class="page-header">
			<h2>服务器名称：${server.serverId }</h2>
		</div>
		<div>
			IP：${server.ip }
		</div>
		<div>
			端口:${server.port}
		</div>
		<div>
			游戏项目:${server.stores.name}
		</div>
		<div>
			运营大区:${server.serverZone.serverName}
		</div>
		<div class="clear"></div>
</body>
</html>