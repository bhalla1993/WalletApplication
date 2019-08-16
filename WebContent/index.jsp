<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="com.wallet.transaction.StringUtil" %> 
    
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<%
String fromWhere=(String)request.getAttribute("fromWhere");
String message="";

if(StringUtil.isValidString(fromWhere))
{
	if("invalidUsername".equals(fromWhere))
	{
		message="Please enter valid username to proceed!";
	}
	
}


%>
<title>Wallet Application</title>
<script type="text/javascript">
function validate()
{
	var f=document.welcomeForm;
	var uName=f.username.value;
	
	if(uName=="" || uName=="undefined" || uName=="null")
	{
		alert("Please enter your name!");
		f.username.focus();
		return false;	
	}
}
</script>
</head>
<body style="margin:0px auto;padding:0px auto">
<div style="width:100%;height:auto;">
<div style="background-color:lightgrey;color:white;width:80%;margin:70px 0px 0px 100px;height:300px;border:1px medium black" align="center">

<div style="width:auto;height:auto;text-align:left;padding:20px;color:black">
<h2 align="center">Welcome to Wallet Application</h2> 
<div align="center" style="margin-top:35px"> 
<span style="color:red;font-size:large;"><%=message%></span> 
<form name="welcomeForm" action="${pageContext.request.contextPath}/welcome" method="get">
<span>Enter your name:</span>  
 
<input type="text" name="username" value="" required="required" style="width:30%;height:30px;"/>
<br/> 
<button onclick="javascript:return validate()" name="submitBtn" style="cursor:pointer;background-color:cadetblue;width:20%;margin-left:110px;margin-top:15px;height:50px">Start Session</button>
</form>       
</div>

</div>
</div>
</div>
</body>
</html>