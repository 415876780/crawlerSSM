<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><decorator:title default="欢迎使用语料标记系统"/></title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/main.css"/>
<decorator:head/>
<script type="text/javascript">
	window.onload=function(){
		
		 window.setInterval("doStart()",2000);
		 //setTimeout("doStart()",5000);
		 //refreshTime();
		 
	}
	
	var xmlhttp;
	var i=0
	function createXMLHttpRequest(){
		if (window.XMLHttpRequest)
		  {// code for IE7+, Firefox, Chrome, Opera, Safari
		  xmlhttp=new XMLHttpRequest();
		  }
		else
		  {// code for IE6, IE5
		  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		  }
		
	}
	function doStart(){
		//createXMLHttpRequest();
		 //alert("你好，我是一个警告框！"+application.getAttribute("count"));
		document.getElementById("count").innerHTML=<%=application.getAttribute("count") %>;
		//document.getElementById("count").innerHTML=i++;
	}
	

</script>
</head>
<body>
<h1><decorator:title/></h1>

<c:if test="${not empty loginUser}">
<a href="<%=request.getContextPath() %>/text/add">文本添加</a>
<a href="<%=request.getContextPath() %>/text/texts">新闻列表</a>
<a href="<%=request.getContextPath() %>/text/weibos">微博列表</a>
<a href="<%=request.getContextPath() %>/text/alltexts">所有新闻</a>
<a href="<%=request.getContextPath() %>/text/allweibos">所有微博</a>
<a href="<%=request.getContextPath() %>/logout">退出系统</a>
<div id ="jack" style="text-align:right;border-bottom:0px solid #000;">
	当前用户:${loginUser.nickname }&nbsp;&nbsp;&nbsp;当前在线用户数：<span id ="count">1</span>
</div>


</c:if>
<hr/>
<decorator:body/>
<div align="center" style="width:100%;border-top:1px solid; float:left;margin-top:10px;">
	CopyRight@2015-2017<br/>
	NLP
</div>
</body>
</html>