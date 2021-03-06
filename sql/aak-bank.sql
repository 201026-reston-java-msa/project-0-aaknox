/*******************************************************************************
   AAK Bank Database - Version 1.2
   Script: aak-bank.sql
   Description: Creates and populates the AAK Bank database.
   DB Server: PostgreSql
   Author: Azhya Knox

   Origin Date: 11/09/2020
   Summary:
    - Inital setup of database for Revature Banking Project
    - Added function that executes a transfer request for API
    - Added join statement for transaction history/description junction table
********************************************************************************/
/*******************************
 * 		Drop Tables and Functions
 *******************************/
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS bank_roles;
DROP TABLE IF EXISTS account_status;
DROP TABLE IF EXISTS account_types;
DROP TABLE IF EXISTS bank_transactions;
DROP TABLE IF EXISTS descriptions;
DROP FUNCTION IF EXISTS transfer_request();
/**************************************************
 				Create Functions
***************************************************/
CREATE OR REPLACE FUNCTION transfer_request(senderAccountId integer, recieverAccountId integer, amount decimal(15, 2)) 
RETURNS void 
LANGUAGE plpgsql
AS $func$
DECLARE 
	senderAfterBal decimal(15, 2);
	receiverAfterBal decimal(15, 2);
BEGIN
--substract amount from sender account balance
UPDATE accounts SET account_balance = account_balance - amount WHERE account_id = senderAccountId;
--select new account balance after transfer
SELECT account_balance INTO senderAfterBal FROM accounts WHERE account_id = senderAccountId;

--add amount to receiver account balance
UPDATE accounts SET account_balance = account_balance + amount WHERE account_id = recieverAccountId;
--select new account balance after transfer
SELECT account_balance INTO receiverAfterBal FROM accounts WHERE account_id = recieverAccountId;

--insert new transaction into bank transaction table
INSERT INTO bank_transactions (trans_account_id, trans_time_stamp, trans_amount, trans_description_code) 
VALUES (senderAccountId, current_timestamp, senderAfterBal, 700);

INSERT INTO bank_transactions (trans_account_id, trans_time_stamp, trans_amount, trans_description_code) 
VALUES (recieverAccountId, current_timestamp, receiverAfterBal, 800);

END
$func$;

--for transaction history based on account id
SELECT trans_id, trans_account_id, trans_time_stamp, trans_amount, trans_description_code, description_message
FROM bank_transactions 
FULL JOIN descriptions 
ON trans_description_code = description_code 
WHERE trans_account_id = 1
ORDER BY trans_id;
/**************************************************
 				Create Tables
***************************************************/

CREATE TABLE users (
	user_id SERIAL PRIMARY KEY,
	username VARCHAR(30) UNIQUE NOT NULL,
	user_password VARCHAR(30) NOT NULL,
	user_firstname VARCHAR(50) NOT NULL,
	user_lastname VARCHAR(50) NOT NULL,
	user_email VARCHAR(100) NOT NULL,
	user_roleType VARCHAR(30) NOT NULL
);

CREATE TABLE accounts(
	account_id SERIAL PRIMARY KEY,
	account_balance DECIMAL(15, 2) NOT NULL,
	account_status VARCHAR(30),
	account_type VARCHAR(30),
	account_user_id INT,
	account_creationDate DATE
);
--USER ID IS FOREIGN KEY IN ACCOUNTS TABLE
ALTER TABLE accounts
ADD CONSTRAINT fk_constraint_accountsToUsers
FOREIGN KEY (account_user_id)
REFERENCES users(user_id);

CREATE TABLE account_types(
	type_id INT PRIMARY KEY, 
	type_name VARCHAR(50) NOT NULL
);
--ACCOUNT ID IS FOREIGN KEY IN ACCOUNT_TYPES TABLE
ALTER TABLE account_types
ADD CONSTRAINT fk_constraint_accountTypesToAccounts
FOREIGN KEY (type_id)
REFERENCES accounts(account_id);

CREATE TABLE account_status(
	status_id INT PRIMARY KEY, 
	status_state VARCHAR(50) NOT NULL
);
--ACCOUNT ID IS FOREIGN KEY IN ACCOUNT_STATUS TABLE
ALTER TABLE account_status
ADD CONSTRAINT fk_constraint_accountStatusToAccounts
FOREIGN KEY (status_id)
REFERENCES accounts(account_id);

CREATE TABLE bank_roles(
	role_Type varchar(30) NOT NULL,
	role_id INT PRIMARY KEY
);
--USER ID IS FOREIGN KEY IN BANK_ROLES TABLE
ALTER TABLE bank_roles
ADD CONSTRAINT fk_constraint_bankRolesToUsers
FOREIGN KEY (role_id)
REFERENCES users (user_id);

CREATE TABLE bank_transactions(
	trans_id serial PRIMARY KEY,
	trans_account_id int,
	trans_time_stamp timestamp,
	trans_amount DECIMAL(15, 2) NOT NULL,
	trans_description_code int NOT NULL 
);
--account id is foreign key in bank_transactions table
ALTER TABLE bank_transactions
ADD CONSTRAINT fk_constraint_bankTransactionsToAccounts
FOREIGN KEY (trans_account_id)
REFERENCES accounts(account_id);

--create sequence for description code
CREATE SEQUENCE desciption_sequence
INCREMENT 100
START 100
OWNED BY descriptions.description_code;

CREATE TABLE descriptions(
	description_code int PRIMARY KEY,
	description_message varchar(100) NOT NULL 
);
--description code is foreign key in bank_transactions table
ALTER TABLE bank_transactions
ADD CONSTRAINT fk_constraint_descriptionCodeToBankTransactions
FOREIGN KEY (trans_description_code)
REFERENCES descriptions(description_code);
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

INSERT INTO descriptions values(nextval('desciption_sequence'), 'opened new account, awaiting admin approval');
INSERT INTO descriptions values(nextval('desciption_sequence'),'started existing account');
INSERT INTO descriptions values(nextval('desciption_sequence'),'account approved by adminstrator');
INSERT INTO descriptions values(nextval('desciption_sequence'),'account declined by adminstrator');
INSERT INTO descriptions values(nextval('desciption_sequence'),'made a deposit');
INSERT INTO descriptions values(nextval('desciption_sequence'),'made a withdrawal');
INSERT INTO descriptions values(nextval('desciption_sequence'),'transferred funds to another account');
INSERT INTO descriptions values(nextval('desciption_sequence'),'recieved funds from another account');

INSERT INTO bank_transactions (trans_account_id, trans_time_stamp, trans_amount, trans_description_code) VALUES (1, '2017-01-15 09:05:06', 1500.25, 200);
INSERT INTO bank_transactions (trans_account_id, trans_time_stamp, trans_amount, trans_description_code) VALUES (2, '2018-12-22 11:45:23', 754.25, 200);
INSERT INTO bank_transactions (trans_account_id, trans_time_stamp, trans_amount, trans_description_code) VALUES (3, '2019-06-14 14:33:12', 49.01, 200);
/*******************************
 * SELECT ALL STATEMENTS
 *******************************/
SELECT * FROM users ORDER BY user_id;
SELECT * FROM accounts ORDER BY account_id;
SELECT * FROM bank_roles ORDER BY role_id;
SELECT * FROM account_types;
SELECT * FROM account_status;
SELECT * FROM bank_transactions;
SELECT * FROM descriptions ORDER BY description_code;





/*****************************************************************
 					SELECT/DELETE/UPDATE STATEMENTS
 *****************************************************************/
--all statements commented out - these were used for testing
--SELECT transfer_request(1, 3, 50.25);
--SELECT * FROM bank_transactions WHERE trans_account_id = 1;
--UPDATE descriptions SET description_message = 'started existing account' WHERE description_code = 200;
--UPDATE bank_transactions SET trans_description_code = 200 WHERE trans_id = 1;
--UPDATE bank_transactions SET trans_description_code = 200 WHERE trans_id = 2;
--UPDATE bank_transactions SET trans_description_code = 200 WHERE trans_id = 3;
--UPDATE accounts SET account_creationdate = '2020-10-18 14:15:39' WHERE account_id = 4;
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
--UPDATE accounts SET account_balance = 1500 WHERE account_id = 1;