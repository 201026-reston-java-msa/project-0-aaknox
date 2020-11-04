package com.revature.dao;

import java.util.List;

import com.revature.model.Account;

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

	public List<Account> selectAccountsByType(String type);

	// UPDATE
	public void updateAccountBalance(double balance, int id);

	public void updateAccount(Account enteredAccount, int id);

	// DELETE
	public void deleteAccountById(int id);
}
