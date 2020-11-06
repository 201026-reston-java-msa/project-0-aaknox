package com.revature.serviceimpl;

import java.util.List;

import org.apache.log4j.Logger;

import com.revature.dao.AccountDao;
import com.revature.daoimpl.AccountDaoImpl;
import com.revature.model.Account;
import com.revature.service.AccountService;

public class AccountServiceImpl implements AccountService {
	private static Logger logger = Logger.getLogger(AccountServiceImpl.class);
	private AccountDao accountDao = new AccountDaoImpl();

	@Override
	public void accountCreate(Account acc, String uname) {
		logger.info("Adding account to database.");
		accountDao.insertAccount(acc, uname);
	}

	@Override
	public List<Account> getAllAccounts() {
		List<Account> accounts = accountDao.selectAllAccounts();
		return accounts;
	}

	@Override
	public Account getAccountByAccountId(int accountId) {
		Account account = accountDao.selectAccountByAccountId(accountId);
		return account;
	}

	@Override
	public List<Account> viewAllAccountsByStatus(String status) {
		List<Account> accList = accountDao.selectAllAccountsByStatus(status);
		return accList;
	}

	@Override
	public List<Account> viewAccountsByUserId(int uid) {
		List<Account> accs = accountDao.selectAllAccountsByUserID(uid);
		return accs;
	}

	@Override
	public int findOwnerIdOfAccount(Account account) {
		int ownerid = accountDao.selectUserIdByAccountId(account.getAccountId());
		return ownerid;
	}

	@Override
	public void modifyAccountStatus(String status, int id) {
		accountDao.updateAccountStatus(status, id);
	}

	@Override
	public void removeAccountByAccountId(int id) {
		accountDao.deleteAccountByAccountId(id);
	}

	@Override
	public void makeDeposit(double addedCash, int id) {
		logger.info("Sending deposit request to the database.");
		// find the account
		Account account = accountDao.selectAccountByAccountId(id);
		System.out.println(account);
		// set new balance
		account.setBalance(account.getBalance() + addedCash);
		double myNewBalance = account.getBalance();
		logger.info("New balance: " + account.getBalance());
		logger.info("Updating account balance to account number " + id);
		// update the database
		accountDao.updateAccountBalance(myNewBalance, id);
	}

	@Override
	public void makeWithdraw(double subbedCash, int id) {
		logger.info("Sending withdrawal request to the database.");
		// find the account
		Account account = accountDao.selectAccountByAccountId(id);
		System.out.println(account);
		// set new balance
		account.setBalance(account.getBalance() - subbedCash);
		double myNewBalance = account.getBalance();
		// check if new balance would be in zero or negative range before sending it to
		// dao
		logger.info("New balance: " + account.getBalance());
		logger.info("Updating account balance to account number " + id);
		// update the database
		accountDao.updateAccountBalance(myNewBalance, id);

	}

	@Override
	public void makeTransfer(double amount, int fromId, int toId) {
		accountDao.transferRequestFunc(amount, fromId, toId);
	}

}
