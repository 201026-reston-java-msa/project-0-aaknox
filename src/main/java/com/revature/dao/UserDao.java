package com.revature.dao;

import java.util.List;

import com.revature.model.User;

public interface UserDao {
	//CRUD NETHODS ONLY
	public void insertUser(User user);
	public List<User> selectAllUsers();
	public User selectUserByUsername(String username);
	public void updateUser(User user);
	public void deleteUser(User user);
}
