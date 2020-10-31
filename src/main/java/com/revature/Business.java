package com.revature;

import java.util.Scanner;

import com.revature.service.UserService;
import com.revature.serviceimpl.UserServiceImpl;

import com.revature.model.User;

public class Business {
	private static User user = new User();
	private static Scanner scanner = new Scanner(System.in);
	private static UserService userService = new UserServiceImpl();
	
	public static void main(String[] args) {
		System.out.println("Welcome to the business logic class");
	}

	public static void signIn() {
		
		System.out.println("\n\n\n");
		System.out.println("PLEASE SIGN IN \n");
		System.out.print("Please enter your username: ");
		String username = scanner.next();
		
		System.out.print("Please enter your password: ");
		String password = "";
		password = scanner.next();
		// user verification here
		boolean isValid = userService.checkUsernameAndPassword(username, password);
		if(isValid) {
			//retrieve all information related to our session user
			user = userService.getUserByUsername(username);
			//load args with user in string values
			String[] userInfo = new String[10];
			String userId = String.valueOf(user.getUserId());
			String userFirstName = user.getFirstName();
			String userLastName = user.getLastName();
			String userEmail = user.getEmail();
			String userRoleType = user.getRole().getRoleType();
			
			for (int i = 0; i < 10; i++) {
				switch(i) {
				case 0:
					userInfo[i] = userId;
					break;
				case 1:
					userInfo[i] = username;
					break;
				case 2:
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
			//go to the main menu
			MenuDriver.main(userInfo);
		}
	}

	public static void register() {
		// TODO Auto-generated method stub
		
	}

	public static void resetPassword() {
		// TODO Auto-generated method stub
		
	}
}
