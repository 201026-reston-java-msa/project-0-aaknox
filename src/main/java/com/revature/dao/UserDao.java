package com.revature.dao;

import java.util.List;

import com.revature.model.User;

public interface UserDao {
	//CRUD NETHODS ONLY
	public User insertUser(User user);
	public List<User> selectAllUsers();
	public User selectUserByUsername(String username);
	public User updateUser(User user);
	public User deleteUser(User user);
}
