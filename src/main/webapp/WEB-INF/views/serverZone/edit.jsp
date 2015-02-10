<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>运营大区修改</title>
<style type="text/css"> 
.error{ 
color:Red; 
margin-left:10px;  
} 
</style> 
</head>

<body>

	<div class="page-header">
   		<h2>运营大区修改</h2>
 	</div>
 	 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/serverZone/update"  enctype="multipart/form-data" >
    <input type="hidden" name="id" value="${serverZone.id }">
			
			<div
				class="control-group">
				<label class="control-label" for="id">运营大区Id：</label>
				<div class="controls">
					<input type="text" name="id" class="input-large " value="${serverZone.id}"  readonly="readonly" />
				</div>
			</div>		
			<div
				class="control-group">
				<label class="control-label" for="serverName">运营大区名称：</label>
				<div class="controls">
					<input type="text" name="serverName" class="input-large " value="${serverZone.serverName }"   />
				</div>
			</div>

					
 			<div class="form-actions">
  			     <button type="submit" class="btn btn-primary" id="submit">保存</button>
				 <a href="<%=request.getContextPath()%>/manage/serverZone/index" class="btn btn-primary">返回</a>
	        </div>
	</form>
	<script type="text/javascript">

$(function(){
	$("#inputForm").validate({
		rules:{
			serverName:{
				required:true,
				minlength:1,
				maxlength:12
			}
		},messages:{
			serverName:{
				required:"必须填写",
				minlength:"游戏名称长度1-12位"
			}
		}
	});
})

</script> 
</body>
