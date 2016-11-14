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
	</style>
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
									<%--                                     <label class="checkbox inline"> <input type="checkbox" name="serverZone"  value="所有Ios"  <c:forEach items="${sZones}" var="i" ><c:if test="${i == '所有Ios' }">checked="checked" </c:if></c:forEach> > 所有Ios </label> 
                                    <label class="checkbox inline"> <input type="checkbox" name="serverZone"  value="所有Android"  <c:forEach items="${sZones}" var="i" ><c:if test="${i == '所有Android' }">checked="checked" </c:if></c:forEach> > 所有Android</label> --%>
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
											<div class="input-append date dp3"
												data-date-format="yyyy-mm-dd">
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
											<div class="input-append date dp3"
												data-date-format="yyyy-mm-dd">
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
									</div>

									<div class="form-group">
										<a href="#" class="btn btn-primary" id="yesterday">昨日</a> <a
											href="#" class="btn btn-primary" id="sevenDayAgo">近7日</a> <a
											href="#" class="btn btn-primary" id="thirtyDayAgo">近30日</a>
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
				<div class="span12">
					<div class="ibox float-e-margins">
						<div class="ibox-content">
						    <ul class="nav nav-tabs">
								<li class="active"><a href="#a2" data-toggle="tab">2日留存</a></li>
								<li><a href="#a3" data-toggle="tab">3日留存</a></li>
								<li><a href="#a4" data-toggle="tab">4日留存</a></li>
								<li><a href="#a5" data-toggle="tab">5日留存</a></li>
								<li><a href="#a6" data-toggle="tab">6日留存</a></li>
								<li><a href="#a8" data-toggle="tab">8日留存</a></li>
								<li><a href="#a9" data-toggle="tab">9日留存</a></li>
								<li><a href="#a10" data-toggle="tab">10日留存</a></li>
								<li><a href="#a11" data-toggle="tab">11日留存</a></li>
								<li><a href="#a12" data-toggle="tab">12日留存</a></li>
								<li><a href="#a13" data-toggle="tab">13日留存</a></li>
								<li><a href="#a14" data-toggle="tab">14日留存</a></li>
								<li><a href="#a15" data-toggle="tab">15日留存</a></li>
								<li><a href="#a16" data-toggle="tab">16日留存</a></li>
								<li><a href="#a17" data-toggle="tab">17日留存</a></li>
								<li><a href="#a18" data-toggle="tab">18日留存</a></li>
								<li><a href="#a19" data-toggle="tab">19日留存</a></li>
								<li><a href="#a20" data-toggle="tab">20日留存</a></li>
								<li><a href="#a21" data-toggle="tab">21日留存</a></li>
								<li><a href="#a22" data-toggle="tab">22日留存</a></li>
								<li><a href="#a23" data-toggle="tab">23日留存</a></li>
								<li><a href="#a24" data-toggle="tab">24日留存</a></li>
								<li><a href="#a25" data-toggle="tab">25日留存</a></li>
								<li><a href="#a26" data-toggle="tab">26日留存</a></li>
								<li><a href="#a27" data-toggle="tab">27日留存</a></li>
								<li><a href="#a28" data-toggle="tab">28日留存</a></li>
								<li><a href="#a29" data-toggle="tab">29日留存</a></li>
							</ul>
							<div class="tab-content">
								<div class="tab-pane active" id="a2">
									<div id="maind2Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a3">
									<div id="maind3Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a4">
									<div id="maind4Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a5">
									<div id="maind5Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a6">
									<div id="maind6Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a7">
									<div id="maind8Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a9">
									<div id="maind9Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a10">
									<div id="maind10Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a11">
									<div id="maind11Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a12">
									<div id="maind12Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a13">
									<div id="maind13Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a14">
									<div id="maind14Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a15">
									<div id="maind15Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a16">
									<div id="maind16Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a17">
									<div id="maind17Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a18">
									<div id="maind18Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a19">
									<div id="maind19Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a20">
									<div id="maind20Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a21">
									<div id="maind21Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a22">
									<div id="maind22Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a23">
									<div id="maind23Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a24">
									<div id="maind24Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a25">
									<div id="maind25Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a26">
									<div id="maind26Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a27">
									<div id="maind27Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a28">
									<div id="maind28Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
								<div class="tab-pane" id="a29">
									<div id="maind29Day" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="row-fluid">
				<div class="span12">
					<div class="ibox float-e-margins">
						<div class="ibox-content">
						    <div id="mainNextDay" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<div class="ibox float-e-margins">
						<div class="ibox-content">
						    <div id="mainSevenDay" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12" id="choose">
					<div class="ibox float-e-margins">
						<div class="ibox-content">
						    <div id="mainThirtyDay" style="height:500px;border:0px solid #ccc;padding:10px;"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

    <script src="<%=request.getContextPath()%>/static/echarts/js/echarts.js"></script>
    <script src="<%=request.getContextPath()%>/static/flot/js/bootstrap-datepicker.js"></script>
    <%@ include file="userRetainChart.jsp"%>
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
	        		 $("#time").hide();
	        		 return true;
	        	 }
	        }else{
	        	$("#time").hide();
	        	return true;
	        }
			
		});
	
		$(function(){
			
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
					url: '<%=request.getContextPath()%>/manage/fbRetained/getDate',
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
					url: '<%=request.getContextPath()%>/manage/fbRetained/getDate',
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
					url: '<%=request.getContextPath()%>/manage/fbRetained/getDate',
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
