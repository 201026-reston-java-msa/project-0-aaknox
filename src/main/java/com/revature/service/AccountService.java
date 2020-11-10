package com.revature.service;

import java.util.List;

import com.revature.model.Account;
import com.revature.model.BankTransaction;

public interface AccountService {
	// crud methods
	// create
	public void accountCreate(Account acc, String uname);

	// read
	public List<Account> getAllAccounts();

	public Account getAccountByAccountId(int accountId);

	public List<Account> viewAllAccountsByStatus(String status);

	public List<Account> viewAccountsByUserId(int uid);

	public int findOwnerIdOfAccount(Account account);
	
	public List<BankTransaction> showTransactionHistory(int accountId);

	// update
	public void modifyAccountStatus(String status, int id, int messageCode);

	// delete
	public void removeAccountByAccountId(int id);

	// other business logic methods
	public void makeDeposit(double addedCash, int id, int messageCode);

	public void makeWithdraw(double subbedCash, int id, int messageCode);

	public void makeTransfer(double amount, int fromId, int toId);
}
