package com.wallet.user;

import java.io.IOException;
import java.security.Security;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wallet.transaction.Block;
import com.wallet.transaction.Constant;
import com.wallet.transaction.Main;
import com.wallet.transaction.StringUtil;
import com.wallet.transaction.Transaction;
import com.wallet.transaction.TransactionOutput;
import com.wallet.transaction.Wallet;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import com.wallet.beans.User;

/**
 * Servlet implementation class WelcomeServlet
 */
public class WelcomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public Transaction genesisTransaction;

    public WelcomeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
		String username=request.getParameter("username");
		
		HttpSession session=request.getSession();
		ServletContext sc=request.getServletContext();
		HashMap<String,User> userMap=(HashMap<String,User>)sc.getAttribute("userMap");
		
		if(userMap!=null && StringUtil.isValidString(username))
		{
		
		if(!userMap.containsKey(username))
		{
		session.setAttribute("username", username);
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
		User newUser=new User();
		newUser.setUsername(username);
		
		/*Wallet w1=new Wallet();
		User newUser=new User();
		newUser.setUsername(username);
		newUser.setUserWallet(w1);
		
		
		//create genesis transaction, which sends 100 NoobCoin to walletA: 
				genesisTransaction = new Transaction(userMap.get("admin").getUserWallet().publicKey, w1.publicKey, 100f, null);
				genesisTransaction.generateSignature(userMap.get("admin").getUserWallet().privateKey);	 //manually sign the genesis transaction	
				genesisTransaction.transactionId = "0"; //manually set the transaction id
				genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
				
				HashMap<String,TransactionOutput> UTXOs=newUser.getUTXOs();
				UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
				newUser.setUTXOs(UTXOs);
				
				
				ArrayList<Block> blockchain=newUser.getBlockchain();
				
				System.out.println("Creating and Mining Genesis block... ");
				Block genesis = new Block("0");
				genesis.addTransaction(genesisTransaction);
				
				addBlock(genesis,blockchain); 
				
				newUser.setBlockchain(blockchain);
				
		*/
		userMap.put(username, newUser);
		sc.setAttribute("userMap", userMap);
		
		//response.sendRedirect(request.getContextPath()+"/main.jsp");
		request.getRequestDispatcher("main.jsp").forward(request, response);
		}
		else 
		{
			session.setAttribute("username", username);
			request.getRequestDispatcher("main.jsp").forward(request, response);
			
		}
		}
		else
		{
			//response.sendRedirect(request.getContextPath()+"/index.jsp?fromWhere=invalidUsername");				
			request.setAttribute("fromWhere", "invalidUsername");
			request.getRequestDispatcher("/").forward(request, response);
		}
		
	}
	protected void addBlock(Block newBlock,ArrayList<Block> blockchain) {
		newBlock.mineBlock(Constant.DIFFICULTY);
		blockchain.add(newBlock);		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
