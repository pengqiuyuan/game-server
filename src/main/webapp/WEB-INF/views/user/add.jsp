<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>新增操作员</title>
<style type="text/css"> 
.error{ 
color:Red; 
} 
</style> 
</head>

<body>

	<div class="page-header">
   		<h2>新增用户</h2>
 	</div>
 	 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal"
		action="${ctx}/manage/user/save">
		<div class="control-group">
			<label class="control-label" for="name">名称：</label>
			<div class="controls">
				<input type="text" name="name" class="input-large" value="" />
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="loginName">登入名：</label>
			<div class="controls">
				<input id="loginName" type="text" name="loginName" value=""
					class="input-large" />
			</div>
		</div>

		<div class="control-group ">
			<label class="control-label" for="pwdCipher">登录密码：</label>
			<div class="controls">
				<input type="password" id="pwdCipher" name="pwdCipher"
					class="input-large ">
			</div>
		</div>
		<div class="control-group ">
			<label class="control-label">确认密码：</label>
			<div class="controls">
				<input type="password" id="confirmPwdCipher" name="confirmPwdCipher"
					class="input-large" />
			</div>
		</div>

		<div class="control-group ">
			<label class="control-label" for="serverName">服务器大区：</label>
			<div class="controls">
				<c:forEach items="${serverZones}" var="item" varStatus="i">
					<input type="checkbox" name="serverName" value="${item.id}"
						class="box" />
					<span>${item.serverName}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				         <c:if test="${(i.index+1)%5 == 0}">
						<br />
						<br />
					</c:if>
				</c:forEach>
			</div>
		</div>
		<div class="page-header">
			<span id="addfield" class="btn btn-info">新加项目权限组</span>
		</div>
		<div id="field"></div>

		<div class="control-group ">
			<label class="control-label" for="status">操作员状态：</label>
			<div class="controls">
				<select name="status">
					<option value="1">有效</option>
					<option value="2">冻结</option>
				</select>
			</div>
		</div>
		<input type="hidden" id="storeslength" value="${fn:length(stores)}">
		<input type="hidden" name="roles" value="">
		<div class="form-actions">
			<button type="submit" class="btn btn-primary" id="submit">保存</button>
			<a href="<%=request.getContextPath()%>/manage/user/index"
				class="btn btn-primary">返回</a>
		</div>
	</form>
	<script type="text/javascript">
	 var tindex=0;
	$("#addfield").click(function(){
		tindex++;
		if(tindex<=$("#storeslength").val()){
			 $("#field").prepend("<div id='item'></div>");
		     $("#item").prepend( "<div class='control-group'><label for='functions' class='control-label'>功能选项：</label><div class='controls' id='functions'></div></div>" );
		     $("#item").prepend( "<div class='control-group'><label for='role' class='control-label'>权限组：</label><div class='controls' ><select  id='roleCode' name='role'  class='role-select'></select></div></div>" );
		     $("#item").prepend( "<div class='control-group'><label class='control-label' for='storeId'>选择项目：</label><div class='controls'><select name='storeId' id='storeId'><option value=''>请选择项目</option><c:forEach items='${stores}' var='item' ><option value='${item.id }'>${item.name}</option></c:forEach></select>	&nbsp;<span id='delElememt"+tindex+"' class='del btn btn-danger'>删除权限组</span></div></div>" );

	     $("#roleCode").change(function(e){
			var gameId = $(this).parent().parent().prev().children().children("#storeId").val();
			var role = $(this).children('option:selected').val();
			var th = $(this).parent().parent().next().children("#functions");
			th.empty();
			e.preventDefault();
			$.ajax({                                               
				url: '<%=request.getContextPath()%>/manage/user/findFunctions?gameId='+gameId+'&role='+role, 
				type: 'GET',
				contentType: "application/json;charset=UTF-8",		
				dataType: 'text',
				success: function(data){
	 				var parsedJson = $.parseJSON(data);
					 jQuery.each(parsedJson, function(index, itemData) {
						 jQuery.each(itemData.roleAndEnums, function(i, its) {
					     	th.append("<input type='checkbox' onclick='return false' name='functions' value='"+its.enumRole+"' checked='checked' class='box' /><span>"+its.enumRole+"、"+its.enumName+"</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"); 
							 	if((i+1)%7==0){
									th.append("<br/><br/>");
								}
							}); 
					 }); 
				},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
			});
		}); 
	     
 	 	$("#delElememt"+tindex).click(function(){
		  	$(this).parent().parent().parent().remove();
		  	tindex--;
		}); 
		
		$("#storeId").change(function(e){
			var value = $(this).children('option:selected').val();
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
	 }else if(tindex>$("#storeslength").val()){
		 tindex=$("#storeslength").val();
	 }
	}); 
	
$(function(){
	
	//聚焦第一个输入框
	$("#loginName").focus();
	
	$("#inputForm").validate({
		rules:{
			name:{
				required:true,
				minlength:2,
				maxlength:10
			},
			loginName:{
				remote: '<%=request.getContextPath()%>/manage/user/checkLoginName',
				required:true,
				minlength:2,
				maxlength:10
			},pwdCipher:{
				required:true,
				minlength:5,
				maxlength:15
			},confirmPwdCipher:{
				required:true,
				minlength:5,
				maxlength:15,
				equalTo: "#pwdCipher"
			},serverName:{
				required:true
			},storeId:{
				required:true
			},role:{
				required:true
			}
		},messages:{
			name:{
				required:"必须填写",
				minlength:"用户名长度2-10位"
			},
			loginName:{
				remote: "用户登录名已存在",
				required:"必须填写",
				minlength:"登入名长度2-10位"
			},pwdCipher:{
				required:"必须填写",
				minlength:"密码长度5-15位",
				maxlength:"密码长度5-15位"
			},confirmPwdCipher:{
				required:"必须填写",
				minlength:"密码长度5-15位",
				maxlength:"密码长度5-15位",
				equalTo: "两次输入密码不一致，请重新输入"
			},serverName:{
				required:"必须填写"
			},storeId:{
				required:"必须填写"
			},role:{
				required:"必须填写"
			}
		}
	});
	
})

</script> 
</body>
