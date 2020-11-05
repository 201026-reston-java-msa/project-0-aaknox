package com.revature.service;

import java.util.List;

import com.revature.model.Account;

public interface AccountService {
	public void accountCreate(Account acc, String uname);
	
	public void makeDeposit(double balance, int id);
	public void makeWithdraw(double subbedCash, int id);
	public void makeTransfer(double amount, int fromId, int toId);
	public List<Account> viewAccountsByUserId(int uid);
	public Account getAccountByAccountId(int accountId);
	public List<Account> getAllAccounts();
	public List<Account> viewAllAccountsByStatus(String status);
	public void modifyAccountStatus(String status, int id);
	public int findOwnerIdOfAccount(Account account);
	public void removeAccountByAccountId(int id);
}
