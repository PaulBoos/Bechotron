package Databases;

public class botDB extends dbAccess {
	
	protected botDB() {
		super("jdbc:sqlite:./data/database.db");
	}
	
	
	
}
