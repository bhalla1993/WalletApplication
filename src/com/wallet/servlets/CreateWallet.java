package com.wallet.servlets;

import java.io.IOException;
import java.security.Security;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.wallet.transaction.Transaction;
import com.wallet.transaction.TransactionOutput;
import com.wallet.transaction.Wallet;

/**
 * Servlet implementation class CreateWallet
 */
public class CreateWallet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public Transaction genesisTransaction;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateWallet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	
		String amount=request.getParameter("walletAmount");
		ServletContext sc=request.getServletContext();
		
		HashMap<String,User> userMap=(HashMap<String,User>)sc.getAttribute("userMap");
		String username=(String)request.getSession().getAttribute("username");
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	   
		String walletName=request.getParameter("walletName");
		
		if(StringUtil.isValidString(amount))
		{		
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
			Wallet w1=new Wallet(StringUtil.isValidString(walletName)?walletName:"");
			
			Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
			calendar.add(Calendar.SECOND, Constant.EXPIRATION_TIME);
			System.out.println("Time after adding seconds: "+calendar.getTime());
			w1.setExpiratonDate(calendar.getTime());
			
			
			User newUser=(User)userMap.get(username);
			newUser.setUsername(username);
			newUser.setUserPublicKey(w1.publicKey);
			
			
			
			
			ArrayList<Wallet> listOfWallets=(ArrayList<Wallet>)newUser.getUserWallets();
			
			if(listOfWallets!=null && listOfWallets.size()>0)
			{
				listOfWallets.add(w1);
				newUser.setUserWallets(listOfWallets);
			
			//Insert already exist code here 
				ArrayList<Block> blockchain=newUser.getBlockchain();
				
				
				HashMap<String,TransactionOutput> UTXOs=newUser.getUTXOs();
				System.out.println("UTXO SIze"+UTXOs.size());
				
				Transaction t=new Transaction(userMap.get("admin").getUserWallets().get(0).publicKey, w1.publicKey, Float.parseFloat(amount), null,new Date(),userMap.get("admin").getUserWallets().get(0).walletName,w1.walletName);
				t.setRemarks("Intialized wallet balance from admin wallet transaction");
				genesisTransaction = t;
				genesisTransaction.generateSignature(userMap.get("admin").getUserWallets().get(0).privateKey);	 //manually sign the genesis transaction	
				genesisTransaction.transactionId = UTXOs.size()+""; //manually set the transaction id
				genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
				
				
				System.out.println("Genesis outputs present"+genesisTransaction.outputs);
				System.out.println("Genesis outputs present"+genesisTransaction.outputs.size());
				
				
				UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
				
				newUser.setUTXOs(UTXOs);
				
				Block newBlock = new Block(blockchain.get(blockchain.size()-1).hash);
				newBlock.addTransaction(genesisTransaction,newUser.getUTXOs(),"0");
				
				addBlock(newBlock,blockchain);
				
				newUser.setBlockchain(blockchain);
				request.getRequestDispatcher("/wallet").forward(request, response);
			}
			else
			{
				listOfWallets=new ArrayList<Wallet>();
				listOfWallets.add(w1); 
				newUser.setUserWallets(listOfWallets);
				
				//First Transaction code 

				//create genesis transaction, which sends 100 NoobCoin to walletA: 
				Transaction t=new Transaction(userMap.get("admin").getUserWallets().get(0).publicKey, w1.publicKey, Float.parseFloat(amount), null,new Date(),userMap.get("admin").getUserWallets().get(0).walletName,w1.walletName);
				t.setRemarks("Intialized wallet balance from admin wallet transaction");

				genesisTransaction = t;
				genesisTransaction.generateSignature(userMap.get("admin").getUserWallets().get(0).privateKey);	 //manually sign the genesis transaction	
				genesisTransaction.transactionId = "0"; //manually set the transaction id
				genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
				
				HashMap<String,TransactionOutput> UTXOs=newUser.getUTXOs();
				UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
				newUser.setUTXOs(UTXOs);
				
				
				ArrayList<Block> blockchain=newUser.getBlockchain();
				
				//System.out.println("Creating and Mining Genesis block... ");
				Block genesis = new Block("0");
				genesis.addTransaction(genesisTransaction,newUser.getUTXOs());
				
				addBlock(genesis,blockchain); 
				
				newUser.setBlockchain(blockchain);
				userMap.put(username, newUser);
				request.getRequestDispatcher("/wallet").forward(request, response);
				
			
			}
			
			
			
		}
	
	}
	protected void addBlock(Block newBlock,ArrayList<Block> blockchain) {
		newBlock.mineBlock(Constant.DIFFICULTY);
		blockchain.add(newBlock);		
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
