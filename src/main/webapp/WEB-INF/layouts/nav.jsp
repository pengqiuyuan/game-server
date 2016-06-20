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
							<ul class="dropdown-menu storeIdN">
						    </ul>
                        </li>                   
					</ul>
					</shiro:lacksRole>
			</div>
			</shiro:hasAnyRoles>

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
							    <shiro:hasAnyRoles name="admin,21">
							    <li><a href="${ctx}/manage/monitor/index">监控数值</a></li>
							    </shiro:hasAnyRoles>
								<li class="divider"></li>
								<li><a href="#">sample</a></li>
							</ul>
						</li>
					</shiro:hasAnyRoles>
						
					<shiro:hasAnyRoles name="admin,10000">
						<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">项目GM<b class="caret"></b></a>
								<ul class="dropdown-menu">
									<shiro:hasAnyRoles name="admin,fb_gm_server_select,fb_gm_account_select,fb_gm_placard_select,fb_gm_email_select,fb_gm_gag_select,fb_gm_seal_select,fb_gm_product_select">
										<li class="dropdown-submenu"><a tabindex="-1" href="#">《FB》项目GM</a>
											<ul class="dropdown-menu">
												<li class="dropdown-submenu">
													<shiro:hasAnyRoles name="admin,fb_gm_server_select,fb_gm_server_add">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">服务器状态设置</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<shiro:hasAnyRoles name="admin,fb_gm_server_select">
																		<li><a href="${ctx}/manage/gm/fb/serverStatus/index">服务器状态</a></li>
																	</shiro:hasAnyRoles>
																	<shiro:hasAnyRoles name="admin,fb_gm_server_add">
																	</shiro:hasAnyRoles>
																<li>
															</ul>
														</li>
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,fb_gm_account_select,fb_gm_account_add">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">灰度账号管理</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<shiro:hasAnyRoles name="admin,fb_gm_account_select">
																		<li><a href="${ctx}/manage/gm/fb/serverStatus/accountIndex">灰度账号管理</a></li>
																	</shiro:hasAnyRoles>
																	<shiro:hasAnyRoles name="admin,fb_gm_account_add">
																		<li><a href="${ctx}/manage/gm/fb/serverStatus/accountAdd">灰度账号管理新增</a></li>
																	</shiro:hasAnyRoles>
																<li>
															</ul>
														</li>														
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,fb_gm_placard_select,fb_gm_placard_add">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">公告</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<shiro:hasAnyRoles name="admin,fb_gm_placard_select">
																		<li><a href="${ctx}/manage/gm/fb/placard/index">公告</a></li>
																	</shiro:hasAnyRoles>
																	<shiro:hasAnyRoles name="admin,fb_gm_placard_add">
																		<li><a href="${ctx}/manage/gm/fb/placard/add">公告新增</a></li>
																	</shiro:hasAnyRoles>
																<li>
															</ul>
														</li>															
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,fb_gm_email_select,fb_gm_email_add">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">邮件</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<shiro:hasAnyRoles name="admin,fb_gm_email_select">
																		<li><a href="${ctx}/manage/gm/fb/email/index">邮件</a></li>
																	</shiro:hasAnyRoles>
																	<shiro:hasAnyRoles name="admin,fb_gm_email_add">
																		<li><a href="${ctx}/manage/gm/fb/email/add">邮件新增</a></li>
																	</shiro:hasAnyRoles>
																<li>
															</ul>
														</li>												
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,fb_gm_gag_select,fb_gm_gag_add">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">禁言</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<shiro:hasAnyRoles name="admin,fb_gm_gag_select">
																		<li><a href="${ctx}/manage/gm/fb/gag/index">禁言</a></li>
																	</shiro:hasAnyRoles>
																	<shiro:hasAnyRoles name="admin,fb_gm_gag_add">	
																		<li><a href="${ctx}/manage/gm/fb/gag/add">禁言新增</a></li>
																	</shiro:hasAnyRoles>
																<li>
															</ul>
														</li>												
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,fb_gm_seal_select,fb_gm_seal_add">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">封号</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<shiro:hasAnyRoles name="admin,fb_gm_seal_select">
																		<li><a href="${ctx}/manage/gm/fb/seal/index">封号</a></li>
																	</shiro:hasAnyRoles>
																	<shiro:hasAnyRoles name="admin,fb_gm_seal_add">
																		<li><a href="${ctx}/manage/gm/fb/seal/add">封号新增</a></li>
																	</shiro:hasAnyRoles>
																<li>
															</ul>
														</li>														
													</shiro:hasAnyRoles>
												    <shiro:hasAnyRoles name="admin,fb_gm_product_select,fb_gm_product_add">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">商店商品</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<shiro:hasAnyRoles name="admin,fb_gm_product_select">
																		<li><a href="${ctx}/manage/gm/fb/product/index">商店商品</a></li>
																	</shiro:hasAnyRoles>
																	<shiro:hasAnyRoles name="admin,fb_gm_product_add">
																		<li><a href="${ctx}/manage/gm/fb/product/add">商店商品新增</a></li>
																	</shiro:hasAnyRoles>
																<li>
															</ul>
														</li>														    
												    </shiro:hasAnyRoles>
													<li class="divider"></li>
													<li><a href="#">sample</a></li>
												<li>	
											</ul>
										</li>
									</shiro:hasAnyRoles>
									
									<shiro:hasAnyRoles name="admin,kds_gm_server_select,kds_gm_account_select,kds_gm_placard_select,kds_gm_email_select,kds_gm_gag_select,kds_gm_seal_select">
										<li class="dropdown-submenu"><a tabindex="-1" href="#">《KDS》项目GM</a>
											<ul class="dropdown-menu">
												<li class="dropdown-submenu">
													<shiro:hasAnyRoles name="admin,kds_gm_server_select,kds_gm_server_add">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">服务器状态设置</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<shiro:hasAnyRoles name="admin,kds_gm_server_select">
																		<li><a href="${ctx}/manage/gm/kds/serverStatus/index">服务器状态</a></li>
																	</shiro:hasAnyRoles>
																	<shiro:hasAnyRoles name="admin,kds_gm_server_add">
																	</shiro:hasAnyRoles>
																<li>
															</ul>
														</li>
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,kds_gm_account_select,kds_gm_account_add">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">灰度账号管理</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<shiro:hasAnyRoles name="admin,kds_gm_account_select">
																		<li><a href="${ctx}/manage/gm/kds/serverStatus/accountIndex">灰度账号管理</a></li>
																	</shiro:hasAnyRoles>
																	<shiro:hasAnyRoles name="admin,kds_gm_account_add">
																		<li><a href="${ctx}/manage/gm/kds/serverStatus/accountAdd">灰度账号管理新增</a></li>
																	</shiro:hasAnyRoles>
																<li>
															</ul>
														</li>														
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,kds_gm_placard_select,kds_gm_placard_add">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">公告</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<shiro:hasAnyRoles name="admin,kds_gm_placard_select">
																		<li><a href="${ctx}/manage/gm/kds/placard/index">公告</a></li>
																	</shiro:hasAnyRoles>
																	<shiro:hasAnyRoles name="admin,kds_gm_placard_add">
																		<li><a href="${ctx}/manage/gm/kds/placard/add">公告新增</a></li>
																	</shiro:hasAnyRoles>
																<li>
															</ul>
														</li>															
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,kds_gm_email_select,kds_gm_email_add">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">邮件</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<shiro:hasAnyRoles name="admin,kds_gm_email_select">
																		<li><a href="${ctx}/manage/gm/kds/email/index">邮件</a></li>
																	</shiro:hasAnyRoles>
																	<shiro:hasAnyRoles name="admin,kds_gm_email_add">
																		<li><a href="${ctx}/manage/gm/kds/email/add">邮件新增</a></li>
																	</shiro:hasAnyRoles>
																<li>
															</ul>
														</li>												
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,kds_gm_gag_select,kds_gm_gag_add">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">禁言</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<shiro:hasAnyRoles name="admin,kds_gm_gag_select">
																		<li><a href="${ctx}/manage/gm/kds/gag/index">禁言</a></li>
																	</shiro:hasAnyRoles>
																	<shiro:hasAnyRoles name="admin,kds_gm_gag_add">	
																		<li><a href="${ctx}/manage/gm/kds/gag/add">禁言新增</a></li>
																	</shiro:hasAnyRoles>
																<li>
															</ul>
														</li>												
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,kds_gm_seal_select,kds_gm_seal_add">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">封号</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<shiro:hasAnyRoles name="admin,kds_gm_seal_select">
																		<li><a href="${ctx}/manage/gm/kds/seal/index">封号</a></li>
																	</shiro:hasAnyRoles>
																	<shiro:hasAnyRoles name="admin,kds_gm_seal_add">
																		<li><a href="${ctx}/manage/gm/kds/seal/add">封号新增</a></li>
																	</shiro:hasAnyRoles>
																<li>
															</ul>
														</li>														
													</shiro:hasAnyRoles>
												    <shiro:hasAnyRoles name="admin,kds_gm_product_select,kds_gm_product_add">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">商店商品</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<shiro:hasAnyRoles name="admin,kds_gm_product_select">
																		<li><a href="${ctx}/manage/gm/kds/product/index">商店商品</a></li>
																	</shiro:hasAnyRoles>
																	<shiro:hasAnyRoles name="admin,kds_gm_product_add">
																		<li><a href="${ctx}/manage/gm/kds/product/add">商店商品新增</a></li>
																	</shiro:hasAnyRoles>
																<li>
															</ul>
														</li>														    
												    </shiro:hasAnyRoles>
													<li class="divider"></li>
													<li><a href="#">sample</a></li>
												<li>	
											</ul>
										</li>
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
					

				    <shiro:hasAnyRoles name="admin,10002">
					    <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">实时统计日志<b class="caret"></b></a>
								<ul class="dropdown-menu">
									<shiro:hasAnyRoles name="admin,KUN_AP,KUN_ITEM,KUN_MONEY,KUN_DUMMY,KUN_COIN,KUN_USER">
										<li class="dropdown-submenu"><a tabindex="-1" href="#">KUN实时日志</a>
											<ul class="dropdown-menu">
												<shiro:hasAnyRoles name="admin,KUN_ITEM">
												<li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">道具日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/kunItem#/dashboard/file/kun_item.json">道具日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/kunItem#/dashboard/file/kun_itemGet.json">道具获得</a></li>
									                  <li><a href="${ctx}/manage/count/kunItem#/dashboard/file/kun_itemCost.json">道具消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								               <!-- 
								               <shiro:hasAnyRoles name="admin,KUN_AP">
								               <li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">体力日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/kunAp#/dashboard/file/kun_ap.json">体力日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/kunAp#/dashboard/file/kun_apGet.json">体力获得</a></li>
									                  <li><a href="${ctx}/manage/count/kunAp#/dashboard/file/kun_apCost.json">体力消耗</a></li>
									                </ul>
								               </li>
												</shiro:hasAnyRoles>
												 -->
											   <shiro:hasAnyRoles name="admin,KUN_MONEY">
														<li class="dropdown-submenu">
										                    <a tabindex="-1" href="#">真实充值币日志</a>
											                <ul class="dropdown-menu">
											                  <li><a tabindex="-1" href="${ctx}/manage/count/kunMoney#/dashboard/file/kun_money.json">真实充值币日志(总)</a></li>
											                  <li><a href="${ctx}/manage/count/kunMoney#/dashboard/file/kun_moneyGet.json">真实充值币获得</a></li>
											                  <li><a href="${ctx}/manage/count/kunMoney#/dashboard/file/kun_moneyCost.json">真实充值币消耗</a></li>
											                </ul>
										               </li>
												</shiro:hasAnyRoles>
												<shiro:hasAnyRoles name="admin,KUN_DUMMY">
												<li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">虚拟充值币日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/kunDummy#/dashboard/file/kun_dummy.json">虚拟充值币日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/kunDummy#/dashboard/file/kun_dummyGet.json">虚拟充值币获得</a></li>
									                  <li><a href="${ctx}/manage/count/kunDummy#/dashboard/file/kun_dummyCost.json">虚拟充值币消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								               <shiro:hasAnyRoles name="admin,KUN_COIN">
								               <li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">游戏币日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/kunCoin#/dashboard/file/kun_coin.json">游戏币日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/kunCoin#/dashboard/file/kun_coinGet.json">游戏币获得</a></li>
									                  <li><a href="${ctx}/manage/count/kunCoin#/dashboard/file/kun_coinCost.json">游戏币消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								               <shiro:hasAnyRoles name="admin,KUN_USER">
													  <li class="dropdown-submenu">
										                    <a tabindex="-1" href="#">用户相关日志</a>
											                <ul class="dropdown-menu">
											                  <li class="dropdown-submenu">
											                  	<a tabindex="-1" href="${ctx}/manage/count/kunUser#/dashboard/file/kun_user.json">用户相关日志(总)</a>
											                  </li>
											                  <li><a href="${ctx}/manage/count/kunUser#/dashboard/file/kun_userLogin.json">用户登录</a></li>
											                  <li><a href="${ctx}/manage/count/kunUser#/dashboard/file/kun_userCreate.json">用户创建</a></li>
											                  <li><a href="${ctx}/manage/count/kunUser#/dashboard/file/kun_userOnline.json">在线用户</a></li>
											                  <li><a href="${ctx}/manage/count/kunUser#/dashboard/file/kun_userNewbieguide.json">新手引导与功能引导</a></li>
											                </ul>
										               </li>	
												</shiro:hasAnyRoles>
												<shiro:hasAnyRoles name="admin,KUN_USER_PORTRAIT">
													  <li class="dropdown-submenu">
										                    <a tabindex="-1" href="#">用户画像</a>
											                <ul class="dropdown-menu">
											                  <li >
											                  	<a tabindex="-1" href="${ctx}/manage/count/kunUserPortrait#/dashboard/file/kun_userPortrait.json">用户画像</a>
											                  </li>
											                </ul>
										               </li>	
												</shiro:hasAnyRoles>
												<li class="divider"></li>
												<li><a href="#">sample</a></li>
											</ul>
										</li>
									</shiro:hasAnyRoles>
									<shiro:hasAnyRoles name="admin,KDS_AP,KDS_ITEM,KDS_MONEY,KDS_DUMMY,KDS_COIN,KDS_USER">
										<li class="dropdown-submenu"><a tabindex="-1" href="#">KDS实时日志</a>
											<ul class="dropdown-menu">
												<shiro:hasAnyRoles name="admin,KDS_ITEM">
												<li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">道具日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/kdsItem#/dashboard/file/kds_item.json">道具日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/kdsItem#/dashboard/file/kds_itemGet.json">道具获得</a></li>
									                  <li><a href="${ctx}/manage/count/kdsItem#/dashboard/file/kds_itemCost.json">道具消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								               <!--  
								               <shiro:hasAnyRoles name="admin,KDS_AP">
								               <li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">体力日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/kdsAp#/dashboard/file/kds_ap.json">体力日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/kdsAp#/dashboard/file/kds_apGet.json">体力获得</a></li>
									                  <li><a href="${ctx}/manage/count/kdsAp#/dashboard/file/kds_apCost.json">体力消耗</a></li>
									                </ul>
								               </li>
												</shiro:hasAnyRoles>
												-->
											   <shiro:hasAnyRoles name="admin,KDS_MONEY">
														<li class="dropdown-submenu">
										                    <a tabindex="-1" href="#">真实充值币日志</a>
											                <ul class="dropdown-menu">
											                  <li><a tabindex="-1" href="${ctx}/manage/count/kdsMoney#/dashboard/file/kds_money.json">真实充值币日志(总)</a></li>
											                  <li><a href="${ctx}/manage/count/kdsMoney#/dashboard/file/kds_moneyGet.json">真实充值币获得</a></li>
											                  <li><a href="${ctx}/manage/count/kdsMoney#/dashboard/file/kds_moneyCost.json">真实充值币消耗</a></li>
											                </ul>
										               </li>
												</shiro:hasAnyRoles>
												<shiro:hasAnyRoles name="admin,KDS_DUMMY">
												<li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">虚拟充值币日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/kdsDummy#/dashboard/file/kds_dummy.json">虚拟充值币日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/kdsDummy#/dashboard/file/kds_dummyGet.json">虚拟充值币获得</a></li>
									                  <li><a href="${ctx}/manage/count/kdsDummy#/dashboard/file/kds_dummyCost.json">虚拟充值币消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								               <shiro:hasAnyRoles name="admin,KDS_COIN">
								               <li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">游戏币日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/kdsCoin#/dashboard/file/kds_coin.json">游戏币日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/kdsCoin#/dashboard/file/kds_coinGet.json">游戏币获得</a></li>
									                  <li><a href="${ctx}/manage/count/kdsCoin#/dashboard/file/kds_coinCost.json">游戏币消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								               <shiro:hasAnyRoles name="admin,KDS_USER">
													  <li class="dropdown-submenu">
										                    <a tabindex="-1" href="#">用户相关日志</a>
											                <ul class="dropdown-menu">
											                  <li class="dropdown-submenu">
											                  	<a tabindex="-1" href="${ctx}/manage/count/kdsUser#/dashboard/file/kds_user.json">用户相关日志(总)</a>
											                  </li>
											                  <li><a href="${ctx}/manage/count/kdsUser#/dashboard/file/kds_userLogin.json">用户登录</a></li>
											                  <li><a href="${ctx}/manage/count/kdsUser#/dashboard/file/kds_userCreate.json">用户创建</a></li>
											                  <li><a href="${ctx}/manage/count/kdsUser#/dashboard/file/kds_userOnline.json">在线用户</a></li>
											                  <li><a href="${ctx}/manage/count/kdsUser#/dashboard/file/kds_userNewbieguide.json">新手引导与功能引导</a></li>
											                </ul>
										               </li>	
												</shiro:hasAnyRoles>
												<shiro:hasAnyRoles name="admin,KDS_USER_PORTRAIT">
													  <li class="dropdown-submenu">
										                    <a tabindex="-1" href="#">用户画像</a>
											                <ul class="dropdown-menu">
											                  <li>
											                  	<a tabindex="-1" href="${ctx}/manage/count/kdsUserPortrait#/dashboard/file/kds_userPortrait.json">用户画像</a>
											                  </li>
											                </ul>
										               </li>	
												</shiro:hasAnyRoles>
												<li class="divider"></li>
												<li><a href="#">sample</a></li>
											</ul>
										</li>
									</shiro:hasAnyRoles>
									<shiro:hasAnyRoles name="admin,FB_AP,FB_ITEM,FB_MONEY,FB_DUMMY,FB_COIN,FB_ARENACOIN,FB_EXPEDITIONC,FB_USER">
										<li class="dropdown-submenu"><a tabindex="-1" href="#">FB实时日志</a>
											<ul class="dropdown-menu">
												<shiro:hasAnyRoles name="admin,FB_ITEM">
												<li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">道具日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/fbItem#/dashboard/file/fb_item.json">道具日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/fbItem#/dashboard/file/fb_itemGet.json">道具获得</a></li>
									                  <li><a href="${ctx}/manage/count/fbItem#/dashboard/file/fb_itemCost.json">道具消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								               <!--  
								               <shiro:hasAnyRoles name="admin,FB_AP">
								               <li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">体力日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/fbAp#/dashboard/file/fb_ap.json">体力日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/fbAp#/dashboard/file/fb_apGet.json">体力获得</a></li>
									                  <li><a href="${ctx}/manage/count/fbAp#/dashboard/file/fb_apCost.json">体力消耗</a></li>
									                </ul>
								               </li>
												</shiro:hasAnyRoles>
												-->
												<shiro:hasAnyRoles name="admin,FB_MONEY">
														<li class="dropdown-submenu">
										                    <a tabindex="-1" href="#">真实充值币日志</a>
											                <ul class="dropdown-menu">
											                  <li><a tabindex="-1" href="${ctx}/manage/count/fbMoney#/dashboard/file/fb_money.json">真实充值币日志(总)</a></li>
											                  <li><a href="${ctx}/manage/count/fbMoney#/dashboard/file/fb_moneyGet.json">真实充值币获得</a></li>
											                  <li><a href="${ctx}/manage/count/fbMoney#/dashboard/file/fb_moneyCost.json">真实充值币消耗</a></li>
											                </ul>
										               </li>
												</shiro:hasAnyRoles>
												<shiro:hasAnyRoles name="admin,FB_DUMMY">
												<li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">虚拟充值币日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/fbDummy#/dashboard/file/fb_dummy.json">虚拟充值币日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/fbDummy#/dashboard/file/fb_dummyGet.json">虚拟充值币获得</a></li>
									                  <li><a href="${ctx}/manage/count/fbDummy#/dashboard/file/fb_dummyCost.json">虚拟充值币消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								               <shiro:hasAnyRoles name="admin,FB_COIN">
								               <li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">游戏币日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/fbCoin#/dashboard/file/fb_coin.json">游戏币日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/fbCoin#/dashboard/file/fb_coinGet.json">游戏币获得</a></li>
									                  <li><a href="${ctx}/manage/count/fbCoin#/dashboard/file/fb_coinCost.json">游戏币消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								               <!--  
								               <shiro:hasAnyRoles name="admin,FB_ARENACOIN">
								               <li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">竞技场徽章(货币)日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/fbArenacoin#/dashboard/file/fb_arenacoin.json">竞技场徽章(货币)日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/fbArenacoin#/dashboard/file/fb_arenacoinGet.json">竞技场徽章获得</a></li>
									                  <li><a href="${ctx}/manage/count/fbArenacoin#/dashboard/file/fb_arenacoinCost.json">竞技场徽章消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								               <shiro:hasAnyRoles name="admin,FB_EXPEDITIONC">
								               <li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">燃烧远征龙鳞币(货币)日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/fbExpeditioncoin#/dashboard/file/fb_expeditioncoin.json">燃烧远征龙鳞币(货币)(总)</a></li>
									                  <li><a href="${ctx}/manage/count/fbExpeditioncoin#/dashboard/file/fb_expeditioncoinGet.json">燃烧远征龙鳞币获得</a></li>
									                  <li><a href="${ctx}/manage/count/fbExpeditioncoin#/dashboard/file/fb_expeditioncoinCost.json">燃烧远征龙鳞币消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								                -->
												<shiro:hasAnyRoles name="admin,FB_USER">
													  <li class="dropdown-submenu">
										                    <a tabindex="-1" href="#">用户相关日志</a>
											                <ul class="dropdown-menu">
											                  <li>
											                  	<a tabindex="-1" href="${ctx}/manage/count/fbUser#/dashboard/file/fb_user.json">用户相关日志(总)</a>
											                  </li>
											                  <li><a href="${ctx}/manage/count/fbUser#/dashboard/file/fb_userLogin.json">用户登录</a></li>
											                  <li><a href="${ctx}/manage/count/fbUser#/dashboard/file/fb_userCreate.json">用户创建</a></li>
											                  <li><a href="${ctx}/manage/count/fbUser#/dashboard/file/fb_userOnline.json">在线用户</a></li>
											                  <li><a href="${ctx}/manage/count/fbUser#/dashboard/file/fb_userNewbieguide.json">新手引导与功能引导</a></li>
											                </ul>
										               </li>	
												</shiro:hasAnyRoles>
												<shiro:hasAnyRoles name="admin,FB_USER_PORTRAIT">
													  <li class="dropdown-submenu">
										                    <a tabindex="-1" href="#">用户画像</a>
											                <ul class="dropdown-menu">
											                  <li>
											                  	<a tabindex="-1" href="${ctx}/manage/count/fbUserPortrait#/dashboard/file/fb_userPortrait.json">用户画像</a>
											                  </li>
											                </ul>
										               </li>	
												</shiro:hasAnyRoles>
												<li class="divider"></li>
												<li><a href="#">sample</a></li>
											</ul>

										</li>
									</shiro:hasAnyRoles>
									<shiro:hasAnyRoles name="admin,XYJ_AP,XYJ_ITEM,XYJ_MONEY,XYJ_DUMMY,XYJ_COIN,XYJ_USER">
										<li class="dropdown-submenu"><a tabindex="-1" href="#">XYJ实时日志</a>
											<ul class="dropdown-menu">
												<shiro:hasAnyRoles name="admin,XYJ_ITEM">
												<li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">道具日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/xyjItem#/dashboard/file/xyj_item.json">道具日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/xyjItem#/dashboard/file/xyj_itemGet.json">道具获得</a></li>
									                  <li><a href="${ctx}/manage/count/xyjItem#/dashboard/file/xyj_itemCost.json">道具消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								               <!-- 
								               <shiro:hasAnyRoles name="admin,XYJ_AP">
								               <li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">体力日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/xyjAp#/dashboard/file/xyj_ap.json">体力日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/xyjAp#/dashboard/file/xyj_apGet.json">体力获得</a></li>
									                  <li><a href="${ctx}/manage/count/xyjAp#/dashboard/file/xyj_apCost.json">体力消耗</a></li>
									                </ul>
								               </li>
												</shiro:hasAnyRoles>
												 -->
											   <shiro:hasAnyRoles name="admin,XYJ_MONEY">
														<li class="dropdown-submenu">
										                    <a tabindex="-1" href="#">真实充值币日志</a>
											                <ul class="dropdown-menu">
											                  <li><a tabindex="-1" href="${ctx}/manage/count/xyjMoney#/dashboard/file/xyj_money.json">真实充值币日志(总)</a></li>
											                  <li><a href="${ctx}/manage/count/xyjMoney#/dashboard/file/xyj_moneyGet.json">真实充值币获得</a></li>
											                  <li><a href="${ctx}/manage/count/xyjMoney#/dashboard/file/xyj_moneyCost.json">真实充值币消耗</a></li>
											                </ul>
										               </li>
												</shiro:hasAnyRoles>
												<shiro:hasAnyRoles name="admin,XYJ_DUMMY">
												<li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">虚拟充值币日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/xyjDummy#/dashboard/file/xyj_dummy.json">虚拟充值币日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/xyjDummy#/dashboard/file/xyj_dummyGet.json">虚拟充值币获得</a></li>
									                  <li><a href="${ctx}/manage/count/xyjDummy#/dashboard/file/xyj_dummyCost.json">虚拟充值币消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								               <shiro:hasAnyRoles name="admin,XYJ_COIN">
								               <li class="dropdown-submenu">
								                    <a tabindex="-1" href="#">游戏币日志</a>
									                <ul class="dropdown-menu">
									                  <li><a tabindex="-1" href="${ctx}/manage/count/xyjCoin#/dashboard/file/xyj_coin.json">游戏币日志(总)</a></li>
									                  <li><a href="${ctx}/manage/count/xyjCoin#/dashboard/file/xyj_coinGet.json">游戏币获得</a></li>
									                  <li><a href="${ctx}/manage/count/xyjCoin#/dashboard/file/xyj_coinCost.json">游戏币消耗</a></li>
									                </ul>
								               </li>
								               </shiro:hasAnyRoles>
								               <shiro:hasAnyRoles name="admin,XYJ_USER">
													  <li class="dropdown-submenu">
										                    <a tabindex="-1" href="#">用户相关日志</a>
											                <ul class="dropdown-menu">
											                  <li class="dropdown-submenu">
											                  	<a tabindex="-1" href="${ctx}/manage/count/xyjUser#/dashboard/file/xyj_user.json">用户相关日志(总)</a>
											                  </li>
											                  <li><a href="${ctx}/manage/count/xyjUser#/dashboard/file/xyj_userLogin.json">用户登录</a></li>
											                  <li><a href="${ctx}/manage/count/xyjUser#/dashboard/file/xyj_userCreate.json">用户创建</a></li>
											                  <li><a href="${ctx}/manage/count/xyjUser#/dashboard/file/xyj_userOnline.json">在线用户</a></li>
											                  <li><a href="${ctx}/manage/count/xyjUser#/dashboard/file/xyj_userNewbieguide.json">新手引导与功能引导</a></li>
											                </ul>
										               </li>	
												</shiro:hasAnyRoles>
												<shiro:hasAnyRoles name="admin,XYJ_USER_PORTRAIT">
													  <li class="dropdown-submenu">
										                    <a tabindex="-1" href="#">用户画像</a>
											                <ul class="dropdown-menu">
											                  <li >
											                  	<a tabindex="-1" href="${ctx}/manage/count/xyjUserPortrait#/dashboard/file/xyj_userPortrait.json">用户画像</a>
											                  </li>
											                </ul>
										               </li>	
												</shiro:hasAnyRoles>
												<li class="divider"></li>
												<li><a href="#">sample</a></li>
											</ul>
										</li>
									</shiro:hasAnyRoles>
									
									<li class="divider"></li>
									<li><a href="#">sample</a></li>
								</ul>
						</li>
					    <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">离线统计日志<b class="caret"></b></a>
								<ul class="dropdown-menu">
									<shiro:hasAnyRoles name="admin,KUN_OFF_USER_RETAINED,KUN_OFF_USER_ADD,KUN_OFF_USER_ACTIVE,KUN_OFF_USER_PAY,KUN_OFF_USER_INCOME">
										<li class="dropdown-submenu"><a tabindex="-1" href="#">KUN离线日志</a>
											<ul class="dropdown-menu">
												<li class="dropdown-submenu">
													<shiro:hasAnyRoles name="admin,KUN_OFF_USER_RETAINED">
														<li><a href="${ctx}/manage/kunRetained/kun/userRetained?serverZone=all">用户留存</a></li>
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,KUN_OFF_USER_ADD">
														<li><a href="${ctx}/manage/kunUserAdd/kun/userAdd?serverZone=all">用户新增</a></li>
													</shiro:hasAnyRoles>	
													<shiro:hasAnyRoles name="admin,KUN_OFF_USER_ACTIVE">
														<li><a href="${ctx}/manage/kunActive/kun/userActive?serverZone=all">活跃用户</a></li>
													</shiro:hasAnyRoles>	
													<shiro:hasAnyRoles name="admin,KUN_OFF_USER_PAY">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">用户付费率</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<li><a href="${ctx}/manage/kunUserPay/kun/userPay?serverZone=all">新增付费用户</a></li>
																	<li><a href="${ctx}/manage/kunUserPay/kun/userDay?serverZone=all">当日付费率</a></li>
																	<li><a href="${ctx}/manage/kunUserPay/kun/userWeek?serverZone=all">当周付费率</a></li>
																	<li><a href="${ctx}/manage/kunUserPay/kun/userMouth?serverZone=all">当月付费率</a></li>
																<li>
															</ul>
														</li>
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,KUN_OFF_USER_INCOME">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">收入分析</a>
																<ul class="dropdown-menu">
																	<li class="dropdown-submenu">
																		<li><a href="${ctx}/manage/kunIncome/kun/userIncome?serverZone=all">收入分析</a></li>
																		<li><a href="${ctx}/manage/kunMoneyPayP/kun/moneyPayP?serverZone=all">ARPU与ARPPU</a></li>
																	<li>
																</ul>
														</li>
													</shiro:hasAnyRoles>
												<li>	
											</ul>
										</li>
									</shiro:hasAnyRoles>
									<shiro:hasAnyRoles name="admin,KDS_OFF_USER_RETAINED,KDS_OFF_USER_ADD,KDS_OFF_USER_ACTIVE,KDS_OFF_USER_PAY,KDS_OFF_USER_INCOME">
										<li class="dropdown-submenu"><a tabindex="-1" href="#">KDS离线日志</a>
											<ul class="dropdown-menu">
												<li class="dropdown-submenu">
													<shiro:hasAnyRoles name="admin,KDS_OFF_USER_RETAINED">
														<li><a href="${ctx}/manage/kdsRetained/kds/userRetained?serverZone=all">用户留存</a></li>
													</shiro:hasAnyRoles>	
													<shiro:hasAnyRoles name="admin,KDS_OFF_USER_ADD">
														<li><a href="${ctx}/manage/kdsUserAdd/kds/userAdd?serverZone=all">用户新增</a></li>
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,KDS_OFF_USER_ACTIVE">
														<li><a href="${ctx}/manage/kdsActive/kds/userActive?serverZone=all">活跃用户</a></li>
													</shiro:hasAnyRoles>	
													<shiro:hasAnyRoles name="admin,KDS_OFF_USER_PAY">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">用户付费率</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<li><a href="${ctx}/manage/kdsUserPay/kds/userPay?serverZone=all">新增付费用户</a></li>
																	<li><a href="${ctx}/manage/kdsUserPay/kds/userDay?serverZone=all">当日付费率</a></li>
																	<li><a href="${ctx}/manage/kdsUserPay/kds/userWeek?serverZone=all">当周付费率</a></li>
																	<li><a href="${ctx}/manage/kdsUserPay/kds/userMouth?serverZone=all">当月付费率</a></li>
																<li>
															</ul>
														</li>
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,KDS_OFF_USER_INCOME">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">收入分析</a>
																<ul class="dropdown-menu">
																	<li class="dropdown-submenu">
																		<li><a href="${ctx}/manage/kdsIncome/kds/userIncome?serverZone=all">收入分析</a></li>
																		<li><a href="${ctx}/manage/kdsMoneyPayP/kds/moneyPayP?serverZone=all">ARPU与ARPPU</a></li>
																	<li>
																</ul>
														</li>
													</shiro:hasAnyRoles>
												<li>	
											</ul>
										</li>
									</shiro:hasAnyRoles>
									<shiro:hasAnyRoles name="admin,FB_OFF_USER_RETAINED,FB_OFF_USER_ADD,FB_OFF_USER_ACTIVE,FB_OFF_USER_PAY,FB_OFF_USER_INCOME">
										<li class="dropdown-submenu"><a tabindex="-1" href="#">FB离线日志</a>
											<ul class="dropdown-menu">
												<li class="dropdown-submenu">
													<shiro:hasAnyRoles name="admin,FB_OFF_USER_RETAINED">
														<li><a href="${ctx}/manage/fbRetained/fb/userRetained?serverZone=all">用户留存</a></li>
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,FB_OFF_USER_ADD">
														<li><a href="${ctx}/manage/fbUserAdd/fb/userAdd?serverZone=all">用户新增</a></li>
													</shiro:hasAnyRoles>	
													<shiro:hasAnyRoles name="admin,FB_OFF_USER_ACTIVE">
														<li><a href="${ctx}/manage/fbActive/fb/userActive?serverZone=all">活跃用户</a></li>
													</shiro:hasAnyRoles>	
													<shiro:hasAnyRoles name="admin,FB_OFF_USER_PAY">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">用户付费率</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<li><a href="${ctx}/manage/fbUserPay/fb/userPay?serverZone=all">新增付费用户</a></li>
																	<li><a href="${ctx}/manage/fbUserPay/fb/userDay?serverZone=all">当日付费率</a></li>
																	<li><a href="${ctx}/manage/fbUserPay/fb/userWeek?serverZone=all">当周付费率</a></li>
																	<li><a href="${ctx}/manage/fbUserPay/fb/userMouth?serverZone=all">当月付费率</a></li>
																<li>
															</ul>
														</li>
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,FB_OFF_USER_INCOME">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">收入分析</a>
																<ul class="dropdown-menu">
																	<li class="dropdown-submenu">
																		<li><a href="${ctx}/manage/fbIncome/fb/userIncome?serverZone=all">收入分析</a></li>
																		<li><a href="${ctx}/manage/fbMoneyPayP/fb/moneyPayP?serverZone=all">ARPU与ARPPU</a></li>
																	<li>
																</ul>
														</li>
													</shiro:hasAnyRoles>
												<li>	
											</ul>

										</li>
									</shiro:hasAnyRoles>
									<shiro:hasAnyRoles name="admin,XYJ_OFF_USER_RETAINED,XYJ_OFF_USER_ADD,XYJ_OFF_USER_ACTIVE,XYJ_OFF_USER_PAY,XYJ_OFF_USER_INCOME">
										<li class="dropdown-submenu"><a tabindex="-1" href="#">XYJ离线日志</a>
											<ul class="dropdown-menu">
												<li class="dropdown-submenu">
													<shiro:hasAnyRoles name="admin,XYJ_OFF_USER_RETAINED">
														<li><a href="${ctx}/manage/xyjRetained/xyj/userRetained?serverZone=all">用户留存</a></li>
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,XYJ_OFF_USER_ADD">
														<li><a href="${ctx}/manage/xyjUserAdd/xyj/userAdd?serverZone=all">用户新增</a></li>
													</shiro:hasAnyRoles>	
													<shiro:hasAnyRoles name="admin,XYJ_OFF_USER_ACTIVE">
														<li><a href="${ctx}/manage/xyjActive/xyj/userActive?serverZone=all">活跃用户</a></li>
													</shiro:hasAnyRoles>	
													<shiro:hasAnyRoles name="admin,XYJ_OFF_USER_PAY">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">用户付费率</a>
															<ul class="dropdown-menu">
																<li class="dropdown-submenu">
																	<li><a href="${ctx}/manage/xyjUserPay/xyj/userPay?serverZone=all">新增付费用户</a></li>
																	<li><a href="${ctx}/manage/xyjUserPay/xyj/userDay?serverZone=all">当日付费率</a></li>
																	<li><a href="${ctx}/manage/xyjUserPay/xyj/userWeek?serverZone=all">当周付费率</a></li>
																	<li><a href="${ctx}/manage/xyjUserPay/xyj/userMouth?serverZone=all">当月付费率</a></li>
																<li>
															</ul>
														</li>
													</shiro:hasAnyRoles>
													<shiro:hasAnyRoles name="admin,XYJ_OFF_USER_INCOME">
														<li class="dropdown-submenu"><a tabindex="-1" href="#">收入分析</a>
																<ul class="dropdown-menu">
																	<li class="dropdown-submenu">
																		<li><a href="${ctx}/manage/xyjIncome/xyj/userIncome?serverZone=all">收入分析</a></li>
																		<li><a href="${ctx}/manage/xyjMoneyPayP/xyj/moneyPayP?serverZone=all">ARPU与ARPPU</a></li>
																	<li>
																</ul>
														</li>
													</shiro:hasAnyRoles>
												<li>	
											</ul>
										</li>
									</shiro:hasAnyRoles>									
									
									<li class="divider"></li>
									<li><a href="#">sample</a></li>
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
		</div>
	</div>
</div>

<script type="text/javascript">

	var isMobile = {
			Android: function() {
			    return navigator.userAgent.match(/Android/i) ? true : false;
			},
			BlackBerry: function() {
			    return navigator.userAgent.match(/BlackBerry/i) ? true : false;
			},
			iOS: function() {
			    return navigator.userAgent.match(/iPhone|iPad|iPod/i) ? true : false;
			},
			Windows: function() {
			    return navigator.userAgent.match(/IEMobile/i) ? true : false;
			},
			any: function() {
			    return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Windows());
			}
	};
	
	function changeTag(id,categoryId,sta){
		var th = $(".storeIdN");
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
				    if(isMobile.any()){
				    	th.append("<li class='dropdown-submenu "+itemData.storeId+"'><a href='#' onTouchStart='change2("+itemData.storeId+");'>"+itemData.storeName+"</a></li>"); 
				    }else{
				    	th.append("<li class='dropdown-submenu "+itemData.storeId+"'><a href='#' onMouseOver='change2("+itemData.storeId+");'>"+itemData.storeName+"</a></li>"); 
				    }

				}); 
				if(sta!="1"){
					window.location.href='<%=request.getContextPath()%>/manage/index';
				}
			}
		});		
	}
	
	function change2(id){
		var th = $("."+id);
		th.find("ul").remove();
		$.ajax({                                               
			url: '<%=request.getContextPath()%>/manage/findCategorys?storeId='+id,
			type: 'GET',
			contentType: "application/json;charset=UTF-8",		
			dataType: 'text',
			success: function(data){
				th.append("<ul class='dropdown-menu category"+id+"' '></ul>");
 				var parsedJson = $.parseJSON(data);
				jQuery.each(parsedJson, function(index, itemData) {
					$(".category"+id).append("<li><a href='javascript:void(0);' onclick='changeTag("+id+","+itemData.id+",0);'>"+itemData.categoryName+"</a></li>"); 
				}); 
			}
		});		
	}
</script>
