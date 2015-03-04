<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>新增礼品卡</title>
<style type="text/css"> 
.error{ 
color:Red; 
margin-left:10px;  
} 
.checkbox.inline+.checkbox.inline {
margin-left: 0px;
}
.radio.inline, .checkbox.inline {
padding-top: 16px;
}
.radio, .checkbox {
min-height: 20px;
}
label {
line-height: 30px;
}
</style> 
</head>

<body>
	<script type="text/javascript" src="${ctx}/static/resources/date/My97DatePicker/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript">
 	 function display(){   
	  	WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'2012-07-27'});
 	 }
	</script>
	
	<div class="page-header">
   		<h2>新增礼品卡</h2>
 	</div>
 	 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/gift/save"   enctype="multipart/form-data" >
			<div class="container-fluid">
				<div class="row-fluid">
					<div class="span2">
					<shiro:hasAnyRoles name="admin">
					  <div class="control-group">
						<label for="gameId">选择游戏项目：</label>
						<div >
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
					</shiro:hasAnyRoles>

		 			 <div class="control-group">
						<label for="serverZoneId">选择运营大区：</label>
						<div >
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
						<label for="">服务器列表：</label>
						<button type="button" class="btn btn-success" onclick="selectAll();">全选</button>
						<button type="button" class="btn btn-info" onclick="selectAllNot();">反选</button>
						<div id="serverDiv">
						</div>
					 </div>
					</div>
					
					<div class="span10">
						 <div
							class="control-group">
							<label class="control-label" for="playerId">限制Id：</label>
							<div class="controls">  
								<input type="text" name="playerId" class="input-large " placeholder="玩家GUID,逗号隔开"/>
								<span class="help-block">关联多个玩家使用空格分割</span>
							</div>
						</div>	
						<div class="control-group">
								<label class="control-label" for="category">礼品卡类型：</label>
								<div class="controls">
									<label class="radio">
									  <input type="radio" name="category" id="category" value="1" checked>
									  <span class="help-block" style="margin-top: -5px;width: 90%;">礼品卡类型1：同批绑定单服单次使用，使用后作废（即生成的礼品码在所选择的的服务器中只能有一个服务器使用，一个玩家在一个服务器中使用后，此卡就作废）</span>
									</label>
									<label class="radio">
									  <input type="radio" name="category" id="category" value="2">
									  <span class="help-block" style="margin-top: -5px;width: 90%;">礼品卡类型2：同批支持多服单次使用，使用后作废（即生成的礼品码在所选择的的服务器均可使用，这张卡在每个服只可使用一次，一经使用，其他玩家无法使用这张卡，使用这个卡的玩家可以再其他服继续使用这张卡）</span>
									</label>
									<label class="radio">
									  <input type="radio" name="category" id="category" value="3">
									  <span class="help-block" style="margin-top: -5px;width: 90%;">礼品卡类型3：同批支持多服单次使用，使用后在本服无法再次使用，可在其他服再次使用（此种类型礼品卡只生成一个礼品码，此礼品码在所选择的的服务器均可使用，但是每个玩家GUID在每个服只能使用一次这个礼品卡）</span>
									</label>
								</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="number">生成数量：</label>
							<div class="controls">
								<input type="text" name="number" class="input-large " value=""/>
								<span class="help-block">类型3礼品卡时，生成数量为1不可选</span>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for=beginD>活动时间：</label>
							<div class="controls">
								<input type="text" name="beginD" class="input-large " value="" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="beginD"  placeholder="礼品卡生效时间"/>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="endD">结束时间：</label>
							<div class="controls">
								<input type="text" name="endD" class="input-large " value="" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="endD" placeholder="礼品卡过期时间"/>
								<div id="time"  class="alert alert-danger" style="display: none;width: 20%; margin-top: 10px;"><button data-dismiss="alert" class="close">×</button>结束时间不能小于开始时间</div>
							</div>
						</div>
						<div id="item">
						</div>
						
						<div class="page-header">
							<span id="addfield" class="btn btn-info">新加道具及数量</span>
						</div>			
						<div id="field">
						</div>
						
					</div>
				</div>
				
			</div>
			<div class="form-actions">
			  	<button type="submit" class="btn btn-primary" id="submit">保存</button>
				<a href="<%=request.getContextPath()%>/manage/serverZone/index" class="btn btn-primary">返回</a>
			</div>
	</form>
<script type="text/javascript">
	$("#addfield").click(function(){
			$("#field").prepend("<div id='field_div'></div>");
		    $("#field_div").prepend( "<div class='control-group'><label class='control-label' for='name'>道具数量：</label><div class='controls'><input type='text' name='fieldValue'  style='height: 20px;' class='input-large' value='' placeholder='道具数量，如:20' /></div></div>" );
		    $("#field_div").prepend( "<div class='control-group'><label class='control-label' for='name'>道具Id：</label><div class='controls'><input type='text' name='fieldId' style='height: 20px;' class='input-large' value='' placeholder='道具Id，如:10'/>&nbsp;<span id='delElememt' class='del btn btn-danger'>删除道具</span></div></div>" );
	
		 	$("#delElememt").click(function(){
			  		$(this).parent().parent().parent().remove();
			}); 
	});
	    
	
	function selectAll(){  
        $("input[id='server']").attr("checked", true);  
	}	
	function selectAllNot(){
    	$("input[id='server']").attr("checked", false);  
	}	

	$(function(){
		$("#serverZoneId").change(function(e){
			var serverZoneId = $("#serverZoneId").val();
			$("#serverDiv").empty();
			$.ajax({                                               
				url: '<%=request.getContextPath()%>/manage/gift/findServers?serverZoneId='+serverZoneId, 
				type: 'GET',
				contentType: "application/json;charset=UTF-8",		
				dataType: 'text',
				success: function(data){
					var parsedJson = $.parseJSON(data);
					jQuery.each(parsedJson, function(index, itemData) {
					$("#serverDiv").append("<c:forEach items='"+itemData+"' var='ite' varStatus='j'><label class='checkbox inline'><input type='checkbox' id='server' name='server' value='"+itemData.serverId+"'/><span>"+itemData.serverId+"</span><br/></label>&nbsp;</c:forEach>"); 
					});
				},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
			});
		});
		
		$("#gameId").change(function(e){
			var gameId = $("#gameId").val();
			$("#item").empty();
			$.ajax({                                               
				url: '<%=request.getContextPath()%>/manage/gift/findGiftProps?gameId='+gameId, 
				type: 'GET',
				contentType: "application/json;charset=UTF-8",		
				dataType: 'text',
				success: function(data){
					var parsedJson = $.parseJSON(data);
					jQuery.each(parsedJson, function(index, itemData) {
					$("#item").append("<c:forEach items='"+itemData+"' var='ite' varStatus='j'><div class='control-group'><label class='control-label' for='name'>"+itemData.itemName+"：</label><div class='controls'><input type='text' name='fieldValue' class='input-large' value='' placeholder='道具数量，如:10'/> <input type='hidden' name='fieldId' class='input-large' value='"+itemData.itemId+"'/></div></div></c:forEach>"); 
					});
				},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
			});
		});
		
		$(".btn").click(function(){
			var doingDate=$("#beginD").val();
	        var endDoingDate=$("#endD").val();
	        var startTime = new Date(doingDate).getTime();
	        var endTime = new Date(endDoingDate).getTime();
	         if(!endDoingDate.length==0){
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
				fieldValue:{
					maxlength:7,
					number:true
				},
				fieldId:{
					number:true
				}
			},messages:{
				fieldValue:{
					maxlength:"长度1-7位",
					number:"必须是数字"
				},
				fieldId:{
					number:"必须是数字"
				}
			}
		});	
		
	})

</script> 
</body>
