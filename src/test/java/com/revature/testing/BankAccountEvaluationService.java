package com.revature.testing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.revature.model.Account;
import com.revature.security.BankException;
import com.revature.service.AccountService;
import com.revature.serviceimpl.AccountServiceImpl;

/*
 * Most of the classes we're testing delegate the work to other methods in other classes (like the DAO)
 * 
 */
public class BankAccountEvaluationService {
	@Mock
	private static AccountService accountServiceMock = new AccountServiceImpl();
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	//testing accountCreate method
	@Test
	public void testCreateAccount_verified() {
		//this test is testing on dao layer
		Account account = Mockito.mock(Account.class);
		String username = "trigga";
		doNothing().when(accountServiceMock).accountCreate(account, username);
		accountServiceMock.accountCreate(account, username);
		verify(accountServiceMock, times(1)).accountCreate(account, username);
	}
	
	@Test(expected = BankException.class)
	public void testCreateAccountWithNegativeDepositAmount_throwEx() {
		Account account = Mockito.mock(Account.class);
		account.setBalance(-100.50);
		String username = "trigga";
		doThrow(BankException.class).when(accountServiceMock).accountCreate(account, username);
		accountServiceMock.accountCreate(account, username);
	}
	
	@Test(expected = BankException.class)
	public void testCreateAccountWithNonexistentUser_throwEx() {
		Account account = Mockito.mock(Account.class);
		account.setBalance(-100.50);
		String username = "george";
		doThrow(BankException.class).when(accountServiceMock).accountCreate(account, username);
		accountServiceMock.accountCreate(account, username);
	}
	
	@Test
	public void testGetAllAccounts_returnTrue() {
		List<Account> accountList = new ArrayList<Account>();
		when(accountServiceMock.getAllAccounts()).thenReturn(accountList);
		assertEquals(accountServiceMock.getAllAccounts(), true);
	}
}
