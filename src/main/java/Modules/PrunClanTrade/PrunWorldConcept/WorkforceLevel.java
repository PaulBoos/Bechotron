package Modules.PrunClanTrade.PrunWorldConcept;

public enum WorkforceLevel {
	PIONEER("Pioneer"),
	SETTLER("Settler"),
	TECHNICIAN("Technician"),
	ENGINEER("Engineer"),
	SCIENTIST("Scientist");
	
	private final String displayName;
	
	WorkforceLevel(String displayName) {
		this.displayName = displayName;
	}
	
	@Override
	public String toString() {
		return displayName;
	}
}
