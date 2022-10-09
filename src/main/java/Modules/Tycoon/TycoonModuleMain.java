package Modules.Tycoon;

import Utils.Numbers;

import java.sql.SQLException;

public class TycoonModuleMain {
	
	public static void main(String[] args) throws SQLException, Numbers.IntegerOverflowException {
		TycoonDatabase db = new TycoonDatabase();
		Account a = db.getAccount(5, Currency.CREDIT);
		Account.ChangeRequest req = a.makeRequest(-34);
		req.forceFulfillment();
	}
	
}
