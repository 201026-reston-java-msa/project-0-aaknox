package com.revature;

import java.util.Scanner;

//import com.revature.model.User;

public class Business {
	//private static User user = new User();
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		// set up session user
		//user.setUserName(args[0]);
		//user.setUserPassword(args[1]);
//		System.out.println("Business class:");
//		System.out.println(user);
//		System.out.println("Choice: " + Integer.parseInt(args[2]));
		//put choice switch-case here
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
		System.out.println("You entered " + username + " and " + password);
		
	}

	public static void register() {
		// TODO Auto-generated method stub
		
	}

	public static void resetPassword() {
		// TODO Auto-generated method stub
		
	}
}
