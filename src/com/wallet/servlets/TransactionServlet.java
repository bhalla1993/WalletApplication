package com.wallet.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wallet.beans.User;
import com.wallet.transaction.Block;
import com.wallet.transaction.Transaction;
import com.wallet.transaction.TransactionOutput;

/**
 * Servlet implementation class TransactionServlet
 */
@WebServlet("/TransactionServlet")
public class TransactionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TransactionServlet() {
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
		
		HashMap<String,Transaction> transactionsMap=new HashMap<String,Transaction>();
		
		ArrayList<Block> userBlocks=(ArrayList<Block>)userMap.get(username).getBlockchain();
		
		
		ArrayList<Transaction> transactionsList=new ArrayList<Transaction>();
		
		if(userBlocks!=null)
		{
			for(int i=0;i<userBlocks.size();i++)
			{
			ArrayList<Transaction> transactions=(ArrayList<Transaction>)userBlocks.get(i).transactions;
				
			if(transactions!=null)
			{
				for(int j=0;j<transactions.size();j++)
				{
					Transaction t=(Transaction)transactions.get(j);					
					/*transactionsMap.put(i+"", t);*/
					transactionsList.add(t);
				}
			}
			}
			if(transactionsList!=null)
			{				
				Collections.sort(transactionsList, 
						(o1, o2) -> o2.transactionDateTime.compareTo(o1.transactionDateTime));
						
				for(int j=0;j<transactionsList.size();j++)
				{
					Transaction t=(Transaction)transactionsList.get(j);					
					transactionsMap.put(j+"", t);
				}
			}
		}
		
		request.setAttribute("transactionsMap", transactionsMap);
		request.getRequestDispatcher("transaction.jsp").forward(request, response);
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
