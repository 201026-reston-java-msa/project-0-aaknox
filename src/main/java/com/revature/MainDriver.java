package com.revature;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MainDriver {
	
	private static Scanner scanner = new Scanner(System.in);

	private static Display display = new Display();
	private static final Logger logger = LogManager.getLogger(MainDriver.class);
	
	public static void main(String[] args) {
		logger.info("This is an informational message from MainDriver.");
		display.loginMenu();
		int pick = scanner.nextInt();
		try {
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
					break;
			}
		} catch (InterruptedException e) {
			logger.error("Thread interrupted in MainDriver:Main method. Stack Trace: ", e); //this line is not writing to log at current level ERROR
		}catch (InputMismatchException e) {
			logger.error("Input mismatch exception was thrown in MainDriver:Main method. Stack Trace: ", e); //this line is not writing to log at current level ERROR
		}
	}

}
