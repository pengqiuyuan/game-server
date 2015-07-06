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
			<div class="page-header">
				 <span id="addfield" class="btn btn-info">新加道具</span>
			</div>		
			<div id="field">
			</div>
 			<div class="form-actions">
  			     <button type="submit" class="btn btn-primary" id="submit" >保存</button>
				 <a href="<%=request.getContextPath()%>/manage/giftProps/index" class="btn btn-primary">返回</a>
				 <a href="<%=request.getContextPath()%>/manage/tag/uploadExcel" class="btn btn-danger" id="btnBack" style="display: none">导入道具Excel</a>
	        </div>
	</form>
	<script type="text/javascript">
	
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
				phone:true
			}
		},messages:{
			gameId:{
				required:"项目必须填写"
			},
			fieldValue:{
				required:"项目必须填写"
			}
		}
	});
	
    var inum = 0;
	$("#addfield").click(function(){
		var gameId = $("#gameId").val();
		if(gameId!=""){

				$("#message").remove();
				inum++;
				if(inum<=4){
				    $("#field").prepend( "<div class='control-group' id='fieldValueDiv"+inum+"'><label class='control-label' for='name'>道具Id及名称：</label><div class='typeahead-wrapper controls'><input type='text' name='fieldValue' id='fieldValue"+inum+"' style='height: 20px;' class='states' value='' placeholder='道具Id及名称，如: 1:金币'/></div></div>" );
				}
			    $('.states').typeahead({
			        valueKey: 'tagName',
			        minLength: 1,
			        limit:10,
			        remote: '<%=request.getContextPath()%>/manage/tag/findItemNameAndId?query=%QUERY&gameId='+gameId
			    });
			    
				$('#fieldValue'+inum).blur(function(){
					var fieldValue = $('#fieldValue'+inum).val();
					var tel = /^\d+:/;
					
					$('#checkvalue'+inum).remove();
					if(tel.test(fieldValue)){
	 					$.ajax({                                               
							url: '<%=request.getContextPath()%>/manage/giftProps/checkTagId?gameId='+gameId+'&fieldValue='+fieldValue,
							type: 'GET',
							contentType: "application/json;charset=UTF-8",		
							dataType: 'text',
							success: function(data){
								console.log(data);
								if(data=="true"){
									$('#submit').removeAttr("disabled");
								}else if(data=="false"){
									$('#fieldValue'+inum).after("<span id='checkvalue"+inum+"' class='error'>道具Excel不存在此道具</span>");
									$('#submit').attr("disabled","disabled");
								}
							},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
						});
					}
				});
		
		}else{
			$("#message").remove();
			$("#inputForm").prev().prepend("<div id='message' class='alert alert-success'><button data-dismiss='alert' class='close'>×</button>请选择游戏项目</div>")
		}   
		
	});
	
	$("#gameId").change(function(){
		$("#field").empty();
		var gameId = $("#gameId").val();
		if($("#gameId").val()!=""){
			$("#submit").removeAttr("disabled");
			
			$.ajax({                                               
				url: '<%=request.getContextPath()%>/manage/giftProps/checkTagByGameId?gameId='+gameId, 
				type: 'GET',
				contentType: "application/json;charset=UTF-8",		
				dataType: 'text',
				success: function(data){
					if(data != "0"){
						$("#message").remove();
						$("#btnBack").hide();
						$("#addfield").removeAttr("disabled");					
					}else if(data=="0"){
						$("#message").remove();
						$("#btnBack").show();
						$("#addfield").attr("disabled","disabled");
						$("#submit").attr("disabled","disabled");
						$("#inputForm").prev().prepend("<div id='message' class='alert alert-success'><button data-dismiss='alert' class='close'>×</button>没有道具，请先导入此项目的道具Excel.</div>")
					}
				},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
			});
		}else{
			$("#message").remove();
			$("#btnBack").hide();
			$("#addfield").removeAttr("disabled");	
			$("#submit").removeAttr("disabled");	
		}
		
		


	});
	
	
	
</script> 
</body>
