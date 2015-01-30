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
    <title>Kibana 3{{dashboard.current.title ? " - "+dashboard.current.title : ""}}</title>
    <link rel="stylesheet" href="css/bootstrap.light.min.css" title="Light">
    <link rel="stylesheet" href="css/timepicker.css">
    <link rel="stylesheet" href="css/animate.min.css">
    <link rel="stylesheet" href="css/normalize.min.css">
    <!--<link rel="stylesheet" ng-href="css/bootstrap.{{dashboard.current.style||'dark'}}.min.css">-->
    <link rel="stylesheet" href="css/bootstrap-responsive.min.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/jquery-ui.css">
    <link rel="stylesheet" href="css/jquery.multiselect.css">
    <!-- load the root require context -->
    <script src="vendor/require/require.js"></script>
    <script src="app/components/require.config.js"></script>
    <script>require(['app'], function () {})</script>
    <style>
    </style>

  </head>

  <body>
    <noscript>
      <div class="container">
        <center><h3>You must enable javascript to use Kibana</h3></center>
      </div>
    </noscript>
    <div ng-cloak ng-repeat='alert in dashAlerts.list' class="alert-{{alert.severity}} dashboard-notice" ng-show="$last">
      <button type="button" class="close" ng-click="dashAlerts.clear(alert)" style="padding-right:50px">&times;</button>
      <strong>{{alert.title}}</strong> <span ng-bind-html='alert.text'></span> <div style="padding-right:10px" class='pull-right small'> {{$index + 1}} alert(s) </div>
    </div>
    <div ng-cloak class="navbar navbar-static-top" ng-show="dashboard.current.nav.length && dashboard.current.role==${user}">
      <div class="navbar-inner">
        <div class="container-fluid" style="padding-top: 70px">
          <span class="brand"><img src="img/small.png" bs-tooltip="'Kibana '+(kbnVersion=='@REV@'?'master':kbnVersion)" data-placement="bottom">{{dashboard.current.title}}</span>

          <ul class="nav pull-right" ng-controller='dashLoader' ng-init="init()" ng-include="'app/partials/dashLoader.html'">
            </ul>
        </div>
      </div>
    </div>
    <div id="view" ng-cloak ng-view ng-show="dashboard.current.role==${user}"></div>

  </body>
</html>
