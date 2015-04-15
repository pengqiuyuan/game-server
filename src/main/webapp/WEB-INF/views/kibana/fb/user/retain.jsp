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
	<meta http-equiv="Expires" content="0" />
    <link rel="stylesheet" href="css/bootstrap.light.min.css" title="Light">
    <link href="http://hplus.oss.aliyuncs.com/font-awesome/css/font-awesome.css?v=4.3.0" rel="stylesheet">
    <link href="http://hplus.oss.aliyuncs.com/css/animate.css" rel="stylesheet">
    <link href="http://hplus.oss.aliyuncs.com/css/plugins/datapicker/datepicker3.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/static/flot/css/style.css?v=2.1.0" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/static/flot/css/bootstrap-select.min.css" rel="stylesheet">
    
        <!-- Mainly scripts -->
    <script src="<%=request.getContextPath()%>/static/flot/js/jquery-1.8.0.js"></script>
    <script src="<%=request.getContextPath()%>/static/flot/js/bootstrap.js"></script>
    <script src="<%=request.getContextPath()%>/static/flot/js/bootstrap-select.min.js"></script>
    
  </head>

  <body>
      <div id="wrapper">
            <div class="row wrapper border-bottom white-bg page-heading">
                <div class="span10">
                    <h2>Flot图表</h2>
                </div>
            </div>

		<div class="wrapper wrapper-content animated fadeInRight">
					<div class="row-fluid">
				<div class="span3">
					<div class="ibox float-e-margins">
						<div class="ibox-content">
							<div>
								<h3>日期选择器</h3>
								<div class="input-append date dp3" data-date-format="yyyy-mm-dd">
									<input class="span" size="16" type="text" id="dateFrom" value="">
									<span class="add-on"><i class="icon-th"></i></span>
								</div>
								<div class="input-append date">
									<span class="add-on">到</span>
								</div>
								<div class="input-append date dp3" data-date-format="yyyy-mm-dd">
									<input class="span" size="16" type="text" id="dateTo" value="">
									<span class="add-on"><i class="icon-th"></i></span>
								</div>
								<div class="form-group">
									<button class="btn btn-primary" id="yesterday">昨日</button>
									<button class="btn btn-primary" id="sevenDayAgo">近7日</button>
									<button class="btn btn-primary" id="thirtyDayAgo">近30日</button>
									<button class="btn btn-success " type="button"><i class="fa fa-check"></i>&nbsp;&nbsp;<span class="bold">确定</span>
                                </button>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="span9">
					<div class="ibox float-e-margins">
						<div class="ibox-content">
							<div>
								<h3>筛选</h3>
								<div class="form-group">
									<div class="btn-group">
										<select id="category" class="selectpicker show-menu-arrow">
											 <option value="">选择筛选类型</option>
										     <option value="serverZone">运营大区</option>
										     <option value="platForm">渠道</option>
										  </select>
									</div>
									<div class="btn-group" id="divServerResult">

									</div>
									<div class="btn-group" id="divpfResult">

									</div>
								</div>
								<div class="form-group">
									<button class="btn btn-success " type="button"><i class="fa fa-check"></i>&nbsp;&nbsp;<span class="bold">确定</span>
                                </button>

							</div>
						</div>
					</div>
				</div>
			</div>
				
			</div>
		
			<div class="row-fluid">
                     <div class="span6">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <h5>折线图</h5>
                                <div class="ibox-tools">
                                    <a class="collapse-link">
                                        <i class="fa fa-chevron-up"></i>
                                    </a>
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
                    <div class="span6">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <h5>柱状图 <small>自定义颜色</small></h5>
                                <div class="ibox-tools">
                                    <a class="collapse-link">
                                        <i class="fa fa-chevron-up"></i>
                                    </a>
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
                                    <div class="flot-chart-content" id="flot-bar-chart"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span6">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <h5>饼状图</h5>
                                <div class="ibox-tools">
                                    <a class="collapse-link">
                                        <i class="fa fa-chevron-up"></i>
                                    </a>
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
                                    <div class="flot-chart-content" id="flot-pie-chart"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="span6">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <h5>动态图</h5>
                                <div class="ibox-tools">
                                    <a class="collapse-link">
                                        <i class="fa fa-chevron-up"></i>
                                    </a>
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
                                    <div class="flot-chart-content" id="flot-line-chart-moving"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row-fluid">
                    <div class="span12">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <h5>多轴线图表示例</h5>
                                <div class="ibox-tools">
                                    <a class="collapse-link">
                                        <i class="fa fa-chevron-up"></i>
                                    </a>
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
                                    <div class="flot-chart-content" id="flot-line-chart-multi"></div>
                                </div>
                            </div>
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
    <!-- Flot demo data -->
    <script src="<%=request.getContextPath()%>/static/flot/js/flot-demo.js"></script>
    
    <script src="http://hplus.oss.aliyuncs.com/js/plugins/datapicker/bootstrap-datepicker.js"></script>

	<script type="text/javascript">
		$(function(){
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
		    
		    $("#category").change(function(){
		    	var category = $("#category").val()
		    	if(category=="serverZone"){
					$.ajax({                                               
						url: '<%=request.getContextPath()%>/manage/count/findServerZone',
						type: 'GET',
						contentType: "application/json;charset=UTF-8",		
						dataType: 'text',
						success: function(data){
							var parsedJson = $.parseJSON(data);
							$("#divServerResult").empty();
							$("#divpfResult").empty();
							$("#divServerResult").append("<select class='selectpicker' id='serverResult' multiple data-selected-text-format='count>3' ></select>");
							jQuery.each(parsedJson, function(index, itemData) {
								$("#serverResult").append("<option>"+itemData.serverName+"</option>");
			  				});
							$('#serverResult').selectpicker('refresh');
						}//回调看看是否有出错
					});
		    	}else if(category=="platForm"){
					$.ajax({                                               
						url: '<%=request.getContextPath()%>/manage/count/findServerZone',
						type: 'GET',
						contentType: "application/json;charset=UTF-8",		
						dataType: 'text',
						success: function(data){
							var parsedJson = $.parseJSON(data);
							$("#divServerResult").empty();
							$("#divServerResult").append("<select class='selectpicker' id='pfresult'><option value=''>选择大区</option></select>");
							jQuery.each(parsedJson, function(index, itemData) {
								$("#pfresult").append("<option value='"+itemData.id+"'>"+itemData.serverName+"</option>");
			  				});
							$('#pfresult').selectpicker('refresh');
							
							$("#pfresult").change(function(){
								var pf_server = $("#pfresult").val();
								$.ajax({                                               
									url: '<%=request.getContextPath()%>/manage/count/findPlatFormByServerId?serverId='+pf_server,
									type: 'GET',
									contentType: "application/json;charset=UTF-8",		
									dataType: 'text',
									success: function(data){
										var parsedJson = $.parseJSON(data);
										$("#divpfResult").empty();
										$("#divpfResult").append("<select class='selectpicker' id='pfByserverIdResult' multiple data-selected-text-format='count>3' ></select>");
										jQuery.each(parsedJson, function(index, itemData) {
											$("#pfByserverIdResult").append("<option>"+itemData.pfName+"</option>");
						  				});
										$('#pfByserverIdResult').selectpicker('refresh');
									}//回调看看是否有出错
								});
							});		
							
						}//回调看看是否有出错
					});
		    	}else if(category==""){
		    		$("#divServerResult").empty();
		    		$("#divpfResult").empty();
		    	}
		    });
		    
		})
	</script>
  </body>
