<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.wallet.transaction.StringUtil" %> 
<!DOCTYPE html>
<html>
<head>
<%
String username=(String)session.getAttribute("username");

if(!StringUtil.isValidString(username))
	username="";

ServletContext sc=request.getServletContext();

%>
<meta charset="ISO-8859-1">
<title>Wallet Application</title>

</head>
<body style="margin:0px auto;padding:0px auto">
<div style="width:100%;height:auto;">
<div style="background-color:lightgrey;color:white;width:80%;margin:70px 0px 0px 100px;height:auto;border:1px medium black" align="center">
 
<div style="width:auto;height:auto;text-align:left;padding:20px;color:black">
<h2 align="center">Welcome to Wallet Application</h2> 

	<div align="center" style="margin-top:35px;"> 
		<%@include file="menu.jsp"%>
		<h3 align="left">Hello <%=username%>,</h3>
		<p>Please select appropriate option from menu for further enquires. </p> 
	</div>
 
</div>
</div>
</div>
</body>
</html> 