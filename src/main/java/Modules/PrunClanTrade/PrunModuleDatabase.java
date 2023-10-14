package Modules.PrunClanTrade;

import Databases.dbAccess;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PrunModuleDatabase extends dbAccess {

	public PrunModuleDatabase() throws SQLException {
		super("jdbc:sqlite:./data/prun.db");
		checkOfferTable();
	}
	
	private void checkOfferTable() throws SQLException {
		connect();
		PreparedStatement pstmt = conn.prepareStatement(
				"""
					
						CREATE TABLE IF NOT EXISTS trade_offers (
							id INTEGER PRIMARY KEY,
							item TEXT NOT NULL,
							amount TEXT,
							price REAL DEFAULT NULL,
							currency INTEGER DEFAULT NULL,
							location TEXT DEFAULT NULL,
							type INTEGER DEFAULT 0,
							visibility INTEGER DEFAULT 0,
							offeree_id INTEGER DEFAULT NULL);
							
					"""
		);
		pstmt.execute();
		pstmt.close();
	}
	
	
	
}
