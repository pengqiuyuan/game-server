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
<title>礼品卡列表</title>
</head>
<body>
   
	<div >
		<div class="page-header">
			<h2>礼品卡列表</h2>
		</div>
		<div>
		 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
		</c:if>
				<form id="queryForm" class="well form-inline"  method="get" action="${ctx}/manage/gift/index">
					<label>选择项目：</label> 
					<select name="search_LIKE_store">		
						<option value="">选择项目</option>
						<c:forEach items="${stores}" var="item" >
							<option value="${item.id}"  ${param.search_LIKE_store == item.id ? 'selected' : '' }>
								${item.name}
							</option>	
						</c:forEach>
					</select>
					<input type="submit" class="btn" value="查 找" />
				<tags:sort />
				</form>
		</div>
		
		<table class="table table-striped table-bordered table-condensed" id="table">
			<thead>
				<tr>
					<th class="check-header hidden-xs">
                      <label><input id="checkAll" name="checkAll" type="checkbox"><span></span></label>
                    </th>
					<th title="礼品卡Id">礼品卡Id</th>
					<th title="生成人">生成人</th>
					<th title="开始日期">开始日期</th>
					<th title="结束日期">结束日期</th>
					<th title="数量">数量</th>
					<th title="状态">状态</th>
					<th title="操作">操作</th>
					<th title="道具">道具</th>
				</tr>
			</thead>
			<tbody id="tbody">
				<c:forEach items="${gifts.content}" var="item" varStatus="s">
					<tr id="${item.giftId}">
						<td><label><input  type="checkbox" class="checkbox" value="${item.giftId}"><span></span></label></td>
						<td id="iDictionary" value="${item.giftId}">
							<div class="btn-group">
								<a class="btn" href="#">#${item.giftId}</a> <a
									class="btn dropdown-toggle" data-toggle="dropdown" href="#">
									<span class="caret"></span>
								</a>
								<ul class="dropdown-menu">
									<shiro:hasAnyRoles name="admin">
										<c:if test="${item.giftId == 0 ? false : true}">
											<li><a href="javascript:void(0);" rel="${item.giftId}" class="del"><i class="icon-th"></i>删除 </a></li>
										</c:if>
									</shiro:hasAnyRoles>
									<li class="divider"></li>
								</ul>
							</div>
						</td>
						<td><huake:GetUserNameTag id="${item.userId}"></huake:GetUserNameTag></td>
<%-- 						<td>${item.coin}</td>
						<td>${item.diamond}</td>
						<td>${item.arenacoin}</td>
						<td>${item.expeditioncoin}</td>
						<td>${item.tradecoin}</td> --%>
						<td><huake:getDateTag id="${item.beginDate}"></huake:getDateTag></td>
						<td><huake:getDateTag id="${item.endDate}"></huake:getDateTag></td>
						<td>${item.number}</td>
						<td>
							<c:if test="${item.status == 0}"><span>审核中</span></c:if>
							<c:if test="${item.status == 1}"><span>已通过</span></c:if>
							<c:if test="${item.status == 2}"><span>被拒绝</span></c:if>
						</td>
						<td>
							<div class="action-buttons">
								<c:if test="${item.status == 0}">
									<a class="table-actions" href="${ctx}/manage/gift/review?giftId=${item.giftId}&status=1&stores=${param.search_LIKE_store}" style="text-decoration:none"><i class="icon-ok"></i>同意</a>
							    	<a class="table-actions" href="${ctx}/manage/gift/review?giftId=${item.giftId}&status=2&stores=${param.search_LIKE_store}" style="text-decoration:none"><i class=" icon-remove"></i>拒绝</a>
								</c:if>
								<c:if test="${item.status == 1}">
									<a class="table-actions"  href="${ctx}/manage/gift/export?giftId=${item.giftId}" style="text-decoration:none"><i class="icon-download-alt"></i>导出礼品码</a>
								</c:if>
								<c:if test="${item.status == 2}">
									<a class="table-actions"  href="${ctx}/manage/gift/export?giftId=${item.giftId}" style="text-decoration:none"><i class="icon-download-alt"></i>导出礼品码</a>
								</c:if>
							</div>
						</td>	
						<td>
							<c:forEach items="${item.giftItems}" var="item" varStatus="i">
								<span><huake:GetItemNameTag id="${item.id}"></huake:GetItemNameTag>*${item.number}</span>&nbsp;&nbsp;
							</c:forEach>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<tags:pagination page="${gifts}" paginationSize="5"/>
		<div class="form-actions">
			<a onclick="selectAll();" class="btn btn-danger">删除选中礼品码</a>
			<shiro:hasAnyRoles name='admin,29'>
			<a href="${ctx}/manage/gift/add" class="btn btn-primary">新增礼品卡</a>
			</shiro:hasAnyRoles>
		</div>
		
	</div>
		<script type="text/javascript">
		$("#checkAll").click(function(){
	         var bischecked=$('#checkAll').is(':checked');
	         var f=$('input[class="checkbox"]');
	         bischecked?f.attr('checked',true):f.attr('checked',false);
		});
		
		function selectAll(){  
	         var csv = '';           
	         $('input[class="checkbox"]:checked').each(function() {
	             csv += $(this).attr("value") + ",";
	         });         
			if(confirm("该操作会删除。。。。！"))
			{
						$.ajax({
							url: '<%=request.getContextPath()%>/manage/gift/del?id=' + csv, 
							type: 'DELETE',
							contentType: "application/json;charset=UTF-8",
							dataType: 'json',
							success: function(data){
								if(data.status=="success"){
									window.location.href = window.location.href;
									alert("删除成功！");
								}else if(data.status=="false"){
									window.location.href = window.location.href;
									alert("删除失败！");
								}
							}
						});
			}         
		}	
		
		
		$(document).ready(function(){
			$('.showInfo').fancybox({
				autoDimensions:false,
				width:800,
				height:500
			});
			
			$(".del").click(function(){
				if(confirm("该操作会删除。。。。！"))
			    {
				var id = $(this).attr("rel");
					$.ajax({
						url: '<%=request.getContextPath()%>/manage/gift/del?id=' + id, 
						type: 'DELETE',
						contentType: "application/json;charset=UTF-8",
						dataType: 'json',
						success: function(data){
							if(data.status=="success"){
								window.location.href = window.location.href;
								alert("删除成功！");
							}else if(data.status=="false"){
								window.location.href = window.location.href;
								alert("删除失败！");
							}
						}
					});
			     }
			});
		});
		</script> 	
</body>
</html>