package Modules.PrunClanTrade.PrunWorldConcept;

import org.jetbrains.annotations.Nullable;

public enum Currency {
	AIC("Martian Coin", "AI"),
	CIS("Sol", "CI"),
	ECD("Exodus Council Drawing Rights", "EC"),
	ICA("Austral", "IC"),
	NCC("NCE Coupons", "NC");
	
	public final String displayName;
	public final String faction;
	
	Currency(String displayName, String faction) {
		this.displayName = displayName;
		this.faction = faction;
	}
	
	@Nullable
	public static Currency getCurrency(String code) {
		try {
			return Currency.valueOf(code.toUpperCase());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
}
