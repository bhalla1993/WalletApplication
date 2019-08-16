<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.wallet.transaction.StringUtil" %> 
<%@page import="com.wallet.transaction.Transaction" %>
<%@page import="java.util.HashMap" %> 
<%@page import="java.text.SimpleDateFormat" %>
 

<!DOCTYPE html>
<html>
<head>
<%
String username=(String)session.getAttribute("username");

if(!StringUtil.isValidString(username))
	username=""; 

System.out.println("sdasd: "+request.getAttribute("transactionsMap"));


HashMap<String,Transaction> transactionsMap=(HashMap<String,Transaction>)request.getAttribute("transactionsMap");
System.out.println("transactionsMap in jsp : "+transactionsMap);
SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  

 
%>
<meta charset="ISO-8859-1">
<title>Wallet Application</title>
<style type="text/css">
table tr td
{
word-break:break-all;
}  
</style>
</head>
<body style="margin:0px auto;padding:0px auto">
<div style="width:100%;height:auto;">
<div style="background-color:lightgrey;color:white;width:80%;margin:70px 0px 0px 100px;height:auto;border:1px medium black" align="center">
 
<div style="width:auto;height:auto;text-align:left;padding:20px;color:black">
<h2 align="center">Welcome to Wallet Application</h2> 

	<div align="center" style="margin-top:35px;"> 
		<%@include file="menu.jsp"%>
		<h3 align="left">Hello <%=username%>,</h3>
		<%
		String message=(String)request.getAttribute("message");
		String errorMessage=(String)request.getAttribute("errorMessage");
		
		if(StringUtil.isValidString(message))
		{
		%>
		<p><span style="color:#006400"><%=message%></span>
		<%}
		if(StringUtil.isValidString(errorMessage))
		{
		%>
		<p><span style="color:red"><%=errorMessage%></span>
		<%}%>
		
		<%
		if(transactionsMap!=null && transactionsMap.size()>0)
		{
		%>
		<table border="1">
		<tr>
		<td colspan="5"><h3 style="text-align:center;"><%=username%>'s All Wallets Transactions List</h3></td>
		</tr>
		
		<tr>
		<%--<th style="width:10px">Transaction Id</th>--%>		
		<th>Sender Wallet</th>
		<th>Recipient Wallet</th>
		<th>Amount Sent</th>
		<th>Transaction Date</th>
		<th style="width:200px !important">Remarks</th>
		
		</tr>
		<%
		for(int i=0;i<transactionsMap.size();i++)
		{
			Transaction t=(Transaction)transactionsMap.get(i+"");
		%>
			<tr> 
			<%--<td style="width:10px"><%=t.transactionId%></td>--%>
			<td><%=t.senderWalletName%></td>
			<td><%=t.recipientWalletName%></td> 
			<td><%=t.value%></td>
			<td><%=formatter.format(t.transactionDateTime) %></td>
			<td style="width:200px !important"><%=t.getRemarks()%></td>
			</tr>		 
		<% 
		}%>
		
		</table>
		<%
		} else
		{%>
		<table>
		<tr>
		<td>No transaction has been recorded yet for any wallet.</td>
		</tr>
		</table>
		
		<%} %>
	
	</div>
 
</div>
</div>
</div>
</body>
</html>