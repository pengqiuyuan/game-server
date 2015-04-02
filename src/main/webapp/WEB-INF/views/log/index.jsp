<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>



<title>日志管理</title>
</head>
<body>
   
	<div >
		<div class="page-header">
			<h2>日志管理</h2>
		</div>
		<div>
		 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
				<form id="queryForm" class="well form-inline"  method="get"
					action="${ctx}/manage/log/index">
					<label>操作人：</label> <input name="search_EQ_crUser"
						type="text" value="${param.search_EQ_crUser}" /> 
			         <label>日志类型：</label> 	<select name="search_EQ_type" style="width:200px">
					<option value="">---------请选择---------</option>
					<option value="1" ${param.search_EQ_type == '1' ? 'selected' : '' } >订单日志</option>
					<option value="2" ${param.search_EQ_type == '2' ? 'selected' : '' } >商品日志</option>
					<option value="3" ${param.search_EQ_type == '3' ? 'selected' : '' } >用户日志</option>
					<option value="4" ${param.search_EQ_type == '4' ? 'selected' : '' } >门店日志</option>
					<option value="5" ${param.search_EQ_type == '5' ? 'selected' : '' } >劵日志</option>
					<option value="6" ${param.search_EQ_type == '6' ? 'selected' : '' } >配置日志</option>
					<option value="7" ${param.search_EQ_type == '7' ? 'selected' : '' } >主题日志</option>
					<option value="8" ${param.search_EQ_type == '8' ? 'selected' : '' } >活动日志</option>
				</select>	
			
					
						 <input type="submit" class="btn"
						value="查 找" />
				<tags:sort />
				</form>
			
			
		</div>
		<table class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th title="编号" width="120px">编号</th>
					<th title="操作人">操作人</th>

					<th title="内容">内容</th>
					<th title="日志类型">日志类型</th>
					<th title="时间" width="240px">时间</th>
					
					
				</tr>
			</thead>
			<tbody>

				<c:forEach items="${logs.content}" var="item" varStatus="s">
				
					<tr  >
					
						<td>
							<div class="btn-group">
								<a class="btn" href="#">#${s.index+1}</a> <a
									class="btn dropdown-toggle" data-toggle="dropdown" href="#">
									<span class="caret"></span>
								</a>
								<ul class="dropdown-menu">
								</ul>
							</div>
						</td>
						
						<td>
						${item.crUser }
						</td>

						<td>
							<a
							href="<%=request.getContextPath()%>/manage/log/detail?id=${item.id}"
							 data-fancybox-type="iframe" 
							 rel="fancy" title="日志详细" class="showInfo" >
								<c:choose>
		    					<c:when test="${fn:length(item.content)>10}"> 
		     					<c:out value="${fn:substring(item.content,0,10) }..." />
		    					</c:when> 
		    					<c:otherwise> 
		     						<c:out value="${item.content}" />
		    					</c:otherwise> 
							</c:choose>
						
						</a>
						</td>
					<td>${item.type == "1" ? "订单日志" : item.type == "2" ? "商品日志" : item.type == "3" ? "用户日志" : item.type == "4"? "门店日志" : item.type == "5" ? "劵日志" : item.type == "6" ? "配置日志" : item.type == "7"? "主题日志" : item.type == "8"? "活动日志" : "未知"}</td>
						<td><fmt:formatDate value="${item.crDate}"
								pattern="yyyy/MM/dd  HH:mm:ss" /></td>
						
					
						
					</tr>
				</c:forEach>
			</tbody>
		</table>

		
		<tags:pagination page="${logs}" paginationSize="5"/>
		
		

	
	</div>
		<script type="text/javascript">
		$(document).ready(function(){
			$('.showInfo').fancybox({
				autoDimensions:false,
				width:800,
				height:500
			});			
			
		});
	
	
	
		
		
		</script> 	
</body>
</html>