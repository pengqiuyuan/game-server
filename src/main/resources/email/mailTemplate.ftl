<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <style type="text/css">
        h3{
            color:#630000;
        }
        ul li{
            margin: 10px;
        }
        a:link,a:active,a:visited{
            color: #0000EE;
        }
        a{
            text-decoration: none;
        }
    </style>
</head>
<body>
<h2>日志阙值触发报警通知</h2>
<h3>报警条数统计</h3>
<ul>
	<#list sr_hits as being>
	    <li>
	        <td>${being}<td>
	    </li>
	</#list>
</ul>
<h3>增加的道具数量（item_count）</h3>
<ul>
	<#list items as being>
	    <li>
	        <td>${being}<td>
	    </li>
	</#list>
</ul>
<h3>增加的充值币数量（money_count）</h3>
<ul>
	<#list moneys as being>
	    <li>
	        <td>${being}<td>
	    </li>
	</#list>
</ul>
<h3>增加的游戏币数量（coin_count）</h3>
<ul>
	<#list coins as being>
	    <li>
	        <td>${being}<td>
	    </li>
	</#list>
</ul>
<h3>日志道具id（item_id）</h3>
<ul>
	<#list itemIds as being>
	    <li>
	        <td>${being}<td>
	    </li>
	</#list>
</ul>
<script type="text/javascript">
    var href = location.href;
    if(href.indexOf("http") != 0 ){
        alert("部分浏览器下所有弹出框功能的使用会受到限制！");
    }
</script>
</body>
</html>