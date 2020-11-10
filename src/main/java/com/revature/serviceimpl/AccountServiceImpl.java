package com.revature.serviceimpl;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.dao.AccountDao;
import com.revature.daoimpl.AccountDaoImpl;
import com.revature.model.Account;
import com.revature.model.BankTransaction;
import com.revature.security.BankException;
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
	public void modifyAccountStatus(String status, int id, int messageCode) {
		accountDao.updateAccountStatus(status, id, messageCode);
	}

	@Override
	public void removeAccountByAccountId(int id) {
		accountDao.deleteAccountByAccountId(id);
	}

	@Override
	public void makeDeposit(double addedCash, int accountid, int messageCode) {
		logger.info("Sending deposit request to the database.");
		// find the account
		Account account = accountDao.selectAccountByAccountId(accountid);
		System.out.println(account);
		//check if addedCash is positive number 
		if(addedCash > 0) {
			// set new balance
			account.setBalance(account.getBalance() + addedCash);
			double myNewBalance = account.getBalance();
			logger.info("New balance: " + account.getBalance());
			logger.info("Updating account balance to account number " + accountid);
			// update the database
			accountDao.updateAccountBalance(myNewBalance, accountid, messageCode);
		}else {
			//else throw exception
			logger.warn("Overdraft alert: added cash must be at least greater than zero. Amount: " + addedCash);
			throw new BankException("Cannot add negative amount of cash into account.");
		}
		
	}

	@Override
	public void makeWithdraw(double subbedCash, int id, int messageCode) {
		logger.info("Sending withdrawal request to the database.");
		// find the account
		Account account = accountDao.selectAccountByAccountId(id);
		System.out.println(account);
		// set new balance
		if(subbedCash > 0) {
			account.setBalance(account.getBalance() - subbedCash);
			double myNewBalance = account.getBalance();
			// check if new balance would be in zero or negative range before sending it to
			// dao
			logger.info("New balance: " + String.format("$%.2f", account.getBalance()));
			logger.info("Updating account balance to account number " + id);
			if(myNewBalance >= 0) {
				// update the database
				accountDao.updateAccountBalance(myNewBalance, id, messageCode);
			}else {
				//else throw exception
				logger.warn("Overdraft alert: insufficient fund to process request.");
				logger.warn("Potential new balance would be: " + subbedCash);
				throw new BankException("Insufficient funds for withdraw");
			}
		}else {
			//else throw exception
			logger.warn("Overdraft alert: subtracted cash must be at least greater than zero. Amount: " + subbedCash);
			throw new BankException("Cannot subtract negative amount of cash from account.");
		}

	}

	@Override
	public void makeTransfer(double amount, int fromId, int toId) {
		accountDao.transferRequestFunc(amount, fromId, toId);
	}

	@Override
	public List<BankTransaction> showTransactionHistory(int accountId) {
		List<BankTransaction> tList = accountDao.transactionHistoryByAccountId(accountId);
		return tList;
	}

}
