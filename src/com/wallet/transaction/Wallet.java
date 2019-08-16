package com.wallet.transaction;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
	
	public PrivateKey privateKey;
	
	public PublicKey publicKey;
	
	public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	
	public String walletName;
	
	public Date expiratonDate;
	
	public Wallet() {
		generateKeyPair();
	}
	
	public Wallet(String name)
	{
		this.walletName=name+"_"+Math.random();
		generateKeyPair();
	}
	
	
	public Date getExpiratonDate() {
		return expiratonDate;
	}

	public void setExpiratonDate(Date expiratonDate) {
		this.expiratonDate = expiratonDate;
	}

	public PublicKey getWalletPublicKey(String walletName)
	{
		return this.publicKey;
	}
	public void generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random); //256 
	        KeyPair keyPair = keyGen.generateKeyPair();
	        // Set the public and private keys from the keyPair
	        privateKey = keyPair.getPrivate();
	        publicKey = keyPair.getPublic();
	        
	        
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	public float getBalance() 
	{
		return getBalance(null);
	}	
	public float getBalance(HashMap<String,TransactionOutput> userTrans) {
		float total = 0;	
		
		if(userTrans!=null && userTrans.size()>0)
		{
    	for (Map.Entry<String, TransactionOutput> item: userTrans.entrySet()){
        	TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
            	UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
            	total += UTXO.value ; 
            }
        }	
		}
		else
		{
		    for (Map.Entry<String, TransactionOutput> item: Main.UTXOs.entrySet()){
	        	TransactionOutput UTXO = item.getValue();
	            if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
	            	UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
	            	total += UTXO.value ; 
	            }
	        }	    
		}
		return total;
	}
	public Transaction sendFunds(PublicKey _recipient,float value) {
	return 	sendFunds(_recipient,value,null);
	}
	public Transaction sendFunds(PublicKey _recipient,float value,HashMap<String,TransactionOutput> userTrans)
	{
		return 	sendFunds(_recipient,value,userTrans,null,null);
	}
	public Transaction sendFunds(PublicKey _recipient,float value,HashMap<String,TransactionOutput> userTrans,String senderWalletName,String recipientWalletName) 
	{
		if(userTrans!=null)
		{
			if(getBalance(userTrans) < value) {
				System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
				return null;
			}	
		}
		else
		{
		if(getBalance() < value) {
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}
		}
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		
		float total = 0;
		for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
			TransactionOutput UTXO = item.getValue();
			total += UTXO.value;
			inputs.add(new TransactionInput(UTXO.id));
			if(total > value) break;
		}
	    
		Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs,new Date(),senderWalletName,recipientWalletName);
		newTransaction.setRemarks(senderWalletName+" transfer funds "+value+" to "+recipientWalletName);
		newTransaction.generateSignature(privateKey);
		
		for(TransactionInput input: inputs){
			UTXOs.remove(input.transactionOutputId);
		}
		
		return newTransaction;
	}
	
	public String getWalletInformation()
	{
		return getWalletInformation(null);
	}
	
	public String getWalletInformation(HashMap<String,TransactionOutput> UTXOs)
	{
		return privateKey+"<br/>Wallet Balance:"+getBalance(UTXOs);
	}
	
}
