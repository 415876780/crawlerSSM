<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>标记</title>
<style type="text/css">
	     body{
	 		 background:#eff3f6;
		}
 
        
     
   
        #jack
		{
			text-align:center;
            table-layout: fixed;
			font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
			width:80%;
			border-collapse:collapse;
		}
        #jack th 
		{
			text-align:center;
			font-size:1.1em;
			
			padding-top:5px;
			padding-bottom:4px;
			background-color:#A7C942;
			color:#ffffff;
		}
        #jack tr.alt td 
		{
			color:#000000;
			background-color:#EAF2D3;
		}
    </style> 
</head>
<body>
<sf:form method="post" modelAttribute="text">

<table width="700" align="center" border="1" id="jack">


<tr class="alt">
	<td width="25%">标题：</td><td width="75%">${text.title }</td>
</tr>
<tr class="alt">
	<td>内容：</td><td>${text.content}</td>
</tr>

<tr class="alt">
	<td>Topic：</td><td>${text.topic}</td>
</tr>
<tr class="alt">
	<td>相关与否：</td><td>
			<sf:radiobutton path="isRelated" value="相关"/>相关
            <sf:radiobutton path="isRelated" value="不相关"/>不相关
			
			
			<input type="submit"  value="确定">
</td>
</tr>
</table>
</sf:form>



</body>
</html>