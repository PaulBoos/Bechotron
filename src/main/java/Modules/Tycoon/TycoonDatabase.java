package Modules.Tycoon;

import java.sql.*;

public class TycoonDatabase {
	
	private Connection connection;
	private final String DATABASEPATH = "jdbc:sqlite:./data/tycoon.db";
	
	public TycoonDatabase() throws SQLException {
		connect();
		checkMoneyTable();
		checkFacilityTable();
	}
	
	public int getBalance(int account) throws SQLException {
		connect();
		try {
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT * FROM Money WHERE memberid = ?");
			pstmt.setLong(1, account);
			if(pstmt.execute()) {
				ResultSet rs = pstmt.getResultSet();
				if(rs.isClosed()) return 0;
				else return rs.getInt(1);
			} else {
				System.out.println("Could not execute");
				return 0;
			}
		} catch(SQLException throwables) {
			throwables.printStackTrace();
			return 0;
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
				if(rs.isClosed()) return 0;
				else return rs.getInt(1);
			} else {
				System.out.println("Could not execute");
				return 0;
			}
		} catch(SQLException throwables) {
			throwables.printStackTrace();
			return 0;
		}
	}
	
	private void checkFacilityTable() throws SQLException {
		connect();
		connection.prepareStatement(
				"CREATE TABLE IF NOT EXISTS Facility " +
						"(facilityid INTEGER primary key autoincrement, " +
						"type TEXT default null, " +
						"name TEXT default 'unknown facility name', " +
						"owner INTEGER default null, " +
						"locX FLOAT default 0, " +
						"locY FLOAT default 0)"
		).execute();
	}
	
	private void checkMoneyTable() throws SQLException {
		connect();
		connection.prepareStatement(
				"CREATE TABLE IF NOT EXISTS Money " +
						"(memberid INTEGER primary key, " +
						"balance INTEGER default 0)"
		).execute();
	}
	
	// TODO other checkTables
	
	private void connect() throws SQLException {
		if(connection == null) {
			connection = DriverManager.getConnection(DATABASEPATH);
		}
	}
	
}
