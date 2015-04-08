<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
    <link href="<%=request.getContextPath()%>/static/flot/css/style.css?v=2.1.0" rel="stylesheet">
    
    
    <style>
    </style>

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



    <!-- Mainly scripts -->
    <script src="<%=request.getContextPath()%>/static/flot/js/jquery-1.8.0.js"></script>
    <script src="<%=request.getContextPath()%>/static/flot/js/bootstrap.js"></script>
    <!-- Flot -->
    <script src="<%=request.getContextPath()%>/static/flot/js/jquery.flot.js"></script>
    <script src="<%=request.getContextPath()%>/static/flot/js/jquery.flot.tooltip.min.js"></script>
    <script src="<%=request.getContextPath()%>/static/flot/js/jquery.flot.resize.js"></script>
    <script src="<%=request.getContextPath()%>/static/flot/js/jquery.flot.pie.js"></script>
    <!-- Flot demo data -->
    <script src="<%=request.getContextPath()%>/static/flot/js/flot-demo.js"></script>

  </body>
