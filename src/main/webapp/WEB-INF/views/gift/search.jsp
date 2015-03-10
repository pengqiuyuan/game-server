<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="huake" uri="/huake"%>

<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>查询礼品卡</title>
</head>
<body>
	<div >
		<div class="page-header">
			<h2>查询礼品卡</h2>
		</div>
		<div>
		 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
		</c:if>
				<form id="queryForm" class="well form-inline"  method="get" action="${ctx}/manage/gift/search">
					<label>选择项目：</label> 
					<select name="search_LIKE_store">		
						<option value="">选择项目</option>
						<c:forEach items="${stores}" var="item" >
							<option value="${item.id}"  ${param.search_LIKE_store == item.id ? 'selected' : '' }>
								${item.name}
							</option>	
						</c:forEach>
					</select>
					<label>查询方式：</label> 
					<select name="search_LIKE_status" id="search_LIKE_status">	
					    <option value="">选择查询方式</option>	
						<option value="2" ${param.search_LIKE_status == '2' ? 'selected' : '' } >礼品卡Id</option>
						<option value="1" ${param.search_LIKE_status == '1' ? 'selected' : '' } >礼品码</option>
						<option value="0" ${param.search_LIKE_status == '0' ? 'selected' : '' } >GUID</option>
					</select>
					<label>查询名称：</label> <input name="search_LIKE_query" id="search_LIKE_query" type="text" value="${param.search_LIKE_query}" /> 
					<input type="submit" class="btn" value="查 找" />
				<tags:sort />
				</form>
		</div>
		<table class="table table-striped table-bordered table-condensed" id="table">
			<thead>
				<tr>
					<th title="礼品卡Id" width="120px">礼品卡Id</th>
					<th title="礼品码">礼品码</th>
					<th title="GUID">GUID</th>
					<th title="平台Id">平台Id</th>
					<th title="渠道Id">渠道Id</th>
					<th title="使用日期">使用日期</th>
					<th title="生成账号">生成账号</th>
				</tr>
			</thead>
			<tbody id="tbody">
				<c:forEach items="${giftSearchs.content}" var="item" varStatus="s">
					<tr id="${item.giftId}">
						<td id="iDictionary" value="${item.giftId}">
							<div class="btn-group">
								<a class="btn" href="#">#${item.giftId}</a> <a
									class="btn dropdown-toggle" data-toggle="dropdown" href="#">
									<span class="caret"></span>
								</a>
							</div>
						</td>
						<td>${item.giftcode}</td>
						<td>${item.guid}</td>
						<td>${item.platformId}</td>
						<td>${item.channelId}</td>
						<td><huake:getDateTag id="${item.begindate}"></huake:getDateTag></td>
						<td><huake:GetUserNameTag id="${item.userId}"></huake:GetUserNameTag></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		
		<tags:pagination page="${giftSearchs}" paginationSize="5"/>
		
	</div>
		<script type="text/javascript">
		$(document).ready(function(){
	
			$('.showInfo').fancybox({
				autoDimensions:false,
				width:800,
				height:500
			});
			
			$("#search_LIKE_status").change(function(e){
				$("#search_LIKE_query").val("");
			});
			
			$("#search_LIKE_query").change(function(e){
				if($("#search_LIKE_status").val()=="0"){
		 			jQuery.validator.addMethod("stringCheck", function(value, element) {       
						return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);       
					}, "只能包括、英文字母、数字");   
					$("#queryForm").validate({
						rules:{
							search_LIKE_query:{
								required:true,
								stringCheck:true
							}
						},messages:{
							search_LIKE_query:{
								required:"必须填写"
							}
						}
					});	
				}else if($("#search_LIKE_status").val()=="1"){
		 			jQuery.validator.addMethod("stringCheck", function(value, element) {       
						return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);       
					}, "只能包括、英文字母、数字");   
					$("#queryForm").validate({
						rules:{
							search_LIKE_query:{
								required:true,
								stringCheck:true,
								minlength:1,
								maxlength:10
							}
						},messages:{
							search_LIKE_query:{
								required:"必须填写",
								minlength:"游戏名称长度1-10位"
							}
						}
					});	
				}else if($("#search_LIKE_status").val()=="2"){
		 			jQuery.validator.addMethod("stringCheck", function(value, element) {       
						return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);       
					}, "只能包括、英文字母、数字");   
					$("#queryForm").validate({
						rules:{
							search_LIKE_query:{
								required:true,
								stringCheck:true
							}
						},messages:{
							search_LIKE_query:{
								required:"必须填写"
							}
						}
					});	
				}
			});
		
		});	
		</script> 	
</body>
</html>