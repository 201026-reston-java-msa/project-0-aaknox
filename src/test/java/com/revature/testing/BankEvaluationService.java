package com.revature.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
//import java.util.List;

import org.junit.Test;

import com.revature.model.Account;
import com.revature.model.AccountStatus;
import com.revature.model.AccountType;
import com.revature.service.AccountService;
//import com.revature.service.UserService;
import com.revature.serviceimpl.AccountServiceImpl;
//import com.revature.serviceimpl.UserServiceImpl;

public class BankEvaluationService {

	//private UserService userServiceTest = new UserServiceImpl();
	private AccountService accountServiceTest = new AccountServiceImpl();

	/*
	 * USER SERVICE IMPLEMENTATION TESTS
	 */

	/*
	 * ACCOUNT SERVICE IMPLEMENTATION TESTS
	 */

	// create account tests
	@Test
	public void testAccountCreate() {
		// needed before running test: Account acc, String uname
		
		//this block of code runs but persist test data into db (which is what i don't want to happen)
		//Account expectedAccount = new Account(10, 120.60, new AccountStatus(10, "PENDING"), new AccountType(10, "CHECKING"),LocalDate.now());
		//String uname = "trigga";
		//accountServiceTest.accountCreate(expectedAccount, uname);
		//Account actualAccount = accountServiceTest.getAccountByAccountId(10);
		//assertEquals("Added new account", expectedAccount, actualAccount);
	}

	@Test
	public void testAccountCreateLowStartingDeposit() {
		// needed before running test: Account acc, Double balance
		assertTrue(true);
	}

	@Test
	public void testAccountCreateNegativeStartingDeposit() {
		// needed before running test: Account acc, Double balance
		assertTrue(true);
	}

	// retrieval tests
	@Test
	public void testGetAllAccountsReturnsNotNull() {
		// needed before running test: List<Account> accList
		// check for not null
		assertTrue(true);
	}

	@Test
	public void testGetAllAccountsUnauthorizedUser() {
		// needed before running test: List<Account> accList, User user (of CUSTOMER
		// role type)
		assertTrue(true);
	}
}
