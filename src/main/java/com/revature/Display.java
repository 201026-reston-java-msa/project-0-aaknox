package com.revature;

public class Display {
	public void welcomeBox() {
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("|                                                                                  |");
		System.out.println("|                                    WELCOME                                       |");
		System.out.println("|                                                                                  |");
		System.out.println("------------------------------------------------------------------------------------\n\n");
	}

	public void mainMenuBox() {
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("|                                                                                  |");
		System.out.println("|                               BANK MAIN MENU                                     |");
		System.out.println("|                                                                                  |");
		System.out.println("------------------------------------------------------------------------------------\n\n");

		System.out.println("Options: ");
		System.out.println("1 - open an account");
		System.out.println("2 - make a deposit");
		System.out.println("3 - check your balance");
		System.out.println("4 - make a withdrawal");
		System.out.println("5 - transfer funds to another account");
		System.out.println();
		System.out.println("6 - Show customer account information");
		System.out.println("7 - Show customer account balance(s)");
		System.out.println("7 - Show customer personal information");
		System.out.println("8 - Check pending account applications");
		System.out.println();
		System.out.println("9 - Approve/Deny open applications for accounts");
		System.out.println("10 - Cancel an account");
		System.out.println("11 - Log Out");
		System.out.print("-----------Your pick: ");
	}

	public void goodbyeBox() {
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("|                                                                                  |");
		System.out.println("|                        THANK YOU FOR VISITING OUR BANK                           |");
		System.out.println("|                                                                                  |");
		System.out.println("------------------------------------------------------------------------------------\n\n");
	}
	
	public void loginMenu() {
		welcomeBox();
		System.out.println("Options:");
		System.out.println("1 - Sign into Bank API");
		System.out.println("2 - Register to API");
		System.out.println("3 - Reset Password");
		System.out.println("4 - Exit API");
		System.out.print("-----------Your pick: ");
	}

}
