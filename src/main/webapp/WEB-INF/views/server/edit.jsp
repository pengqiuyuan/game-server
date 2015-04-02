<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>修改服务器</title>
<style type="text/css"> 
.error{ 
color:Red; 
} 
</style> 
</head>

<body>

	<div class="page-header">
   		<h2>修改服务器</h2>
 	</div>
 	 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/server/update"   enctype="multipart/form-data" >
			<input type="hidden" name="id" value="${st.id}">
			<div class="control-group">
				<label class="control-label" for="storeId">选择游戏项目：</label>
				<div class="controls">
					<select name="storeId" id="storeId">	
					    <option value="">请选择项目</option>	
						<c:forEach items="${stores}" var="item" >
								<option value="${item.id }"  ${st.storeId == item.id ? 'selected' : '' }>
								${item.name }
								</option>
						</c:forEach>
					</select>	
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="serverZoneId">选择运营大区：</label>
				<div class="controls">
					<select name="serverZoneId" id="serverZoneId">	
					    <option value="">请选择项目</option>	
						<c:forEach items="${serverZones}" var="item" >
								<option value="${item.id }"  ${st.serverZoneId == item.id ? 'selected' : '' }>
								${item.serverName }
								</option>
						</c:forEach>
					</select>	
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="serverId">服务器ID：</label>
				<div class="controls">
					<input type="text" name="serverId" class="input-large " value="${st.serverId}" readonly="readonly"/>
				</div>
			</div>	
			<div
				class="control-group">
				<label class="control-label" for="ip">服务器IP：</label>
				<div class="controls">
					<input type="text" name="ip" class="input-large " value="${st.ip}" placeholder="如：10.0.10.5"/>
				</div>
			</div>	
			<div
				class="control-group">
				<label class="control-label" for="port">服务器端口：</label>
				<div class="controls">
					<input type="text" name="port" class="input-large " value="${st.port}" />
				</div>
			</div>	
 			<div class="form-actions">
  			     <button type="submit" class="btn btn-primary" id="submit">保存</button>
				 <a href="<%=request.getContextPath()%>/manage/server/index" class="btn btn-primary">返回</a>
	        </div>
	</form>
	<script type="text/javascript">

	$(function(){
		jQuery.validator.addMethod("ip", function(value, element) { 
			var tel = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/; 
			return this.optional(element) || (tel.test(value)); 
		}, "IP格式错误");
		$("#inputForm").validate({
			rules:{
				storeId:{
					required:true
				},
				serverZoneId:{
					required:true
				},
				ip:{
					ip:true
				},
				port:{
					required:true,
					number:true
				}
			},messages:{
				storeId:{
					required:"游戏项目必须填写"
				},
				serverZoneId:{
					required:"运营大区必须填写"
				},
				ip:{
					required:"IP必须填写"
				},
				port:{
					required:"端口必须填写",
					number: "请输入合法的数字"
				}
			}
		});
	})

</script> 
</body>
