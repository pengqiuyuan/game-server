<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="huake" uri="/huake"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>用户修改</title>
<style type="text/css"> 
.error{ 
	color:Red; 
	margin-left:10px;  
} 
</style> 
</head>

<body>
	<div class="page-header">
   		<h2>用户修改</h2>
 	</div>

	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/user/update" >
	<input type="hidden" id="userId" name="id" value="${user.id}" > 
	  <div
	class="control-group">
	<label class="control-label" for="name">用户名:</label>
	<div class="controls">
		<input type="text" name="name" class="input-large " value="${user.name }"   />
	</div>
</div>

<div
	class="control-group">
	<label class="control-label" for="loginName">登入名:</label>
	<div class="controls">
		<input type="text" name="loginName" value="${user.loginName }"  disabled='disabled' class="input-large"  />
	</div>
</div>

	<div class="control-group ">
			<label class="control-label" for="serverName">服务器大区：</label>
			<div class="controls">	
				<c:forEach items="${serverZones}" var="item" varStatus="i">
							   <input type="checkbox" name="serverName" value="${item}" checked="checked"  class="box" />
					           	<span><huake:getServerZoneNameTag id="${item}"></huake:getServerZoneNameTag></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					         <c:if test="${(i.index+1)%5 == 0}">
							<br/>
							<br/>
							</c:if>
				</c:forEach>
				<c:forEach items="${serverZonesNot}" var="item" varStatus="i">
							   <input type="checkbox" name="serverName" value="${item}" class="box" />
					           	<span><huake:getServerZoneNameTag id="${item}"></huake:getServerZoneNameTag></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					         <c:if test="${(i.index+1)%5 == 0}">
							<br/>
							<br/>
							</c:if>
				</c:forEach>			
			</div>
	</div>

	<div class="page-header">
	  	<span id="addfield" class="btn btn-info">新加项目权限组</span>
	</div>
	<div id="field">
		<c:forEach items="${userRoles}" var="item" varStatus="i">
			<div id="item${item.storeId}">
			<div class="control-group">
				<label class="control-label" for="storename">选择项目：</label>
				<div class="controls">	
					<input id="storename" type="text" name="storename" value="<huake:getStoreNameTag id="${item.storeId}"></huake:getStoreNameTag>"  class="input-large"  readonly="readonly"/>
					<input type="hidden" id="storeId" name="storeId" value="${item.storeId}" >
					<span id="${item.storeId}"  class="delOne btn btn-danger">删除权限组</span> 
					<span id="${item.storeId}"  class="roleCodeO btn btn-info">更新权限组</span> 
				</div>
			</div>
			<div class="control-group">
				<label for="role" class="control-label">权限组：</label>		
					<div class="controls">			
					<select id='roleCode' name="role" class="roleCodeOne">	
					        <option value=''>选择权限组</option>
							<c:forEach items="${item.roleFunctions}" var="it" >
								<option value="${it}" ${it == item.role ? 'selected' : '' }>
									${it}
								</option>
							</c:forEach>
					</select>
					</div>
			</div>
			<div class="control-group">
					    <label for="functions" class="control-label">功能选项：</label>
						<div class="controls" id="functions">
							<c:forEach items="${item.functions}" var="item" varStatus="i">
								   <input onclick='return false' type="checkbox" name="functions" value="${item}" checked="checked" class="box" />
						           	<span>${item}、<huake:getFunctionNameTag id="${item}"></huake:getFunctionNameTag></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						         <c:if test="${(i.index+1)%5 == 0}">
								<br/>
								<br/>
								</c:if>
							</c:forEach>
						</div>
			</div>
		</div>
		</c:forEach>

	</div>


	<div
		class="control-group ">
		<label class="control-label" for="status">操作员状态:</label>
		<div class="controls">
			<select  name="status">
				<option value="1" ${user.status=='1'?'selected' : ''} >有效</option>
				<option value="2" ${user.status=='2'?'selected' : ''} >冻结</option>
			</select>
		</div>
	</div>	
	<input type="hidden" name="roles" value="" >
 			<div class="form-actions">
  			     <button type="submit" class="btn btn-primary" id="submit">保存</button>
				 <a href="<%=request.getContextPath()%>/manage/user/index" class="btn btn-primary">返回</a>
	        </div>
	</form>
	<script type="text/javascript">


	
		$(function(){
			var userId = $("#userId").val();
			var boot = true;
		  	$("#addfield").click(function(){
		  			$.ajax({                                               
		  				url: '<%=request.getContextPath()%>/manage/user/findSt?userId='+$("#userId").val(), 
		  				type: 'GET',
		  				contentType: "application/json;charset=UTF-8",		
		  				dataType: 'text',
		  				success: function(data){
		  	 				var parsedJson = $.parseJSON(data);
		  	 				if(parsedJson.stos!=null && parsedJson.storesLength>0 && boot==true){
		  						$("#field").prepend("<div id='item'></div>");
		  						$("#item").prepend( "<div class='control-group'><label for='functions' class='control-label'>功能选项：</label><div class='controls' id='functions'></div></div>" );
		  						$("#item").prepend( "<div class='control-group'><label for='role' class='control-label'>权限组：</label><div class='controls'><select  id='roleCode' name='role' class='roleCode'><option value=''>请选择权限组</option></select></div></div>" );
		  						$("#item").prepend( "<div class='control-group'><label class='control-label' for='storeId'>选择项目：</label><div class='controls'><select name='storeId' id='storeId'><option value=''>请选择项目</option></select>&nbsp;<span id='delElememt' class='del btn btn-danger'>删除权限组</span></div></div>" );
		  						jQuery.each(parsedJson.stos, function(index, itemData) {
		  							$("#storeId").append("<option value='"+itemData.id+"'>"+itemData.name+"</option>"); 
		  						});
		  						$("#item").prepend();
		  						boot = false;
		  						
		  	  	 		  		$('select[name="storeId"]').each(function(){
		  	  	 		  			if($(this).val()!=0){
		  	  	 					    $("#field").children().children(":first").children().children("#storeId").find("option[value='"+$(this).val()+"']").remove();	
		  	  	 		  			}
		  	  	 				});
		  	  	 				
		  	  	 	  			$("#storeId").change(function(e){
		  	  	 	  				var value = $(this).children('option:selected').val();
		  	  	 	  			    $(this).next().attr('id',value);
		  	  	 	  		 		var th = $(this).parent().parent().next().children().children("#roleCode");
		  	  	 	  		 		$(this).find("option:not(:selected)").remove();
		  	  	 	  		 		th.empty();
		  	  	 	  				e.preventDefault();
		  	  	 	  				$.ajax({
		  	  	 	  					url: '<%=request.getContextPath()%>/manage/user/findRoles?gameId=' + value, 
		  	  	 	  					type: 'GET',
		  	  	 	  					contentType: "application/json;charset=UTF-8",
		  	  	 	  					data: JSON.stringify({name:value}),					
		  	  	 	  					dataType: 'text',
		  	  	 	  					success: function(data){
		  	  	 	  						var parsedJson = $.parseJSON(data);
		  	  	 	  						th.append("<option value=''>"+"选择权限组"+"</option>");
		  	  	 	  						 jQuery.each(parsedJson, function(index, itemData) {
		  	  	 	  						 th.append("<option value='"+itemData+"'>"+itemData+"</option>"); 
		  	  	 	  						 });
		  	  	 	  					},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
		  	  	 	  				});
		  	  	 	  			}); 
		  	  	 	  			
		  		  	 		    $("#roleCode").change(function(e){
		  		  	 			    boot = true;
			  		  	 			if(confirm("该操作会修改权限组。。。。！")){
				  		  				var gameId = $(this).parent().parent().prev().children().children("#storeId").val();
				  		  				var role = $(this).children('option:selected').val();
				  		  		    	var userId = $("#userId").val();
				  		  				var th = $(this).parent().parent().next().children("#functions");
				  		  				th.empty();
				  		  				e.preventDefault();
				  		  				$.ajax({                                               
				  		  					url: '<%=request.getContextPath()%>/manage/user/updateFunctions?gameId='+gameId+'&role='+role+'&userId='+userId, 
				  		  					type: 'GET',
				  		  					contentType: "application/json;charset=UTF-8",		
				  		  					dataType: 'text',
				  		  					success: function(data){
				  		  		 				var parsedJson = $.parseJSON(data);
				  		  						 jQuery.each(parsedJson.roleFunctions, function(index, itemData) {
				  		  							jQuery.each(itemData.roleAndEnums, function(i, its) {
				  		  						 		th.append("<input type='checkbox' onclick='return false' name='functions' value='"+its.enumRole+"' checked='checked' class='box' /><span>"+its.enumRole+"、"+its.enumName+"</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"); 
				  									 	if((i+1)%5==0){
				  											th.append("<br/><br/>");
				  										}
				  		  							}); 
				  		  						 }); 
				 		  		  	 		     window.location.href="<%=request.getContextPath()%>/manage/user/edit?id="+userId;
				  		  					}
				  		  				});
				  		  			}  
		  		  	 		    }); 
		  		  	 		    
			  		  	 	    $("#delElememt").click(function(){
				  		  		    $(this).parent().parent().parent().remove();
				  		  		    boot = true;
				  		  	    }); 		    
		  						
		  	 				}  	 				
		  	 			}
		  			});
			});
		  
		    $(".roleCodeO").click(function(e){
				var gameId = $(this).prev().prev("#storeId").val();
				var role = $(this).parent().parent().next().children().children().children('option:selected').val();
		    	var userId = $("#userId").val();
				if(confirm("该操作会修改权限组。。。。！")){
					var th = $(this).parent().parent().next().next().children("#functions");
					th.empty();
					e.preventDefault();
					$.ajax({                                               
						url: '<%=request.getContextPath()%>/manage/user/updateFunctions?gameId='+gameId+'&role='+role+'&userId='+userId, 
						type: 'GET',
						contentType: "application/json;charset=UTF-8",		
						dataType: 'text',
						success: function(data){
			 				var parsedJson = $.parseJSON(data);
							jQuery.each(parsedJson.roleFunctions, function(index, itemData) {
			  					jQuery.each(itemData.roleAndEnums, function(i, its) {
			  						th.append("<input type='checkbox' onclick='return false' name='functions' value='"+its.enumRole+"' checked='checked' class='box' /><span>"+its.enumRole+"、"+its.enumName+"</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"); 
									if((i+1)%5==0){
										th.append("<br/><br/>");
									}
			  					}); 
			  				}); 
						}
					});
				}  
			}); 
		    
			
		    $(".delOne").click(function(){
		    	if(confirm("该操作会删除该用户此项目的权限组。。。。!")){
			    	var gameId = $(this).attr("id");
					$.ajax({                                               
						url: '<%=request.getContextPath()%>/manage/user/delUserRole?gameId='+gameId + '&userId=' + userId, 
						type: 'DELETE',
						contentType: "application/json;charset=UTF-8",		
						dataType: 'text',
						success: function(data){
							var parsedJson = $.parseJSON(data);
					    	$('#item'+$(this).attr("id")).remove();
					    	window.location.href="<%=request.getContextPath()%>/manage/user/edit?id="+userId;
						}//回调看看是否有出错
					});
		    	}
		    });
			
			$("#inputForm").validate({
				rules:{
					name:{
						required:true
					},
					loginName:{
						required:true
					},
					serverName:{
						required:true
					},
					role:{
						required:true
					},
					storeId:{
						required:true
					}
				},messages:{
					name:{
						required:"必须填写",
					},
					loginName:{
						required:"必须填写",
					},
					serverName:{
						required:"必须填写",
					},
					role:{
						required:"必须填写",
					},
					storeId:{
						required:"必须填写",
					}
				}
			});
		})

</script> 
</body>
