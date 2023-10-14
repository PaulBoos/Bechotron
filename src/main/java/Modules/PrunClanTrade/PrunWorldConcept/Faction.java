package Modules.PrunClanTrade.PrunWorldConcept;

import org.jetbrains.annotations.Nullable;

public enum Faction {
	AI("Antares Initiative", "AIC"),
	CI("Castillo-Ito Mercantile", "CIS"),
	EC("Exodus Council", "ECD"),
	IC("Insitor Cooperative", "ICA"),
	NC("NEO Charter Exploration", "NCC");
	
	public final String displayName;
	public final String currency;
	
	Faction(String displayName, String currency) {
		this.displayName = displayName;
		this.currency = currency;
	}
	
	@Nullable
	public static Faction getFaction(String code) {
		try {
			return Faction.valueOf(code.toUpperCase());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
}