package com.revature;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.model.Account;
import com.revature.model.AccountStatus;
import com.revature.model.AccountType;
import com.revature.model.BankRole;
import com.revature.model.BankTransaction;
import com.revature.model.User;
import com.revature.security.BankException;
import com.revature.service.AccountService;
import com.revature.service.UserService;
import com.revature.serviceimpl.AccountServiceImpl;
import com.revature.serviceimpl.UserServiceImpl;

public class Business {
	private static User user;
	private static Scanner scanner = new Scanner(System.in);
	private static UserService userService = new UserServiceImpl();
	private static AccountService accountService = new AccountServiceImpl();
	private static Logger logger = Logger.getLogger(Business.class);

	/**
	 * This main method acts as a controller for all business logic methods
	 * 
	 * For each choice entered from the main menu, there is a respective method for
	 * its logic
	 * 
	 * @author Azhya
	 * 
	 **/

	public static void main(String[] args) {
		// set up session user
		System.out.println("Current session user name: " + args[0]);
		user = userService.getUserByUsername(args[0]);
		int choice = Integer.parseInt(args[2]);
		switch (choice) {
		case 1:
			createNewAccount();
			break;
		case 2:
			deposit();
			break;
		case 3:
			checkBalance();
			break;
		case 4:
			withdraw();
			break;
		case 5:
			transfer();
			break;
		case 6:
			viewAccounts();
			break;
		case 7:
			viewUsers();
			break;
		case 8:
			viewPendingApplications();
			break;
		case 9:
			viewTransactionHistory();
		case 10:
			authorizeApplications();
			break;
		case 11:
			removeAccount();
			break;
		case 12:
			signOut();
			break;
		default:
			break;
		}
	}

	

	public static void signIn() {
		logger.info("Logging in existing user...");
		System.out.println("\n\n\n");
		System.out.println("PLEASE SIGN IN \n");
		System.out.print("Please enter your username: ");
		String username = scanner.next();

		System.out.print("Please enter your password: ");
		String password = "";
		password = scanner.next();

		// user verification here
		boolean isValid = userService.checkUsernameAndPassword(username, password);
		if (isValid) {
			// retrieve all information related to our session user
			user = userService.getUserByUsername(username);
			if (user == null) {
				logger.warn("User does not exist in our system.");
				throw new NullPointerException();
			} else {
				logger.info("User found in database: " + user);
				// load args with user in string values
				String[] userInfo = new String[10];
				String userFirstName = user.getFirstName();
				String userLastName = user.getLastName();
				String userEmail = user.getEmail();
				String userRoleType = user.getRole().getRoleType();

				for (int i = 0; i < 10; i++) {
					switch (i) {
					case 0:
						userInfo[i] = username;
						break;
					case 1:
						userInfo[i] = password;
						break;
					case 3:
						userInfo[i] = userFirstName;
						break;
					case 4:
						userInfo[i] = userLastName;
						break;
					case 5:
						userInfo[i] = userEmail;
						break;
					case 6:
						userInfo[i] = userRoleType;
						break;
					}
				}
				// go to the main menu
				MenuDriver.main(userInfo);
			}
		}
	}

	public static void register() {

		// prompt user to provide personal information
		logger.info("Now starting registration process for a new user.");
		User newPerson = new User();
		System.out.print("Please enter your first name: ");
		newPerson.setFirstName(scanner.next());
		System.out.print("Please enter your last name: ");
		newPerson.setLastName(scanner.next());
		System.out.print("Please enter your new username: ");
		newPerson.setUsername(scanner.next());
		System.out.print("Please enter your new password: ");
		newPerson.setPassword(scanner.next());
		System.out.print("Please enter your email address: ");
		newPerson.setEmail(scanner.next());
		System.out.println("Select a user role: \n1 - Customer\n2 - Employee\n3 - Admin");
		System.out.print("--------Your Pick: ");
		int rolePick = scanner.nextInt();
		newPerson.setRole(new BankRole());
		switch (rolePick) {
		case 1:
			newPerson.getRole().setRoleType("CUSTOMER");
			break;
		case 2:
			// some admin approval method call here
			newPerson.getRole().setRoleType("EMPLOYEE");
			break;
		case 3:
			// some admin approval method call here
			newPerson.getRole().setRoleType("ADMIN");
			break;
		default:
			logger.warn("Invalid selection. Default role has been assigned.");
			System.out.println("Not a valid role. You have been set as CUSTOMER by default.");
			System.out.println("Please contact an adminstrator if you need to upgrade to a different role");
			newPerson.getRole().setRoleType("CUSTOMER");
			break;
		}
		// verify that given user information is correct
		System.out.println("**********************************");
		String formatter = String.format(
				"First name: %s\nLast name: %s\nUsername: %s\nPassword: %s\nEmail address: %s\nRole: %s",
				newPerson.getFirstName(), newPerson.getLastName(), newPerson.getUsername(), newPerson.getPassword(),
				newPerson.getEmail(), newPerson.getRole().getRoleType());
		System.out.println(formatter);
		System.out.println("**********************************");
		System.out.println("\nIs this information correct? Y/N");
		System.out.print("--------Your Pick: ");
		char verified = scanner.next().charAt(0);
		// add user to database if verified
		if (verified == 'Y' || verified == 'y') {
			userService.addUser(newPerson);
			// if successfully added, show success message
			logger.info("Successfully added user " + newPerson.getUsername() + " to API database.");
			User createdUser = userService.getUserByUsername(newPerson.getUsername());
			System.out.println(
					"You are now register to the bank API! Your new user ID number is " + createdUser.getUserId());
			try {
				Thread.sleep(2000);
				// return to welcome screen
				MainDriver.welcomeScreen();
			} catch (InterruptedException e) {
				logger.warn("Thread sleep failed. Stack Trace: ", e);
			}
		} else if (verified == 'N' || verified == 'n') {
			// if not verified information entered, make recursive call here
			register();
		} else {
			logger.warn("Invalid role type picked. Y or N values only.");
			// to prevent any human errors, the register form will be automatically reloaded
			register();
		}
	}

	public static void resetPassword() {
		logger.info("Beginning password reset process.");
		System.out.print("Please enter your username: ");
		String username = scanner.next();
		// get user's current password
		user = userService.getUserByUsername(username);
		String oldPassword = user.getPassword();

		// prompt for new password
		System.out.print("Please enter your new password: ");
		String newPassword = scanner.next();
		// check to make sure new password does not match the old one
		if (!newPassword.equals(oldPassword)) {
			// set the new password
			user.setPassword(newPassword);
			userService.modifyPassword(username, newPassword);
			try {
				Thread.sleep(1000);
				// show user that password request has been completed
				System.out.println("Success! " + user.getUsername() + ", your new password is: " + newPassword);
				// return to welcome screen
				MainDriver.welcomeScreen();
			} catch (InterruptedException e) {
				logger.warn("Thread sleep failed", e);
			}
		} else {
			try {
				// prompt user to choice to reenter new password or return to welcome screen
				System.out.println("Sorry, this request can not be completed.");
				System.out.println("Good news is: the new password you wanted actually matches the old one.");
				Thread.sleep(1000);
				System.out.println("Would you still like to reset your password? Y/N");
				System.out.print("--------Your Pick: ");
				char decision = scanner.next().charAt(0);
				switch (decision) {
				case 'Y':
				case 'y':
					resetPassword();
					break;
				case 'N':
				case 'n':
					MainDriver.welcomeScreen();
					break;
				default:
					logger.warn("Invalid selection. Returning to welcome screen.");
					MainDriver.welcomeScreen();
					break;
				}
			} catch (InterruptedException e) {
				logger.warn("Thread sleep failed. Stack Trace: ", e);
			}
		}
	}

	public static void signOut() {
		logger.info("Exiting current session");
		try {
			// wait a sec...
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.warn("Thread.sleep() failed here. Stack Trace: ", e);
		} finally {
			// return to welcome screen
			MainDriver.welcomeScreen();
		}

	}

	public static void createNewAccount() {
		logger.info("Beginning a new account application.");
		System.out.println("What kind of account are you applying for today?");
		System.out.println("1 - Checking\n2 - Savings");
		System.out.print("--------Your Pick: ");
		int pick = scanner.nextInt();
		String accountTypeName = "";
		if (pick == 1) {
			accountTypeName = "CHECKING";
		} else if (pick == 2) {
			accountTypeName = "SAVINGS";
		} else {
			logger.debug("Invalid selection. Account will be set to CHECKING by default.");
			System.out.println("Invalid account type selected. You have been set as CHECKING by default.");
			System.out.println("Please contact an adminstrator if you need to upgrade to a different type of account.");
			accountTypeName = "CHECKING";
		}

		System.out.print("How much would you like to deposit into this account ($50.00 min): $");

		double balance = scanner.nextDouble();
		if (balance >= 50) {
			// create account
			System.out.println(user.getUsername());
			accountService.accountCreate(new Account(0, balance, new AccountStatus(0, "PENDING"),
					new AccountType(0, accountTypeName), LocalDate.now()), user.getUsername());
		} else {
			throw new BankException("Starting deposit amount is too low");
		}

		// return to main menu
		String[] sessionUserInfo = new String[10];
		sessionUserInfo[0] = user.getUsername();
		sessionUserInfo[1] = user.getPassword();
		MenuDriver.main(sessionUserInfo);
	}

	public static void deposit() {
		logger.info("Beginning deposit request.");
		user = userService.getUserByUsername(user.getUsername());
		// prompt user for deposit amount
		System.out.print("How much would you like to deposit into your account today: ");
		double myDeposit = scanner.nextDouble();
		// prompt user for account number
		System.out.print("Please enter your account number: ");
		int userAccNo = scanner.nextInt();
		Account account = accountService.getAccountByAccountId(userAccNo);
		int realOwnerId = accountService.findOwnerIdOfAccount(account);
		
		logger.debug("Deposit request submitted: $" + myDeposit + " to account number " + userAccNo + ".\n Requestor: "
				+ user.getUsername() + ", Role: " + user.getRole().getRoleType());
		// some logic to check if this user is authorized to make changes to account
		if(user.getUserId() == realOwnerId || 
				user.getRole().getRoleType().equals("EMPLOYEE") || 
				user.getRole().getRoleType().equals("ADMIN")) {
			accountService.makeDeposit(myDeposit, userAccNo, 500);
			logger.info("Deposit request has been successfully submitted. Returning to main menu.");
			// return to main menu
			String[] sessionUserInfo = new String[10];
			sessionUserInfo[0] = user.getUsername();
			sessionUserInfo[1] = user.getPassword();
			MenuDriver.main(sessionUserInfo);
		}else {
			
			throw new BankException("Only the account holder has access to make transactions of this type.");
		}
		
	}

	public static void checkBalance() {
		logger.info("Beginning to check user balance");
		user = userService.getUserByUsername(user.getUsername());
		// check to see if session has more than one account
		List<Account> accounts = accountService.viewAccountsByUserId(user.getUserId());
		// if zero--> throw exception and return to main menu
		if (accounts == null) {
			throw new BankException("list returned empty");
		} else {
			// if one or more--> populate account table
			// print table headers
			System.out.println(
					"+----------------------------------------------------------------------------------------------------------------+");
			System.out.printf("| %-20s %-20s %-20s %-20s %-5s %-20s |", "ACCOUNT_ID", "BALANCE", "STATUS", "TYPE",
					"OWNER", "CREATION_DATE");
			System.out.println();
			System.out.println(
					"+----------------------------------------------------------------------------------------------------------------+");
			for (Account a : accounts) {
				// print table content
				System.out.printf("| %-20s $%.2f %-20s %-20s %-5s %-20s |", a.getAccountId(), a.getBalance(),
						a.getStatus().getStatus(), a.getType().getType(), user.getUserId(), a.getCreationDate());
				System.out.println();
			}
			System.out.println(
					"+----------------------------------------------------------------------------------------------------------------+");
			// run loop to check if user q button
			boolean isExited = false;
			while (isExited == false) {
				System.out.print("Press [Q] at any time to return to the main menu: ");
				char exitKey = scanner.next().charAt(0);
				if (exitKey == 'q' || exitKey == 'Q') {
					logger.info("User has pressed the quit key.");
					isExited = true;
				}
			}
			// if q-> return to main menu
			logger.info("User is now ready to return to main menu.");
			String[] sessionUserInfo = new String[10];
			sessionUserInfo[0] = user.getUsername();
			sessionUserInfo[1] = user.getPassword();
			MenuDriver.main(sessionUserInfo);
		}
	}

	public static void withdraw() {
		logger.info("Beginning withdrawal request.");
		user = userService.getUserByUsername(user.getUsername());
		// prompt user for deposit amount
		System.out.print("How much would you like to withdraw from your account today: ");
		double myWithdraw = scanner.nextDouble();
		// prompt user for account number
		System.out.print("Please enter your account number: ");
		int userAccNo = scanner.nextInt();

		logger.debug("Withdrawal request submitted: $" + myWithdraw + " to account number " + userAccNo
				+ ".\n Requestor: " + user.getUsername() + ", Role: " + user.getRole().getRoleType());
		// some logic to check if this user is authorized to make changes to account
		accountService.makeWithdraw(myWithdraw, userAccNo, 600);
		logger.info("Withdrawal request has been successfully submitted. Returning to main menu.");
		// return to main menu
		String[] sessionUserInfo = new String[10];
		sessionUserInfo[0] = user.getUsername();
		sessionUserInfo[1] = user.getPassword();
		MenuDriver.main(sessionUserInfo);
	}

	public static void transfer() {
		// get session user
		logger.info("Beginning transfer request.");
		user = userService.getUserByUsername(user.getUsername());
		List<Account> ulist = accountService.viewAccountsByUserId(user.getUserId());
		// get fromAccount from user
		System.out.print("Please enter the account ID number that will be SENDING money: ");
		int fromAccountId = scanner.nextInt();
		Account fromAccount = accountService.getAccountByAccountId(fromAccountId);

		logger.debug("Testing ownership check.");
		boolean isAuthorized = false;
		for (Account account : ulist) {
			logger.debug("Index: " + account);
			if (account.getAccountId() == fromAccount.getAccountId()) {
				logger.info("Account found in list: " + account);
				isAuthorized = true;
				break;
			}
		}

		if (isAuthorized == true) {
			logger.debug("Check passed.");
		} else {
			logger.debug("User is not an owner.");
		}

		// check if user is authorize to access this account
		if (user.getRole().getRoleType().equals("EMPLOYEE") || user.getRole().getRoleType().equals("ADMIN")
				|| isAuthorized == true) {
			logger.info(user.getUsername() + ", " + user.getRole().getRoleType()
					+ " is an authorized user for this type of request.");
			// ask for toAccount
			System.out.print("Please enter the account ID number that will be RECEIVING money: ");
			int toAccountId = scanner.nextInt();
			Account toAccount = accountService.getAccountByAccountId(toAccountId);
			// ask for transferAmt
			System.out.print("Amount of money transferred: $");
			double transferAmt = scanner.nextDouble();
			double afterFromAccountAmt = fromAccount.getBalance() - transferAmt;
			// check if both accounts are open or one is at least pending
			boolean isOP = false;
			if (fromAccount.getStatus().getStatus().equals("OPEN")
					&& toAccount.getStatus().getStatus().equals("OPEN")) {
				isOP = true;
			} else if (fromAccount.getStatus().getStatus().equals("PENDING")
					|| toAccount.getStatus().getStatus().equals("PENDING")) {
				isOP = true;
			} else {
				isOP = false;
			}

			while (isOP == true) {
				// check if fromAccount has enough money
				if (afterFromAccountAmt >= 0) {
					logger.info("Processing transfer request now.");
					accountService.makeTransfer(transferAmt, fromAccountId, toAccountId);
					logger.info("Transfer request to database completed.");
					isOP = false;
				} else {
					logger.warn("Insufficient funds from sender. Balance: $" + fromAccount.getBalance()
							+ ", Transfer Amount: $" + transferAmt, new BankException());
				}
			}

		} else {
			// everything else - unauthorized user
			logger.warn(user.getUsername() + ", " + user.getRole().getRoleType()
					+ " is an unauthorized user for this type of request.");
		}

		// return to main menu
		String[] sessionUserInfo = new String[10];
		sessionUserInfo[0] = user.getUsername();
		sessionUserInfo[1] = user.getPassword();
		MenuDriver.main(sessionUserInfo);
	}

	public static void viewAccounts() {
		// get session user
		logger.info("Loading account master list.");
		user = userService.getUserByUsername(user.getUsername());
		// retrieve all accounts
		List<Account> masterList = accountService.getAllAccounts();

		// do route guarding here (must be EMPLOYEE or ADMIN access only)
		if (masterList == null) {
			throw new BankException("list returned empty");
		} else if (user.getRole().getRoleType().equals("CUSTOMER")) {
			logger.warn("Unauthorized user. User: " + user.getUsername() + ", " + user.getRole().getRoleType() + ".",
					new BankException());
		} else {
			// populate table after route guarding
			// print table headers
			System.out.println(
					"+----------------------------------------------------------------------------------------------------------------+");
			System.out.printf("| %-20s %-20s %-20s %-20s %-5s %-20s |", "ACCOUNT_ID", "BALANCE", "STATUS", "TYPE",
					"OWNER", "CREATION_DATE");
			System.out.println();
			System.out.println(
					"+----------------------------------------------------------------------------------------------------------------+");
			for (Account a : masterList) {
				// find owner of account
				int ownerId = accountService.findOwnerIdOfAccount(a);
				// print table content
				System.out.printf("| %-20s %-20s %-20s %-20s %-5s %-20s |", a.getAccountId(), a.getBalance(),
						a.getStatus().getStatus(), a.getType().getType(), ownerId, a.getCreationDate());
				System.out.println();
			}
			System.out.println(
					"+----------------------------------------------------------------------------------------------------------------+");
			// run loop to check if user q button
			boolean isExited = false;
			while (isExited == false) {
				System.out.print("Press [Q] at any time to return to the main menu: ");
				char exitKey = scanner.next().charAt(0);
				if (exitKey == 'q' || exitKey == 'Q') {
					logger.info("User has pressed the quit key.");
					isExited = true;
				}
			}
			// if q-> return to main menu
			logger.info("User is now ready to return to main menu.");
			String[] sessionUserInfo = new String[10];
			sessionUserInfo[0] = user.getUsername();
			sessionUserInfo[1] = user.getPassword();
			MenuDriver.main(sessionUserInfo);
		}
	}

	public static void viewUsers() {
		// get session user
		logger.info("Loading user master list.");
		user = userService.getUserByUsername(user.getUsername());
		// retrieve all users
		List<User> masterList = userService.getAllUsers();

		// do route guarding here (must be EMPLOYEE or ADMIN access only)
		if (masterList == null) {
			throw new BankException("list returned empty");
		} else if (user.getRole().getRoleType().equals("CUSTOMER")) {
			logger.warn("Unauthorized user. User: " + user.getUsername() + ", " + user.getRole().getRoleType() + ".",
					new BankException());
		} else {
			// populate table after route guarding
			// print table headers
			System.out.println(
					"+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+");
			System.out.printf("| %-20s %-20s %-20s %-20s %-20s %-40s %-40s %-5s ", "USER_ID", "USERNAME", "PASSWORD",  "FIRST_NAME", "LAST_NAME",
					"EMAIL", "ROLE", "NUM_OF_ACCOUNTS");
			System.out.println();
			System.out.println(
					"+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+");
			for (User u : masterList) {
				// find all accounts for each user
				List<Account> accounts = accountService.viewAccountsByUserId(u.getUserId());
				int numOfAccounts = 0;
				
				if(accounts == null) {
					numOfAccounts = 0;
				}else {
					numOfAccounts = accounts.size();
				}
				// print table content
				System.out.printf("| %-20s %-20s %-20s %-20s %-20s %-40s %-40s %-5s ", u.getUserId(), u.getUsername(),
						u.getPassword(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getRole().getRoleType(), numOfAccounts);
				System.out.println();
			}
			System.out.println(
					"+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+");
			// run loop to check if user q button
			boolean isExited = false;
			while (isExited == false) {
				System.out.print("Press [Q] at any time to return to the main menu: ");
				char exitKey = scanner.next().charAt(0);
				if (exitKey == 'q' || exitKey == 'Q') {
					logger.info("User has pressed the quit key.");
					isExited = true;
				}
			}
			// if q-> return to main menu
			logger.info("User is now ready to return to main menu.");
			String[] sessionUserInfo = new String[10];
			sessionUserInfo[0] = user.getUsername();
			sessionUserInfo[1] = user.getPassword();
			MenuDriver.main(sessionUserInfo);
		}
	}

	public static void viewPendingApplications() {
		logger.info("Gathering pending applications...");
		//get session user
		user = userService.getUserByUsername(user.getUsername());
		// check to see if session has more than one account
		List<Account> accounts = accountService.viewAllAccountsByStatus("PENDING");
		// if zero--> throw exception and return to main menu
		if (accounts == null) {
			throw new BankException("list returned empty");
		} else if (user.getRole().getRoleType().equals("CUSTOMER")) {
			logger.warn("Unauthorized user. User: " + user.getUsername() + ", " + user.getRole().getRoleType() + ".",
					new BankException());
		}else {
			// if one or more--> populate account table
			// print table headers
			System.out.println(
					"+----------------------------------------------------------------------------------------------------------------+");
			System.out.printf("| %-20s %-20s %-20s %-20s %-5s %-20s |", "ACCOUNT_ID", "BALANCE", "STATUS", "TYPE",
					"OWNER", "CREATION_DATE");
			System.out.println();
			System.out.println(
					"+----------------------------------------------------------------------------------------------------------------+");
			for (Account a : accounts) {
				// print table content
				int ownerId = accountService.findOwnerIdOfAccount(a);
				System.out.printf("| %-20s %-20s %-20s %-20s %-5s %-20s |", a.getAccountId(), a.getBalance(),
						a.getStatus().getStatus(), a.getType().getType(), ownerId, a.getCreationDate());
				System.out.println();
			}
			System.out.println(
					"+----------------------------------------------------------------------------------------------------------------+");
			// run loop to check if user q button
			boolean isExited = false;
			while (isExited == false) {
				System.out.print("Press [Q] at any time to return to the main menu: ");
				char exitKey = scanner.next().charAt(0);
				if (exitKey == 'q' || exitKey == 'Q') {
					logger.info("User has pressed the quit key.");
					isExited = true;
				}
			}
			// if q-> return to main menu
			logger.info("User is now ready to return to main menu.");
			String[] sessionUserInfo = new String[10];
			sessionUserInfo[0] = user.getUsername();
			sessionUserInfo[1] = user.getPassword();
			MenuDriver.main(sessionUserInfo);
		}
	}
	
	private static void viewTransactionHistory() {
		logger.info("Beginning transaction history request.");
		user = userService.getUserByUsername(user.getUsername());
		// prompt user for account number
		System.out.print("Please enter the account number: ");
		int userAccNo = scanner.nextInt();
		Account account = accountService.getAccountByAccountId(userAccNo);
		int realOwnerId = accountService.findOwnerIdOfAccount(account);
		
		logger.debug("Transaction history request submitted for account number " + userAccNo + ".\n Requestor: "
				+ user.getUsername() + ", Role: " + user.getRole().getRoleType());
		// some logic to check if this user is authorized to make changes to account
		if(user.getUserId() == realOwnerId || 
				user.getRole().getRoleType().equals("EMPLOYEE") || 
				user.getRole().getRoleType().equals("ADMIN")) {
			List<BankTransaction> tList = accountService.showTransactionHistory(userAccNo);
			// print table headers
						System.out.println(
								"+-------------------------------------------------------------------------------------------------------------------------------------------------------------+");
						System.out.printf("| %-15s %-15s %-30s %-30s %-20s %-40s |", "TRANS_ID", "TRANS_ACC_ID", "TRANS_TIMESTAMP",  "POST_TRANS_BALANCE", "DESCRIPTION_CODE",
								"DESCRIPTION_MESSAGE");
						System.out.println();
						System.out.println(
								"+-------------------------------------------------------------------------------------------------------------------------------------------------------------+");
						for (BankTransaction t : tList) {
							//format time stamp to formatter
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
							// print table content
							System.out.printf("| %-15s %-15s %-30s %-30s %-20s %-40s ", t.getTransactionId(), t.getTransactionAccountId(),
									t.getTransactionTimeStamp().format(formatter), t.getTransactionBalance(), t.getDescription().getDescriptionCode(), t.getDescription().getDescriptionMessage());
							System.out.println();
						}
						System.out.println(
								"+-------------------------------------------------------------------------------------------------------------------------------------------------------------+");
						// run loop to check if user q button
						boolean isExited = false;
						while (isExited == false) {
							System.out.print("Press [Q] at any time to return to the main menu: ");
							char exitKey = scanner.next().charAt(0);
							if (exitKey == 'q' || exitKey == 'Q') {
								logger.info("User has pressed the quit key.");
								isExited = true;
							}
						}
						// if q-> return to main menu
						logger.info("User is now ready to return to main menu.");
						String[] sessionUserInfo = new String[10];
						sessionUserInfo[0] = user.getUsername();
						sessionUserInfo[1] = user.getPassword();
						MenuDriver.main(sessionUserInfo);
		}else {
			throw new BankException("Only the account holder or an EMPLOYEE/ADMIN user has access to view transactions of this account.");
		}
	}

	public static void authorizeApplications() {
		logger.info("Opening authorization request.");
		//get session user
		user = userService.getUserByUsername(user.getUsername());
		//check if user is admin level
		if(!user.getRole().getRoleType().equals("ADMIN")) {
			//check failed - throw bank exception
			logger.warn("Unauthorized user. User: " + user.getUsername() + ", " + user.getRole().getRoleType() + ".");
			throw new BankException("Unauthorized user.");
		}else {
			//check passed - populate the pending account into a list
			List<Account> accounts = accountService.viewAllAccountsByStatus("PENDING");
			//show table
			// print table headers
			System.out.println(
					"+----------------------------------------------------------------------------------------------------------------+");
			System.out.printf("| %-20s %-20s %-20s %-20s %-5s %-20s |", "ACCOUNT_ID", "BALANCE", "STATUS", "TYPE",
					"OWNER", "CREATION_DATE");
			System.out.println();
			System.out.println(
					"+----------------------------------------------------------------------------------------------------------------+");
			for (Account a : accounts) {
				// find owner of account
				int ownerId = accountService.findOwnerIdOfAccount(a);
				// print table content
				System.out.printf("| %-20s %-20s %-20s %-20s %-5s %-20s |", a.getAccountId(), a.getBalance(),
						a.getStatus().getStatus(), a.getType().getType(), ownerId, a.getCreationDate());
				System.out.println();
			}
			System.out.println(
					"+----------------------------------------------------------------------------------------------------------------+");
			//ask admin for account number they want to change
			System.out.println();
			System.out.print("Enter the account ID number that needs a change of status: ");
			int desiredAccountNum = scanner.nextInt();
			//ask admin if account is approved or denied
			System.out.println("\nAuthorization Options:");
			System.out.println("1 - Approve application (update to OPEN status)");
			System.out.println("2 - Deny application (update to CLOSED status)");
			System.out.print("-----------Your pick: ");
			int authorizePick = scanner.nextInt();
			switch (authorizePick) {
			case 1:
				//if approved, update account to OPEN status
				accountService.modifyAccountStatus("OPEN", desiredAccountNum, 300);
				break;
			case 2:
				//if denied, update account to CLOSED status
				accountService.modifyAccountStatus("CLOSED", desiredAccountNum, 400);
				break;
			default:
				logger.warn("invalid authorization code entered.");
				break;
			}
			//show success message
			logger.info("Application status change request is complete.");
			//return to main menu
			String[] sessionUserInfo = new String[10];
			sessionUserInfo[0] = user.getUsername();
			sessionUserInfo[1] = user.getPassword();
			MenuDriver.main(sessionUserInfo);
		}
	}

	public static void removeAccount() {
		logger.info("Beginning cancellation request.");
		//get session user
		user = userService.getUserByUsername(user.getUsername());
		//check if user is admin
		if(!user.getRole().getRoleType().equals("ADMIN")) {
			//check failed - throw bank exception
			logger.warn("Unauthorized user- Username: " + user.getUsername() + ", " + user.getRole().getRoleType());
			throw new BankException("Unauthorized user for this request.");
		}else {
			//check passed - populate master accounts table
			List<Account> masterList = accountService.getAllAccounts();
			// print table headers
			System.out.println(
					"+----------------------------------------------------------------------------------------------------------------+");
			System.out.printf("| %-20s %-20s %-20s %-20s %-5s %-20s |", "ACCOUNT_ID", "BALANCE", "STATUS", "TYPE",
					"OWNER", "CREATION_DATE");
			System.out.println();
			System.out.println(
					"+----------------------------------------------------------------------------------------------------------------+");
			for (Account a : masterList) {
				// find owner of account
				int ownerId = accountService.findOwnerIdOfAccount(a);
				// print table content
				System.out.printf("| %-20s %-20s %-20s %-20s %-5s %-20s |", a.getAccountId(), a.getBalance(),
						a.getStatus().getStatus(), a.getType().getType(), ownerId, a.getCreationDate());
				System.out.println();
			}
			System.out.println(
					"+----------------------------------------------------------------------------------------------------------------+");
			//ask admin what account they wish to remove
			System.out.println();
			System.out.print("Enter the account ID number that you to remove from the API: ");
			int desiredAccountNum = scanner.nextInt();
			//get desired account info
			Account desiredAcc = accountService.getAccountByAccountId(desiredAccountNum);
			//check if account has CLOSED status
			if(desiredAcc.getStatus().getStatus().equals("CLOSED")) {
				//check 2 passed - remove account
				accountService.removeAccountByAccountId(desiredAccountNum);
			}else {
				//check 2 failed - tell admin that account status must be changed first
				System.out.println("This account is currently listed as " + desiredAcc.getStatus().getStatus() + ".");
				//ask admin if they want the status changed
				System.out.print("Do you wish to change this account's status to CLOSED? [Y/N]: ");
				char answer = scanner.next().charAt(0);
				switch (answer) {
				case 'Y':
				case 'y':
					//if YES - change status to CLOSED
					accountService.modifyAccountStatus("CLOSED", desiredAccountNum, 400);
					//system now removes the account from API
					accountService.removeAccountByAccountId(desiredAccountNum);
					break;
				case 'N':
				case 'n':
				default:
					//if NO or anything else - log warning here
					logger.warn("Admin declined to changed the account status of account ID number " + desiredAccountNum + " at this time.");
					break;
				}
			}
			//show success message
			logger.info("Account removal request finished.");
			//return to main menu
			String[] sessionUserInfo = new String[10];
			sessionUserInfo[0] = user.getUsername();
			sessionUserInfo[1] = user.getPassword();
			MenuDriver.main(sessionUserInfo);
		}
	}
}
