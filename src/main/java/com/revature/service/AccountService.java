package com.revature.service;

import java.util.List;

import com.revature.model.Account;

public interface AccountService {
	public void accountCreate(Account acc, String uname);
	
	public void makeDeposit(double balance, int id);
	public void makeWithdraw(double subbedCash, int id);
	public List<Account> viewAccountsByUserId(int uid);
	public Account getAccountByAccountId(int accountId);
	
	public List<Account> getAllAccounts();
	public int findOwnerIdOfAccount(Account account);
}
