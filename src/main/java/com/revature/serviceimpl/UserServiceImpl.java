package com.revature.serviceimpl;

import java.util.List;

import org.apache.log4j.Logger;
import com.revature.dao.UserDao;
import com.revature.daoimpl.UserDaoImpl;
import com.revature.model.User;
import com.revature.service.UserService;

public class UserServiceImpl implements UserService {
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	private UserDao userDao = new UserDaoImpl();
	
	@Override
	public void addUser(User myNewUser) {
		userDao.insertUser(myNewUser);
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByUsername(String uname) {
		User user = userDao.selectUserByUsername(uname);
		if(user == null) {
			return null;
		}else {	
			return user;
		}
	}

	@Override
	public void modifyUser(User user) {
		userDao.updateUser(user);
	}
	
	@Override
	public void modifyPassword(String uname, String pw) {
		userDao.updatePassword(uname, pw);
	}

	@Override
	public void removeUser(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkUsernameAndPassword(String u, String p) {
		User user = userDao.selectUserByUsername(u);
		
		//user not found in DB
		if(user == null) {
			logger.error("user was null");
			return false;
		}
		//if username and password match
		if(user.getUsername().equals(u) && 
				user.getPassword().equals(p)) {
			logger.info("credentials match");
			return true;
		}else {
			//else username and password doesn't match
			logger.error("Credentials do not match database information for this user.");
			return false;
		}
	}

	

}
