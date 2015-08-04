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
	<title>新增商品</title>
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
	<div class="page-header">
   		<h2>新增商品</h2>
 	</div>
 	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/gm/fb/product/save"   enctype="multipart/form-data" >
			<div class="control-group">
				<label class="control-label" for="gameId">选择游戏项目：</label>
				<div class="controls">
						<select name="gameId" id="gameId">	
							<option value="">请选择项目</option>	
							<c:forEach items="${stores}" var="item" >
								<option value="${item.id }"  >
										${item.name }
								</option>
							</c:forEach>
						</select>	
				</div>
			</div>		
			<div class="control-group">
				<label class="control-label" for="serverZoneId">选择运营大区：</label>
				<div class="controls">
					<select name="serverZoneId" id="serverZoneId">	
						<option value="">请选择项目</option>	
						<c:forEach items="${serverZones}" var="item" >
							<option value="${item.id }"  >
								${item.serverName }
							</option>
						</c:forEach>
					</select>	
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="">服务器列表：</label>
				<div class="controls" id="serverDiv"></div>
			</div>
			<div class="form-ac">
				<button type="button" class="btn btn-success" onclick="selectAll();">全选</button>
				<button type="button" class="btn btn-info" onclick="selectAllNot();">反选</button>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="itemId">道具ID：</label>
				<div class="controls">
					<input type="text" name="itemId" class="input-large "  placeholder="道具ID"/>
				</div>
			</div>	
			<div
				class="control-group">
				<label class="control-label" for="num">道具个数：</label>
				<div class="controls">
					<input type="text" name="num" class="input-large "  placeholder="道具个数"/>
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="prodcutStoreId">商店ID：</label>
				<div class="controls">
					<input type="text" name="prodcutStoreId" class="input-large "  placeholder="商店ID"/>
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="storeLocation">商品出现位置：</label>
				<div class="controls">
					<input type="text" name="storeLocation" class="input-large "  placeholder="商品出现位置"/>
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="isRandom">出现是否随机：</label>
				<div class="controls">
					<input type="text" name="isRandom" class="input-large "  placeholder="出现是否随机"/>
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="randomProbability">随机概率：</label>
				<div class="controls">
					<input type="text" name="randomProbability" class="input-large "  placeholder="随机概率"/>
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="comsumeType">消费类型：</label>
				<div class="controls">
					<input type="text" name="comsumeType" class="input-large "  placeholder="消费类型"/>
				</div>
			</div>			
			<div
				class="control-group">
				<label class="control-label" for="comsumeNum">消费数量：</label>
				<div class="controls">
					<input type="text" name="comsumeNum" class="input-large "  placeholder="消费数量"/>
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="discount">折扣率：</label>
				<div class="controls">
					<input type="text" name="discount" class="input-large "  placeholder="折扣率"/>
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="levelLimit">玩家获取该商品的等级下限：</label>
				<div class="controls">
					<input type="text" name="levelLimit" class="input-large "  placeholder="玩家获取该商品的等级下限"/>
				</div>
			</div>
			<div
				class="control-group">
				<label class="control-label" for="levelCap">玩家获取该商品的等级上限：</label>
				<div class="controls">
					<input type="text" name="levelCap" class="input-large "  placeholder="玩家获取该商品的等级下限"/>
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
					<input type="text" name="showLevel" class="input-large "  placeholder="显示优先级"/>
				</div>
			</div>

			<div class="form-actions">
				<shiro:hasAnyRoles name="admin,fb_gm_product_add">
				 	<button type="submit" class="btn btn-primary" id="submit">保存</button>
				</shiro:hasAnyRoles>
				<a href="${ctx}/manage/gm/fb/product/index" class="btn btn-primary">返回</a>
			</div>
	</form>
<script type="text/javascript">
	function selectAll(){  
        $("input[id='serverId']").attr("checked", true);  
	}	
	function selectAllNot(){
    	$("input[id='serverId']").attr("checked", false);  
	}	
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
						$("#serverDiv").append("<c:forEach items='"+itemData+"' var='ite' varStatus='j'><label class='checkbox inline'><input type='checkbox' id='serverId' name='serverId' value='"+itemData.serverId+"'/><span>"+itemData.serverId+"</span><br/></label></c:forEach>"); 
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
						$("#serverDiv").append("<c:forEach items='"+itemData+"' var='ite' varStatus='j'><label class='checkbox inline'><input type='checkbox' id='serverId' name='server' value='"+itemData.serverId+"'/><span>"+itemData.serverId+"</span><br/></label>&nbsp;</c:forEach>"); 
						});
					},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
				});
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
					required:"商品id必须填写"
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
