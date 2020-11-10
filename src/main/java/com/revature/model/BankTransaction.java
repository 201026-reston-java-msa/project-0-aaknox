package com.revature.model;

import java.time.LocalDateTime;

public class BankTransaction {
	private int transactionId;
	private int transactionAccountId;
	private LocalDateTime transactionTimeStamp;
	private double transactionBalance;
	private Description description;

	public BankTransaction() {
		// TODO Auto-generated constructor stub
	}

	public BankTransaction(int transactionId, int transactionAccountId, LocalDateTime transactionTimeStamp,
			double transactionBalance, Description description) {
		this.transactionId = transactionId;
		this.transactionAccountId = transactionAccountId;
		this.transactionTimeStamp = transactionTimeStamp;
		this.transactionBalance = transactionBalance;
		this.description = description;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getTransactionAccountId() {
		return transactionAccountId;
	}

	public void setTransactionAccountId(int transactionAccountId) {
		this.transactionAccountId = transactionAccountId;
	}

	public LocalDateTime getTransactionTimeStamp() {
		return transactionTimeStamp;
	}

	public void setTransactionTimeStamp(LocalDateTime transactionTimeStamp) {
		this.transactionTimeStamp = transactionTimeStamp;
	}

	public double getTransactionBalance() {
		return transactionBalance;
	}

	public void setTransactionBalance(double transactionBalance) {
		this.transactionBalance = transactionBalance;
	}

	public Description getDescription() {
		return description;
	}

	public void setDescription(Description description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "BankTransaction [transactionId=" + transactionId + ", transactionAccountId=" + transactionAccountId
				+ ", transactionTimeStamp=" + transactionTimeStamp + ", transactionBalance=" + transactionBalance
				+ ", description=" + description + "]";
	}

}
