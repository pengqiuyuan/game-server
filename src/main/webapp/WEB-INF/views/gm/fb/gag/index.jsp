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
	<title>禁言</title>
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
	<script type="text/javascript" src="${ctx}/static/resources/date/My97DatePicker/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript">
 	 function display(){   
	  	WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',startDate:'2012-07-27'});
 	 }
	</script>
	<div >
		<div class="page-header">
			<h2>禁言</h2>
		</div>
		<div>
			<c:if test="${not empty message}">
				<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
			</c:if>
			<form id="queryForm" class="form-horizontal"  method="get" action="${ctx}/manage/gm/fb/gag/index">
				<div class="control-group">
					<label class="control-label" for="gameId">选择游戏项目：</label>
					<div class="controls">
							<select name="search_LIKE_storeId" id="gameId">	
								<option value="">请选择项目</option>	
								<c:forEach items="${stores}" var="item" >
									<option value="${item.id }"   ${param.search_LIKE_storeId == item.id ? 'selected' : '' }>
											${item.name }
									</option>
								</c:forEach>
							</select>	
					</div>
				</div>		
				<div class="control-group">
					<label class="control-label" for="serverZoneId">选择运营大区：</label>
					<div class="controls">
						<select name="search_LIKE_serverZoneId" id="serverZoneId">	
							<option value="">请选择项目</option>	
							<c:forEach items="${serverZones}" var="item" >
								<option value="${item.id }"  ${param.search_LIKE_serverZoneId == item.id ? 'selected' : '' } >
									${item.serverName }
								</option>
							</c:forEach>
						</select>	
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="">服务器列表：</label>
					<div class="controls" id="serverDiv">
						<c:forEach items="${servers}" var="item" >
							<label class="radio inline" >
								<input type="radio" id="serverId" name="search_LIKE_serverId"  value="${item.serverId }" ${param.search_LIKE_serverId == item.serverId? 'checked' : '' }/><span>${item.serverId}</span><br/>
							</label>
						</c:forEach>
					</div>
				</div>
				<div class="form-ac">
					<input type="submit" class="btn btn-primary" id="selBtn" value="查 找" />
				</div>
			</form>
		</div>
		<div class="control-group">
				<table class="table table-striped table-bordered table-condensed" id="table">
					<thead>
						<tr>
		                    <th title="禁言guid">禁言guid</th>
							<th title="禁言名称">禁言名称</th>
							<th title="禁言account">禁言account器ID</th>
							<th title="禁言渠道">禁言渠道</th>
							<th title="禁言时间">禁言时间</th>
							<th title="禁言开始时间">禁言开始时间</th>
							<th title="禁言结束时间">禁言结束时间</th>
						</tr>
					</thead>
					<tbody id="tbody">
						<c:forEach items="${gag.content}" var="item" varStatus="s">
							<tr id="${item.guid}">
								<td>${item.name}</td>
								<td>${item.account}</td>
								<td>${item.platForm}</td>
								<td>${item.gagTime}</td>
								<td>${item.gagStart}</td>
								<td>${item.gagEnd}</td>
			   					<td>
									<div class="action-buttons">
										<shiro:hasAnyRoles name="admin">
											<a class="exportCode btn table-actions" onclick="updateGag('${item.guid}','${item.name}','${item.account}','${item.platForm}','${item.id}')"><i class="icon-ok"></i>修改</a>
									    	<a class="exportCode btn table-actions" onclick="delGag('${item.id}')"><i class="icon-remove"></i>删除</a>
										</shiro:hasAnyRoles>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<tags:pagination page="${gag}" paginationSize="5"/>
		</div>
		


		<form id="inputForm" method="post" Class="form-horizontal" action="<%=request.getContextPath()%>/manage/gm/fb/gag/update"   enctype="multipart/form-data"  style="display: none;">
			<div style="color:#3352CC;clear:both">
			<br><hr style="background-color:#808080;height:1px;width:800px;margin:auto"><h4>修改禁言时间：</h4></div>
			<input type="hidden" name="id" id="id">
			<div class="control-group">
				<label class="control-label" for="">游戏:</label>
				<div class="controls">
					<c:if test="${not empty param.search_LIKE_serverZoneId}">
						<input type="text" value='<huake:getStoreNameTag id="${param.search_LIKE_storeId}"></huake:getStoreNameTag>' disabled="disabled"/>
						<input type="text" value='${param.search_LIKE_storeId}'  name="search_LIKE_storeId" style="display: none;"/>
					</c:if>
					<c:if test="${empty param.search_LIKE_serverZoneId}">
						<input type="text"  readonly="readonly" name="search_LIKE_storeId"/>
					</c:if>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="">运营大区:</label>
				<div class="controls">
					<c:if test="${not empty param.search_LIKE_serverZoneId}">
						<input type="text" value="<huake:getServerZoneNameTag id="${param.search_LIKE_serverZoneId}"></huake:getServerZoneNameTag>"   disabled="disabled"/>
						<input type="text" value="${param.search_LIKE_serverZoneId}"  name="search_LIKE_serverZoneId" style="display: none;"/>
					</c:if>		
					<c:if test="${empty param.search_LIKE_serverZoneId}">
						<input type="text"  readonly="readonly" name="search_LIKE_serverZoneId"/>
					</c:if>		
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="">服务器:</label>
				<div class="controls">
					<c:if test="${not empty param.search_LIKE_serverId}">
						<input type="text" value="${param.search_LIKE_serverId}"  disabled="disabled" />
						<input type="text" value="${param.search_LIKE_serverId}"  name="search_LIKE_serverId" style="display: none;"/>
					</c:if>		
					<c:if test="${empty param.search_LIKE_serverId}">
						<input type="text"  readonly="readonly" name="search_LIKE_serverId"/>
					</c:if>		
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="platForm">渠道：</label>
				<div class="controls">
					<input type="text" id="platForm" name="platForm" class="input-large"  placeholder="渠道"/ readonly="readonly">
				</div>
			</div>	
			<div
				class="control-group">
				<label class="control-label" for="guid">禁言guid：</label>
				<div class="controls">
					<input type="text" id="guid" name="guid" class="input-large"  placeholder="禁言guid"/ readonly="readonly">
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="name">禁言名称：</label>
				<div class="controls">
					<input type="text" id="name" name="name" class="input-large"  placeholder="禁言名称"/ readonly="readonly">
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="account">禁言account：</label>
				<div class="controls">
					<input type="text" id="account" name="account" class="input-large"  placeholder="禁言account"/ readonly="readonly">
				</div>
			</div>
			<div class="control-group" id="selectDate">
				<label class="control-label" for="gagTime">选择禁言时间：</label>
				<div class="controls">
					<select name="gagTime" id="gagTime">	
						<option value="">请选择禁言时间</option>
						<option value="1800">封禁半小时</option>	
						<option value="43200">封禁12小时</option>	
						<option value="86400">封禁1天</option>	
						<option value="2592000">封禁1个月</option>	
						<option value="31536000">封禁1年</option>	
						<option value="－1">永久封禁</option>		
					</select>	
				</div>
			</div>
			<div id="customDate" style="display: none;">
				<div class="control-group">
					<label class="control-label" for=gagStart>禁言开始时间：</label>
					<div class="controls">
						<input type="text" name="gagStart" class="input-large " value="" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="gagStart"  placeholder="禁言开始时间"/>
					</div>
				</div>	
				<div class="control-group">
					<label class="control-label" for="gagEnd">禁言结束时间：</label>
					<div class="controls">
						<input type="text" name="gagEnd" class="input-large " value="" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="gagEnd" placeholder="禁言结束时间"/>
						<div id="time"  class="alert alert-danger" style="display: none;width: 20%; margin-top: 10px;"><button data-dismiss="alert" class="close">×</button>结束时间不能小于开始时间</div>
					</div>
				</div>
			</div>
			<div class="form-ac">
				<button type="button" class="btn btn-success" onclick="customDate();">自定义禁言时间</button>
				<button type="button" class="btn btn-info" onclick="cancelCustomDate();">取消自定义禁言</button>
			</div>
			<shiro:hasAnyRoles name="admin">
				<div class="form-actions" >
					<input type="submit" class="btn btn-primary" value="修改服务器公告" />
					<a class="btn btn-primary" id="cancel">取消</a>
				</div>
			</shiro:hasAnyRoles>	
		</form>


	</div>
	<script type="text/javascript">
	
			$(function(){
				$('.intro').tooltip();
			});
			
			function updateGag(platForm,guid,name,account,id){
				$('#inputForm').show();
				
			    $("#gagTime").val("pxx");
				$('#gagStart').attr('value','');
				$('#gagEnd').attr('value','');
				
				
				$("#id").attr('value',id);
				$("#platForm").attr('value',platForm);
				$("#guid").attr('value',guid);
				$("#name").attr('value',name);
				$("#account").attr('value',account);
				
				
				$("#selBtn").attr("disabled","disabled");  
				$("#gameId").attr("disabled","disabled");  
				$("#serverZoneId").attr("disabled","disabled");  
				$("input#serverId").attr("disabled","disabled");  
			}
			
			function delGag(id){
				if(confirm("该操作会删除。。。。！"))
				    {
							$.ajax({
								url: '<%=request.getContextPath()%>/manage/gm/fb/gag/del?id=' + id, 
								type: 'DELETE',
								contentType: "application/json;charset=UTF-8",
								dataType: 'json',
								success: function(data){
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
				$("input#serverId").removeAttr("disabled"); 
			});
			
			function customDate(){  
				$('#customDate').show();
				$("#gagStart").removeAttr("disabled");  
				$("#gagEnd").removeAttr("disabled");  
				$("#gagTime").attr("disabled","disabled");  
			    $("#gagTime").val("pxx");
				$('#selectDate').hide();
			}	
			function cancelCustomDate(){  
				$('#selectDate').show();
				$("#gagTime").removeAttr("disabled");  
				
				$("#gagStart").attr("disabled","disabled");  
				$("#gagEnd").attr("disabled","disabled");  
				$('#gagStart').attr('value','');
				$('#gagEnd').attr('value','');
				$('#customDate').hide();
			
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
								$("#serverDiv").append("<c:forEach items='"+itemData+"' var='ite' varStatus='j'><label class='radio inline'><input type='radio' id='serverId' name='search_LIKE_serverId' value='"+itemData.serverId+"'/><span>"+itemData.serverId+"</span><br/></label></c:forEach>"); 
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
								$("#serverDiv").append("<c:forEach items='"+itemData+"' var='ite' varStatus='j'><label class='radio inline'><input type='radio' id='serverId' name='search_LIKE_serverId' value='"+itemData.serverId+"'/><span>"+itemData.serverId+"</span><br/></label></c:forEach>"); 
								});
							},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
						});
					}	
					
				});
				
				$("#queryForm").validate({
					rules:{
						search_LIKE_storeId:{
							required:true
						},
						search_LIKE_serverZoneId:{
							required:true
						},
						search_LIKE_serverId:{
							required:true
						}
					},messages:{
						search_LIKE_storeId:{
							required:"游戏项目"
						},
						search_LIKE_serverZoneId:{
							required:"运营必须填写"
						},
						search_LIKE_serverId:{
							required:"服务器必须填写"
						}
					}
				});			
				
				
				$("#inputForm").validate({
					rules:{
						gagTime:{
							required:true
						},
						gagStart:{
							required:true
						},
						gagEnd:{
							required:true
						}
					},messages:{
						gagTime:{
							required:"禁言时间必须填写"
						},
						gagStart:{
							required:"禁言开始时间必须填写"
						},
						gagEnd:{
							required:"禁言结束时间必须填写"
						}
					}
				});			
				
			})
			
			
	</script> 	
</body>
</html>