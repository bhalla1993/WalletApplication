<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.wallet.transaction.StringUtil" %> 
<%@page import="com.wallet.transaction.Wallet" %>
<%@page import="com.wallet.transaction.TransactionOutput" %>
<%@page import="java.util.HashMap" %>
<%@page import="com.wallet.beans.User" %>

<!DOCTYPE html>
<html>
<head>
<%
String username=(String)session.getAttribute("username");

if(!StringUtil.isValidString(username))
	username="";

ServletContext sc=request.getServletContext();
HashMap<String,User> userMap=(HashMap<String,User>)sc.getAttribute("userMap");

Wallet w1=(Wallet)request.getAttribute("userWallet");
HashMap<String,Wallet> recipientsWalletMap=(HashMap<String,Wallet>)request.getAttribute("recipientsWalletMap");

HashMap<String,Wallet> senderWalletMap=(HashMap<String,Wallet>)request.getAttribute("senderWalletMap");

System.out.println("Sender Wallet Map in sendMoney.jsp file: "+senderWalletMap);
System.out.println("Recipient Wallet Map in sendMoney.jsp file: "+recipientsWalletMap);


System.out.println("Sender Wallet Name: "+request.getAttribute("senderWalletName"));
%>
<meta charset="ISO-8859-1">
<title>Wallet Application</title>
<script type="text/javascript">
function beginTransaction(name)
{
	window.open("transfer.jsp?recipientUsername="+name,null,"height=500,width=800,status=yes,toolbar=no,menubar=no,location=no");
}
function transferMoney(recipientWalletName,senderWalletName)
{
	/*alert("recipientWalletName:"+recipientWalletName);
	alert("senderWalletName:"+senderWalletName);
	*/
	
	var moneyToSend=document.getElementById(recipientWalletName+"_amount").value;
	if(moneyToSend==0 || moneyToSend=='' || moneyToSend=='undefined')
	{
		alert("Please enter amount to send!");
		document.getElementById(recipientWalletName+"_amount").focus();
		return false;	
	}
	
	var f=document.transferForm;
	f.senderWalletName.value=senderWalletName;
	f.recipientWalletName.value=recipientWalletName;
	f.amount.value=moneyToSend;
	f.submit();
}
</script>
<style type="text/css">
table tr td
{
width:30%;
word-break:break-all;
}
</style>
<%
String senderWalletName="";
%>
</head>
<body style="margin:0px auto;padding:0px auto">
<div style="width:100%;height:auto;">
<div style="background-color:lightgrey;color:white;width:80%;margin:70px 0px 0px 100px;height:300px;border:1px medium black" align="center">

<div style="width:auto;height:auto;text-align:left;padding:20px;color:black">
<h2 align="center">Welcome to Wallet Application</h2> 

	<div align="center" style="margin-top:35px;"> 
		<%@include file="menu.jsp"%>
		<h3 align="left">Hello <%=username%>,</h3>
 		
		<%
		if(recipientsWalletMap!=null && recipientsWalletMap.size()>0)
		{ %>
		<p style="color:brown">Please choose recipient from following present recipients to send money.</p>		
		<h4>Sender Wallet Detail:</h4>
		
		<%for(String name : senderWalletMap.keySet()) { 
		Wallet senderWal=(Wallet)senderWalletMap.get(name);
		senderWalletName=senderWal.walletName;
		%>
		<p>Wallet Name: <%=senderWal.walletName %> <br/>
		Wallet Public Key: <%=senderWal.publicKey %>	
		</p>
		<%} %>
		<table border="1"> 
		<tr>
		<th>Recipients Wallet Detail</th>
		<th>Action</th>
		</tr>
		<%
		for(String name : recipientsWalletMap.keySet()) {
			
			Wallet wallet=(Wallet)recipientsWalletMap.get(name);
		%>
		<tr>
		<td>Name: <%=wallet.walletName%> <br/> Public Key : <%=wallet.getWalletPublicKey(wallet.walletName)%></td>
		<td>
		<a onclick="javascript:beginTransaction('<%=name%>')" 
		style="cursor:pointer;text-decoration:underline;color:blue">
		
		</a>
		<p>Enter Amount to send: </p>
		<input type="number" name="<%=wallet.walletName%>_amount" id="<%=wallet.walletName%>_amount" value=""/>
		<button onclick="javascript:return transferMoney('<%=wallet.walletName%>','<%=senderWalletName%>')" style="text-align:center; cursor:pointer;font-size: 12px;font-family: sans-serif;width: 30%;"  name="<%=wallet.walletName%>_submitBtn">Transfer</button>
		</td> 
		</tr> 
		<%}%>
		</table>
		<% 
		 }else{ %>
		<p style="margin-top:35px"><b>No other user/wallet is present in system to transfer money. Please contact administrator.</b></p>
		<a href="${pageContext.request.contextPath}/wallet">Back</a>
		<%} %> 
	</div>
 		<form name="transferForm" action="${pageContext.request.contextPath}/transfer" method="post">
 		<input type="hidden" name="senderWalletName" value=""/>
 		<input type="hidden" name="recipientWalletName" value=""/>
 		<input type="hidden" name="amount" value=""/>
 		</form>
</div>
</div>
</div>
</body>
</html>