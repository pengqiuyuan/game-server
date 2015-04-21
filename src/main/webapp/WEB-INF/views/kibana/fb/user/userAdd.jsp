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
      		<input type="hidden" id="userTotal" value="${userTotal}" >
      		<input type="hidden" id="userAdd" value="${userAdd}" >

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
								<c:if test="${not empty param.search_EQ_platForm_value}">
									<c:if test="${param.search_EQ_category=='platForm'}">
										<span id="show" class="btn btn-primary btn-xs m-l-sm" type="button">查询渠道为：${param.search_EQ_platForm_value}</span>
									</c:if>
								</c:if>
								<c:if test="${not empty param.search_EQ_server_value}">
									<c:if test="${param.search_EQ_category=='server'}">
										<span id="show" class="btn btn-primary btn-xs m-l-sm" type="button">查询服务器为：${param.search_EQ_server_value}</span>
									</c:if>
								</c:if>
								</h3>
								
								<!-- 选择筛选  开始页面-->
								<c:if test="${empty param.search_EQ_category}">
									<div class="form-group">
										<select class="selectpicker show-menu-arrow t1" name="search_EQ_category" id="t1" >	
											 <option value="platForm">渠道</option>	
										</select>
										<select class="selectpicker show-menu-arrow t2" name=search_EQ_platForm_value id="t2" data-size="30">	
											 <option value="">选择渠道</option>	
											 <c:forEach items="${platForm}" var="item" >
													<option value="${item.pfName }" ${param.search_EQ_platForm_value == item.pfName ? 'selected' : '' }>${item.pfName}</option>
											 </c:forEach>
										</select>	
										<span class="btn btn-primary btn-xs m-l-sm" type="button" id='psfilter'>切换筛选</span>
									</div>
									<div class="form-group">
										<select class="selectpicker show-menu-arrow t3" name="search_EQ_category" id="t3" disabled="disabled">	
											 <option value="server">服务器</option>	
										</select>
										<select class="selectpicker show-menu-arrow t4" name="search_EQ_server_value" id="t4" disabled="disabled" data-size="30">	
											    <option value="">选择服务器</option>	
												<c:forEach items="${server}" var="item" >
														<option value="${item.serverId }" ${param.search_EQ_server_value == item.serverId ? 'selected' : '' }>${item.serverId}</option>
												</c:forEach>
										</select>	
									</div>								
								</c:if>
								<!-- 选择筛选  search_EQ_category 为渠道-->
								<c:if test="${param.search_EQ_category=='platForm'}">
									<div class="form-group">
										<select class="selectpicker show-menu-arrow t1" name="search_EQ_category" id="t1" >	
											 <option value="platForm">渠道</option>	
										</select>
										<select class="selectpicker show-menu-arrow t2" name="search_EQ_platForm_value" id="t2" data-size="30">	
											 <option value="">选择渠道</option>	
											 <c:forEach items="${platForm}" var="item" >
													<option value="${item.pfName }" ${param.search_EQ_platForm_value == item.pfName ? 'selected' : '' }>${item.pfName}</option>
											 </c:forEach>
										</select>	
										<span class="btn btn-primary btn-xs m-l-sm" type="button" id='psfilter'>切换筛选</span>
									</div>
									<div class="form-group">
										<select class="selectpicker show-menu-arrow t3" name="search_EQ_category" id="t3" disabled="disabled">	
											 <option value="server">服务器</option>	
										</select>
										<select class="selectpicker show-menu-arrow t4" name="search_EQ_server_value" id="t4" disabled="disabled" data-size="30">	
											    <option value="">选择服务器</option>	
												<c:forEach items="${server}" var="item" >
														<option value="${item.serverId }" ${param.search_EQ_server_value == item.serverId ? 'selected' : '' }>${item.serverId}</option>
												</c:forEach>
										</select>	
									</div>								
								</c:if>
								<!-- 选择筛选  search_EQ_category 为服务器-->
								<c:if test="${param.search_EQ_category=='server'}">
									<div class="form-group">
										<select class="selectpicker show-menu-arrow t1" name="search_EQ_category" id="t1"  disabled="disabled">	
											 <option value="platForm">渠道</option>	
										</select>
										<select class="selectpicker show-menu-arrow t2" name="search_EQ_platForm_value" id="t2"  disabled="disabled" data-size="30">	
											 <option value="">选择渠道</option>	
											 <c:forEach items="${platForm}" var="item" >
													<option value="${item.pfName }" ${param.search_EQ_platForm_value == item.pfName ? 'selected' : '' }>${item.pfName}</option>
											 </c:forEach>
										</select>	
										<span class="btn btn-primary btn-xs m-l-sm" type="button" id='psfilter'>切换筛选</span>
									</div>
									<div class="form-group">
										<select class="selectpicker show-menu-arrow t3" name="search_EQ_category" id="t3">	
											 <option value="server">服务器</option>	
										</select>
										<select class="selectpicker show-menu-arrow t4" name="search_EQ_server_value" id="t4" data-size="30">	
											    <option value="">选择服务器</option>	
												<c:forEach items="${server}" var="item" >
														<option value="${item.serverId }" ${param.search_EQ_server_value == item.serverId ? 'selected' : '' }>${item.serverId}</option>
												</c:forEach>
										</select>	
									</div>								
								</c:if>
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
	                     <div class="span6">
	                        <div class="ibox float-e-margins">
	                            <div class="ibox-title">
	                                <h5>新增玩家</h5>
	                                <div class="ibox-tools">
										<span>
					                            <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#" >
					                                <i class="fa fa-envelope"></i>
					                            </a>
					                            <div class="dropdown-menu dropdown-messages well" >
													<h4>新增玩家</h4>
													<font>当日新增加的玩家帐户数</font>
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
	                                    <div class="flot-chart-content" id="flot-line-chart-add"></div>
	                                </div>
	                            </div>
	                        </div>
	                    </div>
	                    
	                    <div class="span6">
	                        <div class="ibox float-e-margins">
	                            <div class="ibox-title">
	                                <h5>累计玩家</h5>
	                                <div class="ibox-tools">
										<span>
					                            <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#" >
					                                <i class="fa fa-envelope"></i>
					                            </a>
					                            <div class="dropdown-menu dropdown-messages well" >
													<h4>累计玩家</h4>
													<font>截至当日，累计的玩家数。</font>
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
	                                    <div class="flot-chart-content" id="flot-line-chart-total"></div>
	                                </div>
	                            </div>
	                        </div>
	                    </div>
	                    
	             </div>
	             
	             <div class="row-fluid">
                    <div class="span6">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <h5>新增玩家 <small>分类，查找</small></h5>
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
                                            <th>新增玩家(账户)</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    	<c:forEach items="${tableAdd}" var="it" >
				  							<tr class="gradeX"><td>${it.value.date}</td><td>${it.value.userAdd}</td></tr>
										</c:forEach>
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <th>日期</th>
                                            <th>新增玩家(账户)</th>
                                        </tr>
                                    </tfoot>
                                </table>

                            </div>
                        </div>
                    </div>
                    
                    <div class="span6">
                        <div class="ibox float-e-margins">
                            <div class="ibox-title">
                                <h5>累计玩家 <small>分类，查找</small></h5>
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
                                            <th>累计玩家(账户)</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    	<c:forEach items="${tableTotal}" var="it" >
				  							<tr class="gradeX"><td>${it.value.date}</td><td>${it.value.userTotal}</td></tr>
										</c:forEach>
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <th>日期</th>
                                            <th>累计玩家(账户)</th>
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
	
		$("#psfilter").click(function(){
			if($("#t1").attr("disabled")=="disabled" && $("#t2").attr("disabled")=="disabled"){ 
				$("#t1").removeAttr("disabled"); 
				$("#t2").removeAttr("disabled"); 
			}else{
				$("#t2").val("");
				$("#t1").attr("disabled","disabled"); 
				$("#t2").attr("disabled","disabled"); 
			}
			
			if($("#t3").attr("disabled")=="disabled" && $("#t4").attr("disabled")=="disabled"){ 
				$("#t3").removeAttr("disabled"); 
				$("#t4").removeAttr("disabled"); 
			}else{
				$("#t4").val("");
				$("#t3").attr("disabled","disabled"); 
				$("#t4").attr("disabled","disabled"); 
			}
			$('#t1').selectpicker('refresh');
			$('#t2').selectpicker('refresh');
			$('#t3').selectpicker('refresh');
			$('#t4').selectpicker('refresh');
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
			
			$("#serverZone").change(function(){
				$("#show").text("无筛选条件");
				
		    	var serverZoneValue=$("#serverZone").val();
		    	var serverZoneText=$("#serverZone  option:selected").text();
				var storeId = $("#storeId").val();
				if($("#serverZone").val()=="all"){
					$.ajax({                                               
						url: '<%=request.getContextPath()%>/manage/fbRetained/findPlatForm',
						type: 'GET',
						contentType: "application/json;charset=UTF-8",		
						dataType: 'text',
						success: function(data){
							var parsedJson = $.parseJSON(data);
							$("#t2").empty();
							$("#t2").append("<option value=''>选择渠道</option>	");
							if(parsedJson!=""){
								jQuery.each(parsedJson, function(index, itemData) {
									$("#t2").append("<option value='"+itemData.pfName+"'>"+itemData.pfName+"</option>");
				  				});
							}
				    		$("#t2 option[value='']").attr("selected", true);
							$('#t2').selectpicker('refresh'); 
						}//回调看看是否有出错
					});
					
					$.ajax({                                               
						url: '<%=request.getContextPath()%>/manage/fbRetained/findServerByStoreId?storeId='+storeId,
						type: 'GET',
						contentType: "application/json;charset=UTF-8",		
						dataType: 'text',
						success: function(data){
							var parsedJson = $.parseJSON(data);
							$("#t4").empty();
							$("#t4").append("<option value=''>选择服务器</option>");
							if(parsedJson!=""){
								jQuery.each(parsedJson, function(index, itemData) {
									$("#t4").append("<option value='"+itemData.serverId+"'>"+itemData.serverId+"</option>");
				  				});
							}
				    		$("#t4 option[value='']").attr("selected", true);
							$('#t4').selectpicker('refresh');
						}//回调看看是否有出错
					});
				}else{
					$.ajax({                                               
						url: '<%=request.getContextPath()%>/manage/fbRetained/findPlatFormByServerId?serverId='+serverZoneValue,
						type: 'GET',
						contentType: "application/json;charset=UTF-8",		
						dataType: 'text',
						success: function(data){
							var parsedJson = $.parseJSON(data);
							$("#t2").empty();
							$("#t2").append("<option value=''>选择渠道</option>	");
							if(parsedJson!=""){
								jQuery.each(parsedJson, function(index, itemData) {
									$("#t2").append("<option value='"+itemData.pfName+"'>"+itemData.pfName+"</option>");
				  				});
							}
				    		$("#t2 option[value='']").attr("selected", true);
							$('#t2').selectpicker('refresh');
						}//回调看看是否有出错
					});
					
					$.ajax({                                               
						url: '<%=request.getContextPath()%>/manage/fbRetained/findServerByStoreIdAndServerZoneId?storeId='+storeId+'&serverZoneId='+serverZoneValue,
						type: 'GET',
						contentType: "application/json;charset=UTF-8",		
						dataType: 'text',
						success: function(data){
							var parsedJson = $.parseJSON(data);
							$("#t4").empty();
							$("#t4").append("<option value=''>选择服务器</option>	");
							if(parsedJson!=""){
								jQuery.each(parsedJson, function(index, itemData) {
									$("#t4").append("<option value='"+itemData.serverId+"'>"+itemData.serverId+"</option>");
				  				});
							}
				    		$("#t4 option[value='']").attr("selected", true);
							$('#t4').selectpicker('refresh');
						}//回调看看是否有出错
					});
				}
			
			});
		    
		
            var userAdd=$.parseJSON($("#userAdd").val());
 			var userTotal=$.parseJSON($("#userTotal").val());
            var barOptionsAdd = {
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
                        axisLabel: "新增用户数量(账户)"
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
                        content: "日期: %x, %s为: %y"
                    }
                };
          		var userAdd = {
                    label: "新增用户",
                    points: {  show: true,radius: 3 },
                    lines: { show: true },
                    data: userAdd
                };

          		
                var barOptionsTotal = {
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
                            axisLabel: "累计用户数量(账户)"
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
                            content: "日期: %x, %s为: %y"
                        }
                    };
              		var userTotal = {
                        label: "累计用户",
                        points: {  show: true,radius: 3 },
                        lines: { show: true },
                        data: userTotal
                    };
                $.plot($("#flot-line-chart-add"), [userAdd], barOptionsAdd);
                $.plot($("#flot-line-chart-total"), [userTotal], barOptionsTotal);
                
                $(".dataTables-example").dataTable();
	    
		})
		
	</script>
  </body>
