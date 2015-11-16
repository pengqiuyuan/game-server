<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%	
	//设置返回码200，避免浏览器自带的错误页面
	response.setStatus(200);
	//记录日志
	Logger logger = LoggerFactory.getLogger("500.jsp");
	logger.error(exception.getMessage(), exception);
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>光线游戏内容管理平台<sitemesh:title/></title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<script src="${ctx}/static/js/jquery.min.js" type="text/javascript"></script>


<link type="image/x-icon" href="${ctx}/static/images/favicon.ico" rel="shortcut icon">
<link href="${ctx}/static/bootstrap/2.3.2/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/jquery-validation/1.11.1/validate.css" type="text/css" rel="stylesheet" />

 <link href="${ctx}/static/styles/default.css" type="text/css" rel="stylesheet" />
<script src="${ctx}/static/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctx}/static/jquery-validation/1.11.1/messages_bs_zh.js" type="text/javascript"></script>

	<script type="text/javascript" src="${ctx}/static/fancyBox-2.1.5/lib/jquery.mousewheel-3.0.6.pack.js"></script>
	<script type="text/javascript" src="${ctx}/static/fancyBox-2.1.5/source/jquery.fancybox.js?v=2.1.5"></script>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/fancyBox-2.1.5/source/jquery.fancybox.css?v=2.1.5" media="screen" />
	<link rel="stylesheet" type="text/css" href="${ctx}/static/fancyBox-2.1.5/source/helpers/jquery.fancybox-buttons.css?v=1.0.5" />
	<script type="text/javascript" src="${ctx}/static/fancyBox-2.1.5/source/helpers/jquery.fancybox-buttons.js?v=1.0.5"></script>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/fancyBox-2.1.5/source/helpers/jquery.fancybox-thumbs.css?v=1.0.7" />
	<script type="text/javascript" src="${ctx}/static/fancyBox-2.1.5/source/helpers/jquery.fancybox-thumbs.js?v=1.0.7"></script>
	<script type="text/javascript" src="${ctx}/static/fancyBox-2.1.5/source/helpers/jquery.fancybox-media.js?v=1.0.6"></script>

<style type="text/css">
	.head_content{margin-left:9%;}
	.member_bar{margin-right:-25%;}
	.div_footer{margin-bottom:20px;text-align:center;background:#E6E6E6;}

</style>
</head>
<body>
	
	<%@ include file="/WEB-INF/layouts/nav.jsp"%>
		<div style="height: 300px">
            <font style="font-size: 14px">&nbsp;&nbsp;&nbsp;系统错误！</font>
        </div>
	<%@ include file="/WEB-INF/layouts/footer.jsp"%>
	<script src="${ctx}/static/bootstrap/3.1.1/js/bootstrap.min.js" type="text/javascript"></script>
	
</body>
</html>

