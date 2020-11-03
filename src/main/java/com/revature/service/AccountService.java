package com.revature.service;

import com.revature.model.Account;

public interface AccountService {
	public void accountCreate(Account acc, String uname);
	
	public void makeDeposit(double balance, int id);
	public void makeWithdraw(double subbedCash, int id);
}
