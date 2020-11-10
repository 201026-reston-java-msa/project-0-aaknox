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
		System.out.println("1 - Open an account");
		System.out.println("2 - Make a deposit");
		System.out.println("3 - Check your balance");
		System.out.println("4 - Make a withdrawal");
		System.out.println("5 - Transfer funds to another account");
		System.out.println();
		System.out.println("6 - View all accounts");
		System.out.println("7 - View all users");
		System.out.println("8 - View pending account applications");
		System.out.println("9 - View transaction history");
		System.out.println();
		System.out.println("10 - Approve/Deny applications for accounts");
		System.out.println("11 - Cancel an account");
		System.out.println("12 - Log Out");
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
