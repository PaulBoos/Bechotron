package Databases;

import java.sql.*;

public abstract class dbAccess {
	
	private final String DATABASEPATH;
	protected Connection conn;
	
	protected dbAccess(String path) {
		DATABASEPATH = path;
		try {
			connect();
		} catch(SQLException throwables) {
			throwables.printStackTrace();
		}
	}
	
	protected void connect() throws SQLException {
		if(conn == null) {
			conn = DriverManager.getConnection(DATABASEPATH);
		}
	}
	
	protected void disconnect() throws SQLException {
		if(conn != null) {
			conn.close();
		}
	}
	
	/*
	public synchronized long getMember(long user) throws SQLException {
		connect();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM memberID WHERE userID = ?");
		pstmt.setLong(0, user);
		pstmt.execute();
		ResultSet rs = pstmt.getResultSet();
		return 0L;
	}
	
	public synchronized long getGuild(long member) throws SQLException {
		connect();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM memberID WHERE memberID = ?");
		pstmt.setLong(0, member);
		pstmt.execute();
		ResultSet rs = pstmt.getResultSet();
		try {
			return rs.getLong("guildID");
		} catch(SQLException throwables) {
			throwables.printStackTrace();
		}
		return 0L;
	}
	
	public synchronized long getUser(long member) throws SQLException {
		connect();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM memberID WHERE memberID = ?");
		pstmt.setLong(0, member);
		pstmt.execute();
		ResultSet rs = pstmt.getResultSet();
		try {
			return rs.getLong("userID");
		} catch(SQLException throwables) {
			throwables.printStackTrace();
		}
		return 0L;
	}
	*/
}
