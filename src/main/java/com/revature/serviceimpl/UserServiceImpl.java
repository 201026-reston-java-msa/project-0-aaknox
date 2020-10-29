package com.revature.serviceimpl;

import java.util.List;

import com.revature.dao.UserDao;
import com.revature.daoimpl.UserDaoImpl;
import com.revature.model.User;
import com.revature.security.BankException;
import com.revature.service.UserService;

public class UserServiceImpl implements UserService {

	private UserDao userDao = new UserDaoImpl();

	public List<User> getAllUsers() {
		return null;
	}

	public User getUser(User user) {
		return userDao.selectUserById(user.getUserId());
	}

	public User getUserByUsername(String uname) {
		return userDao.selectUserByUsername(uname);
	}

	public void accountCreate(String user, double startBal) throws BankException {
		

	}

	public boolean checkUsernameAndPassword(String u, String p) {
		User user = userDao.selectUserByUsername(u);
		System.out.println(user);

		// user not found in DB
		if (user == null) {
			System.out.println("user was null");
			return false;
		}
		// if username and password match
		if (user.getUserName().equals(u) && user.getUserPassword().equals(p)) {
			System.out.println("credentials match");
			return true;
		} else {
			// else username and password doesn't match
			return false;
		}
	}

	@Override
	public void addUser(User myNewUser) {
	}

	@Override
	public void removeUser(User user) {
	}

	@Override
	public User getUserById(int id) {
		return null;
	}

	@Override
	public void modifyUser(User enteredUser, int originalId) {
		
	}
}
