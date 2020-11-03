package com.revature;

import java.time.LocalDate;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.model.Account;
import com.revature.model.AccountStatus;
import com.revature.model.AccountType;
import com.revature.model.BankRole;
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
			viewCustomers();
			break;
		case 8:
			viewPendingApplications();
			break;
		case 9:
			authorizeApplications();
			break;
		case 10:
			cancelAccount();
			break;
		case 11:
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
			//return to welcome screen
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
		//prompt user for deposit amount
		System.out.print("How much would you like to deposit into your account today: ");
		double myDeposit = scanner.nextDouble();
		//prompt user for account number
		System.out.print("Please enter your account number: ");
		int userAccNo = scanner.nextInt();
		
		logger.debug("Deposit request submitted: $" + myDeposit 
				+ " to account number " + userAccNo 
				+ ".\n Requestor: " + user.getUsername() 
				+ ", Role: " + user.getRole().getRoleType());
		//some logic to check if this user is authorized to make changes to account
		accountService.makeDeposit(myDeposit, userAccNo);
		logger.info("Deposit request has been successfully submitted. Returning to main menu.");
		// return to main menu
		String[] sessionUserInfo = new String[10];
		sessionUserInfo[0] = user.getUsername();
		sessionUserInfo[1] = user.getPassword();
		MenuDriver.main(sessionUserInfo);
	}

	public static void checkBalance() {

	}

	public static void withdraw() {
		logger.info("Beginning withdrawal request.");
		user = userService.getUserByUsername(user.getUsername());
		//prompt user for deposit amount
		System.out.print("How much would you like to withdraw from your account today: ");
		double myWithdraw = scanner.nextDouble();
		//prompt user for account number
		System.out.print("Please enter your account number: ");
		int userAccNo = scanner.nextInt();
		
		logger.debug("Withdrawal request submitted: $" + myWithdraw 
				+ " to account number " + userAccNo 
				+ ".\n Requestor: " + user.getUsername() 
				+ ", Role: " + user.getRole().getRoleType());
		//some logic to check if this user is authorized to make changes to account
		accountService.makeWithdraw(myWithdraw, userAccNo);
		logger.info("Withdrawal request has been successfully submitted. Returning to main menu.");
		// return to main menu
		String[] sessionUserInfo = new String[10];
		sessionUserInfo[0] = user.getUsername();
		sessionUserInfo[1] = user.getPassword();
		MenuDriver.main(sessionUserInfo);
	}

	public static void transfer() {

	}

	public static void viewAccounts() {

	}

	public static void viewCustomers() {

	}

	public static void viewPendingApplications() {

	}

	public static void authorizeApplications() {

	}

	public static void cancelAccount() {

	}
}
