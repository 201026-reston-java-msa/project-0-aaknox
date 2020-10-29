package com.revature;

import java.util.Scanner;

import com.revature.model.User;

public class Business {
	private static User user = new User();
	private static Display display = new Display();
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		// set up session user
		user.setUserName(args[0]);
		user.setUserPassword(args[1]);
		System.out.println("Business class:");
		System.out.println(user);
		System.out.println("Choice: " + Integer.parseInt(args[2]));
		//put choice switch-case here
	}
}
