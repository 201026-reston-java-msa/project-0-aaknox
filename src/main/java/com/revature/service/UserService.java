package com.revature.service;

import java.util.List;

import com.revature.model.User;

public interface UserService {

	// database methods
	public void addUser(User myNewUser);

	public List<User> getAllUsers();

	public User getUserByUsername(String uname);

	public void modifyUser(User user);
	
	public void modifyPassword(String uname, String pw);

	public void removeUser(User user);

	// business methods
	public void accountCreate(String user, double startBal);

	public boolean checkUsernameAndPassword(String u, String p);
}
