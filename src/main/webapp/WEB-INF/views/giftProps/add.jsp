<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>新增道具</title>
<style type="text/css"> 
.error{ 
color:Red; 
margin-left:10px;  
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
			
			<div class="page-header">
				 <span id="addfield" class="btn btn-info">新加道具</span>
			</div>			
			<div id="field">
			</div>
 			<div class="form-actions">
  			     <button type="submit" class="btn btn-primary" id="submit">保存</button>
				 <a href="<%=request.getContextPath()%>/manage/giftProps/index" class="btn btn-primary">返回</a>
	        </div>
	</form>
	<script type="text/javascript">
    var i = 0;
	$("#addfield").click(function(){
		i++;
		if(i<=4){
		    $("#field").prepend( "<div class='control-group'><label class='control-label' for='name'>道具Id：</label><div class='controls'><input type='text' name='fieldValue'  style='height: 30px;' class='input-large' value='' placeholder='道具Id，如:10' /></div></div>" );
		    $("#field").prepend( "<div class='control-group'><label class='control-label' for='name'>道具名称：</label><div class='controls'><input type='text' name='fieldId' style='height: 30px;' class='input-large' value='' placeholder='道具名称，如:金币'/></div></div>" );
		    iFrameHeight();  
		}
	});
	
	$(function(){
		$("#inputForm").validate({
			rules:{
				gameId:{
					required:true
				}
			},messages:{
				gameId:{
					required:"项目必须填写"
				}
			}
		});
	})
</script> 
</body>
