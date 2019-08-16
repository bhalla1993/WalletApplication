package com.wallet.servlets;

import java.io.IOException;

import java.security.Security;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wallet.transaction.StringUtil;
import com.wallet.transaction.Wallet;
import java.util.HashMap;
import com.wallet.beans.User;

/**
 * Servlet implementation class WalletServlet
 */
public class WalletServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WalletServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		ServletContext sc=request.getServletContext();
		
		HashMap<String,User> userMap=(HashMap<String,User>)sc.getAttribute("userMap");
		String username=(String)request.getSession().getAttribute("username");
		
		if(userMap!=null && StringUtil.isValidString(username) && userMap.containsKey(username))
		{	
		User myUser=(User)userMap.get(username);
			if(myUser!=null)
			{
				request.setAttribute("userTransactionMaps", myUser.getUTXOs());
				request.setAttribute("userWallet", myUser.getUserWallets());
			}
		}
		request.getRequestDispatcher("wallet.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
