<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>修改渠道</title>
<style type="text/css"> 
.error{ 
color:Red; 
margin-left:10px;  
} 
</style> 
</head>

<body>

	<div class="page-header">
   		<h2>修改渠道</h2>
 	</div>
 	 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/platForm/update"   enctype="multipart/form-data" >
			<input type="hidden" name="id" value="${platForm.id}">
			<div class="control-group">
				<label class="control-label" for="serverZoneId">选择运营大区：</label>
				<div class="controls">
					<select name="serverZoneId" id="serverZoneId">	
					    <option value="">请选择项目</option>	
						<c:forEach items="${serverZones}" var="item" >
								<option value="${item.id }"  ${platForm.serverZoneId == item.id ? 'selected' : '' }>
								${item.serverName }
								</option>
						</c:forEach>
					</select>	
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="pfId">渠道编号：</label>
				<div class="controls">
					<input type="text" name="pfId" class="input-large " value="${platForm.pfId}"/>
				</div>
			</div>	
			<div
				class="control-group">
				<label class="control-label" for="pfName">渠道名称：</label>
				<div class="controls">
					<input type="text" name="pfName" class="input-large " value="${platForm.pfName}" />
				</div>
			</div>	
 			<div class="form-actions">
  			     <button type="submit" class="btn btn-primary" id="submit">保存</button>
				 <a href="<%=request.getContextPath()%>/manage/platForm/index" class="btn btn-primary">返回</a>
	        </div>
	</form>
	<script type="text/javascript">

	$(function(){
		$("#inputForm").validate({
			rules:{
				serverZoneId:{
					required:true
				},
				pfId:{
					required:true
				},
				pfName:{
					required:true
				}
			},messages:{
				serverZoneId:{
					required:"运营大区必须填写"
				},
				pfId:{
					required:"渠道编号必须填写"
				},
				pfName:{
					required:"渠道名称必须填写"
				}
			}
		});
	})

</script> 
</body>
