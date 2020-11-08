package com.revature.testing;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.revature.daoimpl.AccountDaoImpl;
import com.revature.model.Account;
import com.revature.security.BankException;
import com.revature.serviceimpl.AccountServiceImpl;

/*
 * Most of the classes we're testing delegate the work to other methods in other classes (like the DAO)
 * 
 */
public class BankAccountEvaluationService {
	
	@InjectMocks
	private AccountServiceImpl service;

	@Mock
	private AccountDaoImpl daoMock;

	@Before
	public void setUp() {
		service = new AccountServiceImpl();
		business = new Business();
		MockitoAnnotations.initMocks(this);
	}

	/*
	 * TEST CASES FOR SERVICE LAYER
	 */

	// accountCreate()
	@Test
	public void testCreateAccount_verified() {
		// test object declarations
		Account account = new Account();
		String username = "trigga";
		// run test method with service injector
		service.accountCreate(account, username);
		// verify that the dao mock ran with injector
		verify(daoMock, times(1)).insertAccount(account, username);
	}

	// makeDeposit() - test passed (DO NOT CHANGE CODE)
	@Test
	public void testMakeDeposit_ValidUserId() {
		// test objects
		Account account = new Account();
		account.setAccountId(1);
		double amount = 20.50;

		// setup (on dao layer mock)
		when(daoMock.selectAccountByAccountId(1)).thenReturn(account);

		// run test method (on service injector)
		service.makeDeposit(account.getBalance() + amount, account.getAccountId());

		// verify that the mock worked
		verify(daoMock, times(1)).updateAccountBalance(account.getBalance(), account.getAccountId());
	}

	@Test(expected = BankException.class)
	public void testMakeDeposit_NegativeDeposit() {
		// test objects
		Account account = new Account();
		account.setAccountId(1);
		double amount = -100.50;
		// set up (on dao layer mock)
		when(daoMock.selectAccountByAccountId(1)).thenReturn(account);

		// call test method (on service injector)
		// this method call should fail due to a bank exception being thrown
		service.makeDeposit(account.getBalance() + amount, account.getAccountId());
	}

	// makeWithDrawal()

	@Test
	public void testMakeWithdrawal_ValidUserId() {
		// test objects
		Account account = new Account();
		account.setAccountId(1);
		account.setBalance(100);
		double amount = 20.50;

		// setup (on dao layer mock)
		when(daoMock.selectAccountByAccountId(1)).thenReturn(account);

		// run test method (on service injector)
		service.makeWithdraw(account.getBalance() - amount, account.getAccountId());

		// verify that the mock worked
		verify(daoMock, times(1)).updateAccountBalance(account.getBalance(), account.getAccountId());
	}

	@Test(expected = NullPointerException.class)
	public void testMakeWithdrawal_InvalidUserId() {
		// test objects
		Account account = new Account();
		account.setBalance(100);
		double amount = 20.50;

		// setup (on dao layer mock)
		doThrow(new NullPointerException()).when(daoMock).selectAccountByAccountId(0);

		// run test method (on service injector)
		service.makeWithdraw(account.getBalance() - amount, account.getAccountId());
	}

	@Test(expected = BankException.class)
	public void testMakeWithdrawal_NegativeSubbedAmount() {
		// test objects
		Account account = new Account();
		account.setAccountId(1);
		double amount = -20.50;

		// setup (on dao layer mock)
		when(daoMock.selectAccountByAccountId(account.getAccountId())).thenReturn(account);

		// run test method (on service injector)
		// should be returns a BankException: Cannot subtract negative amount of cash
		// from account.
		service.makeWithdraw(amount, account.getAccountId());
	}

	@Test(expected = BankException.class)
	public void testMakeWithdrawal_InsufficientFunds() {
		// test objects
		Account account = new Account();
		account.setAccountId(1);
		account.setBalance(-100);
		double amount = 20.50;

		// setup (on dao layer mock)
		when(daoMock.selectAccountByAccountId(account.getAccountId())).thenReturn(account);

		// run test method (on service injector)
		// should be returns a BankException: insufficient funds for withdraw
		service.makeWithdraw(amount, account.getAccountId());
	}
	
	@Test
	public void testGetAllAccounts_verified(){
		List<Account> accounts = new ArrayList<Account>();
		when(daoMock.selectAllAccounts()).thenReturn(accounts);
		service.getAllAccounts();
		verify(daoMock, times(1)).selectAllAccounts();
	}

}
