package com.wallet.servlets;

import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wallet.beans.User;
import com.wallet.transaction.StringUtil;
import com.wallet.transaction.Wallet;
import java.util.Map;

/**
 * Servlet implementation class SendMoneyServlet
 */
@WebServlet("/SendMoneyServlet")
public class SendMoneyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendMoneyServlet() { 
        super();
        // TODO Auto-generated constructor stub
    } 

	/** 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		HttpSession session=request.getSession();
		ServletContext sc=request.getServletContext();
		Map<String,User> userMap=(HashMap<String,User>)sc.getAttribute("userMap");
		
		String username=(String)request.getSession().getAttribute("username");

		String senderWalletName=request.getParameter("senderWalletName");
		System.out.println("senderPublicKey in SendMoneyServlet is: "+senderWalletName);
		
		/*Wallet senderWallet=(Wallet)obj;
		
		if(senderWallet!=null)
		{
			System.out.println("Wallet:"+senderWallet.privateKey+"\n"+senderWallet.publicKey);
		}*/

		
		if(userMap!=null && StringUtil.isValidString(username))
		{
		System.out.println("Username: "+username+"\n userMap.containsKey(username): "+userMap.containsKey(username));

			ArrayList<Wallet> userWallets=(ArrayList<Wallet>)userMap.get(username).getUserWallets();
			HashMap<String,Wallet> recipientsWalletMap=new HashMap<String,Wallet>();
			HashMap<String,Wallet> senderWalletMap=new HashMap<String,Wallet>();
			
			
			for(int i=0;i<userWallets.size();i++)
			{
				Wallet w=(Wallet)userWallets.get(i);
				if(!w.walletName.equals(senderWalletName))
				{
					recipientsWalletMap.put(w.walletName, w);
				}
				else
				{
					senderWalletMap.put(w.walletName, w);
				}
			}
	
		request.setAttribute("senderWalletName",senderWalletName);		
		request.setAttribute("recipientsWalletMap", recipientsWalletMap);
		request.setAttribute("senderWalletMap", senderWalletMap);
		request.setAttribute("transactionsMap", userMap.get(username).getUTXOs());
		
		request.getRequestDispatcher("sendMoney.jsp").forward(request, response);
		
		}
		else
		{
			//response.sendRedirect(request.getContextPath()+"/index.jsp?fromWhere=invalidUsername");				
			request.setAttribute("fromWhere", "invalidUsername");
			request.getRequestDispatcher("/").forward(request, response);
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
