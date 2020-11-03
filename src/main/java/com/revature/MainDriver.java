package com.revature;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.log4j.Logger;



public class MainDriver {
	
	private static Scanner scanner = new Scanner(System.in);
	private static Display display = new Display();
	private static Logger logger = Logger.getLogger(MainDriver.class);
	
	public static void main(String[] args) {
		welcomeScreen();
	}
	
	public static void welcomeScreen() {
		logger.info("This is an informational message from MainDriver.");
		display.loginMenu();
		try {
			int pick = scanner.nextInt();
			logger.info("Welcome menu pick entered is a valid number.");
			Thread.sleep(2000);
			switch (pick) {
				case 1:
					Business.signIn();
					break;
				case 2:
					Business.register();
					break;
				case 3:
					Business.resetPassword();
					break;
				case 4:
					display.goodbyeBox();
					scanner.close();
					System.exit(0);
					break;
				default:
					logger.warn(pick + " is an unavailable menu option. Please restart the API.");
					break;
			}
		} catch (InterruptedException e) {
			logger.error("Thread interrupted in MainDriver:Main method. Stack Trace: ", e); 
		}catch (InputMismatchException e) {
			logger.error("InputMismatch - Numbers accepted here only. From - MainDriver:Main method. Stack Trace: ", e); 
		}
	}

}
