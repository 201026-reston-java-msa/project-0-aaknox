package com.revature.daoimpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.dao.UserDao;
import com.revature.model.Account;
import com.revature.model.BankRole;
import com.revature.model.User;

public class UserDaoImpl implements UserDao {
	private static Logger logger = Logger.getLogger(UserDaoImpl.class);

	// REAL CREDENTIALS
	private static String url = "jdbc:postgresql://" + System.getenv("TRAINING_REVATUREDB_URL") + "/AAKBank";
	private static String dbUsername = System.getenv("TRAINING_REVATUREDB_USERNAME");
	private static String dbPassword = System.getenv("TRAINING_REVATUREDB_PASSWORD");

	// BACKUP DB CREDENTIALS
//	private static String url = "jdbc:postgresql://localhost:5432/postgres";
//	private static String dbUsername = "postgres";
//	private static String dbPassword = "password";

	@Override
	public void insertUser(User user) {
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
			String sql = "INSERT INTO users(username, user_password, user_firstname, user_lastname, user_email, user_roleType)"
					+ " VALUES( ?, ?, ?, ?, ?, ?);";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getFirstName());
			ps.setString(4, user.getLastName());
			ps.setString(5, user.getEmail());
			ps.setString(6, user.getRole().getRoleType());
			ps.executeUpdate();

			// select the new user by username
			sql = "SELECT * FROM users WHERE username = ?;";
			PreparedStatement ps2 = conn.prepareStatement(sql);
			ps2.setString(1, user.getUsername());
			ResultSet rs = ps2.executeQuery();
			if (rs.next()) {
				// save the new user id to our user object
				user.setUserId(rs.getInt(1));
			}

			// insert new role to role table
			sql = "INSERT INTO bank_roles (role_id, role_type) VALUES (?, ?);";
			PreparedStatement ps3 = conn.prepareStatement(sql);
			ps3.setInt(1, user.getUserId());
			ps3.setString(2, user.getRole().getRoleType());
			ps3.executeUpdate();

			logger.info("User has been successfully added to Bank API database.");
		} catch (SQLException e) {
			logger.debug("SQL statement failed to execute in UserDaoImpl class::insertUser method. Stack Trace: ", e);
		}
	}

	@Override
	public List<User> selectAllUsers() {
		List<User> userList = new ArrayList<User>();
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
			String sql = "SELECT * FROM users ORDER BY user_id;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				userList.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), new BankRole(rs.getInt(1), rs.getString(7)), new ArrayList<Account>()));
			}
		} catch (SQLException e) {
			logger.warn("SQL statement failed to execute in UserDaoImpl class::selectAllUsers method. Stack Trace: ",
					e);
		}
		return userList;
	}

	@Override
	public User selectUserByUsername(String username) {
		User user = new User();

		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
			String sql = "SELECT * FROM users WHERE username = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				user.setUserId(rs.getInt(1));
				user.setUsername(username);
				user.setPassword(rs.getString(3));
				user.setFirstName(rs.getString(4));
				user.setLastName(rs.getString(5));
				user.setEmail(rs.getString(6));
				user.setRole(new BankRole(rs.getInt(1), rs.getString(7)));
				user.setAccounts(new ArrayList<Account>());
			}

			logger.info("User search by username was successful. " + user);
		} catch (SQLException e) {
			logger.warn(
					"SQL statement failed to execute in UserDaoImpl class::selectUserByUsername method. Stack Trace: ",
					e);
		}

		return user;
	}

	@Override
	public void updateUser(User user) {
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
			// delete from bank roles table first
			String sql = "UPDATE bank_roles SET role_type = ? WHERE role_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user.getRole().getRoleType());
			ps.setInt(2, user.getRole().getRoleId());
			ps.executeUpdate();

			// now updating users table
			sql = "UPDATE users SET username = ?, user_password = ?, user_firstname = ?, user_lastname = ?, user_email = ?, user_roletype =? WHERE user_id = ?;";
			PreparedStatement ps3 = conn.prepareStatement(sql);
			ps3.setString(1, user.getUsername());
			ps3.setString(2, user.getPassword());
			ps3.setString(3, user.getFirstName());
			ps3.setString(4, user.getLastName());
			ps3.setString(5, user.getEmail());
			ps3.setString(6, user.getRole().getRoleType());
			ps3.setInt(7, user.getUserId());
			ps3.executeUpdate();

			logger.info("User update for userId " + user.getUserId() + " was successful. ");

		} catch (SQLException e) {
			logger.warn("SQL statement failed - UserDaoImpl class::updateUser method. Stack Trace: ", e);
		}
	}

	@Override
	public void updatePassword(String username, String password) {
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
			// now updating users table
			String sql = "UPDATE users SET user_password = ? WHERE username = ?;";
			PreparedStatement ps3 = conn.prepareStatement(sql);
			ps3.setString(1, password);
			ps3.setString(2, username);
			ps3.executeUpdate();

			logger.info("User update for " + username + " was successful. ");

		} catch (SQLException e) {
			logger.warn("SQL statement failed - UserDaoImpl class::updateUser method. Stack Trace: ", e);
		}

	}

	@Override
	public void deleteUser(User user) {
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
			// delete from bank roles table first
			String sql = "DELETE FROM bank_roles WHERE role_id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, user.getUserId());
			ps.executeUpdate();

			// now delete user
			sql = "DELETE FROM users WHERE user_id = ?;";
			PreparedStatement ps3 = conn.prepareStatement(sql);
			ps3.setInt(1, user.getUserId());
			ps3.executeUpdate();

			logger.info("User " + user.getUsername() + " has successfully been deleted from database.");

		} catch (SQLException e) {
			logger.warn("SQL statement failed - UserDaoImpl class::deleteUser method. Stack Trace: ", e);
		}
	}

}
