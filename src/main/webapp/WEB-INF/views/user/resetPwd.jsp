<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<title>重置密码</title>
	<style type="text/css"> 
	   .error{ color:Red; margin-left:10px;  } 
	   .sub{margin: 0 15px 0 150px;}
	</style> 
</head>

<body>
	<div class="page-header">
   		<h2>重置密码</h2>
 	</div>
 	 <c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	<form id="inputForm" method="post"  Class="form-horizontal" action="${ctx}/manage/user/savePwd" >
		
		<input type="hidden" name="id" value="${user.id }"/>
		
		<div class="control-group ">
	<div class="controls">
		<label class="control-label" >新密码：</label>
		<input type="password" id="pwdCipher" name="pwdCipher" />
	</div>
</div>

<div class="control-group ">
	<div class="controls">
		<label class="control-label" >再次输入新密码：</label>
		<input type="password" id="confirmPwdCipher" name="confirmPwdCipher" />
	</div>
</div>
		
		
		<div class="form-actions">
			<button type="submit" class="btn btn-primary sub" id="submit">保存</button>
		    <a href="<%=request.getContextPath()%>/manage/user/index" class="btn btn-primary">返回</a>
        </div>
	</form>
	<script type="text/javascript">

$(function(){
	
	$("#inputForm").validate({
		rules:{
		    pwdCipher:{
				required:true,
				minlength:5,
				maxlength:15
			},confirmPwdCipher:{
				required:true,
				minlength:5,
				maxlength:15,
				equalTo: "#pwdCipher"
			}
		},messages:{
		    pwdCipher:{
				required:"必须填写",
				minlength:"密码长度5-15位",
				maxlength:"密码长度5-15位"
			},confirmPwdCipher:{
				required:"必须填写",
				minlength:"密码长度5-15位",
				maxlength:"密码长度5-15位",
				equalTo: "两次输入密码不一致，请重新输入"
			}
		}
	});
})
</script>
</body>

</html>