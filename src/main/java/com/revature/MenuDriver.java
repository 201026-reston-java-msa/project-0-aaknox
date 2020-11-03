package com.revature;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.model.User;


public class MenuDriver {
	private static User user = new User();
	private static Display display = new Display();
	private static Scanner scanner = new Scanner(System.in);
	private static Logger logger = Logger.getLogger(MenuDriver.class);
	
	public static void main(String[] args) {
		//set up session user
		user.setUsername(args[0]);
		user.setPassword(args[1]);
		//display the menu
		display.mainMenuBox();
		int choice = scanner.nextInt();
		logger.info("You picked option: " + choice);
		//go to appropriate choice method within business class (aka controller class)
		args[0] = user.getUsername();
		args[1] = user.getPassword();
		args[2] = String.valueOf(choice);
		Business.main(args);
	}

}
