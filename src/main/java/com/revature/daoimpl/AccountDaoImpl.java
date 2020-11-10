package com.revature.daoimpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.dao.AccountDao;
import com.revature.model.Account;
import com.revature.model.AccountStatus;
import com.revature.model.AccountType;
import com.revature.model.BankTransaction;
import com.revature.model.Description;

public class AccountDaoImpl implements AccountDao {
	private static Logger logger = Logger.getLogger(UserDaoImpl.class);
	//REAL CREDENTIALS
	private static String url = "jdbc:postgresql://" + System.getenv("TRAINING_REVATUREDB_URL") + "/AAKBank";
	private static String dbUsername = System.getenv("TRAINING_REVATUREDB_USERNAME");
	private static String dbPassword = System.getenv("TRAINING_REVATUREDB_PASSWORD");
	
	//BACKUP DB CREDENTIALS
//	private static String url = "jdbc:postgresql://localhost:5432/postgres";
//	private static String dbUsername = "postgres";
//	private static String dbPassword = "password";
	
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
					
					//insert initial transaction into transactions table
					sql = "INSERT INTO bank_transactions (trans_account_id, trans_time_stamp, trans_amount, trans_description_code) "
							+ "VALUES (?, ?, ?, 100);";
					PreparedStatement ps9 = conn.prepareStatement(sql);
					ps9.setInt(1, rs.getInt(1));
					ps9.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
					ps9.setDouble(3, acc.getBalance());
					ps9.executeUpdate();
					
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
		logger.info("in account dao impl selectAllAccounts method");
		List<Account> accounts = new ArrayList<Account>();
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {

			String sql = "SELECT * FROM accounts ORDER BY account_id;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Account account = new Account();
				account.setAccountId(rs.getInt("account_id"));
				account.setBalance(rs.getDouble("account_balance"));
				account.setStatus(new AccountStatus(rs.getInt("account_id"), rs.getString("account_status")));
				account.setType(new AccountType(rs.getInt("account_id"), rs.getString("account_type")));
				String date = rs.getDate(6).toString();
				account.setCreationDate(LocalDate.parse(date));
				accounts.add(account);
			}

			logger.info("Sucessful extraction of the account list.");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return accounts;
	}

	@Override
	public int selectUserIdByAccountId(int accId) {
		int ownerId = 0;
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {

			String sql = "SELECT * FROM accounts WHERE account_id = " + accId + ";";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				ownerId = rs.getInt(5);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ownerId;
	}
	
	@Override
	public Account selectAccountByAccountId(int id) {
		logger.info("in account dao impl selectAccountById method");
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
		logger.info("Account found: " + account);
		return account;
	}

	@Override
	public List<Account> selectAllAccountsByUsername(String user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Account> selectAllAccountsByStatus(String status) {
		List<Account> accs = new ArrayList<Account>();
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {

			String sql = "SELECT * FROM accounts WHERE account_status = ? ORDER BY account_id;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, status);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				String date = rs.getDate(6).toString();
				accs.add(new Account(rs.getInt(1), rs.getDouble(2), new AccountStatus(rs.getInt(1), rs.getString(3)),
						new AccountType(rs.getInt(1), rs.getString(4)), LocalDate.parse(date)));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return accs;
	}

	@Override
	public List<Account> selectAllAccountsByUserID(int userid) {
		List<Account> accs = new ArrayList<Account>();
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {

			String sql = "SELECT * FROM accounts WHERE account_user_id = ? ORDER BY account_id;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, userid);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				String date = rs.getDate(6).toString();
				accs.add(new Account(rs.getInt(1), rs.getDouble(2), new AccountStatus(rs.getInt(1), rs.getString(3)),
						new AccountType(rs.getInt(1), rs.getString(4)), LocalDate.parse(date)));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return accs;
	}

	@Override
	public void updateAccountBalance(double balance, int id, int messageCode) {
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {

			String sql = "UPDATE accounts SET account_balance = ?  WHERE account_id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setDouble(1, balance);
			ps.setInt(2, id);
			ps.executeUpdate();
			logger.info("new balance is now set");
			
			//add change to transaction table
			sql = "INSERT INTO bank_transactions (trans_account_id, trans_time_stamp, trans_amount, trans_description_code) " + 
					"VALUES (?, ?, ?, ?);";
			PreparedStatement ps2 = conn.prepareStatement(sql);
			ps2.setInt(1, id);
			ps2.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
			ps2.setDouble(3, balance);
			ps2.setInt(4, messageCode);
			ps2.executeUpdate();
			logger.info("transaction has been logged");

		} catch (SQLException e) {
			logger.warn("Error in SQL execution to update balance. Stack Trace: ", e);
		}

	}

	@Override
	public void updateAccountStatus(String status, int id, int messageCode) {
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
			// update status table first
			String sql = "UPDATE account_status SET status_state = ? WHERE status_id = ?";
			PreparedStatement ps0 = conn.prepareStatement(sql);
			ps0.setString(1, status);
			ps0.setInt(2, id);
			ps0.executeUpdate();
			
			//now updating accounts table
			sql = "UPDATE accounts SET account_status = ?  WHERE account_id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, status);
			ps.setInt(2, id);
			ps.executeUpdate();
			
			//add change to transaction table
			sql = "INSERT INTO bank_transactions (trans_account_id, trans_time_stamp, trans_amount, trans_description_code) " + 
					"VALUES (?, ?, ?, ?);";
			PreparedStatement ps2 = conn.prepareStatement(sql);
			ps2.setInt(1, id);
			ps2.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
			Account acc = selectAccountByAccountId(id);
			ps2.setDouble(3, acc.getBalance());
			ps2.setInt(4, messageCode);
			ps2.executeUpdate();
			logger.info("transaction has been logged");

		} catch (SQLException e) {
			logger.warn("Error in SQL execution to update status. Stack Trace: ", e);
		}
		
	}
	
	@Override
	public void transferRequestFunc(double amount, int fromId, int toId) {
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
			logger.debug("TESTING FUNCTION...");
			String sql = "SELECT transfer_request(?, ?, ?);";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, fromId);
			ps.setInt(2, toId);
			//convert from double to big decimal (amount is of decimal datatype within database function)
			ps.setBigDecimal(3, BigDecimal.valueOf(amount));
			try {
				ps.executeQuery();
			} catch (Exception e) {
				logger.error("PreparedStatement failed here. Stack Trace: ", e);
			}
			logger.info("Transfer request is completed");

		} catch (SQLException e) {
			logger.warn("Error in SQL execution to update balance. Stack Trace: ", e);
		}
		
	}
	
	@Override
	public void deleteAccountByAccountId(int id) {
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
			// delete from child tables to accounts table first
			String sql = "DELETE FROM account_status WHERE status_id = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();

			sql = "DELETE FROM account_types WHERE type_id = ?;";
			PreparedStatement ps2 = conn.prepareStatement(sql);
			ps2.setInt(1, id);
			ps2.executeUpdate();
			
			sql = "DELETE FROM bank_transactions WHERE trans_account_id = ?;";
			PreparedStatement ps5 = conn.prepareStatement(sql);
			ps5.setInt(1, id);
			ps5.executeUpdate();

			// now from accounts table --> delete account
			sql = "DELETE FROM accounts WHERE account_id = ?;";
			PreparedStatement ps3 = conn.prepareStatement(sql);
			ps3.setInt(1, id); // 1st "?"
			ps3.executeUpdate();

		} catch (SQLException e) {
			logger.warn("SQL statement failed. Stack Trace: ", e);
		}

	}

	@Override
	public List<BankTransaction> transactionHistoryByAccountId(int accountId) {
		List<BankTransaction> transList = new ArrayList<BankTransaction>();
		
		try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
			//select with join
			String sql = "SELECT trans_id, trans_account_id, trans_time_stamp, trans_amount, trans_description_code, description_message " + 
					"FROM bank_transactions FULL JOIN descriptions ON trans_description_code = description_code " + 
					"WHERE trans_account_id = ? ORDER BY trans_id;";
			PreparedStatement ps0 = conn.prepareStatement(sql);
			ps0.setInt(1, accountId);
			ResultSet rs = ps0.executeQuery();
			
			while (rs.next()) {
				BankTransaction transaction = new BankTransaction();
				transaction.setTransactionId(rs.getInt(1));
				transaction.setTransactionAccountId(rs.getInt(2));
				transaction.setTransactionTimeStamp(rs.getTimestamp(3).toLocalDateTime());
				transaction.setTransactionBalance(rs.getDouble(4));
				transaction.setDescription(new Description(rs.getInt(5), rs.getString(6)));
				transList.add(transaction);
			}

		} catch (SQLException e) {
			logger.warn("Error in SQL execution of join. Stack Trace: ", e);
		}
		
		return transList;
	}
}
