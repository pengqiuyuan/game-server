<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>修改功能</title>
<style type="text/css"> 
.error{ 
color:Red; 
} 
</style> 
</head>

<body>

	<div class="page-header">
   		<h2>修改功能</h2>
 	</div>
 	 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/enumFunction/update"   enctype="multipart/form-data" >
			<input type="hidden" name="id" value="${enumFunction.id}">
			<div class="control-group">
				<label class="control-label" for="gameId">选择游戏项目：</label>
				<div class="controls">
					<select name="gameId" id="gameId">	
					    <option value="">请选择项目</option>	
					    <option value="0">通用</option>	
						<c:forEach items="${stores}" var="item" >
								<option value="${item.id }"  ${enumFunction.gameId == item.id ? 'selected' : '' }>
								${item.name }
								</option>
						</c:forEach>
					</select>	
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="categoryId">选择平台：</label>
				<div class="controls">
					<select name="categoryId" id="categoryId">	
					    <option value="">请选择平台</option>	
						<c:forEach items="${enumCategories}" var="item" >
								<option value="${item.id }"  ${enumFunction.categoryId == item.id ? 'selected' : '' }>
								${item.categoryName }
								</option>
						</c:forEach>
					</select>	
				</div>
			</div>
			<div
				class="control-group" >
				<label class="control-label" for="enumRole">功能权限：</label>
				<div class="controls">
					<input type="text" name="enumRole" class="input-large "   value="${enumFunction.enumRole}" readonly="readonly"/>
				</div>
			</div>	
			<div
				class="control-group">
				<label class="control-label" for="enumName">功能名称：</label>
				<div class="controls">
					<input type="text" name="enumName" class="input-large "   value="${enumFunction.enumName}"/>
				</div>
			</div>	

			<shiro:hasAnyRoles name="admin">
 			<div class="form-actions">
  			     <button type="submit" class="btn btn-primary" id="submit">保存</button>
				 <a href="<%=request.getContextPath()%>/manage/enumFunction/index" class="btn btn-primary">返回</a>
	        </div>
	        </shiro:hasAnyRoles>
	</form>
	<script type="text/javascript">
	$(function(){
		$("#inputForm").validate({
			rules:{
				gameId:{
					required:true
				},
				categoryId:{
					required:true
				},
				enumName:{
					required:true
				},
				enumRole:{
					required:true,
					remote: '<%=request.getContextPath()%>/manage/enumFunction/checkEnumRole'
				}
			},messages:{
				gameId:{
					required:"游戏项目必须填写"
				},
				categoryId:{
					required:"平台必须填写"
				},
				enumName:{
					required:"功能名称必须填写"
				},
				enumRole:{
					required:"功能权限必须填写",
					remote: "功能权限已存在"
				}
			}
		});
	})
</script> 
</body>
