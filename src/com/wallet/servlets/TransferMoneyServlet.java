package com.wallet.servlets;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wallet.beans.User;
import com.wallet.transaction.Block;
import com.wallet.transaction.Constant;
import com.wallet.transaction.StringUtil;
import com.wallet.transaction.Wallet;

/**
 * Servlet implementation class TransferMoneyServlet
 */
public class TransferMoneyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TransferMoneyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
		String senderWalletName=request.getParameter("senderWalletName");
		String recipientWalletName=request.getParameter("recipientWalletName");
		
		String amount=request.getParameter("amount");
		System.out.println("senderWalletName:"+senderWalletName+"\n recipientWalletName:"+recipientWalletName+" \n Amount:"+amount);
		
		ServletContext sc=request.getServletContext();
		
		HashMap<String,User> userMap=(HashMap<String,User>)sc.getAttribute("userMap");
		String username=(String)request.getSession().getAttribute("username");
		
		ArrayList<Block> blockChain=(ArrayList<Block>)userMap.get(username).getBlockchain();
		
		if(blockChain!=null)
		{
			Block lastElem=(Block)blockChain.get(blockChain.size()-1);
			Block newBlock=new Block(lastElem.hash);
			
			ArrayList<Wallet> userWallets=(ArrayList<Wallet>)userMap.get(username).getUserWallets();
			Wallet senderWallet=null;
			Wallet recipientWallet=null;
			
			for (Wallet wallet : userWallets) {
					if(wallet!=null)
					{
						if(wallet.walletName.equals(senderWalletName))
							senderWallet=wallet;
						if(wallet.walletName.equals(recipientWalletName))
							recipientWallet=wallet;
					}
			}
			
			if(senderWallet!=null && recipientWallet!=null && StringUtil.isValidString(amount))
			{
			Float amnt=Float.parseFloat(amount);				
			boolean isTransactionSucceed=false;
			
			isTransactionSucceed = newBlock.addTransaction(senderWallet.sendFunds(recipientWallet.publicKey, amnt,userMap.get(username).getUTXOs(),senderWalletName,recipientWalletName),userMap.get(username).getUTXOs());
			
			newBlock.mineBlock(Constant.DIFFICULTY);
			blockChain.add(newBlock);	
		
			userMap.get(username).setBlockchain(blockChain);//Again set new blockChain for user
			
			if(isTransactionSucceed)
				request.setAttribute("message", "Money Transfer successfully!");
			else
				request.setAttribute("errorMessage", "Transaction failed to process. Discarded either due to insufficient funds or Signature failed.");
			
			request.getRequestDispatcher("/transaction").forward(request, response);
			}
		
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
