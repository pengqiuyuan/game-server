<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="huake" uri="/huake"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<meta http-equiv="Cache-Control" content="no-store" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	<link href="<%=request.getContextPath()%>/static/flot/css/bootstrap.light.min.css" title="Light" rel="stylesheet">
	<link href="<%=request.getContextPath()%>/static/flot/css/datepicker3.css" rel="stylesheet">
	<link href="<%=request.getContextPath()%>/static/flot/css/style.css?v=2.1.0" rel="stylesheet">
	<!-- Mainly scripts -->
	<script src="<%=request.getContextPath()%>/static/flot/js/jquery-1.8.0.js"></script>
	<script src="<%=request.getContextPath()%>/static/flot/js/bootstrap.js"></script>
	<script src="${ctx}/static/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
	<link href="<%=request.getContextPath()%>/static/echarts/css/echartsHome.css" rel="stylesheet">
	<style type="text/css">
		.tab-content>.tab-pane, .pill-content>.pill-pane {
			display: block; /* undo display:none          */
			height: 0; /* height:0 is also invisible */
			overflow-y: hidden; /* no-overflow                */
		}
		
		.tab-content>.active, .pill-content>.active {
			height: auto; /* let the content decide it */
		}
		
		.table-responsive {
			min-height: .01%;
			overflow-x: auto;
		}
	</style>
</head>
<body>
	<div class="container-fluid ibox-content">
		<form id="inputForm" Class="form-horizontal">
			<div class="control-group">
				<div class="form-group">
					<input type="hidden" id="storeId" value="${store.id}"> <span
						class="btn btn-primary btn-xs m-l-sm" type="button">${store.name}：用户留存</span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">运营大区：</label>
				<div class="controls">
					<label class="checkbox inline"> 
						<input type="checkbox" name="serverZone" value="all" <c:forEach items="${sZones}" var="i" ><c:if test="${i == '所有运营大区' }">checked="checked" </c:if></c:forEach>>
						<span>所有运营大区</span>
					</label>
					<c:forEach items="${serverZone}" var="item">
						<label class="checkbox inline"> 
							<input type="checkbox" name="serverZone" value="${item.id }" <c:forEach items="${sZones}" var="i" ><c:if test="${i == item.serverName }">checked="checked" </c:if></c:forEach>>
							<span>${item.serverName }</span> 
							<c:if test="${(j.index+1)%12 == 0}">
							</c:if>
						</label>
					</c:forEach>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">渠道：</label>
				<div class="controls">
					<c:forEach items="${platForm}" var="item">
						<label class="checkbox inline"> 
							<input type="checkbox" name="platForm" value="${item.pfId }" <c:forEach items="${pForms}" var="i" ><c:if test="${i == item.pfName }">checked="checked" </c:if></c:forEach>>
							<span>${item.pfName }</span> 
							<c:if test="${(j.index+1)%12 == 0}">
							</c:if>
						</label>
					</c:forEach>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">服务器：</label>
				<div class="controls">
					<c:forEach items="${server}" var="item">
						<label class="checkbox inline"> <input type="checkbox" name="server" value="${item.serverId }" <c:forEach items="${svs}" var="i" ><c:if test="${i == item.serverId }">checked="checked" </c:if></c:forEach>>
							<span>${item.serverId }</span> 
							<c:if test="${(j.index+1)%12 == 0}">
							</c:if>
						</label>
					</c:forEach>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="datetimepickerStart">起始时间：</label>
				<div class="controls">
					<div class="input-append date dp3" data-date-format="yyyy-mm-dd">
						<c:if test="${not empty param.search_EQ_dateFrom}">
							<input size="16" type="text" id="dateFrom" value="${param.search_EQ_dateFrom == dateFrom  ? dateFrom : param.search_EQ_dateFrom }" name="search_EQ_dateFrom">
						</c:if>
						<c:if test="${empty param.search_EQ_dateFrom}">
							<input size="16" type="text" id="dateFrom" value="${dateFrom}" name="search_EQ_dateFrom">
						</c:if>
						<span class="add-on" style="display: none"></span>
					</div>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="datetimepickerEnd">结束时间：</label>
				<div class="controls">
					<div class="input-append date dp3" data-date-format="yyyy-mm-dd">
						<c:if test="${not empty param.search_EQ_dateFrom}">
							<input size="16" type="text" id="dateTo" value="${param.search_EQ_dateTo == dateTo  ? dateTo : param.search_EQ_dateTo }" name="search_EQ_dateTo">
						</c:if>
						<c:if test="${empty param.search_EQ_dateFrom}">
							<input size="16" type="text" id="dateTo" value="${dateTo}" name="search_EQ_dateTo">
						</c:if>
						<span class="add-on" style="display: none"></span>
					</div>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="datetimepickerEnd">结束时间：</label>
				<div class="controls">
					<label class="radio inline"> 
						<input type="radio" name="switchTable" value="1" ${switchTable == '1'?'checked' : ''}>
						<span>查看所有</span> 
					</label>
					<label class="radio inline"> 
						<input type="radio" name="switchTable" value="2" ${switchTable == '2'?'checked' : ''}>
						<span>查看登录</span> 
					</label>
					<label class="radio inline"> 
						<input type="radio" name="switchTable" value="3" ${switchTable == '3'?'checked' : ''}>
						<span>查看留存</span> 
					</label>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls">
					<a href="#" class="btn btn-primary" id="sevenDayAgo">近7日</a> 
					<a href="#" class="btn btn-primary" id="fifteenDayAgo">近15日</a>
					<a href="#" class="btn btn-primary" id="thirtyDayAgo">近30日</a>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls">
					<button class="btn btn-success" id="sub" type="submit">
						<i class="fa fa-check"></i>&nbsp;&nbsp;<span class="bold">确定</span>
					</button>
				</div>
			</div>
		</form>
	</div>

	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="row-fluid">
			<div class="table-responsive ibox-content span12">
				<p>
					<code>次日~10日留存（留存|登录|新增）</code>
				</p>
				<table class="table table-striped table-bordered table-condensed"
					id="table">
					<thead>
						<tr>
							<th title="时间">时间</th>
							<th title="新增">新增</th>
							<th title="次日">${switchTable == '2'? '次日登录':'次日留存'}</th>
							<th title="3日">${switchTable == '2'? '3日登录':'3日留存'}</th>
							<th title="4日">${switchTable == '2'? '4日登录':'4日留存'}</th>
							<th title="5日">${switchTable == '2'? '5日登录':'5日留存'}</th>
							<th title="6日">${switchTable == '2'? '6日登录':'6日留存'}</th>
							<th title="7日">${switchTable == '2'? '7日登录':'7日留存'}</th>
							<th title="8日">${switchTable == '2'? '8日登录':'8日留存'}</th>
							<th title="9日">${switchTable == '2'? '9日登录':'9日留存'}</th>
							<th title="10日">${switchTable == '2'? '10日登录':'10日留存'}</th>
							<th title="11日">${switchTable == '2'? '11日登录':'11日留存'}</th>
						</tr>
					</thead>
					<tbody id="tbody">
						<c:forEach items="${retained1s}" var="item" varStatus="s">
							<tr id="">
								<td>${item.xDate}</td>
								<td>${item.addUser}</td>
								<td>${item.day2}</td>
								<td>${item.day3}</td>
								<td>${item.day4}</td>
								<td>${item.day5}</td>
								<td>${item.day6}</td>
								<td>${item.day7}</td>
								<td>${item.day8}</td>
								<td>${item.day9}</td>
								<td>${item.day10}</td>
								<td>${item.day11}</td>
							</tr>
						</c:forEach>
							<tr id="" style="background-color: #d9edf7;">
								<td>${retainedAll1.xDate}</td>
								<td>${retainedAll1.addUser}</td>
								<td><a href="#" class="intro" title="${retainedAll1.day2text}">${retainedAll1.day2}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day3text}">${retainedAll1.day3}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day4text}">${retainedAll1.day4}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day5text}">${retainedAll1.day5}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day6text}">${retainedAll1.day6}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day7text}">${retainedAll1.day7}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day8text}">${retainedAll1.day8}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day9text}">${retainedAll1.day9}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day10text}">${retainedAll1.day10}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day11text}">${retainedAll1.day11}</a></td>
							</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid">
			<div class="table-responsive ibox-content span12">
				<p>
					<code>11日~20日留存（留存|登录|新增）</code>
				</p>
				<table class="table table-striped table-bordered table-condensed"
					id="table">
					<thead>
						<tr>
							<th title="时间">时间</th>
							<th title="新增">新增</th>
							<th title="12日">${switchTable == '2'? '12日登录':'12日留存'}</th>
							<th title="13日">${switchTable == '2'? '13日登录':'13日留存'}</th>
							<th title="14日">${switchTable == '2'? '14日登录':'14日留存'}</th>
							<th title="15日">${switchTable == '2'? '15日登录':'15日留存'}</th>
							<th title="16日">${switchTable == '2'? '16日登录':'16日留存'}</th>
							<th title="17日">${switchTable == '2'? '17日登录':'17日留存'}</th>
							<th title="18日">${switchTable == '2'? '18日登录':'18日留存'}</th>
							<th title="19日">${switchTable == '2'? '19日登录':'19日留存'}</th>
							<th title="20日">${switchTable == '2'? '20日登录':'20日留存'}</th>
							<th title="21日">${switchTable == '2'? '21日登录':'21日留存'}</th>
						</tr>
					</thead>
					<tbody id="tbody">
						<c:forEach items="${retained2s}" var="item" varStatus="s">
							<tr id="">
								<td>${item.xDate}</td>
								<td>${item.addUser}</td>
								<td>${item.day12}</td>
								<td>${item.day13}</td>
								<td>${item.day14}</td>
								<td>${item.day15}</td>
								<td>${item.day16}</td>
								<td>${item.day17}</td>
								<td>${item.day18}</td>
								<td>${item.day19}</td>
								<td>${item.day20}</td>
								<td>${item.day21}</td>
							</tr>
						</c:forEach>
						<tr id="" style="background-color: #d9edf7;">
								<td>${retainedAll1.xDate}</td>
								<td>${retainedAll1.addUser}</td>
								<td><a href="#" class="intro" title="${retainedAll1.day12text}">${retainedAll1.day12}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day13text}">${retainedAll1.day13}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day14text}">${retainedAll1.day14}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day15text}">${retainedAll1.day15}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day16text}">${retainedAll1.day16}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day17text}">${retainedAll1.day17}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day18text}">${retainedAll1.day18}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day19text}">${retainedAll1.day19}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day20text}">${retainedAll1.day20}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day21text}">${retainedAll1.day21}</a></td>
							</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row-fluid">
			<div class="table-responsive ibox-content span12">
				<p>
					<code>21日~30日留存（留存|登录|新增）</code>
				</p>
				<table class="table table-striped table-bordered table-condensed"
					id="table">
					<thead>
						<tr>
							<th title="时间">时间</th>
							<th title="新增">新增</th>
							<th title="22日">${switchTable == '2'? '22日登录':'22日留存'}</th>
							<th title="23日">${switchTable == '2'? '23日登录':'23日留存'}</th>
							<th title="24日">${switchTable == '2'? '24日登录':'24日留存'}</th>
							<th title="25日">${switchTable == '2'? '25日登录':'25日留存'}</th>
							<th title="26日">${switchTable == '2'? '26日登录':'26日留存'}</th>
							<th title="27日">${switchTable == '2'? '27日登录':'27日留存'}</th>
							<th title="28日">${switchTable == '2'? '28日登录':'28日留存'}</th>
							<th title="29日">${switchTable == '2'? '29日登录':'29日留存'}</th>
							<th title="30日">${switchTable == '2'? '30日登录':'30日留存'}</th>
							<th title="31日">${switchTable == '2'? '31日登录':'31日留存'}</th>
						</tr>
					</thead>
					<tbody id="tbody">
						<c:forEach items="${retained3s}" var="item" varStatus="s">
							<tr id="">
								<td>${item.xDate}</td>
								<td>${item.addUser}</td>
								<td>${item.day22}</td>
								<td>${item.day23}</td>
								<td>${item.day24}</td>
								<td>${item.day25}</td>
								<td>${item.day26}</td>
								<td>${item.day27}</td>
								<td>${item.day28}</td>
								<td>${item.day29}</td>
								<td>${item.day30}</td>
								<td>${item.day31}</td>
							</tr>
						</c:forEach>
							<tr id="" style="background-color: #d9edf7;">
								<td>${retainedAll1.xDate}</td>
								<td>${retainedAll1.addUser}</td>
								<td><a href="#" class="intro" title="${retainedAll1.day22text}">${retainedAll1.day22}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day23text}">${retainedAll1.day23}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day24text}">${retainedAll1.day24}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day25text}">${retainedAll1.day25}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day26text}">${retainedAll1.day26}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day27text}">${retainedAll1.day27}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day28text}">${retainedAll1.day28}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day29text}">${retainedAll1.day29}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day30text}">${retainedAll1.day30}</a></td>
								<td><a href="#" class="intro" title="${retainedAll1.day31text}">${retainedAll1.day31}</a></td>
							</tr>
					</tbody>
				</table>
			</div>
		</div>

	</div>

	<script src="<%=request.getContextPath()%>/static/flot/js/bootstrap-datepicker.js"></script>
	<script type="text/javascript">
		$(function(){
			$('.intro').tooltip();
		});
		$(function(){
			//单选组事件
		    $(":checkbox").bind("click", function () {
		        $(":checkbox").prop("checked", false);  
		        $(this).prop("checked", true);  
		    })
		    
		    //自定义验证方法
		    jQuery.validator.addMethod("endDate",
		    function(value, element) {
		        var startDate = $('#dateFrom').val();
		        return new Date(Date.parse(startDate.replace("-", "/"))) <= new Date(Date.parse(value.replace("-", "/")));
		    },
		    "结束日期必须大于开始日期");
			
		    jQuery.validator.addMethod("endDate2",
		    function(value, element) {
		        var startDate = $('#dateFrom').val();
		        return Date.parse(startDate.replace("-", "/")) + 30*24*3600*1000 >= Date.parse(value.replace("-", "/"));
		    },
		    "查询日期间隔不超过 30 日");
		    
			$("#inputForm").validate({
				rules:{
					search_EQ_dateFrom:{
						required:true
					},
					search_EQ_dateTo:{
						required:true,
						endDate:true,
						endDate2:true
					}
				},messages:{
					search_EQ_dateFrom:{
						required:"选择起始时间"
					},
					search_EQ_dateTo:{
						required:"选择结束时间"
					}
				}
			});
			
			$('.dp3').datepicker({
		        keyboardNavigation: false,
		        forceParse: false,
		        autoclose: true
		    });
		    
			$("#yesterday").click(function(){
				$.ajax({                                               
					url: '<%=request.getContextPath()%>/manage/count/getDate',
					type: 'GET',
					contentType: "application/json;charset=UTF-8",		
					dataType: 'text',
					success: function(data){
						var parsedJson = $.parseJSON(data);
						$("#dateFrom").val(parsedJson.yesterday);
						$("#dateTo").val(parsedJson.nowDate);
					}//回调看看是否有出错
				});
			});
			$("#sevenDayAgo").click(function(){
				$.ajax({                                               
					url: '<%=request.getContextPath()%>/manage/count/getDate',
					type: 'GET',
					contentType: "application/json;charset=UTF-8",		
					dataType: 'text',
					success: function(data){
						var parsedJson = $.parseJSON(data);
						$("#dateFrom").val(parsedJson.sevenDayAgo);
						$("#dateTo").val(parsedJson.nowDate);
					}//回调看看是否有出错
				});
			});
			$("#fifteenDayAgo").click(function(){
				$.ajax({                                               
					url: '<%=request.getContextPath()%>/manage/count/getDate',
					type: 'GET',
					contentType: "application/json;charset=UTF-8",		
					dataType: 'text',
					success: function(data){
						var parsedJson = $.parseJSON(data);
						$("#dateFrom").val(parsedJson.fifteenDayAgo);
						$("#dateTo").val(parsedJson.nowDate);
					}//回调看看是否有出错
				});
			});
			$("#thirtyDayAgo").click(function(){
				$.ajax({                                               
					url: '<%=request.getContextPath()%>/manage/count/getDate',
					type : 'GET',
					contentType : "application/json;charset=UTF-8",
					dataType : 'text',
					success : function(data) {
						var parsedJson = $.parseJSON(data);
						$("#dateFrom").val(parsedJson.thirtyDayAgo);
						$("#dateTo").val(parsedJson.nowDate);
					}//回调看看是否有出错
				});
			});
		})
	</script>
</body>