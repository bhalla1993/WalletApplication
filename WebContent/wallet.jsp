<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.wallet.transaction.StringUtil" %> 
<%@page import="com.wallet.transaction.Wallet" %>
<%@page import="com.wallet.transaction.TransactionOutput" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html>
<head>
<%
String username=(String)session.getAttribute("username");

if(!StringUtil.isValidString(username))
	username="";

ArrayList<Wallet> walletList=(ArrayList<Wallet>)request.getAttribute("userWallet");

HashMap<String, TransactionOutput> userTransactionMaps=(HashMap<String, TransactionOutput>)request.getAttribute("userTransactionMaps");
SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  

 
%> 
<meta charset="ISO-8859-1">
<title>Wallet Application</title>
<script type="text/javascript">
function toggleElement(id)
{
	 var e = document.getElementById(id);
     if(e.style.display == 'block')
     {
    	 e.style.display = 'none';
    	 document.getElementById("addNewLink").style.display='block';
    	 document.getElementById("contentDiv").style.display='block';
     }
     else
     {
    	 e.style.display = 'block';
    	 document.getElementById("addNewLink").style.display='none';
    	 document.getElementById("contentDiv").style.display='none';
     }
}

function beginTransaction(val)
{
	var f=document.sendMoneyForm;
	f.senderWalletName.value=val;
	f.submit();
}
function validate()
{
	var f=document.createWalletForm;
	var walletAmount=f.walletAmount.value;
	var walletName=f.walletName.value;
	
	if(walletName=="" || walletName=="undefined" || walletName=="null")
	{
		alert("Please enter wallet name!");
		f.walletName.focus();
		return false;
	}
	if(walletAmount=="" || walletAmount=="undefined" || walletAmount=="null")
	{
		alert("Please enter wallet amount!");
		f.walletAmount.focus();
		return false;
	}
	else
	{
		var x = parseFloat(walletAmount);
		if (isNaN(x)) 
		{
			alert("Wallet Amount must be number.");
			f.walletAmount.focus();
			return false;
			
		}
		else
		{
			if(x<0.1 || x>100000)
			{
				alert("Wallet Amount must be in range from (0.1 - 100000)");
				f.walletAmount.value="";
				f.walletAmount.focus();
				return false;	
			}
			else
			{
				f.submit();	
			}
		}
		
	}
}
</script>
<style type="text/css">
#mainTable 
{
width:35%;
}
#mainTable tr td
{
padding:10px;
}

#createWalletBtn
{
cursor:pointer;background-color:cadetblue;width:40%;height:30px;
}
#contentDiv table tr td
{
width:20%;
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
		
		<%if(!"admin".equals(username)){ %>
		<p id="addNewLink"><a onclick="javascript:toggleElement('formDiv')" style="text-decoration:underline;cursor:pointer;color:blue;text-align:right;">Add New Wallet</a></p>
		<%} %>
		
		<div id="formDiv" style="display:none">
		<form name="createWalletForm" action="${pageContext.request.contextPath}/createWallet" method="post">
		<table id="mainTable"> 
		<tr> 
		<td colspan="2" style="text-align:center;">
		<h3>Add Wallet</h3> 
		</td>
		</tr>
		<tr>
		<td>Wallet Name<span style="color:red">*</span>:</td>
		<td><input type="text" name="walletName" style="width:100%;height:35px" required="required"/> </td>
		</tr>
		
		<tr>
		<td>Enter Wallet Initial Amount<span style="color:red">*</span>:</td>
		<td><input type="number" name="walletAmount" style="width:100%;height:35px" required="required" min="0.1" max="100000"/> </td>
		</tr>
		 
		<tr>
		<td colspan="2" style="text-align:center;" >
		<button id="createWalletBtn" name="createWalletBtn" onclick="javascript:return validate()" style="text-align: center;font-size: 12px;font-family: sans-serif;width:30%;height:35px">ADD</button>
		</td> 
		</tr> 
		
		<tr>
		<td colspan="2">
		<a href="${pageContext.request.contextPath}/wallet">Back</a>
		</td>
		</tr>
		
		</table> 
		</form> 
		</div>
		
		<div id="contentDiv">
		<%  
		if(walletList!=null)
		{
		%>
		<table border="1">
		<tr><td colspan="6"><h3 style="text-align:center;"><%=username%>'s Wallet(s) Information: </h3></td></tr>
		 
		<%if(walletList.size()>0){ 
			%>
			<tr>
			<th>Wallet Name</th>
			<th>Public Key</th>
			<th>Private Key</th> 
			<th>Balance</th>
			<th>Expired On</th>
			<%if(!"admin".equals(username)){ %>
			<th>Action</th>
			<%}%>
			</tr> 
			<%
			for(int i=0;i<walletList.size();i++){	 
			%>
			<tr>
			<td><%=walletList.get(i).walletName%></td>
			<td><%=walletList.get(i).publicKey%></td>
			<td><%=walletList.get(i).privateKey%></td>
			<td><%=walletList.get(i).getBalance(userTransactionMaps)%></td>
			<td><%=formatter.format(walletList.get(i).getExpiratonDate())%></td>
			<%if(!"admin".equals(username)){ %>
			<td><a onclick="javascript:beginTransaction('<%=walletList.get(i).walletName%>')" style="cursor:pointer;text-decoration:underline;color:blue">Send Money</a></td>	
			<%}%>		
			</tr>       
			<%	
			}
			
		}
		else
		{
			%>
			<tr>
			<td colspan="6">There is no wallet present for you. Please add a new one!</td>
			</tr>
			<%
		}
		%>
		</table>
		
		<%}
		
		else
		{
			%>
			<table style="padding-top:30px;">
			<tr>  
			<td style="text-align:center;"><b>There is no wallet present for you. Please add a new one!</b></td>
			</tr> 
			</table>
			<%
		}
		%>
		</div>
		
		<form name="sendMoneyForm" action="${pageContext.request.contextPath}/sendMoney" method="post">
		<input type="hidden" name="senderWalletName" value=""/>		
		</form>
		
	</div>
 
</div>
</div>
</div>
</body>
</html>