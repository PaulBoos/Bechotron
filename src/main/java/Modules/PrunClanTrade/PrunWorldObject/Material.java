package Modules.PrunClanTrade.PrunWorldObject;

import Modules.PrunClanTrade.PrunWorldConcept.MaterialCategory;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.beans.ConstructorProperties;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Material Class representing a Material from Prosperous Universe
 */
@JsonRootName("Material")
public class Material {
	
	public static final Material[] allMaterials;
	
	static {
		Material[] cache;
		try {
			cache = new JsonMapper().readValue(Path.of("./sources/materials.json").toFile(), Material[].class);
		} catch(IOException e) {
			cache = null;
		}
		allMaterials = cache;
	}
	
	public static Material getMaterial(String tickerNameOrId) {
		for(Material material : allMaterials) {
			if(material.ticker.equalsIgnoreCase(tickerNameOrId) || material.name.equalsIgnoreCase(tickerNameOrId) || material.materialId.equalsIgnoreCase(tickerNameOrId)) {
				return material;
			}
		}
		return null;
	}
	
	@JsonProperty("MaterialId")
	public final String materialId;
	@JsonProperty("CategoryName")
	public final MaterialCategory category;
	@JsonProperty("CategoryId")
	public final String categoryId;
	@JsonProperty("Name")
	public final String name;
	@JsonProperty("Ticker")
	@JsonAlias("MaterialTicker")
	public final String ticker;
	@JsonProperty("Weight")
	public final float weight;
	@JsonProperty("Volume")
	public final float volume;
	@JsonProperty("UserNameSubmitted")
	public final String userNameSubmitted;
	@JsonProperty("Timestamp")
	public final String timestamp;
	
	@JsonCreator
	@ConstructorProperties({"MaterialId", "CategoryName", "CategoryId", "Name", "Ticker", "Weight", "Volume", "UserNameSubmitted", "Timestamp"})
	public Material(String materialId, String category, String categoryId, String name, String ticker, float weight, float volume, String userNameSubmitted, String timestamp) {
		this.materialId = materialId;
		this.category = MaterialCategory.getCategory(category);
		this.categoryId = categoryId;
		this.name = name;
		this.ticker = ticker;
		this.userNameSubmitted = userNameSubmitted;
		this.timestamp = timestamp;
		this.weight = weight;
		this.volume = volume;
	}
	
	@Override
	public String toString() {
		return materialId;
	}
}