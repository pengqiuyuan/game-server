<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>导入Excel</title>
</head>
<body>
	<div class="page-header">
   		<h2>导入Excel</h2>
 	</div>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/tag/uploadingExcel" method="post" enctype="multipart/form-data">
		
		 <div class="control-group">
				<label class="control-label" for="gameId">选择项目：</label>
				<div class="controls">
					<select name="gameId" id="gameId">	
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
			<label class="control-label" for="category">Excel类型：</label>
			<div class="controls">
				<select id="category" name="category">	
				        <option value="">请选择类型</option>
						<option value="1">道具Excel</option>
						<option value="2">新手引导Excel</option>
				</select>	
			</div>	
		</div>
		<div class="control-group">
				<label for=fileInput class="control-label">导入Excel文件:</label>
				<div class="controls">
					<input class="input-file" name="fileInput" id="fileInput" type="file">
				</div>
		</div>	
		<div class="form-actions">
				<button class="btn" id="submitBtn">开始导入</button>
		        <a class="btn" href="<%=request.getContextPath()%>/manage/tag/uploadExcel">返回</a>
		</div>
	</form>
	<div>
		<c:if test="${tags != null}">
			<h5>应导入：${fn:length(tags)}条；实际导入：${fn:length(tagsAll)}；实际新增导入：${fn:length(tagsAdd)}；实际更新导入：${fn:length(tagsUpdate)}；导入失败：${fn:length(tagsFails)}</h5>
			<h5>导入最后一条信息为：${lastTag}</h5>
		</c:if>
		<c:if test="${repeatPlacesInExcel != null}">
			<h5 style="color: blue;">*请确认下列${fn:length(repeatPlacesInExcel)}条信息是否已经存在数据库中</h5>
		</c:if>
	</div>
	<%-- <c:if test="${repeatPlacesInExcel == null}">
		<h5 style="color: blue;">*导入成功</h5>
	</c:if> --%>
	<script type="text/javascript">
		
		$(function(){
			$("#inputForm").validate({
				rules:{
					category:{
						required:true
					},
					fileInput:{
						required:true
					}
				},messages:{
					category:{
						required:"必须填写",
					},
					fileInput:{
						required:"必须填写",
					}
				}
			});
			
			$(".compareDB").fancybox({});
			$("#submitBtn").click(function(){
				$("#message").hide();
				var fileValue = $("#fileInput").val();
				if(fileValue == ''){
					  alert('请选择需要导入的excel文档');
					  return false;
				}
				var suffix = fileValue.match(/^(.*)(\.)(.{1,8})$/)[3]; 
				if(suffix != 'xls'){
					alert('文件格式不正确！请使用excel文档');
				  	return false;
				}else{
					if(confirm("确定要导入生成数据吗？")){
						return true;
					}else{
						return false;
					}
				}
			});
			
			
		});
			
	</script> 	
</body>
</html>