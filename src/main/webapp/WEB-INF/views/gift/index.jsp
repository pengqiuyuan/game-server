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
<script type="text/javascript" src="${ctx}/static/js/jquery-ui-1.8.21.custom.min.js"></script>
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
		</div>
		
		<table class="table table-striped table-bordered table-condensed" id="table">
			<thead>
				<tr>
					<th class="check-header hidden-xs">
                      <label><input id="checkAll" name="checkAll" type="checkbox"><span></span></label>
                    </th>
					<th title="名称">名称</th>
					<th title="ID">ID</th>
					<th title="生成人">生成人</th>
					<th title="金币">金币</th>
					<th title="钻石">钻石</th>
					<th title="竞技币">竞技币</th>
					<th title="龙鳞币">龙鳞币</th>
					<th title="工会币">工会币</th>
					<th title="开始日期">开始日期</th>
					<th title="结束日期">结束日期</th>
					<th title="道具">道具</th>
					<th title="数量">数量</th>
					<th title="状态">状态</th>
					<th title="操作">操作</th>
				</tr>
			</thead>
			<tbody id="tbody">
				<c:forEach items="${gifts.content}" var="item" varStatus="s">
					<tr id="${item.giftId}">
						<td></td>
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
						
						<td>${item.userId}</td>
						<td>${item.giftId}</td>
						<td>${item.coin}</td>
						<td>${item.diamond}</td>
						<td>${item.arenacoin}</td>
						<td>${item.expeditioncoin}</td>
						<td>${item.tradecoin}</td>
						<td><huake:getDateTag id="${item.beginDate}"></huake:getDateTag></td>
						<td><huake:getDateTag id="${item.endDate}"></huake:getDateTag></td>
						<td>${item.number}</td>
						<td>${item.number}</td>
						<td>${item.status}</td>
						<td></td>	
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<tags:pagination page="${gifts}" paginationSize="5"/>
		
	</div>
		<script type="text/javascript">
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
						url: '<%=request.getContextPath()%>/manage/store/del?id=' + id, 
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
			});
		});
		</script> 	
</body>
</html>