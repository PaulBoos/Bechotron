package Modules.Tycoon;

import Utils.Numbers;

import java.sql.SQLException;

public class TycoonModuleMain {
	
	public static void main(String[] args) throws SQLException, Numbers.IntegerOverflowException {
		TycoonDatabase db = new TycoonDatabase();
		Facility f = db.getFacility(1);
		f.setLandlord(new Landlord(1L));
		f.getLandlord().registerFacility(f);
		System.out.println(f);
	}
	
}
