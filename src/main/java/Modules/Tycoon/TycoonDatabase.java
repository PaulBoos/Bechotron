package Modules.Tycoon;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TycoonDatabase {
	
	private Connection connection;
	private final String DATABASEPATH = "jdbc:sqlite:./data/tycoon.db";
	private final HashMap<String, Account> accountRegistry = new HashMap<>();
	private final HashMap<Integer, Facility> facilityRegistry = new HashMap<>();
	private final HashMap<Long, Landlord> landlordRegistry = new HashMap<>();
	
	public TycoonDatabase() throws SQLException {
		connect();
		checkAccountTable();
		checkFacilityTable();
	}
	
	public Account getAccount(long user, Currency currency) throws SQLException {
		String key = user + ":" + currency.ordinal();
		if(accountRegistry.containsKey(key)) {
			return accountRegistry.get(key);
		} else {
			Account a = new Account(this, currency, user, !(getBalance(user, currency.ordinal()) == 0));
			accountRegistry.put(user + ":" + currency.ordinal(), a);
			return a;
		}
	}
	
	public List<Account> getAccounts(long user) throws SQLException {
		connect();
		try {
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT * FROM Account WHERE memberid = ?");
			pstmt.setLong(1, user);
			if(pstmt.execute()) {
				ResultSet rs = pstmt.getResultSet();
				ArrayList<Account> out = new ArrayList<>();
				if(rs.isClosed()) return new ArrayList<>();
				else while(rs.next()) {
					int currency = rs.getInt("currency");
					if(accountRegistry.containsKey(user + ":" + currency)) // Check if cache contains the account in question to avoid doublettes.
						out.add(accountRegistry.get(user + ":" + currency)); // Return the account from cache.
					else {
						Account a = new Account(this, Currency.values()[currency], user, true); // Create new account
						accountRegistry.put(user + ":" + currency, a); // Add new account to cache
						out.add(a); // Add new account to return list
					}
				}
				return out;
			} else {
				System.out.println("Could not execute");
				return new ArrayList<>();
			}
		} catch(SQLException throwables) {
			throwables.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	public int getBalance(long account, int currency) throws SQLException {
		connect();
		try {
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT * FROM Account WHERE memberid = ? AND currency = ?");
			pstmt.setLong(1, account);
			pstmt.setInt(2, currency);
			if(pstmt.execute()) {
				ResultSet rs = pstmt.getResultSet();
				if(rs.isClosed()) return 0;
				else return rs.getInt("balance");
			} else {
				System.out.println("Could not execute");
				return 0;
			}
		} catch(SQLException throwables) {
			throwables.printStackTrace();
			return 0;
		}
	}
	
	public List<Facility> getFacilitiesOwned(long owner) throws SQLException {
		connect();
		try {
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT * FROM Facility WHERE owner = ?");
			pstmt.setLong(1, owner);
			if(pstmt.execute()) {
				ResultSet rs = pstmt.getResultSet();
				ArrayList<Facility> out = new ArrayList<>();
				if(rs.isClosed()) return new ArrayList<>();
				else while(rs.next()) {
					if(facilityRegistry.containsKey(rs.getInt("facilityid"))) {
						out.add(facilityRegistry.get(rs.getInt("facilityid")));
					} else {
						Facility f = new Facility(
								rs.getInt("facilityid"),
								rs.getString("name"),
								Facility.FacilityType.valueOf(rs.getString("type").toUpperCase()),
								rs.getFloat("locX"),
								rs.getFloat("locY"));
						facilityRegistry.put(rs.getInt("facilityid"), f);
						out.add(f);
					}
				}
				return out;
			} else {
				System.out.println("Could not execute");
				return new ArrayList<>();
			}
		} catch(SQLException throwables) {
			throwables.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	public Facility getFacility(int facilityid) throws SQLException {
		connect();
		try {
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT * FROM Facility WHERE facilityid = ?");
			pstmt.setLong(1, facilityid);
			if(pstmt.execute()) {
				ResultSet rs = pstmt.getResultSet();
				if(rs.isClosed()) return null;
				else {
					if(facilityRegistry.containsKey(facilityid)) {
						return facilityRegistry.get(facilityid);
					} else {
						Facility f = new Facility(
								facilityid,
								rs.getString("name"),
								Facility.FacilityType.valueOf(rs.getString("type").toUpperCase()),
								rs.getInt("locX"),
								rs.getInt("locY"));
						facilityRegistry.put(facilityid, f);
						return f;
					}
				}
			} else {
				System.out.println("Could not execute");
				return null;
			}
		} catch(SQLException throwables) {
			throwables.printStackTrace();
			return null;
		}
	}
	
	private void checkFacilityTable() throws SQLException {
		connect();
		connection.prepareStatement(
				"CREATE TABLE IF NOT EXISTS Facility " +
						"(facilityid INTEGER primary key autoincrement NOT NULL, " +
						"type TEXT default null, " +
						"name TEXT default 'unknown facility name', " +
						"owner INTEGER default null, " +
						"locX FLOAT default null, " +
						"locY FLOAT default null)"
		).execute();
	}
	
	private void checkAccountTable() throws SQLException {
		connect();
		connection.prepareStatement(
				"CREATE TABLE IF NOT EXISTS Account " +
						"(memberid INTEGER primary key NOT NULL, " +
						"currency INTEGER default 0 NOT NULL," +
						"balance INTEGER default 0)"
		).execute();
	}
	
	// TODO other checkTables
	
	private void connect() throws SQLException {
		if(connection == null) {
			connection = DriverManager.getConnection(DATABASEPATH);
		}
	}
	
	public void saveBalance(Account account) throws SQLException {
		if(account.fromDatabase) updateAccount(account);
		else                     insertAccount(account);
	}
	
	private void insertAccount(Account account) throws SQLException {
		connect();
		PreparedStatement pstmt = connection.prepareStatement(
				"INSERT INTO Account (memberid, currency, balance) VALUES (?, ?, ?)");
		pstmt.setLong(1, account.getBAN());
		pstmt.setInt(2, account.getCurrency().ordinal());
		pstmt.setInt(3, account.getBalance());
		pstmt.execute();
	}
	
	private void updateAccount(Account account) throws SQLException {
		connect();
		try {
			PreparedStatement pstmt = connection.prepareStatement("UPDATE Account SET balance = ? WHERE memberid = ? AND currency = ?");
			pstmt.setInt(1, account.getBalance());
			pstmt.setLong(2, account.getBAN());
			pstmt.setInt(3, account.getCurrency().ordinal());
			if(pstmt.executeUpdate() == 0) {
				pstmt.close();
			}
		} catch(SQLException throwables) {
			throwables.printStackTrace();
		}
	}
	
}
