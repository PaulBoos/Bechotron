package Modules.SlashCommands;

import Databases.dbAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CommandDB extends dbAccess {
	
	final SlashModule module;
	
	CommandDB(SlashModule slashModule) {
		super("jdbc:sqlite:data/guild/" + slashModule.instance.guild + ".db");
		module = slashModule;
		try {
			connect();
		} catch (SQLException throwables) {
			System.out.println(throwables.getErrorCode());
		}
	}
	
	public synchronized ArrayList<Permission> readPermissions(long member) throws SQLException {
		connect();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Permission WHERE memberID = ?");
		pstmt.setLong(1, member);
		pstmt.execute();
		ResultSet rs = pstmt.getResultSet();
		String[] strings = rs.getString("permissions").split(",");
		ArrayList<Permission> perms = new ArrayList<>();
		for(String s: strings) {
			perms.add(Permission.getPermission(s));
		}
		return perms;
	}
	
	public synchronized void writePermissions(long member, Permission... permissions) throws SQLException {
		connect();
		
		StringBuilder insert = new StringBuilder();
		for(Permission p: permissions) insert.append(p.sign).append(',');
		insert.deleteCharAt(insert.length());
		
		PreparedStatement pstmt = conn.prepareStatement(
				"UPDATE Permission SET permissions = ? WHERE memberID = ?");
		pstmt.setLong(2,member);
		pstmt.setString(1,insert.toString());
	}
	
	private synchronized void newPermissions(long member, Permission... permissions) throws SQLException {
		connect();
		
		StringBuilder insert = new StringBuilder();
		for(Permission p: permissions) insert.append(p.sign).append(',');
		insert.deleteCharAt(insert.length()-1);
		
		PreparedStatement pstmt = conn.prepareStatement(
				"INSERT INTO Permission(memberID,permissions) VALUES(?,?)");
		pstmt.setLong(1,member);
		pstmt.setString(2,insert.toString());
	}
	
}
