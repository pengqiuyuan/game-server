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
      <link rel="stylesheet" href="css/bootstrap.light.min.css" title="Light">

    <link href="http://hplus.oss.aliyuncs.com/font-awesome/css/font-awesome.css?v=4.3.0" rel="stylesheet">
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
								<h3>
									日期选择器
									<div class="ibox-tools">
		                                 <label class="label label-primary" id="chooseBtn">添加筛选条件</label>
		                            </div>
								</h3>
								
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
				
				<div class="span9" id="choose">
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
    <script src="<%=request.getContextPath()%>/static/flot/js/flot-demo.js"></script>
    <script src="<%=request.getContextPath()%>/static/flot/js/bootstrap-datepicker.js"></script>
    

	<script type="text/javascript">
		$(function(){
			$("#chooseBtn").click(function(){
				if($("#choose").css("display")=="none"){
					$("#choose").show();
					$("#chooseBtn").text("关闭筛选条件");
				}else if($("#choose").css("display")=="block"){
					$("#choose").hide();
					$("#chooseBtn").text("添加筛选条件");
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
		    
            var data1 = {
            		"companys": [
            			{
            				"date": "2015-04-12",
            				"nextDayRetain": "65%",
            				"sevenDayRetain": "30%",
            				"thirtyDayRetain": "12%"
            			},
            			{
            				"date": "2015-04-13",
            				"nextDayRetain": "78%",
            				"sevenDayRetain": "32%",
            				"thirtyDayRetain": "15%"
            			},
            			{
            				"date": "2015-04-15",
            				"nextDayRetain": "56%",
            				"sevenDayRetain": "26%",
            				"thirtyDayRetain": "11%"
            			}
            		]
            	}
            var messageElement = $("#retainTable").find('tbody');
            $.each(data1.companys,function(i,val){
            	var content = '<tr class="gradeX"><td>'+val.date+'</td>'
            					  +'<td>'+val.nextDayRetain+'</td>'
            					  +'<td>'+val.sevenDayRetain+'</td>'
            					  +'<td class="center">'+val.thirtyDayRetain+'</td>';
            	messageElement.append(content);
            });
            $(".dataTables-example").dataTable();
		    
		})
		
	</script>
  </body>
