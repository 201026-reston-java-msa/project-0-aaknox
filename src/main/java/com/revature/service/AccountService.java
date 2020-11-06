package com.revature.service;

import java.util.List;

import com.revature.model.Account;

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

	// update
	public void modifyAccountStatus(String status, int id);

	// delete
	public void removeAccountByAccountId(int id);

	// other business logic methods
	public void makeDeposit(double balance, int id);

	public void makeWithdraw(double subbedCash, int id);

	public void makeTransfer(double amount, int fromId, int toId);
}
