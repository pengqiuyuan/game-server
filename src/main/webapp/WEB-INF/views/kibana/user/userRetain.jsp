<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="huake" uri="/huake"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<meta http-equiv="Cache-Control" content="no-store" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0"/>
    <link href="<%=request.getContextPath()%>/static/flot/css/bootstrap.light.min.css" title="Light" rel="stylesheet">
    
    <link href="<%=request.getContextPath()%>/static/flot/css/font-awesome.css?v=4.3.0" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/static/flot/css/animate.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/static/flot/css/datepicker3.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/static/flot/css/style.css?v=2.1.0" rel="stylesheet">
        <!-- Mainly scripts -->
    <script src="<%=request.getContextPath()%>/static/flot/js/jquery-1.8.0.js"></script>
    <script src="<%=request.getContextPath()%>/static/flot/js/bootstrap.js"></script>
    
    <script src="${ctx}/static/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
    
    <link href="<%=request.getContextPath()%>/static/echarts/css/echartsHome.css" rel="stylesheet">
	<style type="text/css">
		.tab-content > .tab-pane,
		.pill-content > .pill-pane {
			display: block;     /* undo display:none          */
			height: 0;          /* height:0 is also invisible */
			overflow-y: hidden; /* no-overflow                */
		}
		.tab-content > .active,
		.pill-content > .active {
			height: auto; /* let the content decide it */
		}
		.table-responsive {
		    min-height: .01%;
		    overflow-x: auto;
		}
	</style>
</head>	
  <body>
	<div id="wrapper">
		<div class="wrapper wrapper-content animated fadeInRight">
			<form id="inputForm" class="orm-inline" method="get" action="#">
				<div class="row-fluid">
					<div class="span12">
						<div class="ibox float-e-margins">
							<div class="ibox-content">
								<div>
									<div class="form-group">
										<input type="hidden" id="storeId" value="${store.id}">
										<span class="btn btn-primary btn-xs m-l-sm" type="button">${store.name}：用户留存</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="row-fluid" id="chooseSPS" hidden="hidden">
					<div class="span12">
						<div class="ibox float-e-margins">
							<div class="ibox-content">
								<h3>筛选</h3>
								<h4>
									运营大区： <label class="checkbox inline"> <input
										type="checkbox" name="serverZone" value="all"
										<c:forEach items="${sZones}" var="i" ><c:if test="${i == '所有运营大区' }">checked="checked" </c:if></c:forEach>>
										所有运营大区
									</label>
									<c:forEach items="${serverZone}" var="item">
										<label class="checkbox inline"> <input type="checkbox"
											name="serverZone" value="${item.id }"
											<c:forEach items="${sZones}" var="i" ><c:if test="${i == item.serverName }">checked="checked" </c:if></c:forEach>>
											${item.serverName }
										</label>
									</c:forEach>
								</h4>
								<h4>
									渠道：
									<c:forEach items="${platForm}" var="item">
										<label class="checkbox inline"> <input type="checkbox"
											name="platForm" value="${item.pfId }"
											<c:forEach items="${pForms}" var="i" ><c:if test="${i == item.pfName }">checked="checked" </c:if></c:forEach>>
											${item.pfName }
										</label>
									</c:forEach>
								</h4>
								<h4>
									服务器：
									<c:forEach items="${server}" var="item">
										<label class="checkbox inline"> <input type="checkbox"
											name="server" value="${item.serverId }"
											<c:forEach items="${svs}" var="i" ><c:if test="${i == item.serverId }">checked="checked" </c:if></c:forEach>>
											${item.serverId }
										</label>
									</c:forEach>
								</h4>
								<div class="form-group">
									<button class="btn btn-success " type="submit">
										<i class="fa fa-check"></i>&nbsp;&nbsp;<span class="bold">确定</span>
									</button>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="row-fluid">
					<div class="span6">
						<div class="ibox float-e-margins">
							<div class="ibox-content">
								<div>
									<h3>
										日期选择器
										<div class="ibox-tools">
											<label class="label label-primary" id="chooseBtn">添加筛选条件</label>
										</div>
									</h3>
									<div class="control-group">
										<label class="control-label" for=beginD>活动时间：
											<div class="input-append date dp3" data-date-format="yyyy-mm-dd">
												<c:if test="${not empty param.search_EQ_dateFrom}">
													<input size="16" type="text" id="dateFrom"
														value="${param.search_EQ_dateFrom == dateFrom  ? dateFrom : param.search_EQ_dateFrom }"
														name="search_EQ_dateFrom">
												</c:if>
												<c:if test="${empty param.search_EQ_dateFrom}">
													<input size="16" type="text" id="dateFrom"
														value="${dateFrom}" name="search_EQ_dateFrom">
												</c:if>
												<span class="add-on" style="display: none"></span>
											</div>
										</label>
									</div>

									<div class="control-group">
										<label class="control-label" for="endD">结束时间：
											<div class="input-append date dp3" data-date-format="yyyy-mm-dd">
												<c:if test="${not empty param.search_EQ_dateFrom}">
													<input size="16" type="text" id="dateTo"
														value="${param.search_EQ_dateTo == dateTo  ? dateTo : param.search_EQ_dateTo }"
														name="search_EQ_dateTo">
												</c:if>
												<c:if test="${empty param.search_EQ_dateFrom}">
													<input size="16" type="text" id="dateTo" value="${dateTo}"
														name="search_EQ_dateTo">
												</c:if>
												<span class="add-on" style="display: none"></span>
											</div>
										</label>
										<div id="time"
											style="display: none; width: 20%; margin-top: 10px;">
											<button data-dismiss="alert" class="close">×</button>
											结束时间不能小于开始时间
										</div>
										<div id="time1"
											style="display: none; width: 20%; margin-top: 10px;">
											<button data-dismiss="alert" class="close">×</button>
											一次查询的时间区间不能超过30天
										</div>
									</div>

									<div class="form-group">
										<a href="#" class="btn btn-primary" id="sevenDayAgo">近7日</a> 
										<a href="#" class="btn btn-primary" id="thirtyDayAgo">近30日</a> 
										<button class="btn btn-success" id="sub" type="submit">
											<i class="fa fa-check"></i>&nbsp;&nbsp;<span class="bold">确定</span>
										</button>
									</div>
								</div>
							</div>
						</div>
					</div>

					<div class="span6" id="choose">
						<div class="ibox float-e-margins">
							<div class="ibox-content">
								<div>
									<h3>筛选</h3>
									<h4>
										运营大区：
										<c:forEach items="${sZones}" var="item">
											<a href="#" class="file-control">${item}</a>
										</c:forEach>
									</h4>
									<h4>
										渠道：
										<c:forEach items="${pForms}" var="item">
											<a href="#" class="file-control">${item}</a>
										</c:forEach>
									</h4>
									<h4>
										服务器：
										<c:forEach items="${svs}" var="item">
											<a href="#" class="file-control">${item}</a>
										</c:forEach>
									</h4>
									<div class="form-group">
										<button class="btn btn-success " type="submit">
											<i class="fa fa-check"></i>&nbsp;&nbsp;<span class="bold">确定</span>
										</button>
									</div>
								</div>
							</div>
						</div>
					</div>

				</div>
			</form>		
			<div class="row-fluid">
					<div class="table-responsive ibox-content span12">
							<p><code>次日~10日留存（留存|登录|新增）</code></p>
							<table class="table table-striped table-bordered table-condensed" id="table">
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
								</tbody>
							</table>
					</div>
			</div>	
			<div class="row-fluid">
					<div class="table-responsive ibox-content span12">
							<p><code>11日~20日留存（留存|登录|新增）</code></p>
							<table class="table table-striped table-bordered table-condensed" id="table">
								<thead>
									<tr>
										<th title="时间">时间</th>
										<th title="新增">新增</th>
										<th title="12日">${switchTable == '2'? '12日登录':'11日留存'}</th>
										<th title="13日">${switchTable == '2'? '13日登录':'12日留存'}</th>
										<th title="14日">${switchTable == '2'? '14日登录':'13日留存'}</th>
										<th title="15日">${switchTable == '2'? '15日登录':'14日留存'}</th>
										<th title="16日">${switchTable == '2'? '16日登录':'15日留存'}</th>
										<th title="17日">${switchTable == '2'? '17日登录':'16日留存'}</th>
										<th title="18日">${switchTable == '2'? '18日登录':'17日留存'}</th>
										<th title="19日">${switchTable == '2'? '19日登录':'18日留存'}</th>
										<th title="20日">${switchTable == '2'? '20日登录':'19日留存'}</th>
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
								</tbody>
							</table>
					</div>
			</div>	
			<div class="row-fluid">
					<div class="table-responsive ibox-content span12">
							<p><code>21日~30日留存（留存|登录|新增）</code></p>
							<table class="table table-striped table-bordered table-condensed" id="table">
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
								</tbody>
							</table>
					</div>
			</div>	
			
		</div>
	</div>

    <script src="<%=request.getContextPath()%>/static/echarts/js/echarts.js"></script>
    <script src="<%=request.getContextPath()%>/static/flot/js/bootstrap-datepicker.js"></script>
	<script type="text/javascript">
		$("#chooseBtn").click(function(){
			if($("#chooseSPS").attr("hidden")=="hidden"){ 
				$("#chooseSPS").removeAttr("hidden"); 
			}else{
				$("#chooseSPS").attr("hidden","hidden"); 
			}
		});
	
		$(".btn-success").click(function(){
			var doingDate=$("#dateFrom").val();
	        var endDoingDate=$("#dateTo").val();
	        var startTime = new Date(doingDate).getTime();
	        var endTime = new Date(endDoingDate).getTime();
	         if(!endDoingDate.length==0){
	        	 if(startTime>endTime){
	             	$("#time").show();
	             	return false;
	        	 }else{
	        		 if(endTime-30*24*3600*1000 > startTime){
	        			 $("#time1").show();
		        		 return false;
	        		 }else{
	 	             	$("#time1").hide();
		             	return true;
	        		 }
	        	 }
	        }else{
	        	$("#time1").hide();
	        	$("#time").hide();
	        	return true;
	        }
			
		});
	
		$(function(){
			//单选组事件
		    $(":checkbox").bind("click", function () {
		        $(":checkbox").prop("checked", false);  
		        $(this).prop("checked", true);  
		    })
		    
			$("#inputForm").validate({
				rules:{
					search_EQ_dateFrom:{
						required:true
					},
					search_EQ_dateTo:{
						required:true
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
			$("#thirtyDayAgo").click(function(){
				$.ajax({                                               
					url: '<%=request.getContextPath()%>/manage/count/getDate',
					type: 'GET',
					contentType: "application/json;charset=UTF-8",		
					dataType: 'text',
					success: function(data){
						var parsedJson = $.parseJSON(data);
						$("#dateFrom").val(parsedJson.thirtyDayAgo);
						$("#dateTo").val(parsedJson.nowDate);
					}//回调看看是否有出错
				});
			});
		})
		
	</script>
  </body>
