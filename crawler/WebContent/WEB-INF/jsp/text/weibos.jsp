<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

     <style type="text/css">
	     body{
	 		 background:#eff3f6;
		}
 
        
        td
        {
			font-size:1em;
			border:1px solid #98bf21;
			padding:3px 7px 2px 7px;

            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            background-color: #ccc;
            align: center;
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


<table width="800" align="center" border="1" id="jack">

<tr>
<th width="15%">ID</th>
<th width="22%">用户</th>
<th width="47%">内容</th>
<th width="16%">时间</th>

</tr>
<c:if test="${pager.total le 0 }" >
	<tr>
		<td colspan="4">目前还没有文本数据</td>
	</tr>
</c:if>

<c:if test="${pager.total gt 0 }" >
	<c:forEach items="${pager.datas}" var="t">
		<tr class="alt">
		<td>${t.id}</td>
		<td >${t.user }</td>
		<td style="white-space:nowrap;overflow:hidden;text-overflow:ellipsis;" title="jack"><a href="${t.id}/weibos">${t.content}</a></td>
		<td>${t.time}</td>
		</tr>
	</c:forEach>
	</tr>
	<tr class="alt"><td colspan="4">
	
	<jsp:include page="/inc/pager.jsp">
		<jsp:param value="weibos" name="url"/>
		<jsp:param value="${pager.total}" name="items"/>
	</jsp:include>
	</td>
	</tr>
</c:if>

</table>
</body>
</html>