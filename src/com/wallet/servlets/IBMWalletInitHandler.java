package com.wallet.servlets;

import java.io.IOException;
import java.security.Security;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.wallet.beans.User;
import com.wallet.transaction.Block;
import com.wallet.transaction.Constant;
import com.wallet.transaction.Main;
import com.wallet.transaction.Transaction;
import com.wallet.transaction.TransactionOutput;
import com.wallet.transaction.Wallet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;  
import javax.servlet.ServletContextListener;


/**
 * Servlet implementation class IBMWalletInitServlet
 */
public class IBMWalletInitHandler implements ServletContextListener  {
	private static final long serialVersionUID = 1L;
   
	public void contextInitialized(ServletContextEvent servletContextEvent) {

		ServletContext sc= servletContextEvent.getServletContext();
		User user=new User();
		user.setUsername("admin");
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
		Wallet w1=new Wallet("ShivaWallet");//Admin Wallet
		user.setUserPublicKey(w1.publicKey);
		Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
		calendar.add(Calendar.YEAR,1);
		w1.setExpiratonDate(calendar.getTime());
		
		ArrayList<Wallet> a1=new ArrayList<Wallet>();
		a1.add(w1);
		user.setUserWallets(a1);
		
		Map<String,User> userMap=new HashMap<String,User>();
		userMap.put(user.getUsername(), user);
		
		sc.setAttribute("userMap",userMap);
		
		ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
		exec.scheduleAtFixedRate(new Runnable() {
			  @Override
			  public void run() {
				  try {
			    // do stuff
				 // System.out.println("##############Thread is calling##############");
				  
				  HashMap<String,User> users=(HashMap<String,User>)sc.getAttribute("userMap");
			       Iterator usersIterator = users.entrySet().iterator(); 
				  while (usersIterator.hasNext()) 
				  {
					  Map.Entry mapElement = (Map.Entry)usersIterator.next(); 
			          User u=(User)mapElement.getValue();
			          HashMap<String, TransactionOutput> userTrans=null;
			          userTrans=u.getUTXOs();
			          
			          ArrayList<Wallet> userWallets=(ArrayList<Wallet>)u.getUserWallets();
				      if(userWallets!=null)
				      {
				      for (Wallet wallet : userWallets) 
				      {
				    	  //System.out.println("Current Time: "+java.util.Calendar.getInstance().getTime());
				    	  //System.out.println("Wallet Name: "+wallet.walletName+"\nWallet Expiry Date: "+wallet.getExpiratonDate());
				    	  
				    	 // System.out.println("Compare current date & expiration date : "+wallet.getExpiratonDate().compareTo(java.util.Calendar.getInstance().getTime()));
				    	  
				    	  if(wallet.getExpiratonDate().compareTo(java.util.Calendar.getInstance().getTime())<=0)
				    	  {
				    		
				    		  ArrayList<Block> blockchain=userMap.get("admin").getBlockchain();
				    		  Transaction genesisTransaction;
			    				HashMap<String,TransactionOutput> UTXOs=userMap.get("admin").getUTXOs();

				    		  if(blockchain!=null && blockchain.size()>0)
				    		  {
				    			  
				    			Transaction t=  new Transaction(wallet.publicKey, userMap.get("admin").getUserWallets().get(0).publicKey, wallet.getBalance(userTrans), null,new Date(),wallet.walletName,userMap.get("admin").getUserWallets().get(0).walletName);
				    			t.setRemarks("Balance transfer from "+wallet.walletName+" to ShivaWallet due to its expiration.");
				    			genesisTransaction =t;
				  				genesisTransaction.generateSignature(wallet.privateKey);	 //manually sign the genesis transaction	
				  				genesisTransaction.transactionId = UTXOs.size()+""; //manually set the transaction id
				  				genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
				  				 
								UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.

			    				userMap.get("admin").setUTXOs(UTXOs);
			    				
			    				Block newBlock = new Block(blockchain.get(blockchain.size()-1).hash);
			    				newBlock.addTransaction(genesisTransaction,userMap.get("admin").getUTXOs(),"0");
			    				
			    				addBlock(newBlock,blockchain);
			    				userMap.get("admin").setBlockchain(blockchain);
				    		  }
				    		  else
				    		  {
				    				
				    			  Transaction t =new Transaction(wallet.publicKey, userMap.get("admin").userPublicKey, wallet.getBalance(userTrans), null,new Date(),wallet.walletName,userMap.get("admin").getUserWallets().get(0).walletName);
				    			  t.setRemarks("Balance transfer from "+wallet.walletName+" to ShivaWallet due to its expiration.");
					    				
				    			  	genesisTransaction = t;
				    				genesisTransaction.generateSignature(wallet.privateKey);	 //manually sign the genesis transaction	
				    				genesisTransaction.transactionId = "0"; //manually set the transaction id
				    				genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
				    					

				    				UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
				    				userMap.get("admin").setUTXOs(UTXOs);
				    				
				    				//System.out.println("Creating and Mining Genesis block... ");
				    				Block genesis = new Block("0");
				    				genesis.addTransaction(genesisTransaction,userMap.get("admin").getUTXOs());
				    				
				    				addBlock(genesis,blockchain); 
				    				
				    				userMap.get("admin").setBlockchain(blockchain);
				    		  }

				    		
				    		  
				    		  //Finally Remove the wallet from userMap 
				    		  userWallets.remove(wallet);  
				    	  
				    	  }
				      }
				      u.setUserWallets(userWallets);
				      
				  }
			            
				}
				  	
				  }
				  catch (ConcurrentModificationException ce) 
				  {
					System.out.println("Same collection is modifying by thread on to which loop part is executing");
				  }
			   catch ( Exception e ) {
			        System.out.println( "ERROR - unexpected exception"+e.getMessage());
			       // e.printStackTrace();
			    }
				  
			  }
				  
			}, 0, 5, TimeUnit.SECONDS);
		
		
        System.out.println("Starting up!");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Shutting down!");
    }
    
    protected void addBlock(Block newBlock,ArrayList<Block> blockchain) {
		newBlock.mineBlock(Constant.DIFFICULTY);
		blockchain.add(newBlock);		
	}
    
}
