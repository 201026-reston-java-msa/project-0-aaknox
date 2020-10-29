package com.revature.dao;

import java.util.List;

import com.revature.model.User;

public interface UserDao {
	// CREATE
	public void insertUser(User user);

	// READ
	public List<User> selectAllPokemon();

	public User selectUserById(int id);

	public User selectUserByUsername(String username);

	// UPDATE
	public void updateUser(User user);

	// DELETE
	public void deleteUser(User user);
}
