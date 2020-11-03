package com.revature;

import java.util.Scanner;

import com.revature.model.User;


public class MenuDriver {
	private static User user = new User();
	private static Display display = new Display();
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		//set up session user
		user.setUsername(args[1]);
		user.setPassword(args[2]);
		//display the menu
		display.mainMenuBox();
		int choice = scanner.nextInt();
		System.out.println("You picked option: " + choice);
		//go to appropriate choice method within business class
		args[0] = user.getUsername();
		args[1] = user.getPassword();
		args[2] = String.valueOf(choice);
		Business.main(args);
	}

}
