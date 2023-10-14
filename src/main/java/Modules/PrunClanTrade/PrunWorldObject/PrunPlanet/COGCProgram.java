package Modules.PrunClanTrade.PrunWorldObject.PrunPlanet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;

public class COGCProgram {
	
	@JsonProperty("ProgramType")
	public final String programType;
	@JsonProperty("StartEpochMs")
	public final long startEpochMs;
	@JsonProperty("EndEpochMs")
	public final long endEpochMs;
	
	@JsonCreator
	@ConstructorProperties({"ProgramType", "StartEpochMs", "EndEpochMs"})
	public COGCProgram(String programType, long startEpochMs, long endEpochMs) {
		this.programType = programType;
		this.startEpochMs = startEpochMs;
		this.endEpochMs = endEpochMs;
	}
}
