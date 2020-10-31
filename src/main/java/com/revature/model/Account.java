package com.revature.model;

import java.time.LocalDate;

public class Account {
	private int accountId; // primary key
	private double balance; // not null
	private AccountStatus status;
	private AccountType type;
	private LocalDate creationDate;

}
