<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>新增公告</title>
	<style type="text/css">
		.form-ac {
		  padding: 19px 20px 20px;
		  margin-top: 20px;
		  margin-bottom: 20px;
		  padding-left: 180px;
		}
	</style>
</head>

<body>
	<script type="text/javascript" src="${ctx}/static/ckeditor/ckeditor.js"></script>
	<div class="page-header">
   		<h2>新增公告</h2>
 	</div>
 	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/gm/fb/placard/save"   enctype="multipart/form-data" >
			<div class="control-group">
				<label class="control-label" for="gameId">选择游戏项目：</label>
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
				<label class="control-label" for="serverZoneId">选择运营大区：</label>
				<div class="controls">
					<select name="serverZoneId" id="serverZoneId">	
						<option value="">请选择项目</option>	
						<c:forEach items="${serverZones}" var="item" >
							<option value="${item.id }"  >
								${item.serverName }
							</option>
						</c:forEach>
					</select>	
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="">服务器列表：</label>
				<div class="controls" id="serverDiv"></div>
			</div>
			<div class="form-ac">
				<button type="button" class="btn btn-success" onclick="selectAll();">全选</button>
				<button type="button" class="btn btn-info" onclick="selectAllNot();">反选</button>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="version">版本号：</label>
				<div class="controls">
					<input type="text" name="version" class="input-large "  placeholder="版本号"/>
				</div>
			</div>	
			<div class="control-group ">
				<label class="control-label" for="contents">公告内容：</label>
				<div class="controls">
					<textarea path="contents" name="contents" cssClass="input-xlarge" value="" cols="200" rows="20" /></textarea>
				</div>
			</div>
			<shiro:hasAnyRoles name="admin">
				<div class="form-actions">
				  	<button type="submit" class="btn btn-primary" id="submit">保存</button>
				  	<a href="${ctx}/manage/gm/fb/placard/index" class="btn btn-primary">返回</a>
				</div>
			</shiro:hasAnyRoles>
	</form>
<script type="text/javascript">
	CKEDITOR.replace('contents');
	function selectAll(){  
        $("input[id='serverId']").attr("checked", true);  
	}	
	function selectAllNot(){
    	$("input[id='serverId']").attr("checked", false);  
	}	
	$(function(){
		$("#serverZoneId").change(function(e){
			var serverZoneId = $("#serverZoneId").val();
			if($("#gameId").val()!=""){
				var gameId = $("#gameId").val();
				$("#serverDiv").empty();
				$.ajax({                                               
					url: '<%=request.getContextPath()%>/manage/gift/findServers?serverZoneId='+serverZoneId+'&gameId='+gameId, 
					type: 'GET',
					contentType: "application/json;charset=UTF-8",		
					dataType: 'text',
					success: function(data){
						var parsedJson = $.parseJSON(data);
						jQuery.each(parsedJson, function(index, itemData) {
						$("#serverDiv").append("<c:forEach items='"+itemData+"' var='ite' varStatus='j'><label class='checkbox inline'><input type='checkbox' id='serverId' name='serverId' value='"+itemData.serverId+"'/><span>"+itemData.serverId+"</span><br/></label></c:forEach>"); 
						});
					},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
				});
			}
		});
		
		$("#gameId").change(function(e){
			var gameId = $("#gameId").val();
			if($("#gameId").val()!=""){
				$("#submit").removeAttr("disabled");
				$("#item").empty();
				$("#field").empty();				
			}else{
				$("#message").remove();
				$("#addfield").removeAttr("disabled");	
			}

			if($("#serverZoneId").val()!="" && $("#gameId").val()!=""){
				var serverZoneId = $("#serverZoneId").val();
				$("#serverDiv").empty();
				$.ajax({                                               
					url: '<%=request.getContextPath()%>/manage/gift/findServers?serverZoneId='+serverZoneId+'&gameId='+gameId, 
					type: 'GET',
					contentType: "application/json;charset=UTF-8",		
					dataType: 'text',
					success: function(data){
						var parsedJson = $.parseJSON(data);
						jQuery.each(parsedJson, function(index, itemData) {
						$("#serverDiv").append("<c:forEach items='"+itemData+"' var='ite' varStatus='j'><label class='checkbox inline'><input type='checkbox' id='serverId' name='server' value='"+itemData.serverId+"'/><span>"+itemData.serverId+"</span><br/></label>&nbsp;</c:forEach>"); 
						});
					},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
				});
			}	
			
		});
		
		$("#inputForm").validate({
			rules:{
				gameId:{
					required:true
				},
				serverZoneId:{
					required:true
				},
				serverId:{
					required:true
				},
				version:{
					required:true
				},
				contents:{
					required:true
				}
			},messages:{
				gameId:{
					required:"游戏项目"
				},
				serverZoneId:{
					required:"运营必须填写"
				},
				serverId:{
					required:"服务器必须填写"
				},
				version:{
					required:"版本必须填写"
				},
				contents:{
					required:"内容必须填写"
				}
			}
		});			
		
	})
	

</script> 
</body>
