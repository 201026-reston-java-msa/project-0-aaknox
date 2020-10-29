package com.revature.service;

import java.util.List;

import com.revature.model.User;
import com.revature.security.BankException;

public interface UserService {
	public List<User> getAllUsers();
	public User getUser(User user); //retrieve a specific User
	public User getUserByUsername(String uname);
	public User getUserById(int id);
	public void accountCreate(String user, double startBal) throws BankException;
	public void addUser(User myNewUser);
	public void removeUser(User user);
	public void modifyUser(User enteredUser, int originalId);
	public boolean checkUsernameAndPassword(String u, String p);
}
