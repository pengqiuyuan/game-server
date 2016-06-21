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
	<title>新增封号</title>
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
	<script type="text/javascript" src="${ctx}/static/resources/date/My97DatePicker/My97DatePicker/WdatePicker.js"></script>
	<div class="page-header">
   		<h2>新增封号</h2>
 	</div>
 	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/gm/kds/seal/save"   enctype="multipart/form-data" >
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
			<div class="control-group">
				<label class="control-label" for="">服务器列表：</label>
				<div class="controls" id="serverDiv"></div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="guid">封号guid：</label>
				<div class="controls">
					<input type="text" id="guid" name="guid" class="input-large"  placeholder="封号guid"/>
				</div>
			</div>
			<div class="control-group" id="selectDate">
				<label class="control-label" for="sealTime">选择封号时间：</label>
				<div class="controls">
					<select name="sealTime" id="sealTime">	
						<option value="">请选择封号时间</option>
						<option value="1800">封禁半小时</option>	
						<option value="43200">封禁12小时</option>	
						<option value="86400">封禁1天</option>	
						<option value="2592000">封禁1个月</option>	
						<option value="31536000">封禁1年</option>	
						<option value="-1">永久封禁</option>		
					</select>	
				</div>
			</div>
			<div id="customDate" style="display: none;">
				<div class="control-group">
					<label class="control-label" for=sealStart>封号开始时间：</label>
					<div class="controls">
						<input type="text" name="sealStart" class="input-large " value="" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="sealStart"  placeholder="封号开始时间"/>
					</div>
				</div>	
				<div class="control-group">
					<label class="control-label" for="sealEnd">封号结束时间：</label>
					<div class="controls">
						<input type="text" name="sealEnd" class="input-large " value="" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="sealEnd" placeholder="封号结束时间"/>
						<div id="time"  class="alert alert-danger" style="display: none;width: 20%; margin-top: 10px;"><button data-dismiss="alert" class="close">×</button>结束时间不能小于开始时间</div>
					</div>
				</div>
			</div>
			<div class="form-ac">
				<button type="button" class="btn btn-success" onclick="customDate();">自定义封号时间</button>
				<button type="button" class="btn btn-info" onclick="cancelCustomDate();">取消自定义封号</button>
			</div>

			<div class="form-actions">
				<shiro:hasAnyRoles name="admin,kds_gm_seal_add">
					<button type="submit" class="btn btn-primary btnvali" id="submit">保存</button>
				</shiro:hasAnyRoles>
				<a href="${ctx}/manage/gm/kds/seal/index" class="btn btn-primary">返回</a>
			</div>
	</form>
<script type="text/javascript">

	function customDate(){  
		$('#customDate').show();
		$("#sealStart").removeAttr("disabled");  
		$("#sealEnd").removeAttr("disabled");  
		$("#sealTime").attr("disabled","disabled");  
	    $("#sealTime").val("");
		$('#selectDate').hide();
	}	
	function cancelCustomDate(){  
		$('#selectDate').show();
		$("#sealTime").removeAttr("disabled");  
		
		$("#sealStart").attr("disabled","disabled");  
		$("#sealEnd").attr("disabled","disabled");  
		$('#sealStart').attr('value','');
		$('#sealEnd').attr('value','');
		$('#customDate').hide();
	
	}
	$(function(){
		$("#serverZoneId").change(function(e){
			var serverZoneId = $("#serverZoneId").val();
			if($("#gameId").val()!=""){
				var gameId = $("#gameId").val();
				$("#serverDiv").empty();
				$.ajax({                                               
					url: '<%=request.getContextPath()%>/manage/gm/kds/serverStatus/findServers?serverZoneId='+serverZoneId+'&gameId='+gameId, 
					type: 'GET',
					contentType: "application/json;charset=UTF-8",		
					dataType: 'text',
					success: function(data){
						var parsedJson = $.parseJSON(data);
						jQuery.each(parsedJson, function(index, itemData) {
						$("#serverDiv").append("<c:forEach items='"+itemData+"' var='ite' varStatus='j'><label class='radio inline'><input type='radio' id='serverId' name='serverId' value='"+itemData.serverId+"'/><span>"+itemData.serverId+"</span><br/></label></c:forEach>"); 
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
					url: '<%=request.getContextPath()%>/manage/gm/kds/serverStatus/findServers?serverZoneId='+serverZoneId+'&gameId='+gameId, 
					type: 'GET',
					contentType: "application/json;charset=UTF-8",		
					dataType: 'text',
					success: function(data){
						var parsedJson = $.parseJSON(data);
						jQuery.each(parsedJson, function(index, itemData) {
						$("#serverDiv").append("<c:forEach items='"+itemData+"' var='ite' varStatus='j'><label class='radio inline'><input type='radio' id='serverId' name='server' value='"+itemData.serverId+"'/><span>"+itemData.serverId+"</span><br/></label>&nbsp;</c:forEach>"); 
						});
					},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
				});
			}	
			
		});
		
		$(".btnvali").click(function(){
			var doingDate=$("#sealStart").val();
	        var endDoingDate=$("#sealEnd").val();
	        var startTime = new Date(doingDate).getTime();
	        var endTime = new Date(endDoingDate).getTime();
	         if(endDoingDate.length!=0){
	        	 if(startTime>endTime){
	             	$("#time").show();
	             	return false;
	        	 }else{
	        		 $("#time").hide();
	        		 return true;
	        	 }
	        }else{
	        	$("#time").hide();
	        	return true;
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
				guid:{
					required:true
				},
				sealTime:{
					required:true
				},
				sealStart:{
					required:true
				},
				sealEnd:{
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
				guid:{
					required:"封号guid必须填写"
				},
				sealTime:{
					required:"封号时间必须填写"
				},
				sealStart:{
					required:"封号开始时间必须填写"
				},
				sealEnd:{
					required:"封号结束时间必须填写"
				}
			}
		});			
		
	})
	

</script> 
</body>
