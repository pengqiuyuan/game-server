<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>修改游戏功能权限分配</title>
<style type="text/css"> 
.error{ 
color:Red; 
margin-left:10px;  
} 
</style> 
</head>

<body>
	<div class="page-header">
   		<h2>修改游戏功能权限分配</h2>
 	</div>
 	 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/roleFunction/update">
	    <input type="hidden" name="id" value="${roleFunction.id}" > 
	    <input type="hidden" name="gameId" value="${roleFunction.gameId}" > 
		<div class="control-group">
			<label class="control-label" for="gameId">项目名称</label>
			<div class="controls">
<%-- 				<select name="gameId">		
					<c:forEach items="${stores}" var="item" >
						<option value="${item.id}"  ${roleFunction.gameId == item.id ? "selected":"" } readonly="readonly">
							${item.name }
						</option>
					</c:forEach>
				</select>	 --%>
				<input type="text" name="gameName" class="input-large " value="${roleFunction.gameName}"   readonly="readonly"/>
			</div>
		</div>
		<div
			class="control-group">
			<label class="control-label" for="role">权限名称：</label>
			<div class="controls">
				<input type="text" name="role" class="input-large " value="${roleFunction.role}"  placeholder="建议最高权限从0开始, 低级权限索引号逐渐加1" readonly="readonly"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="functions">功能选择：</label>
			<div class="controls">
					<c:forEach items="${enumFusHas}" var="item" varStatus="i">
						   <input type="checkbox" name="functions" value="${item.enumRole}" checked="checked" class="box" />
				           	<span>${item.enumRole}、${item.enumName}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				         <c:if test="${(i.index+1)%5 == 0}">
						<br/>
						<br/>
						</c:if>
					</c:forEach>
					<br />
					<br/>
				    <c:forEach items="${enumFusNohas}" var="item" varStatus="i">
						   <input type="checkbox" name="functions" value="${item.enumRole}" ${roleFunction.function == item.enumRole ? "checked":"" } class="box" />
				           	<span>${item.enumRole}、${item.enumName}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				         <c:if test="${(i.index+1)%5 == 0}">
						<br/>
						<br/>
						</c:if>
					</c:forEach>
			</div>
		</div>
<%-- 		<div class="control-group">
				<label for="tag_status" class="control-label">状态:</label>
				<div class="controls">
					<select path="status" name="status">
						<option value="1"  ${roleFunction.status == "1" ? "selected":"" }>有效</option>
						<option value="0"  ${roleFunction.status == "0" ? "selected":"" }>无效</option>
					</select>
				</div>
	   </div> --%>
	   <div class="form-actions">
  			<button type="submit" class="btn btn-primary" id="submit">保存</button>
			<a href="<%=request.getContextPath()%>/manage/roleFunction/index?search_EQ_gameId=${roleFunction.gameId}" class="btn btn-primary">返回</a>
			<a href="#" class="btn btn-danger">删除当前权限</a>
	   </div>
	</form>
	<script type="text/javascript">

$(function(){
	
	jQuery.validator.addMethod("rules", function(value, element) { 

		var tel = /^([0-9])*$/; 

		return this.optional(element) || (tel.test(value)); 

		}, "数字格式错误");
	
	$("#inputForm").validate({
		rules:{
			gameName:{
				required:true
			},
			role:{
				rules:true,
				required:true
			},
			functions:{
	     		required:true
			}
		},messages:{
			gameName:{
				required:"必须填写",
			},
			role:{
				required:"必须填写",
			},
			functions:{
				required:"至少选择一个功能",
			}
		}
	});
})


</script> 
</body>
