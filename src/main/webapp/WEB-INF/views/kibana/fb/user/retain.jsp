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
    <link href="<%=request.getContextPath()%>/static/flot/css/bootstrap-select.min.css" rel="stylesheet">
        <!-- Mainly scripts -->
    <script src="<%=request.getContextPath()%>/static/flot/js/jquery-1.8.0.js"></script>
    <script src="<%=request.getContextPath()%>/static/flot/js/bootstrap.js"></script>
    <script src="<%=request.getContextPath()%>/static/flot/js/bootstrap-select.min.js"></script>
    
        <!-- Data Tables -->
    <link href="<%=request.getContextPath()%>/static/flot/css/dataTables.bootstrap.css" rel="stylesheet">
            <!-- Data Tables -->
    <script src="<%=request.getContextPath()%>/static/flot/js/jquery.dataTables.js"></script>
    <script src="<%=request.getContextPath()%>/static/flot/js/dataTables.bootstrap.js"></script>
    <script src="${ctx}/static/jquery-validation/1.11.1/jquery.validate.min.js" type="text/javascript"></script>
    <style type="text/css">
	    form {
		  margin: 0 0 0px;
		}
		.control-label {
		  float: left;
		  padding-top: 5px;
		  text-align: right;
		}
		.well {	
		  margin-left: -150px;
		}
    </style>
  </head>

  <body>
      <div id="wrapper">
      		<input type="hidden" id="datenext" value="${datenext}" >
      		<input type="hidden" id="dateSeven" value="${dateSeven}" >
      		<input type="hidden" id="datethirty" value="${datethirty}" >
      		<input type="hidden" id="table" value="${table}" >
            <div class="row wrapper border-bottom white-bg page-heading">
                <div class="span10">
                    <h2>Flot图表</h2>
                </div>
            </div>

		<div class="wrapper wrapper-content animated fadeInRight">

			<form id="inputForm" class="orm-inline"  method="get" action="#">
			<div class="row-fluid">
					<div class="span12">
						<div class="ibox float-e-margins">
							<div class="ibox-content">
								<div>
									<div class="form-group">
										<input type="hidden" id="storeId"  value="${store.id}" > 
										<span class="btn btn-primary btn-xs m-l-sm" type="button">${store.name}</span>
										<div class="btn-group">
											<select class="selectpicker show-menu-arrow">
											     <option value="serverZone">运营大区</option>
											</select>
										</div>
										<div class="btn-group">
											<select class='selectpicker' data-width="100%" name="search_EQ_serverZone" id="serverZone">
													<option value="all">IOS官方、IOS越狱、安卓官方、安卓越狱</option>
													<c:forEach items="${serverZone}" var="it" >
														<option value="${it.id}" ${param.search_EQ_serverZone == it.id ? 'selected' : '' }>
															${it.serverName}
														</option>
													</c:forEach>
											</select>
										</div>
									</div>
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
									<label class="control-label" for=beginD>活动时间：</label>
									<div class="input-append date dp3" data-date-format="yyyy-mm-dd">
										<c:if test="${not empty param.search_EQ_dateFrom}">
											<input  size="16" type="text" id="dateFrom" value="${param.search_EQ_dateFrom == dateFrom  ? dateFrom : param.search_EQ_dateFrom }" name="search_EQ_dateFrom">
										</c:if>
										<c:if test="${empty param.search_EQ_dateFrom}">
											<input  size="16" type="text" id="dateFrom" value="${dateFrom}" name="search_EQ_dateFrom">
										</c:if> 
										<span class="add-on" style="display: none"></span>
									</div>
								</div>
								
								<div class="control-group">
									<label class="control-label" for="endD">结束时间：</label>
									<div class="input-append date dp3" data-date-format="yyyy-mm-dd">
										<c:if test="${not empty param.search_EQ_dateFrom}">
											<input  size="16" type="text" id="dateTo" value="${param.search_EQ_dateTo == dateTo  ? dateTo : param.search_EQ_dateTo }" name="search_EQ_dateTo">
										</c:if>
										<c:if test="${empty param.search_EQ_dateFrom}">
											<input  size="16" type="text" id="dateTo" value="${dateTo}" name="search_EQ_dateTo">
										</c:if>
										<span class="add-on" style="display: none"></span>
									</div>
									<div id="time" style="display: none;width: 20%; margin-top: 10px;"><button data-dismiss="alert" class="close">×</button>结束时间不能小于开始时间</div>
								</div>
								
								<div class="form-group">
									<a href="#" class="btn btn-primary" id="yesterday">昨日</a>
									<a href="#" class="btn btn-primary" id="sevenDayAgo">近7日</a>
									<a href="#" class="btn btn-primary" id="thirtyDayAgo">近30日</a>
									<button class="btn btn-success" id="sub" type="submit"><i class="fa fa-check"></i>&nbsp;&nbsp;<span class="bold">确定</span>
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
								<h3>筛选	
								<c:if test="${not empty param.search_EQ_value}">
									<c:if test="${param.search_EQ_category=='platForm'}">
										<span id="show" class="btn btn-primary btn-xs m-l-sm" type="button">查询渠道为：${param.search_EQ_value}</span>
									</c:if>
									<c:if test="${param.search_EQ_category=='server'}">
										<span id="show" class="btn btn-primary btn-xs m-l-sm" type="button">查询服务器为：${param.search_EQ_value}</span>
									</c:if>
								</c:if>
								</h3>
								
								<div class="form-group">
									<div class="btn-group">
										<select id="category" class="selectpicker show-menu-arrow" name="search_EQ_category">
											 <option value="">选择筛选类型</option>
										     <option value="platForm">渠道</option>
										     <option value="server">服务器</option>
										  </select>
									</div>
									<div class="btn-group" id="divpfResult">

									</div>
								</div>
								<div class="form-group">
									<button class="btn btn-success " type="submit"><i class="fa fa-check"></i>&nbsp;&nbsp;<span class="bold">确定</span>
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
	                            <div class="ibox-title">
	                                <h5>玩家留存</h5>
	                                <div class="ibox-tools">
										<span>
					                            <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#" >
					                                <i class="fa fa-envelope"></i>
					                            </a>
					                            <div class="dropdown-menu dropdown-messages well" >
													<h4>N日留存率</h4>
													<font>某日新增的玩家/设备中，在该日后的第N日中，还有进行游戏的玩家/设备比例。例 如：5月3日新增玩家为100人，这100人中有24人在5月10日这一天内还有玩过游戏，5月3日的7日留存 率=24/100=24%</font>
					                            </div>
										</span>
	                                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
	                                        <i class="fa fa-wrench"></i>
	                                    </a>
	                                    <ul class="dropdown-menu dropdown-user">
	                                        <li><a href="#">选项1</a>
	                                        </li>
	                                        <li><a href="#">选项2</a>
	                                        </li>
	                                    </ul>
	                                    <a class="close-link">
	                                        <i class="fa fa-times"></i>
	                                    </a>
	                                </div>
	                            </div>
	                            <div class="ibox-content">
	
	                                <div class="flot-chart">
	                                    <div class="flot-chart-content" id="flot-line-chart"></div>
	                                </div>
	                            </div>
	                        </div>
	                    </div>
	             </div>
	             
	             <div class="row-fluid">
                    <div class="span12">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <h5>基本 <small>分类，查找</small></h5>
                                <div class="ibox-tools">
                                    <a class="collapse-link">
                                        <i class="fa fa-chevron-up"></i>
                                    </a>
                                    <a class="dropdown-toggle" data-toggle="dropdown" href="table_data_tables.html#">
                                        <i class="fa fa-wrench"></i>
                                    </a>
                                    <ul class="dropdown-menu dropdown-user">
                                        <li><a href="table_data_tables.html#">选项1</a>
                                        </li>
                                        <li><a href="table_data_tables.html#">选项2</a>
                                        </li>
                                    </ul>
                                    <a class="close-link">
                                        <i class="fa fa-times"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="ibox-content">
                                <table class="table table-striped table-bordered table-hover dataTables-example" id="retainTable">
                                    <thead>
                                        <tr>
                                            <th>日期</th>
                                            <th>次日留存率(%)</th>
                                            <th>7日留存率(%)</th>
                                            <th>30日留存率(%)</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    	<c:forEach items="${table}" var="it" >
				  							<tr class="gradeX"><td>${it.value.date}</td><td>${it.value.nextDayRetain}</td><td>${it.value.sevenDayRetain}</td><td class="center">${it.value.thirtyDayRetain}</td></tr>
										</c:forEach>
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <th>日期</th>
                                            <th>次日留存率(%)</th>
                                            <th>7日留存率(%)</th>
                                            <th>30日留存率(%)</th>
                                        </tr>
                                    </tfoot>
                                </table>

                            </div>
                        </div>
                    </div>
                </div> 


            </div>
                        
      </div>

    <!-- Flot -->
    <script src="<%=request.getContextPath()%>/static/flot/js/jquery.flot.js"></script>
    <script src="<%=request.getContextPath()%>/static/flot/js/jquery.flot.tooltip.min.js"></script>
    <script src="<%=request.getContextPath()%>/static/flot/js/jquery.flot.resize.js"></script>
    <script src="<%=request.getContextPath()%>/static/flot/js/jquery.flot.pie.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/static/flot/js/jquery.flot.axislabels.js"></script>
    <!-- Flot demo data -->

    <script src="<%=request.getContextPath()%>/static/flot/js/bootstrap-datepicker.js"></script>
    
	<script type="text/javascript">
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
			
			$("#serverZone").change(function(){
				$("#show").text("无筛选条件");
	    		$("#divpfResult").empty();
	    		$("#category option[value='']").attr("selected", true);
	    		$('#category').selectpicker('refresh');
			});
		    
		    $("#category").change(function(){
		    	var serverZoneValue=$("#serverZone").val();
		    	var serverZoneText=$("#serverZone  option:selected").text();
		    	var category = $("#category").val()
				var storeId = $("#storeId").val();

		    	if($("#serverZone").val()=="all"){
					if(category=="platForm"){
						$.ajax({                                               
							url: '<%=request.getContextPath()%>/manage/fbRetained/findPlatForm',
							type: 'GET',
							contentType: "application/json;charset=UTF-8",		
							dataType: 'text',
							success: function(data){
								var parsedJson = $.parseJSON(data);
								$("#divpfResult").empty();
								if(parsedJson!=""){
									$("#divpfResult").append("<select class='selectpicker' id='pfByserverIdResult' name='search_EQ_value'></select>");
									jQuery.each(parsedJson, function(index, itemData) {
										$("#pfByserverIdResult").append("<option value='"+itemData.pfName+"'>"+itemData.pfName+"</option>");
					  				});
									$('#pfByserverIdResult').selectpicker('refresh');
								}
							}//回调看看是否有出错
						});
			    	}else if(category=="server"){
						$.ajax({                                               
							url: '<%=request.getContextPath()%>/manage/fbRetained/findServerByStoreId?storeId='+storeId,
							type: 'GET',
							contentType: "application/json;charset=UTF-8",		
							dataType: 'text',
							success: function(data){
								var parsedJson = $.parseJSON(data);
								$("#divpfResult").empty();
								if(parsedJson!=""){
									$("#divpfResult").append("<select class='selectpicker' id='serverByStoreIdResult' name='search_EQ_value'></select>");
									jQuery.each(parsedJson, function(index, itemData) {
										$("#serverByStoreIdResult").append("<option value='"+itemData.serverId+"'>"+itemData.serverId+"</option>");
					  				});
									$('#serverByStoreIdResult').selectpicker('refresh');
								}
							}//回调看看是否有出错
						});
			    	}else if(category==""){
			    		$("#divpfResult").empty();
			    	}
		    	}else{
					if(category=="platForm"){
						$.ajax({                                               
							url: '<%=request.getContextPath()%>/manage/fbRetained/findPlatFormByServerId?serverId='+serverZoneValue,
							type: 'GET',
							contentType: "application/json;charset=UTF-8",		
							dataType: 'text',
							success: function(data){
								var parsedJson = $.parseJSON(data);
								$("#divpfResult").empty();
								if(parsedJson!=""){
									$("#divpfResult").append("<select class='selectpicker' id='pfByserverIdResult' name='search_EQ_value'></select>");
									jQuery.each(parsedJson, function(index, itemData) {
										$("#pfByserverIdResult").append("<option value='"+itemData.pfName+"'>"+itemData.pfName+"</option>");
					  				});
									$('#pfByserverIdResult').selectpicker('refresh');
								}
							}//回调看看是否有出错
						});
			    	}else if(category=="server"){
						$.ajax({                                               
							url: '<%=request.getContextPath()%>/manage/fbRetained/findServerByStoreIdAndServerZoneId?storeId='+storeId+'&serverZoneId='+serverZoneValue,
							type: 'GET',
							contentType: "application/json;charset=UTF-8",		
							dataType: 'text',
							success: function(data){
								var parsedJson = $.parseJSON(data);
								$("#divpfResult").empty();								
								if(parsedJson!=""){
									$("#divpfResult").append("<select class='selectpicker' id='serverByStoreIdResult' name='search_EQ_value'></select>");
									jQuery.each(parsedJson, function(index, itemData) {
										$("#serverByStoreIdResult").append("<option value='"+itemData.serverId+"'>"+itemData.serverId+"</option>");
					  				});
									$('#serverByStoreIdResult').selectpicker('refresh');
								}
							}//回调看看是否有出错
						});
			    	}else if(category==""){
			    		$("#divpfResult").empty();
			    	}
		    		
		    	}

		    });
		    
			
            var dateNextDay=$.parseJSON($("#datenext").val())
            var dateSevenDay=$.parseJSON($("#dateSeven").val())
            var dateThirtyDay=$.parseJSON($("#datethirty").val())

            var barOptions = {
                    series: {
                        lines: {
                            show: true,
                            lineWidth: 2,
                            fill: true,
                            fillColor: {
                                colors: [{
                                    opacity: 0.0
                                }, {
                                    opacity: 0.0
                                }]
                            }
                        }
                    },
                    yaxis: {
                        axisLabel: "用户留存率(%)"
                    },
                    xaxes: [{
                        mode: 'time',
                        timeformat: "%m/%d"
                    }],
                    colors: ["#1ab394"],
                    grid: {
                        color: "#999999",
                        hoverable: true,
                        clickable: true,
                        tickColor: "#D4D4D4",
                        borderWidth:0
                    },
                    legend: {
                        show: true,            
                        position: 'sw'
                    },
                    tooltip: true,
                    tooltipOpts: {
                        content: "日期: %x, %s为: %y %"
                    }
                };
          		var dateNextDay = {
                    label: "次日留存率",
                    points: {  show: true,radius: 3 },
                    lines: { show: true },
                    data: dateNextDay
                };
                var dateSevenDay = {
                    label: "7日留存率",
                    points: {  show: true,radius: 3 },
                    lines: { show: true },
                    data: dateSevenDay
                };
                var dateThirtyDay = {
                        label: "30日留存率",
                        points: {  show: true ,radius: 3 },
                        lines: { show: true },
                        data: dateThirtyDay
                    };
                $.plot($("#flot-line-chart"), [dateNextDay,dateSevenDay,dateThirtyDay], barOptions);
                
                $(".dataTables-example").dataTable();
	    
		})
		
	</script>
  </body>
