package com.revature.serviceimpl;

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

}
