package com.wallet.beans;
import com.wallet.transaction.Block;
import com.wallet.transaction.TransactionOutput;
import com.wallet.transaction.Wallet;

import java.util.ArrayList;
import java.util.HashMap;
import java.security.*;


public class User {

	public String username;
	public ArrayList<Wallet> userWallets;
	
	public ArrayList<Block> blockchain;
	public HashMap<String,TransactionOutput> UTXOs;
	
	public PublicKey userPublicKey;
	
	
	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public ArrayList<Wallet> getUserWallets() {
		return userWallets;
	}


	public void setUserWallets(ArrayList<Wallet> userWallets) {
		this.userWallets = userWallets;
	}


	public ArrayList<Block> getBlockchain() {
		return blockchain;
	}


	public void setBlockchain(ArrayList<Block> blockchain) {
		this.blockchain = blockchain;
	}


	public HashMap<String, TransactionOutput> getUTXOs() {
		return UTXOs;
	}


	public void setUTXOs(HashMap<String, TransactionOutput> uTXOs) {
		UTXOs = uTXOs;
	}


	public PublicKey getUserPublicKey() {
		return userPublicKey;
	}


	public void setUserPublicKey(PublicKey userPublicKey) {
		this.userPublicKey = userPublicKey;
	}


	public User() 
	{
		blockchain=new ArrayList<Block>();
		UTXOs= new HashMap<String,TransactionOutput>();
	}
	
}
