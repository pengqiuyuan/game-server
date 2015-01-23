<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<div class="navbar navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container" style="margin-left: 40px;width: auto;">
			<a class="btn btn-navbar" data-toggle="collapse"
				data-target=".nav-collapse"> <span class="icon-bar"></span> <span
				class="icon-bar"></span> <span class="icon-bar"></span>
			</a>

             <div class="nav-collapse" style="padding-top: 10px;font-size: 18px;font-weight: bold;color:black;float: left;width: 200px;">
                                                  光线游戏内容管理平台
            </div>
            <shiro:hasAnyRoles name='admin,1,2,3,4,5,6,7,8'>
			<div class="nav-collapse">
				<ul class="nav navbar-nav">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown">系统管理 <b class="caret"></b></a>
						<ul class="dropdown-menu">
					     	<!-- 总店管理员，分店管理员 -->
							<shiro:hasAnyRoles name="admin">
								<li><a href="${ctx}/manage/user/index">用户管理</a></li>
							</shiro:hasAnyRoles>
							<!-- 总店管理员，总店业务员，分店管理员 -->
							<shiro:hasAnyRoles name="admin">
							<li><a href="${ctx}/manage/store/index">项目管理</a></li>
							</shiro:hasAnyRoles>
							<li><a href="#">意见反馈管理</a></li>
							<shiro:hasAnyRoles name="admin">
							<li><a href="${ctx}/manage/log/index">日志管理</a></li>
							</shiro:hasAnyRoles>
							<li class="divider"></li>
							<li><a href="#">sample</a></li>
						</ul></li>

					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown">服务器管理 <b class="caret"></b></a>
						<ul class="dropdown-menu">
						    <shiro:hasAnyRoles name='admin,1'>
								<li><a href="#">服务器搜索与查看</a></li>
							</shiro:hasAnyRoles>
							<shiro:hasAnyRoles name='admin,2'>
								<li><a href="#">服务器配置与开关</a></li>
							</shiro:hasAnyRoles>
						</ul>
				    </li>

					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown">玩家账号<b class="caret"></b></a>
						<ul class="dropdown-menu">
							<shiro:hasAnyRoles name='admin,3'>
							<li><a href="#">游戏玩家列表查看与搜索</a></li>
								</shiro:hasAnyRoles>
							<shiro:hasAnyRoles name='admin,4'>
								<li><a href="#">角色数据修改与登录封禁</a></li>
							</shiro:hasAnyRoles>
						</ul>
					</li>
					
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown">系统邮件 <b class="caret"></b></a>
						<ul class="dropdown-menu">
							<shiro:hasAnyRoles name='admin,5'>
								<li><a href="#">邮件查看与检索</a></li>
							</shiro:hasAnyRoles>
							<shiro:hasAnyRoles name='admin,6'>
								<li><a href="#">新增邮件</a></li>
							</shiro:hasAnyRoles>
						</ul>
					</li>
						
                 <shiro:hasAnyRoles name="admin">
					<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">统计日志展示<b class="caret"></b></a>
						<ul class="dropdown-menu">					
							
							<li class="dropdown-submenu">
			                    <a tabindex="-1" href="#">道具日志</a>
				                <ul class="dropdown-menu">
				                  <li><a tabindex="-1" href="${ctx}/manage/count/item?id=1">道具日志(总)</a></li>
				                  <li><a href="${ctx}/manage/count/item?id=2">道具获得</a></li>
				                  <li><a href="${ctx}/manage/count/item?id=3">道具消耗</a></li>
				                </ul>
			               </li>
			               
			               <li class="dropdown-submenu">
			                    <a tabindex="-1" href="#">体力日志</a>
				                <ul class="dropdown-menu">
				                  <li><a tabindex="-1" href="${ctx}/manage/count/ap?id=1">体力日志(总)</a></li>
				                  <li><a href="${ctx}/manage/count/ap?id=2">体力获得</a></li>
				                  <li><a href="${ctx}/manage/count/ap?id=3">体力消耗</a></li>
				                </ul>
			               </li>
							
							<shiro:hasAnyRoles name="admin">
									<li class="dropdown-submenu">
					                    <a tabindex="-1" href="#">真实充值币日志</a>
						                <ul class="dropdown-menu">
						                  <li><a tabindex="-1" href="${ctx}/manage/count/money?id=1">真实充值币日志(总)</a></li>
						                  <li><a href="${ctx}/manage/count/money?id=2">真实充值币获得</a></li>
						                  <li><a href="${ctx}/manage/count/money?id=3">真实充值币消耗</a></li>
						                </ul>
					               </li>
							</shiro:hasAnyRoles>
							
							<li class="dropdown-submenu">
			                    <a tabindex="-1" href="#">虚拟充值币日志</a>
				                <ul class="dropdown-menu">
				                  <li><a tabindex="-1" href="${ctx}/manage/count/dummy?id=1">虚拟充值币日志(总)</a></li>
				                  <li><a href="${ctx}/manage/count/dummy?id=2">虚拟充值币获得</a></li>
				                  <li><a href="${ctx}/manage/count/dummy?id=3">虚拟充值币消耗</a></li>
				                </ul>
			               </li>
			               
			               <li class="dropdown-submenu">
			                    <a tabindex="-1" href="#">游戏币日志</a>
				                <ul class="dropdown-menu">
				                  <li><a tabindex="-1" href="${ctx}/manage/count/coin?id=1">游戏币日志(总)</a></li>
				                  <li><a href="${ctx}/manage/count/coin?id=2">游戏币获得</a></li>
				                  <li><a href="${ctx}/manage/count/coin?id=3">游戏币消耗</a></li>
				                </ul>
			               </li>
			               
			               <li class="dropdown-submenu">
			                    <a tabindex="-1" href="#">竞技场徽章(货币)日志</a>
				                <ul class="dropdown-menu">
				                  <li><a tabindex="-1" href="${ctx}/manage/count/arenacoin?id=1">竞技场徽章(货币)日志(总)</a></li>
				                  <li><a href="${ctx}/manage/count/arenacoin?id=2">竞技场徽章获得</a></li>
				                  <li><a href="${ctx}/manage/count/arenacoin?id=3">竞技场徽章消耗</a></li>
				                </ul>
			               </li>
			               
			               <li class="dropdown-submenu">
			                    <a tabindex="-1" href="#">燃烧远征龙鳞币(货币)日志</a>
				                <ul class="dropdown-menu">
				                  <li><a tabindex="-1" href="${ctx}/manage/count/expeditioncoin?id=1">燃烧远征龙鳞币(货币)(总)</a></li>
				                  <li><a href="${ctx}/manage/count/expeditioncoin?id=2">燃烧远征龙鳞币获得</a></li>
				                  <li><a href="${ctx}/manage/count/expeditioncoin?id=3">燃烧远征龙鳞币消耗</a></li>
				                </ul>
			               </li>
			               
							<shiro:hasAnyRoles name="admin">
								  <li class="dropdown-submenu">
					                    <a tabindex="-1" href="#">用户相关日志</a>
						                <ul class="dropdown-menu">
						                  <li><a tabindex="-1" href="${ctx}/manage/count/user?id=1">用户相关日志(总)</a></li>
						                  <li><a href="${ctx}/manage/count/user?id=2">用户登录</a></li>
						                  <li><a href="${ctx}/manage/count/user?id=3">用户创建</a></li>
						                  <li><a href="${ctx}/manage/count/user?id=4">在线用户</a></li>
						                </ul>
					               </li>	
							</shiro:hasAnyRoles>
			
						</ul></li>			
				 </shiro:hasAnyRoles>

				</ul>
				<shiro:user>
					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown"><i class="icon-user icon-white"></i>&nbsp;<shiro:principal
									property="name" /><b class="caret"></b></a>
							<ul class="dropdown-menu nav-list">
								<li class="divider"></li>
								<li><a href="${ctx}/profile">编辑个人资料</a></li>
								<li><a href="${ctx}/logout">安全退出</a></li>
							</ul></li>
					</ul>
				</shiro:user>
			</div>
				</shiro:hasAnyRoles>
			
			<!--/.nav-collapse -->

		</div>
	</div>
</div>
