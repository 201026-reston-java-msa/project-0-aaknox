package com.revature.dao;

import java.util.List;

import com.revature.model.Account;
import com.revature.model.BankTransaction;

public interface AccountDao {
	// CREATE
	public void insertAccount(Account acc, String uname);

	// READ
	public List<Account> selectAllAccounts();
	
	public int selectUserIdByAccountId(int accId);

	public Account selectAccountByAccountId(int id);

	public List<Account> selectAllAccountsByUsername(String user);

	public List<Account> selectAllAccountsByStatus(String status);

	public List<Account> selectAllAccountsByUserID(int userid);
	
	public List<BankTransaction> transactionHistoryByAccountId(int accountId);

	// UPDATE
	public void updateAccountBalance(double balance, int id, int messageCode);
	
	public void updateAccountStatus(String status, int id, int messageCode);
	
	public void transferRequestFunc(double amount, int fromId, int toId);

	// DELETE
	public void deleteAccountByAccountId(int id);
}
