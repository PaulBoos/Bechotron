package Modules.PrunClanTrade.PrunWorldConcept;

public enum ProductionCategory {
	AGRICULTURE("Agriculture"),
	CHEMISTRY("Chemistry"),
	CONSTRUCTION("Construction"),
	ELECTRONICS("Electronics"),
	FOOD_INDUSTRIES("Food Industries"),
	FUEL_REFINING("Fuel Refining"),
	MANUFACTURING("Manufacturing"),
	METALLURGY("Metallurgy"),
	RESOURCE_EXTRACTION("Resource Extraction");
	
	public final String displayName;
	
	ProductionCategory(String displayName) {
		this.displayName = displayName;
	}
	
}
