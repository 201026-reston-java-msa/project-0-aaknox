package com.revature.security;

public class BankException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public BankException() {
		super();
	}
	
	public BankException(String message) {
		super(message);
	}
	
	public BankException(Throwable cause) {
		super(cause);
	}
	public BankException(String message, Throwable cause) {
		super(message, cause);
	}
}
