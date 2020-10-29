package com.revature.daoimpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.revature.dao.UserDao;
import com.revature.model.BankRole;
import com.revature.model.User;

public class UserDaoImpl implements UserDao {

	// AWS DB Credentials////////////////
//		private static String url= "jdbc:postgresql://" + 
//				System.getenv("TRAINING_REVATUREDB_URL") + "/AAKBank";
//		private static String username= System.getenv("TRAINING_REVATUREDB_USERNAME");
//		private static String password= System.getenv("TRAINING_REVATUREDB_PASSWORD");

	// FOR OFFLINE TESTING
	private static String url = "jdbc:postgresql://localhost:5432/postgres";
	private static String username = "postgres";
	private static String password = "password";
	
	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Static block has failed me");
		}
	}

	@Override
	public void insertUser(User user) {
		try (Connection conn = DriverManager.getConnection(url, username, password)) {

			String sql = "INSERT INTO users(username, user_password, user_firstname, user_lastname, user_email, user_roleType) "
					+ "VALUES(?,?, ?, ?, ?, ?);";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, user.getUserName()); // 1st "?"
			ps.setString(2, user.getUserPassword()); // 2nd "?"
			ps.setString(3, user.getUserFirstName()); // 3rd "?"
			ps.setString(4, user.getUserLastName()); // 4th "?"
			ps.setString(5, user.getUserEmail()); // 5th "?"
			ps.setString(6, user.getRole().getRole()); // 6th "?"
			ps.executeUpdate();
			//select the new user by username
			sql="SELECT * FROM users WHERE username = ?;";
			PreparedStatement ps2 = conn.prepareStatement(sql);
			ps2.setString(1, user.getUserName());
			ResultSet rs = ps2.executeQuery();
			if(rs.next()) {
				//save the new user id to our user object
				user.setUserId(rs.getInt(1));
				//save the new user creation date to our user object
				user.setUserCreationDate(LocalDate.parse(rs.getDate("user_creationDate").toString()));
			}
			
			//insert new role to role table
			sql="INSERT INTO bank_roles (role_id, role_type) VALUES (?, ?);";
			PreparedStatement ps3 = conn.prepareStatement(sql);
			ps3.setInt(1, user.getUserId());
			ps3.setString(2, user.getRole().getRole());
			ps3.executeUpdate();
			
			System.out.println("All insert for user creation complete");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<User> selectAllPokemon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User selectUserById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User selectUserByUsername(String uname) {
		System.out.println("in user dao impl selectUserByUsername method");

		User user = new User();
		user.setRole(new BankRole());

		try (Connection conn = DriverManager.getConnection(url, username, password)) {

			String sql = "SELECT * FROM users WHERE username = '" + uname + "';";

			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				user.setUserId(rs.getInt(1));

				user.setUserName(rs.getString(2));
				user.setUserPassword(rs.getString(3));
				user.setUserFirstName(rs.getString(4));
				user.setUserLastName(rs.getString(5));
				user.setUserEmail(rs.getString(6));
				user.getRole().setRole(rs.getString(7));
			}else {
				System.out.println("No user found in DB...setting it to null...");
				user = null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("User found in DB: " + user);
		return user;
	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteUser(User user) {
		// TODO Auto-generated method stub

	}

}
