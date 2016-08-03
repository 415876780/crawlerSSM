<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<style type="text/css">
	     body{
	 		 background:#eff3f6;
		}       
    </style> 
</head>
<body >

<form method="post">
<div align="center">
	<p>&nbsp;</p>
	<p>主题：
	  <input type="text" name="title"/>  
	  <select name="select">  
	    <option value ="新浪新闻">新浪新闻</option>  
	    <option value ="新浪微博">新浪微博</option>  
	     <option value ="腾讯新闻">腾讯新闻</option>  
      </select>
	  <input type="submit" value="确定">
    </p>
	<p>&nbsp;</p>
	<p>&nbsp;</p>
</div>

</form>
<br/>
	<br/>
	<br/>


</body>
</html>