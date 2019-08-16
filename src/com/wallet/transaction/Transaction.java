package com.wallet.transaction;

import java.security.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Transaction {
	
	public String transactionId; //Contains a hash of transaction*
	public PublicKey sender; //Senders address/public key.
	public PublicKey reciepient; //Recipients address/public key.
	public float value; //Contains the amount we wish to send to the recipient.
	public byte[] signature; //This is to prevent anybody else from spending funds in our wallet.
	public Date transactionDateTime;
	
	public String senderWalletName;
	public String recipientWalletName;
	public String remarks;
	
	public void setRemarks(String remarks)
	{
		this.remarks=remarks;
	}
	public String getRemarks()
	{
		return this.remarks;
	}
	
	
	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();
	
	private static int sequence = 0; //A rough count of how many transactions have been generated 
	
	// Constructor: 
	public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
	}
	public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs,Date transactionDateTime) 
	{
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
		this.transactionDateTime=transactionDateTime;
		
	}
	public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs,Date transactionDateTime,String senderWalletName,String recipientWalletName) 
	{
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
		this.transactionDateTime=transactionDateTime;
		this.senderWalletName=senderWalletName;
		this.recipientWalletName=recipientWalletName;
	}
	
	public boolean processTransaction() 
	{
		return processTransaction(null);
	}
	public boolean processTransaction(HashMap<String,TransactionOutput> userTrans) {
		
		if(verifySignature() == false) {
			System.out.println("#Transaction Signature failed to verify");
			return false;
		}
				
		//Gathers transaction inputs (Making sure they are unspent):
		for(TransactionInput i : inputs) {
			
			if(userTrans!=null)
				i.UTXO = userTrans.get(i.transactionOutputId);
			else
				i.UTXO = Main.UTXOs.get(i.transactionOutputId);
				
		}

		//Checks if transaction is valid:
		if(getInputsValue() < Constant.MINIMUM_TRANSACTION) {
			System.out.println("Transaction Inputs too small: " + getInputsValue());
			System.out.println("Please enter the amount greater than " + Constant.MINIMUM_TRANSACTION);
			return false;
		}
		
		//Generate transaction outputs:
		float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
		transactionId = calulateHash();
		outputs.add(new TransactionOutput( this.reciepient, value,transactionId)); //send value to recipient
		outputs.add(new TransactionOutput( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender		
				
		//Add outputs to Unspent list
		for(TransactionOutput o : outputs) {
			if(userTrans!=null)
				userTrans.put(o.id , o);
			else
				Main.UTXOs.put(o.id , o);
		}
		
		//Remove transaction inputs from UTXO lists as spent:
		for(TransactionInput i : inputs) {
			if(i.UTXO == null) continue; //if Transaction can't be found skip it 
		
			if(userTrans!=null)
				userTrans.remove(i.UTXO.id);
			else
				Main.UTXOs.remove(i.UTXO.id);
		}
		
		return true;
	}
	
	public float getInputsValue() {
		float total = 0;
		for(TransactionInput i : inputs) {
			if(i.UTXO == null) continue; //if Transaction can't be found skip it, This behavior may not be optimal.
			total += i.UTXO.value;
		}
		return total;
	}
	
	public void generateSignature(PrivateKey privateKey) {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
		signature = StringUtil.applyECDSASig(privateKey,data);		
	}
	
	public boolean verifySignature() {
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
		return StringUtil.verifyECDSASig(sender, data, signature);
	}
	
	public float getOutputsValue() {
		float total = 0;
		for(TransactionOutput o : outputs) {
			total += o.value;
		}
		return total;
	}
	
	private String calulateHash() {
		sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
		return StringUtil.applySha256(
				StringUtil.getStringFromKey(sender) +
				StringUtil.getStringFromKey(reciepient) +
				Float.toString(value) + sequence
				);
	}
}