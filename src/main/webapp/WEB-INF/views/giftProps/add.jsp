<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>新增道具</title>
	<link href="${ctx}/static/typeahead/examples.css" type="text/css" rel="stylesheet" />
    <script src="${ctx}/static/typeahead/typeahead.js" type="text/javascript"></script>
<style type="text/css"> 
.error{ 
color:Red; 
} 
</style> 
</head>

<body>

	<div class="page-header">
   		<h2>新增道具</h2>
 	</div>
 	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/giftProps/save"   enctype="multipart/form-data" >
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
			<c:if test="${tagsSize != '0' ? 'true':'false' }">
			<div class="page-header">
				 <span id="addfield" class="btn btn-info">新加道具</span>
			</div>		
			</c:if>	
			<div id="field">
			</div>
 			<div class="form-actions">
  			     <button type="submit" class="btn btn-primary" id="submit">保存</button>
				 <a href="<%=request.getContextPath()%>/manage/giftProps/index" class="btn btn-primary">返回</a>
				 <c:if test="${tagsSize == '0' ? 'true':'false' }">
				 <a href="<%=request.getContextPath()%>/manage/tag/uploadExcel" class="btn btn-danger">导入道具Excel</a>
				 </c:if>
	        </div>
	</form>
	<script type="text/javascript">
    var i = 0;
	$("#addfield").click(function(){
		i++;
		if(i<=4){
		    $("#field").prepend( "<div class='control-group'><label class='control-label' for='name'>道具Id及名称：</label><div class='typeahead-wrapper controls'><input type='text' name='fieldValue' id='fieldValue' style='height: 20px;' class='states' value='' placeholder='道具Id及名称，如: 1:金币'/></div></div>" );
		}
		
	    $('.states').typeahead({
	        valueKey: 'tagName',
	        minLength: 1,
	        limit:5,
	        remote: '<%=request.getContextPath()%>/manage/tag/findItemNameAndId?query=%QUERY'
	      });
	    
	});
	
	$(function(){
		jQuery.validator.addMethod("phone", function(value, element) { 
			var tel = /^\d+:/;
			return this.optional(element) || (tel.test(value)); 

			}, "格式错误(如 1:金币)");
		
		$("#inputForm").validate({
			rules:{
				gameId:{
					required:true
				},
				fieldValue:{
					required:true,
					phone:true,
					remote: '<%=request.getContextPath()%>/manage/giftProps/checkTagId'
				}
			},messages:{
				gameId:{
					required:"项目必须填写"
				},
				fieldValue:{
					required:"项目必须填写",
					remote: "道具Excel不存在此道具Id"
				}
			}
		});
	})
</script> 
</body>
