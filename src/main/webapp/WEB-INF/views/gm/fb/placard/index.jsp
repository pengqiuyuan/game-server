<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="huake" uri="/huake"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>公告</title>
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

	<div >
		<div class="page-header">
			<h2>公告</h2>
		</div>
		<div>
			<c:if test="${not empty message}">
				<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
			</c:if>
			<form id="queryForm" class="form-horizontal"  method="get" action="${ctx}/manage/gm/fb/placard/index">
				<div class="control-group">
					<label class="control-label" for="gameId">选择游戏项目：</label>
					<div class="controls">
							<select name="search_EQ_storeId" id="gameId">	
								<option value="">请选择项目</option>	
								<c:forEach items="${stores}" var="item" >
									<option value="${item.storeId }"   ${param.search_EQ_storeId == item.storeId ? 'selected' : '' }>
										<huake:getGoStoreNameTag id="${item.storeId }"></huake:getGoStoreNameTag>
									</option>
								</c:forEach>
							</select>	
					</div>
				</div>		
				<div class="control-group">
					<label class="control-label" for="serverZoneId">选择运营大区：</label>
					<div class="controls">
						<select name="search_EQ_serverZoneId" id="serverZoneId">	
							<option value="">请选择项目</option>	
							<c:forEach items="${serverZones}" var="item" >
								<option value="${item.serverZoneId }"  ${param.search_EQ_serverZoneId == item.serverZoneId ? 'selected' : '' }>
									<huake:getGoServerZoneNameTag id="${item.serverZoneId }"></huake:getGoServerZoneNameTag>
								</option>
							</c:forEach>
						</select>	
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="">服务器列表：</label>
					<div class="controls" id="serverDiv">
						<c:forEach items="${servers}" var="item" >
							<label class="radio inline">
								<input type="radio" id="serverId"  name="search_EQ_serverId" value="${item.serverId }" ${param.search_EQ_serverId == item.serverId? 'checked' : '' }/><span>${item.serverId}</span><br/>
							</label>
						</c:forEach>
					</div>
				</div>
				<shiro:hasAnyRoles name="admin,fb_gm_placard_select">
					<div class="form-ac">
						<input type="submit" class="btn btn-primary" id="selBtn" value="查 找" />
					</div>
				</shiro:hasAnyRoles>
			</form>
		</div>
		<div class="control-group">
				<table class="table table-striped table-bordered table-condensed" id="table">
					<thead>
						<tr>
		                    <th title="游戏ID">游戏ID</th>
							<th title="运营大区ID">运营大区ID</th>
							<th title="服务器ID">服务器ID</th>
							<th title="版本">版本</th>
							<th title="内容">内容</th>
							<th title="操作">操作</th>
						</tr>
					</thead>
					<tbody id="tbody">
						<c:forEach items="${placard.content}" var="item" varStatus="s">
							<tr id="${item.id}">
								<td>${item.gameId}</td>
								<td>${item.serverZoneId}</td>
								<td>${item.serverId}</td>
								<td>${item.version}</td>
								<td><a href="#"  class="intro" title="${item.contents}" >
								<c:choose> 
			    					<c:when test="${fn:length(item.contents)>3 }"> 
			     						<c:out value="${fn:substring(item.contents,0,3) }..." /> 
			    					</c:when> 
			    					<c:otherwise> 
			     						<c:out value="${item.contents }" /> 
			    					</c:otherwise> 
			   					</c:choose>
			   					</a>
			   					</td>
			   					<td>
									<div class="action-buttons">
										<shiro:hasAnyRoles name="admin,fb_gm_placard_update">
											<a class="exportCode btn table-actions" onclick="updatePlacard('${item.version}','${item.contents}','${item.id}')"><i class="icon-ok"></i>修改</a>
										</shiro:hasAnyRoles>
										<shiro:hasAnyRoles name="admin,fb_gm_placard_delete">
									    	<a class="exportCode btn table-actions" onclick="delPlacard('${item.id}','${item.gameId}','${item.serverZoneId}','${item.serverId}')"><i class="icon-remove"></i>删除</a>
										</shiro:hasAnyRoles>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<tags:pagination page="${placard}" paginationSize="5"/>
				<div class="form-actions">
					<shiro:hasAnyRoles name="admin,fb_gm_placard_add">
					  	<a href="${ctx}/manage/gm/fb/placard/add" class="btn btn-primary">新增公告</a>
					</shiro:hasAnyRoles>
				</div>
		</div>
		


		<form id="inputForm" method="post" Class="form-horizontal" action="<%=request.getContextPath()%>/manage/gm/fb/placard/update"   enctype="multipart/form-data"  style="display: none;">
			<div style="color:#3352CC;clear:both">
			<br><hr style="background-color:#808080;height:1px;width:800px;margin:auto"><h4>修改公告：</h4></div>
			
			<input type="hidden" name="id" id="id">
			<div class="control-group">
				<label class="control-label" for="">游戏:</label>
				<div class="controls">
					<c:if test="${not empty param.search_EQ_serverZoneId}">
						<input type="text" value='<huake:getStoreNameTag id="${param.search_EQ_storeId}"></huake:getStoreNameTag>' disabled="disabled"/>
						<input type="text" value='${param.search_EQ_storeId}'  name="search_EQ_storeId" style="display: none;"/>
					</c:if>
					<c:if test="${empty param.search_EQ_serverZoneId}">
						<input type="text"  readonly="readonly" name="search_EQ_storeId"/>
					</c:if>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="">运营大区:</label>
				<div class="controls">
					<c:if test="${not empty param.search_EQ_serverZoneId}">
						<input type="text" value="<huake:getServerZoneNameTag id="${param.search_EQ_serverZoneId}"></huake:getServerZoneNameTag>"   disabled="disabled"/>
						<input type="text" value="${param.search_EQ_serverZoneId}"  name="search_EQ_serverZoneId" style="display: none;"/>
					</c:if>		
					<c:if test="${empty param.search_EQ_serverZoneId}">
						<input type="text"  readonly="readonly" name="search_EQ_serverZoneId"/>
					</c:if>		
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="version">版本号：</label>
				<div class="controls">
					<input type="text" id="version" name="version" class="input-large"  placeholder="版本号"/>
				</div>
			</div>	
			<div class="control-group ">
				<label class="control-label" for="contents">公告内容：</label>
				<div class="controls">
					<textarea ="contents" id="contents" name="contents" cssClass="input-xlarge" value="" style="height: 200px;width: 800px" /></textarea>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="">选择修改服务器：</label>
				<div class="controls" id="serverDivCheckbox">
					<c:forEach items="${servers}" var="item" >
						<label class="checkbox inline">
							<input type="checkbox" id="serverId_checkbox" name="serverId" value="${item.serverId }"/><span>${item.serverId}</span><br/>
						</label>
					</c:forEach>
				</div>
			</div>
			<div class="form-ac">
				<button type="button" class="btn btn-success" onclick="selectAll();">全选</button>
				<button type="button" class="btn btn-info" onclick="selectAllNot();">反选</button>
			</div>
			<div class="form-actions" >
				<shiro:hasAnyRoles name="admin,fb_gm_placard_update">
					<input type="submit" class="btn btn-primary" value="修改服务器公告" />
				</shiro:hasAnyRoles>	
				<a class="btn btn-primary" id="cancel">取消</a>
			</div>
		</form>


	</div>
	<script type="text/javascript">
			$(function(){
				$('.intro').tooltip();
			});
			
			function updatePlacard(version,contents,id){
				$("#id").attr('value',id);
				$('#inputForm').show();
				$("#version").attr('value','');
				$("#contents").attr('value','');
				
				$("#version").attr('value',version);
				$("#contents").attr('value',contents);
				
				$("#selBtn").attr("disabled","disabled");  
				$("#gameId").attr("disabled","disabled");  
				$("#serverZoneId").attr("disabled","disabled");  
				$("#itemDiv").attr("disabled","disabled"); 
				$("input#serverId").attr("disabled","disabled");  
			}
			
			function delPlacard(id,gameId,serverZoneId,serverId){
				if(confirm("该操作会删除。。。。！"))
				    {
							$.ajax({
								url: '<%=request.getContextPath()%>/manage/gm/fb/placard/del?id='+id+'&gameId='+gameId+'&serverZoneId='+serverZoneId+'&serverId='+serverId, 
								type: 'DELETE',
								contentType: "application/json;charset=UTF-8",
								dataType: 'json',
								success: function(data){
									if(data.message == 'error'){
										alert("删除失败");
									}
									window.location.href = window.location.href;
								},error:function(xhr){
									alert('错误了，请重试');
								}
							});
				    }
			}
			
			$("#cancel").click(function(){
				$('#inputForm').hide();
				
				$("#selBtn").removeAttr("disabled"); 
				$("#gameId").removeAttr("disabled"); 
				$("#serverZoneId").removeAttr("disabled"); 
				$("#itemDiv").removeAttr("disabled"); 
				$("input#serverId").removeAttr("disabled"); 
			});
			
			$("#checkAll").click(function(){
		         var bischecked=$('#checkAll').is(':checked');
		         var f=$('input[class="checkbox"]');
		         bischecked?f.attr('checked',true):f.attr('checked',false);
		         var m=$('.cStatus');
		         bischecked?m.show():m.hide();
			});
			$(".checkbox").click(function(){
		         var bischecked=$('.checkbox').is(':checked');
		         var m=$('.cStatus');
		         bischecked?m.show():m.hide();
			});
			
			function selectAll(){  
		        $("input[id='serverId_checkbox']").attr("checked", true);  
			}	
			function selectAllNot(){
		    	$("input[id='serverId_checkbox']").attr("checked", false);  
			}	
			$(function(){
				$("#serverZoneId").change(function(e){
					var serverZoneId = $("#serverZoneId").val();
					if($("#gameId").val()!=""){
						var gameId = $("#gameId").val();
						$("#serverDiv").empty();
						$.ajax({                                               
							url: '<%=request.getContextPath()%>/manage/gm/fb/serverStatus/findServers?serverZoneId='+serverZoneId+'&gameId='+gameId, 
							type: 'GET',
							contentType: "application/json;charset=UTF-8",		
							dataType: 'text',
							success: function(data){
								var parsedJson = $.parseJSON(data);
								jQuery.each(parsedJson, function(index, itemData) {
								$("#serverDiv").append("<c:forEach items='"+itemData+"' var='ite' varStatus='j'><label class='radio inline'><input type='radio' id='serverId' name='search_EQ_serverId' value='"+itemData.serverId+"'/><span>"+itemData.serverId+"</span><br/></label></c:forEach>"); 
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
							url: '<%=request.getContextPath()%>/manage/gm/fb/serverStatus/findServers?serverZoneId='+serverZoneId+'&gameId='+gameId, 
							type: 'GET',
							contentType: "application/json;charset=UTF-8",		
							dataType: 'text',
							success: function(data){
								var parsedJson = $.parseJSON(data);
								jQuery.each(parsedJson, function(index, itemData) {
								$("#serverDiv").append("<c:forEach items='"+itemData+"' var='ite' varStatus='j'><label class='radio inline'><input type='radio' id='serverId' name='search_EQ_serverId' value='"+itemData.serverId+"'/><span>"+itemData.serverId+"</span><br/></label></c:forEach>"); 
								});
							},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
						});
					}	
					
				});
				
				$("#queryForm").validate({
					rules:{
						search_EQ_storeId:{
							required:true
						},
						search_EQ_serverZoneId:{
							required:true
						},
						search_EQ_serverId:{
							required:true
						}
					},messages:{
						search_EQ_storeId:{
							required:"游戏项目"
						},
						search_EQ_serverZoneId:{
							required:"运营必须填写"
						},
						search_EQ_serverId:{
							required:"服务器必须填写"
						}
					}
				});			
				
				
				$("#inputForm").validate({
					rules:{
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
</html>