package com.revature.daoimpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import com.revature.dao.UserDao;
import com.revature.model.User;

public class UserDaoImpl implements UserDao{
	private static String url = "jdbc:postgresql://localhost:5432/postgres";
	private static String dbUsername = "postgres";
	private static String dbPassword = "password";
	
	@Override
	public User insertUser(User user) {
		try(Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
			String sql = "";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	@Override
	public List<User> selectAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public User selectUserByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public User deleteUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
