<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="huake" uri="/huake"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>新增邮件</title>
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
	<div class="page-header">
   		<h2>新增邮件</h2>
 	</div>
 	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/gm/xyj/email/save"   enctype="multipart/form-data" >
			<div class="control-group">
				<label class="control-label" for="gameId">选择游戏项目：</label>
				<div class="controls">
						<select name="gameId" id="gameId">	
							<option value="">请选择项目</option>	
							<c:forEach items="${stores}" var="item" >
								<option value="${item.storeId }"  >
									<huake:getGoStoreNameTag id="${item.storeId }"></huake:getGoStoreNameTag>
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
							<option value="${item.serverZoneId }"  >
								<huake:getGoServerZoneNameTag id="${item.serverZoneId }"></huake:getGoServerZoneNameTag>
							</option>
						</c:forEach>
					</select>	
				</div>
			</div>
			<div class="control-group" id="sDiv">
				<label class="control-label" for="">服务器列表：</label>
				<div class="controls" id="serverDiv"></div>
			</div>
			<div class="control-group" id="pDiv" style="display: none;">
				<label class="control-label" for="">渠道列表：</label>
				<div class="controls" id="platFormDiv"></div>
			</div>
			<div class="form-ac">
				<button type="button" class="btn btn-primary" onclick="selectCheck();">切换列表</button>
				<button type="button" class="btn btn-success" onclick="selectAll();">全选</button>
				<button type="button" class="btn btn-info" onclick="selectAllNot();">反选</button>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="sender">发件人：</label>
				<div class="controls">
					<input type="text" name="sender" class="input-large "  placeholder="发件人"/>
				</div>
			</div>	
			<div class="control-group">
				<label class="control-label" for="receiver">收件玩家Guid：</label>
					<div class="controls">  
						<input type="text" name="receiver" class="input-large " placeholder="玩家Guid,英文逗号隔开"/>
						<span class="help-block">多个收件玩家使用英文逗号分割（不填默认为群发！）</span>
					</div>
			</div>	
			<div
				class="control-group">
				<label class="control-label" for="title">标题：</label>
				<div class="controls">
					<input type="text" name="title" class="input-large "  placeholder="标题"/>
				</div>
			</div>	
			<div class="control-group ">
				<label class="control-label" for="contents">公告内容：</label>
				<div class="controls">
					<textarea path="contents" name="contents" cssClass="input-xlarge" value="" style="height: 200px;width: 800px" /></textarea>
				</div>
			</div>
			
			<div class="page-header" id="addmess">
				<span id="addfield" class="btn btn-info">新加道具及数量</span>
			</div>			
			<div id="field">
			</div>
			<div class="form-actions">
				<shiro:hasAnyRoles name="admin,xyj_gm_email_add">
					<button type="submit" class="btn btn-primary" id="submit">保存</button>
				</shiro:hasAnyRoles>
				<!-- 
				<a href="${ctx}/manage/gm/xyj/email/index" class="btn btn-primary">返回</a>
				 -->
				<a href="${ctx}/manage/index" class="btn btn-primary">返回</a>
			</div>

	</form>
<script type="text/javascript">
	$("#addfield").click(function(){
		var gameId = $("#gameId").val();
		if(gameId!=""){
			$("#message").remove();
			$("#field").prepend("<div id='field_div'></div>");
		    $("#field_div").prepend( "<div class='control-group'><label class='control-label' for='name'>道具数量：</label><div class='controls'><input type='text' name='itemNum' id='itemNum'  style='height: 20px;' class='input-large tt-query' value='' placeholder='道具数量，如:20' /></div></div>" );
		    $("#field_div").prepend( "<div class='control-group'><label class='control-label' for='name'>道具Id：</label><div class='typeahead-wrapper controls'><input type='text' name='itemId' id='itemId' style='height: 20px;' class='states' value='' placeholder='道具Id，如:10:金币'/>&nbsp;<span id='delElememt' class='del btn btn-danger' style='margin-bottom: -6px;'>删除道具</span></div></div>" );
		 	$("#delElememt").click(function(){
			  		$(this).parent().parent().parent().remove();
			}); 
		}else{
			$("#message").remove();
			$("#inputForm").prev().prepend("<div id='message' class='alert alert-success'><button data-dismiss='alert' class='close'>×</button>请选择游戏项目</div>")
		}
	});

	function selectCheck(){  
		var temp = $("#sDiv").is(":hidden"); //是否隐藏
		if(temp == true){
			$('#sDiv').show();
			$('#pDiv').hide();
			$("input[id='platForm']").attr("checked", false);  
		}else{
			$('#pDiv').show();
			$('#sDiv').hide();
			$("input[id='serverId']").attr("checked", false);  
		}
	}
	function selectAll(){  
		var temp = $("#sDiv").is(":hidden"); //是否隐藏
		if(temp == true){
			$("input[id='platForm']").attr("checked", true);  
		}else{
			$("input[id='serverId']").attr("checked", true);  
		}
       
	}	
	function selectAllNot(){
		var temp = $("#sDiv").is(":hidden"); //是否隐藏
		if(temp == true){
			$("input[id='platForm']").attr("checked", false);  
		}else{
			$("input[id='serverId']").attr("checked", false);  
		}
	}	
	$(function(){
		$("#serverZoneId").change(function(e){
			var serverZoneId = $("#serverZoneId").val();
			if($("#gameId").val()!=""){
				var gameId = $("#gameId").val();
				$("#serverDiv").empty();
				$.ajax({                                               
					url: '<%=request.getContextPath()%>/manage/gm/xyj/serverStatus/findServers?serverZoneId='+serverZoneId+'&gameId='+gameId, 
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
				
				$("#platFormDiv").empty();
				$.ajax({                                               
					url: '<%=request.getContextPath()%>/manage/gm/xyj/serverStatus/findPlatForms?serverZoneId='+serverZoneId+'&gameId='+gameId, 
					type: 'GET',
					contentType: "application/json;charset=UTF-8",		
					dataType: 'text',
					success: function(data){
						var parsedJson = $.parseJSON(data);
						jQuery.each(parsedJson, function(index, itemData) {
						$("#platFormDiv").append("<c:forEach items='"+index+"' var='ite' varStatus='j'><label class='checkbox inline' ><input type='checkbox' id='platForm' name='platForm' value='"+index+"'/><span>"+itemData+"</span><br/></label></c:forEach>"); 
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
					url: '<%=request.getContextPath()%>/manage/gm/xyj/serverStatus/findServers?serverZoneId='+serverZoneId+'&gameId='+gameId, 
					type: 'GET',
					contentType: "application/json;charset=UTF-8",		
					dataType: 'text',
					success: function(data){
						var parsedJson = $.parseJSON(data);
						jQuery.each(parsedJson, function(index, itemData) {
						$("#serverDiv").append("<c:forEach items='"+itemData+"' var='ite' varStatus='j'><label class='checkbox inline'><input type='checkbox' id='serverId' name='serverId' value='"+itemData.serverId+"'/><span>"+itemData.serverId+"</span><br/></label>&nbsp;</c:forEach>"); 
						});
					},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
				});
				
				$("#platFormDiv").empty();
				$.ajax({                                               
					url: '<%=request.getContextPath()%>/manage/gm/xyj/serverStatus/findServers?serverZoneId='+serverZoneId+'&gameId='+gameId, 
					type: 'GET',
					contentType: "application/json;charset=UTF-8",		
					dataType: 'text',
					success: function(data){
						var parsedJson = $.parseJSON(data);	
						jQuery.each(parsedJson, function(index, itemData) {
						$("#platFormDiv").append("<c:forEach items='"+index+"' var='ite' varStatus='j'><label class='checkbox inline' ><input type='checkbox' id='platForm' name='platForm' value='"+index+"'/><span>"+itemData+"</span><br/></label>&nbsp;</c:forEach>"); 
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
				platForm:{
					required:true
				},
				sender:{
					required:true
				},
				title:{
					required:true
				},
				contents:{
					required:true
				},
				itemId:{
					required:true,
					number:true
				},
				itemNum:{
					required:true,
					number:true
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
				platForm:{
					required:"渠道必须填写"
				},
				sender:{
					required:"发送人必须填写"
				},
				title:{
					required:"标题必须填写"
				},
				contents:{
					required:"内容必须填写"
				},
				itemId:{
					required:"道具Id必须填写",
					number: "请输入合法的数字"
				},
				itemNum:{
					required:"道具数量必须填写",
					number: "请输入合法的数字"
				}
			}
		});			
		
	})
	

</script> 
</body>
