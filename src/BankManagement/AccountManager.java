package BankManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {

	private Connection connection;
	private Scanner scanner;
	
	public AccountManager(Connection connection , Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}
	
	public void credit_money(long account_number) throws SQLException{
		scanner.nextLine();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.println("Enter Security Pin: ");
		String security_pin = scanner.nextLine();
		
		try {
			connection.setAutoCommit(false);
			if(account_number != 0) {
				PreparedStatement  preparedStatement = connection.prepareStatement("select * from accounts where  account_number = ? and security_pin = ?");
				preparedStatement.setDouble(1, amount);
				preparedStatement.setString(2, security_pin);
				ResultSet resultSet = preparedStatement.executeQuery();
				
				if(resultSet.next()) {
					String credit_query = "Update accounts set balance = balance + ? where account_number = ?";
					PreparedStatement  preparedStatement1 = connection.prepareStatement(credit_query);
					preparedStatement1.setDouble(1, amount);
					preparedStatement1.setLong(2, account_number);
					int rowAffected = preparedStatement1.executeUpdate();
					if(rowAffected>0) {
						System.out.println("Rs. "+ amount + " credited Successfully!");
						connection.commit();
						connection.setAutoCommit(true);
					}else {
						System.out.println("Transaction Failed!");
						connection.rollback();
						connection.setAutoCommit(true);
					}
 			}else {
 				System.out.println("Invalid Security Pin!");
 			}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}
	

	public void debit_money(long account_number) throws SQLException{
		scanner.nextLine();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.println("Enter Security Pin: ");
		String security_pin = scanner.nextLine();
		
		try {
			connection.setAutoCommit(false);
			if(account_number != 0) {
				PreparedStatement  preparedStatement = connection.prepareStatement("select * from accounts where  account_number = ? and security_pin = ?");
				preparedStatement.setDouble(1, amount);
				preparedStatement.setString(2, security_pin);
				ResultSet resultSet = preparedStatement.executeQuery();
				if(resultSet.next()) {
					double current_balance = resultSet.getDouble("balance");
					if(amount<=current_balance) {
						String credit_query = "Update accounts set balance = balance - ? where account_number = ?";
						PreparedStatement  preparedStatement1 = connection.prepareStatement(credit_query);
						preparedStatement1.setDouble(1, amount);
						preparedStatement1.setLong(2, account_number);
						int rowAffected = preparedStatement1.executeUpdate();
						if(rowAffected>0) {
							System.out.println("Rs. "+ amount + " debited Successfully!");
							connection.commit();
							connection.setAutoCommit(true);
						}else {
							System.out.println("Transaction Failed!");
							connection.rollback();
							connection.setAutoCommit(true);
						}

					}else {
						System.out.println("Insufficient Balace");
					}
			}else {
 				System.out.println("Invalid Security Pin!");
			}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}
	
	public void transfer_money(long sender_account_number) throws SQLException {        
    scanner.nextLine();
    System.out.println("Enter Receiver Account number: ");
    long receiver_account_number = scanner.nextLong();
    System.out.println("Enter Amount: ");
    double amount = scanner.nextDouble();
    scanner.nextLine();
    System.out.println("Enter Security Pin");
    String security_pin = scanner.nextLine();
    
    try {
        connection.setAutoCommit(false);
        if (sender_account_number != 0 && receiver_account_number != 0) {
            // Corrected query to check sender's account and security pin
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement.setLong(1, sender_account_number); // Set sender account number
            preparedStatement.setString(2, security_pin); // Set security pin
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                double current_balance = resultSet.getDouble("balance");
                if (amount <= current_balance) {
                    String credit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                    String debit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                    
                    PreparedStatement debitpreparedStatement = connection.prepareStatement(debit_query);
                    PreparedStatement creditpreparedStatement = connection.prepareStatement(credit_query);
                    
                    creditpreparedStatement.setDouble(1, amount);
                    creditpreparedStatement.setLong(2, receiver_account_number);
                    debitpreparedStatement.setDouble(1, amount);
                    debitpreparedStatement.setLong(2, sender_account_number);
                    
                    int rowAffected1 = debitpreparedStatement.executeUpdate(), rowAffected2 = creditpreparedStatement.executeUpdate();
                    if (rowAffected1 > 0 && rowAffected2 > 0) {
                        System.out.println("Transaction successful!");
                        System.out.println("Rs. " + amount + " transferred successfully!");
                        connection.commit();
                    } else {
                        System.out.println("Transaction failed!");
                        connection.rollback();
                    }
                } else {
                    System.out.println("Insufficient balance!");
                }
            } else {
                System.out.println("Invalid security pin!");
            }
        } else {
            System.out.println("Invalid account number!");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        connection.setAutoCommit(true);
    }
}

	public void getBalance(long account_number) throws SQLException {
		scanner.nextLine();
		System.out.println("Enter Security pin: ");
		String security_pin = scanner.nextLine();
		try {
			connection.setAutoCommit(false);
			if(account_number != 0) {
				PreparedStatement  preparedStatement = connection.prepareStatement("select balance from accounts where  account_number = ? and security_pin = ?");
				preparedStatement.setDouble(1, account_number);
				preparedStatement.setString(2, security_pin);
				ResultSet resultSet = preparedStatement.executeQuery();
				if(resultSet.next()) {
					double balance = resultSet.getDouble("balance");
					System.out.println("Balance : " + balance);
				}else {
					System.out.println("Invalid pin");
				}
			}

	}catch(SQLException e) {
		e.printStackTrace();
	}
	}		
}

