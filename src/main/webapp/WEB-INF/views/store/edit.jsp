<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>游戏修改</title>
<style type="text/css"> 
.error{ 
color:Red; 
margin-left:10px;  
} 
</style> 
</head>

<body>

	<div class="page-header">
   		<h2>游戏修改</h2>
 	</div>
 	 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post" Class="form-horizontal" action="${ctx}/manage/store/update"  enctype="multipart/form-data" >
    <input type="hidden" name="id" value="${store.id }">
			
			<div
				class="control-group">
				<label class="control-label" for="name">游戏Id：</label>
				<div class="controls">
					<input type="text" name="id" class="input-large " value="${store.id }"  readonly="readonly" />
				</div>
			</div>		
			<div
				class="control-group">
				<label class="control-label" for="name">游戏名称：</label>
				<div class="controls">
					<input type="text" name="name" class="input-large " value="${store.name }"   />
				</div>
			</div>

					
 			<div class="form-actions">
  			     <button type="submit" class="btn btn-primary" id="submit">保存</button>
				 <a href="<%=request.getContextPath()%>/manage/store/index" class="btn btn-primary">返回</a>
	        </div>
	</form>
	<script type="text/javascript">

$(function(){
	
	var code = $("#initCity").val();
	if(code != ''){
		//格式化省份，并在选中状态
		$.get('<%=request.getContextPath()%>/manage/store/formatProvince?cityCode='+code, function(response){
			var pros = document.getElementsByName("province");
			for(var i in pros){
				if(response.id == pros[i].value){
					pros[i].selected = "selected";
					
				}
			}
		});
		//格式化城市，并在选中状态
		$.get('<%=request.getContextPath()%>/manage/store/formatCity?cityCode='+code, function(response){
			for(var i in response){
				if(code == response[i].areaCode){
					$("#cityCode").append("<option selected='selected' value='"+response[i].areaCode+"'>"+response[i].name+"</option>");
					
				}else{
					$("#cityCode").append(new Option(response[i].name, response[i].areaCode));
				
				}
			}

		});
		
	}else{
		$("#cityCode").append("<option value=''>请选择城市</option>");
	}
	
	
	$("#province").change(function(e){
		var value = $("#province").val();
		$("#cityCode").empty();
		e.preventDefault();
		$.ajax({
			url: '<%=request.getContextPath()%>/manage/store/findCitys?provinceId=' + value, 
			type: 'GET',
			contentType: "application/json;charset=UTF-8",
			data: JSON.stringify({name:value}),					
			dataType: 'text',
			success: function(data){
				var parsedJson = $.parseJSON(data);
				$("#cityCode").append("<option value=''>"+"选择下级城市"+"</option>");
				 jQuery.each(parsedJson, function(index, itemData) {
				 $("#cityCode").append("<option value='"+itemData.areaCode+"'>"+itemData.name+"</option>"); 
				 });
			},error:function(xhr){alert('错误了\n\n'+xhr.responseText)}//回调看看是否有出错
		});
	}); 
	
	

	jQuery.validator.addMethod("phone", function(value, element) { 

		var tel = /^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/; 

		return this.optional(element) || (tel.test(value)); 

		}, "电话号码格式错误");
	
/* jQuery.validator.addMethod("longitude", function(value, element) { 
		
		//var reg1 =  /^((\d|[1-9]\d|1[0-7]\d)[°](\d|[0-5]\d)[′](\d|[0-5]\d)(\.\d{1,2})?[\″]?[E]|[W]$)|(180[°]0[′]0[\″]?[E]|[W]$)/; 
		var reg1 =  /^((\d|[1-9]\d|1[0-7]\d)[°](\d|[0-5]\d)[′](\d|[0-5]\d)(\.\d{1,6})?[\″]$)|(180[°]0[′]0[\″]$)/;
		return this.optional(element) || (reg1.test(value)); 

		}, "经度错误");
	jQuery.validator.addMethod("latitude", function(value, element) { 

		var reg2 = /^((\d|[1-8]\d)[°](\d|[0-5]\d)[′](\d|[0-5]\d)(\.\d{1,6})?[\″]$)|(90[°]0[′]0[\″]$)/;
		//var reg2 =  /^((\d|[1-8]\d)[°](\d|[0-5]\d)[′](\d|[0-5]\d)(\.\d{1,2})?[\″]?[N]|[S]$)|(90[°]0[′]0[\″]?[N]|[S]$)/; 

		return this.optional(element) || (reg2.test(value)); 

		}, "纬度错误"); */
	$("#inputForm").validate({
		rules:{
			name:{
				required:true
			},
			longitude:{
		        required:false,
			    number:true,
			    maxlength:30 
			},
			latitude:{
				required:false,
				number:true,
				maxlength:30 
				
		    },
		    city:{
	     		required:true
			},
			province:{
	     		required:true
			},
			tel:{
				phone:true
			},
			partner:{
	     		required:true
			},
			seller:{
	     		required:true
			},
			privateKey:{
	     		required:true
			}
		},messages:{
			name:{
				required:"必须填写",
			},
			latitude:{
				number:"请填数字",
				maxlength:jQuery.format("超过30个字")
			},
			longitude:{
				number:"请填数字",
				maxlength:jQuery.format("超过30个字")
			},
			loginName:{
				required:"必须填写",
			}
		}
	});
})

</script> 
</body>
