<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="com.wallet.transaction.Constant" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title></title>
<style type="text/css">
ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  overflow: hidden;
  background-color: #333333;
}

li {
  float: left;
  width: 20%;
}

li a {
  display: block;
  color: white;
  text-align: center;
  padding: 16px;
  text-decoration: none;
}
li a:hover {
  background-color: #111111;
}   

</style>
</head>
<body>
<p style="color:#006400">
<span style="color:black"><b>Note:</b></span>
<span>The expiration time set for wallet is <%=Constant.EXPIRATION_TIME%> seconds. Wallet will automatically be expired on time and will no longer be visible in wallet(s) section.</span> 
</p>
<ul>
		<li>
		<a href="${pageContext.request.contextPath}/main.jsp">Home</a>  
		</li>
		<li>
		<a href="${pageContext.request.contextPath}/wallet">My Wallet(s)</a>
		</li>
		<li>
		<a href="${pageContext.request.contextPath}/transaction">Transaction(s)</a>  
		</li>
		<li>
		<a href="${pageContext.request.contextPath}/logout">Log Out</a> 
		</li>		
		</ul>
</body>
</html>