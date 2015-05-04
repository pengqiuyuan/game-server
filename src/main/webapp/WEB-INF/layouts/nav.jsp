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
            <shiro:hasAnyRoles name='admin,10000,10001,10002'>
			<div class="nav-collapse">
					<shiro:lacksRole name="admin">
					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown">
<!-- 						style="background-color: #5bc0de;text-shadow: 0 0px 0 #fff;" -->
							<a href="#" id="storeIdY" class="dropdown-toggle"  data-toggle="dropdown" onclick='changeTag(<shiro:principal property="storeId"/>,<shiro:principal property="categoryId"/>,1);'>
							<shiro:principal property="storeName"/>
							<b class="caret"></b></a>
							<ul class="dropdown-menu" id="storeIdN">
						    </ul>
                        </li>                   
					</ul>
					</shiro:lacksRole>
			</div>
			</shiro:hasAnyRoles>
<!--              <div class="nav-collapse" style="padding-top: 10px;font-size: 18px;font-weight: bold;color:black;float: left;width: 200px;">
                                                  光线游戏内容管理平台
            </div> -->
            <shiro:hasAnyRoles name='admin,10000,10001,10002'>
			<div class="nav-collapse">
				<ul class="nav navbar-nav">
					<shiro:hasAnyRoles name="admin,10000">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown">系统管理 <b class="caret"></b></a>
							<ul class="dropdown-menu">
								<li><a href="${ctx}/manage/user/index">用户管理</a></li>
								<shiro:hasAnyRoles name="admin,1">
								<li><a href="${ctx}/manage/serverZone/index">运营大区设置</a></li>
								</shiro:hasAnyRoles>
								<shiro:hasAnyRoles name="admin,4">
								<li><a href="${ctx}/manage/store/index">游戏项目管理</a></li>
								</shiro:hasAnyRoles>
								<shiro:hasAnyRoles name="admin,7">
								<li><a href="${ctx}/manage/server/index">服务器信息设置</a></li>
								</shiro:hasAnyRoles>
								<shiro:hasAnyRoles name="admin,10">
								<li><a href="${ctx}/manage/platForm/index">渠道管理</a></li>
								</shiro:hasAnyRoles>
								<shiro:hasAnyRoles name="admin,13">
								<li><a href="${ctx}/manage/roleFunction/index">权限管理</a></li>
								</shiro:hasAnyRoles>
							    <shiro:hasAnyRoles name="admin">
							    <li><a href="${ctx}/manage/log/index">日志管理</a></li>
							    </shiro:hasAnyRoles>
							    <shiro:hasAnyRoles name="admin,16">
							    <li><a href="${ctx}/manage/tag/uploadExcel">上传文档</a></li>
							    </shiro:hasAnyRoles>
								<li class="divider"></li>
								<li><a href="#">sample</a></li>
							</ul>
						</li>
					</shiro:hasAnyRoles>
					
					<shiro:hasAnyRoles name="admin,10001">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown">礼品卡功能管理 <b class="caret"></b></a>
							<ul class="dropdown-menu">
							    <shiro:hasAnyRoles name='admin,18'>
									<li><a href="${ctx}/manage/gift/index">查看礼品卡</a></li>
								</shiro:hasAnyRoles>
								<shiro:hasAnyRoles name='admin,17'>
									<li><a href="${ctx}/manage/gift/add">新增礼品卡</a></li>
								</shiro:hasAnyRoles>
								<shiro:hasAnyRoles name='admin,20'>
									<li><a href="${ctx}/manage/gift/search">查询礼品卡</a></li>
								</shiro:hasAnyRoles>
								<shiro:hasAnyRoles name='admin,29'>
									<li><a href="${ctx}/manage/giftProps/index">礼品卡增加道具</a></li>
								</shiro:hasAnyRoles>
								<li class="divider"></li>
								<li><a href="#">sample</a></li>
							</ul>
					    </li>
					</shiro:hasAnyRoles>

<%-- 					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown">玩家账号<b class="caret"></b></a>
						<ul class="dropdown-menu">
							<shiro:hasAnyRoles name='admin,3'>
							<li><a href="#">游戏玩家列表查看与搜索</a></li>
								</shiro:hasAnyRoles>
							<shiro:hasAnyRoles name='admin,4'>
								<li><a href="#">角色数据修改与登录封禁</a></li>
							</shiro:hasAnyRoles>
							<li class="divider"></li>
							<li><a href="#">sample</a></li>
						</ul>
					</li> --%>
					

				    <shiro:hasAnyRoles name="admin,10002">
					    <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">实时统计日志<b class="caret"></b></a>
								<ul class="dropdown-menu">
									<shiro:hasAnyRoles name="admin,31">
										<li class="dropdown-submenu"><a tabindex="-1" href="#">KUN实时日志</a>
											<ul class="dropdown-menu">
												<li><a tabindex="-1" href="#">KUN测试</a></li>
											</ul>
										</li>
									</shiro:hasAnyRoles>
									<shiro:hasAnyRoles name="admin,32">
										<li class="dropdown-submenu"><a tabindex="-1" href="#">KDS实时日志</a>
											<ul class="dropdown-menu">
												<li><a tabindex="-1" href="#">KDS测试</a></li>
											</ul>
										</li>
									</shiro:hasAnyRoles>
									<shiro:hasAnyRoles name="admin,21,22,23,24,25,26,27,28">
										<li class="dropdown-submenu"><a tabindex="-1" href="#">FB实时日志</a>
											<ul class="dropdown-menu">
												<shiro:hasAnyRoles name="admin,22">
												<li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">道具日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/fbItem?id=1#/dashboard/file/fb_item.json">道具日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/fbItem?id=2#/dashboard/file/fb_itemGet.json">道具获得</a></li>
									                  <li><a href="${ctx}/manage/count/fbItem?id=3#/dashboard/file/fb_itemCost.json">道具消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								               <shiro:hasAnyRoles name="admin,21">
								               <li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">体力日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/fbAp?id=1#/dashboard/file/fb_ap.json">体力日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/fbAp?id=2#/dashboard/file/fb_apGet.json">体力获得</a></li>
									                  <li><a href="${ctx}/manage/count/fbAp?id=3#/dashboard/file/fb_apCost.json">体力消耗</a></li>
									                </ul>
								               </li>
												</shiro:hasAnyRoles>
												<shiro:hasAnyRoles name="admin,23">
														<li class="dropdown-submenu">
										                    <a tabindex="-1" href="#">真实充值币日志</a>
											                <ul class="dropdown-menu">
											                  <li><a tabindex="-1" href="${ctx}/manage/count/fbMoney?id=1#/dashboard/file/fb_money.json">真实充值币日志(总)</a></li>
											                  <li><a href="${ctx}/manage/count/fbMoney?id=2#/dashboard/file/fb_moneyGet.json">真实充值币获得</a></li>
											                  <li><a href="${ctx}/manage/count/fbMoney?id=3#/dashboard/file/fb_moneyCost.json">真实充值币消耗</a></li>
											                </ul>
										               </li>
												</shiro:hasAnyRoles>
												<shiro:hasAnyRoles name="admin,24">
												<li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">虚拟充值币日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/fbDummy?id=1#/dashboard/file/fb_dummy.json">虚拟充值币日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/dummy?id=2#/fbDashboard/file/fb_dummyGet.json">虚拟充值币获得</a></li>
									                  <li><a href="${ctx}/manage/count/dummy?id=3#/fbDashboard/file/fb_dummyCost.json">虚拟充值币消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								               <shiro:hasAnyRoles name="admin,25">
								               <li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">游戏币日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/fbCoin?id=1#/dashboard/file/fb_coin.json">游戏币日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/fbCoin?id=2#/dashboard/file/fb_coinGet.json">游戏币获得</a></li>
									                  <li><a href="${ctx}/manage/count/fbCoin?id=3#/dashboard/file/fb_coinCost.json">游戏币消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								               <shiro:hasAnyRoles name="admin,26">
								               <li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">竞技场徽章(货币)日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/fbArenacoin?id=1#/dashboard/file/fb_arenacoin.json">竞技场徽章(货币)日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/fbArenacoin?id=2#/dashboard/file/fb_arenacoinGet.json">竞技场徽章获得</a></li>
									                  <li><a href="${ctx}/manage/count/fbArenacoin?id=3#/dashboard/file/fb_arenacoinCost.json">竞技场徽章消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								               <shiro:hasAnyRoles name="admin,27">
								               <li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">燃烧远征龙鳞币(货币)日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/fbExpeditioncoin?id=1#/dashboard/file/fb_expeditioncoin.json">燃烧远征龙鳞币(货币)(总)</a></li>
									                  <li><a href="${ctx}/manage/count/fbExpeditioncoin?id=2#/dashboard/file/fb_expeditioncoinGet.json">燃烧远征龙鳞币获得</a></li>
									                  <li><a href="${ctx}/manage/count/fbExpeditioncoin?id=3#/dashboard/file/fb_expeditioncoinCost.json">燃烧远征龙鳞币消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
												<shiro:hasAnyRoles name="admin,28">
													  <li class="dropdown-submenu">
										                    <a tabindex="-1" href="#">用户相关日志</a>
											                <ul class="dropdown-menu">
											                  <li class="dropdown-submenu">
											                  	<a tabindex="-1" href="${ctx}/manage/count/fbUser?id=1#/dashboard/file/fb_user.json">用户相关日志(总)</a>
											                    <ul class="dropdown-menu">
												                  <li><a href="${ctx}/manage/retained/fb/userRetained">用户留存</a></li>
												                </ul>
											                  </li>
											                  <li><a href="${ctx}/manage/count/fbUser?id=2#/dashboard/file/fb_userLogin.json">用户登录</a></li>
											                  <li><a href="${ctx}/manage/count/fbUser?id=3#/dashboard/file/fb_userCreate.json">用户创建</a></li>
											                  <li><a href="${ctx}/manage/count/fbUser?id=4#/dashboard/file/fb_userOnline.json">在线用户</a></li>
											                  <li><a href="${ctx}/manage/count/fbUser?id=5#/dashboard/file/fb_userNewbieguide.json">新手引导与功能引导</a></li>
											                </ul>
										               </li>	
												</shiro:hasAnyRoles>
												<li class="divider"></li>
												<li><a href="#">sample</a></li>
											</ul>

										</li>
									</shiro:hasAnyRoles>
								</ul>
						</li>
					    <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">离线统计日志<b class="caret"></b></a>
								<ul class="dropdown-menu">
									<shiro:hasAnyRoles name="admin,31">
										<li class="dropdown-submenu"><a tabindex="-1" href="#">KUN离线日志</a>
											<ul class="dropdown-menu">
												<li><a tabindex="-1" href="#">KUN测试</a></li>
											</ul>
										</li>
									</shiro:hasAnyRoles>
									<shiro:hasAnyRoles name="admin,32">
										<li class="dropdown-submenu"><a tabindex="-1" href="#">KDS离线日志</a>
											<ul class="dropdown-menu">
												<li><a tabindex="-1" href="#">KDS测试</a></li>
											</ul>
										</li>
									</shiro:hasAnyRoles>
									<shiro:hasAnyRoles name="admin,21,22,23,24,25,26,27,28">
										<li class="dropdown-submenu"><a tabindex="-1" href="#">FB离线日志</a>
											<ul class="dropdown-menu">
												<shiro:hasAnyRoles name="admin,28">
													  <li class="dropdown-submenu">
															<li><a href="${ctx}/manage/retained/fb/userRetained">用户留存</a></li>
										               </li>	
												</shiro:hasAnyRoles>
												<li class="divider"></li>
												<li><a href="#">sample</a></li>
											</ul>

										</li>
									</shiro:hasAnyRoles>
								</ul>
						</li>									
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

<script type="text/javascript">
	function changeTag(id,categoryId,sta){
		var th = $("#storeIdN");
		th.empty();
		$.ajax({                                               
			url: '<%=request.getContextPath()%>/manage/findStores?storeId='+id+'&categoryId='+categoryId+'&sta='+sta,
			type: 'GET',
			contentType: "application/json;charset=UTF-8",		
			dataType: 'text',
			success: function(data){
 				var parsedJson = $.parseJSON(data);
				th.append("<li class='divider'></li>");
				jQuery.each(parsedJson, function(index, itemData) {
				    th.append("<li class='dropdown-submenu' id='"+itemData.storeId+"'><a href='#' onMouseOver='change2("+itemData.storeId+");'>"+itemData.storeName+"</a></li>"); 
				}); 
				if(sta!="1"){
					window.location.href='<%=request.getContextPath()%>/manage/index';
				}
			}
		});		
	}
	
	function change2(id){
		var th = $("#"+id);
		th.find("ul").remove();
		$.ajax({                                               
			url: '<%=request.getContextPath()%>/manage/findCategorys?storeId='+id,
			type: 'GET',
			contentType: "application/json;charset=UTF-8",		
			dataType: 'text',
			success: function(data){
				th.append("<ul class='dropdown-menu' id='category"+id+"'></ul>");
 				var parsedJson = $.parseJSON(data);
				jQuery.each(parsedJson, function(index, itemData) {
					$("#category"+id).append("<li><a href='#' onclick='changeTag("+id+","+itemData.id+",0);'>"+itemData.categoryName+"</a></li>"); 
				}); 
			}
		});		
	}
</script>
