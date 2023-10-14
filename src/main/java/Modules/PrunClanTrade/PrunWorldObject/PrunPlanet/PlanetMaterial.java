package Modules.PrunClanTrade.PrunWorldObject.PrunPlanet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;


public class PlanetMaterial {
	
	@JsonProperty("MaterialName")
	public final String name;
	@JsonProperty("MaterialId")
	public final String id;
	@JsonProperty("MaterialTicker")
	public final String ticker;
	@JsonProperty("MaterialCategory")
	public final String category;
	@JsonProperty("MaterialAmount")
	public final int amount;
	@JsonProperty("MaterialWeight")
	public final float weight;
	@JsonProperty("MaterialVolume")
	public final float volume;
	
	@JsonCreator
	@ConstructorProperties({"MaterialName", "MaterialId", "MaterialTicker", "MaterialCategory", "MaterialAmount", "MaterialWeight", "MaterialVolume"})
	public PlanetMaterial(String name, String id, String ticker, String category, int amount, float weight, float volume) {
		this.name = name;
		this.id = id;
		this.ticker = ticker;
		this.category = category;
		this.amount = amount;
		this.weight = weight;
		this.volume = volume;
	}
}
