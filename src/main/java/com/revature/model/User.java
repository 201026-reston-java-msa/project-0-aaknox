package com.revature.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
	private int userId;
	private String userName;
	private String userPassword;
	private String userFirstName;
	private String userLastName;
	private String userAddress;
	private String userEmail;
	private BankRole role;
	private LocalDate userCreationDate;
	private List<Account> userAccounts = new ArrayList<Account>();

	public User() {
	}

	public User(int userId, String userName, String userPassword, String userFirstName, String userLastName,
			String userAddress, String userEmail, BankRole role, LocalDate userCreationDate,
			List<Account> userAccounts) {
		this.userId = userId;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.userAddress = userAddress;
		this.userEmail = userEmail;
		this.role = role;
		this.userCreationDate = userCreationDate;
		this.userAccounts = userAccounts;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public BankRole getRole() {
		return role;
	}

	public void setRole(BankRole role) {
		this.role = role;
	}

	public LocalDate getUserCreationDate() {
		return userCreationDate;
	}

	public void setUserCreationDate(LocalDate userCreationDate) {
		this.userCreationDate = userCreationDate;
	}

	public List<Account> getUserAccounts() {
		return userAccounts;
	}

	public void setUserAccounts(List<Account> userAccounts) {
		this.userAccounts = userAccounts;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", userPassword=" + userPassword
				+ ", userFirstName=" + userFirstName + ", userLastName=" + userLastName + ", userAddress=" + userAddress
				+ ", userEmail=" + userEmail + ", role=" + role + ", userCreationDate=" + userCreationDate
				+ ", userAccounts=" + userAccounts + "]";
	}

}
