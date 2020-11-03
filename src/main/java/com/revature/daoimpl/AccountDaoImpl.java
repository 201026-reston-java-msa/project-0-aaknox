package com.revature.daoimpl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.dao.AccountDao;
import com.revature.model.Account;
import com.revature.model.AccountStatus;
import com.revature.model.AccountType;

public class AccountDaoImpl implements AccountDao {
	private static Logger logger = Logger.getLogger(UserDaoImpl.class);

	private static String url = "jdbc:postgresql://localhost:5432/postgres";
	private static String dbUsername = "postgres";
	private static String dbPassword = "password";

	@Override
	public void insertAccount(Account acc, String uname) {
		System.out.println("inside insertAccount method in account dao impl");
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {

			String sql = "INSERT INTO accounts(account_type, account_status, account_balance, account_creationdate) VALUES( ?, ?, ?, ?);";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, acc.getType().getType());
			ps.setString(2, acc.getStatus().getStatus());
			ps.setDouble(3, acc.getBalance());
			ps.setDate(4, Date.valueOf(acc.getCreationDate()));
			ps.executeUpdate();
			logger.info("insert into account table complete");

			// select new account
			sql = "SELECT * FROM accounts WHERE account_balance = " + acc.getBalance() + ";";
			PreparedStatement ps2 = conn.prepareStatement(sql);
			ResultSet rs = ps2.executeQuery();

			if (rs.next()) {
				System.out.println("new account id after insert: " + rs.getInt("account_id"));
				if (rs.getInt(1) != acc.getStatus().getStatusId()) {
					// update account_status table
					sql = "INSERT INTO account_status (status_id, status_state) VALUES(?, ?);";
					PreparedStatement ps3 = conn.prepareStatement(sql);
					ps3.setInt(1, rs.getInt(1));
					ps3.setString(2, acc.getStatus().getStatus());
					ps3.executeUpdate();

					// update the account_type table
					sql = "INSERT INTO account_types (type_id, type_name) VALUES(?, ?);";
					PreparedStatement ps4 = conn.prepareStatement(sql);
					ps4.setInt(1, rs.getInt(1));
					ps4.setString(2, acc.getType().getType());
					ps4.executeUpdate();

					// update the users table
					System.out.println("Adding account to username: " + uname);
					sql = "UPDATE accounts SET account_user_id = (SELECT u2.user_id FROM users u2 WHERE u2.username = ?) WHERE account_id = ?;";
					PreparedStatement ps5 = conn.prepareStatement(sql);
					ps5.setString(1, uname);
					ps5.setInt(2, rs.getInt(1));
					ps5.executeUpdate();
					System.out.println("all related tables have been updated");
				}
			}

			System.out.println("new account is now in DB");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Account> selectAllAccounts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account selectAccountById(int id) {
		System.out.println("in account dao impl selectAccountById method");
		Account account = new Account();
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {

			String sql = "SELECT * FROM accounts WHERE account_id = " + id + ";";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				account.setAccountId(rs.getInt(1));
				account.setBalance(rs.getDouble(2));
				account.setStatus(new AccountStatus(rs.getInt(1), rs.getString(3)));
				account.setType(new AccountType(rs.getInt(1), rs.getString(4)));
				String date = rs.getDate(6).toString();
				account.setCreationDate(LocalDate.parse(date));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return account;
	}

	@Override
	public List<Account> selectAllAccountsByUsername(String user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Account> selectAllAccountsByStatus(String status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Account> selectAllAccountsByUserID(int userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Account> selectAccountsByType(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAccountBalance(double balance, int id) {
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {

			String sql = "UPDATE accounts SET account_balance = ?  WHERE account_id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setDouble(1, balance);
			ps.setInt(2, id);
			ps.executeUpdate();
			logger.info("new balance is now set");

		} catch (SQLException e) {
			logger.warn("Error in SQL execution to update balance. Stack Trace: ", e);
		}

	}

	@Override
	public void updateAccount(Account enteredAccount, int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAccountById(int id) {
		// TODO Auto-generated method stub

	}

}
