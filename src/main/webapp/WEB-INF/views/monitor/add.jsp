<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>新增监控</title>
</head>

<body>

	<div class="page-header">
   		<h2>新增监控</h2>
 	</div>
 	 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/monitor/save"   enctype="multipart/form-data" >
			<div class="control-group">
				<label class="control-label" for="storeId">选择游戏项目：</label>
				<div class="controls">
					<select name="storeId" id="storeId">	
					    <option value="">请选择项目</option>	
						<c:forEach items="${stores}" var="item" >
								<option value="${item.id }"  >
								${item.name }
								</option>
						</c:forEach>
					</select>	
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="monitorKey">监控字段名称：</label>
				<div class="controls">
					<select name="monitorKey" id="monitorKey">
						<option value="">请选择监控字段</option>
						<option value="增加的道具数量">item_mount</option>
						<option value="日志道具id">item_id</option>
						<option value="增加的充值币数量" >money_mount</option>
						<option value="增加的游戏币数量" >coin_mount</option>
					</select>
				</div>
			</div>	
			<div class="control-group">
				<label class="control-label" for="eql">判断值：</label>
				<div class="controls">
					<select name="eql" id="eql">
						<option value="">请选择</option>
						<option value="gte">大于</option>
						<option value="eql" >等于</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="monitorValue">监控字段值：</label>
				<div class="controls">
					<input type="text" name="monitorValue" class="input-large "/>
				</div>
			</div>		
 			<div class="form-actions">
  			     <button type="submit" class="btn btn-primary" id="submit">保存</button>
				 <a href="<%=request.getContextPath()%>/manage/monitor/index" class="btn btn-primary">返回</a>
	        </div>
	</form>
	<script type="text/javascript">
		$(function(){
			$("#inputForm").validate({
				rules:{
					monitorKey:{
						required:true
					},
					eql:{
						required:true
					},
					monitorValue:{
						required:true
					}
				},messages:{
					monitorKey:{
						required:"监控字段名称必须填写"
					},
					eql:{
						required:"判断值必须填写"
					},
					monitorValue:{
						required:"监控字段值必须填写"
					}
				}
			});
		})
	</script> 
</body>
