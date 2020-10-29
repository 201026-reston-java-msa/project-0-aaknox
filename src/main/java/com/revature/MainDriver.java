package com.revature;

import java.util.Scanner;

import com.revature.security.BankException;
import com.revature.serviceimpl.UserServiceImpl;

public class MainDriver {
	
	private static Scanner scanner = new Scanner(System.in);
	
	private static Display display = new Display();
	
	private static UserServiceImpl userServiceImpl = new UserServiceImpl();
	
	public static void main(String[] args) {
		display.welcomeBox();
		System.out.print("Please enter your username: ");
		String username = scanner.next();
		
		System.out.print("Please enter your password: ");
		String password = scanner.next();
		
		//user verification here
		try {
			//go to bank api menu if verified
			boolean isVerified = userServiceImpl.checkUsernameAndPassword(username, password);
			if(isVerified) {
				args = new String[100];
				args[0] = username;
				args[1] = password;
				MenuDriver.main(args);
			}else {
				throw new BankException();
			}
		} catch (BankException e) {
			//not verified -> throw bank exception and ask user to try again
			throw new BankException("Invalid credentials entered");
		}
	}

}
