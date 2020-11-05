/*******************************************************************************
   AAK Bank Database - Version 1.0
   Script: aak-bank.sql
   Description: Creates and populates the AAK Bank database.
   DB Server: PostgreSql
   Author: Azhya Knox

   Origin Date: 10/17/2020
   Summary:
    - Inital setup of database for Revature Banking Project
********************************************************************************/
/**************************************************
 				Create Stored Procedures
***************************************************/
--PROCEDURE MAY BE DEPRECIATED
--CREATE PROCEDURE transfer_request(
--	senderAccountId int,
--	recieverAccountId int,
--	amount decimal(1000, 2)
--)
--LANGUAGE plpgsql
--AS $$
--BEGIN 
--	--substract amount from sender account balance
--	UPDATE accounts 
--	SET account_balance = account_balance - amount 
--	WHERE account_id = senderAccountId;
--	--add amount to receiver account balance
--	UPDATE accounts 
--	SET account_balance = account_balance + amount 
--	WHERE account_id = recieverAccountId;
--	--save changes
--	COMMIT;
--END
--$$;

CREATE OR REPLACE FUNCTION transfer_request(senderAccountId integer, recieverAccountId integer, amount decimal(15, 2)) 
RETURNS void 
LANGUAGE plpgsql
AS $func$
    BEGIN
		--substract amount from sender account balance
		UPDATE accounts SET account_balance = account_balance - amount WHERE account_id = senderAccountId;
		--add amount to receiver account balance
		UPDATE accounts SET account_balance = account_balance + amount WHERE account_id = recieverAccountId;
    END
    $func$;
   
SELECT * FROM accounts;
SELECT transfer_request(1, 2, 20);

/**************************************************
 				Create Tables
***************************************************/

CREATE TABLE account_types(
	type_id INT PRIMARY KEY, 
	type_name VARCHAR(50) NOT NULL
);
CREATE TABLE account_status(
	status_id INT PRIMARY KEY, 
	status_state VARCHAR(50) NOT NULL
);
CREATE TABLE bank_roles(
	role_Type varchar(30) NOT NULL,
	role_id INT PRIMARY KEY
);
CREATE TABLE accounts(
	account_id SERIAL PRIMARY KEY,
	account_balance DECIMAL(15, 2) NOT NULL,
	account_status VARCHAR(30),
	account_type VARCHAR(30),
	account_user_id INT,
	account_creationDate DATE
);
CREATE TABLE users (
	user_id SERIAL PRIMARY KEY,
	username VARCHAR(30) UNIQUE NOT NULL,
	user_password VARCHAR(30) NOT NULL,
	user_firstname VARCHAR(50) NOT NULL,
	user_lastname VARCHAR(50) NOT NULL,
	user_email VARCHAR(100) NOT NULL,
	user_roleType VARCHAR(30) NOT NULL
);

/**************************************************
 				Populate Tables
***************************************************/

INSERT INTO users(username, user_password, user_firstname, user_lastname, user_email, user_roleType) 
	VALUES('aaknox','password', 'Azhya', 'Knox', 'azhya.knox@gmail.com', 'ADMIN');
INSERT INTO users(username, user_password, user_firstname, user_lastname, user_email, user_roleType) 
	VALUES('employee001','password', 'John', 'Smith', 'john.smith@yahoo.com', 'EMPLOYEE');
INSERT INTO users(username, user_password, user_firstname, user_lastname, user_email, user_roleType) 
	VALUES('customer001','suits123', 'Steve', 'Harvey', 'sharvey@gmail.com', 'CUSTOMER');
INSERT INTO users(username, user_password, user_firstname, user_lastname, user_email, user_roleType) 
	VALUES('customer002','sheready', 'Tiffany', 'Haddish', 'tiff.haddish34@hotmail.com', 'CUSTOMER');
INSERT INTO users (username, user_password, user_firstname, user_lastname, user_email, user_roleType) 
	VALUES ('trigga', 'songz123', 'Trey', 'Songz', 'trey.songz@gmail.com', 'CUSTOMER');

INSERT INTO accounts(account_type, account_status, account_balance, account_creationDate) VALUES('CHECKING','OPEN', 1500.25, '2017-01-15');
INSERT INTO accounts(account_type, account_status, account_balance, account_creationDate) VALUES('CHECKING','OPEN', 754.25, '2018-12-22');
INSERT INTO accounts(account_type, account_status, account_balance, account_creationDate) VALUES('SAVINGS','OPEN', 49.01, '2019-06-14');
UPDATE accounts SET account_user_id = 3 WHERE account_id = 1;
UPDATE accounts SET account_user_id = 3 WHERE account_id = 2;
UPDATE accounts SET account_user_id = 3 WHERE account_id = 3;
--UPDATE accounts SET account_creationdate = '2020-10-18 14:15:39' WHERE account_id = 4;

INSERT INTO bank_roles (role_id, role_type) VALUES (1, 'ADMIN');
INSERT INTO bank_roles (role_id, role_type) VALUES (2, 'EMPLOYEE');
INSERT INTO bank_roles (role_id, role_type) VALUES (3, 'CUSTOMER');
INSERT INTO bank_roles (role_id, role_type) VALUES (4, 'CUSTOMER');
INSERT INTO bank_roles (role_id, role_type) VALUES (5, 'CUSTOMER');

INSERT INTO account_status (status_id, status_state) VALUES(1, 'OPEN');
INSERT INTO account_status (status_id, status_state) VALUES(2, 'OPEN');
INSERT INTO account_status (status_id, status_state) VALUES(3, 'OPEN');

INSERT INTO account_types (type_id, type_name) VALUES(1, 'CHECKING');
INSERT INTO account_types (type_id, type_name) VALUES(2, 'CHECKING');
INSERT INTO account_types (type_id, type_name) VALUES(3, 'SAVINGS');
/**************************************************
 				Create Constraints
 **************************************************/
--BASIC SYNTAX-------
--ALTER TABLE child_table
--ADD CONSTRAINT fk_constraint_childToParent
--FOREIGN KEY (fk_columns)
--REFERENCES parent_table(parent_key_columns)
--ON DELETE CASCADE;
--END OF SYNTAX-------

--Role_id = child/foreign key of user_id
ALTER TABLE bank_roles
ADD CONSTRAINT fk_constraint_bankRolesToUsers
FOREIGN KEY (role_id)
REFERENCES users (user_id);
--status_id = child/foreign key of account_id
ALTER TABLE account_status
ADD CONSTRAINT fk_constraint_accountStatusToAccounts
FOREIGN KEY (status_id)
REFERENCES accounts(account_id);
--type_id = child/foreign key of account_id
ALTER TABLE account_types
ADD CONSTRAINT fk_constraint_accountTypesToAccounts
FOREIGN KEY (type_id)
REFERENCES accounts(account_id);

--USER ID IS FOREIGN KEY IN ACCOUNTS TABLE
ALTER TABLE accounts
ADD CONSTRAINT fk_constraint_accountsToUsers
FOREIGN KEY (account_user_id)
REFERENCES users(user_id);

/*****************************************************************
 					SELECT/DELETE/UPDATE STATEMENTS
 *****************************************************************/
--INSERT INTO accounts(account_type, account_status, account_balance, account_user_id) VALUES('CHECKING','OPEN', 150.28, 5);
--SELECT * FROM accounts WHERE account_id = 1;
--UPDATE account_status SET status_state = 'OPEN' WHERE status_id = 3;
--UPDATE accounts SET account_status = 'OPEN' WHERE account_id = 3;
--SELECT * FROM users WHERE username = 'customer001';
--SELECT * FROM accounts WHERE account_user_id IN (SELECT u2.user_id FROM users u2 WHERE u2.username = 'customer001');
--SELECT * FROM (SELECT * FROM users WHERE user_roletype = 'EMPLOYEE') AS type_table WHERE user_id = 2;
--SELECT * FROM users WHERE user_accountid IS NULL AND user_roletype = 'CUSTOMER' ORDER BY user_id;
--DELETE FROM accounts WHERE account_id > 4;
--DELETE FROM account_status WHERE status_id = 4;
--DELETE FROM accounts WHERE account_id = 7;
--DELETE FROM bank_roles WHERE role_id = 6;
--DELETE FROM users WHERE user_id = 6;
--UPDATE accounts SET account_user_id = (SELECT u2.user_id FROM users u2 WHERE u2.username = 'trigga') WHERE account_id = 5;
/*******************************
 * SELECT ALL STATEMENTS
 *******************************/
UPDATE accounts SET account_balance = 1500 WHERE account_id = 1;
SELECT * FROM users ORDER BY user_id;
SELECT * FROM accounts ORDER BY account_id;
SELECT * FROM bank_roles;
SELECT * FROM account_types;
SELECT * FROM account_status;

/*******************************
 * CALL FUNCTIONS
 *******************************/
--DEBUG: function executes but no changes to accounts table

/*******************************
 * 		Drop Tables
 *******************************/
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS bank_roles;
DROP TABLE IF EXISTS account_status;
DROP TABLE IF EXISTS account_types;