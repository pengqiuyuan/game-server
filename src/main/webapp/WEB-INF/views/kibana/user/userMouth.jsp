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
										<span class="btn btn-primary btn-xs m-l-sm" type="button">${store.name}：当月付费用户</span>
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
							<div id="mainAdd"
								style="height: 500px; border: 0px solid #ccc; padding: 10px;"></div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="row-fluid">
				<div class="span12">
					<div class="ibox float-e-margins">
						<div class="ibox-content">
							<div id="mainTotal"
								style="height: 500px; border: 0px solid #ccc; padding: 10px;"></div>
						</div>
					</div>
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
	
	<script type="text/javascript">
	
    require.config({
        paths: {
            echarts: '../../../static/echarts/js'
        }
    });
    
    require(
        [
            'echarts',
            'echarts/chart/bar',
            'echarts/chart/line',
            'echarts/chart/map'
        ],
        function (ec) {
            var totalName = [];
            var totalDate =[];
            var totalSeries = [];
        	var td = 0;
            for(var key in ${mTotal}){
            	totalName.push(key);
            	var da = [];
            	for ( var datakey in ${mTotal}[key]) {  
            		if(td == 0){
            			totalDate.push(datakey)
            		}
            		da.push(${mTotal}[key][datakey])                    
                }  
            	totalSeries.push({name:key,type:'line',data:da});
            	td = 1;
            }
            
            
            var addName = [];
            var addDate = [];
            var addSeries = [];
            var ad = 0;
            for(var key in ${mAdd}){
            	addName.push(key);
            	var da = [];
            	for ( var datakey in ${mAdd}[key]) {  
            		if(ad == 0){
            			addDate.push(datakey)
            		}
            		da.push(${mAdd}[key][datakey])                   
                } 
            	addSeries.push({name:key,type:'line',data:da});
            	ad = 1;
            }
            //--- 折柱 ---
            var myChart = ec.init(document.getElementById('mainTotal'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '首月付费率',
                    subtext: '所选时期（当月起，一月内）的新增玩家中，在新增后的30日内进行了充值的玩家数和其占比。'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:totalName
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : totalDate
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}%'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : totalSeries
            });
            
            var myChart = ec.init(document.getElementById('mainAdd'));
            myChart.setTheme('macarons');
            myChart.setOption({
                title : {
                    text: '当日新增加的玩家，在一月内（包含新增当天）付费的帐户数（去重）',
                    subtext: '例：2015-10-19日新增的玩家中，有3人当月内付费'
                },
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:addName
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                dataZoom : {
                    show : true,
                    realtime : true,
                    start : 0,
                    end : 100
                },
                calculable : true,
                xAxis : [
                    {
                        type : 'category',
                        data : addDate
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        axisLabel : {
                            formatter: '{value}人'
                        },
                        splitArea : {show : true}
                    }
                ],
                series : addSeries
            });
            
            
            
        }
    );
    </script>
  </body>
