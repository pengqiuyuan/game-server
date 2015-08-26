<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="huake" uri="/huake"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>商品</title>
	<style type="text/css">
		.form-ac {
		  padding: 19px 20px 20px;
		  margin-top: 20px;
		  margin-bottom: 20px;
		  padding-left: 180px;
		}
	</style>
</head>
<body>
	<script type="text/javascript" src="${ctx}/static/resources/date/My97DatePicker/My97DatePicker/WdatePicker.js"></script>
	<div >
		<div class="page-header">
			<h2>商品</h2>
		</div>
		<div>
			<c:if test="${not empty message}">
				<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
			</c:if>
			<form id="queryForm" class="form-horizontal"  method="get" action="${ctx}/manage/gm/fb/product/index">
				<div class="control-group">
					<label class="control-label" for="gameId">选择游戏项目：</label>
					<div class="controls">
							<select name="search_LIKE_storeId" id="gameId">	
								<option value="">请选择项目</option>	
								<c:forEach items="${stores}" var="item" >
									<option value="${item.storeId }"   ${param.search_LIKE_storeId == item.storeId ? 'selected' : '' }>
										<huake:getGoStoreNameTag id="${item.storeId }"></huake:getGoStoreNameTag>
									</option>
								</c:forEach>
							</select>	
					</div>
				</div>		
				<div class="control-group">
					<label class="control-label" for="serverZoneId">选择运营大区：</label>
					<div class="controls">
						<select name="search_LIKE_serverZoneId" id="serverZoneId">	
							<option value="">请选择项目</option>	
							<c:forEach items="${serverZones}" var="item" >
								<option value="${item.serverZoneId }"  ${param.search_LIKE_serverZoneId == item.serverZoneId ? 'selected' : '' }>
									<huake:getGoServerZoneNameTag id="${item.serverZoneId }"></huake:getGoServerZoneNameTag>
								</option>
							</c:forEach>
						</select>	
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="">服务器列表：</label>
					<div class="controls" id="serverDiv">
						<c:forEach items="${servers}" var="item" >
							<label class="radio inline">
								<input type="radio" name="search_LIKE_serverId" value="${item.serverId }" ${param.search_LIKE_serverId == item.serverId? 'checked' : '' }/><span>${item.serverId}</span><br/>
							</label>
						</c:forEach>
					</div>
				</div>
				<div
					class="control-group">
					<label class="control-label" for="search_LIKE_itemId">道具ID：</label>
					<div class="controls">
						<input name="search_LIKE_itemId" id="itemDiv" type="text" value="${param.search_LIKE_itemId}" class="input-large "  placeholder="道具ID"/> 
					</div>
				</div>
				<shiro:hasAnyRoles name="admin,fb_gm_product_select">	
					<div class="form-ac">
						<input type="submit" class="btn btn-primary" id="selBtn" value="查 找"/>
					</div>
				</shiro:hasAnyRoles>
			</form>
		</div>

		<div class="control-group">
				<table class="table table-striped table-bordered table-condensed" id="table">
					<thead>
						<tr>
		                    <th title="游戏ID">游戏ID</th>
							<th title="运营大区ID">运营大区ID</th>
							<th title="服务器ID">服务器ID</th>
							<th title="道具ID">道具ID</th>
							<th title="道具数量">道具数量</th>
							<th title="商店ID">商店ID</th>
							<th title="商品出现位置">商品出现位置</th>
							<th title="出现是否随机">出现是否随机</th>
							<th title="随机概率">随机概率</th>
							<th title="消费类型">消费类型</th>
							<th title="消费数量">消费数量</th>
							<th title="折扣率">折扣率</th>
							<th title="玩家获取该商品的等级下限">玩家获取该商品的等级下限</th>
							<th title="玩家获取该商品的等级上限">玩家获取该商品的等级上限</th>
							<th title="折扣生效时间">折扣生效时间</th>
							<th title="折扣持续时间">折扣持续时间</th>
							<th title="折扣循环时间">折扣循环时间</th>
							<th title="商品上架时间">商品上架时间</th>
							<th title="商品下架时间">商品下架时间</th>
							<th title="显示优先级">显示优先级</th>
							<th title="操作">操作</th>
						</tr>
					</thead>
					<tbody id="tbody">
						<c:forEach items="${product.content}" var="item" varStatus="s">
							<tr id="${item.id}">
								<td>${item.gameId}</td>
								<td>${item.serverZoneId}</td>
								<td>${item.serverId}</td>
								<td>${item.itemId}</td>
								<td>${item.num}</td>
								<td>${item.prodcutStoreId}</td>
								<td>${item.storeLocation}</td>
								<td>${item.isRandom}</td>
								<td>${item.randomProbability}</td>
								<td>${item.comsumeType}</td>
								<td>${item.comsumeNum}</td>
								<td>${item.discount}</td>
								<td>${item.levelLimit}</td>
								<td>${item.levelCap}</td>
								<td>${item.discountStartDate}</td>
								<td>${item.discountContinueDate}</td>
								<td>${item.discountCycleDate}</td>
								<td>${item.productPostDate}</td>
								<td>${item.productDownDate}</td>
								<td>${item.showLevel}</td>
			   					<td>
									<div class="action-buttons">
										<shiro:hasAnyRoles name="admin,fb_gm_product_update">
											<a class="exportCode btn table-actions" onclick="updateProduct(${item.id},'${item.itemId}','${item.num}','${item.prodcutStoreId}'
											,'${item.storeLocation}','${item.isRandom}','${item.randomProbability}'
											,'${item.comsumeType}','${item.comsumeNum}','${item.discount}'
											,'${item.levelLimit}','${item.levelCap}','${item.discountStartDate}'
											,'${item.discountContinueDate}','${item.discountCycleDate}','${item.productPostDate}','${item.productDownDate}','${item.showLevel}')"><i class="icon-ok"></i>修改</a>
										</shiro:hasAnyRoles>
										<shiro:hasAnyRoles name="admin,fb_gm_product_delete">
											<a class="exportCode btn table-actions" onclick="delProduct('${item.id}')"><i class="icon-remove"></i>删除</a>
										</shiro:hasAnyRoles>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<tags:pagination page="${product}" paginationSize="5"/>
		</div>
		


		<form id="inputForm" method="post" Class="form-horizontal" action="<%=request.getContextPath()%>/manage/gm/fb/product/update"   enctype="multipart/form-data"  style="display: none;">
			<div style="color:#3352CC;clear:both">
			<br><hr style="background-color:#808080;height:1px;width:800px;margin:auto"><h4>修改公告：</h4></div>
			
			<input type="hidden" name="id" id="id">
			<div class="control-group">
				<label class="control-label" for="">游戏:</label>
				<div class="controls">
					<c:if test="${not empty param.search_LIKE_serverZoneId}">
						<input type="text" value='<huake:getStoreNameTag id="${param.search_LIKE_storeId}"></huake:getStoreNameTag>' disabled="disabled"/>
						<input type="text" value='${param.search_LIKE_storeId}'  name="search_LIKE_storeId" style="display: none;"/>
					</c:if>
					<c:if test="${empty param.search_LIKE_serverZoneId}">
						<input type="text"  readonly="readonly" name="search_LIKE_storeId"/>
					</c:if>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="">运营大区:</label>
				<div class="controls">
					<c:if test="${not empty param.search_LIKE_serverZoneId}">
						<input type="text" value="<huake:getServerZoneNameTag id="${param.search_LIKE_serverZoneId}"></huake:getServerZoneNameTag>"   disabled="disabled"/>
						<input type="text" value="${param.search_LIKE_serverZoneId}"  name="search_LIKE_serverZoneId" style="display: none;"/>
					</c:if>		
					<c:if test="${empty param.search_LIKE_serverZoneId}">
						<input type="text"  readonly="readonly" name="search_LIKE_serverZoneId"/>
					</c:if>		
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="">服务器:</label>
				<div class="controls">
					<c:if test="${not empty param.search_LIKE_serverId}">
						<input type="text" value="${param.search_LIKE_serverId}"  disabled="disabled" />
						<input type="text" value="${param.search_LIKE_serverId}"  name="search_LIKE_serverId" style="display: none;"/>
					</c:if>		
					<c:if test="${empty param.search_LIKE_serverId}">
						<input type="text"  readonly="readonly" name="search_LIKE_serverId"/>
					</c:if>		
				</div>
			</div>
			
			<div
				class="control-group">
				<label class="control-label" for="itemId">道具ID：</label>
				<div class="controls">
					<input type="text" name="itemId" id="itemId" class="input-large "  placeholder="道具ID" readonly="readonly"/>
				</div>
			</div>	
			<div
				class="control-group">
				<label class="control-label" for="num">道具个数：</label>
				<div class="controls">
					<input type="text" name="num" id="num" class="input-large "  placeholder="道具个数"/>
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="prodcutStoreId">商店ID：</label>
				<div class="controls">
					<input type="text" name="prodcutStoreId" id="prodcutStoreId" class="input-large "  placeholder="商店ID"/>
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="storeLocation">商品出现位置：</label>
				<div class="controls">
					<input type="text" name="storeLocation" id="storeLocation" class="input-large "  placeholder="商品出现位置"/>
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="isRandom">出现是否随机：</label>
				<div class="controls">
					<input type="text" name="isRandom" id="isRandom" class="input-large "  placeholder="出现是否随机"/>
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="randomProbability">随机概率：</label>
				<div class="controls">
					<input type="text" name="randomProbability" id="randomProbability" class="input-large "  placeholder="随机概率"/>
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="comsumeType">消费类型：</label>
				<div class="controls">
					<input type="text" name="comsumeType" id="comsumeType" class="input-large "  placeholder="消费类型"/>
				</div>
			</div>			
			<div
				class="control-group">
				<label class="control-label" for="comsumeNum">消费数量：</label>
				<div class="controls">
					<input type="text" name="comsumeNum" id="comsumeNum" class="input-large "  placeholder="消费数量"/>
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="discount">折扣率：</label>
				<div class="controls">
					<input type="text" name="discount" id="discount" class="input-large "  placeholder="折扣率"/>
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="levelLimit">玩家获取该商品的等级下限：</label>
				<div class="controls">
					<input type="text" name="levelLimit" id="levelLimit" class="input-large "  placeholder="玩家获取该商品的等级下限"/>
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="levelCap">玩家获取该商品的等级上限：</label>
				<div class="controls">
					<input type="text" name="levelCap" id="levelCap" class="input-large "  placeholder="玩家获取该商品的等级下限"/>
				</div>
			</div>
			<div>
				<div class="control-group">
					<label class="control-label" for=discountStartDate>折扣生效时间：</label>
					<div class="controls">
						<input type="text" name="discountStartDate" class="input-large " value="" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="discountStartDate"  placeholder="折扣生效时间"/>
					</div>
				</div>	
				<div class="control-group">
					<label class="control-label" for="discountContinueDate">折扣持续时间：</label>
					<div class="controls">
						<input type="text" name="discountContinueDate" class="input-large " value="" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="discountContinueDate" placeholder="折扣持续时间"/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="discountCycleDate">折扣循环时间：</label>
					<div class="controls">
						<input type="text" name="discountCycleDate" class="input-large " value="" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="discountCycleDate" placeholder="折扣循环时间"/>
					</div>
				</div>				
			</div>
			
			<div>
				<div class="control-group">
					<label class="control-label" for=productPostDate>商品上架时间：</label>
					<div class="controls">
						<input type="text" name="productPostDate" class="input-large " value="" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="productPostDate"  placeholder="商品上架时间"/>
					</div>
				</div>	
				<div class="control-group">
					<label class="control-label" for="productDownDate">商品下架时间：</label>
					<div class="controls">
						<input type="text" name="productDownDate" class="input-large " value="" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="productDownDate" placeholder="商品下架时间"/>
					</div>
				</div>			
			</div>			
			<div
				class="control-group">
				<label class="control-label" for="showLevel">显示优先级：</label>
				<div class="controls">
					<input type="text" name="showLevel" id ="showLevel"  class="input-large "  placeholder="显示优先级"/>
				</div>
			</div>
			<div class="form-actions" >
				<shiro:hasAnyRoles name="admin,fb_gm_product_update">
					<input type="submit" class="btn btn-primary" value="修改服务器公告" />
				</shiro:hasAnyRoles>	
				<a class="btn btn-primary" id="cancel">取消</a>
			</div>
		</form>


	</div>
	<script type="text/javascript">
			
			function updateProduct(id,itemId,num,prodcutStoreId,storeLocation,isRandom
					,randomProbability,comsumeType,comsumeNum,discount,levelLimit
					,levelCap,discountStartDate,discountContinueDate,discountCycleDate,productPostDate,productDownDate,showLevel){
				$("#id").attr('value',id);
				$('#inputForm').show();
				
				$("#itemId").attr('value','');
				$("#num").attr('value','');
				$("#prodcutStoreId").attr('value','');
				$("#storeLocation").attr('value','');
				$("#isRandom").attr('value','');
				$("#randomProbability").attr('value','');
				$("#comsumeType").attr('value','');
				$("#comsumeNum").attr('value','');
				$("#discount").attr('value','');
				$("#levelLimit").attr('value','');
				$("#levelCap").attr('value','');
				$("#discountStartDate").attr('value','');
				$("#discountContinueDate").attr('value','');
				$("#discountCycleDate").attr('value','');
				$("#productPostDate").attr('value','');
				$("#productDownDate").attr('value','');
				$("#showLevel").attr('value','');
				
				$("#itemId").attr('value',itemId);
				$("#num").attr('value',num);
				$("#prodcutStoreId").attr('value',prodcutStoreId);
				$("#storeLocation").attr('value',storeLocation);
				$("#isRandom").attr('value',isRandom);
				$("#randomProbability").attr('value',randomProbability);
				$("#comsumeType").attr('value',comsumeType);
				$("#comsumeNum").attr('value',comsumeNum);
				$("#discount").attr('value',discount);
				$("#levelLimit").attr('value',levelLimit);
				$("#levelCap").attr('value',levelCap);
				$("#discountStartDate").attr('value',discountStartDate);
				$("#discountContinueDate").attr('value',discountContinueDate);
				$("#discountCycleDate").attr('value',discountCycleDate);
				$("#productPostDate").attr('value',productPostDate);
				$("#productDownDate").attr('value',productDownDate);
				$("#showLevel").attr('value',showLevel);
				
				$("#selBtn").attr("disabled","disabled");  
				$("#gameId").attr("disabled","disabled");  
				$("#serverZoneId").attr("disabled","disabled");  
				$("#itemDiv").attr("disabled","disabled"); 
				$("input#serverId").attr("disabled","disabled");  
			}
			
			function delProduct(id){
				if(confirm("该操作会删除。。。。！"))
				    {
							$.ajax({
								url: '<%=request.getContextPath()%>/manage/gm/fb/product/del?id=' + id, 
								type: 'DELETE',
								contentType: "application/json;charset=UTF-8",
								dataType: 'json',
								success: function(data){
									window.location.href = window.location.href;
								},error:function(xhr){
									alert('错误了，请重试');
								}
							});
				    }
			}
			
			$("#cancel").click(function(){
				$('#inputForm').hide();
				
				$("#selBtn").removeAttr("disabled"); 
				$("#gameId").removeAttr("disabled"); 
				$("#serverZoneId").removeAttr("disabled"); 
				$("#itemDiv").removeAttr("disabled"); 
				$("input#serverId").removeAttr("disabled"); 
			});
			

		
			$(function(){
				$("#serverZoneId").change(function(e){
					var serverZoneId = $("#serverZoneId").val();
					if($("#gameId").val()!=""){
						var gameId = $("#gameId").val();
						$("#serverDiv").empty();
						$.ajax({                                               
							url: '<%=request.getContextPath()%>/manage/gift/findServers?serverZoneId='+serverZoneId+'&gameId='+gameId, 
							type: 'GET',
							contentType: "application/json;charset=UTF-8",		
							dataType: 'text',
							success: function(data){
								var parsedJson = $.parseJSON(data);
								jQuery.each(parsedJson, function(index, itemData) {
								$("#serverDiv").append("<c:forEach items='"+itemData+"' var='ite' varStatus='j'><label class='radio inline'><input type='radio' id='serverId' name='search_LIKE_serverId' value='"+itemData.serverId+"'/><span>"+itemData.serverId+"</span><br/></label></c:forEach>"); 
								});
							},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
						});
					}
				});
				
				$("#gameId").change(function(e){
					var gameId = $("#gameId").val();
					if($("#gameId").val()!=""){
						$("#submit").removeAttr("disabled");
						$("#item").empty();
						$("#field").empty();				
					}else{
						$("#message").remove();
						$("#addfield").removeAttr("disabled");	
					}

					if($("#serverZoneId").val()!="" && $("#gameId").val()!=""){
						var serverZoneId = $("#serverZoneId").val();
						$("#serverDiv").empty();
						$.ajax({                                               
							url: '<%=request.getContextPath()%>/manage/gift/findServers?serverZoneId='+serverZoneId+'&gameId='+gameId, 
							type: 'GET',
							contentType: "application/json;charset=UTF-8",		
							dataType: 'text',
							success: function(data){
								var parsedJson = $.parseJSON(data);
								jQuery.each(parsedJson, function(index, itemData) {
								$("#serverDiv").append("<c:forEach items='"+itemData+"' var='ite' varStatus='j'><label class='radio inline'><input type='radio' id='serverId' name='search_LIKE_serverId' value='"+itemData.serverId+"'/><span>"+itemData.serverId+"</span><br/></label></c:forEach>"); 
								});
							},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
						});
					}	
					
				});
				
				$("#queryForm").validate({
					rules:{
						search_LIKE_storeId:{
							required:true
						},
						search_LIKE_serverZoneId:{
							required:true
						},
						search_LIKE_serverId:{
							required:true
						}
					},messages:{
						search_LIKE_storeId:{
							required:"游戏项目"
						},
						search_LIKE_serverZoneId:{
							required:"运营必须填写"
						},
						search_LIKE_serverId:{
							required:"服务器必须填写"
						}
					}
				});			
				
				
				$("#inputForm").validate({
					rules:{
						gameId:{
							required:true
						},
						serverZoneId:{
							required:true
						},
						serverId:{
							required:true
						},
						itemId:{
							required:true
						},
						num:{
							required:true
						},
						prodcutStoreId:{
							required:true
						},
						storeLocation:{
							required:true
						},
						isRandom:{
							required:true
						},
						randomProbability:{
							required:true
						},
						comsumeType:{
							required:true
						},
						comsumeNum:{
							required:true
						},
						discount:{
							required:true
						},
						levelLimit:{
							required:true
						},
						levelCap:{
							required:true
						},
						discountStartDate:{
							required:true
						},
						discountContinueDate:{
							required:true
						},
						discountCycleDate:{
							required:true
						},
						productPostDate:{
							required:true
						},
						productDownDate:{
							required:true
						},
						showLevel:{
							required:true
						}
					},messages:{
						gameId:{
							required:"游戏项目"
						},
						serverZoneId:{
							required:"运营必须填写"
						},
						serverId:{
							required:"服务器必须填写"
						},
						itemId:{
							required:"道具ID必须填写"
						},
						num:{
							required:"个数必须填写"
						},
						prodcutStoreId:{
							required:"商店ID必须填写"
						},
						storeLocation:{
							required:"商品出现位置必须填写"
						},
						isRandom:{
							required:"出现是否随机必须填写"
						},
						randomProbability:{
							required:"随机概率必须填写"
						},
						comsumeType:{
							required:"消费类型必须填写"
						},
						comsumeNum:{
							required:"消费数量必须填写"
						},
						discount:{
							required:"折扣率"
						},
						levelLimit:{
							required:"玩家获取该商品的等级下限必须填写"
						},
						levelCap:{
							required:"玩家获取该商品的等级上限必须填写"
						},
						discountStartDate:{
							required:"折扣生效时间必须填写"
						},
						discountContinueDate:{
							required:"折扣持续时间必须填写"
						},
						discountCycleDate:{
							required:"折扣循环时间必须填写"
						},
						productPostDate:{
							required:"商品上架时间必须填写"
						},
						productDownDate:{
							required:"商品下架时间必须填写"
						},
						showLevel:{
							required:"显示优先级必须填写"
						}
					}
				});				
				
			})
			
			
	</script> 	
</body>
</html>