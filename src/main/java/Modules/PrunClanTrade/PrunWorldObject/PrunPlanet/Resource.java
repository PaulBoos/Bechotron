package Modules.PrunClanTrade.PrunWorldObject.PrunPlanet;

import Modules.PrunClanTrade.PrunWorldConcept.ResourceState;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;


public class Resource {
	
	@JsonProperty("MaterialId")
	public final String materialid;
	@JsonProperty("ResourceType")
	public final ResourceState resourceType;
	@JsonProperty("Factor")
	public final float factor;
	
	@JsonCreator
	@ConstructorProperties({"MaterialId", "ResourceType", "Factor"})
	public Resource(String materialid, ResourceState resourceType, float factor) {
		this.materialid = materialid;
		this.resourceType = resourceType;
		this.factor = factor;
	}
	
}
